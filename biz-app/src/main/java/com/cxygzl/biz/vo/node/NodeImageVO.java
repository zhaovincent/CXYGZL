package com.cxygzl.biz.vo.node;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Huijun Zhao
 * @description
 * @date 2023-08-10 17:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NodeImageVO   {

    private List<Node> nodes;
    private List<Edge> edges;

    private Map<String, Integer> nodeStatuMap;
    private com.cxygzl.common.dto.flow.Node node;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Node{
        private Integer x;
        private Integer y;


        private String id;

        private String text;
        private String type;

        private Object properties;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Edge{


        private String sourceNodeId;
        private String targetNodeId;

        private String type="polyline";
    }

}
