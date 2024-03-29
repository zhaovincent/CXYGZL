package com.cxygzl.biz.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.ProcessInstanceCopy;
import com.cxygzl.biz.mapper.ProcessInstanceCopyMapper;
import com.cxygzl.biz.service.IProcessInstanceCopyService;
import com.cxygzl.biz.service.IProcessNodeDataService;
import com.cxygzl.biz.service.IProcessService;
import com.cxygzl.biz.vo.FormItemVO;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
public class ProcessInstanceCopyServiceImpl extends ServiceImpl<ProcessInstanceCopyMapper, ProcessInstanceCopy> implements IProcessInstanceCopyService {
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
        ProcessInstanceCopy processInstanceCopy = this.getById(id);
        String flowId = processInstanceCopy.getFlowId();
        Process oaForms = processService.getByFlowId(flowId);
        if(oaForms==null){
            return R.fail("流程不存在");
        }
        String formData = processInstanceCopy.getFormData();

        Map<String, Object> variableMap = JSON.parseObject(formData, new TypeReference<Map<String, Object>>() {
        });

        String nodeId = processInstanceCopy.getNodeId();


        String data = nodeDataService.getNodeData(flowId, nodeId).getData();
        Node node = JSON.parseObject(data, Node.class);
        Map<String, String> formPerms = node.getFormPerms();


        List<FormItemVO> jsonObjectList = JSON.parseArray(oaForms.getFormItems(), FormItemVO.class);
        for (FormItemVO formItemVO : jsonObjectList) {
            String fid = formItemVO.getId();
            String perm = formPerms.get(fid);
            formItemVO.setPerm(StrUtil.isBlankIfStr(perm)? ProcessInstanceConstant.FormPermClass.HIDE:perm);




            if(formItemVO.getType().equals(FormTypeEnum.LAYOUT.getType())){
                //明细

                List<Map<String, Object>> subParamList = MapUtil.get(variableMap, fid, new cn.hutool.core.lang.TypeReference<List<Map<String, Object>>>() {
                });

                Object value = formItemVO.getProps().getValue();

                List<List<FormItemVO>> l=new ArrayList<>();
                for (Map<String, Object> map : subParamList) {
                    List<FormItemVO> subItemList = Convert.toList(FormItemVO.class, value);
                    for (FormItemVO itemVO : subItemList) {
                        itemVO.getProps().setValue(map.get(itemVO.getId()));

                        String permSub = formPerms.get(itemVO.getId());
                        if (StrUtil.isNotBlank(permSub)) {
                            itemVO.setPerm(ProcessInstanceConstant.FormPermClass.EDIT.equals(permSub)?ProcessInstanceConstant.FormPermClass.READ:permSub

                            );


                        }else{
                            itemVO.setPerm(ProcessInstanceConstant.FormPermClass.HIDE);
                        }

                    }
                    l.add(subItemList);
                }
                formItemVO.getProps().setValue(l);


            }else{
                formItemVO.getProps().setValue(variableMap.get(fid));

            }

        }
        Dict set = Dict.create()
                .set("formItems", jsonObjectList)
                ;

        return R.success(set);
    }
}
