ALTER TABLE  `process_instance_record`
    ADD COLUMN `result` tinyint(1) NULL COMMENT '流程结果' AFTER `process`;