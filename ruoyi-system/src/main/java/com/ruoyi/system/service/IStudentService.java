package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;

import com.ruoyi.system.domain.Student;

/**
 * 学生信息Service接口
 * 
 * @author ruoyi
 * @date 2025-11-05
 */
public interface IStudentService 
{
    public Map<String, Object> getStudyStatistics();
    /**
     * 查询学生信息
     * 
     * @param studentId 学生信息主键
     * @return 学生信息
     */
    public Student selectStudentByStudentId(String studentId);

    /**
     * 查询学生信息列表
     * 
     * @param student 学生信息
     * @return 学生信息集合
     */
    public List<Student> selectStudentList(Student student);

    /**
     * 新增学生信息
     * 
     * @param student 学生信息
     * @return 结果
     */
    public int insertStudent(Student student);

    /**
     * 修改学生信息
     * 
     * @param student 学生信息
     * @return 结果
     */
    public int updateStudent(Student student);

    /**
     * 批量删除学生信息
     * 
     * @param studentIds 需要删除的学生信息主键集合
     * @return 结果
     */
    public int deleteStudentByStudentIds(String[] studentIds);

    /**
     * 删除学生信息信息
     * 
     * @param studentId 学生信息主键
     * @return 结果
     */
    public int deleteStudentByStudentId(String studentId);
}
