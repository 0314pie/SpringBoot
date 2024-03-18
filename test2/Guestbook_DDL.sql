
DROP TABLE guestbook;
DROP SEQUENCE guestbook_seq;

CREATE TABLE guestbook
(
    guest_seq   NUMBER PRIMARY KEY,
    guest_name  VARCHAR2(50) NOT NULL,
    guest_pwd   VARCHAR2(20) NOT NULL,
    guest_text  VARCHAR2(2000),
    regdate DATE DEFAULT SYSDATE
);

CREATE SEQUENCE guestbook_seq;