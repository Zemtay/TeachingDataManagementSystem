package com.ruoyi.system.domain;
import lombok.Data;
import java.util.Date;

@Data
public class AiChatHistory {
    private Long id;
    private Long userId;
    private String messages; // 存 JSON 字符串
    private Date updateTime;
}
