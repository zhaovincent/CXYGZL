package com.cxygzl.biz.service.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.cxygzl.biz.service.IFormService;
import com.cxygzl.biz.vo.FormRemoteSelectOptionParamVo;
import com.cxygzl.common.dto.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FormServiceImpl implements IFormService {
    /**
     * 远程请求下拉选项
     *
     * @param formRemoteSelectOptionParamVo
     * @return
     */
    @Override
    public Object selectOptions(FormRemoteSelectOptionParamVo formRemoteSelectOptionParamVo) {

        String remoteUrl = formRemoteSelectOptionParamVo.getRemoteUrl();
        String s = HttpUtil.post(remoteUrl,"");
        List<Map> mapList = JSON.parseArray(s, Map.class);

        for (Map map : mapList) {
            String str = MapUtil.getStr(map, "key");
            map.put("key",str);
        }

        return R.success(mapList);
    }
}
