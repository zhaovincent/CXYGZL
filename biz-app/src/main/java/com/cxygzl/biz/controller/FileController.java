package com.cxygzl.biz.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.biz.service.IFileService;
import com.cxygzl.common.config.NotWriteLogAnno;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 文件控制器
 */
@RestController
@RequestMapping(value = {"file","api/file"})
public class FileController {

    @Value("${file.dir}")
    private String fileDir;
    @Value("${file.showUrl}")
    private String fileShowUrl;

    @Resource
    private IFileService fileService;



    /**
     * 上传文件
     * @param file
     * @return
     */
    @SneakyThrows
    @PostMapping("upload")
    @NotWriteLogAnno(exclude = false,all = true)
    public Object upload(MultipartFile file){


        String originalFilename = file.getOriginalFilename();

        return fileService.save(file.getBytes(),originalFilename);

    }

    /**
     * 显示文件
     * @param key
     */
    @SneakyThrows
    @GetMapping("/show/{key}")
    @NotWriteLogAnno(exclude = false,all = false,printResultLog = false,paramsExclude = "response")
    public void show(@PathVariable String key, HttpServletResponse response){
        String format = StrUtil.format("{}/{}", fileDir, key);
        IoUtil.write(response.getOutputStream(),true,FileUtil.readBytes(format));
    }

    /**
     * 显示文件
     * @param key
     */
    @SneakyThrows
    @GetMapping("/show/{s}/{key}")
    @NotWriteLogAnno(exclude = false,all = false,printResultLog = false,paramsExclude = "response")
    public void show(@PathVariable String key,@PathVariable String s, HttpServletResponse response){
        String format = StrUtil.format("{}/{}/{}", fileDir,s, key);
        IoUtil.write(response.getOutputStream(),true,FileUtil.readBytes(format));
    }
}
