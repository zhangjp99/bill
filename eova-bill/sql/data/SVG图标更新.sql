
-- 将菜单图标更新为SVG图标, 其它的图标请自行配置

update eova_menu set iconskip = 'eova_Ballance' where code = 'eova';
update eova_menu set iconskip = 'eova_Apps' where code = 'eova_menu';
update eova_menu set iconskip = 'eova_Delivery_4' where code = 'eova_button';
update eova_menu set iconskip = 'eova_Database' where code = 'eova_object';
update eova_menu set iconskip = 'eova_Book' where code = 'eova_dict';
update eova_menu set iconskip = 'eova_Stopwatch' where code = 'eova_task';
update eova_menu set iconskip = 'eova_Package_Opened' where code = 'eova_code';
update eova_menu set iconskip = 'eova_Cog_Wheel' where code = 'sys';
update eova_menu set iconskip = 'eova_Keys' where code = 'sys_users';
update eova_menu set iconskip = 'eova_Shield_2' where code = 'sys_auth_role';
update eova_menu set iconskip = 'eova_To_Do' where code = 'sys_log';
update eova_menu set iconskip = 'eova_Desktop_2' where code = 'biz_demo';
update eova_menu set iconskip = 'eova_team' where code = 'biz_demo_users';
update eova_menu set iconskip = 'eova_Charts' where code = 'biz_caidan';
update eova_menu set iconskip = 'eova_Folder_2' where code = 'biz_4j';
update eova_menu set iconskip = 'eova_Like_UI' where code = 'biz_demo_views';
update eova_menu set iconskip = 'eova_Heart' where code = 'eova_druid';
update eova_menu set iconskip = 'eova_Folder_2' where code = 'menu3';
update eova_menu set iconskip = 'eova_Group_2' where code = 'eova_org';
update eova_menu set iconskip = 'eova_Online_Store_2' where code = 'eova_store';
update eova_menu set iconskip = 'eova_Database' where code = 'eova_ddl';
update eova_menu set iconskip = 'eova_Drawer' where code = 'eova_config';
update eova_menu set iconskip = 'eova_File_2' where code = 'eova_sms';

-- all success