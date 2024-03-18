package net.kdigital.test2.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.kdigital.test2.entity.GuestBookEntity;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class GuestBookDTO {
	private Long guestSeq;
	
	@Size(min=2, max=10, message = "2~10글자 사이로 입력하세요")
	private String gname;
	@Pattern(regexp = "[0-9]+$", message = "숫자만 입력하세요")
	@Size(min=4, max=10, message = "4~10자리 사이로 입력하세요")
	private String pwd;
	@Size(min=5, message = "5글자 이상 입력하세요")
	private String text;
	private LocalDateTime regdate;
	
	public static GuestBookEntity toEntity(GuestBookDTO guestBookDTO) {
		return GuestBookEntity.builder()
				.gname(guestBookDTO.getGname())
				.pwd(guestBookDTO.getPwd())
				.text(guestBookDTO.getText())
				.regdate(guestBookDTO.getRegdate())
				.build();
	}
	
}
