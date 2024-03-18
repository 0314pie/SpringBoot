-- 2024년 3월 12일
-- 회원전용 게시판 테이블

-- 객체 삭제 명령
DROP TABLE reply;
DROP TABLE board;
DROP TABLE members;

DROP SEQUENCE reply;
DROP SEQUENCE board_seq;
DROP SEQUENCE members;

-- 1)회원 테이블





-- 2) 게시판 테이블
CREATE TABLE board
(
    board_num          NUMBER constraint board_seq PRIMARY KEY
    , board_writer      VARCHAR2(20) constraint board_writer NOT NULL
    , board_title      VARCHAR2(200) DEFAULT '제목없음'
    , board_content     VARCHAR2(4000) 
    , hit_count         NUMBER DEFAULT 0
    , favorite_count    NUMBER DEFAULT 0
    , create_date       DATE DEFAULT sysdate
    , update_date       DATE

);

CREATE SEQUENCE board_seq;

-- 첨부파일을 위한 컬럼 추가
ALTER TABLE board ADD originalFileName VARCHAR2(200);
ALTER TABLE board ADD savedFileName VARCHAR2(200);

SELECT * FROM board;

UPDATE board
SET
create_date = sysdate
where
board_num = 1;
commit;

-- 3) 댓글 테이블




