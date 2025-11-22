package com.ruoyi.web.controller.dashboard;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.domain.server.Sys;
import com.ruoyi.system.domain.CourseActivity;
import com.ruoyi.system.service.IAiAssistantService;
import com.ruoyi.system.service.IStudentService;
import com.ruoyi.system.service.ICourseActivityService;
import com.ruoyi.system.service.ICourseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import com.ruoyi.system.domain.CourseInfo;
import java.util.Comparator;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboard/studyinfo")
public class StudyInfoController {
    @Autowired
//    private IStudyInfoService StudyInfoService;
    private ICourseInfoService courseInfoService;

    @Autowired
    private ICourseActivityService courseActivityService;

    @Autowired
    private  IAiAssistantService aiAssistantService;

    @Autowired
    private IStudentService studentService;

    @PostMapping("/getAiReport")
    public AjaxResult getAiReport(@RequestBody Map<String, String> map) {
        String msg = map.get("msg");
        String sys_prompt = "你是一个教育教学智能助手，请根据以下事实提出建议。";
        String reply = aiAssistantService.callAI(sys_prompt, msg);
        return AjaxResult.success(reply);
    }

    @GetMapping("/getTeacherStudyinfo")
    public AjaxResult getTeacherStudyinfo() {
        String teacherUserNo = SecurityUtils.getUsername();
        CourseInfo query = new CourseInfo();
        query.setCreatorId(teacherUserNo);
        List<CourseInfo> courseList = courseInfoService.selectCourseInfoList(query);

        CourseActivity courseActivity = new CourseActivity();
//        System.out.println(courseList);

        Map<String, Object> info_list = new HashMap<>();
//        info_list.put("courseList", courseList);
        int sum = 0;
        int students = 0;
        String msg1;
        String msg2 = "在学生的课程学习情况方面，";
//        String msg2_AI = "在学生的课程学习情况方面，";

        List<Map<String, Object>> detail = new ArrayList<>();
        String class_detail = "";

        for (CourseInfo courseInfo : courseList) {
            sum += courseInfo.getClassCount();
            String performance_detail = "";
            String msg2_tmp="";
            students += courseInfo.getTotalStudents();
            Map<String, Object> course = new HashMap<>();
            course.put("name", courseInfo.getCourseName());
            course.put("classCount", courseInfo.getClassCount());
            course.put("totalStudents", courseInfo.getTotalStudents());
            course.put("avgStudent", courseInfo.getTotalStudents() / courseInfo.getClassCount());

            class_detail += String.format("%s有%d名学生，平均每个班级有%d名学生。",course.get("name"), course.get("totalStudents"), course.get("avgStudent"));
            msg2_tmp += String.format("\n%s课的学生参与情况具体如下：\n", course.get("name"));
            List<CourseActivity> courseaclist = courseActivityService.selectCourseActivityList(courseActivity);
            // 对每个班级进行处理
//            System.out.println(courseaclist);
            List<Map<String, Object>> bottom5SelectedStudents = courseaclist.stream()
                    .sorted(Comparator.comparingLong(CourseActivity::getSelectedCount))
                    .limit(5)
                    .map(c -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("studentId", c.getStudentId());
                        map.put("selectedCount", c.getSelectedCount());
                        return map;
                    })
                    .collect(Collectors.toList());
            course.put("bottom5SelectedStudents", bottom5SelectedStudents);
            performance_detail += "\n- 被选中次数最少的学生名单如下：\n";
            for(Map<String, Object> map:bottom5SelectedStudents){
                performance_detail += String.format("学号%s，被选中次数%d；", map.get("studentId"), map.get("selectedCount"));
            }

            List<Map<String, Object>> bottom5studentAnswers = courseaclist.stream()
                    .sorted(Comparator.comparingLong(CourseActivity::getStudentAnswers))
                    .limit(5)
                    .map(c -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("studentId", c.getStudentId());
                        map.put("studentAnswers", c.getStudentAnswers());
                        return map;
                    })
                    .collect(Collectors.toList());
            course.put("bottom5studentAnswers", bottom5studentAnswers);
            performance_detail += "\n- 课程问题回复次数最少的学生名单如下：\n";
            for(Map<String, Object> map:bottom5studentAnswers){
                performance_detail += String.format("学号%s，问题回答次数%d；", map.get("studentId"), map.get("studentAnswers"));
            }

            List<Map<String, Object>> bottom5studentTasks= courseaclist.stream()
                    .sorted(Comparator.comparingLong(CourseActivity::getStudentTasks))
                    .limit(5)
                    .map(c -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("studentId", c.getStudentId());
                        map.put("StudentTasks", c.getStudentTasks());
                        return map;
                    })
                    .collect(Collectors.toList());
            course.put("bottom5studentTasks", bottom5studentTasks);
            performance_detail += "\n- 课程任务完成次数最少的学生名单如下：\n";
            for(Map<String, Object> map:bottom5studentTasks){
                performance_detail += String.format("学号%s，任务完成次数%d；", map.get("studentId"), map.get("StudentTasks"));
            }

            List<Map<String, Object>> bottom5FinalScore = courseaclist.stream()
                    // 按 finalScore 升序排序
                    .sorted(Comparator.comparing(CourseActivity::getFinalScore))
                    .limit(5) // 取前 5 个
                    .map(c -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("studentId", c.getStudentId());
                        map.put("finalScore", c.getFinalScore()); // 对应 finalScore
                        return map;
                    })
                    .collect(Collectors.toList());
            course.put("bottom5FinalScore", bottom5FinalScore);
            performance_detail += "\n- 当前课程总分最少的学生名单如下：\n";
            for(Map<String, Object> map:bottom5FinalScore){
                performance_detail += String.format("学号%s，任务完成次数%s；", map.get("studentId"), map.get("finalScore"));
            }

            double avgTasks = courseaclist.stream()
                    .filter(c -> c.getStudentTaskRate() != null) // 避免空指针
                    .mapToDouble(c -> c.getStudentTaskRate().doubleValue()) // BigDecimal 转 double
                    .average()
                    .orElse(0); // 如果列表为空返回 0
            course.put("avgTasks", avgTasks);
            msg2_tmp += String.format("学生平均任务完成率为：%f；", avgTasks);

            double avgAnswer = courseaclist.stream()
                    .filter(c -> c.getStudentAnswerRate() != null) // 避免空指针
                    .mapToDouble(c -> c.getStudentAnswerRate().doubleValue()) // BigDecimal 转 double
                    .average()
                    .orElse(0); // 如果列表为空返回 0
            course.put("avgAnswers", avgAnswer);
            msg2_tmp += String.format("学生平均问题回答率为：%f；", avgAnswer);

            double avgRating = courseaclist.stream()
                    .filter(c -> c.getStudentRatingRate() != null) // 避免空指针
                    .mapToDouble(c -> c.getStudentRatingRate().doubleValue()) // BigDecimal 转 double
                    .average()
                    .orElse(0); // 如果列表为空返回 0
            course.put("avgRating", avgRating);
            msg2_tmp += String.format("学生平均课堂投票率为：%f；", avgRating);

            double avgScore = courseaclist.stream()
                    .filter(c -> c.getFinalScore() != null) // 避免空指针
                    .mapToDouble(c -> c.getFinalScore().doubleValue()) // BigDecimal 转 double
                    .average()
                    .orElse(0); // 如果列表为空返回 0
            course.put("avgScore", avgScore);
            msg2_tmp += String.format("学生平均总分为：%f；", avgScore);
            msg2 += msg2_tmp + performance_detail;
//            msg2_AI += msg2_tmp;
// 前十
//            List<CourseActivity> top10Selected = courseaclist.stream()
//                    .sorted(Comparator.comparingLong(CourseActivity::getSelectedCount).reversed())
//                    .limit(10)
//                    .collect(Collectors.toList());
            detail.add(course);
        }
        int course_sum = courseList.toArray().length;
        info_list.put("course_sum", course_sum);
        info_list.put("class_sum", sum);
        info_list.put("student_sum", students);
        info_list.put("detail", detail);


        double avg = sum == 0 ? 0.0 : (double) students / sum;
        String msg1_tmp = String.format("您总共创建了%d个班级，涉及%d个课程；总共有%d位学生，平均每个班级有%.2f位学生。",
                sum, course_sum, students, avg);

        msg1 = String.format("%s其中，%s", msg1_tmp, class_detail);
        info_list.put("msg1", msg1);
        info_list.put("msg2", msg2);
//        System.out.println(info_list);

        return AjaxResult.success(info_list);
    }

//    {
//        "student_count": 260,
//            "top10_student_ids": ["2023001","2023002",...],
//        "top10_course_counts": [9,9,8,8,7,...],
//        "chapter_study_total": 4630,
//            "chapter_study_avg": 17.8,
//            "chapter_study_max": 43,
//            "chapter_study_min": 5,
//            "avg_ratings": 3.1,
//            "avg_tasks": 4.6,
//            "avg_answers": 1.8,
//            "avg_selected": 0.9
//    }
    @GetMapping("/getExecutorStudyinfo")
    public AjaxResult getExecutorStudyinfo(){
        Map<String, Object> info = studentService.getStudyStatistics();
        String detail = "";
//        List<String> ids = (List<String>)info.get("top10_student_ids");
//        List<String> counts = (List<String>)info.get("top10_course_counts");
//        for (int i = 0; i < ids.size(); i++) {
//            detail += String.format("学号：%s，选课数目：%d", ids.get(i), counts.get(i));
//        }
//        String msg = String.format("本学期共有%d位学生参与选课。" +
//                "本学期系统登记的章节学习次数总共是%d，平均每位学生的章节学习次数是%d，最高学习了%d次，最少学习了%d次。\n" +
//                "学生评分参与数平均为%f，学生任务参与数平均为%f，学生学生抢答参与数平均为%f，选人被选中次数平均为%f。" +
//                "选课数量前十的学生信息如下：%s",
//                info.get("student_count"),
//                info.get("chapter_study_total"), info.get("chapter_study_avg"), info.get("chapter_study_max"),info.get("chapter_study_min"),
//                info.get("avg_ratings"), info.get("avg_tasks"), info.get("avg_answers"), info.get("avg_selected"),
//                detail);
        String msg = String.format("本学期共有%d位学生参与选课。" +
                        "本学期系统登记的章节学习次数总共是%d次，平均每位学生的章节学习次数是%f次，最高学习了%d次，最少学习了%d次。\n" +
                        "学生评分参与数平均为%f，学生任务参与数平均为%f，学生学生抢答参与数平均为%f，选人被选中次数平均为%f。" ,
                info.get("student_count"),
                info.get("chapter_study_total"), info.get("chapter_study_avg"), info.get("chapter_study_max"),info.get("chapter_study_min"),
                info.get("avg_ratings"), info.get("avg_tasks"), info.get("avg_answers"), info.get("avg_selected"));
//        System.out.println("$$$$$$$$$$$" + msg);
        return AjaxResult.success(msg);
    }

}
