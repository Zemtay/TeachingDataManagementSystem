//package com.ruoyi.system.service.impl;
//
//import com.ruoyi.system.service.IAiAssistantService;
//import com.theokanning.openai.completion.chat.*;
//import com.theokanning.openai.service.OpenAiService;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//// Java 基础类
//
//// OkHttp
//
//// Retrofit
//
//// Jackson
//
//
//// 希望在sql文件更新时更新prompt来引导分析表格
////public class ReadFileExample {
////    public static void main(String[] args) {
////        try {
////            String content = new String(Files.readAllBytes(Paths.get("path/to/your/file.txt")));
////            System.out.println(content);
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////    }
////}
//
//@Service
//public class AiAssistantServiceImpl_draft implements IAiAssistantService{
//    @Value("${openai.api-key}")
//    private String API_KEY;
//
//    @Value("${openai.model-name}")
//    private String MODEL_NAME;
//
//    @Value("${openai.base-url}")
//    private String BASE_URL;
//
//    OpenAiService service = new OpenAiService(API_KEY, BASE_URL);  // , Duration.ofSeconds(150)
//
//    String coursetable_title = "**course_info(课程信息表)**: 该表用于存储课程的基本信息。每条记录对应一门课程，包含课程的id(标识)、" +
//            "course_name(名称)、course_num(编号)、course_semester(所属学期)、" +
//            "creator_id(创建人信息)、class_count(该课程下开设的班级数量)、total_students(选修该课程的学生总人数)";
//    String coursetable_info = coursetable_title + "详细信息如下：\n" +
//            "| 字段名               | 类型           | 说明                   |\n" +
//            "| ----------------- | ------------ | -------------------- |\n" +
//            "| `id`              | BIGINT       | 主键，自增编号              |\n" +
//            "| `course_name`     | VARCHAR(100) | 课程名称（如“高等数学”）        |\n" +
//            "| `course_num`      | VARCHAR(50)  | 课程编号（唯一标识课程的代码）      |\n" +
//            "| `course_semester` | VARCHAR(50)  | 课程开设的学期（如“2025春季学期”） |\n" +
//            "| `creator_id`      | VARCHAR(50)  | 建课人工号或学号（用于标识创建者）    |\n" +
//            "| `class_count`     | INT          | 该课程下开设的班级数量          |\n" +
//            "| `total_students`  | INT          | 选修该课程的学生总人数          |\n";
//
//    String classtable_title = "**class_info(班级信息表)**: 该表记录每门课程下开设的各个教学班信息。每条记录代表一个教学班，" +
//            "包含所属课程信息、班级标识及学生人数等，" +
//            "包括字段 id(主键)、course_name(课程名称)、course_id(课程ID)、course_num(课程编号)、" +
//            "course_type(课程类型)、total_students(班级学生总人数)、class_id(班级唯一标识)、EAclass_id(教务系统课班级ID)。";
//    String classtable_info = classtable_title + "详细信息如下：\n" +
//            "| 字段名           | 类型           | 说明                     |\n" +
//            "| ---------------- | -------------- | ------------------------ |\n" +
//            "| `id`             | BIGINT         | 主键，自增编号               |\n" +
//            "| `course_name`    | VARCHAR(100)   | 课程名称（与 course_info 表关联）|\n" +
//            "| `course_id`      | VARCHAR(50)    | 课程ID（外键，对应 course_info.id）|\n" +
//            "| `course_num`     | VARCHAR(50)    | 课程编号                   |\n" +
//            "| `course_type`    | VARCHAR(50)    | 课程类型（如“必修”、“选修”）      |\n" +
//            "| `total_students` | INT            | 班级学生总人数               |\n" +
//            "| `class_id`       | VARCHAR(50)    | 班级唯一标识（教学系统内部标识）    |\n" +
//            "| `EAclass_id`     | VARCHAR(50)    | 教务系统课班级ID（外部同步用）     |\n";
//
//
//    String studenttable_title = "**student(学生信息表)**: 该表保存学生总体学习参与度与行为统计数据。每条记录代表一个学生（学号唯一），" +
//            "记录其学习进度、互动行为及任务完成情况，" +
//            "包括字段 student_id(学号/工号)、course_count(课程数量)、tasks_completed(任务点完成数)、" +
//            "video_tasks_completed(视频任务点完成数)、chapter_tests_completed(章节测验完成数)、homework_completed(作业完成数)、" +
//            "exams_completed(考试完成数)、chapter_study_count(章节学习次数)、total_discussions(讨论总数)、total_posts(发帖总数)、" +
//            "total_replies(回帖总数)、course_points(课程积分)、student_votes(问卷参与数)、selected_count(选人被选中次数)、" +
//            "student_answers(抢答参与数)、student_ratings(评分参与数)、student_tasks(任务参与数)。";
//    String studenttable_info = studenttable_title + "详细信息如下：\n" +
//            "| 字段名                   | 类型            | 说明                       |\n" +
//            "| ------------------------ | --------------- | -------------------------- |\n" +
//            "| `student_id`             | VARCHAR(50)     | 主键，学号或工号，用于唯一标识学生   |\n" +
//            "| `course_count`           | INT             | 该学生参与的课程总数             |\n" +
//            "| `tasks_completed`        | INT             | 已完成的任务点数量（总数）         |\n" +
//            "| `video_tasks_completed`  | INT             | 完成的视频任务点数量             |\n" +
//            "| `chapter_tests_completed`| INT             | 完成的章节测验数量               |\n" +
//            "| `homework_completed`     | INT             | 完成的作业数量                 |\n" +
//            "| `exams_completed`        | INT             | 完成的考试数量                 |\n" +
//            "| `chapter_study_count`    | INT             | 学习章节的次数                 |\n" +
//            "| `total_discussions`      | INT             | 讨论区参与次数                 |\n" +
//            "| `total_posts`            | INT             | 发帖总数                     |\n" +
//            "| `total_replies`          | INT             | 回帖总数                     |\n" +
//            "| `course_points`          | DECIMAL(10,2)   | 课程积分                     |\n" +
//            "| `student_votes`          | INT             | 学生问卷参与数                 |\n" +
//            "| `selected_count`         | INT             | 选人被选中次数                 |\n" +
//            "| `student_answers`        | INT             | 学生抢答参与数                 |\n" +
//            "| `student_ratings`        | INT             | 学生评分参与数                 |\n" +
//            "| `student_tasks`          | INT             | 学生任务参与数                 |\n";
//
//    String courseactivity_title = "**course_activity(课程活动表)**: 该表保存学生在特定课程/教学班中的活动表现与成绩统计。" +
//            "每条记录是“学生 × 班级”的唯一组合（student_id + class_id 唯一），用于追踪学生在该班级中的学习行为和综合成绩。" +
//            "包括字段 id(主键)、student_id(学号/工号)、course_name(课程名称)、class_id(教学班ID)、" +
//            "total_discussions(讨论总数)、total_posts(发帖总数)、total_replies(回帖总数)、course_points(课程积分)、" +
//            "student_votes(问卷参与数)、student_vote_rate(问卷参与率)、selected_count(选人被选中次数)、selected_rate(选人被选中率)、" +
//            "student_answers(抢答参与数)、student_answer_rate(抢答参与率)、student_ratings(评分参与数)、student_rating_rate(评分参与率)、" +
//            "student_tasks(任务参与数)、student_task_rate(任务参与率)、final_score(综合成绩)、certificate_status(证书发放状态)。";
//
//    String courseactivity_info = courseactivity_title + "详细信息如下：\n" +
//            "| 字段名                  | 类型           | 说明                         |\n" +
//            "| ----------------------- | -------------- | ---------------------------- |\n" +
//            "| `id`                    | BIGINT         | 主键，自增编号                   |\n" +
//            "| `student_id`            | VARCHAR(50)    | 学号/工号，对应 student 表主键     |\n" +
//            "| `course_name`           | VARCHAR(100)   | 课程名称                       |\n" +
//            "| `class_id`              | VARCHAR(50)    | 教学班ID                      |\n" +
//            "| `total_discussions`     | INT            | 讨论总数                       |\n" +
//            "| `total_posts`           | INT            | 发帖总数                       |\n" +
//            "| `total_replies`         | INT            | 回帖总数                       |\n" +
//            "| `course_points`         | DECIMAL(10,2)  | 课程积分                       |\n" +
//            "| `student_votes`         | INT            | 学生投票问卷参与数               |\n" +
//            "| `student_vote_rate`     | DECIMAL(5,2)   | 学生投票问卷参与率               |\n" +
//            "| `selected_count`        | INT            | 选人被选中次数                   |\n" +
//            "| `selected_rate`         | DECIMAL(5,2)   | 选人被选中次率                   |\n" +
//            "| `student_answers`       | INT            | 学生抢答参与数                   |\n" +
//            "| `student_answer_rate`   | DECIMAL(5,2)   | 学生抢答参与率                   |\n" +
//            "| `student_ratings`       | INT            | 学生评分参与数                   |\n" +
//            "| `student_rating_rate`   | DECIMAL(5,2)   | 学生评分参与率                   |\n" +
//            "| `student_tasks`         | INT            | 学生任务参与数                   |\n" +
//            "| `student_task_rate`     | DECIMAL(5,2)   | 学生任务参与率                   |\n" +
//            "| `final_score`           | DECIMAL(10,2)  | 综合成绩                       |\n" +
//            "| `certificate_status`    | VARCHAR(10)    | 证书发放状态（是/否）             |\n";
//
//
//    //    private static final String API_KEY = System.getenv("OPENAI_API_KEY");
//    public String queryAPI(String sysprompt, ChatMessage userMessage){
//        List<ChatMessage> messages = new ArrayList<>();
//        ChatMessage systemMessage = new SystemMessage(sysprompt);
//        messages.add(systemMessage);
//        messages.add(userMessage);
//
//        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
//                .model(MODEL_NAME)
//                .messages(messages)
//                .n(1)
//                .maxTokens(500)
//                .build();
//        ChatCompletionResult chatCompletion = service.createChatCompletion(chatCompletionRequest);
//        String answer = chatCompletion.getChoices().get(0).getMessage().getContent();
//        return answer;
//    }
//
//    public Map<String, Object> Stage1(String input){
//        String sysprompt = "你是一位非常聪明、有用的智能助手。你将从事教学数据分析工作。\n";
//        ChatMessage userMessage = new UserMessage(
//                "以下是聊天历史以及用户的输入：" + input + "\n\n请结合聊天历史，识别并总结用户的对话意图。如果用户的问题有关教育教学数据，并且与course_info(课程信息表)、student(学生信息表)、" +
//                        "course_activity(课程活动表)中的任一表格有关，请在回复中严格包含'我需要查看相关数据'," +
//                        "否则，正常输出与用户的对话结果。\n"
//        );
//
//        Map<String, Object> map = new HashMap<>();
//        String reply = queryAPI(sysprompt, input);
//        Boolean flag = false;
//        if (reply.contains("我需要查看相关数据")) {
//            flag = true;
//        }
//        map.put("flag", flag);
//        map.put("reply", reply);
//        return map;
//    }
//
//    public Map<String, Object> Stage2(String input){
//        Map<String, Object> map = new HashMap<>();
//        String sysprompt = "你是一位非常聪明、有用的智能助手。你将从事教学数据分析工作。\n" +
//                "下面对你提供有关教学分析的四张表（course_info、class_info、student、course_activity）结构化描述，" +
//                "每张表我都写成一段清晰的说明，便于你后续抽取字段名、做查询、做接口映射。\n随后请用括号列出你想要进一步分析的表格，用','分割，" +
//                "如(8,9)表示你想要进一步分析表格8和9。"
//                +"表格1："+coursetable_title
//                +"表格2："+classtable_title
//                +"表格3："+studenttable_title
//                +"表格4："+courseactivity_title
//                +"\n你的选择是：(请从1、2、3、4中做出选择)";;
//        String reply = queryAPI(sysprompt, input);
//        Pattern pattern = Pattern.compile("\\((.*?)\\)");
//        Matcher matcher = pattern.matcher(reply);
//
//        boolean flag = false;
//        if (!matcher.find()) {
//            map.put("flag",flag);
//            map.put("reply",reply);
//        }else{
//            String content = matcher.group(1);
//            List<String> tables = new ArrayList<>();
//            for (int i = 1; i < 4; i++) {
//                String key = String.valueOf(i);   // 转成字符串 "0" "1" "2" "3"
//                if (reply.contains(key)) {
//                    tables.add(key);
//                }
//            }
//
//            map.put("flag", flag);
//            map.put("choices", tables);
//        }
//        return map;
//    }
//
//    public String Stage3(List<String> choices, String input){
//        String tableDetail = "";
//        Map<String,String> tables = new HashMap<>();
//        tables.put("1", coursetable_title);
//        tables.put("2", classtable_title);
//        tables.put("3", studenttable_title);
//        tables.put("4", courseactivity_title);
//        for (int i = 1; i <= 4; i++) {
//            String key = String.valueOf(i);
//            if (choices.contains(key)) {
//                tableDetail += tables.get(key) + "\n\n";
//            }
//        }
//        String sysprompt = "你是一位非常聪明、有用的智能助手。你将从事教学数据分析工作。\n" +
//                "根据用户的问题，你决定选择以下表格进行分析。请根据表格的详细信息写出查询的SQL语句，以便执行查询操作。\n" +
//                "请把每条可以执行的sql语句用括号括起来，如(select * from coursetable_title;)"
//                + tableDetail;
//        String reply = queryAPI(sysprompt, input);
//        return reply;
//    }
//
//    public String Stage4(String reply){
//        Pattern pattern = Pattern.compile("\\((.*?)\\)");
//        Matcher matcher = pattern.matcher(reply);
//
//        boolean flag = false;
//        if (matcher.find()) {
//            flag = true;
//            Stage5(matcher);
//        }else{
//            Stage3();
//        }
//
//        retur flag;
//    }
//
//    @Override
//    public String getAiReply(String input) {
//        System.out.println("========\n");
//        System.out.println(input);
////        OpenAiService service = new OpenAiService(API_KEY, Duration.ofSeconds(60));
////        OpenAiService service = new CustomOpenAiService(API_KEY, Duration.ofSeconds(150));
//
//        List<ChatMessage> messages = new ArrayList<>();
//
//
//        ChatMessage systemMessage = new SystemMessage("");
//
//        messages.add(systemMessage);
//        ChatMessage userMessage = new UserMessage(input);
//        messages.add(userMessage);
//
//        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
//                .model(MODEL_NAME)
//                .messages(messages)
//                .n(1)
//                .maxTokens(500)
//                .build();
//        ChatCompletionResult chatCompletion = service.createChatCompletion(chatCompletionRequest);
//        String answer = chatCompletion.getChoices().get(0).getMessage().getContent();
//
//        String Tablerelated =
//        System.out.println("-------" + answer);
//
//        return answer;
////        return "everything is OK";
//    }
//}
