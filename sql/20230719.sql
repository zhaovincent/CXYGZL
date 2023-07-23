ALTER TABLE `process_instance_record`
    ADD COLUMN `process`  json NULL COMMENT '节点对象' ;

ALTER TABLE `user_field`
    CHANGE COLUMN `configuration` `props`  varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置json字符串' AFTER `required`;

