package backend.codebackend;

import backend.codebackend.domain.Chat;
import backend.codebackend.domain.Menu;
import backend.codebackend.domain.Restuarant;
import backend.codebackend.dto.ChatDTO;
import backend.codebackend.domain.Member;
import backend.codebackend.repository.MemberRepository;
import backend.codebackend.service.ChatService;
import backend.codebackend.service.ChatUserService;
import backend.codebackend.service.MemberService;
import backend.codebackend.service.RestaurantService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

//import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
class CodeBackendApplicationTests {
	@Autowired MemberService memberService;
	@Autowired MemberRepository MemberRepository;
	@Autowired
	ChatService chatService;
	@Autowired
	ChatUserService chatUserService;

	@Autowired
	RestaurantService restaurantService;



	@Test
	@DisplayName("유저 리스트 조회")
	void 유저리스트조회() {
		List<String> result = chatUserService.getUserList(87L);
		List<String> result2 = chatUserService.getUserList(88L);

		System.out.println(result);
		System.out.println(result2);
	}

	@Test
	@DisplayName("채팅방의 유저 닉네임 중복 조회")
	void 유저중복조회() {
		if(chatUserService.addUser(87L, "야왕이네"))
			System.out.println("저장 잘됨!");
		else
			System.out.println("저장 안됨!");
	}

	@Test
	@DisplayName("채팅 친 시간 조회")
	void 채팅시간조회() {
		ChatDTO chatDTO = ChatDTO.builder()
						.id(13L)
				        .type(ChatDTO.MessageTYpe.valueOf("ENTER"))
						.sender("야왕")
						.message("테스트테스트테스트테스트테스트테스트테스트테스트테스트")
				        .build();

		Chat chat = chatService.save(chatDTO);
		chatService.timestamp(chat);
	}

	@Test
	@DisplayName("방장 조회 테스트")
	void 방장조회() {

		if (chatService.isCurrentUserHost(64L, "김윤준")){
			System.out.println("방장 맞음 ");
		}
	}

	@Test
	@DisplayName("가게 정보 조회 테스트")
	void 가게정보조회() {
		Member member = memberService.findLoginId("dbswns1101").get();
		System.out.println(member + "님의 주소는 : " + member.getAddress() + "입니다.");
		List<Restuarant> rs = restaurantService.RsData(member.getAddress());

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		for (Restuarant r : rs) {
			System.out.println("가게 이름 : " + r.getTitle());
			System.out.println("최소 주문 금액 : " + r.getMinPrice());
			System.out.println("이미지 url : " + r.getImageUrl());
		}
	}

	@SneakyThrows
	@Test
	@DisplayName("메뉴 리스트 조회 테스트")
	void 메뉴리스트조회() {
		Member member = memberService.findLoginId("dbswns1101").get();
		System.out.println(member + "님의 주소는 : " + member.getAddress() + "입니다.");
		List<Menu> menu = restaurantService.menuList("버거킹-의정부서부역점", member.getAddress());

			for(Menu m : menu) {
				System.out.println("메뉴 이름 : " + m.getMenuName());
				System.out.println("메뉴 가격 : " + m.getMenuPrice());
				System.out.println("메뉴 상세 : " + m.getMenuDesc());
//				System.out.println("메뉴 포토 : " + m.getMenuPhoto());
			}

	}
}
