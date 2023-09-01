package com.cxygzl.core.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-09-01 11:24
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskCommentDto {

    private String content;
    private String title;
    private String userId;
    private Boolean sys;

}
