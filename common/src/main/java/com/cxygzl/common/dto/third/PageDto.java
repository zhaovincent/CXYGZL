package com.cxygzl.common.dto.third;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-09-04 16:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageDto <T>{

    private Long total;

    private List<T> records;

}
