package net.kdigital.spring6.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.kdigital.spring6.entity.FriendEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class FriendDTO {
	private Long friendSeq;
	
	@Size(min=2, max=10, message = "2~10자 이내로 이름을 입력하세요")
	private String fname;		// 글자수 제한 or Null 여부 확인
	
	@Min(value = 15, message = "나이는 15세 이상 입력해주세요")
	private int age;			// 15살 이상만 가능
	
	@Pattern(regexp = "01[016789]\\d{4}\\d{4}", message = "- 없이 숫자로만 입력해주세요")
	private String phone;		// 숫자 11자리만 입력되도록 함 (정규식으로 처리)
	
	@PastOrPresent(message = "과거날짜를 선택해주세요")
	@NotNull(message = "생년월일을 선택해주세요")
	private LocalDate birthday;	// 과거 날짜만 가능
	private boolean active;	
	
	public static FriendEntity toEntity(FriendDTO friendDTO) {
		return FriendEntity.builder()
				.friendSeq(friendDTO.getFriendSeq())
				.fname(friendDTO.getFname())
				.age(friendDTO.getAge())
				.phone(friendDTO.getPhone())
				.birthday(friendDTO.getBirthday())
				.active(friendDTO.isActive())
				.build();
		
	}
}
