-- simple_flow_biz_vue3.message definition

CREATE TABLE `message` (
                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
                           `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                           `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                           `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                           `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
                           `readed` tinyint(1) NOT NULL COMMENT '是否已读',
                           `user_id` varchar(50) COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
                           `unique_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '唯一id',
                           `param` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '消息参数',
                           `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '消息内容',
                           `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '消息头',
                           `flow_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                           `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程实例id',
                           PRIMARY KEY (`id`) USING BTREE,
                           KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB   DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='通知消息';


-- 添加消息列表菜单
INSERT INTO  `menu` (
    `parent_id`,
    `tree_path`,
    `name`,
    `type`,
    `path`,
    `component`,
    `perm`,
    `visible`,
    `sort`,
    `icon`,
    `redirect`,
    `create_time`,
    `update_time`,
    `del_flag`
)
VALUES
    (
        '1',
        '0,1',
        '消息列表',
        '1',
        'message',
        'system/message/index',
        NULL,
        '1',
        '1',
        'message',
        NULL,
        '2023-07-25 20:37:35',
        '2023-07-25 20:37:35',
        '0'
    );

