package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.CourseActivity;

/**
 * 课程活动Service接口
 * 
 * @author ruoyi
 * @date 2025-11-05
 */
public interface ICourseActivityService 
{
    /**
     * 查询课程活动
     * 
     * @param id 课程活动主键
     * @return 课程活动
     */
    public CourseActivity selectCourseActivityById(Long id);

    /**
     * 查询课程活动列表
     * 
     * @param courseActivity 课程活动
     * @return 课程活动集合
     */
    public List<CourseActivity> selectCourseActivityList(CourseActivity courseActivity);

    /**
     * 新增课程活动
     * 
     * @param courseActivity 课程活动
     * @return 结果
     */
    public int insertCourseActivity(CourseActivity courseActivity);

    /**
     * 修改课程活动
     * 
     * @param courseActivity 课程活动
     * @return 结果
     */
    public int updateCourseActivity(CourseActivity courseActivity);

    /**
     * 批量删除课程活动
     * 
     * @param ids 需要删除的课程活动主键集合
     * @return 结果
     */
    public int deleteCourseActivityByIds(Long[] ids);

    /**
     * 删除课程活动信息
     * 
     * @param id 课程活动主键
     * @return 结果
     */
    public int deleteCourseActivityById(Long id);
}
