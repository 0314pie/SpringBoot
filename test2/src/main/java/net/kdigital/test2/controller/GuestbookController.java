package net.kdigital.test2.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import net.kdigital.test2.dto.GuestBookDTO;
import net.kdigital.test2.service.GuestBookService;

@Controller
@Slf4j
public class GuestbookController {

	private GuestBookService service;

	public GuestbookController(GuestBookService service) {
		this.service = service;
	}

	@GetMapping("/insert")
	public String insert(Model model) {	
		model.addAttribute("guestBookDTO", new GuestBookDTO());
		return "insertForm";
	}

	@PostMapping("/insert")
	public String insert(
			@Valid @ModelAttribute GuestBookDTO guestBookDTO, BindingResult bindingResult) {
//		log.info("{}", guestBookDTO.toString());
		guestBookDTO.setRegdate(LocalDateTime.now());
		log.info("{}", guestBookDTO.toString());
		service.insertGuestbook(guestBookDTO);

		return "redirect:/";
	}


	@GetMapping("/list")
	public String list(Model model) {

		List<GuestBookDTO> guestList = service.selectAll();

		model.addAttribute("list", guestList);

		return "list";
	}

	@GetMapping("/deleteOne")
	public String deleteOne(
			@RequestParam(name="guestSeq", defaultValue = "0") Long guestSeq) {
		log.info("삭제할 seq: {}", guestSeq);
		service.deleteOne(guestSeq);

		return "redirect:/list";
	}
	
	@GetMapping("/updateOne")
	public String updateOne(
			@RequestParam(name="guestSeq", defaultValue = "0") Long guestSeq, Model model) {
		
		GuestBookDTO guestBookDTO = service.selectOne(guestSeq);
		
		log.info("수정할 data: {}", guestBookDTO.toString());
		
		model.addAttribute("guestBookDTO", guestBookDTO);
		
		return "updateForm";
	}
	
	@PostMapping("/updateProc")
	public String updateProc(
			@Valid @ModelAttribute GuestBookDTO guestBookDTO, BindingResult bindingResult
			) {
		log.info("{}", guestBookDTO.toString());
		log.info("bindingResult {}", bindingResult); 
		
		if(bindingResult.hasErrors()) {
			log.info("방명록 정보 수정 실패(오류가 포함)");
			return "updateForm";   // 에러 포함된 상태로 수정화면으로 감
		}
		
		 service.updateProc(guestBookDTO);
		
		// 목록 화면으로 이동
		return "redirect:/list";  
	}
	



}
