package com.cxygzl.biz.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import lombok.SneakyThrows;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;


@Controller
public class WebController {

    @RequestMapping("/")
    @SneakyThrows
    public void index(HttpServletResponse response){
        ClassPathResource classPathResource=new ClassPathResource("web/index.html");

        String s = FileUtil.readUtf8String(classPathResource.getPath());

        IoUtil.writeUtf8(response.getOutputStream(),true,s);
    }


    @RequestMapping("{a}")
    @SneakyThrows
    public void f(HttpServletResponse response,@PathVariable String a){


        ClassPathResource classPathResource=new ClassPathResource("web/"+a);

        byte[] s = FileUtil.readBytes(classPathResource.getPath());

        IoUtil.write(response.getOutputStream(),true,s);
    }

    @RequestMapping("{js}/{a}")
    @SneakyThrows
    public void jscss(HttpServletResponse response,@PathVariable String js, @PathVariable String a){

        ClassPathResource classPathResource=new ClassPathResource("web/"+js+"/"+a);

        byte[] s = FileUtil.readBytes(classPathResource.getPath());

        IoUtil.write(response.getOutputStream(),true,s);
    }

}
