INSERT INTO   `menu` (`id`, `parent_id`, `tree_path`, `name`, `type`, `path`, `component`, `perm`, `visible`, `sort`, `icon`, `redirect`, `create_time`, `update_time`, `del_flag`) VALUES (1716270348700979202, 1, '0,1', '前端版本维护', 1, 'system/version', 'cxygzl/views/system/version', NULL, 1, 1, 'language', NULL, '2023-10-23 09:48:33', '2023-10-23 09:48:33', 0);

INSERT INTO  `role_menu` (`role_id`, `menu_id`, `id`, `del_flag`, `create_time`, `update_time`) VALUES (2, 1716270348700979202, 1716270422453620740, 0, '2023-10-23 09:48:50', '2023-10-23 09:48:50');
