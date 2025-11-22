package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.List;


/**
 * 班级信息对象 class_info
 * 
 * @author ruoyi
 * @date 2025-11-05
 */
public class ClassInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String courseName;

    /** 课程ID */
    @Excel(name = "课程ID")
    private String courseId;

    /** 课程编号 */
    @Excel(name = "课程编号")
    private String courseNum;

    /** 课程类型 */
    @Excel(name = "课程类型")
    private String courseType;

    /** 课程学生总数 */
    @Excel(name = "课程学生总数")
    private Long totalStudents;

    /** 班级ID */
    @Excel(name = "班级ID")
    private String classId;

    /** 教务课班级ID */
    @Excel(name = "教务课班级ID")
    private String eaclassId;

    /** 班级数量统计 */
    @Excel(name = "班级数量统计")
    private Long count;

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

    public void setCourseId(String courseId) 
    {
        this.courseId = courseId;
    }

    public String getCourseId() 
    {
        return courseId;
    }

    public void setCourseNum(String courseNum) 
    {
        this.courseNum = courseNum;
    }

    public String getCourseNum() 
    {
        return courseNum;
    }

    public void setCourseType(String courseType) 
    {
        this.courseType = courseType;
    }

    public String getCourseType() 
    {
        return courseType;
    }

    public void setTotalStudents(Long totalStudents) 
    {
        this.totalStudents = totalStudents;
    }

    public Long getTotalStudents() 
    {
        return totalStudents;
    }

    public void setClassId(String classId) 
    {
        this.classId = classId;
    }

    public String getClassId() 
    {
        return classId;
    }

    public void setEaclassId(String eaclassId) 
    {
        this.eaclassId = eaclassId;
    }

    public String getEaclassId() 
    {
        return eaclassId;
    }

    public void setCount(Long count) 
    {
        this.count = count;
    }

    public Long getCount() 
    {
        return count;
    }

    private List<String> courseNameList;

    public List<String> getCourseNameList() {
        return courseNameList;
    }

    public void setCourseNameList(List<String> courseNameList) {
        this.courseNameList = courseNameList;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("courseName", getCourseName())
            .append("courseId", getCourseId())
            .append("courseNum", getCourseNum())
            .append("courseType", getCourseType())
            .append("totalStudents", getTotalStudents())
            .append("classId", getClassId())
            .append("eaclassId", getEaclassId())
            .append("count", getCount())
            .toString();
    }
}