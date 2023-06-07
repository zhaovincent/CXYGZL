/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 80021
Source Host           : localhost:3307
Source Database       : simple_flow_biz

Target Server Type    : MYSQL
Target Server Version : 80021
File Encoding         : 65001

Date: 2023-06-07 21:44:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for dept
-- ----------------------------
DROP TABLE IF EXISTS `dept`;
CREATE TABLE `dept` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名',
  `parent_id` bigint NOT NULL DEFAULT '0' COMMENT '上级部门id',
  `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
  `leader_user_id` bigint NOT NULL COMMENT '主管user_id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_id` (`id`) USING BTREE,
  KEY `idx_parent_id` (`parent_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='部门表';

-- ----------------------------
-- Table structure for process
-- ----------------------------
DROP TABLE IF EXISTS `process`;
CREATE TABLE `process` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `form_id` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '表单ID',
  `form_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单名称',
  `logo` json NOT NULL COMMENT '图标配置',
  `settings` json NOT NULL COMMENT '设置项',
  `group_id` int NOT NULL COMMENT '分组ID',
  `form_items` json NOT NULL COMMENT '表单设置内容',
  `process` json NOT NULL COMMENT '流程设置内容',
  `remark` varchar(125) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `sort` int NOT NULL,
  `is_hidden` tinyint(1) NOT NULL COMMENT '0 正常 1=隐藏',
  `is_stop` tinyint(1) NOT NULL COMMENT '0 正常 1=停用 ',
  `admin_id` bigint DEFAULT NULL COMMENT '流程管理员',
  `unique_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '唯一性id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_form_id` (`form_id`) USING BTREE,
  KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for process_copy
-- ----------------------------
DROP TABLE IF EXISTS `process_copy`;
CREATE TABLE `process_copy` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `start_time` datetime NOT NULL COMMENT ' 流程发起时间',
  `node_time` datetime NOT NULL COMMENT '当前节点时间',
  `start_user_id` bigint NOT NULL COMMENT '发起人',
  `process_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
  `process_instance_id` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '实例id',
  `node_id` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点id',
  `group_id` bigint NOT NULL COMMENT '分组id',
  `group_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '分组名称',
  `process_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程名称',
  `node_name` varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点 名称',
  `form_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单数据',
  `user_id` bigint NOT NULL COMMENT '抄送人id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='流程抄送数据';

-- ----------------------------
-- Table structure for process_group
-- ----------------------------
DROP TABLE IF EXISTS `process_group`;
CREATE TABLE `process_group` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分组名',
  `sort` int NOT NULL DEFAULT '0' COMMENT '排序',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Table structure for process_instance_record
-- ----------------------------
DROP TABLE IF EXISTS `process_instance_record`;
CREATE TABLE `process_instance_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程名字',
  `logo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '头像',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `process_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '流程id',
  `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '流程实例id',
  `form_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '表单数据',
  `group_id` int DEFAULT NULL COMMENT '组id',
  `group_name` varchar(100) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '组名称',
  `status` int DEFAULT '1' COMMENT '状态',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `parent_process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '上级流程实例id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_id` (`id`) USING BTREE,
  KEY `idx_dep_id` (`user_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=274 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='流程记录';

-- ----------------------------
-- Table structure for process_node_data
-- ----------------------------
DROP TABLE IF EXISTS `process_node_data`;
CREATE TABLE `process_node_data` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `process_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
  `data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表单数据',
  `node_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=810 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='流程节点数据';

-- ----------------------------
-- Table structure for process_node_record
-- ----------------------------
DROP TABLE IF EXISTS `process_node_record`;
CREATE TABLE `process_node_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `process_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
  `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程实例id',
  `data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '表单数据',
  `node_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `node_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '节点类型',
  `node_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '节点名字',
  `status` int NOT NULL COMMENT '节点状态',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `execution_id` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '执行id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1136 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='流程节点记录';

-- ----------------------------
-- Table structure for process_node_record_assign_user
-- ----------------------------
DROP TABLE IF EXISTS `process_node_record_assign_user`;
CREATE TABLE `process_node_record_assign_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `process_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程id',
  `process_instance_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '流程实例id',
  `data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '表单数据',
  `node_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT ' 用户id',
  `status` int NOT NULL COMMENT '节点状态',
  `start_time` datetime NOT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `execution_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '执行id',
  `task_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT ' 任务id',
  `approve_desc` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '审批意见',
  `node_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT ' 节点名称',
  `task_type` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务类型',
  `local_data` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '表单本地数据',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=480 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='流程节点记录-执行人';

-- ----------------------------
-- Table structure for process_starter
-- ----------------------------
DROP TABLE IF EXISTS `process_starter`;
CREATE TABLE `process_starter` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `type_id` bigint NOT NULL COMMENT '用户id或者部门id',
  `type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT ' 类型 user dept',
  `process_id` bigint NOT NULL COMMENT '流程id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='流程发起人';

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `pinyin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '拼音  全拼',
  `py` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '拼音, 首字母缩写',
  `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '昵称',
  `avatar_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像url',
  `gender` int DEFAULT '1' COMMENT '性别1男2女',
  `dep_id` bigint NOT NULL COMMENT '部门id',
  `del_flag` tinyint(1) NOT NULL COMMENT '逻辑删除字段',
  `entry_date` date DEFAULT NULL COMMENT '入职日期',
  `leave_date` date DEFAULT NULL COMMENT '离职日期',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `password` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录密码',
  `phone` varchar(18) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_id` (`id`) USING BTREE,
  KEY `idx_leave_date` (`leave_date`) USING BTREE,
  KEY `idx_dep_id` (`dep_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='用户表';
INSERT INTO `dept` (`id`, `name`, `parent_id`, `del_flag`, `leader_user_id`, `create_time`, `update_time`) VALUES
                                                                                                               ('1', '第一个部门', '0', '0', '1', '2023-05-05 15:22:42', '2023-05-05 15:22:43');
INSERT INTO  `user` (`id`, `name`, `pinyin`, `py`, `nick_name`, `avatar_url`, `gender`, `dep_id`, `del_flag`,
                     `entry_date`, `leave_date`, `create_time`, `update_time`, `password`, `phone`) VALUES ('1',
                                                                                                            '测试1', NULL, NULL, '测试1', 'http://127.0.0.1:26859/file/show/6b86a2746cf0413dbd93179d03536091-24267b3756723f443adc897072dbd092.jpg', '1', '1', '0', '2023-05-07', NULL, '2023-05-07 11:53:14', '2023-05-23 20:53:19', '123456', '18755289564');
