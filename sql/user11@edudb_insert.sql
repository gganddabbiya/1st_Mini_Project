insert into account values('admin', '包府磊', 'admin@gmail.com', '1234');
insert into account values('admin1', '包府磊1', 'admin1@gmail.com', '1234');
insert into account values('admin2', '包府磊2', 'admin2@gmail.com', '1234');
insert into project(project_name) values ('test project1');
insert into project_member(leader, account_user_id, project_project_id) values (0, 'admin', 2);

insert into task(task_name, priority, completion, project_project_id, project_member_member_id, member_id, project_id1) values ('test task', 'High', 1, 2, 3, 3, 2);
insert into task(task_name, priority, completion, project_project_id, project_member_member_id, member_id, project_id1) values ('test task1', 'Low', 0, 2, 3, 3, 2);
insert into task(task_name, priority, completion, project_project_id, project_member_member_id, member_id, project_id1) values ('test task2', 'High', 1, 3, 5, 5, 3);
insert into task(task_name, priority, completion, project_project_id, project_member_member_id, member_id, project_id1) values ('test task3', 'Middle', 0, 3, 5, 5, 3);
insert into task(task_name, priority, completion, project_project_id, project_member_member_id, member_id, project_id1) values ('test task4', 'High', 1, 3, 4, 4, 3);
commit;