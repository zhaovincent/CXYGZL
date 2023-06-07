package com.cxygzl.biz.vo;

import com.cxygzl.common.dto.process.NodeUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessSettingVo {

    private List<NodeUserDto> admin;

}
