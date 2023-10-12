package com.cxygzl.biz.vo;

import com.cxygzl.common.dto.SimpleApproveDescDto;
import lombok.Data;

import java.util.List;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-10-12 09:47
 */
@Data
public class PrintDataResultVO {

    private Integer processInstanceResult;
    private Integer processStatus;
    private String processInstanceId;
    private String processStatusShow;
    private String starterName;
    private String starterDeptName;
    private String processName;
    private String startTime;
    private List<Form> formList;
    private List<Approve> approveList;

    @Data
    public static class Form{
        private String formName;
        private String formType;
        private Object formValue;
        private String formValueShow;
    }
    @Data
    public static class Approve{
        private String userName;
        private String nodeName;
        private String taskType;
        private String taskTypeShow;
        private String date;
        private List<SimpleApproveDescDto> comment;
    }

}
