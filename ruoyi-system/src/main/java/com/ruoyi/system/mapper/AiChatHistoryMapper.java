package com.ruoyi.system.mapper;
import com.ruoyi.system.domain.AiChatHistory;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface AiChatHistoryMapper {
    AiChatHistory selectByUserId(Long userId);
    int insertOrUpdate(AiChatHistory history);
    int deleteByUserId(Long userId);
}
