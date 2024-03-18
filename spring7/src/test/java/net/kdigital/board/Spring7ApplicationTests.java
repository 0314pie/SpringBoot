package net.kdigital.board;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import net.kdigital.board.entity.BoardEntity;
import net.kdigital.board.repository.BoardRepository;

@SpringBootTest
class Spring7ApplicationTests {

	@Test
	void contextLoads() {
	}
	
	@Autowired
	private BoardRepository repository;
	
	@Test
	void testInsertBoard() {
		for(int i=0; i<22; ++i) {
			BoardEntity b = new BoardEntity();
			
			b.setBoardWriter("디마짱");
			b.setBoardTitle("페이징 해보기" + (i+1));
			b.setBoardContent("테스트 중");
			
			repository.save(b);
		}
	}

}
