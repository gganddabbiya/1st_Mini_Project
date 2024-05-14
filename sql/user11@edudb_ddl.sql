-- 생성자 Oracle SQL Developer Data Modeler 23.1.0.087.0806
--   위치:        2024-05-08 18:08:19 KST
--   사이트:      Oracle Database 12c
--   유형:      Oracle Database 12c



-- predefined type, no DDL - MDSYS.SDO_GEOMETRY

-- predefined type, no DDL - XMLTYPE

CREATE TABLE account (
    user_id   VARCHAR2(25) NOT NULL,
    user_name VARCHAR2(25) NOT NULL,
    email     VARCHAR2(50),
    password  VARCHAR2(10) NOT NULL
);

ALTER TABLE account ADD CONSTRAINT account_pk PRIMARY KEY ( user_id );

CREATE TABLE chat_message (
    message_id                        NUMBER generated always as identity NOT NULL,
    message_contents                  VARCHAR2(200),
    sending_time                      DATE,
    account_user_id                   VARCHAR2(25) NOT NULL,
    project_project_id                NUMBER NOT NULL, 
--  ERROR: Column name length exceeds maximum allowed length(30) 
    project_member_project_project_id NUMBER NOT NULL,
    project_member_account_user_id    VARCHAR2(25) NOT NULL
);

ALTER TABLE chat_message ADD CONSTRAINT chat_message_pk PRIMARY KEY ( message_id );

CREATE TABLE feedback (
    advice_id                NUMBER generated always as identity NOT NULL,
    advice_name              VARCHAR2(20),
    advice_score             NUMBER,
    task_task_id             NUMBER NOT NULL,
    project_member_member_id NUMBER NOT NULL,
    member_id                NUMBER NOT NULL,
    project_id               NUMBER NOT NULL
);

ALTER TABLE feedback ADD CONSTRAINT feedback_pk PRIMARY KEY ( advice_id );

ALTER TABLE feedback ADD CONSTRAINT feedback_member_project_id_un UNIQUE ( member_id,
                                                                              project_id );

CREATE TABLE project (
    project_id   NUMBER generated always as identity NOT NULL,
    project_name VARCHAR2(50) NOT NULL
);

ALTER TABLE project ADD CONSTRAINT project_pk PRIMARY KEY ( project_id );

CREATE TABLE project_member (
    member_id          NUMBER generated always as identity NOT NULL,
    leader             NUMBER,
    account_user_id    VARCHAR2(25) NOT NULL,
    project_project_id NUMBER NOT NULL
);

ALTER TABLE project_member ADD CONSTRAINT project_member_pk PRIMARY KEY ( member_id );

ALTER TABLE project_member ADD CONSTRAINT project_mem_project_user_id_un UNIQUE ( project_project_id,
                                                                                                        account_user_id );
                                                                                                        
CREATE TABLE task (
    task_id                  NUMBER generated always as identity NOT NULL,
    task_name                VARCHAR2(200) NOT NULL,
    priority                 VARCHAR2(10),
    completion               NUMBER,
    project_project_id       NUMBER NOT NULL,
    project_member_member_id NUMBER NOT NULL,
    member_id                NUMBER NOT NULL,
    project_id1              NUMBER NOT NULL
);

ALTER TABLE task ADD CONSTRAINT task_pk PRIMARY KEY ( task_id );

CREATE TABLE time (
    time_id      NUMBER generated always as identity NOT NULL,
    start_time   DATE,
    end_time     DATE,
    task_task_id NUMBER NOT NULL
);

ALTER TABLE time ADD CONSTRAINT time_pk PRIMARY KEY ( time_id );

ALTER TABLE chat_message
    ADD CONSTRAINT chat_message_account_fk FOREIGN KEY ( account_user_id )
        REFERENCES account ( user_id );

ALTER TABLE chat_message
    ADD CONSTRAINT chat_message_project_fk FOREIGN KEY ( project_project_id )
        REFERENCES project ( project_id );

ALTER TABLE chat_message
    ADD CONSTRAINT chat_message_project_member_fk FOREIGN KEY ( project_member_project_project_id,
                                                                project_member_account_user_id )
        REFERENCES project_member ( project_project_id,
                                    account_user_id );

ALTER TABLE feedback
    ADD CONSTRAINT feedback_project_member_fk FOREIGN KEY ( project_member_member_id )
        REFERENCES project_member ( member_id );

ALTER TABLE feedback
    ADD CONSTRAINT feedback_task_fk FOREIGN KEY ( task_task_id )
        REFERENCES task ( task_id );

ALTER TABLE project_member
    ADD CONSTRAINT project_member_account_fk FOREIGN KEY ( account_user_id )
        REFERENCES account ( user_id );

ALTER TABLE project_member
    ADD CONSTRAINT project_member_project_fk FOREIGN KEY ( project_project_id )
        REFERENCES project ( project_id );

ALTER TABLE task
    ADD CONSTRAINT task_project_fk FOREIGN KEY ( project_project_id )
        REFERENCES project ( project_id );

ALTER TABLE task
    ADD CONSTRAINT task_project_member_fk FOREIGN KEY ( project_member_member_id )
        REFERENCES project_member ( member_id );

ALTER TABLE time
    ADD CONSTRAINT time_task_fk FOREIGN KEY ( task_task_id )
        REFERENCES task ( task_id );



-- Oracle SQL Developer Data Modeler 요약 보고서: 
-- 
-- CREATE TABLE                             7
-- CREATE INDEX                             2
-- ALTER TABLE                             19
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           0
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          0
-- CREATE MATERIALIZED VIEW                 0
-- CREATE MATERIALIZED VIEW LOG             0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- TSDP POLICY                              0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   3
-- WARNINGS                                 0


GRANT ALL PRIVILEGES ON chat_message TO user04;
GRANT ALL PRIVILEGES ON feedback TO user04;
GRANT ALL PRIVILEGES ON project TO user04;
GRANT ALL PRIVILEGES ON project_member TO user04;
GRANT ALL PRIVILEGES ON task TO user04;
GRANT ALL PRIVILEGES ON time TO user04;
GRANT ALL PRIVILEGES ON account TO user04;

ALTER TABLE task
ADD(feedback_total_score NUMBER);

ALTER TABLE task
ADD(retry NUMBER);

ALTER TABLE feedback DROP CONSTRAINT FEEDBACK_MEMBER_ID_PROJECT_ID_UN;

ALTER TABLE feedback ADD CONSTRAINT feedback_task_member_project_id_un UNIQUE (task_task_id, member_id,
                                                                              project_id );
