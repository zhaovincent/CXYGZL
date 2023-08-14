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


ALTER TABLE `process_group`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `process_instance_record`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `process_node_data`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `process_node_record`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;

ALTER TABLE `process_node_record_assign_user`
    MODIFY COLUMN `id` bigint NOT NULL COMMENT 'id' FIRST;


ALTER TABLE `process_starter`
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