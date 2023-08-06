ALTER TABLE `process_oper_record`
    ADD COLUMN `parent_id` varchar(255) NULL COMMENT '上级id' ;


ALTER TABLE `process_oper_record`
    ADD COLUMN `task_type` varchar(255) NULL COMMENT '任务类型' ;
