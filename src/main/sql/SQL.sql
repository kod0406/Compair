-- userTable 생성
CREATE TABLE userTable (
    user_id VARCHAR2(100) PRIMARY KEY,
    user_mail VARCHAR2(100),
    password VARCHAR2(100) NOT NULL,
    user_name VARCHAR2(100),
    APPROVED NUMBER(1) DEFAULT 0
);

-- serverTable 생성
CREATE TABLE serverTable (
    server_code NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id VARCHAR2(100),
    sever_name VARCHAR2(100) NOT NULL,
    CONSTRAINT fk_user_id1 FOREIGN KEY (user_id) REFERENCES userTable (user_id)
);

-- boardTable 생성
CREATE TABLE boardTable (
    server_code NUMBER,    
    board_code NUMBER GENERATED ALWAYS AS IDENTITY,  -- 자동 증가 컬럼
    title VARCHAR2(255) NOT NULL,       
    author VARCHAR2(100) NOT NULL,     
    post_date DATE DEFAULT SYSDATE,     
    content LONG,                      
    attachment VARCHAR2(4000),   
    PRIMARY KEY (server_code, board_code),
    CONSTRAINT fk_server_code FOREIGN KEY (server_code) REFERENCES serverTable (server_code)
);

-- todoList 생성
CREATE TABLE todoList (
    todo_code NUMBER GENERATED ALWAYS AS IDENTITY, -- 자동 증가 컬럼
    server_code NUMBER,
    todo_title VARCHAR2(100),
    post_date DATE DEFAULT SYSDATE,
    tag VARCHAR2(1000),
    todo_check NUMBER(1),
    todo_writer VARCHAR(100),
    PRIMARY KEY (todo_code, server_code),
    CONSTRAINT fk_todo_server_code FOREIGN KEY (server_code) REFERENCES serverTable (server_code)
);

-- todoContent 생성
CREATE TABLE todoContent (
    todo_code NUMBER,
    server_code NUMBER,
    todo_content LONG,
    attachment VARCHAR2(4000),
    PRIMARY KEY (todo_code, server_code),
    CONSTRAINT fk_todo_content_title_code FOREIGN KEY (todo_code, server_code) REFERENCES todoList (todo_code, server_code)
);

-- mail 생성
CREATE TABLE mail (
    mail_code NUMBER GENERATED ALWAYS AS IDENTITY, -- 자동 증가 컬럼
    server_code NUMBER,
    writer VARCHAR2(100),
    post_date DATE DEFAULT SYSDATE,
    mail_title VARCHAR2(100),
    receiver VARCHAR2(100),
    PRIMARY KEY (mail_code, server_code),
    CONSTRAINT fk_mail_code FOREIGN KEY (server_code) REFERENCES serverTable(server_code)
);

-- mailContent 생성
CREATE TABLE mailContent (
    mail_code NUMBER,
    server_code NUMBER,
    mail_title VARCHAR2(100),
    todo_content LONG,
    attachment VARCHAR2(4000),
    PRIMARY KEY (mail_code, server_code),
    CONSTRAINT fk_mail_content_title_code FOREIGN KEY (mail_code, server_code) REFERENCES mail (mail_code, server_code)
);


--Mail의 삭제여부를 확인하는 테이블
CREATE TABLE mail_deletion (
    mail_code NUMBER,
    server_code NUMBER,
    user_id VARCHAR2(100),
    deleted_at DATE DEFAULT SYSDATE,
    PRIMARY KEY (mail_code, server_code, user_id),
    FOREIGN KEY (mail_code, server_code) REFERENCES mail(mail_code, server_code)
);
