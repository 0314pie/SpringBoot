package net.kdigital.test2.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.kdigital.test2.dto.GuestBookDTO;
import net.kdigital.test2.entity.GuestBookEntity;
import net.kdigital.test2.repository.GuestBookRepository;

@Service
@Slf4j
public class GuestBookService {
	
	private GuestBookRepository guestbookRepository; 
	
	public GuestBookService(GuestBookRepository guestbookRepository) {
		this.guestbookRepository = guestbookRepository;
	}
	
	public void insertGuestbook(GuestBookDTO guestbookDTO) {
		log.info("service에 도착!");
		
		GuestBookEntity entity = GuestBookDTO.toEntity(guestbookDTO); 
		
		guestbookRepository.save(entity);
	}
	
	public List<GuestBookDTO> selectAll() {
		              
//		List<GuestBookEntity> list = guestbookRepository.findAll();
		
		List<GuestBookEntity> list = guestbookRepository.findAll(Sort.by(Sort.Direction.ASC,"gname"));
		List<GuestBookDTO> guestBookDTOList = new ArrayList<>();
		list.forEach((entity) -> guestBookDTOList.add(GuestBookEntity.toDTO(entity)));
		
		return guestBookDTOList;
	}
	
	public void deleteOne(Long guestSeq) {
		guestbookRepository.deleteById(guestSeq);
	}
	
	@SuppressWarnings("static-access")
	public GuestBookDTO selectOne(Long guestSeq) {
		Optional<GuestBookEntity> entity = guestbookRepository.findById(guestSeq);
		
		if(entity.isPresent()) {
			GuestBookEntity guestBookEntity = entity.get();
			return guestBookEntity.toDTO(guestBookEntity);
		}
		return null;	
	}
	@Transactional
	public void updateProc(GuestBookDTO guestBookDTO) {
		Optional<GuestBookEntity> entity = guestbookRepository.findById(guestBookDTO.getGuestSeq());
		
		// find 한 데이터의 값을 set으로 바꾸는 동작 ==> update
		if(entity.isPresent()) {
			GuestBookEntity g = entity.get();
			g.setGname(guestBookDTO.getGname());
			g.setPwd(guestBookDTO.getPwd());
			g.setText(guestBookDTO.getText());
		}
		
	}
		
}
