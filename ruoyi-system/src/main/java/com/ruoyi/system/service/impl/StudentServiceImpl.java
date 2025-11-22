package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.StudentMapper;
import com.ruoyi.system.domain.Student;
import com.ruoyi.system.service.IStudentService;

/**
 * 学生信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-05
 */
@Service
public class StudentServiceImpl implements IStudentService 
{
    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Map<String, Object> getStudyStatistics() {

        List<Student> list = studentMapper.selectAllStudents();
//        list.forEach(s -> System.out.println(s));
        List<Student> top10 = studentMapper.selectTop10ByCourseCount();

        int studentCount = list.size();   // 学生总数

        // 选课数量前十名单
//        List<String> top10StudentIds = top10.stream()
//                .map(Student::getStudentId)
//                .collect(Collectors.toList());
//
//        // 前十选课数量
//        List<Long> top10CourseCounts = top10.stream()
//                .map(Student::getCourseCount)
//                .collect(Collectors.toList());

        long chapterStudyTotal = list.stream()
                .filter(Objects::nonNull)          // 先剔除 null 元素
                .mapToLong(s -> s.getChapterStudyCount() == null ? 0L : s.getChapterStudyCount())
                .sum();

        double chapterStudyAvg = list.stream()
                .filter(Objects::nonNull)
                .mapToLong(s -> s.getChapterStudyCount() == null ? 0L : s.getChapterStudyCount())
                .average()
                .orElse(0);

        long chapterStudyMax = list.stream()
                .filter(Objects::nonNull)
                .mapToLong(Student::getChapterStudyCount)
                .max().orElse(0);

        long chapterStudyMin = list.stream()
                .filter(Objects::nonNull)
                .mapToLong(Student::getChapterStudyCount)
                .min().orElse(0);

        double avgRatings = list.stream()
                .filter(Objects::nonNull)
                .mapToLong(Student::getStudentRatings)
                .average().orElse(0);

        double avgTasks = list.stream()
                .filter(Objects::nonNull)
                .mapToLong(Student::getStudentTasks)
                .average().orElse(0);

        double avgAnswers = list.stream()
                .filter(Objects::nonNull)
                .mapToLong(Student::getStudentAnswers)
                .average().orElse(0);

        double avgSelected = list.stream()
                .filter(Objects::nonNull)
                .mapToLong(Student::getSelectedCount)
                .average().orElse(0);

        // 封装到 Map（前端直接用）
        Map<String, Object> map = new HashMap<>();

        map.put("student_count", studentCount);
//        map.put("top10_student_ids", top10StudentIds);
//        map.put("top10_course_counts", top10CourseCounts);

        map.put("chapter_study_total", chapterStudyTotal);
        map.put("chapter_study_avg", chapterStudyAvg);
        map.put("chapter_study_max", chapterStudyMax);
        map.put("chapter_study_min", chapterStudyMin);

        map.put("avg_ratings", avgRatings);
        map.put("avg_tasks", avgTasks);
        map.put("avg_answers", avgAnswers);
        map.put("avg_selected", avgSelected);

        return map;
    }

    /**
     * 查询学生信息
     * 
     * @param studentId 学生信息主键
     * @return 学生信息
     */
    @Override
    public Student selectStudentByStudentId(String studentId)
    {
        return studentMapper.selectStudentByStudentId(studentId);
    }

    /**
     * 查询学生信息列表
     * 
     * @param student 学生信息
     * @return 学生信息
     */
    @Override
    public List<Student> selectStudentList(Student student)
    {
        return studentMapper.selectStudentList(student);
    }

    /**
     * 新增学生信息
     * 
     * @param student 学生信息
     * @return 结果
     */
    @Override
    public int insertStudent(Student student)
    {
        return studentMapper.insertStudent(student);
    }

    /**
     * 修改学生信息
     * 
     * @param student 学生信息
     * @return 结果
     */
    @Override
    public int updateStudent(Student student)
    {
        return studentMapper.updateStudent(student);
    }

    /**
     * 批量删除学生信息
     * 
     * @param studentIds 需要删除的学生信息主键
     * @return 结果
     */
    @Override
    public int deleteStudentByStudentIds(String[] studentIds)
    {
        return studentMapper.deleteStudentByStudentIds(studentIds);
    }

    /**
     * 删除学生信息信息
     * 
     * @param studentId 学生信息主键
     * @return 结果
     */
    @Override
    public int deleteStudentByStudentId(String studentId)
    {
        return studentMapper.deleteStudentByStudentId(studentId);
    }
}
