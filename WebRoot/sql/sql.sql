DROP TABLE IF EXISTS up7_files;
DROP TABLE IF EXISTS up7_folders;
DROP TABLE IF EXISTS down3_files;
DROP TABLE IF EXISTS down3_folders;
DROP PROCEDURE IF EXISTS f_process;
DROP PROCEDURE IF EXISTS fd_process;
DROP PROCEDURE IF EXISTS fd_files_add_batch;
DROP PROCEDURE IF EXISTS fd_files_check;
DROP PROCEDURE IF EXISTS fd_remove;
DROP PROCEDURE IF EXISTS fd_add_batch;

CREATE TABLE IF NOT EXISTS `up7_files` (
  `f_id` 				int(11) NOT NULL auto_increment,
  `f_pid` 				int(11) default '0',
  `f_pidRoot` 			int(11) default '0',
  `f_fdTask` 			tinyint(1) default '0',
  `f_fdID` 				int(11) default '0',
  `f_fdChild` 			tinyint(1) default '0',
  `f_uid` 				int(11) default '0',
  `f_nameLoc` 			varchar(255) default '',
  `f_nameSvr` 			varchar(255) default '',
  `f_pathLoc` 			varchar(255) default '',
  `f_pathSvr` 			varchar(255) default '',
  `f_pathRel` 			varchar(255) default '',
  `f_md5` 				varchar(40) default '',
  `f_lenLoc` 			bigint(19) default '0',
  `f_sizeLoc` 			varchar(10) default '0',
  `f_pos` 				bigint(19) default '0',
  `f_lenSvr` 			bigint(19) default '0',
  `f_perSvr` 			varchar(6) default '0%',
  `f_complete` 			tinyint(1) default '0',
  `f_time` 				timestamp NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `f_deleted` 			tinyint(1) default '0',
  `f_sign` 				varchar(32) default '',
  PRIMARY KEY  (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

/*--更新文件进度*/
CREATE PROCEDURE f_process(
in `posSvr` bigint(19),
in `lenSvr` bigint(19),
in `perSvr` varchar(6),
in `uidSvr` int,
in `fidSvr` int,
in `complete` tinyint(1))
update up7_files set f_pos=posSvr,f_lenSvr=lenSvr,f_perSvr=perSvr,f_complete=complete where f_uid=uidSvr and f_id=fidSvr;

/*更新文件夹进度*/
CREATE PROCEDURE fd_process(
in uidSvr int,
in fd_idSvr int,
in fd_lenSvr bigint(19),
in perSvr varchar(6))
update up7_files set f_lenSvr=fd_lenSvr,f_perSvr=perSvr where f_uid=uidSvr and f_id=fd_idSvr;

/*文件夹表*/
CREATE TABLE IF NOT EXISTS up7_folders (
  `fd_id` 				int(11) NOT NULL auto_increment,
  `fd_name` 			varchar(50) default '',
  `fd_pid` 				int(11) default '0',
  `fd_uid` 				int(11) default '0',
  `fd_length` 			bigint(19) default '0',
  `fd_size` 			varchar(50) default '0',
  `fd_pathLoc` 			varchar(255) default '',
  `fd_pathSvr` 			varchar(255) default '',
  `fd_folders` 			int(11) default '0',
  `fd_files` 			int(11) default '0',
  `fd_filesComplete` 	int(11) default '0',
  `fd_complete` 		tinyint(1) default '0',
  `fd_delete` 			tinyint(1) default '0',
  `fd_json` 			varchar(20000) default '',
  `timeUpload` 			timestamp NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `fd_pidRoot` 			int(11) default '0',
  `fd_pathRel` 			varchar(255) default '',
  PRIMARY KEY  (`fd_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

/*下载数据表*/
CREATE TABLE down3_files
(
 f_id      		int(11) NOT NULL auto_increment    
,f_uid        	int(11) 	DEFAULT '0' 
,f_mac        	varchar(50) DEFAULT  '' 
,f_nameLoc		varchar(255)DEFAULT ''
,f_pathLoc      varchar(255)DEFAULT '' 	
,f_fileUrl      varchar(255)DEFAULT '' 	
,f_perLoc    	varchar(6) 	DEFAULT '0' 
,f_lenLoc    	bigint(19) 	DEFAULT '0' 
,f_lenSvr		bigint(19) DEFAULT '0'
,f_sizeSvr      varchar(10) DEFAULT '0' 
,f_complete		tinyint(1)	DEFAULT '0'	
,f_pidRoot		int(11) 	DEFAULT '0'	
,f_fdTask		tinyint(1) 	DEFAULT '0'	
,PRIMARY KEY  (`f_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

/*下载文件夹表*/
CREATE TABLE down3_folders
(
   fd_id		int(11) 		NOT NULL auto_increment  		 /*--文件夹ID，自动编号*/
  ,fd_name  	varchar(50) 	DEFAULT ''   /*--文件夹名称。test*/
  ,fd_uid  		int(11) 		DEFAULT '0'  /*--用户ID */
  ,fd_mac  		varchar(50) 	DEFAULT ''   /*--用户电脑识别码*/
  ,fd_pathLoc	varchar(255) 	DEFAULT ''   /*--文件夹信息文件在本地路径。D:\\Soft\\test.cfg*/
  ,fd_complete  tinyint(1) 		DEFAULT '0'  /*--是否已经下载*/
  ,fd_id_old	varchar(512) 	DEFAULT ''   /*--对应表字段：xdb_folders.fd_id，用来获取文件夹JSON信息*/
  ,fd_percent	varchar(7) 		DEFAULT ''   /*--上传百分比。*/
  ,PRIMARY KEY  (`fd_id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 AUTO_INCREMENT=1 ;

/*文件夹初始化*/
DELIMITER $$
CREATE PROCEDURE fd_files_add_batch(
	in f_count int	/*文件总数，要单独增加一个文件夹*/
   ,in fd_count int	/*文件夹总数*/
)
BEGIN
	declare i int;
	/*使用临时表存ID*/
	create temporary table if not exists tb_ids 
         (  
           t_file tinyint(1),
           t_id int
         )engine=memory;
    truncate TABLE tb_ids;
	
	set i = 0;
	
	/*批量添加文件夹*/
	while(i<fd_count) do	
		insert into up7_folders(fd_pid) values(0);
		insert into tb_ids values(0,last_insert_id());	
		set i = i + 1;
	end while;
	
	/*批量添加文件*/
	set i = 0;
	while(i<f_count) do	
		insert into up7_files(f_pid) values(0);
		insert into tb_ids values(1,last_insert_id());	
		set i = i + 1;
	end while;
	
	select * from tb_ids;
end$$
DELIMITER ;

/*批量查询MD5*/
DELIMITER $$
CREATE PROCEDURE fd_files_check(
	in md5s longtext	/*md5列表:a,b,c,d。*/
   ,in md5_len int /*单个MD5长度*/
   ,in md5s_len	int /*md5字符串总长度*/
)
BEGIN
	/*拆分md5*/
	declare md5_item varchar(40);
	declare md5_cur int;
	declare split_pos int;/*当前分割符位置*/
	create temporary table if not exists t_md5 /*不存在则创建临时表  */
         (  
           md5 varchar(40) primary key
         )engine=memory;
    truncate TABLE t_md5;  /*使用前先清空临时表*/
	
	set md5_cur = 0;
	set split_pos = position("," in md5s);

	/*有多个md5*/
	if md5s_len > md5_len then	
		while md5_cur < md5s_len do
			set md5_item = substring(md5s,md5_cur+1,md5_len);
			insert into t_md5(md5) values(md5_item);
			set md5_cur = md5_cur + md5_len + 1;
		end while;	
	else/*只有一个md5*/
		insert into t_md5(md5) values(md5s);
	end if;

	/*查询数据库*/
	select *
	from (select * from up7_files where f_id in (select max(f_id) from up7_files group by f_md5))fs
	inner join t_md5 t
	on t.md5 = fs.f_md5 ;
end$$
DELIMITER ;
	
/*文件夹删除*/
DELIMITER $$
CREATE PROCEDURE fd_remove(
	 in id_file int
	,in id_folder int
	,in uid int
)
BEGIN
	update up7_files set f_deleted=1 where f_id=id_file and f_uid=uid;
	update up7_files set f_deleted=1 where f_pidRoot=id_folder and f_uid=uid;
	update up7_folders set fd_delete=1 where fd_id=id_folder and fd_uid=uid;
end$$
DELIMITER ;

/*下载文件夹初始化*/
DELIMITER $$
CREATE PROCEDURE fd_add_batch(
	in f_count int	/*文件总数，要单独增加一个文件夹*/
   ,in uid int
)
BEGIN
	declare i int;
	/*使用临时表存ID*/
	create temporary table if not exists tb_ids 
         (  
           t_id int primary key
         )engine=memory;
    truncate TABLE tb_ids;
	
	set i = 0;
	
	while(i<f_count) do	
		insert into down3_files(f_uid) values(uid);
		insert into tb_ids(t_id) values(last_insert_id());	
		set i = i + 1;
	end while;
	
	select * from tb_ids;
END$$
DELIMITER ;

select f_id from up7_files limit 0,1;