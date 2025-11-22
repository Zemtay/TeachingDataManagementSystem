package com.ruoyi.system.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.ClassInfoMapper;
import com.ruoyi.system.domain.ClassInfo;
import com.ruoyi.system.service.IClassInfoService;

/**
 * 班级信息Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-11-05
 */
@Service
public class ClassInfoServiceImpl implements IClassInfoService 
{
    @Autowired
    private ClassInfoMapper classInfoMapper;

    /**
     * 查询班级信息
     * 
     * @param id 班级信息主键
     * @return 班级信息
     */
    @Override
    public ClassInfo selectClassInfoById(Long id)
    {
        return classInfoMapper.selectClassInfoById(id);
    }

    /**
     * 查询班级信息列表
     * 
     * @param classInfo 班级信息
     * @return 班级信息
     */
    @Override
    public List<ClassInfo> selectClassInfoList(ClassInfo classInfo)
    {

        return classInfoMapper.selectClassInfoList(classInfo);
    }

    /**
     * 新增班级信息
     * 
     * @param classInfo 班级信息
     * @return 结果
     */
    @Override
    public int insertClassInfo(ClassInfo classInfo)
    {
        return classInfoMapper.insertClassInfo(classInfo);
    }

    /**
     * 修改班级信息
     * 
     * @param classInfo 班级信息
     * @return 结果
     */
    @Override
    public int updateClassInfo(ClassInfo classInfo)
    {
        return classInfoMapper.updateClassInfo(classInfo);
    }

    /**
     * 批量删除班级信息
     * 
     * @param ids 需要删除的班级信息主键
     * @return 结果
     */
    @Override
    public int deleteClassInfoByIds(Long[] ids)
    {
        return classInfoMapper.deleteClassInfoByIds(ids);
    }

    /**
     * 删除班级信息信息
     * 
     * @param id 班级信息主键
     * @return 结果
     */
    @Override
    public int deleteClassInfoById(Long id)
    {
        return classInfoMapper.deleteClassInfoById(id);
    }

    /**
     * 实现：查询各课程的班级数（按课程名称分组统计）
     * 调用Mapper层的selectCourseClassCount方法获取数据
     */
    @Override
    public List<Map<String, Object>> getCourseClassCount()
    {
        // return classInfoMapper.selectCourseClassCount();
        List<Map<String, Object>> result = classInfoMapper.selectCourseClassCount();
        
        // 添加调试日志
        System.out.println("=== getCourseClassCount 调试信息 ===");
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
}