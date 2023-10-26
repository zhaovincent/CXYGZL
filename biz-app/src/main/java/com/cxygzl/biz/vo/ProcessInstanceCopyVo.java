package com.cxygzl.biz.vo;

import cn.hutool.core.lang.Dict;
import com.cxygzl.biz.entity.ProcessInstanceCopy;
import lombok.Data;

import java.util.List;

@Data
public class ProcessInstanceCopyVo extends ProcessInstanceCopy {

    private String startUserName;
    /**
     *  表单值显示
     */
    private List<Dict> formValueShowList;

    private Integer processInstanceResult;
    private Integer processInstanceStatus;
}
