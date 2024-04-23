package com.acejobber.chatserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

// @SpringBootTest
@SpringBootTest(classes = ChatserverApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatserverApplicationTests {

	@Test
	void contextLoads() {
	}

}
