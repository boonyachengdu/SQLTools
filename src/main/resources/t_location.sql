/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50621
Source Host           : localhost:3306
Source Database       : mycat_hsae_db

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2017-06-14 19:56:06
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_location`
-- ----------------------------
DROP TABLE IF EXISTS `t_location`;
CREATE TABLE `t_location` (
  `F_ID` bigint(20) NOT NULL,
  `F_VEHICLE_ID` bigint(20) DEFAULT NULL,
  `F_LATITUDE` int(11) DEFAULT NULL,
  `F_LONGITUDE` int(11) DEFAULT NULL,
  `F_HIGH` smallint(6) DEFAULT NULL,
  `F_SPEED` smallint(6) DEFAULT NULL,
  `F_DIRECTION` smallint(6) DEFAULT NULL,
  `F_ALARM_COUNT` smallint(6) DEFAULT NULL,
  `F_DSPEED` smallint(6) DEFAULT NULL,
  `F_OIL_LEVEL` smallint(6) DEFAULT NULL,
  `F_MILEAGE` int(11) DEFAULT NULL,
  `F_ALARM_DATA` int(11) DEFAULT NULL,
  `F_GPS_TIME` timestamp NULL DEFAULT NULL,
  `F_RECV_TIME` timestamp NULL DEFAULT NULL,
  `F_ACC_STATUS` smallint(6) DEFAULT NULL,
  `F_IS_REAL_LOCATION` smallint(6) DEFAULT NULL,
  `F_TERMINAL_ID` bigint(20) DEFAULT NULL,
  `F_ENTERPRISE_CODE` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL,
  `F_GPSENCRYPT` smallint(6) NOT NULL DEFAULT '0',
  `F_GPSSTATE` bigint(20) NOT NULL DEFAULT '0',
  `F_ISPASSUP` smallint(6) NOT NULL DEFAULT '0',
  `F_ALARM_DATA1` bigint(20) DEFAULT NULL,
  `F_AREASN` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `F_GEO` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`F_ID`),
  KEY `IDX_GPSTIME_VEHICLEID_20170507` (`F_VEHICLE_ID`,`F_GPS_TIME`) USING BTREE,
  KEY `F_GPS_TIME_20170507` (`F_GPS_TIME`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='T_LOCATION_20170507';
