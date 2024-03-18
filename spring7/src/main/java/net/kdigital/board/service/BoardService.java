package net.kdigital.board.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.kdigital.board.dto.BoardDTO;
import net.kdigital.board.entity.BoardEntity;
import net.kdigital.board.repository.BoardRepository;
import net.kdigital.board.util.FileService;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardService {
	private final BoardRepository boardRepository;
	
	@Value("${user.board.pageLimit}")
	int pageLimit;

	// 업로드된 파일이 저장될 디렉토리 경로를 읽어옴
	@Value("${spring.servlet.multipart.location}")
	String uploadPath;
	

	public Page<BoardDTO> selectAll(Pageable pageable, String searchItem, String searchWord) {
		// 사용자가 요청한 페이지 번호(default=1)
		// -1을 한 이유: page 위치의 값은 0부터 시작함
		// 사용자가 1페이지를 요청하면 DB에서는 0페이지를 가져와야 함
		int page = pageable.getPageNumber() -1;
		
		// Java Reflection 기능을 이용할 수도 있다.
		// List<BoardEntity> entityList = null;
		Page<BoardEntity> entityList = null;
		
		switch(searchItem) {
		case "boardTitle":
			entityList = boardRepository.findByBoardTitleContaining(
					searchWord, 
					PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "boardNum")));
			break;
		case "boardWriter":
			entityList = boardRepository.findByBoardWriterContaining(
					searchWord, 
					PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "boardNum")));
			break;
		case "boardContent":
			entityList = boardRepository.findByBoardContentContaining(
					searchWord, 
					PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "boardNum")));
			break;
		}
		
		/*
		System.out.println("글 내용(getContent) : " + entityList.getContent());
		System.out.println("글 개수(getTotalElements) : " + entityList.getTotalElements());
		System.out.println("요청한 페이지번호(getNumber) : " + entityList.getNumber());
		System.out.println("총 페이지수(getTotalPages) : " + entityList.getTotalPages());
		System.out.println("한 페이지에 보여지는 글 개수(getSize==pageLimit) : " + entityList.getSize());
		System.out.println("이전 페이지(hasPrevious) : " + entityList.hasPrevious());
		System.out.println("다음 페이지(hasNext) : " + entityList.hasNext());
		System.out.println("첫번째 페이지(isFirst) : " + entityList.isFirst());
		System.out.println("마지막 페이지(isLast) : " + entityList.isLast());
		*/
		
		
		// List<BoardEntity> entityList = boardRepository.findAll(Sort.by(Sort.Direction.DESC, "createDate"));

		Page<BoardDTO> dtoList = null;   // DTO 생성자 추가

		// entity를 dto로 변환하여 List에 담는 작업
		// entityList.forEach((entity) -> dtoList.add(BoardDTO.toDTO(entity)));  
		
		dtoList = entityList.map(board -> 
			new BoardDTO(
					board.getBoardNum(),
					board.getBoardWriter(),
					board.getBoardTitle(),
					board.getHitCount(),
					board.getCreateDate(),
					board.getOriginalFileName()
					)
				);

		return dtoList;
	}

	/**
	 * DB에 게시글 저장
	 * @param boardDTO
	 */
	public void insertBoard(BoardDTO boardDTO) {
		boardDTO.setBoardWriter("귀요미");

		log.info("저장경로: {}", uploadPath);

		String originalFileName = null;
		String savedFileName = null;

		// 첨부파일이 있으면 파일명 세팅 실시
		if(!boardDTO.getUploadFile().isEmpty()) {
			savedFileName = FileService.saveFile(boardDTO.getUploadFile(), uploadPath);
			originalFileName = boardDTO.getUploadFile().getOriginalFilename();

			boardDTO.setOriginalFileName(originalFileName);
			boardDTO.setSavedFileName(savedFileName);

		}

		BoardEntity boardEntity = BoardEntity.toEntity(boardDTO);

		// DB에 저장
		boardRepository.save(boardEntity);
	}

	// DB에서 boardNum에 해당하는 글을 조회
	public BoardDTO selectOne(Long boardNum) {
		Optional<BoardEntity> entity = boardRepository.findById(boardNum);

		if(entity.isPresent()) {
			BoardEntity boardEntity = entity.get();
			BoardDTO dto = BoardDTO.toDTO(boardEntity);
			return dto;
		}

		return null;
	}

	/**
	 * DB에서 boardNum에 해당하는 글을 삭제
	 * @param boardNum
	 */
	@Transactional
	public void deleteOne(Long boardNum) {
		// 글번호에 해당하는 글을 읽어옴 --> savedfilename을 알아야 하므로!

		Optional<BoardEntity> entity = boardRepository.findById(boardNum);

		if(entity.isPresent()) {
			BoardEntity board = entity.get();

			String savedFileName = board.getSavedFileName();
			log.info("{}", savedFileName);

			if(savedFileName != null) {
				String fullPath = uploadPath + "/" + savedFileName;
				FileService.deleteFile(fullPath);
			}
			boardRepository.deleteById(boardNum);

		}
	}

	/**
	 * DB에 데이터 수정 작업 처리
	 * @param boardDTO
	 */
	@Transactional
	public void updateOne(BoardDTO boardDTO) {
		MultipartFile uploadFile = boardDTO.getUploadFile(); // 첨부파일
		String originalFileName = null;		// 새로운 첨부파일이 있을 때(원래이름)
		String savedFileName = null;		// 새로운 첨부파일이 있을 때(조작된 이름)
		String oldSavedFileName = null;		// 기존 업로드된 파일이 있을 때

		// 업로드한 파일이 있는 경우
		if(!uploadFile.isEmpty()) {
			originalFileName = uploadFile.getOriginalFilename();
			savedFileName	= FileService.saveFile(uploadFile, uploadPath);
		}

		// DB에서 데이터를 가져옴
		Optional<BoardEntity> entity = boardRepository.findById(boardDTO.getBoardNum());

		if(entity.isPresent()) {
			BoardEntity boardEntity = entity.get();
			oldSavedFileName = boardEntity.getSavedFileName();

			// 기존파일이 있고, 업로드한 파일도 있다면
			if(oldSavedFileName != null &&  !uploadFile.isEmpty()) {
				// 예전 파일은 저장장치에서 삭제
				String fullPath = uploadPath + "/" + oldSavedFileName; 
				FileService.deleteFile(fullPath);

				boardEntity.setOriginalFileName(originalFileName);
				boardEntity.setSavedFileName(savedFileName);		
			}
			// 기존파일은 없고, 업로드된 파일이 있을 경우
			else if(oldSavedFileName == null &&  !uploadFile.isEmpty()) {
				// 파일삭제는 못함
				boardEntity.setOriginalFileName(originalFileName);
				boardEntity.setSavedFileName(savedFileName);	
			}

			// 첨부파일이 아예 없는 경우  
			// 파일처리는 하면 안됨 ==> 기존 DB는 건드리면 안됨
			// 파일명이 모두 null인 상태로 넘어옴 : 
			boardEntity.setBoardTitle(boardDTO.getBoardTitle());
			boardEntity.setBoardContent(boardDTO.getBoardContent());
			boardEntity.setUpdateDate(LocalDateTime.now());
		}



	}
	@Transactional
	public void incrementHitcount(Long boardNum) {
		Optional<BoardEntity> entity = boardRepository.findById(boardNum);
		
		if(entity.isPresent()) {
			BoardEntity boardEntity = entity.get();
			boardEntity.setHitCount(boardEntity.getHitCount() + 1);
		}
			
	}

}
