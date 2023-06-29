package com.fork52.rxcodeforces.api;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.Arrays;

class RxCodeforcesApiApplicationTests {

	@Test
	void testGetContestList(){
		StepVerifier.create(new CodeforcesWebClient().getContestList(false))
				.expectSubscription()
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void testGetUserInfo(){
		StepVerifier.create(new CodeforcesWebClient().getUserInfo(Arrays.asList("DmitriyH", "Fefer_Ivan")))
				.expectSubscription()
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void testBlogEntryComments(){
		StepVerifier.create(new CodeforcesWebClient().getBlogEntryComments("79"))
				.expectSubscription()
				.expectNextCount(1)
				.verifyComplete();
	}
}
