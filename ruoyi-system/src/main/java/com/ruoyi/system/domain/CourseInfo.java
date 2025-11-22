package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 课程信息对象 course_info
 * 
 * @author ruoyi
 * @date 2025-11-05
 */
public class CourseInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String courseName;

    /** 课程编号 */
    @Excel(name = "课程编号")
    private String courseNum;

    /** 课程学期 */
    @Excel(name = "课程学期")
    private String courseSemester;

    /** 建课人工号/学号 */
    @Excel(name = "建课人工号/学号")
    private String creatorId;

    /** 班级总数 */
    @Excel(name = "班级总数")
    private Long classCount;

    /** 课程学生总数 */
    @Excel(name = "课程学生总数")
    private Long totalStudents;

    // ==============================================
    // 新增：用于接收“教师创建课程数量”的统计结果
    // ==============================================
    /** 教师创建的课程数量（统计用） */
    private Integer courseCount;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setCourseName(String courseName) 
    {
        this.courseName = courseName;
    }

    public String getCourseName() 
    {
        return courseName;
    }

    public void setCourseNum(String courseNum) 
    {
        this.courseNum = courseNum;
    }

    public String getCourseNum() 
    {
        return courseNum;
    }

    public void setCourseSemester(String courseSemester) 
    {
        this.courseSemester = courseSemester;
    }

    public String getCourseSemester() 
    {
        return courseSemester;
    }

    public void setCreatorId(String creatorId) 
    {
        this.creatorId = creatorId;
    }

    public String getCreatorId() 
    {
        return creatorId;
    }

    public void setClassCount(Long classCount) 
    {
        this.classCount = classCount;
    }

    public Long getClassCount() 
    {
        return classCount;
    }

    public void setTotalStudents(Long totalStudents) 
    {
        this.totalStudents = totalStudents;
    }

    public Long getTotalStudents() 
    {
        return totalStudents;
    }

    // ==============================================
    // 新增：courseCount 的 getter 和 setter 方法
    // ==============================================
    public void setCourseCount(Integer courseCount) 
    {
        this.courseCount = courseCount;
    }

    public Integer getCourseCount() 
    {
        return courseCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("courseName", getCourseName())
            .append("courseNum", getCourseNum())
            .append("courseSemester", getCourseSemester())
            .append("creatorId", getCreatorId())
            .append("classCount", getClassCount())
            .append("totalStudents", getTotalStudents())
            // 新增：把 courseCount 加入 toString，方便调试查看
            .append("courseCount", getCourseCount())
            .toString();
    }
}