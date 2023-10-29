package com.cxygzl.common.dto;

import com.cxygzl.common.dto.flow.UploadValue;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SimpleApproveDescDto {

    private Date date;

    private String message;
    private String msgId;
    private String userId;
    private String type;
    private Boolean sys;


    private List<UploadValue> approveFileList;
    private List<UploadValue> approveImageList;

}
