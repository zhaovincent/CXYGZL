package com.cxygzl.biz.service.impl;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.ProcessCopy;
import com.cxygzl.biz.mapper.ProcessCopyMapper;
import com.cxygzl.biz.service.IProcessCopyService;
import com.cxygzl.biz.service.IProcessNodeDataService;
import com.cxygzl.biz.service.IProcessService;
import com.cxygzl.biz.vo.FormItemVO;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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
    @Resource
    private IProcessNodeDataService nodeDataService;

    /**
     * 查询单个抄送详细信息
     *
     * @param id
     * @return
     */
    @Override
    public Object querySingleDetail(long id) {
        ProcessCopy processCopy = this.getById(id);
        String flowId = processCopy.getFlowId();
        Process oaForms = processService.getByFlowId(flowId);
        if(oaForms==null){
            return R.fail("流程不存在");
        }
        String formData = processCopy.getFormData();

        Map<String, Object> variableMap = JSON.parseObject(formData, new TypeReference<Map<String, Object>>() {
        });

        String nodeId = processCopy.getNodeId();


        String data = nodeDataService.getNodeData(flowId, nodeId).getData();
        Node node = JSON.parseObject(data, Node.class);
        Map<String, String> formPerms = node.getFormPerms();


        List<FormItemVO> jsonObjectList = JSON.parseArray(oaForms.getFormItems(), FormItemVO.class);
        for (FormItemVO jsonObject : jsonObjectList) {
            String fid = jsonObject.getId();
            String perm = formPerms.get(fid);
            jsonObject.setPerm(StrUtil.isBlankIfStr(perm)? ProcessInstanceConstant.FormPermClass.HIDE:perm);
            jsonObject.getProps().setValue(variableMap.get(fid));


        }
        Dict set = Dict.create()
                .set("formItems", jsonObjectList)
                ;

        return R.success(set);
    }
}
