

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dept
-- ----------------------------
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept`  (
                         `id` bigint NOT NULL COMMENT '部门id',
                         `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名',
                         `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '上级部门id',
                         `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                         `leader_user_id` bigint NOT NULL COMMENT '主管user_id',
                         `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                         `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         `status` int NOT NULL DEFAULT 1,
                         `sort` int NOT NULL DEFAULT 1,
                         PRIMARY KEY (`id`) USING BTREE,
                         INDEX `idx_id`(`id` ASC) USING BTREE,
                         INDEX `idx_parent_id`(`parent_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dept
-- ----------------------------
INSERT INTO `dept` VALUES (1, '第一个部门', 0, 0, 13, '2023-05-05 15:22:42', '2023-06-20 10:58:12', 1, 1);
INSERT INTO `dept` VALUES (2, '刚刚1', 1, 0, 1, '2023-05-05 15:30:10', '2023-05-05 15:50:30', 1, 1);
INSERT INTO `dept` VALUES (3, '对对对', 2, 1, 1, '2023-05-05 15:32:17', '2023-05-05 15:45:50', 1, 1);
INSERT INTO `dept` VALUES (4, '后面', 1, 1, 1, '2023-05-05 15:32:26', '2023-05-05 15:45:52', 1, 1);
INSERT INTO `dept` VALUES (5, '技术部1', 2, 0, 3, '2023-05-05 22:26:07', '2023-06-10 23:10:41', 1, 2);
INSERT INTO `dept` VALUES (6, '后端组', 5, 0, 1, '2023-05-05 23:07:20', '2023-07-31 16:02:57', 1, 1);
INSERT INTO `dept` VALUES (7, '阿斯蒂芬', 5, 1, 1, '2023-06-10 23:13:14', '2023-06-10 23:16:00', 1, 2);
INSERT INTO `dept` VALUES (8, '运营部', 2, 0, 1, '2023-06-27 14:01:49', '2023-06-27 14:01:49', 0, 1);

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
                         `id` bigint NOT NULL,
                         `parent_id` bigint NOT NULL COMMENT '父菜单ID',
                         `tree_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '父节点ID路径',
                         `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '菜单名称',
                         `type` tinyint NOT NULL COMMENT '菜单类型(1:菜单；2:目录；3:外链；4:按钮)',
                         `path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由路径(浏览器地址栏路径)',
                         `component` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组件路径(vue页面完整路径，省略.vue后缀)',
                         `perm` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '权限标识',
                         `visible` tinyint(1) NOT NULL DEFAULT 1 COMMENT '显示状态(1-显示;0-隐藏)',
                         `sort` int NULL DEFAULT 0 COMMENT '排序',
                         `icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '菜单图标',
                         `redirect` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转路径',
                         `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                         `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                         `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 105 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of menu
-- ----------------------------
INSERT INTO `menu` VALUES (1, 0, '0', '系统管理', 2, '/system', 'Layout', NULL, 1, 1, 'system', '/system/user', '2021-08-28 09:12:21', '2021-08-28 09:12:21', 0);
INSERT INTO `menu` VALUES (2, 1, '0,1', '用户管理', 1, 'user', 'system/user/index', NULL, 1, 1, 'user', NULL, '2021-08-28 09:12:21', '2021-08-28 09:12:21', 0);
INSERT INTO `menu` VALUES (3, 1, '0,1', '角色管理', 1, 'role', 'system/role/index', NULL, 1, 2, 'role', NULL, '2021-08-28 09:12:21', '2021-08-28 09:12:21', 0);
INSERT INTO `menu` VALUES (4, 1, '0,1', '菜单管理', 1, 'menu', 'system/menu/index', NULL, 1, 3, 'menu', NULL, '2021-08-28 09:12:21', '2021-08-28 09:12:21', 0);
INSERT INTO `menu` VALUES (5, 1, '0,1', '部门管理', 1, 'dept', 'system/dept/index', NULL, 1, 4, 'tree', NULL, '2021-08-28 09:12:21', '2021-08-28 09:12:21', 0);
INSERT INTO `menu` VALUES (6, 1, '0,1', '字典管理', 1, 'dict', 'system/dict/index', NULL, 0, 5, 'dict', NULL, '2021-08-28 09:12:21', '2023-06-11 08:59:40', 0);
INSERT INTO `menu` VALUES (20, 0, '0', '多级菜单', 2, '/multi-level', 'Layout', NULL, 1, 9, 'multi_level', '/multi-level/multi-level1', '2022-02-16 23:11:00', '2023-06-11 09:00:09', 1);
INSERT INTO `menu` VALUES (21, 20, '0,20', '菜单一级', 2, 'multi-level1', 'demo/multi-level/level1', NULL, 1, 1, '', '/multi-level/multi-level2', '2022-02-16 23:13:38', '2023-06-11 09:00:09', 1);
INSERT INTO `menu` VALUES (22, 21, '0,20,21', '菜单二级', 2, 'multi-level2', 'demo/multi-level/children/level2', NULL, 1, 1, '', '/multi-level/multi-level2/multi-level3-1', '2022-02-16 23:14:23', '2023-06-11 09:00:09', 1);
INSERT INTO `menu` VALUES (23, 22, '0,20,21,22', '菜单三级-1', 1, 'multi-level3-1', 'demo/multi-level/children/children/level3-1', NULL, 1, 1, '', '', '2022-02-16 23:14:51', '2023-06-11 09:00:09', 1);
INSERT INTO `menu` VALUES (24, 22, '0,20,21,22', '菜单三级-2', 1, 'multi-level3-2', 'demo/multi-level/children/children/level3-2', NULL, 1, 2, '', '', '2022-02-16 23:15:08', '2023-06-11 09:00:09', 1);
INSERT INTO `menu` VALUES (26, 0, '0', '外部链接', 2, '/external-link', 'Layout', NULL, 0, 8, 'link', 'noredirect', '2022-02-17 22:51:20', '2023-06-11 09:03:20', 0);
INSERT INTO `menu` VALUES (30, 26, '0,26', 'document', 3, 'https://juejin.cn/post/7228990409909108793', '', NULL, 1, 1, 'document', '', '2022-02-18 00:01:40', '2022-02-18 00:01:40', 0);
INSERT INTO `menu` VALUES (31, 2, '0,1,2', '用户新增', 4, '', NULL, 'sys:user:add', 1, 1, '', '', '2022-10-23 11:04:08', '2022-10-23 11:04:11', 0);
INSERT INTO `menu` VALUES (32, 2, '0,1,2', '用户编辑', 4, '', NULL, 'sys:user:edit', 1, 2, '', '', '2022-10-23 11:04:08', '2022-10-23 11:04:11', 0);
INSERT INTO `menu` VALUES (33, 2, '0,1,2', '用户删除', 4, '', NULL, 'sys:user:delete', 1, 3, '', '', '2022-10-23 11:04:08', '2022-10-23 11:04:11', 0);
INSERT INTO `menu` VALUES (36, 0, '0', '组件封装', 2, '/component', 'Layout', NULL, 1, 10, 'menu', '', '2022-10-31 09:18:44', '2022-10-31 09:18:47', 0);
INSERT INTO `menu` VALUES (37, 36, '0,36', '富文本编辑器', 1, 'wang-editor', 'demo/wang-editor', NULL, 1, 1, '', '', NULL, NULL, 0);
INSERT INTO `menu` VALUES (38, 36, '0,36', '图片上传', 1, 'upload', 'demo/upload', NULL, 1, 2, '', '', '2022-11-20 23:16:30', '2022-11-20 23:16:32', 0);
INSERT INTO `menu` VALUES (39, 36, '0,36', '图标选择器', 1, 'icon-selector', 'demo/icon-selector', NULL, 1, 3, '', '', '2022-11-20 23:16:30', '2022-11-20 23:16:32', 0);
INSERT INTO `menu` VALUES (40, 0, '0', '接口', 2, '/api', 'Layout', NULL, 0, 7, 'api', '', '2022-02-17 22:51:20', '2023-06-11 09:02:45', 0);
INSERT INTO `menu` VALUES (41, 40, '0,40', '接口文档', 1, 'apidoc', 'demo/api-doc', NULL, 1, 1, 'api', '', '2022-02-17 22:51:20', '2022-02-17 22:51:20', 0);
INSERT INTO `menu` VALUES (70, 3, '0,1,3', '角色新增', 4, '', NULL, 'sys:role:add', 1, 1, '', NULL, '2023-05-20 23:39:09', '2023-05-20 23:39:09', 0);
INSERT INTO `menu` VALUES (71, 3, '0,1,3', '角色编辑', 4, '', NULL, 'sys:role:edit', 1, 2, '', NULL, '2023-05-20 23:40:31', '2023-05-20 23:40:31', 0);
INSERT INTO `menu` VALUES (72, 3, '0,1,3', '角色删除', 4, '', NULL, 'sys:role:delete', 1, 3, '', NULL, '2023-05-20 23:41:08', '2023-05-20 23:41:08', 0);
INSERT INTO `menu` VALUES (73, 4, '0,1,4', '菜单新增', 4, '', NULL, 'sys:menu:add', 1, 1, '', NULL, '2023-05-20 23:41:35', '2023-05-20 23:41:35', 0);
INSERT INTO `menu` VALUES (74, 4, '0,1,4', '菜单编辑', 4, '', NULL, 'sys:menu:edit', 1, 3, '', NULL, '2023-05-20 23:41:58', '2023-05-20 23:41:58', 0);
INSERT INTO `menu` VALUES (75, 4, '0,1,4', '菜单删除', 4, '', NULL, 'sys:menu:delete', 1, 3, '', NULL, '2023-05-20 23:44:18', '2023-05-20 23:44:18', 0);
INSERT INTO `menu` VALUES (76, 5, '0,1,5', '部门新增', 4, '', NULL, 'sys:dept:add', 1, 1, '', NULL, '2023-05-20 23:45:00', '2023-05-20 23:45:00', 0);
INSERT INTO `menu` VALUES (77, 5, '0,1,5', '部门编辑', 4, '', NULL, 'sys:dept:edit', 1, 2, '', NULL, '2023-05-20 23:46:16', '2023-05-20 23:46:16', 0);
INSERT INTO `menu` VALUES (78, 5, '0,1,5', '部门删除', 4, '', NULL, 'sys:dept:delete', 1, 3, '', NULL, '2023-05-20 23:46:36', '2023-05-20 23:46:36', 0);
INSERT INTO `menu` VALUES (79, 6, '0,1,6', '字典类型新增', 4, '', NULL, 'sys:dict_type:add', 1, 1, '', NULL, '2023-05-21 00:16:06', '2023-05-21 00:16:06', 0);
INSERT INTO `menu` VALUES (81, 6, '0,1,6', '字典类型编辑', 4, '', NULL, 'sys:dict_type:edit', 1, 2, '', NULL, '2023-05-21 00:27:37', '2023-05-21 00:27:37', 0);
INSERT INTO `menu` VALUES (84, 6, '0,1,6', '字典类型删除', 4, '', NULL, 'sys:dict_type:delete', 1, 3, '', NULL, '2023-05-21 00:29:39', '2023-05-21 00:29:39', 0);
INSERT INTO `menu` VALUES (85, 6, '0,1,6', '字典数据新增', 4, '', NULL, 'sys:dict:add', 1, 4, '', NULL, '2023-05-21 00:46:56', '2023-05-21 00:47:06', 0);
INSERT INTO `menu` VALUES (86, 6, '0,1,6', '字典数据编辑', 4, '', NULL, 'sys:dict:edit', 1, 5, '', NULL, '2023-05-21 00:47:36', '2023-05-21 00:47:36', 0);
INSERT INTO `menu` VALUES (87, 6, '0,1,6', '字典数据删除', 4, '', NULL, 'sys:dict:delete', 1, 6, '', NULL, '2023-05-21 00:48:10', '2023-05-21 00:48:20', 0);
INSERT INTO `menu` VALUES (88, 2, '0,1,2', '重置密码', 4, '', NULL, 'sys:user:reset_pwd', 1, 4, '', NULL, '2023-05-21 00:49:18', '2023-05-21 00:49:18', 0);
INSERT INTO `menu` VALUES (89, 0, '0', '流程管理', 2, '/flow', 'Layout', NULL, 1, 1, 'multi_level', NULL, '2023-06-09 23:33:13', '2023-06-11 08:37:00', 0);
INSERT INTO `menu` VALUES (90, 89, '0,89', '流程列表', 1, '/flow/list', 'flow/list', NULL, 1, 2, 'menu', '', '2023-06-09 23:35:14', '2023-08-01 15:16:48', 0);
INSERT INTO `menu` VALUES (91, 89, '0,89', '流程组', 1, '/flow/group', 'flow/group', NULL, 1, 1, 'redis', NULL, '2023-06-09 23:37:38', '2023-06-11 16:21:33', 0);
INSERT INTO `menu` VALUES (92, 0, '0', '订单的1', 1, 'test', 'test/index', NULL, 1, 1, 'cascader', NULL, '2023-06-11 08:08:24', '2023-06-11 08:46:28', 1);
INSERT INTO `menu` VALUES (93, 92, '0,92', 'asadfffff', 1, 'dept1', 'system/dept/index1', NULL, 1, 1, 'cascader', NULL, '2023-06-11 08:46:12', '2023-06-11 08:46:28', 1);
INSERT INTO `menu` VALUES (94, 89, '0,89', '创建流程', 1, '/flow/create', 'flow/create/all', NULL, 1, 1, 'tree', NULL, '2023-06-11 12:57:28', '2023-06-11 16:21:17', 0);
INSERT INTO `menu` VALUES (95, 0, '0', '任务管理', 2, '/task', 'Layout', NULL, 1, 1, 'menu', '', '2023-06-19 09:16:59', '2023-06-19 09:16:59', 0);
INSERT INTO `menu` VALUES (96, 95, '0,95', '待办任务', 1, '/task/pending', 'task/pending', NULL, 1, 1, 'edit', '', '2023-06-19 09:17:30', '2023-06-19 10:49:17', 0);
INSERT INTO `menu` VALUES (97, 95, '0,95', '我的发起', 1, '/task/started', 'task/started', NULL, 1, 1, 'dashboard', '', '2023-06-19 09:18:20', '2023-06-19 09:18:20', 0);
INSERT INTO `menu` VALUES (101, 95, '0,95', '我的已办', 1, '/task/completed', 'task/completed', NULL, 1, 3, 'number', NULL, '2023-06-25 15:50:58', '2023-06-25 15:50:58', 0);
INSERT INTO `menu` VALUES (102, 95, '0,95', '抄送我的', 1, '/task/cc', 'task/cc', NULL, 1, 4, 'bug', NULL, '2023-06-25 15:51:23', '2023-06-25 15:51:23', 0);
INSERT INTO `menu` VALUES (103, 1, '0,1', '用户属性', 1, 'prop', 'system/prop/index', NULL, 1, 1, 'number', NULL, '2023-07-20 14:07:42', '2023-07-20 14:07:42', 0);
INSERT INTO `menu` VALUES (104, 1, '0,1', '消息通知', 1, 'message', 'system/message/index', NULL, 1, 1, 'message', NULL, '2023-07-25 17:09:21', '2023-07-25 17:11:45', 0);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
                         `id` bigint NOT NULL COMMENT 'id',
                         `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                         `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                         `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                         `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '角色名字',
                         `user_id` bigint NULL DEFAULT NULL COMMENT '创建人',
                         `key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                         `desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                         `status` int NULL DEFAULT 1,
                         `sort` int NULL DEFAULT 1,
                         `data_scope` int NOT NULL DEFAULT 0,
                         PRIMARY KEY (`id`) USING BTREE,
                         INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, 0, '2023-06-08 22:49:41', '2023-06-08 22:49:43', '超级管理员', 13, 'ROOT', NULL, 1, 2, 0);
INSERT INTO `role` VALUES (2, 0, '2023-06-08 23:01:30', '2023-06-08 23:01:32', '系统管理员', NULL, 'ADMIN', NULL, 1, 1, 0);
INSERT INTO `role` VALUES (32, 1, '2023-06-10 23:39:23', '2023-06-10 23:41:17', '测试角色', 13, 'test', NULL, 1, 1, 1);
INSERT INTO `role` VALUES (33, 0, '2023-06-10 23:44:17', '2023-06-10 23:44:17', '测试235', 13, 'ts', NULL, 1, 1, 0);

-- ----------------------------
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
                              `role_id` bigint NOT NULL COMMENT '角色ID',
                              `menu_id` bigint NOT NULL COMMENT '菜单ID',
                              `id` bigint NOT NULL COMMENT 'id',
                              `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                              `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                              `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 295 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of role_menu
-- ----------------------------
INSERT INTO `role_menu` VALUES (2, 89, 1, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 91, 2, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 90, 3, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 1, 4, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 2, 5, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 31, 6, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 32, 7, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 33, 8, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 88, 9, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 3, 10, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 70, 11, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 71, 12, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 72, 13, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 4, 14, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 73, 15, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 75, 16, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 74, 17, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 5, 18, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 76, 19, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 77, 20, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 78, 21, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 6, 22, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 79, 23, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 81, 24, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 84, 25, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 85, 26, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 86, 27, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 87, 28, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 40, 29, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 41, 30, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 26, 31, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 30, 32, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 20, 33, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 21, 34, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 22, 35, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 23, 36, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 24, 37, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 36, 38, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 37, 39, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 38, 40, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (2, 39, 41, 1, NULL, NULL);
INSERT INTO `role_menu` VALUES (33, 89, 42, 0, '2023-06-11 00:14:55', '2023-06-11 00:14:55');
INSERT INTO `role_menu` VALUES (33, 91, 43, 0, '2023-06-11 00:14:55', '2023-06-11 00:14:55');
INSERT INTO `role_menu` VALUES (33, 90, 44, 0, '2023-06-11 00:14:55', '2023-06-11 00:14:55');
INSERT INTO `role_menu` VALUES (2, 89, 45, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 94, 46, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 91, 47, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 90, 48, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 1, 49, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 2, 50, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 31, 51, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 32, 52, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 33, 53, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 88, 54, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 3, 55, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 70, 56, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 71, 57, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 72, 58, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 4, 59, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 73, 60, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 74, 61, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 75, 62, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 5, 63, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 76, 64, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 77, 65, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 78, 66, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 6, 67, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 79, 68, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 81, 69, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 84, 70, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 85, 71, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 86, 72, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 87, 73, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 40, 74, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 41, 75, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 26, 76, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 30, 77, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 36, 78, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 37, 79, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 38, 80, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 39, 81, 1, '2023-06-11 12:57:48', '2023-06-11 12:57:48');
INSERT INTO `role_menu` VALUES (2, 1, 82, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 2, 83, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 31, 84, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 32, 85, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 33, 86, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 88, 87, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 3, 88, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 70, 89, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 71, 90, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 72, 91, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 4, 92, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 73, 93, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 74, 94, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 75, 95, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 5, 96, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 76, 97, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 77, 98, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 78, 99, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 6, 100, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 79, 101, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 81, 102, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 84, 103, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 85, 104, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 86, 105, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 87, 106, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 89, 107, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 91, 108, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 94, 109, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 90, 110, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 95, 111, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 96, 112, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 97, 113, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 40, 114, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 41, 115, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 26, 116, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 30, 117, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 36, 118, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 37, 119, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 38, 120, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 39, 121, 1, '2023-06-19 09:18:30', '2023-06-19 09:18:30');
INSERT INTO `role_menu` VALUES (2, 1, 122, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 2, 123, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 31, 124, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 32, 125, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 33, 126, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 88, 127, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 3, 128, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 70, 129, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 71, 130, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 72, 131, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 4, 132, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 73, 133, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 74, 134, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 75, 135, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 5, 136, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 76, 137, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 77, 138, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 78, 139, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 6, 140, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 79, 141, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 81, 142, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 84, 143, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 85, 144, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 86, 145, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 87, 146, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 89, 147, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 94, 148, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 91, 149, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 90, 150, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 95, 151, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 96, 152, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 97, 153, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 101, 154, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 102, 155, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 40, 156, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 41, 157, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 26, 158, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 30, 159, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 36, 160, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 37, 161, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 38, 162, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 39, 163, 1, '2023-06-25 15:51:34', '2023-06-25 15:51:34');
INSERT INTO `role_menu` VALUES (2, 1, 164, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 2, 165, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 31, 166, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 32, 167, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 33, 168, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 88, 169, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 103, 170, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 3, 171, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 70, 172, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 71, 173, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 72, 174, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 4, 175, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 73, 176, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 75, 177, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 74, 178, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 5, 179, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 76, 180, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 77, 181, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 78, 182, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 6, 183, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 79, 184, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 81, 185, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 84, 186, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 85, 187, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 86, 188, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 87, 189, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 95, 190, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 96, 191, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 97, 192, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 101, 193, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 102, 194, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 89, 195, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 91, 196, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 94, 197, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 90, 198, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 40, 199, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 41, 200, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 26, 201, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 30, 202, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 36, 203, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 37, 204, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 38, 205, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 39, 206, 1, '2023-07-20 14:07:52', '2023-07-20 14:07:52');
INSERT INTO `role_menu` VALUES (2, 1, 207, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 2, 208, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 31, 209, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 32, 210, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 33, 211, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 88, 212, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 103, 213, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 3, 214, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 70, 215, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 71, 216, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 72, 217, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 4, 218, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 73, 219, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 75, 220, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 74, 221, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 5, 222, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 76, 223, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 77, 224, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 78, 225, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 6, 226, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 79, 227, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 81, 228, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 84, 229, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 85, 230, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 86, 231, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 87, 232, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 104, 233, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 95, 234, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 96, 235, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 97, 236, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 101, 237, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 102, 238, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 89, 239, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 94, 240, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 91, 241, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 90, 242, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 40, 243, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 41, 244, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 26, 245, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 30, 246, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 36, 247, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 37, 248, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 38, 249, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 39, 250, 1, '2023-07-25 17:09:28', '2023-07-25 17:09:28');
INSERT INTO `role_menu` VALUES (2, 1, 251, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 2, 252, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 31, 253, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 32, 254, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 33, 255, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 88, 256, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 104, 257, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 103, 258, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 3, 259, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 70, 260, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 71, 261, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 72, 262, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 4, 263, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 73, 264, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 75, 265, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 74, 266, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 5, 267, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 76, 268, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 77, 269, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 78, 270, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 6, 271, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 79, 272, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 81, 273, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 84, 274, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 85, 275, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 86, 276, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 87, 277, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 95, 278, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 96, 279, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 97, 280, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 101, 281, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 102, 282, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 89, 283, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 94, 284, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 91, 285, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 90, 286, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 40, 287, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 41, 288, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 26, 289, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 30, 290, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 36, 291, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 37, 292, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 38, 293, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');
INSERT INTO `role_menu` VALUES (2, 39, 294, 0, '2023-07-25 17:11:55', '2023-07-25 17:11:55');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
                         `id` bigint NOT NULL COMMENT 'id',
                         `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
                         `pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拼音  全拼',
                         `py` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拼音, 首字母缩写',
                         `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
                         `avatar_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像url',
                         `gender` int NULL DEFAULT 1 COMMENT '性别1男2女',
                         `dept_id` bigint NOT NULL COMMENT '部门id',
                         `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                         `entry_date` date NULL DEFAULT NULL COMMENT '入职日期',
                         `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                         `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登录密码',
                         `phone` varchar(18) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
                         `status` int NOT NULL DEFAULT 0,
                         PRIMARY KEY (`id`) USING BTREE,
                         INDEX `idx_id`(`id` ASC) USING BTREE,
                         INDEX `idx_dep_id`(`dept_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '用户1', 'yonghu1', 'yh1', '用户1', 'https://f.ittool.cc/pic/m.jpg', 1, 6, 0, '2023-05-05', '2023-05-05 15:23:40', '2023-06-20 17:37:11', '123456', '18765858569', 1);
INSERT INTO `user` VALUES (2, '大幅度', 'dafudu', 'dfd', '大幅度', 'https://f.ittool.cc/pic/m.jpg', 2, 2, 0, '2023-05-03', '2023-05-05 21:02:30', '2023-08-14 10:38:04', NULL, '13541265895', 1);
INSERT INTO `user` VALUES (3, '啊啊啊', 'aaa', 'aaa', '啊啊啊', 'https://f.ittool.cc/pic/m.jpg', 2, 1, 0, '2023-05-05', '2023-05-05 21:03:32', '2023-08-14 10:39:52', '123456', '18754215896', 1);
INSERT INTO `user` VALUES (4, '地方', 'difang', 'df', '地方', 'https://f.ittool.cc/pic/m.jpg', 2, 2, 0, '2023-05-03', '2023-05-05 21:04:33', '2023-08-14 10:38:04', NULL, '13685987458', 1);
INSERT INTO `user` VALUES (5, '地方', 'difang', 'df', '地方', 'https://f.ittool.cc/pic/m.jpg', 1, 2, 0, '2023-05-02', '2023-05-05 21:06:11', '2023-08-14 10:38:04', NULL, '13985968569', 1);
INSERT INTO `user` VALUES (6, '水电费', 'shuidianfei', 'sdf', '水电费', 'https://f.ittool.cc/pic/m.jpg', 1, 2, 0, '2023-05-02', '2023-05-05 22:25:45', '2023-08-14 10:38:04', NULL, '13325859685', 1);
INSERT INTO `user` VALUES (7, '电饭锅', 'dianfanguo', 'dfg', '电饭锅', 'https://f.ittool.cc/pic/m.jpg', 1, 5, 0, '2023-05-05', '2023-05-05 22:26:27', '2023-08-14 10:38:04', '123456', '15265265238', 0);
INSERT INTO `user` VALUES (8, 'java1', NULL, NULL, 'java1', 'https://f.ittool.cc/pic/m.jpg', 1, 6, 1, '2023-05-05', '2023-05-05 23:07:39', '2023-08-14 10:38:04', NULL, NULL, 0);
INSERT INTO `user` VALUES (9, 'dddd', 'dddd', 'dddd', 'dddd', 'https://f.ittool.cc/pic/m.jpg', 1, 5, 0, '2023-05-02', '2023-05-07 11:40:55', '2023-08-14 10:38:04', '123456', '18755289563', 1);
INSERT INTO `user` VALUES (10, 'ttttt', 'ttttt', 'ttttt', 'ttttt', 'https://f.ittool.cc/pic/m.jpg', 1, 1, 0, '2023-05-07', '2023-05-07 11:41:32', '2023-08-14 10:38:04', '123456', '18575421236', 1);
INSERT INTO `user` VALUES (11, 'tttew23', 'tttew23', 'tttew23', 'tttew23', 'https://f.ittool.cc/pic/m.jpg', 1, 1, 0, '2023-05-07', '2023-05-07 11:42:31', '2023-08-14 10:38:04', '123456', '15269852365', 1);
INSERT INTO `user` VALUES (12, '测试2', 'ceshi2', 'cs2', '测试2', 'https://f.ittool.cc/pic/m.jpg', 1, 1, 0, '2023-05-07', '2023-05-07 11:50:51', '2023-08-14 10:38:04', '123456', '18752859635', 1);
INSERT INTO `user` VALUES (13, '测试111', 'ceshi111', 'cs111', '测试111', 'https://f.ittool.cc/pic/m.jpg', 2, 6, 0, '2023-05-07', '2023-05-07 11:53:14', '2023-08-14 10:38:04', '123456', '18755289564', 1);
INSERT INTO `user` VALUES (14, '是防辐', 'shifangfu', 'sff', '是防辐', 'https://f.ittool.cc/pic/m.jpg', 1, 2, 1, NULL, '2023-06-10 16:18:20', '2023-08-14 10:38:04', NULL, '15265235412', 1);
INSERT INTO `user` VALUES (15, '山东省', 'shandongsheng', 'sds', '山东省', 'https://f.ittool.cc/pic/m.jpg', 1, 6, 0, NULL, '2023-06-10 16:35:40', '2023-08-14 10:38:04', '123456', '15265235896', 1);

-- ----------------------------
-- Table structure for user_field
-- ----------------------------
DROP TABLE IF EXISTS `user_field`;
CREATE TABLE `user_field`  (
                               `id` bigint NOT NULL COMMENT 'id',
                               `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
                               `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                               `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                               `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                               `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段类型',
                               `required` tinyint(1) NOT NULL COMMENT '是否必填',
                               `configuration` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置json字符串',
                               `key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段',
                               PRIMARY KEY (`id`) USING BTREE,
                               INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_field
-- ----------------------------
INSERT INTO `user_field` VALUES (17, '哈哈', 1, '2023-06-10 18:48:33', '2023-06-10 18:48:35', 'Number', 1, NULL, 'asdfas');
INSERT INTO `user_field` VALUES (18, 'a', 1, '2023-07-20 14:08:02', '2023-07-20 14:08:02', 'Number', 0, '[]', 'cxygzl_332771171079');
INSERT INTO `user_field` VALUES (19, 'a', 1, '2023-07-20 14:08:07', '2023-07-20 14:08:07', 'Number', 0, '[]', 'cxygzl_332771171079');
INSERT INTO `user_field` VALUES (20, 'b', 1, '2023-07-20 14:08:07', '2023-07-20 14:08:07', 'Textarea', 1, '[]', 'cxygzl_332831161473');
INSERT INTO `user_field` VALUES (21, 'a', 1, '2023-07-20 14:08:14', '2023-07-20 14:08:14', 'Number', 0, '[]', 'cxygzl_332771171079');
INSERT INTO `user_field` VALUES (22, 'b', 1, '2023-07-20 14:08:14', '2023-07-20 14:08:14', 'Textarea', 1, '[]', 'cxygzl_332831161473');
INSERT INTO `user_field` VALUES (23, 'c', 1, '2023-07-20 14:08:14', '2023-07-20 14:08:14', 'Input', 0, '[]', 'cxygzl_332885886315');
INSERT INTO `user_field` VALUES (24, 'b', 1, '2023-07-20 14:43:33', '2023-07-20 14:43:33', 'Textarea', 1, '[]', 'cxygzl_332831161473');
INSERT INTO `user_field` VALUES (25, 'c', 1, '2023-07-20 14:43:33', '2023-07-20 14:43:33', 'Input', 0, '[]', 'cxygzl_332885886315');
INSERT INTO `user_field` VALUES (26, 'b', 1, '2023-07-20 14:43:39', '2023-07-20 14:43:39', 'Textarea', 1, '[]', 'cxygzl_332831161473');
INSERT INTO `user_field` VALUES (27, 'c', 1, '2023-07-20 14:43:39', '2023-07-20 14:43:39', 'Input', 0, '[]', 'cxygzl_332885886315');
INSERT INTO `user_field` VALUES (28, 'a', 1, '2023-07-20 14:43:39', '2023-07-20 14:43:39', 'Number', 1, '{\"options\":[],\"radixNum\":0}', 'cxygzl_354140007892');
INSERT INTO `user_field` VALUES (29, 'a', 1, '2023-07-20 15:14:47', '2023-07-20 15:14:47', 'Number', 1, '{\"options\":[],\"radixNum\":0}', 'cxygzl_372774503135');
INSERT INTO `user_field` VALUES (30, 'b', 1, '2023-07-20 15:14:47', '2023-07-20 15:14:47', 'Input', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_372816979493');
INSERT INTO `user_field` VALUES (31, '数字', 0, '2023-07-20 15:41:59', '2023-07-20 15:41:59', 'Number', 1, '{\"options\":[],\"radixNum\":0}', 'cxygzl_372774503135');
INSERT INTO `user_field` VALUES (32, '单行', 0, '2023-07-20 15:42:00', '2023-07-20 15:42:00', 'Input', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_372816979493');
INSERT INTO `user_field` VALUES (33, '多行', 0, '2023-07-20 15:42:00', '2023-07-20 15:42:00', 'Textarea', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_388562604133');
INSERT INTO `user_field` VALUES (34, '日期', 0, '2023-07-20 15:42:00', '2023-07-20 15:42:00', 'Date', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_388617184797');
INSERT INTO `user_field` VALUES (35, '日期时间', 0, '2023-07-20 15:42:00', '2023-07-20 15:42:00', 'DateTime', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_388662122401');
INSERT INTO `user_field` VALUES (36, '时间', 0, '2023-07-20 15:42:00', '2023-07-20 15:42:00', 'Time', 1, '{\"options\":[],\"radixNum\":0}', 'cxygzl_388715488271');
INSERT INTO `user_field` VALUES (37, '单选', 0, '2023-07-20 15:42:00', '2023-07-20 15:42:00', 'Select', 1, '{\"options\":[{\"key\":\"1\",\"value\":\"a\"},{\"key\":\"2\",\"value\":\"b\"},{\"key\":\"3\",\"value\":\"c\"},{\"key\":\"4\",\"value\":\"d\"}],\"radixNum\":0}', 'cxygzl_388764707074');
INSERT INTO `user_field` VALUES (38, '多选', 0, '2023-07-20 15:42:00', '2023-07-20 15:42:00', 'MultiSelect', 0, '{\"options\":[{\"key\":\"1\",\"value\":\"a\"},{\"key\":\"2\",\"value\":\"b\"},{\"key\":\"3\",\"value\":\"c\"},{\"key\":\"4\",\"value\":\"d\"}],\"radixNum\":0}', 'cxygzl_388798167404');

-- ----------------------------
-- Table structure for user_field_data
-- ----------------------------
DROP TABLE IF EXISTS `user_field_data`;
CREATE TABLE `user_field_data`  (
                                    `id` bigint NOT NULL COMMENT 'id',
                                    `user_id` bigint NOT NULL COMMENT '用户id',
                                    `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                    `data` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据',
                                    `key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 34 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户字段-数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_field_data
-- ----------------------------
INSERT INTO `user_field_data` VALUES (30, 15, 0, '2023-07-20 16:01:51', '2023-07-20 16:01:51', '1', 'cxygzl_372774503135');
INSERT INTO `user_field_data` VALUES (31, 15, 0, '2023-07-20 16:01:51', '2023-07-20 16:01:51', '2023-07-20T08:01:42.934Z', 'cxygzl_388715488271');
INSERT INTO `user_field_data` VALUES (32, 15, 0, '2023-07-20 16:01:51', '2023-07-20 16:01:51', '4', 'cxygzl_388764707074');
INSERT INTO `user_field_data` VALUES (33, 15, 0, '2023-07-20 16:01:51', '2023-07-20 16:01:51', '[]', 'cxygzl_388798167404');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
                              `id` bigint NOT NULL COMMENT 'id',
                              `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                              `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                              `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                              `user_id` bigint NOT NULL COMMENT '用户id',
                              `role_id` bigint NOT NULL COMMENT '角色id',
                              PRIMARY KEY (`id`) USING BTREE,
                              INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 58 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户-角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (30, 1, '2023-06-08 23:10:30', '2023-06-08 23:10:33', 13, 2);
INSERT INTO `user_role` VALUES (31, 1, '2023-06-10 16:22:03', '2023-06-10 16:22:03', 14, 1);
INSERT INTO `user_role` VALUES (32, 1, '2023-06-10 16:22:03', '2023-06-10 16:22:03', 14, 2);
INSERT INTO `user_role` VALUES (33, 1, '2023-06-10 16:35:40', '2023-06-10 16:35:40', 15, 1);
INSERT INTO `user_role` VALUES (34, 1, '2023-06-10 16:59:40', '2023-06-10 16:59:40', 13, 2);
INSERT INTO `user_role` VALUES (35, 1, '2023-06-10 17:06:12', '2023-06-10 17:06:12', 11, 2);
INSERT INTO `user_role` VALUES (36, 1, '2023-06-10 18:48:07', '2023-06-10 18:48:07', 7, 2);
INSERT INTO `user_role` VALUES (37, 1, '2023-06-20 09:59:23', '2023-06-20 09:59:23', 13, 2);
INSERT INTO `user_role` VALUES (38, 0, '2023-06-20 10:58:46', '2023-06-20 10:58:46', 13, 2);
INSERT INTO `user_role` VALUES (39, 1, '2023-06-20 12:00:04', '2023-06-20 12:00:04', 15, 1);
INSERT INTO `user_role` VALUES (40, 0, '2023-06-20 12:00:21', '2023-06-20 12:00:21', 12, 1);
INSERT INTO `user_role` VALUES (41, 0, '2023-06-20 12:00:28', '2023-06-20 12:00:28', 11, 2);
INSERT INTO `user_role` VALUES (42, 0, '2023-06-20 12:00:44', '2023-06-20 12:00:44', 10, 1);
INSERT INTO `user_role` VALUES (43, 0, '2023-06-20 12:00:54', '2023-06-20 12:00:54', 9, 1);
INSERT INTO `user_role` VALUES (44, 1, '2023-06-20 12:01:03', '2023-06-20 12:01:03', 7, 2);
INSERT INTO `user_role` VALUES (45, 0, '2023-06-20 12:04:14', '2023-06-20 12:04:14', 3, 1);
INSERT INTO `user_role` VALUES (46, 1, '2023-06-20 17:36:32', '2023-06-20 17:36:32', 1, 1);
INSERT INTO `user_role` VALUES (47, 1, '2023-06-20 17:36:32', '2023-06-20 17:36:32', 1, 2);
INSERT INTO `user_role` VALUES (48, 0, '2023-06-21 10:04:17', '2023-06-21 10:04:17', 2, 1);
INSERT INTO `user_role` VALUES (49, 0, '2023-06-21 10:04:31', '2023-06-21 10:04:31', 6, 1);
INSERT INTO `user_role` VALUES (50, 0, '2023-06-21 10:05:07', '2023-06-21 10:05:07', 5, 1);
INSERT INTO `user_role` VALUES (51, 0, '2023-06-21 10:05:21', '2023-06-21 10:05:21', 4, 1);
INSERT INTO `user_role` VALUES (52, 0, '2023-06-27 13:53:54', '2023-06-27 13:53:54', 7, 2);
INSERT INTO `user_role` VALUES (53, 1, '2023-07-20 15:50:14', '2023-07-20 15:50:14', 15, 1);
INSERT INTO `user_role` VALUES (54, 1, '2023-07-20 16:00:15', '2023-07-20 16:00:15', 15, 1);
INSERT INTO `user_role` VALUES (55, 0, '2023-07-20 16:01:51', '2023-07-20 16:01:51', 15, 1);
INSERT INTO `user_role` VALUES (56, 0, '2023-08-03 16:58:15', '2023-08-03 16:58:15', 1, 1);
INSERT INTO `user_role` VALUES (57, 0, '2023-08-03 16:58:15', '2023-08-03 16:58:15', 1, 2);

SET FOREIGN_KEY_CHECKS = 1;




    /*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : localhost:3306
 Source Schema         : cxygzl_biz

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 14/08/2023 10:41:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
                            `id` bigint NOT NULL COMMENT 'id',
                            `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                            `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                            `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型',
                            `readed` tinyint(1) NOT NULL COMMENT '是否已读',
                            `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
                            `unique_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '唯一id',
                            `param` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息参数',
                            `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息内容',
                            `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息头',
                            `flow_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                            `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程实例id',
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 238 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知消息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for process
-- ----------------------------
DROP TABLE IF EXISTS `process`;
CREATE TABLE `process`  (
                            `id` bigint NOT NULL COMMENT 'id',
                            `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                            `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                            `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                            `flow_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '表单ID',
                            `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单名称',
                            `logo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图标配置',
                            `settings` json NULL COMMENT '设置项',
                            `group_id` bigint NOT NULL COMMENT '分组ID',
                            `form_items` json NOT NULL COMMENT '表单设置内容',
                            `process` json NOT NULL COMMENT '流程设置内容',
                            `remark` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                            `sort` int NOT NULL,
                            `is_hidden` tinyint(1) NOT NULL COMMENT '0 正常 1=隐藏',
                            `is_stop` tinyint(1) NOT NULL COMMENT '0 正常 1=停用 ',
                            `admin_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程管理员',
                            `unique_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '唯一性id',
                            `admin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员',
                            `range_show` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE INDEX `idx_form_id`(`flow_id` ASC) USING BTREE,
                            INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 230 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for process_copy
-- ----------------------------
DROP TABLE IF EXISTS `process_copy`;
CREATE TABLE `process_copy`  (
                                 `id` bigint NOT NULL COMMENT 'id',
                                 `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                 `create_time` datetime NOT NULL COMMENT '创建时间',
                                 `update_time` datetime NOT NULL COMMENT '更新时间',
                                 `start_time` datetime NOT NULL COMMENT ' 流程发起时间',
                                 `node_time` datetime NOT NULL COMMENT '当前节点时间',
                                 `start_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发起人',
                                 `flow_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                                 `process_instance_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '实例id',
                                 `node_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点id',
                                 `group_id` bigint NOT NULL COMMENT '分组id',
                                 `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分组名称',
                                 `process_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程名称',
                                 `node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点 名称',
                                 `form_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单数据',
                                 `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '抄送人id',
                                 PRIMARY KEY (`id`) USING BTREE,
                                 INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程抄送数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for process_group
-- ----------------------------
DROP TABLE IF EXISTS `process_group`;
CREATE TABLE `process_group`  (
                                  `id` bigint NOT NULL COMMENT 'id',
                                  `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分组名',
                                  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for process_instance_record
-- ----------------------------
DROP TABLE IF EXISTS `process_instance_record`;
CREATE TABLE `process_instance_record`  (
                                            `id` bigint NOT NULL COMMENT 'id',
                                            `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程名字',
                                            `logo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '头像',
                                            `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
                                            `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                            `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                            `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                            `flow_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程id',
                                            `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程实例id',
                                            `form_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '表单数据',
                                            `group_id` bigint NULL DEFAULT NULL COMMENT '组id',
                                            `group_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组名称',
                                            `status` int NULL DEFAULT 1 COMMENT '状态',
                                            `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
                                            `parent_process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上级流程实例id',
                                            `process` json NULL,
                                            PRIMARY KEY (`id`) USING BTREE,
                                            INDEX `idx_id`(`id` ASC) USING BTREE,
                                            INDEX `idx_dep_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 478 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for process_node_data
-- ----------------------------
DROP TABLE IF EXISTS `process_node_data`;
CREATE TABLE `process_node_data`  (
                                      `id` bigint NOT NULL COMMENT 'id',
                                      `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                      `create_time` datetime NOT NULL COMMENT '创建时间',
                                      `update_time` datetime NOT NULL COMMENT '更新时间',
                                      `flow_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                                      `data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单数据',
                                      `node_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                      PRIMARY KEY (`id`) USING BTREE,
                                      INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1755 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程节点数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for process_node_record
-- ----------------------------
DROP TABLE IF EXISTS `process_node_record`;
CREATE TABLE `process_node_record`  (
                                        `id` bigint NOT NULL COMMENT 'id',
                                        `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                        `create_time` datetime NOT NULL COMMENT '创建时间',
                                        `update_time` datetime NOT NULL COMMENT '更新时间',
                                        `flow_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                                        `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程实例id',
                                        `data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '表单数据',
                                        `node_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                        `node_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '节点类型',
                                        `node_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点名字',
                                        `status` int NOT NULL COMMENT '节点状态',
                                        `start_time` datetime NOT NULL COMMENT '开始时间',
                                        `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
                                        `execution_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行id',
                                        PRIMARY KEY (`id`) USING BTREE,
                                        INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2018 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程节点记录' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for process_node_record_assign_user
-- ----------------------------
DROP TABLE IF EXISTS `process_node_record_assign_user`;
CREATE TABLE `process_node_record_assign_user`  (
                                                    `id` bigint NOT NULL COMMENT 'id',
                                                    `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                                    `create_time` datetime NOT NULL COMMENT '创建时间',
                                                    `update_time` datetime NOT NULL COMMENT '更新时间',
                                                    `flow_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                                                    `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程实例id',
                                                    `data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '表单数据',
                                                    `node_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                                    `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT ' 用户id',
                                                    `status` int NOT NULL COMMENT '节点状态',
                                                    `start_time` datetime NOT NULL COMMENT '开始时间',
                                                    `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
                                                    `execution_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行id',
                                                    `task_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ' 任务id',
                                                    `approve_desc` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批意见',
                                                    `node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ' 节点名称',
                                                    `task_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务类型',
                                                    `local_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '表单本地数据',
                                                    PRIMARY KEY (`id`) USING BTREE,
                                                    INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 845 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程节点记录-执行人' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for process_starter
-- ----------------------------
DROP TABLE IF EXISTS `process_starter`;
CREATE TABLE `process_starter`  (
                                    `id` bigint NOT NULL COMMENT 'id',
                                    `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                    `create_time` datetime NOT NULL COMMENT '创建时间',
                                    `update_time` datetime NOT NULL COMMENT '更新时间',
                                    `type_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id或者部门id',
                                    `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT ' 类型 user dept',
                                    `process_id` bigint NOT NULL COMMENT '流程id',
                                    `data` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 66 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程发起人' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
