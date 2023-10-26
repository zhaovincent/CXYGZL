CREATE TABLE `process_instance_user_copy` (
                                              `id` bigint NOT NULL COMMENT 'id',
                                              `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                              `create_time` datetime NOT NULL COMMENT '创建时间',
                                              `update_time` datetime NOT NULL COMMENT '更新时间',
                                              `start_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发起人',
                                              `flow_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                                              `process_instance_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '实例id',
                                              `group_id` bigint NOT NULL COMMENT '分组id',
                                              `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分组名称',
                                              `process_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程名称',
                                              `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '抄送人id',
                                              PRIMARY KEY (`id`) USING BTREE,
                                              KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='流程抄送数据--用户和实例唯一值';
