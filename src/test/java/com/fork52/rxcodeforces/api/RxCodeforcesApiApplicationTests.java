package com.fork52.rxcodeforces.api;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Arrays;

class RxCodeforcesApiApplicationTests {

	@Test
	void testGetContestList(){
		StepVerifier.create(CodeforcesWebClient.getInstance().getContestList(false))
				.expectSubscription()
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void testGetUserInfo(){
		StepVerifier.create(CodeforcesWebClient.getInstance().getUserInfo(Arrays.asList("DmitriyH", "Fefer_Ivan")))
				.expectSubscription()
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void testBlogEntryComments(){
		StepVerifier.create(CodeforcesWebClient.getInstance().getBlogEntryComments("79"))
				.expectSubscription()
				.expectNextCount(1)
				.verifyComplete();
	}
}
