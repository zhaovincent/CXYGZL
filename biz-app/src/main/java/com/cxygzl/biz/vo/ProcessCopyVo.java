package com.cxygzl.biz.vo;

import cn.hutool.core.lang.Dict;
import com.cxygzl.biz.entity.ProcessCopy;
import lombok.Data;

import java.util.List;

@Data
public class ProcessCopyVo extends ProcessCopy {

    private String startUserName;
    /**
     *  表单值显示
     */
    private List<Dict> formValueShowList;

    private Integer processInstanceResult;
}
