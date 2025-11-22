package com.ruoyi.web.controller.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.ruoyi.common.core.domain.AjaxResult;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api")
@EnableAsync // 启用异步
public class FileUploadController {

    @Value("${ruoyi.profile}")
    private String uploadPath;

    @Autowired
    private ImportDataService importDataService;

    @PostMapping("/upload")
    public AjaxResult uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return AjaxResult.error("文件为空");
        }

        String originalFilename = file.getOriginalFilename();
        String projectDir = System.getProperty("user.dir");
        String saveDir = projectDir + File.separator + "uploadPath";
        File dir = new File(saveDir);
        if (!dir.exists()) dir.mkdirs();

        File dest = new File(saveDir, originalFilename);
        if (dest.exists()) dest.delete();

        file.transferTo(dest);

        // 异步处理数据
        importDataService.processFileAsync(dest);

        // 立即返回上传成功
        return AjaxResult.success("上传成功", originalFilename);
    }
}
