package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.cxygzl.biz.service.IFileService;
import com.cxygzl.common.dto.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-11-02 09:54
 */
@Component
public class FileServiceImpl implements IFileService {
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
     * 保存文件
     *
     * @param fileName
     * @return
     */
    @Override
    public R<String> save(byte[] data, String fileName) {

        String loginIdAsLong = StpUtil.getLoginIdAsString();


        String format = StrUtil.format("{}-{}", IdUtil.fastSimpleUUID(), fileName);

        //日期路径
        String s = DateUtil.formatDate(new DateTime());
        {
            String formatted = StrUtil.format("{}/{}", fileDir, s);
            if(!FileUtil.exist(formatted)){
                FileUtil.mkdir(formatted);
            }
        }


        FileUtil.writeBytes(data, StrUtil.format("{}/{}/{}",fileDir,s, format));

        return R.success(StrUtil.format("{}/{}/{}",fileShowUrl,s,format));
    }
}
