package net.kdigital.board.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.kdigital.board.dto.BoardDTO;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder

@Entity
@Table(name="board")
public class BoardEntity {
	@SequenceGenerator(
			name="board_seq"
			, sequenceName = "board_seq"
			, initialValue = 1
			, allocationSize = 1
		)
	
	@Id
	@GeneratedValue(generator = "board_seq")
	@Column(name="board_num")
	private Long boardNum;
	
	@Column(name="board_writer", nullable = false)
	private String boardWriter;
	
	@Column(name="board_title")
	private String boardTitle;
	
	@Column(name="board_content")
	private String boardContent;
	
	@Column(name="hit_count")
	private int hitCount;
	
	@Column(name="favorite_count")
	private int favoriteCount;
	
	@Column(name="create_date")
	@CreationTimestamp   	// 게시글 처음 생성될 떄 자동으로 날짜 세팅
	private LocalDateTime createDate;
	
	@Column(name="update_date")
	@LastModifiedDate		// 게시글이 수정된 마지막 날짜/시간을 세팅
	private LocalDateTime updateDate;
	
	// 첨부파일이 있을 때
	@Column(name="original_file_name")
	private String originalFileName;		// 원본 파일의 파일명
	
	@Column(name="saved_file_name")
	private String savedFileName;			// 하드디스크에 저장될 파일명
	
	// DTO --> Entity
	public static BoardEntity toEntity(BoardDTO boardDTO) {
		return BoardEntity.builder()
				.boardNum(boardDTO.getBoardNum())
				.boardWriter(boardDTO.getBoardWriter())
				.boardTitle(boardDTO.getBoardTitle())
				.boardContent(boardDTO.getBoardContent())
				.hitCount(boardDTO.getHitCount())
				.favoriteCount(boardDTO.getFavoriteCount())
				.originalFileName(boardDTO.getOriginalFileName())
				.savedFileName(boardDTO.getSavedFileName())
				.build();
				
	}
}
