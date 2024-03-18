package net.kdigital.test2.entity;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;

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
import net.kdigital.test2.dto.GuestBookDTO;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name="guestbook")
public class GuestBookEntity {
	@SequenceGenerator(
	name="guestbook_seq"
	,sequenceName = "guestbook_seq"
	,initialValue = 1
	,allocationSize = 1
	)
	@Id
	@Column(name="guest_seq")
	@GeneratedValue(generator="guestbook_seq")
	private Long guestSeq;
	
	@Column(name = "guest_name")
	private String gname;
	
	@Column(name = "guest_pwd")
	private String pwd;
	
	@Column(name = "guest_text")
	private String text;
	
	@Column(name = "regdate")
	private LocalDateTime regdate;
	
	public static GuestBookDTO toDTO(GuestBookEntity guestBookEntity) {
		return GuestBookDTO.builder()
				.guestSeq(guestBookEntity.getGuestSeq())
				.gname(guestBookEntity.getGname())
				.pwd(guestBookEntity.getPwd())
				.text(guestBookEntity.getText())
				.regdate(guestBookEntity.getRegdate())
				.build();
	}

}
