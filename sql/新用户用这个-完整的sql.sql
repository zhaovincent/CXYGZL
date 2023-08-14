

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dept
-- ----------------------------
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept`  (
                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
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
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for menu
-- ----------------------------
DROP TABLE IF EXISTS `menu`;
CREATE TABLE `menu`  (
                         `id` bigint NOT NULL AUTO_INCREMENT,
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
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for message
-- ----------------------------
DROP TABLE IF EXISTS `message`;
CREATE TABLE `message`  (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
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
) ENGINE = InnoDB AUTO_INCREMENT = 516 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '通知消息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for process
-- ----------------------------
DROP TABLE IF EXISTS `process`;
CREATE TABLE `process`  (
                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
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
                            `admin_id` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '流程管理员',
                            `unique_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '唯一性id',
                            `admin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理员',
                            `range_show` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '范围描述显示',
                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE INDEX `idx_form_id`(`flow_id` ASC) USING BTREE,
                            INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 564 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for process_copy
-- ----------------------------
DROP TABLE IF EXISTS `process_copy`;
CREATE TABLE `process_copy`  (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
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
) ENGINE = InnoDB AUTO_INCREMENT = 90 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程抄送数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for process_execution
-- ----------------------------
DROP TABLE IF EXISTS `process_execution`;
CREATE TABLE `process_execution`  (
                                      `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
                                      `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                      `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                      `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                      `execution_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '执行id',
                                      `child_execution_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ' 子级执行id',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 217 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程-执行id关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for process_group
-- ----------------------------
DROP TABLE IF EXISTS `process_group`;
CREATE TABLE `process_group`  (
                                  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
                                  `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分组名',
                                  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
                                  PRIMARY KEY (`id`) USING BTREE,
                                  INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for process_instance_record
-- ----------------------------
DROP TABLE IF EXISTS `process_instance_record`;
CREATE TABLE `process_instance_record`  (
                                            `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
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
                                            `process` json NULL COMMENT '节点对象',
                                            PRIMARY KEY (`id`) USING BTREE,
                                            INDEX `idx_id`(`id` ASC) USING BTREE,
                                            INDEX `idx_dep_id`(`user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 840 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for process_node_data
-- ----------------------------
DROP TABLE IF EXISTS `process_node_data`;
CREATE TABLE `process_node_data`  (
                                      `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
                                      `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                      `create_time` datetime NOT NULL COMMENT '创建时间',
                                      `update_time` datetime NOT NULL COMMENT '更新时间',
                                      `flow_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                                      `data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单数据',
                                      `node_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                      PRIMARY KEY (`id`) USING BTREE,
                                      INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3718 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程节点数据' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for process_node_record
-- ----------------------------
DROP TABLE IF EXISTS `process_node_record`;
CREATE TABLE `process_node_record`  (
                                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
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
) ENGINE = InnoDB AUTO_INCREMENT = 3779 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程节点记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for process_node_record_approve_desc
-- ----------------------------
DROP TABLE IF EXISTS `process_node_record_approve_desc`;
CREATE TABLE `process_node_record_approve_desc`  (
                                                     `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
                                                     `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                                     `create_time` datetime NOT NULL COMMENT '创建时间',
                                                     `update_time` datetime NOT NULL COMMENT '更新时间',
                                                     `flow_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                                                     `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程实例id',
                                                     `node_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                                     `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT ' 用户id',
                                                     `execution_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行id',
                                                     `task_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ' 任务id',
                                                     `approve_desc` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '审批意见',
                                                     `node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT ' 节点名称',
                                                     `desc_date` datetime NULL DEFAULT NULL COMMENT '评论时间',
                                                     `desc_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息id',
                                                     `desc_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型',
                                                     PRIMARY KEY (`id`) USING BTREE,
                                                     INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1344 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程节点记录-执行人-审批意见' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for process_node_record_assign_user
-- ----------------------------
DROP TABLE IF EXISTS `process_node_record_assign_user`;
CREATE TABLE `process_node_record_assign_user`  (
                                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
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
) ENGINE = InnoDB AUTO_INCREMENT = 1502 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程节点记录-执行人' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for process_oper_record
-- ----------------------------
DROP TABLE IF EXISTS `process_oper_record`;
CREATE TABLE `process_oper_record`  (
                                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
                                        `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                        `create_time` datetime NOT NULL COMMENT '创建时间',
                                        `update_time` datetime NOT NULL COMMENT '更新时间',
                                        `flow_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                                        `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程实例id',
                                        `data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '表单数据',
                                        `node_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                        `task_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务id',
                                        `node_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点名字',
                                        `status` int NOT NULL COMMENT '任务状态',
                                        `start_time` datetime NOT NULL COMMENT '开始时间',
                                        `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
                                        `execution_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '执行id',
                                        `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                        `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
                                        `parent_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上级id',
                                        `task_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '任务类型',
                                        PRIMARY KEY (`id`) USING BTREE,
                                        INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3293 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程操作记录' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for process_starter
-- ----------------------------
DROP TABLE IF EXISTS `process_starter`;
CREATE TABLE `process_starter`  (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
                                    `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                    `create_time` datetime NOT NULL COMMENT '创建时间',
                                    `update_time` datetime NOT NULL COMMENT '更新时间',
                                    `type_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id或者部门id',
                                    `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT ' 类型 user dept',
                                    `process_id` bigint NOT NULL COMMENT '流程id',
                                    `data` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 339 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程发起人' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for process_sub_process
-- ----------------------------
DROP TABLE IF EXISTS `process_sub_process`;
CREATE TABLE `process_sub_process`  (
                                        `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
                                        `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                        `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                        `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                        `flow_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
                                        `sub_flow_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '子流程id',
                                        PRIMARY KEY (`id`) USING BTREE,
                                        INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '流程关联下的子流程' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
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
-- Table structure for role_menu
-- ----------------------------
DROP TABLE IF EXISTS `role_menu`;
CREATE TABLE `role_menu`  (
                              `role_id` bigint NOT NULL COMMENT '角色ID',
                              `menu_id` bigint NOT NULL COMMENT '菜单ID',
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
                              `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                              `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                              `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 490 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
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
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user_field
-- ----------------------------
DROP TABLE IF EXISTS `user_field`;
CREATE TABLE `user_field`  (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
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
) ENGINE = InnoDB AUTO_INCREMENT = 116 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户字段' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_field_data
-- ----------------------------
DROP TABLE IF EXISTS `user_field_data`;
CREATE TABLE `user_field_data`  (
                                    `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
                                    `user_id` bigint NOT NULL COMMENT '用户id',
                                    `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                                    `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                                    `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                                    `data` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据',
                                    `key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段',
                                    PRIMARY KEY (`id`) USING BTREE,
                                    INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 129 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户字段-数据' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
                              `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                              `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                              `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                              `user_id` bigint NOT NULL COMMENT '用户id',
                              `role_id` bigint NOT NULL COMMENT '角色id',
                              PRIMARY KEY (`id`) USING BTREE,
                              INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 68 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户-角色' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;






















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

 Date: 13/08/2023 21:05:35
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for dept
-- ----------------------------
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept`  (
                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
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
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

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
                         `id` bigint NOT NULL AUTO_INCREMENT,
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
) ENGINE = InnoDB AUTO_INCREMENT = 103 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单管理' ROW_FORMAT = DYNAMIC;

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
INSERT INTO `menu` VALUES (90, 89, '0,89', '流程列表', 1, '/flow/list', 'flow/list', NULL, 1, 3, 'menu', '', '2023-06-09 23:35:14', '2023-06-26 23:12:46', 0);
INSERT INTO `menu` VALUES (91, 89, '0,89', '流程组', 1, '/flow/group', 'flow/group', NULL, 1, 1, 'redis', NULL, '2023-06-09 23:37:38', '2023-06-11 16:21:33', 0);
INSERT INTO `menu` VALUES (92, 0, '0', '订单的1', 1, 'test', 'test/index', NULL, 1, 1, 'cascader', NULL, '2023-06-11 08:08:24', '2023-06-11 08:46:28', 1);
INSERT INTO `menu` VALUES (93, 92, '0,92', 'asadfffff', 1, 'dept1', 'system/dept/index1', NULL, 1, 1, 'cascader', NULL, '2023-06-11 08:46:12', '2023-06-11 08:46:28', 1);
INSERT INTO `menu` VALUES (94, 89, '0,89', '创建流程', 1, '/flow/create', 'flow/create/all', NULL, 1, 2, 'tree', NULL, '2023-06-11 12:57:28', '2023-06-17 11:52:35', 0);
INSERT INTO `menu` VALUES (95, 0, '0', '任务管理', 2, '/task', 'Layout', NULL, 1, 1, 'number', '', '2023-06-17 23:14:09', '2023-06-17 23:16:53', 0);
INSERT INTO `menu` VALUES (96, 95, '0,95', '待办任务', 1, '/task/pending', 'task/pending', NULL, 1, 1, 'edit', NULL, '2023-06-18 15:01:34', '2023-06-18 15:01:34', 0);
INSERT INTO `menu` VALUES (97, 95, '0,95', '我的发起', 1, 'task/started', 'task/started', NULL, 1, 2, 'menu', NULL, '2023-06-18 22:07:38', '2023-06-18 22:07:38', 0);
INSERT INTO `menu` VALUES (98, 95, '0,95', '抄送给我', 1, '/task/cc', 'task/cc', NULL, 1, 3, 'dashboard', NULL, '2023-06-22 23:38:52', '2023-06-22 23:38:52', 0);
INSERT INTO `menu` VALUES (99, 95, '0,95', '我的已办', 1, '/task/completed', 'task/completed', NULL, 1, 4, 'skill', NULL, '2023-06-23 10:19:04', '2023-06-23 10:19:04', 0);
INSERT INTO `menu` VALUES (100, 1, '0,1', '属性管理', 1, 'prop', 'system/prop/index', NULL, 1, 1, 'bug', NULL, '2023-07-17 22:16:44', '2023-07-17 22:17:36', 0);
INSERT INTO `menu` VALUES (101, 1, '0,1', '消息列表', 1, 'message', 'system/message/index', NULL, 1, 1, 'message', NULL, '2023-07-25 20:37:35', '2023-07-25 20:37:35', 0);
INSERT INTO `menu` VALUES (102, 36, '0,36', '签名', 1, 'signature', 'demo/signature', NULL, 1, 1, 'menu', '', '2023-07-25 22:15:47', '2023-07-25 22:15:47', 0);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
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
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
                              `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                              `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                              `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                              PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 490 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = DYNAMIC;

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
                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
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
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

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
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`  (
                              `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
                              `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
                              `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
                              `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
                              `user_id` bigint NOT NULL COMMENT '用户id',
                              `role_id` bigint NOT NULL COMMENT '角色id',
                              PRIMARY KEY (`id`) USING BTREE,
                              INDEX `idx_id`(`id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 68 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户-角色' ROW_FORMAT = Dynamic;

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

SET FOREIGN_KEY_CHECKS = 1;
