CREATE TABLE `process_execution` (
                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
                                     `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                     `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                     `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                     `execution_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '执行id',
                                     `child_execution_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT ' 子级执行id',
                                     PRIMARY KEY (`id`) USING BTREE,
                                     KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB   DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='流程-执行id关系';


ALTER TABLE  `process_node_record`
    ADD COLUMN `jump_label` varchar(255) NULL COMMENT '跳转标识' ;

ALTER TABLE  `process_node_record`
    CHANGE COLUMN `jump_label` `flow_unqiue_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流转唯一标识' ;