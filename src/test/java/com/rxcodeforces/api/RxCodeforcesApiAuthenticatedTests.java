package com.rxcodeforces.api;

import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class RxCodeforcesApiAuthenticatedTests {

  private final String apiKey = System.getenv("CF_API_KEY");
  private final String apiSecret = System.getenv("CF_API_SECRET");

  @Test
  void testUserFriends() {
    StepVerifier.create(new CodeforcesWebClient(apiKey, apiSecret).getUserFriends(true))
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();
  }
}
