package com.fork52.rxcodeforces.api;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;
import java.time.Duration;
import java.util.Arrays;

class RxCodeforcesApiApplicationTests {

	@Test
	void testGetContests(){
		StepVerifier.create(CodeforcesWebClient.getInstance().getContests(false))
				.expectSubscription()
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void testGetUsers(){
		StepVerifier.create(CodeforcesWebClient.getInstance().getUsers(Arrays.asList("DmitriyH", "Fefer_Ivan")))
				.expectSubscription()
				.expectNextCount(1)
				.verifyComplete();
	}
}
