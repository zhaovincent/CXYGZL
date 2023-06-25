package com.cxygzl.biz;

import com.cxygzl.common.dto.flow.Node;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {

        String json="{\n" +
                "    \"nodeName\": \"发起人\",\n" +
                "    \"type\": 0,\n" +
                "    \"id\": \"root\",\n" +
                "    \"assignedType\": \"\",\n" +
                "    \"formPerms\": {},\n" +
                "    \"nodeUserList\": [],\n" +
                "    \"childNode\": {\n" +
                "        \"id\": \"cxygzl_882619929172\",\n" +
                "        \"nodeName\": \"审批人\",\n" +
                "        \"error\": false,\n" +
                "        \"type\": 1,\n" +
                "        \"assignedType\": 5,\n" +
                "        \"multiple\": false,\n" +
                "        \"multipleMode\": 1,\n" +
                "        \"deptLeaderLevel\": 1,\n" +
                "        \"formUserId\": \"\",\n" +
                "        \"formUserName\": \"\",\n" +
                "        \"formPerms\": {\n" +
                "            \"cxygzl_882573605640\": \"R\",\n" +
                "            \"cxygzl_882586904774\": \"R\"\n" +
                "        },\n" +
                "        \"nobody\": {\n" +
                "            \"handler\": \"TO_PASS\",\n" +
                "            \"assignedUser\": []\n" +
                "        },\n" +

                "        \"nodeUserList\": []\n" +
                "    },\n" +
                "    \"conditionNodes\": []\n" +
                "}";
        ObjectMapper mapper = new ObjectMapper();
        Node node = mapper.readValue(json, Node.class);
        System.out.println("==========");
    }
}
