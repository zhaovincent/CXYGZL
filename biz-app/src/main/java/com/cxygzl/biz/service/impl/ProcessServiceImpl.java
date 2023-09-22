package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxygzl.biz.api.ApiStrategyFactory;
import com.cxygzl.biz.entity.Process;
import com.cxygzl.biz.entity.ProcessInstanceRecord;
import com.cxygzl.biz.entity.ProcessStarter;
import com.cxygzl.biz.entity.ProcessSubProcess;
import com.cxygzl.biz.form.FormStrategyFactory;
import com.cxygzl.biz.mapper.ProcessMapper;
import com.cxygzl.biz.service.IProcessInstanceRecordService;
import com.cxygzl.biz.service.IProcessService;
import com.cxygzl.biz.service.IProcessStarterService;
import com.cxygzl.biz.service.IProcessSubProcessService;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.biz.vo.ProcessDataQueryVO;
import com.cxygzl.biz.vo.ProcessVO;
import com.cxygzl.common.constants.FormTypeEnum;
import com.cxygzl.common.constants.NodeUserTypeEnum;
import com.cxygzl.common.constants.ProcessInstanceConstant;
import com.cxygzl.common.dto.FlowSettingDto;
import com.cxygzl.common.dto.R;
import com.cxygzl.common.dto.flow.FormItemVO;
import com.cxygzl.common.dto.flow.Node;
import com.cxygzl.common.dto.flow.NodeUser;
import com.cxygzl.common.dto.third.CreateProcessDto;
import com.cxygzl.common.dto.third.DeptDto;
import com.cxygzl.common.dto.third.UserDto;
import com.cxygzl.common.utils.CommonUtil;
import com.cxygzl.common.utils.NodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.anyline.entity.DataRow;
import org.anyline.entity.DataSet;
import org.anyline.entity.DefaultPageNavi;
import org.anyline.entity.PageNavi;
import org.anyline.metadata.Table;
import org.anyline.service.AnylineService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-25
 */
@Slf4j
@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements IProcessService {
    @Resource
    private IProcessStarterService processStarterService;
    @Resource
    private IProcessSubProcessService processSubProcessService;

    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;


    @Resource
    public AnylineService anylineService;

    /**
     * 获取详细数据
     *
     * @param flowId
     * @param handleForm
     * @return
     */
    @Override
    public R<ProcessVO> getDetail(String flowId, boolean handleForm) {
        ProcessVO processVO = this.getProcessVO(flowId, handleForm);
        return R.success(processVO);
    }

    private ProcessVO getProcessVO(String flowId, boolean handleForm) {
        Process oaForms = getByFlowId(flowId);
        String process = oaForms.getProcess();
        String formItems = oaForms.getFormItems();
        Node startNode = CommonUtil.toObj(process, Node.class);


        Map<String, String> formPerms = startNode.getFormPerms();
        List<FormItemVO> formItemVOList = JSON.parseArray(formItems, FormItemVO.class);

        for (FormItemVO formItemVO : formItemVOList) {
            String perm = MapUtil.getStr(formPerms, formItemVO.getId(), ProcessInstanceConstant.FormPermClass.EDIT);
            formItemVO.setPerm(perm);

            FormItemVO.Props formItemVOProps = formItemVO.getProps();
            if (StrUtil.equals(formItemVO.getType(), FormTypeEnum.LAYOUT.getType())) {
                //明细
                Object value = formItemVOProps.getValue();
                List<FormItemVO> subList = Convert.toList(FormItemVO.class, value);
                for (FormItemVO itemVO : subList) {
                    String perm1 = MapUtil.getStr(formPerms, itemVO.getId(), ProcessInstanceConstant.FormPermClass.EDIT);
                    itemVO.setPerm(perm1);
                    if (handleForm) {
                        handleForm(itemVO);

                    }
                }


                formItemVOProps.setValue(subList);


            }
            if (handleForm) {

                handleForm(formItemVO);
            }

        }
        oaForms.setFormItems(CommonUtil.toJson(formItemVOList));


        List<String> selectUserNodeId = NodeUtil.selectUserNodeId(startNode);

        ProcessVO processVO = BeanUtil.copyProperties(oaForms, ProcessVO.class);
        processVO.setSelectUserNodeId(selectUserNodeId);

        //发起人范围
        List<ProcessStarter> processStarterList = processStarterService.lambdaQuery().eq(ProcessStarter::getProcessId, oaForms.getId()).list();
        List<NodeUser> rangeList = new ArrayList<>();
        for (ProcessStarter processStarter : processStarterList) {
            NodeUser nodeUser = JSON.parseObject(processStarter.getData(), NodeUser.class);
            rangeList.add(nodeUser);
        }
        processVO.setRangeList(rangeList);

        return processVO;
    }

    /**
     * 处理表单
     *
     * @param formItemVO
     */
    private static void handleForm(FormItemVO formItemVO) {
        FormItemVO.Props formItemVOProps = formItemVO.getProps();
        if (StrUtil.equalsAny(formItemVO.getType(), FormTypeEnum.SELECT_USER.getType(), FormTypeEnum.SELECT_MULTI_USER.getType())) {
            Boolean defaultRoot = formItemVOProps.getDefaultRoot();
            if (defaultRoot != null && defaultRoot) {
                //处理默认值
                UserDto user = ApiStrategyFactory.getStrategy().getUser(StpUtil.getLoginIdAsString());
                NodeUser nodeUserDto = new NodeUser();
                nodeUserDto.setId(String.valueOf(user.getId()));
                nodeUserDto.setName(user.getName());
                nodeUserDto.setType(NodeUserTypeEnum.USER.getKey());
                nodeUserDto.setSelected(true);
                nodeUserDto.setAvatar(user.getAvatarUrl());

                formItemVOProps.setValue(CollUtil.newArrayList(nodeUserDto));
            }
        }
        if (StrUtil.equalsAny(formItemVO.getType(), FormTypeEnum.SELECT_DEPT.getType(), FormTypeEnum.SELECT_MULTI_DEPT.getType())) {
            Boolean defaultRoot = formItemVOProps.getDefaultRoot();
            if (defaultRoot != null && defaultRoot) {
                //处理默认值
                UserDto user = ApiStrategyFactory.getStrategy().getUser(StpUtil.getLoginIdAsString());
                String deptId = user.getDeptId();
                DeptDto dept = ApiStrategyFactory.getStrategy().getDept(deptId);
                NodeUser nodeUserDto = new NodeUser();
                nodeUserDto.setId(String.valueOf(dept.getId()));
                nodeUserDto.setName(dept.getName());
                nodeUserDto.setType(NodeUserTypeEnum.DEPT.getKey());
                nodeUserDto.setSelected(true);

                formItemVOProps.setValue(CollUtil.newArrayList(nodeUserDto));
            }
        }
    }


    @Override
    public Process getByFlowId(String flowId) {
        return this.lambdaQuery().eq(Process::getFlowId, flowId).one();
    }

    @Override
    public void updateByFlowId(Process process) {
        this.lambdaUpdate().eq(Process::getFlowId, process.getFlowId()).update(process);
    }

    @Override
    public void hide(String flowId) {
        this.lambdaUpdate().set(Process::getHidden, true).eq(Process::getFlowId, flowId).update(new Process());
    }

    /**
     * 创建流程
     *
     * @param processVO
     * @return
     */
    @Override
    public R create(ProcessVO processVO) {

        String uniqueId = "";

        {
            //名字唯一
            String name = processVO.getName();
            List<Process> processList = this.lambdaQuery()
                    .eq(Process::getName, name)
                    .eq(Process::getHidden, false)
                    .list();
            if (StrUtil.isNotBlank(processVO.getFlowId())) {
                Process process = this.getByFlowId(processVO.getFlowId());
                uniqueId = process.getUniqueId();

                String finalUniqueId = uniqueId;

                long count = processList.stream().filter(w -> !StrUtil.equals(w.getUniqueId(), finalUniqueId)).count();
                if (count > 0) {
                    return R.fail("流程名字已存在，不能重复");
                }
            } else {
                if (!processList.isEmpty()) {
                    return R.fail("流程名字已存在，不能重复");

                }
            }
        }

        String processStr = processVO.getProcess();

        Node node = JSON.parseObject(processStr, Node.class);
        NodeUtil.handleParentId(node, null);
        com.cxygzl.biz.utils.NodeUtil.handleStarterNode(node, JSON.parseArray(processVO.getFormItems(), FormItemVO.class));
        com.cxygzl.biz.utils.NodeUtil.handleApproveForm(node, JSON.parseArray(processVO.getFormItems(), FormItemVO.class));
        com.cxygzl.biz.utils.NodeUtil.handleApprove(node);


        com.cxygzl.common.dto.R<String> r = CoreHttpUtil.createFlow(node, StpUtil.getLoginIdAsString(), processVO.getName());
        if (!r.isOk()) {
            return com.cxygzl.common.dto.R.fail(r.getMsg());
        }
        String flowId = r.getData();

        //判断是否有子流程 保存子流程
        List<String> subProcessIdList = NodeUtil.selectSubProcessId(node);
        for (String subFlowId : subProcessIdList) {
            ProcessSubProcess entity = new ProcessSubProcess();
            entity.setFlowId(flowId);
            entity.setSubFlowId(subFlowId);
            processSubProcessService.save(entity);
        }


        NodeUser nodeUser = CommonUtil.toArray(processVO.getAdmin(), NodeUser.class).get(0);

        if (StrUtil.isNotBlank(processVO.getFlowId())) {

            Process oldProcess = this.getByFlowId(processVO.getFlowId());
            this.hide(processVO.getFlowId());
            //修改所有的管理员
            this.lambdaUpdate().set(Process::getAdminId, nodeUser.getId()).eq(Process::getUniqueId,
                    oldProcess.getUniqueId()).update(new Process());

        }


        //发起人范围
        List<NodeUser> nodeUserList = processVO.getRangeList();

        StringBuilder stringBuilder = new StringBuilder("");
        if (CollUtil.isNotEmpty(nodeUserList)) {
            int index = 0;

            for (NodeUser user : nodeUserList) {
                if (index > 0) {
                    stringBuilder.append(",");
                }
                stringBuilder.append(user.getName());
                index++;
                if (index > 5) {
                    break;
                }

            }
        }

        Process p = new Process();
        p.setFlowId(flowId);
        p.setName(processVO.getName());
        p.setLogo(processVO.getLogo());
        p.setSettings(processVO.getSettings());
        p.setGroupId(processVO.getGroupId());
        p.setFormItems(processVO.getFormItems());
        p.setProcess(JSON.toJSONString(node));
        p.setRemark(processVO.getRemark());
        p.setSort(0);
        p.setHidden(false);
        p.setStop(false);
        p.setAdminId(nodeUser.getId());
        p.setUniqueId(StrUtil.isBlank(uniqueId) ? IdUtil.fastSimpleUUID() : uniqueId);
        p.setAdmin(processVO.getAdmin());
        p.setRangeShow(stringBuilder.toString());


        this.save(p);

        //保存范围

        if (CollUtil.isNotEmpty(nodeUserList)) {
            for (NodeUser nodeUserDto : nodeUserList) {
                ProcessStarter processStarter = new ProcessStarter();

                processStarter.setProcessId(p.getId());
                processStarter.setTypeId((nodeUserDto.getId()));
                processStarter.setType(nodeUserDto.getType());
                processStarter.setData(JSON.toJSONString(nodeUserDto));
                processStarterService.save(processStarter);

            }
        }

        //创建第三方对接流程
        ApiStrategyFactory.getStrategy().createProcess(CreateProcessDto.builder().oriFlowId(processVO.getFlowId()).flowId(flowId).name(processVO.getName()).description(processVO.getRemark()).formItemVOList(JSON.parseArray(processVO.getFormItems(), FormItemVO.class)).build());

        //建立数据库表
        {
            if (StrUtil.isNotBlank(processVO.getFlowId())) {
                Process process = this.getByFlowId(processVO.getFlowId());
                FlowSettingDto flowSettingDto = JSON.parseObject(process.getSettings(), FlowSettingDto.class);
                FlowSettingDto.DbRecord dbRecord = flowSettingDto.getDbRecord();
                if (dbRecord == null || !dbRecord.getEnable()) {
                    handleDbTable(processVO, p.getUniqueId(), false);
                } else {
                    handleDbTable(processVO, p.getUniqueId(), true);

                }
            } else {
                handleDbTable(processVO, p.getUniqueId(), false);
            }
        }
        return com.cxygzl.common.dto.R.success();
    }

    /**
     * 创建数据库表--记录数据
     *
     * @param processVO
     * @param uniqueId
     */
    private void handleDbTable(ProcessVO processVO, String uniqueId, boolean oldEnable) {
        try {
            FlowSettingDto flowSettingDto = JSON.parseObject(processVO.getSettings(), FlowSettingDto.class);
            FlowSettingDto.DbRecord dbRecord = flowSettingDto.getDbRecord();
            if (dbRecord == null || !dbRecord.getEnable()) {
                if (oldEnable) {
                    log.info("删除数据库表：tb_{}", uniqueId);
                    Table table = anylineService.metadata().table(StrUtil.format("tb_{}", uniqueId), false); //false表示不加载表结构，只简单查询表名
                    anylineService.ddl().drop(table);
                }

                return;
            }


            //根据不同数据库长度精度有可能忽略
            List<FormItemVO> formItemVOS = JSON.parseArray(processVO.getFormItems(), FormItemVO.class);
            Table table = FormStrategyFactory.buildDDLSql(formItemVOS, uniqueId, processVO.getName());
            anylineService.ddl().create(table);
        } catch (Exception e) {
            log.error("Error", e);
        }
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

        return com.cxygzl.common.dto.R.success();
    }

    /**
     * 查询数据列表
     *
     * @param pageDto
     * @return
     */
    @Override
    public R queryDataList(ProcessDataQueryVO pageDto) {

        Map dataMap = new HashMap();

        Process process = getByFlowId(pageDto.getFlowId());

        String formItems = process.getFormItems();
        List<FormItemVO> formItemVOList = JSON.parseArray(formItems, FormItemVO.class);
        {
            List headList = new ArrayList();
            {
                headList.add(Dict.create()
                        .set("id", "index")
                        .set("name", "序号")
                        .set("type", "index")
                );
            }
            {
                headList.add(Dict.create()
                        .set("id", "startUserName")
                        .set("name", "发起人")
                        .set("type", "index")
                );
            }
            {
                headList.add(Dict.create()
                        .set("id", "startTime")
                        .set("name", "发起时间")
                        .set("type", "index")
                );
            }
            for (FormItemVO formItemVO : formItemVOList) {
                Dict set = Dict.create()
                        .set("id", formItemVO.getId())
                        .set("name", formItemVO.getName())
                        .set("type", formItemVO.getType());
                headList.add(set);
            }
            dataMap.put("headList", headList);
        }

        String uniqueId = process.getUniqueId();
        PageNavi navi = new DefaultPageNavi(pageDto.getPageNum(), pageDto.getPageSize());


        DataSet dataSet = anylineService.querys(StrUtil.format("tb_{}", uniqueId), navi,"order by create_time desc");
        List<DataRow> rows = dataSet.getRows();

        List records = new ArrayList();
        int index = 1;
        for (DataRow row : rows) {
            Dict dict = Dict.create();
            dict.set("index", (pageDto.getPageNum() - 1) * pageDto.getPageSize() + index);
            for (FormItemVO formItemVO : formItemVOList) {
                dict.set(formItemVO.getId(), row.get(formItemVO.getId()));
                if (formItemVO.getType().equals(FormTypeEnum.LAYOUT.getType())) {
                    //明细
                    Object value = formItemVO.getProps().getValue();
                    List<FormItemVO> subFormItemVoList = Convert.toList(FormItemVO.class, value);
                    List headList = new ArrayList();
                    {
                        headList.add(Dict.create()
                                .set("id", "index")
                                .set("name", "序号")
                                .set("type", "index")
                        );
                    }
                    for (FormItemVO formItemVOSub : subFormItemVoList) {
                        Dict set = Dict.create()
                                .set("id", formItemVOSub.getId())
                                .set("name", formItemVOSub.getName())
                                .set("type", formItemVOSub.getType());
                        headList.add(set);
                    }
                    Object v = row.get(formItemVO.getId());
                    JSONArray jsonArray = v == null ? new JSONArray() :
                            (v instanceof String ? JSON.parseArray(v.toString()) : JSON.parseArray(JSON.toJSONString(v)));

                    int subIndex = 1;
                    for (Object o : jsonArray) {
                        JSONObject jsonObject = (JSONObject) o;
                        jsonObject.put("index", subIndex);
                        for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                            String key = entry.getKey();
                            Object value1 = entry.getValue();
                            if (value1 == null || value1 instanceof String) {
                                continue;
                            }
                            jsonObject.put(key, JSON.toJSONString(value1));
                        }
                        subIndex++;
                    }

                    Dict set = Dict.create().set("headList", headList).set("dataList", jsonArray);
                    dict.set(formItemVO.getId(), set);


                }
            }
            String processInstanceId = row.getString("process_instance_id");
            ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();
            dict.set("startTime", DateUtil.formatDateTime(processInstanceRecord.getCreateTime()));
            UserDto user = ApiStrategyFactory.getStrategy().getUser(processInstanceRecord.getUserId());
            dict.set("startUserName",user.getName());
            records.add(dict);
            index++;
        }
        dataMap.put("records", records);
        dataMap.put("total", dataSet.total());
        return R.success(dataMap);
    }
}
