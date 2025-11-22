package com.ruoyi.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface DynamicQueryMapper {
    List<Map<String, Object>> dynamicSelect(@Param("tableName") String tableName,
                                            @Param("conditions") Map<String, Object> conditions);
    List<Map<String, Object>> dynamicQuery(@Param("sql") String sql);
}