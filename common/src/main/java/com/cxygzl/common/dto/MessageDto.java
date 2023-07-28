package com.cxygzl.common.dto;

import lombok.Data;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-07-25 17:20
 */
@Data
public class MessageDto extends PageDto{

    private Boolean readed;

    private Long id;

}
