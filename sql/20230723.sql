ALTER TABLE `process`
    MODIFY COLUMN `admin_id`  varchar(50) NULL DEFAULT NULL COMMENT '流程管理员' AFTER `is_stop`;

ALTER TABLE `process_instance_record`
    MODIFY COLUMN `user_id`  varchar(50) NOT NULL COMMENT '用户id' AFTER `logo`;

ALTER TABLE `process_copy`
    MODIFY COLUMN `start_user_id`  varchar(50) NOT NULL COMMENT '发起人' AFTER `node_time`,
    MODIFY COLUMN `user_id`  varchar(50) NOT NULL COMMENT '抄送人id' AFTER `form_data`;

ALTER TABLE `process_starter`
    MODIFY COLUMN `type_id`  varchar(50) NOT NULL COMMENT '用户id或者部门id' AFTER `update_time`;

