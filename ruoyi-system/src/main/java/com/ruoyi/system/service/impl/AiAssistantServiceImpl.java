package com.ruoyi.system.service.impl;

import com.alibaba.fastjson2.TypeReference;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.IAiAssistantService;
import com.ruoyi.system.service.IDynamicQueryService;
import com.ruoyi.system.mapper.AiChatHistoryMapper;
import com.ruoyi.system.domain.AiChatHistory;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.OpenAiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;  // 使用通配符导入所有工具类
import javax.annotation.PostConstruct;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;


@Service
public class AiAssistantServiceImpl implements IAiAssistantService {

    @Value("${openai.api-key}")
    private String API_KEY;

    @Value("${openai.model-name}")
    private String MODEL_NAME;

    @Value("${openai.base-url}")
    private String BASE_URL;

    private OpenAiService service;

    @Autowired
    private IDynamicQueryService dynamicQueryService;

    @Autowired
    private AiChatHistoryMapper chatHistoryMapper;

    @Override
    public List<Map<String, String>> getChatHistory(Long userId) {
        AiChatHistory record = chatHistoryMapper.selectByUserId(userId);
        if (record == null || record.getMessages() == null) {
            return Collections.emptyList();
        }

        // 方案1：推荐 - 使用 TypeReference 保留泛型信息
        return JSON.parseObject(record.getMessages(),
                new TypeReference<List<Map<String, String>>>() {});
    }

    @Override
    public void saveChatHistory(Long userId, String messages) {
        AiChatHistory record = new AiChatHistory();
        record.setUserId(userId);
        record.setMessages(messages);
        chatHistoryMapper.insertOrUpdate(record);
    }

    @Override
    public void clearChatHistory(Long userId) {
        chatHistoryMapper.deleteByUserId(userId);
    }

    @PostConstruct
    public void init() {
        this.service = new OpenAiService(API_KEY, BASE_URL);
    }

    // ========================== 公共方法 ========================
    public String callAI(String systemPrompt, String userInput) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new SystemMessage(systemPrompt));
        messages.add(new UserMessage(userInput));

        ChatCompletionRequest req = ChatCompletionRequest.builder()
                .model(MODEL_NAME)
                .messages(messages)
                .maxTokens(1000)
                .build();

        return service.createChatCompletion(req)
                .getChoices().get(0).getMessage().getContent();
    }

    // ========================== Stage 1 判断是否需要查看教学数据库 ========================
    public Map<String, Object> Stage1(String input) {

        String sys = "你是一位教学数据库管理助手，请根据对话历史，总结、推断出用户当前完整的意图。如果用户希望修改数据库，请输出'！修改数据库警告！'。";
        String reply = callAI(sys, input);

        Map<String, Object> map = new HashMap<>();
        if (reply.contains("！修改数据库警告！")){
            System.out.println("morethanQuery：" + reply);
            map.put("morethanQuery", true);
            return map;
        }

        sys = "你是一位教学数据分析智能助手。你需要判断用户是否正在询问与教学数据相关的问题。" +
                "请根据对话历史，总结、推断出用户当前完整的意图。" +
                "如果你认为需要查询教学数据库（库中包含且仅包含课程活动表、学生信息表、班级信息表、课程信息表），请务必包含：'我需要查看相关数据'，不得添字、少字。";
        reply = callAI(sys, input);
        System.out.println("reply1:" + reply);

        map.put("reply", reply);
        map.put("needQuery", reply.contains("需要查看"));
        map.put("needQuery", !reply.contains("不需要查看"));
        map.put("morethanQuery", false);

        return map;
    }

    // ========================== Stage 2 给出表的基本信息，选择表并写出sql查询语句 ========================
    public Map<String, Object> Stage2(String input) {
        String courseactivity_title = "**course_activity(课程活动表)**: 该表保存学生在特定课程/教学班中的活动表现与成绩统计。" +
                "每条记录是“学生 × 班级”的唯一组合（student_id + class_id 唯一），用于追踪学生在该班级中的学习行为和综合成绩。" +
                "包括字段 id(主键)、student_id(学号/工号)、course_name(课程名称)、class_id(教学班ID)、" +
                "total_discussions(讨论总数)、total_posts(发帖总数)、total_replies(回帖总数)、course_points(课程积分)、" +
                "student_votes(问卷参与数)、student_vote_rate(问卷参与率)、selected_count(选人被选中次数)、selected_rate(选人被选中率)、" +
                "student_answers(抢答参与数)、student_answer_rate(抢答参与率)、student_ratings(评分参与数)、student_rating_rate(评分参与率)、" +
                "student_tasks(任务参与数)、student_task_rate(任务参与率)、final_score(综合成绩)、certificate_status(证书发放状态)。";
        String studenttable_title = "**student(学生信息表)**: 该表保存学生总体学习参与度与行为统计数据。每条记录代表一个学生（学号唯一），" +
                "记录其学习进度、互动行为及任务完成情况，" +
                "包括字段 student_id(学号/工号)、course_count(课程数量)、tasks_completed(任务点完成数)、" +
                "video_tasks_completed(视频任务点完成数)、chapter_tests_completed(章节测验完成数)、homework_completed(作业完成数)、" +
                "exams_completed(考试完成数)、chapter_study_count(章节学习次数)、total_discussions(讨论总数)、total_posts(发帖总数)、" +
                "total_replies(回帖总数)、course_points(课程积分)、student_votes(问卷参与数)、selected_count(选人被选中次数)、" +
                "student_answers(抢答参与数)、student_ratings(评分参与数)、student_tasks(任务参与数)。";
        String classtable_title = "**class_info(班级信息表)**: 该表记录每门课程下开设的各个教学班信息。每条记录代表一个教学班，" +
                "包含所属课程信息、班级标识及学生人数等，" +
                "包括字段 id(主键)、course_name(课程名称)、course_id(课程ID)、course_num(课程编号)、" +
                "course_type(课程类型)、total_students(班级学生总人数)、class_id(班级唯一标识)、EAclass_id(教务系统课班级ID)。";
        String coursetable_title = "**course_info(课程信息表)**: 该表用于存储课程的基本信息。每条记录对应一门课程，包含课程的id(标识)、" +
                "course_name(名称)、course_num(编号)、course_semester(所属学期)、" +
                "creator_id(创建人信息)、class_count(该课程下开设的班级数量)、total_students(选修该课程的学生总人数)";

        String sys = "初步分析之后，你决定查询教学信息相关的数据库来回答用户的问题。此时，作为一名数据库专家。" +
                "请根据用户问题和选择的表，编写 SQL 查询语句。严禁查询不存在的表单和列（注意表名和列名的对应关系）！严禁修改数据库！\n" +
                     "每条 SQL 请用大括号包裹，如：{SELECT * FROM class_info LIMIT 10;}";

        input = "以下是可查询的数据库表及其字段信息：\n" + courseactivity_title + "\n\n" + studenttable_title + "\n\n" +
                classtable_title + "\n\n" +  coursetable_title +
                "你对用户意图的总结和分析结果是：\n" + input;
        String reply = callAI(sys, input);
//        System.out.println("reply in stage2 is: "+reply);
        reply = reply.replaceAll("\\r?\\n", " ");
        System.out.println("reply in stage2 after clearing \\n is: "+reply);

        Pattern p = Pattern.compile("\\{(.*?)\\}");
        Matcher m = p.matcher(reply);

        Map<String, Object> map = new HashMap<>();
        List<String> result = new ArrayList<>();
        while (m.find()) {
            result.add(m.group(1));  // 获取括号内内容
        }
        if (result.isEmpty()) {
            map.put("getSQL", false);
        }else{
            map.put("SQLquery", result);
            map.put("getSQL", true);
        }

        return map;
    }

    // 调用查询语句
    public List<Map<String, Object>> sqlQuery(Map<String, Object> params) {
            String tableName = (String) params.get("tableName");
            Map<String, Object> conditions = (Map<String, Object>) params.get("conditions");
            List<Map<String, Object>> result = dynamicQueryService.executeDynamicQuery(tableName, conditions);
            System.out.printf("查询表 %s 返回 %d 条记录%n", tableName, result.size());
            System.out.printf("查询结果: {}", JSON.toJSONString(result));

            return result;
    }

    public List<Map<String, Object>> sqlQuery_free(String sql) {
        String sql_pro = sql.replace('{', ' ');
        List<Map<String, Object>> result = dynamicQueryService.executeQuery(sql_pro);
        System.out.printf("查询结果: {}", JSON.toJSONString(result));

        return result;
    }

    // 组装成实际查询的sql语句
    public String buildSqlFromMap(Map<String, Object> element) {
        // 1. 提取表名
        String tableName = (String) element.get("tableName");
        if (tableName == null) {
            throw new IllegalArgumentException("缺少表名");
        }

        // 2. 提取条件
//        Map<String, Object> conditions = new HashMap<>(element);
//        conditions.remove("tableName"); // 移除表名，剩余都是查询条件
        Map<String, Object> conditions = (Map<String, Object>) element.get("conditions");
        if (conditions == null) {
            throw new IllegalArgumentException("缺少查询条件");
        }

        // 3. 组装 SQL
        StringBuilder sql = new StringBuilder("SELECT * FROM ").append(tableName);

        if (!conditions.isEmpty()) {
            sql.append(" WHERE ");

            // 遍历条件
            Iterator<Map.Entry<String, Object>> iterator = conditions.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                String column = entry.getKey();
                Object value = entry.getValue();

                // 处理值类型（区分字符串和数字）
                String valueStr;
                if (value instanceof String) {
                    valueStr = "'" + value + "'";  // 字符串加引号
                } else {
                    valueStr = value.toString();   // 数字直接转字符串
                }

                sql.append(column).append(" = ").append(valueStr);

                // 如果不是最后一个条件，添加 AND
                if (iterator.hasNext()) {
                    sql.append(" AND ");
                }
            }
        }

        return sql.toString();
    }

    // ========================== Stage 3: 执行 SQL ========================
    public String Stage3(List<String> sql) {
        String reply = "";
        String systemPrompt = "你是一个非常优秀的数据分析助手。";

        for (String s : sql) {
            String s_lower = s.toLowerCase(Locale.ROOT);          // 统一小写
            if (s_lower.contains("update") || s_lower.contains("insert")
                    || s_lower.contains("delete") || s_lower.contains("drop")) {
                continue;
            }

            String prompt = "- 希望查询的sql语句是: " + s;
            List<Map<String, Object>> results = sqlQuery_free(s);
            String prettyJson = JSON.toJSONString(results, JSONWriter.Feature.WriteNulls, JSONWriter.Feature.PrettyFormat);
            prompt += "，得到的结果是：" + prettyJson;
            System.out.println("\n单条查询语句的结果是: "+prompt);
            reply += prompt;
        }

        if(reply.length() == 0){
            return "抱歉！数据查询失败！请重试！";
        }
        return reply;
    }
//    public String Stage3(List<String> sql) {
//        String reply = "";
//        String systemPrompt = "你是一个非常优秀的数据分析助手。";
//        for (String s : sql) {
////            s = "select total_students from class_info where course_name = 数学";
//            String s_pro = callAI(systemPrompt, "请根据以下sql语句，提取出要查找的表名和条件。注意，每条查询语句中，你只能查询一个表格，所以你可能需要改写一些语句。结果请用大括号包裹、逗号分隔，例如，SELECT * FROM class_info where class_id = 1 的返回结果应该是{class_info, class_id = 1}。你要分析的sql语句是："+s);
//
//            Pattern pattern = Pattern.compile("\\{(.*?)\\}");
//            Matcher matcher = pattern.matcher(s_pro);
//            String prompt = "- 希望查询的sql语句是: " + s;
//            while (matcher.find()) {
//                String item= matcher.group(1); // group(1) 获取括号内的内容
//                System.out.println("\n抽离出的查询对象是: "+item);
//                if(!item.contains("=") || !item.contains(",")){
//                    prompt += "抽取实体对象失败";
//                    System.out.println("\n抽取实体对象失败");
//                    continue;
//                }
//                String[] parts = item.split(",");
//                Map<String, Object> element = new HashMap<>();
//                element.put("tableName", parts[0].trim());
//                Map<String, Object> conditions = new HashMap<>();
//                for (int i = 1; i < parts.length; i++) {
//                    String part = parts[i];
//                    String[] key = part.split("=");
//                    conditions.put(key[0].trim(), key[1].trim());
//                }
//                element.put("conditions", conditions);
//                prompt += "\n    实际执行的查询语句: "+buildSqlFromMap(element);
//                System.out.println("\n实际执行的查询语句: "+buildSqlFromMap(element));
//
//                List<Map<String, Object>> results = sqlQuery(element);
//                String prettyJson = JSON.toJSONString(results, JSONWriter.Feature.WriteNulls, JSONWriter.Feature.PrettyFormat);
//                prompt += "，得到的结果是：" + prettyJson;
//                System.out.println("\n单条查询语句的结果是: "+prompt);
//            }
//
//            String answer = callAI(systemPrompt,  "请根据查询结果，写出'希望查询的sql语句'的查询结果，答案用大括号{}包裹"+prompt);
//            Matcher sql_ans = pattern.matcher(answer);
//
//            if (sql_ans.find()){
//                reply += s + "的查询结果是：" + sql_ans.group(1) +"\n\n";
//            }
//        }
//
//        if(reply.length() == 0){
//            return "抱歉！数据查询失败！请重试！";
//        }
//        return reply;
//    }

    // =========================== 完整对话入口 ============================
    @Override
    public String getAiReply(String userInput) {

        // Stage1：判断是否需要查数据库
        Map<String, Object> s1 = Stage1(userInput);

        if ((boolean) s1.get("morethanQuery")) {
            String systemPrompt = "你是一位非常聪明、有用的数据库智能管理助手。如果用户有修改数据库的意图，请表示拒绝；" +
                    "否则，请与用户进行连贯的对话。";
            System.out.println("======= morethanquery");
            return callAI(systemPrompt, userInput);
        }

        if (!(boolean) s1.get("needQuery")) {
            String systemPrompt = "你是一位非常聪明、有用的智能助手。请与用户进行连贯的对话。";
            System.out.println("======= no need query");
            return callAI(systemPrompt, userInput);
        }

        // Stage2：提取表
        String reply1 = (String)s1.get("reply");
        System.out.println("reply1:"+reply1);
        Map<String, Object> s2 = Stage2(reply1);
        List<String> reply2 = (List<String>)s2.get("SQLquery");
//        System.out.println("reply2:"+reply2);

        if (!(boolean) s2.get("getSQL")) {
            return "我不能决定要查询哪些表单、无法生成查询语句，请问您能给出更详细的问题吗？";
        }

        String s3 = Stage3(reply2);
//        System.out.println("reply3:"+s3.toString());
        String systemPrompt = "根据用户的问题以及历史聊天记录，你写出了对应的sql查询语句，得到了查询数据库后得到结果。" +
                "请根据聊天历史和查询结果生成对用户的回复，使对话流畅自然。对话历史是'基于用户的问题以及历史聊天记录，你的总结与分析是：'前的内容。" +
                "注意，当数据库查询结果返回为空时，你应该表明未查到相关结果，而非捏造数据。严禁编造数据！！！幻觉会被强烈惩罚！";
        return callAI(systemPrompt, userInput + "基于用户的问题以及历史聊天记录，你的总结与分析是：" +
                reply1 + "你生成的sql查询语句以及相应的查询结果是：" + s3);
    }
}
