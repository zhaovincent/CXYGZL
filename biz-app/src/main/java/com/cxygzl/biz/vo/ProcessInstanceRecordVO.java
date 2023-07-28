package com.cxygzl.biz.vo;

import cn.hutool.core.lang.Dict;
import com.cxygzl.biz.entity.ProcessInstanceRecord;
import lombok.Data;

import java.util.List;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-07-28 15:23
 */
@Data
public class ProcessInstanceRecordVO extends ProcessInstanceRecord {

    /**
     *  表单值显示
     */
    private List<Dict> formValueShowList;
}
