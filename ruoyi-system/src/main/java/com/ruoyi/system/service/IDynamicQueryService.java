package com.ruoyi.system.service;

import java.util.List;
import java.util.Map;

public interface IDynamicQueryService {
    List<Map<String, Object>> executeDynamicQuery(String tableName, Map<String, Object> conditions);
    List<Map<String, Object>> executeQuery(String sql);
}