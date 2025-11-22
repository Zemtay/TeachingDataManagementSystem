package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import java.util.List;

/**
 * 课程活动对象 course_activity
 * 
 * @author ruoyi
 * @date 2025-11-05
 */
public class CourseActivity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 学号/工号 */
    @Excel(name = "学号/工号")
    private String studentId;

    /** 课程名称 */
    @Excel(name = "课程名称")
    private String courseName;

    /** 教学班ID */
    @Excel(name = "教学班ID")
    private String classId;

    /** 讨论总数 */
    @Excel(name = "讨论总数")
    private Long totalDiscussions;

    /** 发帖总数 */
    @Excel(name = "发帖总数")
    private Long totalPosts;

    /** 回帖总数 */
    @Excel(name = "回帖总数")
    private Long totalReplies;

    /** 课程积分 */
    @Excel(name = "课程积分")
    private BigDecimal coursePoints;

    /** 学生投票问卷参与数 */
    @Excel(name = "学生投票问卷参与数")
    private Long studentVotes;

    /** 学生投票问卷参与率 */
    @Excel(name = "学生投票问卷参与率")
    private BigDecimal studentVoteRate;

    /** 选人被选中次数 */
    @Excel(name = "选人被选中次数")
    private Long selectedCount;

    /** 选人被选中次率 */
    @Excel(name = "选人被选中次率")
    private BigDecimal selectedRate;

    /** 学生抢答参与数 */
    @Excel(name = "学生抢答参与数")
    private Long studentAnswers;

    /** 学生抢答参与率 */
    @Excel(name = "学生抢答参与率")
    private BigDecimal studentAnswerRate;

    /** 学生评分参与数 */
    @Excel(name = "学生评分参与数")
    private Long studentRatings;

    /** 学生评分参与率 */
    @Excel(name = "学生评分参与率")
    private BigDecimal studentRatingRate;

    /** 学生任务参与数 */
    @Excel(name = "学生任务参与数")
    private Long studentTasks;

    /** 学生任务参与率 */
    @Excel(name = "学生任务参与率")
    private BigDecimal studentTaskRate;

    /** 综合成绩 */
    @Excel(name = "综合成绩")
    private BigDecimal finalScore;

    /** 证书发放状态（是/否） */
    @Excel(name = "证书发放状态", readConverterExp = "是=/否")
    private String certificateStatus;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setStudentId(String studentId) 
    {
        this.studentId = studentId;
    }

    public String getStudentId() 
    {
        return studentId;
    }

    public void setCourseName(String courseName) 
    {
        this.courseName = courseName;
    }

    public String getCourseName() 
    {
        return courseName;
    }

    public void setClassId(String classId) 
    {
        this.classId = classId;
    }

    public String getClassId() 
    {
        return classId;
    }

    public void setTotalDiscussions(Long totalDiscussions) 
    {
        this.totalDiscussions = totalDiscussions;
    }

    public Long getTotalDiscussions() 
    {
        return totalDiscussions;
    }

    public void setTotalPosts(Long totalPosts) 
    {
        this.totalPosts = totalPosts;
    }

    public Long getTotalPosts() 
    {
        return totalPosts;
    }

    public void setTotalReplies(Long totalReplies) 
    {
        this.totalReplies = totalReplies;
    }

    public Long getTotalReplies() 
    {
        return totalReplies;
    }

    public void setCoursePoints(BigDecimal coursePoints) 
    {
        this.coursePoints = coursePoints;
    }

    public BigDecimal getCoursePoints() 
    {
        return coursePoints;
    }

    public void setStudentVotes(Long studentVotes) 
    {
        this.studentVotes = studentVotes;
    }

    public Long getStudentVotes() 
    {
        return studentVotes;
    }

    public void setStudentVoteRate(BigDecimal studentVoteRate) 
    {
        this.studentVoteRate = studentVoteRate;
    }

    public BigDecimal getStudentVoteRate() 
    {
        return studentVoteRate;
    }

    public void setSelectedCount(Long selectedCount) 
    {
        this.selectedCount = selectedCount;
    }

    public Long getSelectedCount() 
    {
        return selectedCount;
    }

    public void setSelectedRate(BigDecimal selectedRate) 
    {
        this.selectedRate = selectedRate;
    }

    public BigDecimal getSelectedRate() 
    {
        return selectedRate;
    }

    public void setStudentAnswers(Long studentAnswers) 
    {
        this.studentAnswers = studentAnswers;
    }

    public Long getStudentAnswers() 
    {
        return studentAnswers;
    }

    public void setStudentAnswerRate(BigDecimal studentAnswerRate) 
    {
        this.studentAnswerRate = studentAnswerRate;
    }

    public BigDecimal getStudentAnswerRate() 
    {
        return studentAnswerRate;
    }

    public void setStudentRatings(Long studentRatings) 
    {
        this.studentRatings = studentRatings;
    }

    public Long getStudentRatings() 
    {
        return studentRatings;
    }

    public void setStudentRatingRate(BigDecimal studentRatingRate) 
    {
        this.studentRatingRate = studentRatingRate;
    }

    public BigDecimal getStudentRatingRate() 
    {
        return studentRatingRate;
    }

    public void setStudentTasks(Long studentTasks) 
    {
        this.studentTasks = studentTasks;
    }

    public Long getStudentTasks() 
    {
        return studentTasks;
    }

    public void setStudentTaskRate(BigDecimal studentTaskRate) 
    {
        this.studentTaskRate = studentTaskRate;
    }

    public BigDecimal getStudentTaskRate() 
    {
        return studentTaskRate;
    }

    public void setFinalScore(BigDecimal finalScore) 
    {
        this.finalScore = finalScore;
    }

    public BigDecimal getFinalScore() 
    {
        return finalScore;
    }

    public void setCertificateStatus(String certificateStatus) 
    {
        this.certificateStatus = certificateStatus;
    }

    public String getCertificateStatus() 
    {
        return certificateStatus;
    }

    /** 允许过滤的课程名称列表（教师权限用） */
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
            .append("studentId", getStudentId())
            .append("courseName", getCourseName())
            .append("classId", getClassId())
            .append("totalDiscussions", getTotalDiscussions())
            .append("totalPosts", getTotalPosts())
            .append("totalReplies", getTotalReplies())
            .append("coursePoints", getCoursePoints())
            .append("studentVotes", getStudentVotes())
            .append("studentVoteRate", getStudentVoteRate())
            .append("selectedCount", getSelectedCount())
            .append("selectedRate", getSelectedRate())
            .append("studentAnswers", getStudentAnswers())
            .append("studentAnswerRate", getStudentAnswerRate())
            .append("studentRatings", getStudentRatings())
            .append("studentRatingRate", getStudentRatingRate())
            .append("studentTasks", getStudentTasks())
            .append("studentTaskRate", getStudentTaskRate())
            .append("finalScore", getFinalScore())
            .append("certificateStatus", getCertificateStatus())
            .toString();
    }
}
