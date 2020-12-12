

CREATE TABLE `gen_table` (
  `table_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(200) DEFAULT '' COMMENT '表名称',
  `table_comment` varchar(500) DEFAULT '' COMMENT '表描述',
  `class_name` varchar(100) DEFAULT '' COMMENT '实体类名称',
  `tpl_category` varchar(200) DEFAULT 'crud' COMMENT '使用的模板（crud单表操作 tree树表操作）',
  `package_name` varchar(100) DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(30) DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(30) DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(50) DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(50) DEFAULT NULL COMMENT '生成功能作者',
  `gen_type` char(1) DEFAULT '0' COMMENT '生成代码方式（0zip压缩包 1自定义路径）',
  `gen_path` varchar(200) DEFAULT '/' COMMENT '生成路径（不填默认项目路径）',
  `options` varchar(1000) DEFAULT NULL COMMENT '其它生成选项',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='代码生成业务表';


# Dump of table gen_table_column
# ------------------------------------------------------------

CREATE TABLE `gen_table_column` (
  `column_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` varchar(64) DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(200) DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(500) DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(100) DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(500) DEFAULT NULL COMMENT 'JAVA类型',
  `java_field` varchar(200) DEFAULT NULL COMMENT 'JAVA字段名',
  `is_pk` char(1) DEFAULT NULL COMMENT '是否主键（1是）',
  `is_increment` char(1) DEFAULT NULL COMMENT '是否自增（1是）',
  `is_required` char(1) DEFAULT NULL COMMENT '是否必填（1是）',
  `is_insert` char(1) DEFAULT NULL COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) DEFAULT NULL COMMENT '是否编辑字段（1是）',
  `is_list` char(1) DEFAULT NULL COMMENT '是否列表字段（1是）',
  `is_query` char(1) DEFAULT NULL COMMENT '是否查询字段（1是）',
  `query_type` varchar(200) DEFAULT 'EQ' COMMENT '查询方式（等于、不等于、大于、小于、范围）',
  `html_type` varchar(200) DEFAULT NULL COMMENT '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  `dict_type` varchar(200) DEFAULT '' COMMENT '字典类型',
  `sort` int(11) DEFAULT NULL COMMENT '排序',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='代码生成业务表字段';



# Dump of table sys_config
# ------------------------------------------------------------

CREATE TABLE `sys_config` (
  `config_id` int(5) NOT NULL AUTO_INCREMENT COMMENT '参数主键',
  `config_name` varchar(100) DEFAULT '' COMMENT '参数名称',
  `config_key` varchar(100) DEFAULT '' COMMENT '参数键名',
  `config_value` varchar(500) DEFAULT '' COMMENT '参数键值',
  `config_type` char(1) DEFAULT 'N' COMMENT '系统内置（Y是 N否）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`config_id`),
  KEY `idx_sys_config` (`config_key`,`config_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='参数配置表';



# Dump of table sys_dept
# ------------------------------------------------------------

CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父部门id',
  `ancestors` varchar(50) DEFAULT '' COMMENT '祖级列表',
  `dept_name` varchar(30) DEFAULT '' COMMENT '部门名称',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `leader` varchar(20) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(11) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `status` char(1) DEFAULT '0' COMMENT '部门状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`dept_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_dept_name` (`dept_name`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部门表';



# Dump of table sys_dict_data
# ------------------------------------------------------------

CREATE TABLE `sys_dict_data` (
  `dict_code` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典编码',
  `dict_sort` int(4) DEFAULT '0' COMMENT '字典排序',
  `dict_label` varchar(100) DEFAULT '' COMMENT '字典标签',
  `dict_value` varchar(100) DEFAULT '' COMMENT '字典键值',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `css_class` varchar(100) DEFAULT NULL COMMENT '样式属性（其他样式扩展）',
  `list_class` varchar(100) DEFAULT NULL COMMENT '表格回显样式',
  `is_default` char(1) DEFAULT 'N' COMMENT '是否默认（Y是 N否）',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`dict_code`),
  KEY `idx_dict_type` (`dict_type`),
  KEY `idx_dict_type_status` (`dict_type`,`dict_label`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典数据表';



# Dump of table sys_dict_type
# ------------------------------------------------------------

CREATE TABLE `sys_dict_type` (
  `dict_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '字典主键',
  `dict_name` varchar(100) DEFAULT '' COMMENT '字典名称',
  `dict_type` varchar(100) DEFAULT '' COMMENT '字典类型',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`dict_id`),
  UNIQUE KEY `dict_type` (`dict_type`),
  KEY `idx_dict_type` (`dict_name`,`dict_type`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='字典类型表';



# Dump of table sys_ldap_config
# ------------------------------------------------------------

CREATE TABLE `sys_ldap_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `enabled` tinyint(4) DEFAULT '0' COMMENT '是否启用: 1开启 0关闭',
  `enableSsl` tinyint(4) DEFAULT '0' COMMENT '是否启用SSL: 1开启 0关闭',
  `urls` varchar(200) DEFAULT '' COMMENT 'ldap地址 例如:ldap://localhost:389',
  `base` varchar(200) DEFAULT '' COMMENT 'base 例如dc=example,dc=com',
  `user_dn_patterns` varchar(100) DEFAULT '' COMMENT 'dn例如: uid={},ou=people或cn={},ou=people',
  `manager_dn` varchar(100) DEFAULT '' COMMENT '管理员账号例如:cn=admin,dc=example,dc=com',
  `manager_password` varchar(60) DEFAULT '' COMMENT '管理员密码',
  `attributes_mail` varchar(60) DEFAULT 'mail' COMMENT '属性:邮箱',
  `attributes_telephone` varchar(60) DEFAULT 'telephone' COMMENT '属性:手机号码',
  `update_by` varchar(100) DEFAULT '' COMMENT '更新者',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='OpenLdap配置表';



# Dump of table sys_logininfor
# ------------------------------------------------------------

CREATE TABLE `sys_logininfor` (
  `info_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '访问ID',
  `user_name` varchar(50) DEFAULT '' COMMENT '用户账号',
  `ipaddr` varchar(50) DEFAULT '' COMMENT '登录IP地址',
  `login_location` varchar(255) DEFAULT '' COMMENT '登录地点',
  `browser` varchar(50) DEFAULT '' COMMENT '浏览器类型',
  `os` varchar(50) DEFAULT '' COMMENT '操作系统',
  `status` char(1) DEFAULT '0' COMMENT '登录状态（0成功 1失败）',
  `msg` varchar(255) DEFAULT '' COMMENT '提示消息',
  `login_time` datetime DEFAULT NULL COMMENT '访问时间',
  PRIMARY KEY (`info_id`),
  KEY `idx_logininfor` (`user_name`,`status`,`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统访问记录';



# Dump of table sys_menu
# ------------------------------------------------------------

CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `is_frame` int(1) DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`menu_id`),
  KEY `idx_menu_name` (`menu_name`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';



# Dump of table sys_notice
# ------------------------------------------------------------

CREATE TABLE `sys_notice` (
  `notice_id` int(4) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) NOT NULL COMMENT '公告标题',
  `notice_type` char(1) NOT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` varchar(2000) DEFAULT NULL COMMENT '公告内容',
  `status` char(1) DEFAULT '0' COMMENT '公告状态（0正常 1关闭）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`notice_id`),
  KEY `idx_notice` (`notice_title`,`notice_type`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知公告表';



# Dump of table sys_oper_log
# ------------------------------------------------------------

CREATE TABLE `sys_oper_log` (
  `oper_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) DEFAULT '' COMMENT '模块标题',
  `business_type` int(2) DEFAULT '0' COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) DEFAULT '' COMMENT '方法名称',
  `request_method` varchar(10) DEFAULT '' COMMENT '请求方式',
  `operator_type` int(1) DEFAULT '0' COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(50) DEFAULT '' COMMENT '操作人员',
  `dept_name` varchar(50) DEFAULT '' COMMENT '部门名称',
  `oper_url` varchar(255) DEFAULT '' COMMENT '请求URL',
  `oper_ip` varchar(50) DEFAULT '' COMMENT '主机地址',
  `oper_location` varchar(255) DEFAULT '' COMMENT '操作地点',
  `oper_param` varchar(2000) DEFAULT '' COMMENT '请求参数',
  `json_result` varchar(2000) DEFAULT '' COMMENT '返回参数',
  `status` int(1) DEFAULT '0' COMMENT '操作状态（0正常 1异常）',
  `error_msg` varchar(2000) DEFAULT '' COMMENT '错误消息',
  `oper_time` datetime DEFAULT NULL COMMENT '操作时间',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`oper_id`),
  KEY `idx_oper_log` (`oper_name`,`operator_type`,`status`,`oper_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志记录';



# Dump of table sys_post
# ------------------------------------------------------------

CREATE TABLE `sys_post` (
  `post_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '岗位ID',
  `post_code` varchar(64) NOT NULL COMMENT '岗位编码',
  `post_name` varchar(50) NOT NULL COMMENT '岗位名称',
  `post_sort` int(4) NOT NULL COMMENT '显示顺序',
  `status` char(1) NOT NULL COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`post_id`),
  KEY `idx_post_code` (`post_code`,`post_name`,`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='岗位信息表';



# Dump of table sys_role
# ------------------------------------------------------------

CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`role_id`),
  KEY `idx_del_flag` (`del_flag`,`status`,`role_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色信息表';



# Dump of table sys_role_dept
# ------------------------------------------------------------

CREATE TABLE `sys_role_dept` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `dept_id` bigint(20) NOT NULL COMMENT '部门ID',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`role_id`,`dept_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和部门关联表';



# Dump of table sys_role_menu
# ------------------------------------------------------------

CREATE TABLE `sys_role_menu` (
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和菜单关联表';



# Dump of table sys_user
# ------------------------------------------------------------

CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `dept_id` bigint(20) DEFAULT NULL COMMENT '部门ID',
  `user_name` varchar(60) NOT NULL COMMENT '用户账号',
  `nick_name` varchar(60) NOT NULL COMMENT '用户昵称',
  `user_type` varchar(2) DEFAULT '00' COMMENT '用户类型（00系统用户）',
  `email` varchar(50) DEFAULT '' COMMENT '用户邮箱',
  `mobile` varchar(60) DEFAULT '' COMMENT '手机号码',
  `sex` char(1) DEFAULT '0' COMMENT '用户性别（0男 1女 2未知）',
  `avatar` varchar(100) DEFAULT '' COMMENT '头像地址',
  `password` varchar(100) DEFAULT '' COMMENT '密码',
  `status` tinyint(4) DEFAULT '0' COMMENT '帐号状态（0停用 1停运 2 锁定）',
  `create_type` tinyint(4) DEFAULT '0' COMMENT '账号创建类型:0 系统 1 ldap',
  `expire_date` datetime DEFAULT NULL COMMENT '账号到期时间',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `login_date` datetime DEFAULT NULL COMMENT '最后登陆时间',
  `login_ip` varchar(50) DEFAULT '' COMMENT '最后登陆IP',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `unq_user_user_name_uindex` (`user_name`),
  KEY `idx_email` (`email`),
  KEY `idx_mobile` (`mobile`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';


# Dump of table sys_user_post
# ------------------------------------------------------------

CREATE TABLE `sys_user_post` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `post_id` bigint(20) NOT NULL COMMENT '岗位ID',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`user_id`,`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户与岗位关联表';



# Dump of table sys_user_role
# ------------------------------------------------------------

CREATE TABLE `sys_user_role` (
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';








INSERT INTO `sys_config` (`config_id`, `config_name`, `config_key`, `config_value`, `config_type`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `gmt_create`, `gmt_modified`)
VALUES
	(1,'主框架页-默认皮肤样式名称','sys.index.skinName','skin-blue','Y','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow','2020-12-06 13:15:37','2020-12-06 13:15:37'),
	(2,'用户管理-账号初始密码','sys.user.initPassword','123456','Y','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','初始化密码 123456','2020-12-06 13:15:37','2020-12-06 13:15:37'),
	(3,'主框架页-侧边栏主题','sys.index.sideTheme','theme-dark','Y','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','深色主题theme-dark，浅色主题theme-light','2020-12-06 13:15:37','2020-12-06 13:15:37');



INSERT INTO `sys_dept` (`dept_id`, `parent_id`, `ancestors`, `dept_name`, `order_num`, `leader`, `phone`, `email`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `gmt_create`, `gmt_modified`)
VALUES
	(100,0,'0','总公司',0,'','','','0','0','admin','2018-03-16 11:33:00','admin','2020-12-06 16:18:33','2020-12-06 13:15:37','2020-12-06 16:18:33'),
	(101,100,'0,100','研发部门',1,'','','','0','0','admin','2018-03-16 11:33:00','admin','2020-12-06 16:13:12','2020-12-06 13:15:37','2020-12-06 16:13:12'),
	(103,101,'0,100,101','后端',1,'','','','0','0','admin','2018-03-16 11:33:00','admin','2020-12-06 16:11:11','2020-12-06 13:15:37','2020-12-06 16:11:11'),
	(104,101,'0,100,101','前端',2,'','','','0','0','admin','2018-03-16 11:33:00','admin','2020-12-06 16:12:31','2020-12-06 13:15:37','2020-12-06 16:12:31'),
	(105,101,'0,100,101','测试',3,'','','','0','0','admin','2018-03-16 11:33:00','admin','2020-12-06 16:12:41','2020-12-06 13:15:37','2020-12-06 16:12:41'),
	(106,101,'0,100,101','产品',4,'','','','0','0','admin','2018-03-16 11:33:00','admin','2020-12-06 16:12:52','2020-12-06 13:15:37','2020-12-06 16:12:52'),
	(107,101,'0,100,101','运维',5,'','','','0','0','admin','2018-03-16 11:33:00','admin','2020-12-06 16:13:12','2020-12-06 13:15:37','2020-12-06 16:13:12'),
	(110,101,'0,100,101','技术支持',6,NULL,NULL,NULL,'0','0','admin','2020-12-06 16:13:36','',NULL,'2020-12-06 16:13:36','2020-12-06 16:13:36'),
	(111,101,'0,100,101','实施',7,NULL,NULL,NULL,'0','0','admin','2020-12-06 16:14:10','',NULL,'2020-12-06 16:14:10','2020-12-06 16:14:10'),
	(112,100,'0,100','市场部门',3,NULL,NULL,NULL,'0','0','admin','2020-12-06 16:18:46','',NULL,'2020-12-06 16:18:46','2020-12-06 16:18:46');



INSERT INTO `sys_dict_data` (`dict_code`, `dict_sort`, `dict_label`, `dict_value`, `dict_type`, `css_class`, `list_class`, `is_default`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `gmt_create`, `gmt_modified`)
VALUES
	(1,1,'男','0','sys_user_sex','','','Y','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','性别男','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(2,2,'女','1','sys_user_sex','','','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','性别女','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(3,3,'未知','2','sys_user_sex','','','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','性别未知','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(4,1,'显示','0','sys_show_hide','','primary','Y','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','显示菜单','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(5,2,'隐藏','1','sys_show_hide','','danger','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','隐藏菜单','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(6,1,'正常','0','sys_normal_disable','','primary','Y','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','正常状态','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(7,2,'停用','1','sys_normal_disable','','danger','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','停用状态','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(8,1,'正常','0','sys_job_status','','primary','Y','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','正常状态','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(9,2,'暂停','1','sys_job_status','','danger','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','停用状态','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(10,1,'默认','DEFAULT','sys_job_group','','','Y','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','默认分组','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(11,2,'系统','SYSTEM','sys_job_group','','','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','系统分组','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(12,1,'是','Y','sys_yes_no','','primary','Y','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','系统默认是','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(13,2,'否','N','sys_yes_no','','danger','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','系统默认否','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(14,1,'通知','1','sys_notice_type','','warning','Y','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','通知','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(15,2,'公告','2','sys_notice_type','','success','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','公告','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(16,1,'正常','0','sys_notice_status','','primary','Y','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','正常状态','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(17,2,'关闭','1','sys_notice_status','','danger','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','关闭状态','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(18,1,'新增','1','sys_oper_type','','info','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','新增操作','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(19,2,'修改','2','sys_oper_type','','info','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','修改操作','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(20,3,'删除','3','sys_oper_type','','danger','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','删除操作','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(21,4,'授权','4','sys_oper_type','','primary','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','授权操作','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(22,5,'导出','5','sys_oper_type','','warning','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','导出操作','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(23,6,'导入','6','sys_oper_type','','warning','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','导入操作','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(24,7,'强退','7','sys_oper_type','','danger','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','强退操作','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(25,8,'生成代码','8','sys_oper_type','','warning','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','生成操作','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(26,9,'清空数据','9','sys_oper_type','','danger','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','清空操作','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(27,10,'运行','10','sys_oper_type','','danger','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','清空操作','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(28,11,'查询','11','sys_oper_type','','info','N','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','清空操作','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(29,1,'成功','0','sys_common_status','','primary','N','0','admin','2018-03-16 11:33:00','user','2020-12-04 11:34:43','正常状态','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(30,2,'失败','1','sys_common_status','','danger','N','0','admin','2018-03-16 11:33:00','user','2020-12-04 11:35:00','停用状态','2020-12-06 13:15:38','2020-12-06 13:15:38');



INSERT INTO `sys_dict_type` (`dict_id`, `dict_name`, `dict_type`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `gmt_create`, `gmt_modified`)
VALUES
	(1,'用户性别','sys_user_sex','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','用户性别列表','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(2,'菜单状态','sys_show_hide','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','菜单状态列表','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(3,'系统开关','sys_normal_disable','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','系统开关列表','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(4,'任务状态','sys_job_status','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','任务状态列表','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(5,'任务分组','sys_job_group','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','任务分组列表','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(6,'系统是否','sys_yes_no','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','系统是否列表','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(7,'通知类型','sys_notice_type','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','通知类型列表','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(8,'通知状态','sys_notice_status','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','通知状态列表','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(9,'操作类型','sys_oper_type','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','操作类型列表','2020-12-06 13:15:38','2020-12-06 13:15:38'),
	(10,'系统状态','sys_common_status','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','登录状态列表','2020-12-06 13:15:38','2020-12-06 13:15:38');



INSERT INTO `sys_ldap_config` (`id`, `enabled`, `enableSsl`, `urls`, `base`, `user_dn_patterns`, `manager_dn`, `manager_password`, `attributes_mail`, `attributes_telephone`, `update_by`, `gmt_create`, `gmt_modified`, `remark`)
VALUES
	(1,1,0,'ldap://localhost:10389','dc=example,dc=com','uid={},ou=people','cn=admin,dc=example,dc=com','password','mail','telephone','','2020-12-04 13:01:25','2020-12-04 13:28:37',NULL);



INSERT INTO `sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `is_frame`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `gmt_create`, `gmt_modified`)
VALUES
	(1,'系统管理',0,1,'system',NULL,1,'M','0','0','','system','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','系统管理目录','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2,'系统监控',0,2,'monitor',NULL,1,'M','0','0','','monitor','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','系统监控目录','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(3,'系统工具',0,3,'tool',NULL,1,'M','0','0','','tool','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','系统工具目录','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(100,'用户管理',1,1,'user','system/user/index',1,'C','0','0','system:user:list','user','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','用户管理菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(101,'角色管理',1,2,'role','system/role/index',1,'C','0','0','system:role:list','peoples','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','角色管理菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(102,'菜单管理',1,3,'menu','system/menu/index',1,'C','0','0','system:menu:list','tree-table','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','菜单管理菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(103,'部门管理',1,4,'dept','system/dept/index',1,'C','0','0','system:dept:list','tree','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','部门管理菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(104,'岗位管理',1,5,'post','system/post/index',1,'C','0','0','system:post:list','post','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','岗位管理菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(105,'字典管理',1,6,'dict','system/dict/index',1,'C','0','0','system:dict:list','dict','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','字典管理菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(106,'参数设置',1,7,'config','system/config/index',1,'C','0','0','system:config:list','edit','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','参数设置菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(107,'通知公告',1,8,'notice','system/notice/index',1,'C','0','0','system:notice:list','message','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','通知公告菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(108,'日志管理',1,9,'log','system/log/index',1,'M','0','0','','log','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','日志管理菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(109,'在线用户',2,1,'online','monitor/online/index',1,'C','0','0','monitor:online:list','online','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','在线用户菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(111,'数据监控',2,3,'druid','monitor/druid/index',1,'C','0','0','monitor:druid:list','druid','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','数据监控菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(112,'服务监控',2,4,'server','monitor/server/index',1,'C','0','0','monitor:server:list','server','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','服务监控菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(113,'表单构建',3,1,'build','tool/build/index',1,'C','0','0','tool:build:list','build','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','表单构建菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(114,'代码生成',3,2,'gen','tool/gen/index',1,'C','0','0','tool:gen:list','code','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','代码生成菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(115,'系统接口',3,3,'swagger','tool/swagger/index',1,'C','0','0','tool:swagger:list','swagger','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','系统接口菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(500,'操作日志',108,1,'operlog','monitor/operlog/index',1,'C','0','0','monitor:operlog:list','form','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','操作日志菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(501,'登录日志',108,2,'logininfor','monitor/logininfor/index',1,'C','0','0','monitor:logininfor:list','logininfor','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','登录日志菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1001,'用户查询',100,1,'','',1,'F','0','0','system:user:query','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1002,'用户新增',100,2,'','',1,'F','0','0','system:user:add','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1003,'用户修改',100,3,'','',1,'F','0','0','system:user:edit','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1004,'用户删除',100,4,'','',1,'F','0','0','system:user:remove','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1005,'用户导出',100,5,'','',1,'F','0','0','system:user:export','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1006,'用户导入',100,6,'','',1,'F','0','0','system:user:import','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1007,'重置密码',100,7,'','',1,'F','0','0','system:user:resetPwd','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1008,'角色查询',101,1,'','',1,'F','0','0','system:role:query','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1009,'角色新增',101,2,'','',1,'F','0','0','system:role:add','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1010,'角色修改',101,3,'','',1,'F','0','0','system:role:edit','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1011,'角色删除',101,4,'','',1,'F','0','0','system:role:remove','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1012,'角色导出',101,5,'','',1,'F','0','0','system:role:export','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1013,'菜单查询',102,1,'','',1,'F','0','0','system:menu:query','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1014,'菜单新增',102,2,'','',1,'F','0','0','system:menu:add','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1015,'菜单修改',102,3,'','',1,'F','0','0','system:menu:edit','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1016,'菜单删除',102,4,'','',1,'F','0','0','system:menu:remove','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1017,'部门查询',103,1,'','',1,'F','0','0','system:dept:query','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1018,'部门新增',103,2,'','',1,'F','0','0','system:dept:add','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1019,'部门修改',103,3,'','',1,'F','0','0','system:dept:edit','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1020,'部门删除',103,4,'','',1,'F','0','0','system:dept:remove','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1021,'岗位查询',104,1,'','',1,'F','0','0','system:post:query','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1022,'岗位新增',104,2,'','',1,'F','0','0','system:post:add','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1023,'岗位修改',104,3,'','',1,'F','0','0','system:post:edit','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1024,'岗位删除',104,4,'','',1,'F','0','0','system:post:remove','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1025,'岗位导出',104,5,'','',1,'F','0','0','system:post:export','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1026,'字典查询',105,1,'#','',1,'F','0','0','system:dict:query','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1027,'字典新增',105,2,'#','',1,'F','0','0','system:dict:add','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1028,'字典修改',105,3,'#','',1,'F','0','0','system:dict:edit','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1029,'字典删除',105,4,'#','',1,'F','0','0','system:dict:remove','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1030,'字典导出',105,5,'#','',1,'F','0','0','system:dict:export','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1031,'参数查询',106,1,'#','',1,'F','0','0','system:config:query','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1032,'参数新增',106,2,'#','',1,'F','0','0','system:config:add','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1033,'参数修改',106,3,'#','',1,'F','0','0','system:config:edit','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1034,'参数删除',106,4,'#','',1,'F','0','0','system:config:remove','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1035,'参数导出',106,5,'#','',1,'F','0','0','system:config:export','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1036,'公告查询',107,1,'#','',1,'F','0','0','system:notice:query','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1037,'公告新增',107,2,'#','',1,'F','0','0','system:notice:add','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1038,'公告修改',107,3,'#','',1,'F','0','0','system:notice:edit','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1039,'公告删除',107,4,'#','',1,'F','0','0','system:notice:remove','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1040,'操作查询',500,1,'#','',1,'F','0','0','monitor:operlog:query','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1041,'操作删除',500,2,'#','',1,'F','0','0','monitor:operlog:remove','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1042,'日志导出',500,4,'#','',1,'F','0','0','monitor:operlog:export','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1043,'登录查询',501,1,'#','',1,'F','0','0','monitor:logininfor:query','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1044,'登录删除',501,2,'#','',1,'F','0','0','monitor:logininfor:remove','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1045,'日志导出',501,3,'#','',1,'F','0','0','monitor:logininfor:export','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1046,'在线查询',109,1,'#','',1,'F','0','0','monitor:online:query','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1047,'批量强退',109,2,'#','',1,'F','0','0','monitor:online:batchLogout','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1048,'单条强退',109,3,'#','',1,'F','0','0','monitor:online:forceLogout','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1055,'生成查询',114,1,'#','',1,'F','0','0','tool:gen:query','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1056,'生成修改',114,2,'#','',1,'F','0','0','tool:gen:edit','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1057,'生成删除',114,3,'#','',1,'F','0','0','tool:gen:remove','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1058,'导入代码',114,2,'#','',1,'F','0','0','tool:gen:import','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1059,'预览代码',114,4,'#','',1,'F','0','0','tool:gen:preview','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(1060,'生成代码',114,5,'#','',1,'F','0','0','tool:gen:code','#','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2000,'定时任务',0,0,'job',NULL,1,'M','0','0','','guide','admin','2020-08-16 11:13:49','admin','2020-12-06 16:20:55','','2020-12-06 13:15:52','2020-12-06 16:20:55'),
	(2001,'任务管理',2000,1,'jobInfo','job/jobInfo/index',1,'C','0','0','job:jobInfo:list','tab','admin','2020-08-16 11:17:14','',NULL,'','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2002,'任务日志',2000,2,'jobLog','job/jobLog/index',1,'C','0','0','job:jobLog:list','build','admin','2020-08-16 11:19:07','admin','2020-09-20 08:35:42','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2003,'执行器管理',2000,3,'jobGroup','job/jobGroup/index',1,'C','0','0','job:jobGroup:list','cascader','admin','2020-08-16 11:19:50','admin','2020-09-20 09:57:35','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2004,'告警记录',2000,4,'alarmLog','job/alarmLog/index',1,'C','0','0','job:alarmLog:list','checkbox','admin','2018-03-01 00:00:00','admin','2020-09-10 15:44:19','任务告警记录菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2005,'任务告警记录查询',2004,1,'#','',1,'F','0','0','job:alarmLog:query','#','admin','2018-03-01 00:00:00','ry','2018-03-01 00:00:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2008,'任务告警记录删除',2004,4,'#','',1,'F','0','0','job:alarmLog:remove','#','admin','2018-03-01 00:00:00','ry','2018-03-01 00:00:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2010,'调度管理',2000,4,'jobThread','job/jobThread/index',1,'C','0','0','job:jobThread:list','chart','admin','2018-03-01 00:00:00','admin','2020-09-20 09:56:43','任务调度菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2011,'任务调度查询',2010,1,'#','',1,'F','0','0','job:jobThread:query','#','admin','2018-03-01 00:00:00','ry','2018-03-01 00:00:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2012,'任务调度新增',2010,2,'#','',1,'F','0','0','job:jobThread:add','#','admin','2018-03-01 00:00:00','ry','2018-03-01 00:00:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2013,'任务调度修改',2010,3,'#','',1,'F','0','0','job:jobThread:edit','#','admin','2018-03-01 00:00:00','ry','2018-03-01 00:00:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2014,'任务调度删除',2010,4,'#','',1,'F','0','0','job:jobThread:remove','#','admin','2018-03-01 00:00:00','ry','2018-03-01 00:00:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2016,'任务配置',2000,6,'config2','job/config/index',1,'C','1','0','job:config:list','edit','admin','2018-03-01 00:00:00','admin','2020-09-20 09:57:48','任务管理配置菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2017,'任务管理配置查询',2016,1,'#','',1,'F','0','0','job:config:query','#','admin','2018-03-01 00:00:00','ry','2018-03-01 00:00:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2018,'任务管理配置新增',2016,2,'#','',1,'F','0','0','job:config:add','#','admin','2018-03-01 00:00:00','ry','2018-03-01 00:00:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2019,'任务管理配置修改',2016,3,'#','',1,'F','0','0','job:config:edit','#','admin','2018-03-01 00:00:00','ry','2018-03-01 00:00:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2020,'任务管理配置删除',2016,4,'#','',1,'F','0','0','job:config:remove','#','admin','2018-03-01 00:00:00','ry','2018-03-01 00:00:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2021,'任务管理配置导出',2016,5,'#','',1,'F','0','0','job:config:export','#','admin','2018-03-01 00:00:00','ry','2018-03-01 00:00:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2022,'告警配置',2000,5,'alarmInfo','job/alarmInfo/index',1,'C','0','0','job:alarmInfo:list','email','admin','2018-03-01 00:00:00','admin','2020-09-14 15:30:14','任务告警方式配置菜单','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2023,'任务告警方式配置查询',2022,1,'#','',1,'F','0','0','job:alarmInfo:query','#','admin','2018-03-01 00:00:00','ry','2018-03-01 00:00:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2024,'任务告警方式配置新增',2022,2,'#','',1,'F','0','0','job:alarmInfo:add','#','admin','2018-03-01 00:00:00','ry','2018-03-01 00:00:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2025,'任务告警方式配置修改',2022,3,'#','',1,'F','0','0','job:alarmInfo:edit','#','admin','2018-03-01 00:00:00','ry','2018-03-01 00:00:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2026,'任务告警方式配置删除',2022,4,'#','',1,'F','0','0','job:alarmInfo:remove','#','admin','2018-03-01 00:00:00','ry','2018-03-01 00:00:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2027,'任务告警方式配置导出',2022,5,'#','',1,'F','0','0','job:alarmInfo:export','#','admin','2018-03-01 00:00:00','ry','2018-03-01 00:00:00','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2028,'任务列表查询',2001,1,'',NULL,1,'F','0','0','job:jobInfo:list','#','admin','2020-09-14 09:11:27','admin','2020-09-14 09:11:41','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2029,'任务新增',2001,2,'',NULL,1,'F','0','0','job:jobInfo:add','#','admin','2020-09-14 09:12:04','admin','2020-09-14 09:12:21','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2030,'任务修改',2001,3,'',NULL,1,'F','0','0','job:jobInfo:update','#','admin','2020-09-14 09:12:41','',NULL,'','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2031,'任务删除',2001,4,'',NULL,1,'F','0','0','job:jobInfo:remove','#','admin','2020-09-14 09:13:07','',NULL,'','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2034,' 任务执行器列表',2003,1,'',NULL,1,'F','0','0','job:jobGroup:list','#','admin','2020-09-14 09:22:43','',NULL,'','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2035,'任务执行器新增',2003,2,'',NULL,1,'F','0','0','job:jobGroup:add','#','admin','2020-09-14 09:23:00','',NULL,'','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2036,'任务执行器修改',2003,3,'',NULL,1,'F','0','0','job:jobGroup:update','#','admin','2020-09-14 09:26:06','',NULL,'','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2037,'任务执行器删除',2003,4,'',NULL,1,'F','0','0','job:jobGroup:remove','#','admin','2020-09-14 09:26:30','',NULL,'','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2038,'任务执行器详情',2003,5,'',NULL,1,'F','0','0','job:jobGroup:detail','#','admin','2020-09-14 09:27:12','',NULL,'','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2039,'任务详情',2001,5,'',NULL,1,'F','0','0','job:jobInfo:detail','#','admin','2020-09-14 09:27:33','',NULL,'','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2040,'任务在线注册地址',2003,6,'',NULL,1,'F','0','0','job:jobGroup:getOnLineAddressList','#','admin','2020-09-14 09:28:18','',NULL,'','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2041,'任务日志清理',2002,1,'',NULL,1,'F','0','0','job:jobLog:clearLog','#','admin','2020-09-14 09:28:47','',NULL,'','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2042,'任务终止',2002,2,'',NULL,1,'F','0','0','job:jobLog:killJob','#','admin','2020-09-14 09:29:07','smart','2020-09-14 14:08:55','','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2044,'任务日志详情',2002,3,'',NULL,1,'F','0','0','job:jobLog:detail','#','admin','2020-09-14 09:30:02','',NULL,'','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2045,'任务触发',2001,7,'',NULL,1,'F','0','0','job:jobInfo:trigger','#','admin','2020-09-14 09:37:42','',NULL,'','2020-12-06 13:15:52','2020-12-06 13:15:52'),
	(2048,'任务线程状态切换',2010,5,'',NULL,1,'F','0','0','job:jobThread:changeStatus','#','admin','2020-09-14 15:16:27','',NULL,'','2020-12-06 13:15:52','2020-12-06 13:15:52');



INSERT INTO `sys_post` (`post_id`, `post_code`, `post_name`, `post_sort`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `gmt_create`, `gmt_modified`)
VALUES
	(1,'ceo','开发',1,'0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:53','2020-12-06 13:15:53'),
	(2,'se','测试',2,'0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:53','2020-12-06 13:15:53'),
	(3,'hr','人力资源',3,'0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:53','2020-12-06 13:15:53'),
	(4,'user','普通员工',4,'0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','','2020-12-06 13:15:53','2020-12-06 13:15:53');


INSERT INTO `sys_role` (`role_id`, `role_name`, `role_key`, `role_sort`, `data_scope`, `status`, `del_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `gmt_create`, `gmt_modified`)
VALUES
	(1,'超级管理员','admin',1,'1','0','0','admin','2018-03-16 11:33:00','ry','2018-03-16 11:33:00','超级管理员','2020-12-06 13:15:53','2020-12-06 13:15:53'),
	(2,'开发者','common',2,'2','0','0','admin','2018-03-16 11:33:00','admin','2020-12-06 16:27:55','普通角色','2020-12-06 13:15:53','2020-12-06 16:27:55'),
	(3,'测试','test',0,'1','0','0','admin','2020-12-06 16:24:54','admin','2020-12-06 16:27:29',NULL,'2020-12-06 16:24:54','2020-12-06 16:27:29'),
	(4,'小组长','leader',0,'1','0','0','admin','2020-12-06 16:27:12','',NULL,NULL,'2020-12-06 16:27:12','2020-12-06 16:27:12');


INSERT INTO `sys_role_dept` (`role_id`, `dept_id`, `gmt_create`, `gmt_modified`)
VALUES
	(2,100,'2020-12-06 13:15:53','2020-12-06 13:15:53'),


INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `gmt_create`, `gmt_modified`)
VALUES
	(2,1,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,100,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,101,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,103,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,104,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,105,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,106,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,107,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,108,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,109,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,111,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,112,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,500,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,501,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,1001,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,1008,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,1017,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,1021,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,1026,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,1031,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,1036,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,1040,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,1043,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,1046,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2000,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2001,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2002,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2003,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2004,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2005,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2008,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2010,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2011,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2012,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2013,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2014,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2022,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2023,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2024,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2025,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2026,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2028,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2029,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2030,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2031,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2034,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2035,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2036,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2037,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2038,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2039,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2040,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2041,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2042,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2044,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2045,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(2,2048,'2020-12-06 16:27:55','2020-12-06 16:27:55'),
	(3,1,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,100,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,108,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,109,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,111,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,112,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,500,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,501,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,1001,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,1040,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,1043,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,1046,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2000,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2001,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2002,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2003,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2004,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2005,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2008,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2010,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2011,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2012,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2013,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2014,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2022,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2023,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2024,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2025,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2026,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2027,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2028,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2029,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2030,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2031,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2034,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2035,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2036,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2037,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2038,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2039,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2040,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2041,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2042,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2044,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2045,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(3,2048,'2020-12-06 16:27:29','2020-12-06 16:27:29'),
	(4,1,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,100,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,101,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,102,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,103,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,104,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,105,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,106,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,107,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,108,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,109,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,111,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,112,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,500,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,501,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1001,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1002,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1003,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1008,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1009,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1010,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1013,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1017,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1018,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1019,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1021,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1022,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1023,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1024,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1026,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1027,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1028,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1029,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1031,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1032,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1033,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1034,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1036,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1037,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1038,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1039,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1040,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1043,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,1046,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2000,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2001,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2002,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2003,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2004,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2005,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2008,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2010,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2011,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2012,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2013,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2014,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2022,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2023,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2024,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2025,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2026,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2027,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2028,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2029,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2030,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2031,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2034,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2035,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2036,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2037,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2038,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2039,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2040,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2041,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2042,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2044,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2045,'2020-12-06 16:27:12','2020-12-06 16:27:12'),
	(4,2048,'2020-12-06 16:27:12','2020-12-06 16:27:12');



INSERT INTO `sys_user` (`user_id`, `dept_id`, `user_name`, `nick_name`, `user_type`, `email`, `mobile`, `sex`, `avatar`, `password`, `status`, `del_flag`, `login_ip`, `login_date`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`, `gmt_create`, `gmt_modified`)
VALUES
	(1,103,'admin','超级管理员','00','xxxx@163.com','','1','','$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2','0','0','127.0.0.1','2018-03-16 11:33:00','admin','2018-03-16 11:33:00','system','2018-03-16 11:33:00','管理员','2020-12-06 13:15:53','2020-12-06 16:16:45');


INSERT INTO `sys_user_post` (`user_id`, `post_id`, `gmt_create`, `gmt_modified`)
VALUES
	(1,1,'2020-12-06 13:15:53','2020-12-06 13:15:54'),


INSERT INTO `sys_user_role` (`user_id`, `role_id`, `gmt_create`, `gmt_modified`)
VALUES
	(1,1,'2020-12-06 13:15:54','2020-12-06 13:15:54'),






