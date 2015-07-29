/*
MySQL Data Transfer
Source Host: localhost
Source Database: orderdish
Target Host: localhost
Target Database: orderdish
Date: 2013/1/21 15:45:48
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for authority
-- ----------------------------
DROP TABLE IF EXISTS `authority`;
CREATE TABLE `authority` (
  `authority_id` char(10) NOT NULL,
  `authority_name` varchar(20) NOT NULL,
  PRIMARY KEY  (`authority_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for authority_role
-- ----------------------------
DROP TABLE IF EXISTS `authority_role`;
CREATE TABLE `authority_role` (
  `role_id` char(10) default NULL,
  `authority_id` char(10) default NULL,
  KEY `fk_role_ar` (`role_id`),
  KEY `fk_authority` (`authority_id`),
  CONSTRAINT `fk_authority` FOREIGN KEY (`authority_id`) REFERENCES `authority` (`authority_id`),
  CONSTRAINT `fk_role_ar` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `cate_id` char(10) NOT NULL,
  `cate_name` varchar(30) NOT NULL,
  `cate_delflag` char(1) default '0',
  `cate_createtime` datetime default NULL,
  `cate_updatetime` datetime default NULL,
  `mcate_id` char(10) default NULL,
  PRIMARY KEY  (`cate_id`),
  KEY `fk_mcate_cate` (`mcate_id`),
  CONSTRAINT `fk_mcate_cate` FOREIGN KEY (`mcate_id`) REFERENCES `maincategory` (`mcate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for countunit
-- ----------------------------
DROP TABLE IF EXISTS `countunit`;
CREATE TABLE `countunit` (
  `unit_id` char(10) NOT NULL,
  `unit_name` varchar(30) NOT NULL,
  `unit_remark` varchar(60) default NULL,
  `unit_delflg` char(1) default '0',
  PRIMARY KEY  (`unit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for maincategory
-- ----------------------------
DROP TABLE IF EXISTS `maincategory`;
CREATE TABLE `maincategory` (
  `mcate_id` char(10) NOT NULL,
  `mcate_name` varchar(30) NOT NULL,
  `mcate_delflag` char(1) default '0',
  PRIMARY KEY  (`mcate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for orderdishconfirm
-- ----------------------------
DROP TABLE IF EXISTS `orderdishconfirm`;
CREATE TABLE `orderdishconfirm` (
  `odc_vege_id` char(10) NOT NULL default '',
  `odc_serveTime` datetime default NULL,
  `odc_vegePri` float(6,1) default NULL,
  `odc_orderId` char(30) NOT NULL default '0',
  `odc_delFlg` char(1) NOT NULL default '0',
  `odc_vegeQua` float(11,1) default NULL,
  `odc_remark` varchar(50) default NULL,
  `odc_code` char(10) NOT NULL default '0',
  `odc_user_id` char(10) NOT NULL,
  PRIMARY KEY  (`odc_vege_id`,`odc_orderId`,`odc_code`,`odc_user_id`),
  KEY `fk_odc_order` (`odc_orderId`),
  KEY `fk_odc_user` (`odc_user_id`),
  CONSTRAINT `fk_odc_order` FOREIGN KEY (`odc_orderId`) REFERENCES `orderinfo` (`o_id`),
  CONSTRAINT `fk_odc_user` FOREIGN KEY (`odc_user_id`) REFERENCES `workerinfo` (`w_id`),
  CONSTRAINT `fk_odc_vege` FOREIGN KEY (`odc_vege_id`) REFERENCES `vege` (`vege_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for orderinfo
-- ----------------------------
DROP TABLE IF EXISTS `orderinfo`;
CREATE TABLE `orderinfo` (
  `o_id` char(30) NOT NULL,
  `point_id` char(10) default NULL,
  `o_workerId` char(10) default NULL,
  `o_totalPri` float(7,1) default NULL,
  `o_ODTime` datetime default NULL,
  `o_SAFlg` char(1) default '0',
  `o_guestnum` int(11) default '0',
  PRIMARY KEY  (`o_id`),
  KEY `fk_o_p` (`point_id`),
  KEY `fk_o_wii` (`o_workerId`),
  CONSTRAINT `fk_o_p` FOREIGN KEY (`point_id`) REFERENCES `point` (`point_id`),
  CONSTRAINT `fk_o_wii` FOREIGN KEY (`o_workerId`) REFERENCES `workerinfo` (`w_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for point
-- ----------------------------
DROP TABLE IF EXISTS `point`;
CREATE TABLE `point` (
  `point_id` char(10) NOT NULL,
  `point_no` varchar(10) default NULL,
  `point_name` varchar(10) default NULL,
  `ptype_id` char(10) default NULL,
  `room_id` char(10) default NULL,
  `point_state` char(1) default '0',
  `point_delflg` char(1) default '0',
  `point_num` int(11) NOT NULL default '0',
  `point_stopflg` char(1) default '0',
  PRIMARY KEY  (`point_id`),
  KEY `fk_r_point` (`room_id`),
  KEY `fk_ptype_point` (`ptype_id`),
  CONSTRAINT `fk_ptype_point` FOREIGN KEY (`ptype_id`) REFERENCES `pointtype` (`ptype_id`),
  CONSTRAINT `fk_r_point` FOREIGN KEY (`room_id`) REFERENCES `room` (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pointtype
-- ----------------------------
DROP TABLE IF EXISTS `pointtype`;
CREATE TABLE `pointtype` (
  `ptype_id` char(10) NOT NULL,
  `ptype_no` varchar(10) NOT NULL,
  `ptype_name` varchar(10) NOT NULL,
  `ptype_delflg` char(1) default '0',
  `ptype_createtime` datetime default NULL,
  `ptype_lastedittime` datetime default NULL,
  PRIMARY KEY  (`ptype_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `role_id` char(10) NOT NULL,
  `role_name` varchar(20) NOT NULL,
  PRIMARY KEY  (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for room
-- ----------------------------
DROP TABLE IF EXISTS `room`;
CREATE TABLE `room` (
  `room_id` char(10) NOT NULL,
  `room_name` varchar(20) NOT NULL,
  `room_no` varchar(10) NOT NULL,
  `room_delflg` char(1) default '0',
  `room_createtime` datetime default NULL,
  `room_lastedittime` datetime default NULL,
  PRIMARY KEY  (`room_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for settleaccounts
-- ----------------------------
DROP TABLE IF EXISTS `settleaccounts`;
CREATE TABLE `settleaccounts` (
  `sa_streamID` char(10) NOT NULL,
  `sa_OrderId` char(30) default NULL,
  `sa_createtime` datetime default NULL,
  `createw_id` char(10) default NULL,
  `sa_requestReceive` float(7,1) default NULL,
  `sa_factReceive` float(7,1) default NULL,
  `sa_ZLMoney` float(6,1) default NULL,
  `sa_giveBillflg` char(1) default '0',
  `sa_remark` varchar(50) default NULL,
  PRIMARY KEY  (`sa_streamID`),
  KEY `fk_gc_order` (`sa_OrderId`),
  KEY `fk_createw_id` (`createw_id`),
  CONSTRAINT `fk_createw_id` FOREIGN KEY (`createw_id`) REFERENCES `workerinfo` (`w_id`),
  CONSTRAINT `fk_gc_order` FOREIGN KEY (`sa_OrderId`) REFERENCES `orderinfo` (`o_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for vege
-- ----------------------------
DROP TABLE IF EXISTS `vege`;
CREATE TABLE `vege` (
  `vege_id` char(10) NOT NULL,
  `vege_name` varchar(20) NOT NULL,
  `vege_vegeprice` float(6,2) NOT NULL,
  `vege_useflg` char(1) default '1',
  `vege_intro` varchar(500) default NULL,
  `unit_id` char(10) default NULL,
  `cate_id` char(10) default NULL,
  `vt_id` char(10) default NULL,
  `vs_id` char(10) default NULL,
  PRIMARY KEY  (`vege_id`),
  KEY `fk_unit_vege` (`unit_id`),
  KEY `fk_cate_vege` (`cate_id`),
  KEY `fk_type_vege` (`vt_id`),
  KEY `fk_veges_vege` (`vs_id`),
  CONSTRAINT `fk-veges_vege` FOREIGN KEY (`vs_id`) REFERENCES `vegestandard` (`vs_id`),
  CONSTRAINT `fk_cate_vege` FOREIGN KEY (`cate_id`) REFERENCES `category` (`cate_id`),
  CONSTRAINT `fk_type_vege` FOREIGN KEY (`vt_id`) REFERENCES `vegetype` (`vt_id`),
  CONSTRAINT `fk_unit_vege` FOREIGN KEY (`unit_id`) REFERENCES `countunit` (`unit_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for vegeimage
-- ----------------------------
DROP TABLE IF EXISTS `vegeimage`;
CREATE TABLE `vegeimage` (
  `vimage_id` char(10) NOT NULL,
  `vege_id` char(10) NOT NULL,
  `vimage_ismain` char(1) NOT NULL default '0',
  `vimage_path` varchar(50) NOT NULL,
  `del_flg` char(1) NOT NULL default '0',
  PRIMARY KEY  (`vimage_id`),
  KEY `fk_image_vege` (`vege_id`),
  CONSTRAINT `fk_image_vege` FOREIGN KEY (`vege_id`) REFERENCES `vege` (`vege_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for vegestandard
-- ----------------------------
DROP TABLE IF EXISTS `vegestandard`;
CREATE TABLE `vegestandard` (
  `vs_id` char(10) NOT NULL,
  `vs_name` varchar(10) NOT NULL,
  PRIMARY KEY  (`vs_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for vegetype
-- ----------------------------
DROP TABLE IF EXISTS `vegetype`;
CREATE TABLE `vegetype` (
  `vt_id` char(10) NOT NULL,
  `vt_name` varchar(30) NOT NULL,
  `vt_remark` varchar(60) default NULL,
  `vt_delflg` char(1) default '0',
  PRIMARY KEY  (`vt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for workerinfo
-- ----------------------------
DROP TABLE IF EXISTS `workerinfo`;
CREATE TABLE `workerinfo` (
  `w_id` char(10) NOT NULL,
  `w_name` varchar(10) NOT NULL,
  `w_password` varchar(10) default '8888',
  `w_sex` varchar(2) default NULL,
  `w_delflg` char(1) default '0',
  `w_stateflg` char(1) default '0',
  `role_id` char(10) default NULL,
  `is_loginflg` char(1) NOT NULL default '0',
  PRIMARY KEY  (`w_id`),
  KEY `fk_role_w` (`role_id`),
  CONSTRAINT `fk_role_w` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for workerinfo_point
-- ----------------------------
DROP TABLE IF EXISTS `workerinfo_point`;
CREATE TABLE `workerinfo_point` (
  `wp_id` char(10) NOT NULL,
  `point_id` char(10) default NULL,
  `w_id` char(10) default NULL,
  `wp_createtime` datetime default NULL,
  `wp_lastedittime` datetime default NULL,
  PRIMARY KEY  (`wp_id`),
  KEY `fk_point_wp` (`point_id`),
  KEY `fk_w_wp` (`w_id`),
  CONSTRAINT `fk_point_wp` FOREIGN KEY (`point_id`) REFERENCES `point` (`point_id`),
  CONSTRAINT `fk_w_wp` FOREIGN KEY (`w_id`) REFERENCES `workerinfo` (`w_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `authority` VALUES ('001', '登陆');
INSERT INTO `authority` VALUES ('002', '修改');
INSERT INTO `authority` VALUES ('003', '查询');
INSERT INTO `authority` VALUES ('004', '删除');
INSERT INTO `authority` VALUES ('007', '上菜');
INSERT INTO `authority` VALUES ('008', '添加');
INSERT INTO `authority_role` VALUES ('001', '001');
INSERT INTO `pointtype` VALUES ('01', '01', '大厅', '0', null, null);
INSERT INTO `pointtype` VALUES ('02', '02', '包厢', '0', null, null);
INSERT INTO `role` VALUES ('001', '服务员');
INSERT INTO `room` VALUES ('001', '一楼', '001', '0', null, '2013-01-10 11:31:28');
INSERT INTO `workerinfo` VALUES ('001', '赵六', '8888', '1', '0', '0', '001', '0');
