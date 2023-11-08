
ALTER TABLE  `process_instance_record`
    ADD COLUMN `process_instance_biz_key` varchar(50) NULL COMMENT '流程实例业务key' AFTER `process_instance_id`;


ALTER TABLE  `process_instance_record`
    ADD COLUMN `process_instance_biz_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程实例业务编码' AFTER `process_instance_id`;


INSERT INTO  `menu` (`id`, `parent_id`, `tree_path`, `name`, `type`, `path`, `component`, `perm`, `visible`, `sort`,
                     `icon`, `redirect`, `create_time`, `update_time`, `del_flag`) VALUES (1722242469999517698, 89, '0,89', '数据管理', 1, '/flow/datamanage', 'cxygzl/views/flow/data', NULL, 1, 1, 'cascader', NULL, '2023-11-08 21:19:37', '2023-11-08 21:20:12', 0);
INSERT INTO  `role_menu` (`role_id`, `menu_id`, `id`, `del_flag`, `create_time`, `update_time`) VALUES (2,
                                                                                                        1722242469999517698, 1722242511636373511, 0, '2023-11-08 21:19:47', '2023-11-08 21:19:47');
