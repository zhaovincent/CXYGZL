CREATE TABLE `process_instance_oper_record` (
                                                `id` bigint NOT NULL COMMENT 'id',
                                                `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
                                                `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                                `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                                `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                                `flow_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '流程id',
                                                `node_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '节点id',
                                                `node_name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '节点名称',
                                                `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '流程实例id',
                                                `comment` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '评论',
                                                `oper_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作类型',
                                                `oper_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作描述',
                                                `image_list` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图片列表',
                                                `file_list` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件列表',
                                                PRIMARY KEY (`id`) USING BTREE,
                                                KEY `idx_id` (`id`) USING BTREE,
                                                KEY `idx_dep_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='流程记录';
