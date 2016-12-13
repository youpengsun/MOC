# Set auto increment for table vendor & contract from 10000
#
alter table moc.VENDOR  auto_increment  = 10000; 
alter table moc.CONTRACT auto_increment = 10000;

insert ignore into moc.USER(username, first_name, password, salt, user_group) 
values("administrator", "administrator","9cOsHPCWCU0YSJnfo40srutIZjiF5EfmGTvOgC8ZKtc=", "111111", "admin");

insert ignore into moc.USER(username, first_name, password, salt, user_group) 
values("admin", "useradmin","9cOsHPCWCU0YSJnfo40srutIZjiF5EfmGTvOgC8ZKtc=", "111111", "admin");

insert ignore into moc.USER(username, first_name, password, salt, user_group) 
values("111111", "useradmin","9cOsHPCWCU0YSJnfo40srutIZjiF5EfmGTvOgC8ZKtc=", "111111", "admin");

insert ignore into moc.USER(username, first_name, password, salt, user_group) 
values("couponadmin", "Coupon Administrator","9cOsHPCWCU0YSJnfo40srutIZjiF5EfmGTvOgC8ZKtc=", "111111", "cpadmin");

insert ignore into moc.USER(username, first_name, password, salt, user_group) 
values("cpadmin", "Coupon Administrator","9cOsHPCWCU0YSJnfo40srutIZjiF5EfmGTvOgC8ZKtc=", "111111", "cpadmin");



INSERT ignore INTO `moc`.`DISTRICT` (`ID`, `DESCRIPTION`) VALUES ('HZ', '汇智商业中心');
INSERT ignore INTO `moc`.`DISTRICT` (`ID`, `DESCRIPTION`) VALUES ('ZJ', '张江镇');
INSERT ignore INTO `moc`.`DISTRICT` (`ID`, `DESCRIPTION`) VALUES ('CT', '长泰广场');
INSERT ignore INTO `moc`.`DISTRICT` (`ID`, `DESCRIPTION`) VALUES ('01', 'SAP Canteen');
INSERT ignore INTO `moc`.`DISTRICT` (`ID`, `DESCRIPTION`) VALUES ('02', 'SAP PVG06');
