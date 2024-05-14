
create or replace package account_pack
is
    procedure get_user_name_by_user_id(
    p_user_id IN account.user_id%TYPE,
    p_user_name OUT account.user_name%TYPE
    );
end;
/

create or replace package body account_pack
is
    
     -- �Է¹��� id�� account ���̺� �����ϸ� �̸� ��ȯ, ������ null ��ȯ
    procedure get_user_name_by_user_id(
        p_user_id IN account.user_id%TYPE,
        p_user_name OUT account.user_name%TYPE
    )
    is
    begin
        select user_name into p_user_name
        from account
        where user_id = p_user_id;
        
        -- ��ȸ�� ����� ���� ���
        exception
            when no_data_found then
                p_user_name := null;
    end;
    
end;
/
    
create or replace package project_pack
is
	procedure get_projets_by_user_id(
    p_user_id in account.user_id%type,
    p_cursor out sys_refcursor);
    
    procedure insert_project(
    p_project_name in project.project_name
    %type,
    p_project_id out project.project_id%type
    );
    
  
end;
/

create or replace package body project_pack
is
    -- login�� ������ project ��� ��������
    procedure get_projets_by_user_id(
    p_user_id in account.user_id%type,
    p_cursor out sys_refcursor)
    is
    begin
        open p_cursor for
            select project_id, project_name
            from project
            where project_id in (select project_project_id
                                from project_member
                                where account_user_id = p_user_id);
    end;
    
     -- ���ο� ������Ʈ ���� - project table insert
    procedure insert_project(
    p_project_name in project.project_name%type, 
    p_project_id out project.project_id%type)
    is
    begin
        insert into project(project_name) values (p_project_name) RETURNING project_id INTO p_project_id;
        commit;
    end;
    
end;
/

set serveroutput on;

declare
    v_cursor sys_refcursor;
    v_project_name project.project_name%type;
begin
    project_pack.get_projet_name_by_user_id('admin', v_cursor);
    loop
	  fetch v_cursor into v_project_name;
	  exit when v_cursor%notfound;
	  dbms_output.put_line(v_project_name);
	end loop;
end;
/

declare
    v_user_id account.user_id%type := 'admin'; 
    v_user_name account.user_name%type; 
    v_message varchar2(20); 
begin
    project_pack.get_user_name(p_user_id = v_user_id,
                  p_user_name = v_user_name,
                  p_message = v_message);
    
    if v_message is null then
        dbms_output.put_line('����� �̸� ' ||  v_user_name);
    else
        dbms_output.put_line(v_message);
    end if;
end;
/

create or replace package project_member_pack
is
    procedure insert_project_member(
    p_user_id account.user_id%TYPE,
    p_leader project_member.leader%type,
    p_project_id project.project_id%type
    );
    
    procedure get_project_members_by_project_id(
    p_project_id in project.project_id%type,
    p_cursur out sys_refcursor);
    
    procedure check_project_member_in_project(
    p_project_id in project_member.project_project_id%type,
    p_account_user_id in project_member.account_user_id%type,
    p_exist out number
    );
    
    procedure insert_project_member (
    p_project_id in project_member.project_project_id%type,
    p_user_id in project_member.account_user_id%type
    );
end;
/

create or replace package body project_member_pack
is 
    -- ���ο� ������Ʈ ���� - project_member table insert 
    procedure insert_project_member(
    p_user_id account.user_id%TYPE,
    p_leader project_member.leader%type,
    p_project_id project.project_id%type
    )
    is
    begin
        insert into project_member(leader, account_user_id, project_project_id) values (p_leader, p_user_id, p_project_id);
        commit;
    end;
    
    procedure get_project_members_by_project_id(
    p_project_id in project.project_id%type,
    p_cursur out sys_refcursor)
    is
    begin
        open p_cursur for
            select member_id, leader, account_user_id
            from project_member
            where project_project_id = p_project_id;
    end;
    
    procedure check_project_member_in_project(
    p_project_id in project_member.project_project_id%type,
    p_account_user_id in project_member.account_user_id%type,
    p_exist out number
    )
    is
        v_count number;
    begin
        -- ������Ʈ�� �ش��ϴ� ����� �ִ��� Ȯ��
        select count(*)
        into v_count
        from project_member
        where project_project_id = p_project_id
        and account_user_id = p_account_user_id;
    
        if v_count > 0 then
            p_exist := 1;
        else
            p_exist := 0;
        end if;
    end;
    
    procedure insert_project_member (
    p_project_id in project_member.project_project_id%type,
    p_user_id in project_member.account_user_id%type
    )
    is
    begin
        insert into project_member (leader, account_user_id, project_project_id) values (0, p_user_id, p_project_id);
        commit;
    end;
    
end;
/
create or replace package task_pack
is
    procedure get_tasks_by_project_id(
    p_project_id in project.project_id%type,
    p_cursor out sys_refcursor);
    
    procedure get_task_count_by_member_id(
    p_member_id in task.project_member_member_id%type, 
    p_task_count out number);
    
    procedure get_complete_task_count_by_member_id(
    p_member_id in task.project_member_member_id%type, 
    p_completion_count out number);
    
    procedure get_total_task_count_by_project_id(
    p_project_id in task.project_project_id%type,
    p_task_count out number);
    
    procedure get_total_complete_task_count_by_project_id(
    p_project_id in task.project_project_id%type,
    p_completion_count out number);
end;
/

create or replace package body task_pack
is
    procedure get_tasks_by_project_id(
    p_project_id in project.project_id%type,
    p_cursor out sys_refcursor)
    is
    begin
        open p_cursor for 
            select *
            from member_task_view
            where project_project_id = p_project_id;
    end;
    
    procedure get_task_count_by_member_id(
    p_member_id in task.project_member_member_id%type, 
    p_task_count out number)
    is
    begin
        select count(*) into p_task_count
        from task
        where project_member_member_id = p_member_id;
    end;
    
    procedure get_complete_task_count_by_member_id(
    p_member_id in task.project_member_member_id%type, 
    p_completion_count out number)
    is
    begin        
        select count(*) into p_completion_count
        from task
        where project_member_member_id = p_member_id and completion = 1;
    end;
    
    procedure get_total_task_count_by_project_id(
    p_project_id in task.project_project_id%type,
    p_task_count out number)
    is
     begin        
        select count(*) into p_task_count
        from task
        where project_project_id = p_project_id;
    end;
    
    procedure get_total_complete_task_count_by_project_id(
    p_project_id in task.project_project_id%type,
    p_completion_count out number)
    is
     begin        
        select count(*) into p_completion_count
        from task
        where project_project_id = p_project_id and completion = 1;
    end;
end;
/

-- ���� ���ν���
-- ��Ű�� ���� �ۼ�
CREATE OR REPLACE PACKAGE account_management_pkg AS
    PROCEDURE login_proc(
        p_username IN VARCHAR2,
        p_password IN VARCHAR2,
        p_result OUT NUMBER
    );
    PROCEDURE insert_user_info(
        p_user_id IN VARCHAR2,
        p_user_name IN VARCHAR2,
        p_email IN VARCHAR2,
        p_password IN VARCHAR2
    );
    PROCEDURE register_member(
        p_username IN VARCHAR2,
        p_password IN VARCHAR2,
        p_email IN VARCHAR2,
        p_name IN VARCHAR2,
        p_result OUT NUMBER
    );
END account_management_pkg;
/

-- ��Ű�� ���� �ۼ�
CREATE OR REPLACE PACKAGE BODY account_management_pkg AS
    PROCEDURE login_proc(
        p_username IN VARCHAR2,
        p_password IN VARCHAR2,
        p_result OUT NUMBER
    ) AS
        v_count NUMBER;
    BEGIN
        -- �Էµ� ���̵�� ��й�ȣ�� ����� ���� ��ȸ
        SELECT COUNT(*) INTO v_count
        FROM user11.account
        WHERE user_id = p_username AND password = p_password;

        -- ��ȸ�� ����� ���� ����� ����
        IF v_count > 0 THEN
            p_result := 1; -- �α��� ����
        ELSE
            p_result := 0; -- �α��� ����
        END IF;
    END login_proc;

    PROCEDURE insert_user_info(
        p_user_id IN VARCHAR2,
        p_user_name IN VARCHAR2,
        p_email IN VARCHAR2,
        p_password IN VARCHAR2
    ) AS
    BEGIN
        INSERT INTO user11.account (user_id, user_name, email, password)
        VALUES (p_user_id, p_user_name, p_email, p_password);
    END insert_user_info;

    PROCEDURE register_member(
        p_username IN VARCHAR2,
        p_password IN VARCHAR2,
        p_email IN VARCHAR2,
        p_name IN VARCHAR2,
        p_result OUT NUMBER
    ) AS
        v_count NUMBER;
    BEGIN
        -- ���̵� �ߺ� Ȯ��
        SELECT COUNT(*)
        INTO v_count
        FROM user11.account
        WHERE user_id = p_username;

        IF v_count > 0 THEN
            -- �ߺ��� ���̵� ���� ���
            p_result := 0;
        ELSE
            -- �ߺ��� ���̵� ���� ���, ȸ������ ����
            INSERT INTO user11.account (user_id, user_name, email, password)
            VALUES (p_username, p_name, p_email, p_password);
            p_result := 1;
        END IF;
    END register_member;
END account_management_pkg;
/

CREATE OR REPLACE PACKAGE MEMBER_PAGE_PKG AS 

  -- �۾� �Ϸ� ���� ������Ʈ ���ν���
  PROCEDURE update_task_completion (
    p_task_id NUMBER,
    p_completion NUMBER
  );
  
  -- ȸ���� �۾� ��ȸ ���ν���
  PROCEDURE fetch_member_tasks (
    p_member_id IN NUMBER,
    p_result OUT SYS_REFCURSOR
  );
  
  -- �۾� �߰� ���ν���
  PROCEDURE add_task (
    p_task_name VARCHAR2,
    p_priority VARCHAR2,
    p_member_id NUMBER,
    p_project_id1 NUMBER
  );

END MEMBER_PAGE_PKG;
/

CREATE OR REPLACE PACKAGE BODY MEMBER_PAGE_PKG AS 

  -- �۾� �Ϸ� ���� ������Ʈ ���ν���
  PROCEDURE update_task_completion (
    p_task_id NUMBER,
    p_completion NUMBER
  ) AS
  BEGIN
    UPDATE user11.task SET completion = p_completion WHERE task_id = p_task_id;
    COMMIT;
  END update_task_completion;
  
  -- ȸ���� �۾� ��ȸ ���ν���
  PROCEDURE fetch_member_tasks (
    p_member_id IN NUMBER,
    p_result OUT SYS_REFCURSOR
  ) AS
  BEGIN
    OPEN p_result FOR
    SELECT task_id, task_name, priority, completion, project_project_id, project_member_member_id, feedback_total_score, retry
    FROM user11.member_task_view
    WHERE project_member_member_id = p_member_id;
  END fetch_member_tasks;
  
  -- �۾� �߰� ���ν���
  PROCEDURE add_task (
    p_task_name VARCHAR2,
    p_priority VARCHAR2,
    p_member_id NUMBER,
    p_project_id1 NUMBER
  ) AS
  BEGIN
    -- task ���̺� ������ ����
    INSERT INTO user11.task (task_name, priority, completion, project_project_id, project_member_member_id, member_id, project_id1)
    VALUES (p_task_name, p_priority, 0, p_project_id1,  p_member_id, p_member_id, p_project_id1);
    
    COMMIT;
  EXCEPTION
    WHEN DUP_VAL_ON_INDEX THEN -- �۾��� �̹� �����ϴ� ��� ó��
      DBMS_OUTPUT.PUT_LINE('Error: Duplicate task');
  END add_task;

END MEMBER_PAGE_PKG;
/

create or replace package task_page_pkg as
    PROCEDURE add_feedback (
        p_advice_name VARCHAR2,
        p_advice_score NUMBER,
        p_task_id NUMBER,
        p_project_member_member_id NUMBER,
        p_member_id NUMBER,
        p_project_id NUMBER
    );
    
    PROCEDURE DELETE_FEEDBACK (
        p_task_id IN NUMBER,
        p_member_id IN NUMBER,
        p_project_id IN NUMBER
    );
    
    PROCEDURE GET_FEEDBACK (
        p_task_id IN NUMBER,
        p_member_id IN NUMBER,
        p_project_id IN NUMBER,
        p_advice_name OUT VARCHAR2
    );
    
    PROCEDURE UPDATE_FEEDBACK (
        p_task_id IN NUMBER,
        p_member_id IN NUMBER,
        p_project_id IN NUMBER,
        p_advice_name IN VARCHAR2,
        p_advice_score IN NUMBER
    );
    
    procedure update_feedback_total_score(
    p_task_id number,
    p_advice_score number
    );
    
    procedure update_task_retry(
    p_task_id task.task_id%type,
    p_feedback_total_score task.feedback_total_score%type
    );
end;
/
create or replace package body task_page_pkg as
    PROCEDURE add_feedback (
    p_advice_name VARCHAR2,
    p_advice_score NUMBER,
    p_task_id NUMBER,
    p_project_member_member_id NUMBER,
    p_member_id NUMBER,
    p_project_id NUMBER
    )
    AS
    BEGIN
        INSERT INTO user11.feedback (advice_name, advice_score, task_task_id, project_member_member_id, member_id, project_id)
        VALUES (p_advice_name, p_advice_score, p_task_id, p_project_member_member_id, p_member_id, p_project_id);
        
        COMMIT;
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('Error occurred while adding feedback: ' || SQLERRM);
    END add_feedback;
    
    PROCEDURE DELETE_FEEDBACK (
    p_task_id IN NUMBER,
    p_member_id IN NUMBER,
    p_project_id IN NUMBER
    )
    AS
    BEGIN
        DELETE FROM user11.feedback
        WHERE task_task_id = p_task_id
        AND member_id = p_member_id
        AND project_id = p_project_id;
    END DELETE_FEEDBACK;
    
    PROCEDURE GET_FEEDBACK (
    p_task_id IN NUMBER,
    p_member_id IN NUMBER,
    p_project_id IN NUMBER,
    p_advice_name OUT VARCHAR2
    )
    AS
    BEGIN
        SELECT advice_name INTO p_advice_name
        FROM user11.feedback
        WHERE task_task_id = p_task_id
        AND member_id = p_member_id
        AND project_id = p_project_id;
    END GET_FEEDBACK;
    
    PROCEDURE UPDATE_FEEDBACK (
    p_task_id IN NUMBER,
    p_member_id IN NUMBER,
    p_project_id IN NUMBER,
    p_advice_name IN VARCHAR2,
    p_advice_score IN NUMBER
    )
    AS
    BEGIN
        UPDATE user11.feedback
        SET advice_name = p_advice_name,
            advice_score = p_advice_score
        WHERE task_task_id = p_task_id
            AND member_id = p_member_id
            AND project_id = p_project_id;
        COMMIT;
    END UPDATE_FEEDBACK;
    
    PROCEDURE update_feedback_total_score(
    p_task_id number,
    p_advice_score number
    )
    IS
        v_result NUMBER;
    BEGIN
        v_result := calculate_average_advice_score(p_task_id, p_advice_score);
        
        DBMS_OUTPUT.PUT_LINE('�Լ� �� :' || v_result);
        
        UPDATE user11.task
        SET feedback_total_score = v_result
        WHERE task_id = p_task_id;
        COMMIT;
    END;
    
    procedure update_task_retry(
    p_task_id task.task_id%type,
    p_feedback_total_score task.feedback_total_score%type
    )
    is
    begin
        IF p_feedback_total_score < 60 THEN
            UPDATE task
            SET retry = 1
            WHERE task_id = p_task_id;
        ELSIF p_feedback_total_score >= 60 THEN
            UPDATE task
            SET retry = 0
            WHERE task_id = p_task_id;
        END IF;
    end;
end;
/

