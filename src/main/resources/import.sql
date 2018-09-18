/*
SQLyog v10.2 
MySQL - 5.7.22-log 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

insert into `user` (`id`, `avatar`, `c_id`, `email`, `name`, `password`, `state`, `s_id`, `style`, `t_id`, `telephone`, `username`) values('1','613.jpg',NULL,'bbb@qq.com','Ö£½¨»ª','123456','2',NULL,'2','9','11111111111','bbb');
insert into `user` (`id`, `avatar`, `c_id`, `email`, `name`, `password`, `state`, `s_id`, `style`, `t_id`, `telephone`, `username`) values('2','612.jpg',NULL,'aaa@qq.com','aaaa','123456','2','5','1',NULL,'11111111111','aaa');
insert into `user` (`id`, `avatar`, `c_id`, `email`, `name`, `password`, `state`, `s_id`, `style`, `t_id`, `telephone`, `username`) values('3','611.jpg','1',NULL,'aaa','123456','2','4','1',NULL,'13923568989','ccc');
insert into `user` (`id`, `avatar`, `c_id`, `email`, `name`, `password`, `state`, `s_id`, `style`, `t_id`, `telephone`, `username`) values('4','611.jpg',NULL,NULL,'2011','123456','2','6','1',NULL,'13609068793','jiayuan');
insert into `user` (`id`, `avatar`, `c_id`, `email`, `name`, `password`, `state`, `s_id`, `style`, `t_id`, `telephone`, `username`) values('5','611.jpg',NULL,'admin@qq.com','2011','123456','1',NULL,'4',NULL,'13609068793','admin');

insert into `authority` (`id`, `name`) values('1','ROLE_ADMIN');
insert into `authority` (`id`, `name`) values('2','ROLE_TEACHER');
insert into `authority` (`id`, `name`) values('3','ROLE_USER');


insert into `user_authority` (`user_id`, `authority_id`) values('1','2');
insert into `user_authority` (`user_id`, `authority_id`) values('2','2');
insert into `user_authority` (`user_id`, `authority_id`) values('3','2');
insert into `user_authority` (`user_id`, `authority_id`) values('2','1');
insert into `user_authority` (`user_id`, `authority_id`) values('1','1');
insert into `user_authority` (`user_id`, `authority_id`) values('4','3');
insert into `user_authority` (`user_id`, `authority_id`) values('5','1');
