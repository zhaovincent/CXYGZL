package com.cxygzl.biz.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.common.config.NotWriteLogAnno;
import com.cxygzl.common.dto.R;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

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

    @PostConstruct
    public void init(){
        File file = new File(fileDir);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    /**
     * 上传文件
     * @param file
     * @return
     */
    @SneakyThrows
    @PostMapping("upload")
    @NotWriteLogAnno(exclude = false,all = true)
    public Object upload(MultipartFile file){

        String loginIdAsLong = StpUtil.getLoginIdAsString();


        String originalFilename = file.getOriginalFilename();
        long size = file.getSize();

        String format = StrUtil.format("{}-{}", IdUtil.fastSimpleUUID(), originalFilename);

        //日期路径
        String s = DateUtil.formatDate(new DateTime());
        {
            String formatted = StrUtil.format("{}/{}", fileDir, s);
            if(!FileUtil.exist(formatted)){
                FileUtil.mkdir(formatted);
            }
        }


        FileUtil.writeBytes(file.getBytes(), StrUtil.format("{}/{}/{}",fileDir,s, format));

        return R.success(StrUtil.format("{}/{}/{}",fileShowUrl,s,format));
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
