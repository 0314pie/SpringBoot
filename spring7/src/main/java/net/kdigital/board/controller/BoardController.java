package net.kdigital.board.controller;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.kdigital.board.dto.BoardDTO;
import net.kdigital.board.service.BoardService;
import net.kdigital.board.util.PageNavigator;

@Controller
@Slf4j
@RequestMapping("/board")
public class BoardController {
	private BoardService boardService;
	
	// 생성자 초기화
	public BoardController(BoardService boardService) {
		this.boardService = boardService;
	}
	
	// 파일의 저장경로
	@Value("${spring.servlet.multipart.location}")
	String uploadPath;
	
	// 한 페이징 당 글의 개수
	@Value("${user.board.pageLimit}")
	int pageLimit;
	
	/**
	 * 글 목록 요청
	 * 1: index에서 넘어올 경우에는 searchItem과 searchWord가 없으므로 기본값 세팅
	 * 	  1 페이지를 요청한 것임
	 * 2: 목록상황에서 검색하여 넘어올 경우 searchItem과 searchWord가 있으므로 그 값 사용
	 * 	  1 페이지를 요청한 것임
	 * 3: 목록 하단에서 페이지를 선택할 경우 선택한 값을 사용
	 * @return
	 */
	@GetMapping("/boardList") 
	public String boardList(
			@PageableDefault(page=1) Pageable pageable   // 페이징을 해주는 객체, 요청한 페이지가 없으면 1로 세팅
			, @RequestParam(name="searchItem", defaultValue="boardTitle") String searchItem
			, @RequestParam(name="searchWord", defaultValue = "") String searchWord
			, Model model) {
		
		System.out.println("=================" + searchItem);
		System.out.println("=================" + searchWord);
		
		// List<BoardDTO> dtolist = boardService.selectAll(searchItem, searchWord);
		Page<BoardDTO> dtoList = boardService.selectAll(pageable, searchItem, searchWord);
		
		int totalPages =(int)dtoList.getTotalPages();
		int page = pageable.getPageNumber();	
		
		PageNavigator navi = new PageNavigator(pageLimit, page, totalPages);
		
		model.addAttribute("list", dtoList);
		model.addAttribute("searchItem", searchItem);
		model.addAttribute("searchWord", searchWord);
		model.addAttribute("navi", navi);
		
		return "board/boardList";
	}
	/**
	 * 글쓰기 화면 요청
	 * @return
	 */
			
	@GetMapping("/boardWrite")
	public String boardWrite() {
		log.info("글쓰기 화면 요청");
		
		return "board/boardWrite";
	}
	
	/**
	 * DB에 글 등록 요청
	 * @return
	 */
	@PostMapping("/boardWrite")
	public String boardWrite(
			@ModelAttribute BoardDTO boardDTO
			) {
		log.info("글 DB 저장 요청: {}", boardDTO.toString());
//		log.info("첨부파일명 : {}", boardDTO.getUploadFile().getOriginalFilename());
//		log.info("첨부파일크기 : {}", boardDTO.getUploadFile().getSize());
	
		boardService.insertBoard(boardDTO);
		
		return "redirect:/board/boardList";
	}
	/**
	 * boardNum에 해당하는 글 한개 조회
	 * @param boardNum
	 * @param model
	 * @return
	 */
	@GetMapping("/boardDetail")
	public String boardDetail(
			@RequestParam(name="searchItem", defaultValue="boardTitle") String searchItem
			, @RequestParam(name="searchWord", defaultValue = "") String searchWord
			, @RequestParam(name="boardNum") Long boardNum
			,Model model
			) {
		BoardDTO boardDTO = boardService.selectOne(boardNum);
		
		boardService.incrementHitcount(boardNum);
		
		model.addAttribute("board", boardDTO);
		model.addAttribute("searchItem", searchItem);
		model.addAttribute("searchWord", searchWord);
		
		return "board/boardDetail";
	}
	/**
	 * boardNum의 글을 삭제하기 위한 요청
	 * @param boardNum
	 * @return
	 */
	@GetMapping("/boardDelete")
	public String boardDelete(
			@RequestParam(name="boardNum") Long boardNum
			,@RequestParam(name="searchItem", defaultValue="boardTitle") String searchItem
			, @RequestParam(name="searchWord", defaultValue = "") String searchWord
			, RedirectAttributes rttr
			) {
	
		boardService.deleteOne(boardNum);
		rttr.addAttribute("searchItem", searchItem);
		rttr.addAttribute("searchWord", searchWord);
			
		return "redirect:/board/boardList";
	}
	
	/**
	 * boardNum의 글을 수정하기 위한 요청
	 * @param boardNum
	 * @return
	 */
	@GetMapping("/boardUpdate")
	public String boardUpdate(
			@RequestParam(name="boardNum") Long boardNum
			, @RequestParam(name="searchItem", defaultValue="boardTitle") String searchItem
			, @RequestParam(name="searchWord", defaultValue = "") String searchWord
			, Model model
			) {
	
		BoardDTO boardDTO = boardService.selectOne(boardNum);
		model.addAttribute("board", boardDTO);
		model.addAttribute("searchItem", searchItem);
		model.addAttribute("searchWord", searchWord);
		
		return "board/boardUpdate";
	}
	/**
	 * 파라미터로 전달받은 BoardDTO를 받아 서비스단으로 전달
	 * @param boardDTO
	 * @param : rttr redirect시 필요한 boaedNum을 담아서 넘김
	 * @return
	 */
	@PostMapping("/boardUpdate")
	public String boardUpdate(
			@ModelAttribute BoardDTO boardDTO
			, @RequestParam(name="searchItem", defaultValue="boardTitle") String searchItem
			, @RequestParam(name="searchWord", defaultValue = "") String searchWord
			, RedirectAttributes rttr) {
		
		log.info("=================={}", boardDTO.toString());
		
		boardService.updateOne(boardDTO);
		
		rttr.addAttribute("boardNum", boardDTO.getBoardNum());
		rttr.addAttribute("searchItem", searchItem);
		rttr.addAttribute("searchWord", searchWord);
		
		return "redirect:/board/boardDetail";
	}
	
	/**
	 * 전달받은 게시판 번호의 파일을 다운로드
	 * @return
	 */
	@GetMapping("/download")
	public String download(
			@RequestParam(name="boardNum") Long boardNum
			, HttpServletResponse response 
			) {
		BoardDTO boardDTO = boardService.selectOne(boardNum);
		String originalFileName = boardDTO.getOriginalFileName();
		String savedFileName = boardDTO.getSavedFileName();
		
		log.info("오리지날: {}", originalFileName);
		log.info("저장명: {}", savedFileName);
		log.info("디렉토리명: {}", uploadPath);
		
		// 다운로드 파일명 한글 인코딩
		try {
			String tempName = URLEncoder.encode(
					originalFileName,
					StandardCharsets.UTF_8.toString()
					);
			
			response.setHeader("Content-Disposition", "attachment;filename=" + tempName);
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String fullPath = uploadPath + "/" + savedFileName;   // 하드디스크의 경로
		
		// 스트림 설정(실제 다운로드)
		FileInputStream filein = null;
		ServletOutputStream fileout = null;
		
		try {
			filein = new FileInputStream(fullPath);		// 하드디스크에 있는 파일을 메모리로 올리는 작업
			fileout = response.getOutputStream();		// 웹에서 다른 원격지로 보내는 작업
			
			FileCopyUtils.copy(filein, fileout);		// 반복 .copy(원본, 복사본)
			
			fileout.close();
			filein.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
			
	
	
}
