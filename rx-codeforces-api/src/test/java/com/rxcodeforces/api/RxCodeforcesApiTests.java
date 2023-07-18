package com.rxcodeforces.api;

import com.rxcodeforces.api.exception.CodeforcesApiException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;


class RxCodeforcesApiTests {

  private static final CodeforcesWebClient codeforcesWebClient = new CodeforcesWebClient();
  private static final CountDownLatch waiter = new CountDownLatch(1);

  @BeforeEach
  void waitMethod() throws InterruptedException {
    waiter.await(2, TimeUnit.SECONDS);
  }

  @Test
  void testGetBlogEntryComments() {
    StepVerifier.create(codeforcesWebClient.getBlogEntryComments("79"))
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  void testGetBlogEntryViews() {
    StepVerifier.create(codeforcesWebClient.getBlogEntryView("79"))
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  void testGetContestHacks() {
    StepVerifier.create(codeforcesWebClient.getContestHacks("566"))
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  void testGetContestList() {
    StepVerifier.create(codeforcesWebClient.getContestList(false))
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  void testGetContestRatingChanges() {
    StepVerifier.create(codeforcesWebClient.getContestRatingChanges("566"))
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  void testGetContestStandings() {
    StepVerifier.create(codeforcesWebClient.getContestStandings(
            "566", 1, 100, null, null, true
        ))
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  void testGetContestStatus() {
    StepVerifier.create(codeforcesWebClient.getContestStatus(
            "566", null, 1, 100
        ))
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  void testGetProblemSetProblems() {
    StepVerifier.create(codeforcesWebClient.getProblemSetProblems(
            List.of("implementation"), null
        ))
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  void testGetUserBlogEntries() throws CodeforcesApiException {

    StepVerifier.create(codeforcesWebClient.getUserBlogEntries("Fefer_Ivan"))
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();
  }


  @Test
  void testGetUserInfo() throws CodeforcesApiException {
    StepVerifier.create(codeforcesWebClient.getUserInfo(Arrays.asList("DmitriyH", "Fefer_Ivan")))
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  void testGetUserRatedList() throws CodeforcesApiException {
    StepVerifier.create(codeforcesWebClient.getUserRatedList(true, false, null))
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();
  }

  @Test
  void testGetUserStatus() throws CodeforcesApiException {
    StepVerifier.create(codeforcesWebClient.getUserStatus("fork52", null, null))
        .expectSubscription()
        .expectNextCount(1)
        .verifyComplete();
  }
}
