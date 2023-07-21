DROP TABLE IF EXISTS `file`;
CREATE TABLE `file` (
                        `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文件id(自增字段)',
                        `user_id` int(11) NOT NULL COMMENT '用户id',
                        `logo_id` int(11) NOT NULL COMMENT 'LOGO id',
                        `parent_id` int(11) NOT NULL NULL COMMENT '父节点id(-1-根节点)',
                        `folder` int(11) NOT NULL COMMENT '是否是文件夹(1-是,0-不是)',
                        `file_name` varchar(100) DEFAULT '新建文件' COMMENT '文件名',
                        `file_size` bigint(20) DEFAULT 'NULL' COMMENT '文件大小',
                        `file_type` varchar(100) DEFAULT 'NULL' COMMENT '文件类型',
                        `remark` varchar(100) DEFAULT NULL COMMENT '备注',
                        `download_count` int(11) DEFAULT NULL COMMENT '下载次数',
                        `upload_time` bigint(20) DEFAULT NULL COMMENT '上传时间',
                        `update_time` bigint(20) DEFAULT NULL COMMENT '更新时间',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=817 DEFAULT CHARSET=utf8;
