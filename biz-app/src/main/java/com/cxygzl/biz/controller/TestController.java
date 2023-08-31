package com.cxygzl.biz.controller;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import com.cxygzl.common.dto.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("test")
public class TestController {



    @PostMapping("check")
    public Object check(@RequestBody String s,@RequestHeader(required = false) String b){
        log.info("收到前置检查：{} {}",s,b);
        return R.success("参数校验失败");
    }


    @PostMapping("notify")
    public Object notify(@RequestBody String s,@RequestHeader(required = false) String b){
        log.info("收到通知：{} {}",s,b);
        return Dict.create().set("a","123");
    }

    @PostMapping("trigger")
    public Object trigger(@RequestBody String s){
        log.info("收到触发器：{}",s);

      return  Dict.create().set("ok",true).set("data",Dict.create().set("rty", IdUtil.fastSimpleUUID()));
    }


    @PostMapping("test1")
    public Object test1(@RequestBody JSONObject jsonObject){
        Dict set = Dict.create().set("ok", true).set("data", Dict.create().set("a", 12));
        return set;
    }

    @GetMapping("getTestSelectOptions")
    public Object getTestSelectOptions(){


        List list =new ArrayList();

        {
            Dict set = Dict.create().set("key", 1).set("value", "aaaa");
            list.add(set);
        }

        {
            Dict set = Dict.create().set("key", "2").set("value", "bbbb");
            list.add(set);
        }
        return list;

    }

}
