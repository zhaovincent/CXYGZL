package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.ProcessStarter;
import com.cxygzl.biz.mapper.ProcessMapper;
import com.cxygzl.biz.service.IProcessService;
import com.cxygzl.biz.service.IProcessStarterService;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.biz.vo.FormItemVO;
import com.cxygzl.biz.vo.ProcessVO;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.utils.CommonUtil;
import com.cxygzl.common.utils.NodeUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-25
 */
@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements IProcessService {
    @Resource
    private IProcessStarterService processStarterService;
    /**
     * 获取详细数据
     *
     * @param flowId
     * @return
     */
    @Override
    public R<ProcessVO> getDetail(String flowId) {
        ProcessVO processVO = this.getProcessVO(flowId);
        return R.success(processVO);
    }

    private ProcessVO getProcessVO(String flowId) {
        Process oaForms = getByFlowId(flowId);
        String process = oaForms.getProcess();
        String formItems = oaForms.getFormItems();
        Node startNode = CommonUtil.toObj(process, Node.class);


        Map<String, String> formPerms = startNode.getFormPerms();
        List<FormItemVO> formItemVOList = JSON.parseArray(formItems, FormItemVO.class);

        for (FormItemVO formItemVO : formItemVOList) {
            String perm = MapUtil.getStr(formPerms, formItemVO.getId(), ProcessInstanceConstant.FormPermClass.EDIT);
            formItemVO.setPerm(perm);

            if(StrUtil.equals(formItemVO.getType(), FormTypeEnum.LAYOUT.getType())){
                //明细
                Object value = formItemVO.getProps().getValue();
                List<FormItemVO> subList = Convert.toList(FormItemVO.class, value);
                for (FormItemVO itemVO : subList) {
                    String perm1 = MapUtil.getStr(formPerms, itemVO.getId(), ProcessInstanceConstant.FormPermClass.EDIT);
                    itemVO.setPerm(perm1);
                }


                formItemVO.getProps().setValue(subList);
                formItemVO.getProps();

            }

        }
        oaForms.setFormItems(CommonUtil.toJson(formItemVOList));


        List<String> selectUserNodeId = NodeUtil.selectUserNodeId(startNode);

        ProcessVO processVO = BeanUtil.copyProperties(oaForms, ProcessVO.class);
        processVO.setSelectUserNodeId(selectUserNodeId);

        return processVO;
    }


    @Override
    public Process getByFlowId(String flowId) {
        return this.lambdaQuery().eq(Process::getFlowId, flowId).one();
    }

    @Override
    public void updateByFlowId(Process process) {
        this.lambdaUpdate().eq(Process::getFlowId,process.getFlowId()).update(process);
    }

    @Override
    public void hide(String flowId) {
        this.lambdaUpdate().set(Process::getHidden,true).eq(Process::getFlowId,flowId).update(new Process());
    }

    /**
     * 创建流程
     *
     * @param process
     * @return
     */
    @Override
    public R create(Process process) {

        String processStr = process.getProcess();

        R<String> r = CoreHttpUtil.createFlow(JSON.parseObject(processStr), StpUtil.getLoginIdAsLong());
        if (!r.isOk()) {
            return R.fail(r.getMsg());
        }
        String flowId = r.getData();


        NodeUser nodeUser = CommonUtil.toArray(process.getAdmin(), NodeUser.class).get(0);

        if (StrUtil.isNotBlank(process.getFlowId())) {

            Process oldProcess = this.getByFlowId(process.getFlowId());
            this.hide(process.getFlowId());
            //修改所有的管理员
            this.lambdaUpdate().set(Process::getAdminId, nodeUser.getId()).eq(Process::getUniqueId,
                    oldProcess.getUniqueId()).update(new Process());

        }

        Node startNode = CommonUtil.toObj(processStr, Node.class);


        List<NodeUser> nodeUserList = startNode.getNodeUserList();

        StringBuilder stringBuilder=new StringBuilder("");
        if(CollUtil.isNotEmpty(nodeUserList)){
            int index=0;

            for (NodeUser user : nodeUserList) {
                if(index>0){
                    stringBuilder.append(",");
                }
                stringBuilder.append(user.getName());
                index++;
                if(index>5){
                    break;
                }

            }
        }

        Process p = new Process();
        p.setFlowId(flowId);
        p.setName(process.getName());
        p.setLogo(process.getLogo());
        p.setSettings(process.getSettings());
        p.setGroupId(process.getGroupId());
        p.setFormItems(process.getFormItems());
        p.setProcess(processStr);
        p.setRemark(process.getRemark());
        p.setSort(0);
        p.setHidden(false);
        p.setStop(false);
        p.setAdminId(Long.valueOf(nodeUser.getId()));
        p.setUniqueId(IdUtil.fastSimpleUUID());
        p.setAdmin(process.getAdmin());
        p.setRangeShow(stringBuilder.toString());




        this.save(p);

        //保存范围

        for (NodeUser nodeUserDto : nodeUserList) {
            ProcessStarter processStarter = new ProcessStarter();

            processStarter.setProcessId(p.getId());
            processStarter.setTypeId(Long.valueOf(nodeUserDto.getId()));
            processStarter.setType(nodeUserDto.getType());
            processStarterService.save(processStarter);

        }


        return R.success();
    }

    /**
     * 编辑表单
     *
     * @param flowId  摸板ID
     * @param type    类型 stop using delete
     * @param groupId
     * @return 操作结果
     */
    @Override
    public R update(String flowId, String type, Long groupId) {
        Process process = new Process();
        process.setFlowId(flowId);
        process.setStop("stop".equals(type));
        process.setHidden("delete".equals(type));
        process.setGroupId(groupId);



        this.updateByFlowId(process);

        return R.success();
    }
}
