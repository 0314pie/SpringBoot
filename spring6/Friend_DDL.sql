/*  Spring 6 */

DROP TABLE friend;
DROP SEQUENCE friend_seq;

CREATE TABLE friend
(
    friend_seq  NUMBER PRIMARY KEY
    ,fname      VARCHAR2(30) NOT NULL
    ,age        NUMBER(3) DEFAULT 1
    ,phone      VARCHAR2(20) UNIQUE
    ,birthday   DATE DEFAULT sysdate
    ,active     CHAR(1) DEFAULT '1'
);

CREATE SEQUENCE friend_seq;
SELECT * FROM friend;