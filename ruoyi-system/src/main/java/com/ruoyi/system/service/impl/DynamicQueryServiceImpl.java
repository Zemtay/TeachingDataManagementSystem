package com.ruoyi.system.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.system.mapper.DynamicQueryMapper;
import com.ruoyi.system.service.IDynamicQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Set;

@Service
public class DynamicQueryServiceImpl implements IDynamicQueryService {

    @Autowired
    private DynamicQueryMapper dynamicQueryMapper;

    // 允许的表白名单（必须配置）
    private static final Set<String> ALLOWED_TABLES = Collections.unmodifiableSet(
//            new HashSet<>(Arrays.asList("ClassInfoMapper", "CourseActivityMapper", "StudentMapper", "CourseInfoMapper"))
            new HashSet<>(Arrays.asList("class_info", "course_activity", "course_info", "student"))
    );

    private static final Set<String> NOT_ALLOWED_TABLES = Collections.unmodifiableSet(
//            new HashSet<>(Arrays.asList("ClassInfoMapper", "CourseActivityMapper", "StudentMapper", "CourseInfoMapper"))
            new HashSet<>(Arrays.asList("qrtz_", "sys_", "ai_chat_history"))
    );

    // 允许的字段前缀白名单
//    private static final Set<String> ALLOWED_PREFIXES = Collections.unmodifiableSet(
//            "user_", "dept_", "role_", "post_"
//    );

    @Override
    public List<Map<String, Object>> executeDynamicQuery(String tableName, Map<String, Object> conditions) {

        // 2. 表名校验（白名单机制）
        if (!ALLOWED_TABLES.contains(tableName)) {
//            log.warn("尝试访问非法表 {}，已跳过查询", tableName);
            System.out.println("尝试访问非法表 {}，已跳过查询" + tableName);
            return Collections.emptyList();
        }

        // 3. 字段名校验
//        for (String key : conditions.keySet()) {
//            // 只允许包含特定前缀的字段
//            if (!ALLOWED_PREFIXES.stream().anyMatch(key::startsWith)) {
//                throw new ServiceException("非法字段名: " + key);
//            }
//        }

        // 4. 执行查询
        return dynamicQueryMapper.dynamicSelect(tableName, conditions);
    }

    @Override
    public List<Map<String, Object>> executeQuery(String sql) {
        // 2. 表名校验（白名单机制）
        String s = sql.toLowerCase(Locale.ROOT);
        for (String table : NOT_ALLOWED_TABLES) {
            if(s.contains((table))){
                System.out.println("尝试访问非法表 {}，已跳过查询" + table);
                return Collections.emptyList();
            }
        }
        return dynamicQueryMapper.dynamicQuery(sql);
    }
}