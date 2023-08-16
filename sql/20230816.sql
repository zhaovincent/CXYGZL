ALTER TABLE message`
    MODIFY COLUMN `param` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '消息参数' AFTER `unique_id`;
