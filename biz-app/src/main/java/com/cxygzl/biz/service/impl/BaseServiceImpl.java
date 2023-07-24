package com.cxygzl.biz.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import com.alibaba.fastjson.JSON;
import com.cxygzl.biz.entity.ProcessCopy;
import com.cxygzl.biz.entity.ProcessInstanceRecord;
import com.cxygzl.biz.service.IBaseService;
import com.cxygzl.biz.service.IProcessCopyService;
import com.cxygzl.biz.service.IProcessInstanceRecordService;
import com.cxygzl.biz.utils.CoreHttpUtil;
import com.cxygzl.common.dto.IndexPageStatistics;
import com.cxygzl.common.dto.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@Slf4j
public class BaseServiceImpl implements IBaseService {

    @Resource
    private IProcessCopyService processCopyService;
    @Resource
    private IProcessInstanceRecordService processInstanceRecordService;

    /**
     * 首页数据
     *
     * @return
     */
    @Override
    public R index() {

        String userId = StpUtil.getLoginIdAsString();


        Long coypNum = processCopyService.lambdaQuery().eq(ProcessCopy::getUserId, userId).count();

        Long startendNum = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getUserId, userId).count();

        IndexPageStatistics indexPageStatistics = CoreHttpUtil.querySimpleData(userId).getData();
        indexPageStatistics.setCopyNum(coypNum);
        indexPageStatistics.setStartedNum(startendNum);

        return R.success(indexPageStatistics);
    }

    /**
     * 获取所有地区数据
     *
     * @return
     */
    @Override
    public R areaList() {

        ClassPathResource classPathResource = new ClassPathResource("area.json");
        String json = FileUtil.readUtf8String(classPathResource.getFile());
//
//        List<AreaVO> areaVOList = JSON.parseArray(json, AreaVO.class);
//        List<AreaVO> provinceList = areaVOList.stream().filter(w -> StrUtil.endWith(w.getCode(), "0000")).collect(Collectors.toList());
//        List<AreaVO> cityList =
//                areaVOList.stream()
//                        .filter(w -> !StrUtil.endWith(w.getCode(), "0000"))
//                        .filter(w -> StrUtil.endWith(w.getCode(), "00"))
//                        .collect(Collectors.toList());
//        List<AreaVO> areaList =
//                areaVOList.stream()
//                        .filter(w -> !StrUtil.endWith(w.getCode(), "0000"))
//                        .filter(w -> !StrUtil.endWith(w.getCode(), "00"))
//                        .collect(Collectors.toList());
//        provinceList.forEach(res -> res.setParentCode("000000"));
//        cityList.forEach(res -> res.setParentCode(res.getProvince() + "0000"));
//        areaList.forEach(res -> res.setParentCode(res.getProvince() + res.getCity() + "00"));
//
//        List<AreaVO> list = new ArrayList<>();
//        list.addAll(provinceList);
//        list.addAll(cityList);
//        list.addAll(areaList);
//        List<TreeNode<String>> nodeList = CollUtil.newArrayList();
//        for (AreaVO areaVO : list) {
//
//            TreeNode<String> treeNode = new TreeNode<>(areaVO.getCode(), areaVO.getParentCode(),
//                    areaVO.getName(), "000000");
//            nodeList.add(treeNode);
//        }
//        List<Tree<String>> treeList = TreeUtil.build(nodeList, "000000");


        return R.success(JSON.parseArray(json));
    }
}
