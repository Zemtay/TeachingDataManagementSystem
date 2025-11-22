package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.CourseInfo;

/**
 * 课程信息Service接口
 * 
 * @author ruoyi
 * @date 2025-11-05
 */
public interface ICourseInfoService 
{
    /**
     * 查询课程信息
     * 
     * @param id 课程信息主键
     * @return 课程信息
     */
    public CourseInfo selectCourseInfoById(Long id);

    /**
     * 查询课程信息列表
     * 
     * @param courseInfo 课程信息
     * @return 课程信息集合
     */
    public List<CourseInfo> selectCourseInfoList(CourseInfo courseInfo);

    /**
     * 新增课程信息
     * 
     * @param courseInfo 课程信息
     * @return 结果
     */
    public int insertCourseInfo(CourseInfo courseInfo);

    /**
     * 修改课程信息
     * 
     * @param courseInfo 课程信息
     * @return 结果
     */
    public int updateCourseInfo(CourseInfo courseInfo);

    /**
     * 批量删除课程信息
     * 
     * @param ids 需要删除的课程信息主键集合
     * @return 结果
     */
    public int deleteCourseInfoByIds(Long[] ids);

    /**
     * 删除课程信息信息
     * 
     * @param id 课程信息主键
     * @return 结果
     */
    public int deleteCourseInfoById(Long id);

    /**
     * 获取创建课程数前5的教师
     * @return 包含创建者ID和课程数量的集合
     */
    public List<Map<String, Object>> getTop5CourseCreators();

    /**
     * 获取各课程的学生总数
     * @return 包含课程名称和学生总数的集合
     */
    public List<Map<String, Object>> getCourseStudentCount();

    /**
     * 获取课程统计信息（对应页面ClassInfo）
     * @return 统计结果：不同班级ID数、不同课程数、热门课程等
     */
    public Map<String, Object> getClassInfoStats();
}