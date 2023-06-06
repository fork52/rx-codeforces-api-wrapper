package com.fork52.rxcodeforces.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import java.time.Duration;

class RxCodeforcesApiApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void testGetContests(){
		StepVerifier.create(CodeforcesWebClient.getInstance().getContests(false))
				.expectSubscription()
				.expectNextCount(1)
				.verifyComplete();
	}
}
