
CREATE DATABASE IF NOT EXISTS `sysuser` DEFAULT CHARACTER SET = 'UTF8';
-------------------------------
-- `dept` 部门表
-------------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
	`id`           int(11)      NOT NULL AUTO_INCREMENT COMMENT '部门id' ,
	`name`         varchar(20)  NOT NULL DEFAULT ''     COMMENT '部门名称' ,
	`parent_id`    int(11)      NOT NULL DEFAULT 0      COMMENT '上级部门的id' ,
	`level`        varchar(200) NOT NULL DEFAULT ''     COMMENT '部门层级' ,
	`seq`          int(11)      NOT NULL DEFAULT 0      COMMENT '部门在当前层级下的排序，从小到大排序' ,
	`remark`       varchar(200)          DEFAULT ''     COMMENT '备注信息' ,
	`operator`     varchar(20)  NOT NULL DEFAULT ''     COMMENT '操作者' ,
	`operate_time` TIMESTAMP    NOT NULL DEFAULT NOW() ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次操作的时间' ,
	`operate_ip`   varchar(20)  NOT NULL DEFAULT ''     COMMENT '最后一次更新操作者的ip地址' ,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='部门数据表';
BEGIN;
INSERT INTO `sys_dept` VALUES ('1', '技术部', '0', '0', '1', '技术部', 'system', '2017-10-11 07:21:40', '127.0.0.1'), ('2', '后端开发', '1', '0.1', '1', '后端', 'system-update', '2017-10-12 07:56:16', '127.0.0.1'), ('3', '前端开发', '1', '0.1', '2', '', 'system-update', '2017-10-14 11:29:45', '127.0.0.1'), ('4', 'UI设计', '1', '0.1', '3', '', 'system', '2017-10-12 07:55:43', '127.0.0.1'), ('11', '产品部', '0', '0', '2', '', 'Admin', '2017-10-16 22:52:29', '0:0:0:0:0:0:0:1'), ('12', '客服部', '0', '0', '4', '', 'Admin', '2017-10-17 00:22:55', '0:0:0:0:0:0:0:1');
COMMIT;
		--	+--------------+--------------+------+-----+-------------------+-----------------------------+
		--	| Field        | Type         | Null | Key | Default           | Extra                       |
		--	+--------------+--------------+------+-----+-------------------+-----------------------------+
		--	| id           | int(11)      | NO   | PRI | NULL              | auto_increment              |
		--	| name         | varchar(20)  | NO   |     |                   |                             |
		--	| parent_id    | int(11)      | NO   |     | 0                 |                             |
		--	| level        | varchar(200) | NO   |     |                   |                             |
		--	| seq          | int(11)      | NO   |     | 0                 |                             |
		--	| remark       | varchar(200) | YES  |     |                   |                             |
		--	| operator     | varchar(20)  | NO   |     |                   |          			         |
		--	| operate_time | timestamp    | NO   |     | CURRENT_TIMESTAMP | on update CURRENT_TIMESTAMP |              |
		--	| operate_ip   | varchar(20)  | NO   |     |                   |                             |
		--	+--------------+--------------+------+-----+-------------------+-----------------------------+
-------------------------------
-- `sys_user` 用户数据表
-------------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
	`id`           int(11)      NOT NULL AUTO_INCREMENT COMMENT '用户id，主键自动递增' ,
	`username`     varchar(20)  NOT NULL DEFAULT ''     COMMENT '用户名称' ,
	`telephone`    varchar(13)  NOT NULL DEFAULT ''     COMMENT '手机号码' ,
	`mail`         varchar(20)  NOT NULL DEFAULT ''     COMMENT '邮箱号' ,
	`password`     varchar(130) NOT NULL DEFAULT ''     COMMENT '登录密码（加密后的密码，SHA-512为128为）' ,
	`dept_id`      int(11)      NOT NULL DEFAULT '0'    COMMENT '用户所在部门的id' ,
	`status`       int(11)      NOT NULL DEFAULT '1'    COMMENT '状态。1：正常，0：冻结状态，2：删除状态（黑名单）' ,
	`remark`       varchar(200)          DEFAULT ''     COMMENT '用户的备注信息' ,
	`operator`     varchar(20)  NOT NULL DEFAULT ''     COMMENT '操作者' ,
	`operate_time` timestamp    NOT NULL DEFAULT NOW() ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次更新的时间' ,
	`operate_ip`   varchar(20)  NOT NULL DEFAULT ''     COMMENT '最后一次更新者的ip地址' ,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户数据表';
BEGIN;
INSERT INTO `sys_user` VALUES ('1', 'Admin', '18612344321', 'admin@qq.com', '25D55AD283AA400AF464C76D713C07AD', '1', '1', 'admin', 'system', '2017-10-13 08:46:16', '127.0.0.1'), ('2', 'Jimin', '13188889999', 'jimin@qq.com', '25D55AD283AA400AF464C76D713C07AD', '1', '1', 'jimin.zheng', 'Admin', '2017-10-14 14:45:19', '127.0.0.1'), ('3', 'Jimmy', '13812344311', 'jimmy@qq.com', '25D55AD283AA400AF464C76D713C07AD', '2', '1', '', 'Admin', '2017-10-16 12:57:35', '0:0:0:0:0:0:0:1'), ('4', 'Kate', '13144445555', 'kate@qq.com', '25D55AD283AA400AF464C76D713C07AD', '1', '1', 'sss', 'Admin', '2017-10-16 23:02:51', '0:0:0:0:0:0:0:1'), ('5', '服务员A', '18677778888', 'service@qq.com', '25D55AD283AA400AF464C76D713C07AD', '12', '1', '', 'Admin', '2017-10-17 00:22:15', '0:0:0:0:0:0:0:1');
COMMIT;
		--	+--------------+--------------+------+-----+-------------------+-----------------------------+
		--	| Field        | Type         | Null | Key | Default           | Extra                       |
		--	+--------------+--------------+------+-----+-------------------+-----------------------------+
		--	| id           | int(11)      | NO   | PRI | NULL              | auto_increment              |
		--	| username     | varchar(20)  | NO   |     |                   |                             |
		--	| telephone    | varchar(13)  | NO   |     |                   |                             |
		--	| mail         | varchar(20)  | NO   |     |                   |                             |
		--	| password     | varchar(130) | NO   |     |                   |                             |
		--	| dept_id      | int(11)      | NO   |     | 0                 |                             |
		--	| status       | int(11)      | NO   |     | 1                 |                             |
		--	| remark       | varchar(200) | YES  |     |                   |                             |
		--	| operator     | varchar(20)  | NO   |     |                   |                             |
		--	| operate_time | timestamp    | NO   |     | CURRENT_TIMESTAMP | on update CURRENT_TIMESTAMP |
		--	| operate_ip   | varchar(20)  | NO   |     |                   |                             |
		--	+--------------+--------------+------+-----+-------------------+-----------------------------+
-------------------------------
-- `sys_acl` 权限数据表
-------------------------------
DROP TABLE IF EXISTS `sys_acl`;
CREATE TABLE `sys_acl` (
	`id`            int(11)      NOT NULL AUTO_INCREMENT COMMENT '权限的id' ,
	`code`          varchar(20)  NOT NULL DEFAULT ''     COMMENT '权限码（根据业务需要，用于判断该用户是否拥有权限等等）' ,
	`name`          varchar(20)  NOT NULL DEFAULT ''     COMMENT '权限的名称' ,
	`acl_module_id` int(11)      NOT NULL DEFAULT '0'    COMMENT '当前这个权限所对应的权限模块的ID编号' ,
	`url`           varchar(100) NOT NULL DEFAULT ''     COMMENT '指定当前这个权限的url路径（可能会有空值）' ,
	`type`          int(11)      NOT NULL DEFAULT '3'    COMMENT '指的是：1菜单，2按钮，以及3其它。' ,
	`status`        int(11)      NOT NULL DEFAULT '1'    COMMENT '权限的状态。1：正常，0：冻结状态' ,
	`seq`           int(11)      NOT NULL DEFAULT '0'    COMMENT '权限在当前层级下的排序，0从小到大，1从大到小 排序' ,
	`remark`        varchar(200)          DEFAULT ''     COMMENT '备注信息' ,
	`operator`      varchar(20)  NOT NULL DEFAULT ''     COMMENT '操作者' ,
	`operate_time`  TIMESTAMP    NOT NULL DEFAULT NOW() ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次操作的时间' ,
	`operate_ip`    varchar(20)  NOT NULL DEFAULT ''     COMMENT '最后一次更新操作者的ip地址' ,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限数据表';
BEGIN;
INSERT INTO `sys_acl` VALUES ('1', '20171015095130_26', '进入产品管理界面', '1', '/sys/product/product.page', '1', '1', '1', '', 'Admin', '2017-10-15 09:51:30', '0:0:0:0:0:0:0:1'), ('2', '20171015095322_14', '查询产品列表', '1', '/sys/product/page.json', '2', '1', '2', '', 'Admin', '2017-10-15 09:53:22', '0:0:0:0:0:0:0:1'), ('3', '20171015095350_69', '产品上架', '1', '/sys/product/online.json', '2', '1', '3', '', 'Admin', '2017-10-15 09:53:51', '0:0:0:0:0:0:0:1'), ('4', '20171015095420_7', '产品下架', '1', '/sys/product/offline.json', '2', '1', '4', '', 'Admin', '2017-10-15 10:11:28', '0:0:0:0:0:0:0:1'), ('5', '20171015212626_63', '进入订单页', '2', '/sys/order/order.page', '1', '1', '1', '', 'Admin', '2017-10-15 21:26:27', '0:0:0:0:0:0:0:1'), ('6', '20171015212657_12', '查询订单列表', '2', '/sys/order/list.json', '2', '1', '2', '', 'Admin', '2017-10-15 21:26:57', '0:0:0:0:0:0:0:1'), ('7', '20171015212907_36', '进入权限管理页', '7', '/sys/aclModule/acl.page', '1', '1', '1', '', 'Admin', '2017-10-15 21:29:07', '0:0:0:0:0:0:0:1'), ('8', '20171015212938_27', '进入角色管理页', '8', '/sys/role/role.page', '1', '1', '1', '', 'Admin', '2017-10-16 17:49:38', '0:0:0:0:0:0:0:1'), ('9', '20171015213009_0', '进入用户管理页', '9', '/sys/dept/dept.page', '1', '1', '1', '', 'Admin', '2017-10-15 21:30:09', '0:0:0:0:0:0:0:1'), ('10', '20171016230429_8', '进入权限更新记录页面', '11', '/sys/log/log.page', '1', '1', '1', '', 'Admin', '2017-10-16 23:04:49', '0:0:0:0:0:0:0:1');
COMMIT;
		--	+---------------+--------------+------+-----+-------------------+-----------------------------+
		--	| Field         | Type         | Null | Key | Default           | Extra                       |
		--	+---------------+--------------+------+-----+-------------------+-----------------------------+
		--	| id            | int(11)      | NO   | PRI | NULL              | auto_increment              |
		--	| code          | varchar(20)  | NO   |     |                   |                             |
		--	| name          | varchar(20)  | NO   |     |                   |                             |
		--	| acl_module_id | int(11)      | NO   |     | 0                 |                             |
		--	| url           | varchar(100) | NO   |     |                   |                             |
		--	| type          | int(11)      | NO   |     | 0                 |                             |
		--	| status        | int(11)      | NO   |     | 1                 |                             |
		--	| seq           | int(11)      | NO   |     | 0                 |                             |
		--	| remark        | varchar(200) | YES  |     |                   |                             |
		--	| operator      | varchar(20)  | NO   |     |                   |                             |
		--	| operate_time  | timestamp    | NO   |     | CURRENT_TIMESTAMP | on update CURRENT_TIMESTAMP |
		--	| operate_ip    | varchar(20)  | NO   |     |                   |                             |
		--	+---------------+--------------+------+-----+-------------------+-----------------------------+
-------------------------------
-- `sys_acl_module` 权限数据表
-------------------------------
DROP TABLE IF EXISTS `sys_acl_module`;
CREATE TABLE `sys_acl_module` (
	`id`           int(11)      NOT NULL AUTO_INCREMENT COMMENT '权限模块的id' ,
	`name`         varchar(20)  NOT NULL DEFAULT ''     COMMENT '权限模块名称' ,
	`parent_id`    int(11)      NOT NULL DEFAULT '0'    COMMENT '上级权限模块的id' ,
	`level`        varchar(200) NOT NULL DEFAULT ''     COMMENT '权限模块的层级' ,
	`seq`          int(11)      NOT NULL DEFAULT '0'    COMMENT '权限模块在当前层级下的排序，从小到大排序' ,
	`status`       int(11)      NOT NULL DEFAULT '1'    COMMENT '权限模块的状态。1：正常，0：冻结状态' ,
	`remark`       varchar(200)          DEFAULT ''     COMMENT '备注信息' ,
	`operator`     varchar(20)  NOT NULL DEFAULT ''     COMMENT '操作者' ,
	`operate_time` TIMESTAMP    NOT NULL DEFAULT NOW() ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次操作的时间' ,
	`operate_ip`   varchar(20)  NOT NULL DEFAULT ''     COMMENT '最后一次更新操作者的ip地址' ,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限模块数据表';
BEGIN;
INSERT INTO `sys_acl_module` VALUES ('1', '产品管理', '0', '0', '1', '1', 'product', 'Admin', '2017-10-14 21:13:15', '0:0:0:0:0:0:0:1'), ('2', '订单管理', '0', '0', '2', '1', '', 'Admin', '2017-10-14 20:17:11', '0:0:0:0:0:0:0:1'), ('3', '公告管理', '0', '0', '3', '1', '', 'Admin', '2017-10-14 20:17:21', '0:0:0:0:0:0:0:1'), ('4', '出售中产品管理', '1', '0.1', '1', '1', '', 'Admin', '2017-10-14 21:13:39', '0:0:0:0:0:0:0:1'), ('5', '下架产品管理', '1', '0.1', '2', '1', '', 'Admin', '2017-10-14 20:18:02', '0:0:0:0:0:0:0:1'), ('6', '权限管理', '0', '0', '4', '1', '', 'Admin', '2017-10-15 21:27:37', '0:0:0:0:0:0:0:1'), ('7', '权限管理', '6', '0.6', '1', '1', '', 'Admin', '2017-10-15 21:27:57', '0:0:0:0:0:0:0:1'), ('8', '角色管理', '6', '0.6', '2', '1', '', 'Admin', '2017-10-15 21:28:22', '0:0:0:0:0:0:0:1'), ('9', '用户管理', '6', '0.6', '2', '1', '', 'Admin', '2017-10-15 21:28:36', '0:0:0:0:0:0:0:1'), ('10', '运维管理', '0', '0', '6', '1', '', 'Admin', '2017-10-16 23:03:37', '0:0:0:0:0:0:0:1'), ('11', '权限更新记录管理', '6', '0.6', '4', '1', '', 'Admin', '2017-10-16 23:04:07', '0:0:0:0:0:0:0:1');
COMMIT;
		--	+--------------+--------------+------+-----+-------------------+-----------------------------+
		--	| Field        | Type         | Null | Key | Default           | Extra                       |
		--	+--------------+--------------+------+-----+-------------------+-----------------------------+
		--	| id           | int(11)      | NO   | PRI | NULL              | auto_increment              |
		--	| name         | varchar(20)  | NO   |     |                   |                             |
		--	| parent_id    | int(11)      | NO   |     | 0                 |                             |
		--	| level        | varchar(200) | NO   |     |                   |                             |
		--	| status       | int(11)      | NO   |     | 1                 |                             |
		--	| seq          | int(11)      | NO   |     | 0                 |                             |
		--	| remark       | varchar(200) | YES  |     |                   |                             |
		--	| operator     | varchar(20)  | NO   |     |                   |                             |
		--	| operate_time | timestamp    | NO   |     | CURRENT_TIMESTAMP | on update CURRENT_TIMESTAMP |
		--	| operate_ip   | varchar(20)  | NO   |     |                   |                             |
		--	+--------------+--------------+------+-----+-------------------+-----------------------------+
-------------------------------
-- `sys_role` 角色数据表
-------------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
	`id`           int(11)      NOT NULL AUTO_INCREMENT COMMENT '角色的id' ,
	`name`         varchar(20)  NOT NULL                COMMENT '角色名称' ,
	`type`         int(11)      NOT NULL DEFAULT '1'    COMMENT '角色类型：1：管理员角色，2：其它用户角色（）' ,
	`status`       int(11)      NOT NULL DEFAULT '1'    COMMENT '角色状态。1：正常可用，0：冻结状态' ,
	`remark`       varchar(200)          DEFAULT ''     COMMENT '备注信息' ,
	`operator`     varchar(20)  NOT NULL DEFAULT ''     COMMENT '操作者' ,
	`operate_time` TIMESTAMP    NOT NULL DEFAULT NOW() ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次操作的时间' ,
	`operate_ip`   varchar(20)  NOT NULL DEFAULT ''     COMMENT '最后一次更新操作者的ip地址' ,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限模块数据表';
BEGIN;
INSERT INTO `sys_role` VALUES ('1', '产品管理员', '1', '1', '', 'Admin', '2017-10-15 12:42:47', '0:0:0:0:0:0:0:1'), ('2', '订单管理员', '1', '1', '', 'Admin', '2017-10-15 12:18:59', '0:0:0:0:0:0:0:1'), ('3', '公告管理员', '1', '1', '', 'Admin', '2017-10-15 12:19:10', '0:0:0:0:0:0:0:1'), ('4', '权限管理员', '1', '1', '', 'Admin', '2017-10-15 21:30:36', '0:0:0:0:0:0:0:1'), ('5', '运维管理员', '1', '1', '运维', 'Admin', '2017-10-17 00:23:28', '0:0:0:0:0:0:0:1');
COMMIT;
		--	+--------------+--------------+------+-----+-------------------+-----------------------------+
		--	| Field        | Type         | Null | Key | Default           | Extra                       |
		--	+--------------+--------------+------+-----+-------------------+-----------------------------+
		--	| id           | int(11)      | NO   | PRI | NULL              | auto_increment              |
		--	| name         | varchar(20)  | NO   |     |                   |                             |
		--	| type         | int(11)      | NO   |     | 1                 |                             |
		--	| status       | int(11)      | NO   |     | 1                 |                             |
		--	| remark       | varchar(200) | YES  |     |                   |                             |
		--	| operator     | varchar(20)  | NO   |     |                   |                             |
		--	| operate_time | timestamp    | NO   |     | CURRENT_TIMESTAMP | on update CURRENT_TIMESTAMP |
		--	| operate_ip   | varchar(20)  | NO   |     |                   |                             |
		--	+--------------+--------------+------+-----+-------------------+-----------------------------+
-------------------------------------------
-- `sys_role_user`角色和用户之间的关联关系的数据表
-------------------------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
	`id`           int(11)      NOT NULL AUTO_INCREMENT COMMENT '主键的id' ,
	`role_id`      int(11)      NOT NULL                COMMENT '角色的ID号' ,
	`user_id`      int(11)      NOT NULL                COMMENT '用户的ID号' ,
	`operator`     varchar(20)  NOT NULL DEFAULT ''     COMMENT '操作者' ,
	`operate_time` TIMESTAMP    NOT NULL DEFAULT NOW() ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次操作的时间' ,
	`operate_ip`   varchar(20)  NOT NULL DEFAULT ''     COMMENT '最后一次更新操作者的ip地址' ,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与用户关联数据表';
BEGIN;
INSERT INTO `sys_role_user` VALUES ('16', '4', '1', 'Admin', '2017-10-17 00:24:04', '0:0:0:0:0:0:0:1'), ('17', '4', '4', 'Admin', '2017-10-17 00:24:04', '0:0:0:0:0:0:0:1');
COMMIT;
		--	+--------------+-------------+------+-----+-------------------+-----------------------------+
		--	| Field        | Type        | Null | Key | Default           | Extra                       |
		--	+--------------+-------------+------+-----+-------------------+-----------------------------+
		--	| id           | int(11)     | NO   | PRI | NULL              | auto_increment              |
		--	| role_id      | int(11)     | NO   |     | 0                 |                             |
		--	| user_id      | int(11)     | NO   |     | 0                 |                             |
		--	| operator     | varchar(20) | NO   |     |                   |                             |
		--	| operate_time | timestamp   | NO   |     | CURRENT_TIMESTAMP | on update CURRENT_TIMESTAMP |
		--	| operate_ip   | varchar(20) | NO   |     |                   |                             |
		--	+--------------+-------------+------+-----+-------------------+-----------------------------+
-------------------------------------------
-- `sys_role_acl`角色和权限之间的关联关系的数据表
-------------------------------------------
DROP TABLE IF EXISTS `sys_role_acl`;
CREATE TABLE `sys_role_acl` (
	`id`           int(11)      NOT NULL AUTO_INCREMENT COMMENT '主键的id' ,
	`role_id`      int(11)      NOT NULL                COMMENT '权限的ID号' ,
	`acl_id`       int(11)      NOT NULL                COMMENT '用户的ID号' ,
	`operator`     varchar(20)  NOT NULL DEFAULT ''     COMMENT '操作者' ,
	`operate_time` TIMESTAMP    NOT NULL DEFAULT NOW() ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次操作的时间' ,
	`operate_ip`   varchar(20)  NOT NULL DEFAULT ''     COMMENT '最后一次更新操作者的ip地址' ,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色与权限关联数据表';
BEGIN;
INSERT INTO `sys_role_acl` VALUES ('9', '4', '7', 'Admin', '2017-10-16 23:34:39', '0:0:0:0:0:0:0:1'), ('10', '4', '8', 'Admin', '2017-10-16 23:34:39', '0:0:0:0:0:0:0:1'), ('11', '4', '9', 'Admin', '2017-10-16 23:34:39', '0:0:0:0:0:0:0:1'), ('12', '4', '10', 'Admin', '2017-10-16 23:34:39', '0:0:0:0:0:0:0:1');
COMMIT;
		--	+--------------+-------------+------+-----+-------------------+-----------------------------+
		--	| Field        | Type        | Null | Key | Default           | Extra                       |
		--	+--------------+-------------+------+-----+-------------------+-----------------------------+
		--	| id           | int(11)     | NO   | PRI | NULL              | auto_increment              |
		--	| role_id      | int(11)     | NO   |     | 0                 |                             |
		--	| acl_id       | int(11)     | NO   |     | 0                 |                             |
		--	| operator     | varchar(20) | NO   |     |                   |                             |
		--	| operate_time | timestamp   | NO   |     | CURRENT_TIMESTAMP | on update CURRENT_TIMESTAMP |
		--	| operate_ip   | varchar(20) | NO   |     |                   |                             |
		--	+--------------+-------------+------+-----+-------------------+-----------------------------+
-------------------------------
-- `sys_log`权限相关的更新的记录数据表（权限更新日志表）
-------------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
	`id`           int(11)      NOT NULL AUTO_INCREMENT COMMENT '主键的id' ,
	`type`         int(11)      NOT NULL DEFAULT '0'    COMMENT '权限更新的类型。1：部门，2：用户，3：权限模块，4：权限，5：角色，6：角色用户关系，7：角色权限关系（我们会采用枚举方式）' ,
	`target_id`    int(11)      NOT NULL                COMMENT '基于type后指定的对象的id,比如用户、权限、角色等表的主键' ,
	`old_value`    text         NULL                    COMMENT '修改之前的数据 ' ,
	`new_value`    text         NULL     		        COMMENT '更新之后的数据' ,
	`operator`     varchar(20)  NOT NULL DEFAULT ''     COMMENT '操作者' ,
	`operate_time` TIMESTAMP    NOT NULL DEFAULT NOW() ON UPDATE CURRENT_TIMESTAMP COMMENT '最后一次操作的时间' ,
	`operate_ip`   varchar(20)  NOT NULL DEFAULT ''     COMMENT '最后一次更新操作者的ip地址' ,
	`status`       int(11)      NOT NULL DEFAULT '0'    COMMENT '当前是否复原过，0：没有，1：复原过',
  PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限更新日志数据表';
BEGIN;
INSERT INTO `sys_log` VALUES ('2', '1', '12', '', '{\"id\":12,\"name\":\"客服部\",\"parentId\":0,\"level\":\"0\",\"seq\":3,\"operator\":\"Admin\",\"operateTime\":1508166002610,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:00:03', '0:0:0:0:0:0:0:1', '1'), ('3', '1', '12', '{\"id\":12,\"name\":\"客服部\",\"parentId\":0,\"level\":\"0\",\"seq\":3,\"operator\":\"Admin\",\"operateTime\":1508166003000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":12,\"name\":\"客服部\",\"parentId\":0,\"level\":\"0\",\"seq\":4,\"operator\":\"Admin\",\"operateTime\":1508166009313,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:00:09', '0:0:0:0:0:0:0:1', '1'), ('4', '2', '4', '', '{\"id\":4,\"username\":\"Kate\",\"telephone\":\"13144445555\",\"mail\":\"kate@qq.com\",\"password\":\"25D55AD283AA400AF464C76D713C07AD\",\"deptId\":1,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508166166297,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:02:46', '0:0:0:0:0:0:0:1', '1'), ('5', '2', '4', '{\"id\":4,\"username\":\"Kate\",\"telephone\":\"13144445555\",\"mail\":\"kate@qq.com\",\"password\":\"25D55AD283AA400AF464C76D713C07AD\",\"deptId\":1,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508166166000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":4,\"username\":\"Kate\",\"telephone\":\"13144445555\",\"mail\":\"kate@qq.com\",\"deptId\":1,\"status\":1,\"remark\":\"sss\",\"operator\":\"Admin\",\"operateTime\":1508166171320,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:02:51', '0:0:0:0:0:0:0:1', '1'), ('6', '3', '10', '', '{\"id\":10,\"name\":\"运维管理\",\"parentId\":0,\"level\":\"0\",\"seq\":5,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508166212527,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:03:33', '0:0:0:0:0:0:0:1', '1'), ('7', '3', '10', '{\"id\":10,\"name\":\"运维管理\",\"parentId\":0,\"level\":\"0\",\"seq\":5,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508166213000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":10,\"name\":\"运维管理\",\"parentId\":0,\"level\":\"0\",\"seq\":6,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508166217376,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:03:37', '0:0:0:0:0:0:0:1', '1'), ('8', '3', '11', '', '{\"id\":11,\"name\":\"权限更新记录管理\",\"parentId\":6,\"level\":\"0.6\",\"seq\":4,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508166246805,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:04:07', '0:0:0:0:0:0:0:1', '1'), ('9', '4', '10', '', '{\"id\":10,\"code\":\"20171016230429_8\",\"name\":\"进入权限更新记录页面\",\"aclModuleId\":1,\"url\":\"/sys/log/log.page\",\"type\":1,\"status\":1,\"seq\":1,\"operator\":\"Admin\",\"operateTime\":1508166269419,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:04:29', '0:0:0:0:0:0:0:1', '1'), ('10', '4', '10', '{\"id\":10,\"code\":\"20171016230429_8\",\"name\":\"进入权限更新记录页面\",\"aclModuleId\":1,\"url\":\"/sys/log/log.page\",\"type\":1,\"status\":1,\"seq\":1,\"operator\":\"Admin\",\"operateTime\":1508166269000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":10,\"name\":\"进入权限更新记录页面\",\"aclModuleId\":11,\"url\":\"/sys/log/log.page\",\"type\":1,\"status\":1,\"seq\":1,\"operator\":\"Admin\",\"operateTime\":1508166288589,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:04:49', '0:0:0:0:0:0:0:1', '1'), ('11', '5', '5', '', '{\"id\":5,\"name\":\"运维管理员\",\"type\":1,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508166301130,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:05:01', '0:0:0:0:0:0:0:1', '1'), ('12', '5', '5', '{\"id\":5,\"name\":\"运维管理员\",\"type\":1,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508166301000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":5,\"name\":\"运维管理员\",\"type\":1,\"status\":1,\"remark\":\"运维\",\"operator\":\"Admin\",\"operateTime\":1508166307317,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-16 23:05:07', '0:0:0:0:0:0:0:1', '1'), ('13', '6', '4', '[7,8,9]', '[7,8,9,10]', 'Admin', '2017-10-16 23:34:39', '0:0:0:0:0:0:0:1', '1'), ('14', '7', '4', '[1]', '[1,4]', 'Admin', '2017-10-16 23:34:44', '0:0:0:0:0:0:0:1', '1'), ('15', '2', '5', '', '{\"id\":5,\"username\":\"服务员A\",\"telephone\":\"18677778888\",\"mail\":\"service@qq.com\",\"password\":\"25D55AD283AA400AF464C76D713C07AD\",\"deptId\":12,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508170918338,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-17 00:21:58', '0:0:0:0:0:0:0:1', '1'), ('16', '2', '5', '{\"id\":5,\"username\":\"服务员A\",\"telephone\":\"18677778888\",\"mail\":\"service@qq.com\",\"password\":\"25D55AD283AA400AF464C76D713C07AD\",\"deptId\":12,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508170918000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":5,\"username\":\"服务员B\",\"telephone\":\"18677778888\",\"mail\":\"service@qq.com\",\"deptId\":12,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508170924698,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-17 00:22:05', '0:0:0:0:0:0:0:1', '1'), ('17', '2', '5', '{\"id\":5,\"username\":\"服务员B\",\"telephone\":\"18677778888\",\"mail\":\"service@qq.com\",\"password\":\"25D55AD283AA400AF464C76D713C07AD\",\"deptId\":12,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508170925000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":5,\"username\":\"服务员A\",\"telephone\":\"18677778888\",\"mail\":\"service@qq.com\",\"password\":\"25D55AD283AA400AF464C76D713C07AD\",\"deptId\":12,\"status\":1,\"operator\":\"Admin\",\"operateTime\":1508170934791,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-17 00:22:15', '0:0:0:0:0:0:0:1', '1'), ('18', '1', '12', '{\"id\":12,\"name\":\"客服部\",\"parentId\":0,\"level\":\"0\",\"seq\":4,\"operator\":\"Admin\",\"operateTime\":1508166009000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":12,\"name\":\"客服部A\",\"parentId\":0,\"level\":\"0\",\"seq\":4,\"operator\":\"Admin\",\"operateTime\":1508170966051,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-17 00:22:46', '0:0:0:0:0:0:0:1', '1'), ('19', '1', '12', '{\"id\":12,\"name\":\"客服部A\",\"parentId\":0,\"level\":\"0\",\"seq\":4,\"operator\":\"Admin\",\"operateTime\":1508170966000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":12,\"name\":\"客服部\",\"parentId\":0,\"level\":\"0\",\"seq\":4,\"operator\":\"Admin\",\"operateTime\":1508170975242,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-17 00:22:55', '0:0:0:0:0:0:0:1', '1'), ('20', '5', '5', '{\"id\":5,\"name\":\"运维管理员\",\"type\":1,\"status\":1,\"remark\":\"运维\",\"operator\":\"Admin\",\"operateTime\":1508166307000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":5,\"name\":\"运维管理员A\",\"type\":1,\"status\":1,\"remark\":\"运维\",\"operator\":\"Admin\",\"operateTime\":1508170997531,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-17 00:23:18', '0:0:0:0:0:0:0:1', '1'), ('21', '5', '5', '{\"id\":5,\"name\":\"运维管理员A\",\"type\":1,\"status\":1,\"remark\":\"运维\",\"operator\":\"Admin\",\"operateTime\":1508170998000,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', '{\"id\":5,\"name\":\"运维管理员\",\"type\":1,\"status\":1,\"remark\":\"运维\",\"operator\":\"Admin\",\"operateTime\":1508171007651,\"operateIp\":\"0:0:0:0:0:0:0:1\"}', 'Admin', '2017-10-17 00:23:28', '0:0:0:0:0:0:0:1', '1'), ('22', '7', '4', '[1,4]', '[1,4,2,3,5]', 'Admin', '2017-10-17 00:23:53', '0:0:0:0:0:0:0:1', '1'), ('23', '7', '4', '[1,4,2,3,5]', '[1,4]', 'Admin', '2017-10-17 00:24:04', '0:0:0:0:0:0:0:1', '1'), ('24', '6', '5', '[]', '[7,8,9,10]', 'Admin', '2017-10-17 00:24:23', '0:0:0:0:0:0:0:1', '1'), ('25', '6', '5', '[7,8,9,10]', '[]', 'Admin', '2017-10-17 00:24:34', '0:0:0:0:0:0:0:1', '1');
COMMIT;
		--	+--------------+-------------+------+-----+-------------------+-----------------------------+
		--	| Field        | Type        | Null | Key | Default           | Extra                       |
		--	+--------------+-------------+------+-----+-------------------+-----------------------------+
		--	| id           | int(11)     | NO   | PRI | NULL              | auto_increment              |
		--	| type         | int(11)     | NO   |     | 0                 |                             |
		--	| target_id    | int(11)     | NO   |     | 0                 |                             |
		--	| old_value    | text        | YES  |     | NULL              |                             |
		--	| new_value    | text        | YES  |     | NULL              |                             |
		--	| operator     | varchar(20) | NO   |     |                   |                             |
		--	| operate_time | timestamp   | NO   |     | CURRENT_TIMESTAMP | on update CURRENT_TIMESTAMP |
		--	| operate_ip   | varchar(20) | NO   |     |                   |                             |
		--	+--------------+-------------+------+-----+-------------------+-----------------------------+