package com.cxygzl.biz.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 菜单路由视图对象
 *
 * @author haoxr
 * @since 2020/11/28
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class RouteVO {

    private String path;

    private String component;

    private String redirect;

    private String name;

    private Meta meta;

    @Data
    public static class Meta {

        private String title;

        private String icon;

        private Boolean hidden;

        private List<String> roles;

        private Boolean keepAlive;
    }

    private List<RouteVO> children;
}
