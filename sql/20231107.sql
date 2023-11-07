
ALTER TABLE  `process_instance_record`
    ADD COLUMN `process_instance_biz_key` varchar(50) NULL COMMENT '流程实例业务key' AFTER `process_instance_id`;