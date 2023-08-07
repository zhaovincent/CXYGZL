ALTER TABLE  `process_node_record`
ADD COLUMN `parent_node_id` varchar(255) NULL COMMENT '上一层级id' AFTER `execution_id`;