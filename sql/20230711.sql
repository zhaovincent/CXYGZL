-- simple_flow_biz_vue3.process_sub_process definition

CREATE TABLE `process_sub_process` (
                                       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
                                       `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                       `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                       `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                       `flow_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                                       `sub_flow_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '子流程id',
                                       PRIMARY KEY (`id`) USING BTREE,
                                       KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB   DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='流程关联下的子流程';