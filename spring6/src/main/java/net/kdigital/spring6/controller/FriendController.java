package net.kdigital.spring6.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kdigital.spring6.dto.FriendDTO;
import net.kdigital.spring6.service.FriendService;

@Controller
@Slf4j
@RequiredArgsConstructor  // final 키워드가 붙은 멤버를 초기화할 때 사용
public class FriendController {
	
	 private final FriendService service;
	
	 // 생성자 주입방식 (예전에는 @Autowired라는 애노테이션을 사용)
//	 public FriendController(FriendService service) {
//		 this.service = service;
//	 }
	 
	@GetMapping("/insert")
	public String insert(Model model) {
		model.addAttribute("friendDTO", new FriendDTO());
		
		return "insertForm";  // 입력화면으로 감
	}
	
	@PostMapping("/insert")
	public String insert(
			@Valid @ModelAttribute FriendDTO friendDTO, BindingResult bindingResult
			) {
		log.info("{}", friendDTO.toString());
		log.info("bindingResult {}", bindingResult);  // 오류확인
		
		if(bindingResult.hasErrors()) {
			log.info("친구정보 등록 실패(오류가 포함)");
			return "insertForm";   // 에러 포함된 상태로 입력화면으로 감
		}
		
		service.insertFriend(friendDTO);
		
		// 입력하는 화면으로 이동
		return "redirect:/";  // 클라이언트에게 /insert 요청을 다시하도록 함!
	}
	
	@GetMapping("/list")
	public String list(Model model) {
		
		List<FriendDTO> friendList = service.selectAll();
		
		model.addAttribute("list", friendList);
		
		return "list";
	}
	
	@GetMapping("/deleteOne")
	public String deleteOne(
			@RequestParam(name="friendSeq", defaultValue = "0") Long friendSeq) {
		
		log.info("삭제할 seq: {}", friendSeq);
		service.deleteOne(friendSeq);
		
		return "redirect:/list";
	}
	
	@GetMapping("/updateOne")
	public String updateOne(
			@RequestParam(name="friendSeq", defaultValue = "0") Long friendSeq, Model model) {
		
		FriendDTO friendDTO = service.selectOne(friendSeq);
		
		log.info("수정할 data: {}", friendDTO.toString());
		
		model.addAttribute("friendDTO", friendDTO);
		
		return "updateForm";
	}
	
	@PostMapping("/updateProc")
	public String updateProc(
			@Valid @ModelAttribute FriendDTO friendDTO, BindingResult bindingResult
			) {
		log.info("{}", friendDTO.toString());
		log.info("bindingResult {}", bindingResult); 
		
		if(bindingResult.hasErrors()) {
			log.info("친구정보 수정 실패(오류가 포함)");
			return "updateForm";   // 에러 포함된 상태로 수정화면으로 감
		}
		
		 service.updateProc(friendDTO);
		
		// 목록 화면으로 이동
		return "redirect:/list";  
	}
}
