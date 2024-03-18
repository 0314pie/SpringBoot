package net.kdigital.spring4.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class FriendDTO {
	private String fname;
	private int age;
	private String phone;
	private LocalDate birthday;
	private boolean active;
}
