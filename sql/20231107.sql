
ALTER TABLE  `process_instance_record`
    ADD COLUMN `process_instance_biz_key` varchar(50) NULL COMMENT '流程实例业务key' AFTER `process_instance_id`;


ALTER TABLE  `process_instance_record`
    ADD COLUMN `process_instance_biz_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程实例业务编码' AFTER `process_instance_id`;
