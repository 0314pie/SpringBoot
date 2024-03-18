## 주요 업무
1) 클라이언트의 요청과 함께 전송된 파라미터를 받는 것.

2) 요청의 종류
/   		    GET 요청
/param/send     GET 요청과 함께 전송되는 데이터 (name, age)
			    a태그로!
/param/sendForm GET 요청과 함께 전송되는 데이터 (name, age)
				for태그로!
3) 서버에서 클라이언트로 응답할 때 데이터를 실어보내기 위해
- Model 객체를 요청
- model 객체의 addAttribute() 메소드를 이용해서 Key, Value의 쌍으로 넣고
- Forwarding 방식으로 응답

4) 클라이언트 측에서 데이터를 받을 때에는 Thymeleaf가 필요!
- <html xmlns:th="http://www.thymeleaf.org"> 설정 후
- 데이터가 표시될 위치에 [[ ${변수명} ]]
				