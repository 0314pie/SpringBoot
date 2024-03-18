package net.kdigital.spring6.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import net.kdigital.spring6.dto.FriendDTO;
import net.kdigital.spring6.entity.FriendEntity;
import net.kdigital.spring6.repository.FriendRepository;

@Service
@Slf4j
public class FriendService {
	
	private FriendRepository friendRepository;
	
	public FriendService(FriendRepository friendRepository) {
		this.friendRepository = friendRepository;
	}
	
	/**
	 * DB에 데이터 저장
	 * @param friendDTO
	 */
	public void insertFriend(FriendDTO friendDTO) {
		log.info("service에 도착!");
		
		// 1) DTO -> Entity로 변환
		// dto -> entity
		FriendEntity entity = FriendDTO.toEntity(friendDTO);
		
		// entity -> dto
        // FriendDTO dto = FriendEntity.toDTO(entity);
		 
		// 2) Repository로 넘겨서 저장
		friendRepository.save(entity);
	}
	
	/**
	 * 데이터 전체 조회
	 * @return
	 */
	public List<FriendDTO> selectAll() {
		// 정렬되지 않음
		// List<FriendEntity> list = friendRepository.findAll();
		
		// 이름순으로 정렬
		List<FriendEntity> list = friendRepository.findAll(Sort.by(Sort.Direction.ASC,"fname"));
		List<FriendDTO> friendDTOList = new ArrayList<>();
		
		list.forEach((entity) -> friendDTOList.add(FriendEntity.toDTO(entity)));
		
		return friendDTOList;
	}
	
	public void deleteOne(Long friendSeq) {
		friendRepository.deleteById(friendSeq);
	}
	
	@SuppressWarnings("static-access")
	public FriendDTO selectOne(Long friendSeq) {
		Optional<FriendEntity> entity = friendRepository.findById(friendSeq);
		
		if(entity.isPresent()) {
			FriendEntity friendEntity = entity.get();
			return friendEntity.toDTO(friendEntity);
		}
		return null;
	}
	
	@Transactional
	public void updateProc(FriendDTO friendDTO) {
		FriendEntity entity = FriendDTO.toEntity(friendDTO);
		log.info("{}", entity.toString());
//		Optional<FriendEntity> entity = friendRepository.findById(friendDTO.getFriendSeq());
		
		// find 한 데이터의 값을 set으로 바꾸는 동작 ==> update
//		if(entity.isPresent()) {
//			FriendEntity f = entity.get();
//			f.setFname(friendDTO.getFname());
//			f.setAge(friendDTO.getAge());
//			f.setPhone(friendDTO.getPhone());
//			f.setBirthday(friendDTO.getBirthday());
//			f.setActive(friendDTO.isActive());
//		}
		
		 friendRepository.updateFriend(entity);
	}
}




