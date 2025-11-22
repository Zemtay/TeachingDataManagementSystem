package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.CourseActivityMapper;
import com.ruoyi.system.domain.CourseActivity;
import com.ruoyi.system.service.ICourseActivityService;

/**
 * 课程活动Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-05
 */
@Service
public class CourseActivityServiceImpl implements ICourseActivityService 
{
    @Autowired
    private CourseActivityMapper courseActivityMapper;

    /**
     * 查询课程活动
     * 
     * @param id 课程活动主键
     * @return 课程活动
     */
    @Override
    public CourseActivity selectCourseActivityById(Long id)
    {
        return courseActivityMapper.selectCourseActivityById(id);
    }

    /**
     * 查询课程活动列表
     * 
     * @param courseActivity 课程活动
     * @return 课程活动
     */
    @Override
    public List<CourseActivity> selectCourseActivityList(CourseActivity courseActivity)
    {
        return courseActivityMapper.selectCourseActivityList(courseActivity);
    }

    /**
     * 新增课程活动
     * 
     * @param courseActivity 课程活动
     * @return 结果
     */
    @Override
    public int insertCourseActivity(CourseActivity courseActivity)
    {
        return courseActivityMapper.insertCourseActivity(courseActivity);
    }

    /**
     * 修改课程活动
     * 
     * @param courseActivity 课程活动
     * @return 结果
     */
    @Override
    public int updateCourseActivity(CourseActivity courseActivity)
    {
        return courseActivityMapper.updateCourseActivity(courseActivity);
    }

    /**
     * 批量删除课程活动
     * 
     * @param ids 需要删除的课程活动主键
     * @return 结果
     */
    @Override
    public int deleteCourseActivityByIds(Long[] ids)
    {
        return courseActivityMapper.deleteCourseActivityByIds(ids);
    }

    /**
     * 删除课程活动信息
     * 
     * @param id 课程活动主键
     * @return 结果
     */
    @Override
    public int deleteCourseActivityById(Long id)
    {
        return courseActivityMapper.deleteCourseActivityById(id);
    }
}
