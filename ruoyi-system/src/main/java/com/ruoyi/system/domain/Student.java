package com.ruoyi.system.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 学生信息对象 student
 * 
 * @author ruoyi
 * @date 2025-11-05
 */
public class Student extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 学号/工号 */
    private String studentId;

    /** 课程数量 */
    @Excel(name = "课程数量")
    private Long courseCount;

    /** 任务点完成数 */
    @Excel(name = "任务点完成数")
    private Long tasksCompleted;

    /** 视频任务点完成数 */
    @Excel(name = "视频任务点完成数")
    private Long videoTasksCompleted;

    /** 章节测验完成数 */
    @Excel(name = "章节测验完成数")
    private Long chapterTestsCompleted;

    /** 作业完成数 */
    @Excel(name = "作业完成数")
    private Long homeworkCompleted;

    /** 考试完成数 */
    @Excel(name = "考试完成数")
    private Long examsCompleted;

    /** 章节学习次数 */
    @Excel(name = "章节学习次数")
    private Long chapterStudyCount;

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

    /** 学生问卷参与数 */
    @Excel(name = "学生问卷参与数")
    private Long studentVotes;

    /** 选人被选中次数 */
    @Excel(name = "选人被选中次数")
    private Long selectedCount;

    /** 学生抢答参与数 */
    @Excel(name = "学生抢答参与数")
    private Long studentAnswers;

    /** 学生评分参与数 */
    @Excel(name = "学生评分参与数")
    private Long studentRatings;

    /** 学生任务参与数 */
    @Excel(name = "学生任务参与数")
    private Long studentTasks;

    public void setStudentId(String studentId) 
    {
        this.studentId = studentId;
    }

    public String getStudentId() 
    {
        return studentId;
    }

    public void setCourseCount(Long courseCount) 
    {
        this.courseCount = courseCount;
    }

    public Long getCourseCount() 
    {
        return courseCount;
    }

    public void setTasksCompleted(Long tasksCompleted) 
    {
        this.tasksCompleted = tasksCompleted;
    }

    public Long getTasksCompleted() 
    {
        return tasksCompleted;
    }

    public void setVideoTasksCompleted(Long videoTasksCompleted) 
    {
        this.videoTasksCompleted = videoTasksCompleted;
    }

    public Long getVideoTasksCompleted() 
    {
        return videoTasksCompleted;
    }

    public void setChapterTestsCompleted(Long chapterTestsCompleted) 
    {
        this.chapterTestsCompleted = chapterTestsCompleted;
    }

    public Long getChapterTestsCompleted() 
    {
        return chapterTestsCompleted;
    }

    public void setHomeworkCompleted(Long homeworkCompleted) 
    {
        this.homeworkCompleted = homeworkCompleted;
    }

    public Long getHomeworkCompleted() 
    {
        return homeworkCompleted;
    }

    public void setExamsCompleted(Long examsCompleted) 
    {
        this.examsCompleted = examsCompleted;
    }

    public Long getExamsCompleted() 
    {
        return examsCompleted;
    }

    public void setChapterStudyCount(Long chapterStudyCount) 
    {
        this.chapterStudyCount = chapterStudyCount;
    }

    public Long getChapterStudyCount() 
    {
        return chapterStudyCount;
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

    public void setSelectedCount(Long selectedCount) 
    {
        this.selectedCount = selectedCount;
    }

    public Long getSelectedCount() 
    {
        return selectedCount;
    }

    public void setStudentAnswers(Long studentAnswers) 
    {
        this.studentAnswers = studentAnswers;
    }

    public Long getStudentAnswers() 
    {
        return studentAnswers;
    }

    public void setStudentRatings(Long studentRatings) 
    {
        this.studentRatings = studentRatings;
    }

    public Long getStudentRatings() 
    {
        return studentRatings;
    }

    public void setStudentTasks(Long studentTasks) 
    {
        this.studentTasks = studentTasks;
    }

    public Long getStudentTasks() 
    {
        return studentTasks;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("studentId", getStudentId())
            .append("courseCount", getCourseCount())
            .append("tasksCompleted", getTasksCompleted())
            .append("videoTasksCompleted", getVideoTasksCompleted())
            .append("chapterTestsCompleted", getChapterTestsCompleted())
            .append("homeworkCompleted", getHomeworkCompleted())
            .append("examsCompleted", getExamsCompleted())
            .append("chapterStudyCount", getChapterStudyCount())
            .append("totalDiscussions", getTotalDiscussions())
            .append("totalPosts", getTotalPosts())
            .append("totalReplies", getTotalReplies())
            .append("coursePoints", getCoursePoints())
            .append("studentVotes", getStudentVotes())
            .append("selectedCount", getSelectedCount())
            .append("studentAnswers", getStudentAnswers())
            .append("studentRatings", getStudentRatings())
            .append("studentTasks", getStudentTasks())
            .toString();
    }
}
