package net.kdigital.spring6.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;
import net.kdigital.spring6.entity.FriendEntity;

public interface FriendRepository extends JpaRepository<FriendEntity, Long> {
	
	@Transactional
	@Modifying
	@Query(value="""
			UPDATE friend
			SET 
			fname = :#{#entity.fname}
			, phone = :#{#entity.phone}
			WHERE friend_seq = :#{#entity.friendSeq}
			""", nativeQuery = true)
	public int updateFriend(@Param("entity") FriendEntity entity);

}
