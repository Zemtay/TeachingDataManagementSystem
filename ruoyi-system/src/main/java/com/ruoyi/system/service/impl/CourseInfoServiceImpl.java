package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.CourseInfoMapper;
import com.ruoyi.system.domain.CourseInfo;
import com.ruoyi.system.service.ICourseInfoService;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;


/**
 * 课程信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-05
 */
@Service
public class CourseInfoServiceImpl implements ICourseInfoService 
{
    @Autowired
    private CourseInfoMapper courseInfoMapper;

    /**
     * 查询课程信息
     * 
     * @param id 课程信息主键
     * @return 课程信息
     */
    @Override
    public CourseInfo selectCourseInfoById(Long id)
    {
        return courseInfoMapper.selectCourseInfoById(id);
    }

    /**
     * 查询课程信息列表
     * 
     * @param courseInfo 课程信息
     * @return 课程信息
     */
    @Override
    public List<CourseInfo> selectCourseInfoList(CourseInfo courseInfo)
    {
        // 获取当前登录用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser sysUser = loginUser.getUser();

        // 检查是否为教师角色（role_key=teacher）
        boolean isTeacher = sysUser.getRoles().stream()
                .map(r -> r.getRoleKey())
                .anyMatch(key -> key.equals("teacher"));

        // 教师只能看到自己创建的课程
        if (isTeacher) {
            // 教师的工号就是 user_name
            String teacherUserNo = sysUser.getUserName();
            courseInfo.setCreatorId(teacherUserNo);
        }

        return courseInfoMapper.selectCourseInfoList(courseInfo);
    }


    /**
     * 新增课程信息
     * 
     * @param courseInfo 课程信息
     * @return 结果
     */
    @Override
    public int insertCourseInfo(CourseInfo courseInfo)
    {
        return courseInfoMapper.insertCourseInfo(courseInfo);
    }

    /**
     * 修改课程信息
     * 
     * @param courseInfo 课程信息
     * @return 结果
     */
    @Override
    public int updateCourseInfo(CourseInfo courseInfo)
    {
        return courseInfoMapper.updateCourseInfo(courseInfo);
    }

    /**
     * 批量删除课程信息
     * 
     * @param ids 需要删除的课程信息主键
     * @return 结果
     */
    @Override
    public int deleteCourseInfoByIds(Long[] ids)
    {
        return courseInfoMapper.deleteCourseInfoByIds(ids);
    }

    /**
     * 删除课程信息信息
     * 
     * @param id 课程信息主键
     * @return 结果
     */
    @Override
    public int deleteCourseInfoById(Long id)
    {
        return courseInfoMapper.deleteCourseInfoById(id);
    }

    /**
     * 实现：获取创建课程数前5的教师
     */
    @Override
    public List<Map<String, Object>> getTop5CourseCreators()  // 改为返回Map
    {
        List<Map<String, Object>> result = courseInfoMapper.selectTop5CourseCreators();
        
        System.out.println("=== getTop5CourseCreators 调试信息 ===");
        System.out.println("返回数据条数: " + (result != null ? result.size() : "null"));
        if (result != null) {
            for (int i = 0; i < result.size(); i++) {
                Map<String, Object> item = result.get(i);
                System.out.println("第" + (i+1) + "条数据: " + item);
            }
        } else {
            System.out.println("返回结果为null");
        }
        System.out.println("===============================");
        
        return result;
    }

    /**
     * 实现：获取各课程的学生总数
     */
    @Override
    public List<Map<String, Object>> getCourseStudentCount()  // 改为返回Map
    {
        List<Map<String, Object>> result = courseInfoMapper.selectCourseStudentCount();
        
        System.out.println("=== getCourseStudentCount 调试信息 ===");
        System.out.println("返回数据条数: " + (result != null ? result.size() : "null"));
        if (result != null && !result.isEmpty()) {
            // 只打印前5条，避免日志过多
            int printCount = Math.min(result.size(), 5);
            for (int i = 0; i < printCount; i++) {
                Map<String, Object> item = result.get(i);
                System.out.println("第" + (i+1) + "条数据: " + item);
            }
            if (result.size() > 5) {
                System.out.println("... 还有 " + (result.size() - 5) + " 条数据未显示");
            }
        } else {
            System.out.println("返回结果为空或null");
        }
        System.out.println("===============================");
        
        return result;
    }

    /**
     * 实现：获取课程统计信息（对应页面ClassInfo）
     */
    @Override
    public Map<String, Object> getClassInfoStats()
    {
        Map<String, Object> result = courseInfoMapper.selectClassInfoStats();
        
        System.out.println("=== getClassInfoStats 调试信息 ===");
        System.out.println("返回数据: " + result);
        System.out.println("===============================");
        
        return result;
    }
}