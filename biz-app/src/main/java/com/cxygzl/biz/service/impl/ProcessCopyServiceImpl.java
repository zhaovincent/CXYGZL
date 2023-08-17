package com.cxygzl.biz.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.ProcessCopy;
import com.cxygzl.biz.entity.ProcessInstanceRecord;
import com.cxygzl.biz.mapper.ProcessCopyMapper;
import com.cxygzl.biz.service.IProcessCopyService;
import com.cxygzl.biz.service.IProcessInstanceRecordService;
import com.cxygzl.biz.service.IProcessNodeDataService;
import com.cxygzl.biz.service.IProcessService;
import com.cxygzl.biz.utils.FormUtil;
import com.cxygzl.biz.vo.TaskDetailViewVO;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.FormItemVO;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.third.UserDto;
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
public class ProcessCopyServiceImpl extends ServiceImpl<ProcessCopyMapper, ProcessCopy> implements IProcessCopyService {
    @Resource
    private IProcessService processService;
    @Resource
    private IProcessNodeDataService nodeDataService;
    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;

    /**
     * 查询单个抄送详细信息
     *
     * @param id
     * @return
     */
    @Override
    public R querySingleDetail(long id) {
        ProcessCopy processCopy = this.getById(id);
        if (processCopy == null) {
            return R.fail("流程不存在");
        }
        String flowId = processCopy.getFlowId();
        Process oaForms = processService.getByFlowId(flowId);
        if (oaForms == null) {
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
        for (FormItemVO formItemVO : jsonObjectList) {


            String fid = formItemVO.getId();
            String perm = formPerms.get(fid);
            formItemVO.setPerm(StrUtil.isBlankIfStr(perm) ? ProcessInstanceConstant.FormPermClass.HIDE : perm);


            if (formItemVO.getType().equals(FormTypeEnum.LAYOUT.getType())) {
                //明细

                List<Map<String, Object>> subParamList = MapUtil.get(variableMap, fid, new cn.hutool.core.lang.TypeReference<List<Map<String, Object>>>() {
                });

                Object value = formItemVO.getProps().getValue();

                List<List<FormItemVO>> l = new ArrayList<>();
                for (Map<String, Object> map : subParamList) {
                    List<FormItemVO> subItemList = Convert.toList(FormItemVO.class, value);
                    for (FormItemVO itemVO : subItemList) {
                        Object value1 = map.get(itemVO.getId());


                        FormUtil.handValue(itemVO, value1);


                        String permSub = formPerms.get(itemVO.getId());
                        if (StrUtil.isNotBlank(permSub)) {
                            itemVO.setPerm(ProcessInstanceConstant.FormPermClass.EDIT.equals(permSub) ? ProcessInstanceConstant.FormPermClass.READ : permSub

                            );


                        } else {
                            itemVO.setPerm(ProcessInstanceConstant.FormPermClass.HIDE);
                        }

                    }
                    l.add(subItemList);
                }
                formItemVO.getProps().setValue(l);


            } else {
                Object value = variableMap.get(fid);

                FormUtil.handValue(formItemVO, value);
            }

        }

        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processCopy.getProcessInstanceId()).one();


        UserDto starterUser = ApiStrategyFactory.getStrategy().getUser(processInstanceRecord.getUserId());

        TaskDetailViewVO taskDetailViewVO = TaskDetailViewVO.builder()
                .formItems(jsonObjectList)
                .processName(oaForms.getName())
                .starterAvatarUrl(starterUser.getAvatarUrl())
                .starterName(starterUser.getName())
                .startTime(processInstanceRecord.getCreateTime())
                .processInstanceStatus(processInstanceRecord.getStatus())
                .processInstanceResult(processInstanceRecord.getResult())
                .build();


        return R.success(taskDetailViewVO);
    }
}
