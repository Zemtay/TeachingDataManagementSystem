package com.ruoyi.web.controller.dashboard;

import com.alibaba.fastjson2.JSON;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.IAiAssistantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import com.ruoyi.common.utils.SecurityUtils;

@RestController
@RequestMapping("/dashboard/ai-assistant")
public class AiAssistantController {

    @Autowired
    private  IAiAssistantService aiAssistantService;

//    @PostMapping("/answer")
//    public AjaxResult chat(@RequestParam("input") String input){
//        try{
//            String reply = aiAssistantService.getAiReply(input);
//            System.out.println("智能助手输出内容: " + reply);
//            return AjaxResult.success("请求成功").put("response",reply);
//        } catch (Exception e){
//            e.printStackTrace();
//            return AjaxResult.error("AI 回复失败： " + e.getMessage());
//        }
//    }
    @PostMapping("/answer")
    public AjaxResult chat(@RequestBody Map<String, Object> body) {
        try {
            String input = body.get("input").toString();
            System.out.println("智能助手输入内容: " + input);
            String reply = aiAssistantService.getAiReply(input);
            System.out.println("智能助手输出内容: " + reply);
//            return AjaxResult.success("请求成功").put("response", reply);
            return AjaxResult.success(reply);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("fail to get input");
            return AjaxResult.error("AI 回复失败: " + e.getMessage());
        }
    }

    /** 获取用户聊天记录 */
    @GetMapping("/chatHistory")
    public AjaxResult getChatHistory() {
        Long userId = SecurityUtils.getUserId();
        return AjaxResult.success(aiAssistantService.getChatHistory(userId));
    }

    /** 保存用户聊天记录 */
    @PostMapping("/chatHistory")
    public AjaxResult saveChatHistory(@RequestBody Map<String, Object> req) {
        Long userId = SecurityUtils.getUserId();
        String messages = JSON.toJSONString(req.get("messages"));
        aiAssistantService.saveChatHistory(userId, messages);
        return AjaxResult.success();
    }

    @DeleteMapping("/chatHistory")
    public AjaxResult clearChatHistory() {
        Long userId = SecurityUtils.getUserId();
        aiAssistantService.clearChatHistory(userId);
        return AjaxResult.success("聊天记录已清空");
    }

}