ALTER TABLE `dept`
MODIFY COLUMN `id` bigint NOT NULL COMMENT '部门id' FIRST;

ALTER TABLE `menu`
    MODIFY COLUMN `id` bigint NOT NULL FIRST;

ALTER TABLE `message`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `process`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `process_copy`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `process_execution`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `process_group`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `process_instance_record`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `process_node_data`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `process_node_record`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `process_node_record_approve_desc`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `process_node_record_assign_user`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `process_oper_record`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `process_starter`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;


ALTER TABLE `process_sub_process`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `role`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `role_menu`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' AFTER `menu_id`;

ALTER TABLE `user`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;


ALTER TABLE `user_field`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `user_field_data`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `user_role`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE  `process`
    MODIFY COLUMN `group_id` bigint NOT NULL COMMENT '分组ID' ;


ALTER TABLE  `process_instance_record`
    MODIFY COLUMN `group_id` bigint NULL DEFAULT NULL COMMENT '组id'  ;

ALTER TABLE `process`
    MODIFY COLUMN `admin_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程管理员' ;
