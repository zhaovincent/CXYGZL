package com.cxygzl.biz.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.ProcessCopy;
import com.cxygzl.biz.mapper.ProcessCopyMapper;
import com.cxygzl.biz.service.IProcessCopyService;
import com.cxygzl.biz.service.IProcessService;
import com.cxygzl.common.dto.process.NodeFormPermDto;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 流程抄送数据 服务实现类
 * </p>
 *
 * @author Vincent
 * @since 2023-05-20
 */
@Service
public class ProcessCopyServiceImpl extends ServiceImpl<ProcessCopyMapper, ProcessCopy> implements IProcessCopyService {
    @Resource
    private IProcessService processService;

    /**
     * 查询单个抄送详细信息
     *
     * @param id
     * @return
     */
    @Override
    public Object querySingleDetail(long id) {
        ProcessCopy processCopy = this.getById(id);
        String formId = processCopy.getProcessId();
        Process oaForms = processService.getByFormId(formId);
        if(oaForms==null){
            return com.cxygzl.biz.utils.R.badRequest("流程不存在");
        }
        String formData = processCopy.getFormData();
        List<NodeFormPermDto> formPerms = JSON.parseArray(formData,NodeFormPermDto.class);


        List<JSONObject> jsonObjectList = JSON.parseArray(oaForms.getFormItems(), JSONObject.class);
        for (JSONObject jsonObject : jsonObjectList) {
            String fid = jsonObject.getString("id");
            NodeFormPermDto nodeFormPermDto =
                    formPerms.stream().filter(w -> StrUtil.equals(fid, w.getId())).findAny().orElse(null);
            if(nodeFormPermDto!=null){
                JSONObject props = jsonObject.getJSONObject("props");
                props.put("perm",nodeFormPermDto.getPerm());
                jsonObject.put("value",nodeFormPermDto.getValue());
            }
        }
        Dict set = Dict.create()
                .set("formItems", jsonObjectList)
                ;

        return com.cxygzl.biz.utils.R.ok(set);
    }
}
