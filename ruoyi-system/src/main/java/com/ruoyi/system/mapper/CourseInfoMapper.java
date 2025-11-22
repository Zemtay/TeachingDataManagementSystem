package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.CourseInfo;

/**
 * 课程信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-05
 */
public interface CourseInfoMapper 
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
     * 删除课程信息
     * 
     * @param id 课程信息主键
     * @return 结果
     */
    public int deleteCourseInfoById(Long id);

    /**
     * 批量删除课程信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteCourseInfoByIds(Long[] ids);

    /**
     * 查询创建课程数前5的教师（按creator_id分组统计）
     *
     * @return 包含创建者ID和课程数量的集合
     */
    public List<Map<String, Object>> selectTop5CourseCreators();  // 改为返回Map

    /**
     * 查询各课程的学生总数（按课程名称分组汇总）
     *
     * @return 包含课程名称和学生总数的集合
     */
    public List<Map<String, Object>> selectCourseStudentCount();  // 改为返回Map

    /**
     * 查询课程统计信息（对应页面ClassInfo）
     * @return 统计结果：不同班级ID数、不同课程数、热门课程等
     */
    public Map<String, Object> selectClassInfoStats();

    void deleteAll();

    void batchInsert(List<CourseInfo> list);
}