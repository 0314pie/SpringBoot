2024년 3월 8일

[실습] 방명록 작성하기(Guestbook)

- 주요기능
1. 방명록 글 쓰기
2. 방명록 보기
3. 방명록 삭제

- 주요 Request
	/			첫요청		(GET) - MainController
	/insert		화면요청	(GET) -	GuestbookController (데이터 입력을 위한 화면요청)
	/insert		저장		(POST)- GuestbookController (DB에 저장하기 위한 요청)
	/list		목록요청	(GET) - GuestbookController (전체 조회)
	/deleteOne	삭제요청	(GET) - GuestbookController (글 한개 삭제) 
								  	비밀번호를 입력받아 같을 경우에만 삭제 
								  	(자바스크립트의 prompt() 사용)













