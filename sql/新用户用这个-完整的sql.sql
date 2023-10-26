/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3307
 Source Schema         : simple_flow_biz

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 03/09/2023 00:09:29
*/

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
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of dept
-- ----------------------------
INSERT INTO `dept` VALUES (1, '第一个部门', 0, 0, 1, '2023-05-05 15:22:42', '2023-05-05 15:22:43', 1, 1);
INSERT INTO `dept` VALUES (2, '刚刚1', 1, 0, 1, '2023-05-05 15:30:10', '2023-05-05 15:50:30', 1, 1);
INSERT INTO `dept` VALUES (3, '对对对', 2, 1, 1, '2023-05-05 15:32:17', '2023-05-05 15:45:50', 1, 1);
INSERT INTO `dept` VALUES (4, '后面', 1, 1, 1, '2023-05-05 15:32:26', '2023-05-05 15:45:52', 1, 1);
INSERT INTO `dept` VALUES (5, '技术部1', 2, 0, 2, '2023-05-05 22:26:07', '2023-08-13 20:52:02', 1, 2);
INSERT INTO `dept` VALUES (6, '后端组', 5, 0, 3, '2023-05-05 23:07:20', '2023-05-05 23:07:20', 1, 1);
INSERT INTO `dept` VALUES (7, '阿斯蒂芬', 5, 1, 1, '2023-06-10 23:13:14', '2023-06-10 23:16:00', 1, 2);
INSERT INTO `dept` VALUES (8, '发发发', 5, 0, 3, '2023-07-07 00:01:54', '2023-07-07 00:01:54', 1, 3);

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
) ENGINE = InnoDB AUTO_INCREMENT = 102 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单管理' ROW_FORMAT = DYNAMIC;

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
INSERT INTO `menu` VALUES (36, 0, '0', '组件封装', 2, '/component', 'Layout', NULL, 1, 10, 'menu', '', '2022-10-31 09:18:44', '2023-07-25 22:15:15', 0);
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
INSERT INTO `menu` VALUES (90, 89, '0,89', '流程列表', 1, '/flow/list', 'cxygzl/views/flow/list', NULL, 1, 3, 'menu', '', '2023-06-09 23:35:14', '2023-09-03 00:08:49', 0);
INSERT INTO `menu` VALUES (91, 89, '0,89', '流程组', 1, '/flow/group', 'cxygzl/views/flow/group', NULL, 1, 1, 'redis', NULL, '2023-06-09 23:37:38', '2023-09-03 00:08:32', 0);
INSERT INTO `menu` VALUES (92, 0, '0', '订单的1', 1, 'test', 'test/index', NULL, 1, 1, 'cascader', NULL, '2023-06-11 08:08:24', '2023-06-11 08:46:28', 1);
INSERT INTO `menu` VALUES (93, 92, '0,92', 'asadfffff', 1, 'dept1', 'system/dept/index1', NULL, 1, 1, 'cascader', NULL, '2023-06-11 08:46:12', '2023-06-11 08:46:28', 1);
INSERT INTO `menu` VALUES (94, 89, '0,89', '创建流程', 1, '/flow/create', 'cxygzl/views/flow/create', NULL, 1, 2, 'tree', NULL, '2023-06-11 12:57:28', '2023-09-03 00:08:42', 0);
INSERT INTO `menu` VALUES (95, 0, '0', '任务管理', 2, '/task', 'Layout', NULL, 1, 1, 'number', '', '2023-06-17 23:14:09', '2023-06-17 23:16:53', 0);
INSERT INTO `menu` VALUES (96, 95, '0,95', '待办任务', 1, '/task/pending', 'cxygzl/views/task/pending', NULL, 1, 1, 'edit', NULL, '2023-06-18 15:01:34', '2023-09-03 00:07:56', 0);
INSERT INTO `menu` VALUES (97, 95, '0,95', '我的发起', 1, 'task/started', 'cxygzl/views/task/started', NULL, 1, 2, 'menu', NULL, '2023-06-18 22:07:38', '2023-09-03 00:08:04', 0);
INSERT INTO `menu` VALUES (98, 95, '0,95', '抄送给我', 1, '/task/cc', 'cxygzl/views/task/cc', NULL, 1, 3, 'dashboard', NULL, '2023-06-22 23:38:52', '2023-09-03 00:08:12', 0);
INSERT INTO `menu` VALUES (99, 95, '0,95', '我的已办', 1, '/task/completed', 'cxygzl/views/task/completed', NULL, 1, 4, 'skill', NULL, '2023-06-23 10:19:04', '2023-09-03 00:08:21', 0);
INSERT INTO `menu` VALUES (100, 1, '0,1', '属性管理', 1, 'prop', 'system/prop/index', NULL, 1, 1, 'bug', NULL, '2023-07-17 22:16:44', '2023-07-17 22:17:36', 0);
INSERT INTO `menu` VALUES (101, 1, '0,1', '消息列表', 1, 'message', 'system/message/index', NULL, 1, 1, 'message', NULL, '2023-07-25 20:37:35', '2023-07-25 20:37:35', 0);
INSERT INTO `menu` VALUES (102, 36, '0,36', '签名', 1, 'signature', 'demo/signature', NULL, 1, 1, 'menu', '', '2023-07-25 22:15:47', '2023-07-25 22:15:47', 0);

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
                            `param` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '消息参数',
                            `content` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息内容',
                            `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息头',
                            `flow_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                            `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程实例id',
                            PRIMARY KEY (`id`) USING BTREE,
                            INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知消息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of message
-- ----------------------------

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
                            `admin_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程管理员',
                            `unique_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '唯一性id',
                            `admin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员',
                            `range_show` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '范围描述显示',
                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE INDEX `idx_form_id`(`flow_id` ASC) USING BTREE,
                            INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 564 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of process
-- ----------------------------

-- ----------------------------
-- Table structure for process_copy
-- ----------------------------
DROP TABLE IF EXISTS `process_instance_copy`;
CREATE TABLE `process_instance_copy`  (
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
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程抄送数据' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of process_copy
-- ----------------------------

-- ----------------------------
-- Table structure for process_execution
-- ----------------------------
DROP TABLE IF EXISTS `process_instance_execution`;
CREATE TABLE `process_instance_execution`  (
                                      `id` bigint NOT NULL COMMENT 'id',
                                      `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                      `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                      `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                      `execution_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '执行id',
                                      `child_execution_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ' 子级执行id',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程-执行id关系' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of process_execution
-- ----------------------------

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
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of process_group
-- ----------------------------

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
                                            `result` int NULL DEFAULT NULL COMMENT '结果',
                                            PRIMARY KEY (`id`) USING BTREE,
                                            INDEX `idx_id`(`id` ASC) USING BTREE,
                                            INDEX `idx_dep_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of process_instance_record
-- ----------------------------

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
) ENGINE = InnoDB AUTO_INCREMENT = 3720 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程节点数据' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of process_node_data
-- ----------------------------

-- ----------------------------
-- Table structure for process_node_record
-- ----------------------------
DROP TABLE IF EXISTS `process_instance_node_record`;
CREATE TABLE `process_instance_node_record`  (
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
                                        `parent_node_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上一层级id',
                                        `flow_unique_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流转唯一标识',
                                        PRIMARY KEY (`id`) USING BTREE,
                                        INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB  CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程节点记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of process_node_record
-- ----------------------------

-- ----------------------------
-- Table structure for process_node_record_assign_user
-- ----------------------------
DROP TABLE IF EXISTS `process_instance_assign_user_record`;
CREATE TABLE `process_instance_assign_user_record`  (
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
                                                    `flow_unique_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流转唯一标识',
                                                    PRIMARY KEY (`id`) USING BTREE,
                                                    INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1512 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程节点记录-执行人' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of process_node_record_assign_user
-- ----------------------------

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程发起人' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of process_starter
-- ----------------------------

-- ----------------------------
-- Table structure for process_sub_process
-- ----------------------------
DROP TABLE IF EXISTS `process_sub_process`;
CREATE TABLE `process_sub_process`  (
                                        `id` bigint NOT NULL COMMENT 'id',
                                        `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                        `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                        `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                        `flow_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                                        `sub_flow_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '子流程id',
                                        PRIMARY KEY (`id`) USING BTREE,
                                        INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程关联下的子流程' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of process_sub_process
-- ----------------------------

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
) ENGINE = InnoDB AUTO_INCREMENT = 33 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色' ROW_FORMAT = DYNAMIC;

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
) ENGINE = InnoDB AUTO_INCREMENT = 489 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

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
INSERT INTO `role_menu` VALUES (2, 95, 82, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 90, 83, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 89, 84, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 91, 85, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 94, 86, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 1, 87, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 2, 88, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 31, 89, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 32, 90, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 33, 91, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 88, 92, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 3, 93, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 70, 94, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 71, 95, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 72, 96, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 4, 97, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 73, 98, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 75, 99, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 74, 100, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 5, 101, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 76, 102, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 77, 103, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 78, 104, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 6, 105, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 79, 106, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 81, 107, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 84, 108, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 85, 109, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 86, 110, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 87, 111, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 40, 112, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 41, 113, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 26, 114, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 30, 115, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 36, 116, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 37, 117, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 38, 118, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 39, 119, 1, '2023-06-17 23:14:51', '2023-06-17 23:14:51');
INSERT INTO `role_menu` VALUES (2, 95, 120, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 90, 121, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 89, 122, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 91, 123, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 94, 124, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 1, 125, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 2, 126, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 31, 127, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 32, 128, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 33, 129, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 88, 130, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 3, 131, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 70, 132, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 71, 133, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 72, 134, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 4, 135, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 73, 136, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 75, 137, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 74, 138, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 5, 139, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 76, 140, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 77, 141, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 78, 142, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 6, 143, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 79, 144, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 81, 145, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 84, 146, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 85, 147, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 86, 148, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 87, 149, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 40, 150, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 41, 151, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 26, 152, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 30, 153, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 36, 154, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 37, 155, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 38, 156, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (2, 39, 157, 1, '2023-06-17 23:19:36', '2023-06-17 23:19:36');
INSERT INTO `role_menu` VALUES (1, 95, 158, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 90, 159, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 89, 160, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 91, 161, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 94, 162, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 1, 163, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 2, 164, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 31, 165, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 32, 166, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 33, 167, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 88, 168, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 3, 169, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 70, 170, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 71, 171, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 72, 172, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 4, 173, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 73, 174, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 75, 175, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 74, 176, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 5, 177, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 76, 178, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 77, 179, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 78, 180, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 6, 181, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 79, 182, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 81, 183, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 84, 184, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 85, 185, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 86, 186, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 87, 187, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 40, 188, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 41, 189, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 26, 190, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 30, 191, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 36, 192, 0, '2023-06-17 23:20:03', '2023-06-17 23:20:03');
INSERT INTO `role_menu` VALUES (1, 37, 193, 0, '2023-06-17 23:20:04', '2023-06-17 23:20:04');
INSERT INTO `role_menu` VALUES (1, 38, 194, 0, '2023-06-17 23:20:04', '2023-06-17 23:20:04');
INSERT INTO `role_menu` VALUES (1, 39, 195, 0, '2023-06-17 23:20:04', '2023-06-17 23:20:04');
INSERT INTO `role_menu` VALUES (2, 95, 196, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 96, 197, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 89, 198, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 91, 199, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 94, 200, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 90, 201, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 1, 202, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 2, 203, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 31, 204, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 32, 205, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 33, 206, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 88, 207, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 3, 208, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 70, 209, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 71, 210, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 72, 211, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 4, 212, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 73, 213, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 75, 214, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 74, 215, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 5, 216, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 76, 217, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 77, 218, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 78, 219, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 6, 220, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 79, 221, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 81, 222, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 84, 223, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 85, 224, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 86, 225, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 87, 226, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 40, 227, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 41, 228, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 26, 229, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 30, 230, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 36, 231, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 37, 232, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 38, 233, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 39, 234, 1, '2023-06-18 15:02:10', '2023-06-18 15:02:10');
INSERT INTO `role_menu` VALUES (2, 95, 235, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 96, 236, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 97, 237, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 89, 238, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 91, 239, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 94, 240, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 90, 241, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 1, 242, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 2, 243, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 31, 244, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 32, 245, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 33, 246, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 88, 247, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 3, 248, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 70, 249, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 71, 250, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 72, 251, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 4, 252, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 73, 253, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 75, 254, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 74, 255, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 5, 256, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 76, 257, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 77, 258, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 78, 259, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 6, 260, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 79, 261, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 81, 262, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 84, 263, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 85, 264, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 86, 265, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 87, 266, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 40, 267, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 41, 268, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 26, 269, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 30, 270, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 36, 271, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 37, 272, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 38, 273, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 39, 274, 1, '2023-06-18 22:08:01', '2023-06-18 22:08:01');
INSERT INTO `role_menu` VALUES (2, 95, 275, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 96, 276, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 97, 277, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 98, 278, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 89, 279, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 91, 280, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 94, 281, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 90, 282, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 1, 283, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 2, 284, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 31, 285, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 32, 286, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 33, 287, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 88, 288, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 3, 289, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 70, 290, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 71, 291, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 72, 292, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 4, 293, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 73, 294, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 75, 295, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 74, 296, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 5, 297, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 76, 298, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 77, 299, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 78, 300, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 6, 301, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 79, 302, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 81, 303, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 84, 304, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 85, 305, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 86, 306, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 87, 307, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 40, 308, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 41, 309, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 26, 310, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 30, 311, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 36, 312, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 37, 313, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 38, 314, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 39, 315, 1, '2023-06-22 23:39:02', '2023-06-22 23:39:02');
INSERT INTO `role_menu` VALUES (2, 1, 316, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 2, 317, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 31, 318, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 32, 319, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 33, 320, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 88, 321, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 3, 322, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 70, 323, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 71, 324, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 72, 325, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 4, 326, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 73, 327, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 74, 328, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 75, 329, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 5, 330, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 76, 331, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 77, 332, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 78, 333, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 6, 334, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 79, 335, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 81, 336, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 84, 337, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 85, 338, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 86, 339, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 87, 340, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 89, 341, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 91, 342, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 94, 343, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 90, 344, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 95, 345, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 96, 346, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 97, 347, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 98, 348, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 99, 349, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 40, 350, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 41, 351, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 26, 352, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 30, 353, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 36, 354, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 37, 355, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 38, 356, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 39, 357, 1, '2023-06-23 10:19:17', '2023-06-23 10:19:17');
INSERT INTO `role_menu` VALUES (2, 95, 358, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 96, 359, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 97, 360, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 98, 361, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 99, 362, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 89, 363, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 91, 364, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 94, 365, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 90, 366, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 1, 367, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 2, 368, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 31, 369, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 32, 370, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 33, 371, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 88, 372, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 100, 373, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 3, 374, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 70, 375, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 71, 376, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 72, 377, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 4, 378, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 73, 379, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 74, 380, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 75, 381, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 5, 382, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 76, 383, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 77, 384, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 78, 385, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 6, 386, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 79, 387, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 81, 388, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 84, 389, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 85, 390, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 86, 391, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 87, 392, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 40, 393, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 41, 394, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 26, 395, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 30, 396, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 36, 397, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 37, 398, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 38, 399, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 39, 400, 1, '2023-07-17 22:17:47', '2023-07-17 22:17:47');
INSERT INTO `role_menu` VALUES (2, 95, 401, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 96, 402, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 97, 403, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 98, 404, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 99, 405, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 89, 406, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 91, 407, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 94, 408, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 90, 409, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 1, 410, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 2, 411, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 31, 412, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 32, 413, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 33, 414, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 88, 415, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 101, 416, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 100, 417, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 3, 418, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 70, 419, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 71, 420, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 72, 421, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 4, 422, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 73, 423, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 74, 424, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 75, 425, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 5, 426, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 76, 427, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 77, 428, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 78, 429, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 6, 430, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 79, 431, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 81, 432, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 84, 433, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 85, 434, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 86, 435, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 87, 436, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 40, 437, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 41, 438, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 26, 439, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 30, 440, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 36, 441, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 37, 442, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 38, 443, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 39, 444, 1, '2023-07-25 20:37:46', '2023-07-25 20:37:46');
INSERT INTO `role_menu` VALUES (2, 95, 445, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 96, 446, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 97, 447, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 98, 448, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 99, 449, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 89, 450, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 91, 451, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 94, 452, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 90, 453, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 1, 454, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 2, 455, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 31, 456, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 32, 457, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 33, 458, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 88, 459, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 101, 460, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 100, 461, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 3, 462, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 70, 463, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 71, 464, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 72, 465, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 4, 466, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 73, 467, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 75, 468, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 74, 469, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 5, 470, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 76, 471, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 77, 472, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 78, 473, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 6, 474, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 79, 475, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 81, 476, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 84, 477, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 85, 478, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 86, 479, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 87, 480, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 40, 481, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 41, 482, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 26, 483, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 30, 484, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 36, 485, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 102, 486, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 37, 487, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 38, 488, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');
INSERT INTO `role_menu` VALUES (2, 39, 489, 0, '2023-07-25 22:16:07', '2023-07-25 22:16:07');

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
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, '用户1', 'yonghu1', 'yh1', '用户1', 'https://f.ittool.cc/pic/m.jpg', 1, 1, 0, '2023-05-05', '2023-05-05 15:23:40', '2023-06-17 04:53:28', '123456', '15584589856', 1);
INSERT INTO `user` VALUES (2, '大幅度', 'dafudu', 'dfd', '大幅度', 'https://f.ittool.cc/pic/m.jpg', 2, 2, 0, '2023-05-03', '2023-05-05 21:02:30', '2023-08-13 13:04:54', '123456', '13241528569', 1);
INSERT INTO `user` VALUES (3, '山东省', NULL, NULL, '山东省', 'https://f.ittool.cc/pic/m.jpg', 2, 1, 0, '2023-05-05', '2023-05-05 21:03:32', '2023-08-13 13:04:54', '123456', '18754215896', 1);
INSERT INTO `user` VALUES (4, '地方', 'difang', 'df', '地方', 'https://f.ittool.cc/pic/m.jpg', 2, 2, 0, '2023-05-03', '2023-05-05 21:04:33', '2023-08-13 13:04:54', '123456', '13325635214', 1);
INSERT INTO `user` VALUES (5, '地方', 'difang', 'df', '地方', 'https://f.ittool.cc/pic/m.jpg', 1, 2, 0, '2023-05-02', '2023-05-05 21:06:11', '2023-08-13 13:04:54', '123456', '13552635263', 1);
INSERT INTO `user` VALUES (6, '水电费', 'shuidianfei', 'sdf', '水电费', 'https://f.ittool.cc/pic/m.jpg', 1, 2, 0, '2023-05-02', '2023-05-05 22:25:45', '2023-08-13 13:04:54', '123456', '15885968526', 1);
INSERT INTO `user` VALUES (7, '电饭锅', 'dianfanguo', 'dfg', '电饭锅', 'https://f.ittool.cc/pic/m.jpg', 1, 5, 0, '2023-05-05', '2023-05-05 22:26:27', '2023-08-13 13:04:54', '123456', '15265265238', 1);
INSERT INTO `user` VALUES (8, 'java1', NULL, NULL, 'java1', 'https://f.ittool.cc/pic/m.jpg', 1, 6, 1, '2023-05-05', '2023-05-05 23:07:39', '2023-08-13 13:04:54', NULL, NULL, 0);
INSERT INTO `user` VALUES (9, 'dddd', 'dddd', 'dddd', 'dddd', 'https://f.ittool.cc/pic/m.jpg', 1, 5, 0, '2023-05-02', '2023-05-07 11:40:55', '2023-08-13 13:04:54', '123456', '18755289563', 1);
INSERT INTO `user` VALUES (10, 'ttttt', 'ttttt', 'ttttt', 'ttttt', 'https://f.ittool.cc/pic/m.jpg', 1, 1, 0, '2023-05-07', '2023-05-07 11:41:32', '2023-08-13 13:04:54', '123456', '15748568596', 1);
INSERT INTO `user` VALUES (11, 'tttew23', 'tttew23', 'tttew23', 'tttew23', 'https://f.ittool.cc/pic/m.jpg', 1, 1, 0, '2023-05-07', '2023-05-07 11:42:31', '2023-08-13 13:04:54', '123456', '15269852365', 1);
INSERT INTO `user` VALUES (12, '测试2', 'ceshi2', 'cs2', '测试2', 'https://f.ittool.cc/pic/m.jpg', 1, 1, 0, '2023-05-07', '2023-05-07 11:50:51', '2023-08-13 13:04:54', '123456', '18752859635', 1);
INSERT INTO `user` VALUES (13, '测试111', 'ceshi111', 'cs111', '测试111', 'https://f.ittool.cc/pic/m.jpg', 2, 8, 0, '2023-05-07', '2023-05-07 11:53:14', '2023-08-13 13:04:54', '123456', '18755289564', 1);
INSERT INTO `user` VALUES (14, '是防辐', 'shifangfu', 'sff', '是防辐', 'https://f.ittool.cc/pic/m.jpg', 1, 2, 1, NULL, '2023-06-10 16:18:20', '2023-08-13 13:04:54', NULL, '15265235412', 1);
INSERT INTO `user` VALUES (15, '张三', 'zhangsan', 'zs', '张三', 'https://f.ittool.cc/pic/m.jpg', 1, 2, 0, NULL, '2023-06-10 16:35:40', '2023-08-13 13:04:54', '123456', '15265235896', 1);

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
                               `props` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置json字符串',
                               `key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段',
                               PRIMARY KEY (`id`) USING BTREE,
                               INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 115 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_field
-- ----------------------------
INSERT INTO `user_field` VALUES (17, '哈哈', 1, '2023-06-10 18:48:33', '2023-06-10 18:48:35', 'Number', 1, NULL, 'asdfas');
INSERT INTO `user_field` VALUES (18, 'a', 1, '2023-07-17 23:46:12', '2023-07-17 23:46:12', 'Number', 1, '[]', 'cxygzl_087646546015');
INSERT INTO `user_field` VALUES (19, 'b', 1, '2023-07-17 23:46:12', '2023-07-17 23:46:12', 'Input', 0, '[]', 'cxygzl_087671491569');
INSERT INTO `user_field` VALUES (20, 'a', 1, '2023-07-17 23:46:59', '2023-07-17 23:46:59', 'Number', 1, '[]', 'cxygzl_087646546015');
INSERT INTO `user_field` VALUES (21, 'b', 1, '2023-07-17 23:46:59', '2023-07-17 23:46:59', 'Input', 0, '[]', 'cxygzl_087671491569');
INSERT INTO `user_field` VALUES (22, 'c', 1, '2023-07-17 23:46:59', '2023-07-17 23:46:59', 'Time', 0, '[]', 'cxygzl_088003845841');
INSERT INTO `user_field` VALUES (23, 'd', 1, '2023-07-17 23:46:59', '2023-07-17 23:46:59', 'Select', 0, '[{\"key\":\"1\",\"value\":\"a收到\"},{\"key\":\"2\",\"value\":\"阿斯蒂芬\"}]', 'cxygzl_088052122430');
INSERT INTO `user_field` VALUES (24, 'a', 1, '2023-07-17 23:49:04', '2023-07-17 23:49:04', 'Number', 1, '[]', 'cxygzl_087646546015');
INSERT INTO `user_field` VALUES (25, 'b', 1, '2023-07-17 23:49:04', '2023-07-17 23:49:04', 'Input', 0, '[]', 'cxygzl_087671491569');
INSERT INTO `user_field` VALUES (26, 'c', 1, '2023-07-17 23:49:04', '2023-07-17 23:49:04', 'Time', 1, '[]', 'cxygzl_088003845841');
INSERT INTO `user_field` VALUES (27, 'd', 1, '2023-07-17 23:49:04', '2023-07-17 23:49:04', 'Select', 0, '[{\"key\":\"1\",\"value\":\"a收到\"},{\"key\":\"2\",\"value\":\"阿斯蒂芬\"}]', 'cxygzl_088052122430');
INSERT INTO `user_field` VALUES (28, 'a', 1, '2023-07-17 23:50:01', '2023-07-17 23:50:01', 'Number', 1, '[]', 'cxygzl_087646546015');
INSERT INTO `user_field` VALUES (29, 'c', 1, '2023-07-17 23:50:01', '2023-07-17 23:50:01', 'Time', 1, '[]', 'cxygzl_088003845841');
INSERT INTO `user_field` VALUES (30, 'd', 1, '2023-07-17 23:50:01', '2023-07-17 23:50:01', 'Select', 0, '[{\"key\":\"1\",\"value\":\"a收到\"},{\"key\":\"2\",\"value\":\"阿斯蒂芬\"}]', 'cxygzl_088052122430');
INSERT INTO `user_field` VALUES (31, 'a', 1, '2023-07-17 23:50:06', '2023-07-17 23:50:06', 'Number', 1, '[]', 'cxygzl_087646546015');
INSERT INTO `user_field` VALUES (32, 'c', 1, '2023-07-17 23:50:06', '2023-07-17 23:50:06', 'Time', 0, '[]', 'cxygzl_088003845841');
INSERT INTO `user_field` VALUES (33, 'd', 1, '2023-07-17 23:50:06', '2023-07-17 23:50:06', 'Select', 0, '[{\"key\":\"1\",\"value\":\"a收到\"},{\"key\":\"2\",\"value\":\"阿斯蒂芬\"}]', 'cxygzl_088052122430');
INSERT INTO `user_field` VALUES (34, '数字', 1, '2023-07-20 21:15:51', '2023-07-20 21:15:51', 'Number', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_589340798480');
INSERT INTO `user_field` VALUES (35, '单行文本', 1, '2023-07-20 21:15:51', '2023-07-20 21:15:51', 'Input', 1, '{\"options\":[],\"radixNum\":0}', 'cxygzl_589401979402');
INSERT INTO `user_field` VALUES (36, '数字', 1, '2023-07-20 22:39:01', '2023-07-20 22:39:01', 'Number', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_589340798480');
INSERT INTO `user_field` VALUES (37, '单行文本', 1, '2023-07-20 22:39:01', '2023-07-20 22:39:01', 'Input', 1, '{\"options\":[],\"radixNum\":0}', 'cxygzl_589401979402');
INSERT INTO `user_field` VALUES (38, '单选', 1, '2023-07-20 22:39:01', '2023-07-20 22:39:01', 'Select', 0, '{\"options\":[{\"key\":\"1\",\"value\":\"啊啊\"},{\"key\":\"2\",\"value\":\"版本\"}],\"radixNum\":0}', 'cxygzl_639227109812');
INSERT INTO `user_field` VALUES (39, '数字', 1, '2023-07-20 23:18:26', '2023-07-20 23:18:26', 'Number', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_589340798480');
INSERT INTO `user_field` VALUES (40, '单行文本', 1, '2023-07-20 23:18:26', '2023-07-20 23:18:26', 'Input', 1, '{\"options\":[],\"radixNum\":0}', 'cxygzl_589401979402');
INSERT INTO `user_field` VALUES (41, '单选', 1, '2023-07-20 23:18:26', '2023-07-20 23:18:26', 'SingleSelect', 0, '{\"options\":[{\"key\":\"1\",\"value\":\"给各个\"},{\"key\":\"2\",\"value\":\"订单的\"}],\"radixNum\":0}', 'cxygzl_662901137717');
INSERT INTO `user_field` VALUES (42, '数字', 1, '2023-07-21 23:07:22', '2023-07-21 23:07:22', 'Number', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_589340798480');
INSERT INTO `user_field` VALUES (43, '单行文本', 1, '2023-07-21 23:07:22', '2023-07-21 23:07:22', 'Input', 1, '{\"options\":[],\"radixNum\":0}', 'cxygzl_589401979402');
INSERT INTO `user_field` VALUES (44, '单选', 1, '2023-07-21 23:07:23', '2023-07-21 23:07:23', 'SingleSelect', 0, '{\"options\":[{\"key\":\"1\",\"value\":\"给各个\"},{\"key\":\"2\",\"value\":\"订单的\"}],\"radixNum\":0}', 'cxygzl_662901137717');
INSERT INTO `user_field` VALUES (45, '数字', 1, '2023-07-21 23:18:48', '2023-07-21 23:18:48', 'Number', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_589340798480');
INSERT INTO `user_field` VALUES (46, '单行文本', 1, '2023-07-21 23:18:48', '2023-07-21 23:18:48', 'Input', 1, '{\"options\":[],\"radixNum\":0}', 'cxygzl_589401979402');
INSERT INTO `user_field` VALUES (47, '单选', 1, '2023-07-21 23:18:48', '2023-07-21 23:18:48', 'SingleSelect', 0, '{\"options\":[{\"key\":\"1\",\"value\":\"给各个\"},{\"key\":\"2\",\"value\":\"订单的\"}],\"radixNum\":0}', 'cxygzl_662901137717');
INSERT INTO `user_field` VALUES (48, '日期', 1, '2023-07-21 23:18:48', '2023-07-21 23:18:48', 'Date', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526833808878');
INSERT INTO `user_field` VALUES (49, '日期时间', 1, '2023-07-21 23:18:48', '2023-07-21 23:18:48', 'DateTime', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526917176344');
INSERT INTO `user_field` VALUES (50, '时间', 1, '2023-07-21 23:18:48', '2023-07-21 23:18:48', 'Time', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526978033309');
INSERT INTO `user_field` VALUES (51, '多选', 1, '2023-07-21 23:18:48', '2023-07-21 23:18:48', 'MultiSelect', 0, '{\"options\":[{\"key\":\"a\",\"value\":\"aa\"},{\"key\":\"b\",\"value\":\"bb\"}],\"radixNum\":0}', 'cxygzl_527041476634');
INSERT INTO `user_field` VALUES (52, '数字', 1, '2023-07-21 23:35:40', '2023-07-21 23:35:40', 'Number', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_589340798480');
INSERT INTO `user_field` VALUES (53, '单行文本', 1, '2023-07-21 23:35:40', '2023-07-21 23:35:40', 'Input', 1, '{\"options\":[],\"radixNum\":0}', 'cxygzl_589401979402');
INSERT INTO `user_field` VALUES (54, '单选', 1, '2023-07-21 23:35:40', '2023-07-21 23:35:40', 'SingleSelect', 0, '{\"options\":[{\"key\":\"1\",\"value\":\"给各个\"},{\"key\":\"2\",\"value\":\"订单的\"}],\"radixNum\":0}', 'cxygzl_662901137717');
INSERT INTO `user_field` VALUES (55, '日期', 1, '2023-07-21 23:35:40', '2023-07-21 23:35:40', 'Date', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526833808878');
INSERT INTO `user_field` VALUES (56, '日期时间', 1, '2023-07-21 23:35:40', '2023-07-21 23:35:40', 'DateTime', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526917176344');
INSERT INTO `user_field` VALUES (57, '时间', 1, '2023-07-21 23:35:40', '2023-07-21 23:35:40', 'Time', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526978033309');
INSERT INTO `user_field` VALUES (58, '多选', 1, '2023-07-21 23:35:40', '2023-07-21 23:35:40', 'MultiSelect', 0, '{\"options\":[{\"key\":\"a\",\"value\":\"aa\"},{\"key\":\"b\",\"value\":\"bb\"}],\"radixNum\":0}', 'cxygzl_527041476634');
INSERT INTO `user_field` VALUES (59, '金额', 1, '2023-07-21 23:35:40', '2023-07-21 23:35:40', 'Number', 0, '{\"options\":[],\"radixNum\":4}', 'cxygzl_537339762010');
INSERT INTO `user_field` VALUES (60, '年龄', 1, '2023-07-21 23:43:04', '2023-07-21 23:43:04', 'Number', 0, '{\"options\":[],\"radixNum\":2}', 'cxygzl_589340798480');
INSERT INTO `user_field` VALUES (61, '单行文本', 1, '2023-07-21 23:43:04', '2023-07-21 23:43:04', 'Input', 1, '{\"options\":[],\"radixNum\":0}', 'cxygzl_589401979402');
INSERT INTO `user_field` VALUES (62, '单选', 1, '2023-07-21 23:43:04', '2023-07-21 23:43:04', 'SingleSelect', 0, '{\"options\":[{\"key\":\"1\",\"value\":\"给各个\"},{\"key\":\"2\",\"value\":\"订单的\"}],\"radixNum\":0}', 'cxygzl_662901137717');
INSERT INTO `user_field` VALUES (63, '日期', 1, '2023-07-21 23:43:04', '2023-07-21 23:43:04', 'Date', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526833808878');
INSERT INTO `user_field` VALUES (64, '日期时间', 1, '2023-07-21 23:43:04', '2023-07-21 23:43:04', 'DateTime', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526917176344');
INSERT INTO `user_field` VALUES (65, '时间', 1, '2023-07-21 23:43:04', '2023-07-21 23:43:04', 'Time', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526978033309');
INSERT INTO `user_field` VALUES (66, '多选', 1, '2023-07-21 23:43:04', '2023-07-21 23:43:04', 'MultiSelect', 0, '{\"options\":[{\"key\":\"a\",\"value\":\"aa\"},{\"key\":\"b\",\"value\":\"bb\"}],\"radixNum\":0}', 'cxygzl_527041476634');
INSERT INTO `user_field` VALUES (67, '金额', 1, '2023-07-21 23:43:04', '2023-07-21 23:43:04', 'Number', 0, '{\"options\":[],\"radixNum\":1}', 'cxygzl_537339762010');
INSERT INTO `user_field` VALUES (68, '年龄', 1, '2023-07-21 23:43:14', '2023-07-21 23:43:14', 'Number', 0, '{\"options\":[],\"radixNum\":2}', 'cxygzl_589340798480');
INSERT INTO `user_field` VALUES (69, '单行文本', 1, '2023-07-21 23:43:14', '2023-07-21 23:43:14', 'Input', 1, '{\"options\":[],\"radixNum\":0}', 'cxygzl_589401979402');
INSERT INTO `user_field` VALUES (70, '单选', 1, '2023-07-21 23:43:14', '2023-07-21 23:43:14', 'SingleSelect', 0, '{\"options\":[{\"key\":\"1\",\"value\":\"给各个\"},{\"key\":\"2\",\"value\":\"订单的\"}],\"radixNum\":0}', 'cxygzl_662901137717');
INSERT INTO `user_field` VALUES (71, '日期', 1, '2023-07-21 23:43:14', '2023-07-21 23:43:14', 'Date', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526833808878');
INSERT INTO `user_field` VALUES (72, '日期时间', 1, '2023-07-21 23:43:14', '2023-07-21 23:43:14', 'DateTime', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526917176344');
INSERT INTO `user_field` VALUES (73, '时间', 1, '2023-07-21 23:43:14', '2023-07-21 23:43:14', 'Time', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526978033309');
INSERT INTO `user_field` VALUES (74, '多选', 1, '2023-07-21 23:43:14', '2023-07-21 23:43:14', 'MultiSelect', 0, '{\"options\":[{\"key\":\"a\",\"value\":\"aa\"},{\"key\":\"b\",\"value\":\"bb\"}],\"radixNum\":0}', 'cxygzl_527041476634');
INSERT INTO `user_field` VALUES (75, '金额', 1, '2023-07-21 23:43:14', '2023-07-21 23:43:14', 'Number', 0, '{\"options\":[],\"radixNum\":1}', 'cxygzl_537339762010');
INSERT INTO `user_field` VALUES (76, '年龄', 1, '2023-07-21 23:45:14', '2023-07-21 23:45:14', 'Number', 0, '{\"options\":[],\"radixNum\":2}', 'cxygzl_589340798480');
INSERT INTO `user_field` VALUES (77, '单行文本', 1, '2023-07-21 23:45:14', '2023-07-21 23:45:14', 'Input', 1, '{\"options\":[],\"radixNum\":0}', 'cxygzl_589401979402');
INSERT INTO `user_field` VALUES (78, '单选', 1, '2023-07-21 23:45:14', '2023-07-21 23:45:14', 'SingleSelect', 0, '{\"options\":[{\"key\":\"1\",\"value\":\"给各个\"},{\"key\":\"2\",\"value\":\"订单的\"}],\"radixNum\":0}', 'cxygzl_662901137717');
INSERT INTO `user_field` VALUES (79, '日期', 1, '2023-07-21 23:45:14', '2023-07-21 23:45:14', 'Date', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526833808878');
INSERT INTO `user_field` VALUES (80, '日期时间', 1, '2023-07-21 23:45:14', '2023-07-21 23:45:14', 'DateTime', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526917176344');
INSERT INTO `user_field` VALUES (81, '时间', 1, '2023-07-21 23:45:14', '2023-07-21 23:45:14', 'Time', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526978033309');
INSERT INTO `user_field` VALUES (82, '多选', 1, '2023-07-21 23:45:14', '2023-07-21 23:45:14', 'MultiSelect', 0, '{\"options\":[{\"key\":\"a\",\"value\":\"aa\"},{\"key\":\"b\",\"value\":\"bb\"}],\"radixNum\":0}', 'cxygzl_527041476634');
INSERT INTO `user_field` VALUES (83, '金额', 1, '2023-07-21 23:45:14', '2023-07-21 23:45:14', 'Number', 0, '{\"options\":[],\"radixNum\":1}', 'cxygzl_537339762010');
INSERT INTO `user_field` VALUES (84, '年龄', 1, '2023-07-21 23:45:22', '2023-07-21 23:45:22', 'Number', 0, '{\"options\":[],\"radixNum\":2}', 'cxygzl_589340798480');
INSERT INTO `user_field` VALUES (85, '单行文本', 1, '2023-07-21 23:45:22', '2023-07-21 23:45:22', 'Input', 1, '{\"options\":[],\"radixNum\":0}', 'cxygzl_589401979402');
INSERT INTO `user_field` VALUES (86, '单选', 1, '2023-07-21 23:45:22', '2023-07-21 23:45:22', 'SingleSelect', 0, '{\"options\":[{\"key\":\"1\",\"value\":\"给各个\"},{\"key\":\"2\",\"value\":\"订单的\"}],\"radixNum\":0}', 'cxygzl_662901137717');
INSERT INTO `user_field` VALUES (87, '日期', 1, '2023-07-21 23:45:22', '2023-07-21 23:45:22', 'Date', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526833808878');
INSERT INTO `user_field` VALUES (88, '日期时间', 1, '2023-07-21 23:45:22', '2023-07-21 23:45:22', 'DateTime', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526917176344');
INSERT INTO `user_field` VALUES (89, '时间', 1, '2023-07-21 23:45:22', '2023-07-21 23:45:22', 'Time', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526978033309');
INSERT INTO `user_field` VALUES (90, '多选', 1, '2023-07-21 23:45:22', '2023-07-21 23:45:22', 'MultiSelect', 0, '{\"options\":[{\"key\":\"a\",\"value\":\"aa\"},{\"key\":\"b\",\"value\":\"bb\"}],\"radixNum\":0}', 'cxygzl_527041476634');
INSERT INTO `user_field` VALUES (91, '金额', 1, '2023-07-21 23:45:22', '2023-07-21 23:45:22', 'Number', 0, '{\"options\":[],\"radixNum\":1}', 'cxygzl_537339762010');
INSERT INTO `user_field` VALUES (92, '年龄', 1, '2023-07-21 23:45:38', '2023-07-21 23:45:38', 'Number', 0, '{\"options\":[],\"radixNum\":2}', 'cxygzl_589340798480');
INSERT INTO `user_field` VALUES (93, '单行文本', 1, '2023-07-21 23:45:38', '2023-07-21 23:45:38', 'Input', 1, '{\"options\":[],\"radixNum\":0}', 'cxygzl_589401979402');
INSERT INTO `user_field` VALUES (94, '单选', 1, '2023-07-21 23:45:38', '2023-07-21 23:45:38', 'SingleSelect', 0, '{\"options\":[{\"key\":\"1\",\"value\":\"给各个\"},{\"key\":\"2\",\"value\":\"订单的\"}],\"radixNum\":0}', 'cxygzl_662901137717');
INSERT INTO `user_field` VALUES (95, '日期', 1, '2023-07-21 23:45:38', '2023-07-21 23:45:38', 'Date', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526833808878');
INSERT INTO `user_field` VALUES (96, '日期时间', 1, '2023-07-21 23:45:38', '2023-07-21 23:45:38', 'DateTime', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526917176344');
INSERT INTO `user_field` VALUES (97, '时间', 1, '2023-07-21 23:45:38', '2023-07-21 23:45:38', 'Time', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526978033309');
INSERT INTO `user_field` VALUES (98, '多选', 1, '2023-07-21 23:45:38', '2023-07-21 23:45:38', 'MultiSelect', 0, '{\"options\":[{\"key\":\"a\",\"value\":\"aa\"},{\"key\":\"b\",\"value\":\"bb\"}],\"radixNum\":0}', 'cxygzl_527041476634');
INSERT INTO `user_field` VALUES (99, '金额', 1, '2023-07-21 23:45:38', '2023-07-21 23:45:38', 'Number', 0, '{\"options\":[],\"radixNum\":1}', 'cxygzl_537339762010');
INSERT INTO `user_field` VALUES (100, '年龄', 1, '2023-07-21 23:45:40', '2023-07-21 23:45:40', 'Number', 0, '{\"options\":[],\"radixNum\":2}', 'cxygzl_589340798480');
INSERT INTO `user_field` VALUES (101, '单行文本', 1, '2023-07-21 23:45:40', '2023-07-21 23:45:40', 'Input', 1, '{\"options\":[],\"radixNum\":0}', 'cxygzl_589401979402');
INSERT INTO `user_field` VALUES (102, '单选', 1, '2023-07-21 23:45:40', '2023-07-21 23:45:40', 'SingleSelect', 0, '{\"options\":[{\"key\":\"1\",\"value\":\"给各个\"},{\"key\":\"2\",\"value\":\"订单的\"}],\"radixNum\":0}', 'cxygzl_662901137717');
INSERT INTO `user_field` VALUES (103, '日期', 1, '2023-07-21 23:45:40', '2023-07-21 23:45:40', 'Date', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526833808878');
INSERT INTO `user_field` VALUES (104, '日期时间', 1, '2023-07-21 23:45:40', '2023-07-21 23:45:40', 'DateTime', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526917176344');
INSERT INTO `user_field` VALUES (105, '时间', 1, '2023-07-21 23:45:40', '2023-07-21 23:45:40', 'Time', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526978033309');
INSERT INTO `user_field` VALUES (106, '多选', 1, '2023-07-21 23:45:40', '2023-07-21 23:45:40', 'MultiSelect', 0, '{\"options\":[{\"key\":\"a\",\"value\":\"aa\"},{\"key\":\"b\",\"value\":\"bb\"}],\"radixNum\":0}', 'cxygzl_527041476634');
INSERT INTO `user_field` VALUES (107, '金额', 1, '2023-07-21 23:45:40', '2023-07-21 23:45:40', 'Number', 0, '{\"options\":[],\"radixNum\":1}', 'cxygzl_537339762010');
INSERT INTO `user_field` VALUES (108, '年龄', 0, '2023-07-21 23:47:43', '2023-07-21 23:47:43', 'Number', 0, '{\"options\":[],\"radixNum\":2}', 'cxygzl_589340798480');
INSERT INTO `user_field` VALUES (109, '单行文本', 0, '2023-07-21 23:47:43', '2023-07-21 23:47:43', 'Input', 1, '{\"options\":[],\"radixNum\":0}', 'cxygzl_589401979402');
INSERT INTO `user_field` VALUES (110, '单选', 0, '2023-07-21 23:47:43', '2023-07-21 23:47:43', 'SingleSelect', 0, '{\"options\":[{\"key\":\"1\",\"value\":\"给各个\"},{\"key\":\"2\",\"value\":\"订单的\"}],\"radixNum\":0}', 'cxygzl_662901137717');
INSERT INTO `user_field` VALUES (111, '日期', 0, '2023-07-21 23:47:43', '2023-07-21 23:47:43', 'Date', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526833808878');
INSERT INTO `user_field` VALUES (112, '日期时间', 0, '2023-07-21 23:47:43', '2023-07-21 23:47:43', 'DateTime', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526917176344');
INSERT INTO `user_field` VALUES (113, '时间', 0, '2023-07-21 23:47:43', '2023-07-21 23:47:43', 'Time', 0, '{\"options\":[],\"radixNum\":0}', 'cxygzl_526978033309');
INSERT INTO `user_field` VALUES (114, '多选', 0, '2023-07-21 23:47:43', '2023-07-21 23:47:43', 'MultiSelect', 0, '{\"options\":[{\"key\":\"a\",\"value\":\"aa\"},{\"key\":\"b\",\"value\":\"bb\"}],\"radixNum\":0}', 'cxygzl_527041476634');
INSERT INTO `user_field` VALUES (115, '金额', 0, '2023-07-21 23:47:43', '2023-07-21 23:47:43', 'Number', 0, '{\"options\":[],\"radixNum\":6}', 'cxygzl_537339762010');

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
) ENGINE = InnoDB AUTO_INCREMENT = 128 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户字段-数据' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_field_data
-- ----------------------------
INSERT INTO `user_field_data` VALUES (30, 15, 1, '2023-07-20 21:19:58', '2023-07-20 21:19:58', '啊啊', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (31, 15, 1, '2023-07-20 21:20:04', '2023-07-20 21:20:04', '2', 'cxygzl_589340798480');
INSERT INTO `user_field_data` VALUES (32, 15, 1, '2023-07-20 21:20:04', '2023-07-20 21:20:04', '啊啊', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (33, 13, 1, '2023-07-20 23:49:05', '2023-07-20 23:49:05', '3', 'cxygzl_589340798480');
INSERT INTO `user_field_data` VALUES (34, 13, 1, '2023-07-20 23:49:05', '2023-07-20 23:49:05', '啊333', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (35, 13, 1, '2023-07-20 23:49:05', '2023-07-20 23:49:05', '1', 'cxygzl_662901137717');
INSERT INTO `user_field_data` VALUES (36, 15, 1, '2023-07-20 23:49:29', '2023-07-20 23:49:29', '2', 'cxygzl_589340798480');
INSERT INTO `user_field_data` VALUES (37, 15, 1, '2023-07-20 23:49:29', '2023-07-20 23:49:29', '啊啊', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (38, 15, 1, '2023-07-20 23:49:29', '2023-07-20 23:49:29', '2', 'cxygzl_662901137717');
INSERT INTO `user_field_data` VALUES (39, 13, 1, '2023-07-21 22:55:31', '2023-07-21 22:55:31', '3', 'cxygzl_589340798480');
INSERT INTO `user_field_data` VALUES (40, 13, 1, '2023-07-21 22:55:31', '2023-07-21 22:55:31', '44', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (41, 13, 1, '2023-07-21 22:55:31', '2023-07-21 22:55:31', '1', 'cxygzl_662901137717');
INSERT INTO `user_field_data` VALUES (42, 13, 1, '2023-07-21 23:03:33', '2023-07-21 23:03:33', '3', 'cxygzl_589340798480');
INSERT INTO `user_field_data` VALUES (43, 13, 1, '2023-07-21 23:03:33', '2023-07-21 23:03:33', '441', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (44, 13, 1, '2023-07-21 23:03:33', '2023-07-21 23:03:33', '1', 'cxygzl_662901137717');
INSERT INTO `user_field_data` VALUES (45, 13, 1, '2023-07-21 23:07:02', '2023-07-21 23:07:02', '1', 'cxygzl_589340798480');
INSERT INTO `user_field_data` VALUES (46, 13, 1, '2023-07-21 23:07:02', '2023-07-21 23:07:02', '441', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (47, 13, 1, '2023-07-21 23:07:02', '2023-07-21 23:07:02', '1', 'cxygzl_662901137717');
INSERT INTO `user_field_data` VALUES (48, 13, 1, '2023-07-21 23:19:07', '2023-07-21 23:19:07', '1', 'cxygzl_589340798480');
INSERT INTO `user_field_data` VALUES (49, 13, 1, '2023-07-21 23:19:07', '2023-07-21 23:19:07', '441', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (50, 13, 1, '2023-07-21 23:19:07', '2023-07-21 23:19:07', '1', 'cxygzl_662901137717');
INSERT INTO `user_field_data` VALUES (51, 13, 1, '2023-07-21 23:19:07', '2023-07-21 23:19:07', '2023-07-20T16:00:00.000Z', 'cxygzl_526833808878');
INSERT INTO `user_field_data` VALUES (52, 13, 1, '2023-07-21 23:19:07', '2023-07-21 23:19:07', '2023-07-21T15:18:58.000Z', 'cxygzl_526917176344');
INSERT INTO `user_field_data` VALUES (53, 13, 1, '2023-07-21 23:19:07', '2023-07-21 23:19:07', '2023-07-21T15:18:52.263Z', 'cxygzl_526978033309');
INSERT INTO `user_field_data` VALUES (54, 13, 1, '2023-07-21 23:19:07', '2023-07-21 23:19:07', '[\"a\",\"b\"]', 'cxygzl_527041476634');
INSERT INTO `user_field_data` VALUES (55, 13, 1, '2023-07-21 23:22:42', '2023-07-21 23:22:42', '1', 'cxygzl_589340798480');
INSERT INTO `user_field_data` VALUES (56, 13, 1, '2023-07-21 23:22:42', '2023-07-21 23:22:42', '441', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (57, 13, 1, '2023-07-21 23:22:42', '2023-07-21 23:22:42', '1', 'cxygzl_662901137717');
INSERT INTO `user_field_data` VALUES (58, 13, 1, '2023-07-21 23:22:42', '2023-07-21 23:22:42', '2023-07-20T16:00:00.000Z', 'cxygzl_526833808878');
INSERT INTO `user_field_data` VALUES (59, 13, 1, '2023-07-21 23:22:42', '2023-07-21 23:22:42', '2023-07-21T15:18:58.000Z', 'cxygzl_526917176344');
INSERT INTO `user_field_data` VALUES (60, 13, 1, '2023-07-21 23:22:42', '2023-07-21 23:22:42', '2023-07-21T15:18:52.263Z', 'cxygzl_526978033309');
INSERT INTO `user_field_data` VALUES (61, 13, 1, '2023-07-21 23:22:42', '2023-07-21 23:22:42', '[\"a\",\"b\"]', 'cxygzl_527041476634');
INSERT INTO `user_field_data` VALUES (62, 13, 1, '2023-07-21 23:23:13', '2023-07-21 23:23:13', '1', 'cxygzl_589340798480');
INSERT INTO `user_field_data` VALUES (63, 13, 1, '2023-07-21 23:23:13', '2023-07-21 23:23:13', '441', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (64, 13, 1, '2023-07-21 23:23:13', '2023-07-21 23:23:13', '1', 'cxygzl_662901137717');
INSERT INTO `user_field_data` VALUES (65, 13, 1, '2023-07-21 23:23:13', '2023-07-21 23:23:13', '2023-07-19', 'cxygzl_526833808878');
INSERT INTO `user_field_data` VALUES (66, 13, 1, '2023-07-21 23:23:13', '2023-07-21 23:23:13', '2023-07-21 23:23:03', 'cxygzl_526917176344');
INSERT INTO `user_field_data` VALUES (67, 13, 1, '2023-07-21 23:23:13', '2023-07-21 23:23:13', '20:03:07', 'cxygzl_526978033309');
INSERT INTO `user_field_data` VALUES (68, 13, 1, '2023-07-21 23:23:13', '2023-07-21 23:23:13', '[\"\\\"a\\\"\",\"\\\"b\\\"\"]', 'cxygzl_527041476634');
INSERT INTO `user_field_data` VALUES (69, 13, 1, '2023-07-21 23:24:17', '2023-07-21 23:24:17', '1', 'cxygzl_589340798480');
INSERT INTO `user_field_data` VALUES (70, 13, 1, '2023-07-21 23:24:17', '2023-07-21 23:24:17', '441', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (71, 13, 1, '2023-07-21 23:24:17', '2023-07-21 23:24:17', '1', 'cxygzl_662901137717');
INSERT INTO `user_field_data` VALUES (72, 13, 1, '2023-07-21 23:24:17', '2023-07-21 23:24:17', '2023-07-21', 'cxygzl_526833808878');
INSERT INTO `user_field_data` VALUES (73, 13, 1, '2023-07-21 23:24:17', '2023-07-21 23:24:17', '2023-07-21 23:23:03', 'cxygzl_526917176344');
INSERT INTO `user_field_data` VALUES (74, 13, 1, '2023-07-21 23:24:17', '2023-07-21 23:24:17', '20:03:07', 'cxygzl_526978033309');
INSERT INTO `user_field_data` VALUES (75, 13, 1, '2023-07-21 23:24:17', '2023-07-21 23:24:17', '[\"\\\"\\\\\\\"a\\\\\\\"\\\"\",\"\\\"\\\\\\\"b\\\\\\\"\\\"\"]', 'cxygzl_527041476634');
INSERT INTO `user_field_data` VALUES (76, 13, 1, '2023-07-21 23:27:58', '2023-07-21 23:27:58', '1', 'cxygzl_589340798480');
INSERT INTO `user_field_data` VALUES (77, 13, 1, '2023-07-21 23:27:58', '2023-07-21 23:27:58', '441', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (78, 13, 1, '2023-07-21 23:27:58', '2023-07-21 23:27:58', '2', 'cxygzl_662901137717');
INSERT INTO `user_field_data` VALUES (79, 13, 1, '2023-07-21 23:27:58', '2023-07-21 23:27:58', '2023-07-21', 'cxygzl_526833808878');
INSERT INTO `user_field_data` VALUES (80, 13, 1, '2023-07-21 23:27:58', '2023-07-21 23:27:58', '2023-07-21 23:23:03', 'cxygzl_526917176344');
INSERT INTO `user_field_data` VALUES (81, 13, 1, '2023-07-21 23:27:58', '2023-07-21 23:27:58', '20:03:07', 'cxygzl_526978033309');
INSERT INTO `user_field_data` VALUES (82, 13, 1, '2023-07-21 23:27:58', '2023-07-21 23:27:58', '[\"\\\"\\\\\\\"\\\\\\\\\\\\\\\"a\\\\\\\\\\\\\\\"\\\\\\\"\\\"\",\"\\\"\\\\\\\"\\\\\\\\\\\\\\\"b\\\\\\\\\\\\\\\"\\\\\\\"\\\"\"]', 'cxygzl_527041476634');
INSERT INTO `user_field_data` VALUES (83, 13, 1, '2023-07-21 23:35:49', '2023-07-21 23:35:49', '1', 'cxygzl_589340798480');
INSERT INTO `user_field_data` VALUES (84, 13, 1, '2023-07-21 23:35:49', '2023-07-21 23:35:49', '441', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (85, 13, 1, '2023-07-21 23:35:49', '2023-07-21 23:35:49', '2', 'cxygzl_662901137717');
INSERT INTO `user_field_data` VALUES (86, 13, 1, '2023-07-21 23:35:49', '2023-07-21 23:35:49', '2023-07-21', 'cxygzl_526833808878');
INSERT INTO `user_field_data` VALUES (87, 13, 1, '2023-07-21 23:35:49', '2023-07-21 23:35:49', '2023-07-21 23:23:03', 'cxygzl_526917176344');
INSERT INTO `user_field_data` VALUES (88, 13, 1, '2023-07-21 23:35:49', '2023-07-21 23:35:49', '20:03:07', 'cxygzl_526978033309');
INSERT INTO `user_field_data` VALUES (89, 13, 1, '2023-07-21 23:35:49', '2023-07-21 23:35:49', '[]', 'cxygzl_527041476634');
INSERT INTO `user_field_data` VALUES (90, 13, 1, '2023-07-21 23:35:49', '2023-07-21 23:35:49', '6', 'cxygzl_537339762010');
INSERT INTO `user_field_data` VALUES (91, 15, 1, '2023-07-26 21:40:43', '2023-07-26 21:40:43', '2', 'cxygzl_589340798480');
INSERT INTO `user_field_data` VALUES (92, 15, 1, '2023-07-26 21:40:43', '2023-07-26 21:40:43', '啊啊', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (93, 15, 1, '2023-07-26 21:40:43', '2023-07-26 21:40:43', '2', 'cxygzl_662901137717');
INSERT INTO `user_field_data` VALUES (94, 15, 1, '2023-07-26 21:40:43', '2023-07-26 21:40:43', '[]', 'cxygzl_527041476634');
INSERT INTO `user_field_data` VALUES (95, 15, 1, '2023-07-26 21:51:42', '2023-07-26 21:51:42', '2', 'cxygzl_589340798480');
INSERT INTO `user_field_data` VALUES (96, 15, 1, '2023-07-26 21:51:42', '2023-07-26 21:51:42', '啊啊', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (97, 15, 1, '2023-07-26 21:51:42', '2023-07-26 21:51:42', '2', 'cxygzl_662901137717');
INSERT INTO `user_field_data` VALUES (98, 15, 1, '2023-07-26 21:51:42', '2023-07-26 21:51:42', '[]', 'cxygzl_527041476634');
INSERT INTO `user_field_data` VALUES (99, 13, 1, '2023-07-28 23:04:47', '2023-07-28 23:04:47', '1', 'cxygzl_589340798480');
INSERT INTO `user_field_data` VALUES (100, 13, 1, '2023-07-28 23:04:47', '2023-07-28 23:04:47', '441', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (101, 13, 1, '2023-07-28 23:04:47', '2023-07-28 23:04:47', '2', 'cxygzl_662901137717');
INSERT INTO `user_field_data` VALUES (102, 13, 1, '2023-07-28 23:04:47', '2023-07-28 23:04:47', '2023-07-21', 'cxygzl_526833808878');
INSERT INTO `user_field_data` VALUES (103, 13, 1, '2023-07-28 23:04:47', '2023-07-28 23:04:47', '2023-07-21 23:23:03', 'cxygzl_526917176344');
INSERT INTO `user_field_data` VALUES (104, 13, 1, '2023-07-28 23:04:47', '2023-07-28 23:04:47', '20:03:07', 'cxygzl_526978033309');
INSERT INTO `user_field_data` VALUES (105, 13, 1, '2023-07-28 23:04:47', '2023-07-28 23:04:47', '[\"a\",\"b\"]', 'cxygzl_527041476634');
INSERT INTO `user_field_data` VALUES (106, 13, 1, '2023-07-28 23:04:47', '2023-07-28 23:04:47', '6', 'cxygzl_537339762010');
INSERT INTO `user_field_data` VALUES (107, 13, 1, '2023-07-28 23:16:56', '2023-07-28 23:16:56', '1', 'cxygzl_589340798480');
INSERT INTO `user_field_data` VALUES (108, 13, 1, '2023-07-28 23:16:56', '2023-07-28 23:16:56', '441', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (109, 13, 1, '2023-07-28 23:16:56', '2023-07-28 23:16:56', '1', 'cxygzl_662901137717');
INSERT INTO `user_field_data` VALUES (110, 13, 1, '2023-07-28 23:16:56', '2023-07-28 23:16:56', '2023-07-21', 'cxygzl_526833808878');
INSERT INTO `user_field_data` VALUES (111, 13, 1, '2023-07-28 23:16:56', '2023-07-28 23:16:56', '2023-07-21 23:23:03', 'cxygzl_526917176344');
INSERT INTO `user_field_data` VALUES (112, 13, 1, '2023-07-28 23:16:56', '2023-07-28 23:16:56', '20:03:07', 'cxygzl_526978033309');
INSERT INTO `user_field_data` VALUES (113, 13, 1, '2023-07-28 23:16:56', '2023-07-28 23:16:56', '[\"\\\"a\\\"\",\"\\\"b\\\"\"]', 'cxygzl_527041476634');
INSERT INTO `user_field_data` VALUES (114, 13, 1, '2023-07-28 23:16:56', '2023-07-28 23:16:56', '6', 'cxygzl_537339762010');
INSERT INTO `user_field_data` VALUES (115, 12, 0, '2023-07-30 00:51:57', '2023-07-30 00:51:57', '234', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (116, 12, 0, '2023-07-30 00:51:57', '2023-07-30 00:51:57', '1', 'cxygzl_662901137717');
INSERT INTO `user_field_data` VALUES (117, 13, 0, '2023-08-13 20:46:12', '2023-08-13 20:46:12', '1', 'cxygzl_589340798480');
INSERT INTO `user_field_data` VALUES (118, 13, 0, '2023-08-13 20:46:12', '2023-08-13 20:46:12', '441', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (119, 13, 0, '2023-08-13 20:46:12', '2023-08-13 20:46:12', '1', 'cxygzl_662901137717');
INSERT INTO `user_field_data` VALUES (120, 13, 0, '2023-08-13 20:46:12', '2023-08-13 20:46:12', '2023-07-21', 'cxygzl_526833808878');
INSERT INTO `user_field_data` VALUES (121, 13, 0, '2023-08-13 20:46:12', '2023-08-13 20:46:12', '2023-07-21 23:23:03', 'cxygzl_526917176344');
INSERT INTO `user_field_data` VALUES (122, 13, 0, '2023-08-13 20:46:12', '2023-08-13 20:46:12', '20:03:07', 'cxygzl_526978033309');
INSERT INTO `user_field_data` VALUES (123, 13, 0, '2023-08-13 20:46:12', '2023-08-13 20:46:12', '[]', 'cxygzl_527041476634');
INSERT INTO `user_field_data` VALUES (124, 13, 0, '2023-08-13 20:46:12', '2023-08-13 20:46:12', '6', 'cxygzl_537339762010');
INSERT INTO `user_field_data` VALUES (125, 15, 0, '2023-08-13 20:54:06', '2023-08-13 20:54:06', '2', 'cxygzl_589340798480');
INSERT INTO `user_field_data` VALUES (126, 15, 0, '2023-08-13 20:54:06', '2023-08-13 20:54:06', '啊啊', 'cxygzl_589401979402');
INSERT INTO `user_field_data` VALUES (127, 15, 0, '2023-08-13 20:54:06', '2023-08-13 20:54:06', '2', 'cxygzl_662901137717');
INSERT INTO `user_field_data` VALUES (128, 15, 0, '2023-08-13 20:54:06', '2023-08-13 20:54:06', '[]', 'cxygzl_527041476634');

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
) ENGINE = InnoDB AUTO_INCREMENT = 67 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户-角色' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES (30, 1, '2023-06-08 23:10:30', '2023-06-08 23:10:33', 13, 2);
INSERT INTO `user_role` VALUES (31, 0, '2023-06-10 16:22:03', '2023-06-10 16:22:03', 14, 1);
INSERT INTO `user_role` VALUES (32, 0, '2023-06-10 16:22:03', '2023-06-10 16:22:03', 14, 2);
INSERT INTO `user_role` VALUES (33, 1, '2023-06-10 16:35:40', '2023-06-10 16:35:40', 15, 1);
INSERT INTO `user_role` VALUES (34, 1, '2023-06-10 16:59:40', '2023-06-10 16:59:40', 13, 2);
INSERT INTO `user_role` VALUES (35, 0, '2023-06-10 17:06:12', '2023-06-10 17:06:12', 11, 2);
INSERT INTO `user_role` VALUES (36, 0, '2023-06-10 18:48:07', '2023-06-10 18:48:07', 7, 2);
INSERT INTO `user_role` VALUES (37, 0, '2023-06-17 12:54:04', '2023-06-17 12:54:04', 1, 1);
INSERT INTO `user_role` VALUES (38, 1, '2023-06-20 21:59:37', '2023-06-20 21:59:37', 15, 1);
INSERT INTO `user_role` VALUES (39, 0, '2023-06-20 21:59:54', '2023-06-20 21:59:54', 10, 1);
INSERT INTO `user_role` VALUES (40, 0, '2023-06-20 22:00:05', '2023-06-20 22:00:05', 9, 1);
INSERT INTO `user_role` VALUES (41, 0, '2023-06-20 22:00:22', '2023-06-20 22:00:22', 6, 1);
INSERT INTO `user_role` VALUES (42, 0, '2023-06-20 22:00:40', '2023-06-20 22:00:40', 5, 1);
INSERT INTO `user_role` VALUES (43, 0, '2023-06-20 22:00:54', '2023-06-20 22:00:54', 4, 1);
INSERT INTO `user_role` VALUES (44, 0, '2023-06-20 22:01:09', '2023-06-20 22:01:09', 2, 1);
INSERT INTO `user_role` VALUES (45, 1, '2023-07-20 21:16:03', '2023-07-20 21:16:03', 15, 1);
INSERT INTO `user_role` VALUES (46, 1, '2023-07-20 21:16:12', '2023-07-20 21:16:12', 15, 1);
INSERT INTO `user_role` VALUES (47, 1, '2023-07-20 21:16:32', '2023-07-20 21:16:32', 15, 1);
INSERT INTO `user_role` VALUES (48, 1, '2023-07-20 21:19:58', '2023-07-20 21:19:58', 15, 1);
INSERT INTO `user_role` VALUES (49, 1, '2023-07-20 21:20:04', '2023-07-20 21:20:04', 15, 1);
INSERT INTO `user_role` VALUES (50, 1, '2023-07-20 23:49:05', '2023-07-20 23:49:05', 13, 2);
INSERT INTO `user_role` VALUES (51, 1, '2023-07-20 23:49:29', '2023-07-20 23:49:29', 15, 1);
INSERT INTO `user_role` VALUES (52, 1, '2023-07-21 22:55:31', '2023-07-21 22:55:31', 13, 2);
INSERT INTO `user_role` VALUES (53, 1, '2023-07-21 23:03:33', '2023-07-21 23:03:33', 13, 2);
INSERT INTO `user_role` VALUES (54, 1, '2023-07-21 23:07:02', '2023-07-21 23:07:02', 13, 2);
INSERT INTO `user_role` VALUES (55, 1, '2023-07-21 23:19:07', '2023-07-21 23:19:07', 13, 2);
INSERT INTO `user_role` VALUES (56, 1, '2023-07-21 23:22:42', '2023-07-21 23:22:42', 13, 2);
INSERT INTO `user_role` VALUES (57, 1, '2023-07-21 23:23:13', '2023-07-21 23:23:13', 13, 2);
INSERT INTO `user_role` VALUES (58, 1, '2023-07-21 23:24:17', '2023-07-21 23:24:17', 13, 2);
INSERT INTO `user_role` VALUES (59, 1, '2023-07-21 23:27:58', '2023-07-21 23:27:58', 13, 2);
INSERT INTO `user_role` VALUES (60, 1, '2023-07-21 23:35:49', '2023-07-21 23:35:49', 13, 2);
INSERT INTO `user_role` VALUES (61, 1, '2023-07-26 21:40:43', '2023-07-26 21:40:43', 15, 1);
INSERT INTO `user_role` VALUES (62, 1, '2023-07-26 21:51:42', '2023-07-26 21:51:42', 15, 1);
INSERT INTO `user_role` VALUES (63, 1, '2023-07-28 23:04:47', '2023-07-28 23:04:47', 13, 2);
INSERT INTO `user_role` VALUES (64, 1, '2023-07-28 23:16:56', '2023-07-28 23:16:56', 13, 2);
INSERT INTO `user_role` VALUES (65, 0, '2023-07-30 00:51:57', '2023-07-30 00:51:57', 12, 1);
INSERT INTO `user_role` VALUES (66, 0, '2023-08-13 20:46:12', '2023-08-13 20:46:12', 13, 2);
INSERT INTO `user_role` VALUES (67, 0, '2023-08-13 20:54:06', '2023-08-13 20:54:06', 15, 1);

-- ----------------------------
-- Table structure for weixin_user
-- ----------------------------
DROP TABLE IF EXISTS `weixin_user`;
CREATE TABLE `weixin_user`  (
                                `id` bigint NOT NULL COMMENT 'id',
                                `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
                                `union_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'union_id',
                                `open_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'open_id',
                                `phone` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
                                PRIMARY KEY (`id`) USING BTREE,
                                INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '微信用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of weixin_user
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;


INSERT INTO  `menu` (`id`, `parent_id`, `tree_path`, `name`, `type`, `path`, `component`, `perm`, `visible`, `sort`, `icon`, `redirect`, `create_time`, `update_time`, `del_flag`) VALUES (1706144197164838914, 89, '0,89', '流程报表', 1, '/flow/data', 'cxygzl/views/flow/flowdata', NULL, 0, 1, 'document', NULL, '2023-09-25 11:10:50', '2023-09-25 11:10:50', 0);
INSERT INTO  `role_menu` (`role_id`, `menu_id`, `id`, `del_flag`, `create_time`, `update_time`) VALUES (2, 1706144197164838914, 1706144314223669259, 0, '2023-09-25 11:11:18', '2023-09-25 11:11:18');


INSERT INTO   `menu` (`id`, `parent_id`, `tree_path`, `name`, `type`, `path`, `component`, `perm`, `visible`, `sort`, `icon`, `redirect`, `create_time`, `update_time`, `del_flag`) VALUES (1716270348700979202, 1, '0,1', '前端版本维护', 1, 'system/version', 'cxygzl/views/system/version', NULL, 1, 1, 'language', NULL, '2023-10-23 09:48:33', '2023-10-23 09:48:33', 0);

INSERT INTO  `role_menu` (`role_id`, `menu_id`, `id`, `del_flag`, `create_time`, `update_time`) VALUES (2, 1716270348700979202, 1716270422453620740, 0, '2023-10-23 09:48:50', '2023-10-23 09:48:50');


CREATE TABLE `signature_record` (
                                    `id` bigint NOT NULL COMMENT 'id',
                                    `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                    `create_time` datetime NOT NULL COMMENT '创建时间',
                                    `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片地址',
                                    `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='签名记录';

CREATE TABLE `process_instance_user_copy` (
                                              `id` bigint NOT NULL COMMENT 'id',
                                              `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                              `create_time` datetime NOT NULL COMMENT '创建时间',
                                              `update_time` datetime NOT NULL COMMENT '更新时间',
                                              `start_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发起人',
                                              `flow_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                                              `process_instance_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '实例id',
                                              `group_id` bigint NOT NULL COMMENT '分组id',
                                              `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分组名称',
                                              `process_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程名称',
                                              `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '抄送人id',
                                              PRIMARY KEY (`id`) USING BTREE,
                                              KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='流程抄送数据--用户和实例唯一值';
