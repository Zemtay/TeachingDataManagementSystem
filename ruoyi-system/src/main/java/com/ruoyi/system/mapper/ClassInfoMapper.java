package com.ruoyi.system.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.system.domain.ClassInfo;

/**
 * 班级信息Mapper接口
 * 
 * @author ruoyi
 * @date 2025-11-05
 */
public interface ClassInfoMapper 
{
    /**
     * 查询班级信息
     * 
     * @param id 班级信息主键
     * @return 班级信息
     */
    public ClassInfo selectClassInfoById(Long id);

    /**
     * 查询班级信息列表
     * 
     * @param classInfo 班级信息
     * @return 班级信息集合
     */
    public List<ClassInfo> selectClassInfoList(ClassInfo classInfo);

    /**
     * 新增班级信息
     * 
     * @param classInfo 班级信息
     * @return 结果
     */
    public int insertClassInfo(ClassInfo classInfo);

    /**
     * 修改班级信息
     * 
     * @param classInfo 班级信息
     * @return 结果
     */
    public int updateClassInfo(ClassInfo classInfo);

    /**
     * 删除班级信息
     * 
     * @param id 班级信息主键
     * @return 结果
     */
    public int deleteClassInfoById(Long id);

    /**
     * 批量删除班级信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteClassInfoByIds(Long[] ids);

    /**
     * 新增：查询各课程的班级数（按课程名称分组统计）
     * @return 包含课程名称（courseName）和班级数（count）的集合
     */
    public List<Map<String, Object>> selectCourseClassCount();

    void deleteAll();

    void batchInsert(List<ClassInfo> list);
}
