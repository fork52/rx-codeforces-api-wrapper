package com.fork52.rxcodeforces.api;

import com.fork52.rxcodeforces.api.dto.BlogEntry;
import com.fork52.rxcodeforces.api.dto.CFResponse;
import com.fork52.rxcodeforces.api.dto.CFResponseList;
import com.fork52.rxcodeforces.api.dto.Comment;
import com.fork52.rxcodeforces.api.dto.Contest;
import com.fork52.rxcodeforces.api.dto.ContestStandings;
import com.fork52.rxcodeforces.api.dto.Hack;
import com.fork52.rxcodeforces.api.dto.ProblemSet;
import com.fork52.rxcodeforces.api.dto.RatingChange;
import com.fork52.rxcodeforces.api.dto.RecentAction;
import com.fork52.rxcodeforces.api.dto.Submission;
import com.fork52.rxcodeforces.api.dto.User;
import com.fork52.rxcodeforces.api.exception.CodeforcesApiException;
import com.fork52.rxcodeforces.api.util.Pair;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

/**
 * Represents a CodeforcesWebClient
 */
@Slf4j
public class CodeforcesWebClient {

  private final WebClient cfWebClient;
  private CFAuthenticator cfAuthenticator;

  public CodeforcesWebClient() {
    final int size = 1 << 26;
    final ExchangeStrategies strategies = ExchangeStrategies.builder()
        .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
        .build();

    this.cfWebClient = WebClient.builder()
        .exchangeStrategies(strategies)
        .baseUrl("https://codeforces.com/api/")
        .build();

    this.cfAuthenticator = null;
  }

  public CodeforcesWebClient(String apiKey, String apiSecret) {
    this();
    this.cfAuthenticator = new CFAuthenticator(apiKey, apiSecret);
  }

  /**
   * Prepares the uriBuildler.
   */
  private UriBuilder getUriBuilder(UriBuilder uriBuilder, String path, List<Pair> params,
      Boolean isAuthorized) {
    uriBuilder = uriBuilder.path(path);
    List<Pair> queryParams = new ArrayList<>(params);

    if (Boolean.TRUE.equals(isAuthorized)) {
      long unixTime = System.currentTimeMillis() / 1000L;
      queryParams.add(new Pair("apiKey", cfAuthenticator.getApiKey()));
      queryParams.add(new Pair("time", String.valueOf(unixTime)));
      queryParams.add(new Pair("apiSig", cfAuthenticator.getApiSignature(path, queryParams)));
    }

    for (Pair p : queryParams) {
      uriBuilder = uriBuilder.queryParam(p.getKey(), p.getValue());
    }

    return uriBuilder;
  }

  /**
   * Returns paramList after filtering out null parameters.
   */
  private List<Pair> filterNullParameters(String[] keys, Object[] values) {
    List<Pair> paramList = new ArrayList<>();
    for (int i = 0; i < values.length; i++) {
      if (values[i] != null) {
        paramList.add(new Pair(keys[i], values[i].toString()));
      }
    }
    return paramList;
  }

  /**
   * makeRequest to Codeforces api
   *
   * @param path         Endpoint path
   * @param params       List of parameters. Each parameter is a pair
   * @param isAuthorized Boolean value showing whether authorization is required.
   */
  private <T> Mono<CFResponseList<T>> makeListRequest(String path, List<Pair> params,
      Boolean isAuthorized, ParameterizedTypeReference<CFResponseList<T>> typeReference) {
    return this.cfWebClient
        .get()
        .uri(
            uriBuilder -> getUriBuilder(uriBuilder, path, params, isAuthorized)
                .build()
        )
        .retrieve()
        .bodyToMono(typeReference);
  }

  /**
   * makeRequest to Codeforces api
   *
   * @param path         Endpoint path
   * @param params       List of parameters. Each parameter is a pair
   * @param isAuthorized Boolean value showing whether authorization is required.
   */
  private <T> Mono<CFResponse<T>> makeRequest(String path, List<Pair> params,
      Boolean isAuthorized,
      ParameterizedTypeReference<CFResponse<T>> typeReference
  ) {
    return this.cfWebClient
        .get()
        .uri(
            uriBuilder -> getUriBuilder(uriBuilder, path, params, isAuthorized)
                .build()
        )
        .retrieve()
        .bodyToMono(typeReference);
  }

  /**
   * Returns a list of comments to the specified blog entry.
   *
   * @param blogEntryId Id of the blog entry. It can be seen in blog entry URL. For example:
   *                    /blog/entry/79
   */
  public Mono<CFResponseList<Comment>> getBlogEntryComments(String blogEntryId) {
    return this.makeListRequest(
        "blogEntry.comments",
        List.of(new Pair("blogEntryId", blogEntryId)),
        false,
        new ParameterizedTypeReference<CFResponseList<Comment>>() {
        }
    );
  }

  /**
   * Returns blog entry.
   *
   * @param blogEntryId Id of the blog entry. It can be seen in blog entry URL. For example:
   *                    /blog/entry/79
   */
  public Mono<CFResponse<BlogEntry>> getBlogEntryView(String blogEntryId) {
    return this.makeRequest(
        "blogEntry.view",
        List.of(new Pair("blogEntryId", blogEntryId)),
        false,
        new ParameterizedTypeReference<CFResponse<BlogEntry>>() {
        }
    );
  }

  /**
   * Returns list of hacks in the specified contests. Full information about hacks is available only
   * after some time after the contest end. During the contest user can see only own hacks.
   *
   * @param contestId (Required) Id of the contest. It is not the round number. It can be seen in
   *                  contest URL. For example: /contest/566/status
   */
  public Mono<CFResponseList<Hack>> getContestHacks(String contestId) {
    return this.makeListRequest(
        "contest.hacks",
        List.of(new Pair("contestId", contestId)),
        false,
        new ParameterizedTypeReference<CFResponseList<Hack>>() {
        }
    );
  }

  /**
   * Returns information about all available contests.
   *
   * @param gym Boolean. If true — than gym contests are returned. Otherwise, regular contests are
   *            returned.
   */
  public Mono<CFResponseList<Contest>> getContestList(Boolean gym) {
    return this.makeListRequest(
        "contest.list",
        List.of(new Pair("gym", gym.toString())),
        false,
        new ParameterizedTypeReference<CFResponseList<Contest>>() {
        }
    );
  }

  /**
   * Returns rating changes after the contest.
   *
   * @param contestId Id of the contest. It is not the round number. It can be seen in contest URL.
   *                  For example: /contest/566/status
   */
  public Mono<CFResponseList<RatingChange>> getContestRatingChanges(String contestId) {
    return this.makeListRequest(
        "contest.ratingChanges",
        List.of(new Pair("contestId", contestId)),
        false,
        new ParameterizedTypeReference<CFResponseList<RatingChange>>() {
        }
    );
  }

  /**
   * Returns the requested part of the contest standings.
   *
   * @param contestId      (Required) The ID of the contest. It is not the round number. It can be
   *                       seen in the contest URL. For example: /contest/566/status
   * @param from           The 1-based index of the standings row to start the ranklist.
   * @param count          The number of standing rows to return.
   * @param handles        The semicolon-separated list of handles. No more than 10000 handles are
   *                       accepted.
   * @param room           If specified, only participants from this room will be shown in the
   *                       result. If not specified, all participants will be shown.
   * @param showUnofficial If true, all participants (including virtual and out of competition) are
   *                       shown. Otherwise, only official contestants are shown.
   * @return The ContestStandings for the specified contest and parameters.
   */
  public Mono<CFResponse<ContestStandings>> getContestStandings(
      String contestId,
      Integer from,
      Integer count,
      List<String> handles,
      String room,
      Boolean showUnofficial
  ) {
    Object[] values = {contestId, from, count, handles, room, showUnofficial};
    String[] keys = {"contestId", "from", "count", "handles", "room", "showUnofficial"};

    return this.makeRequest(
        "contest.standings",
        filterNullParameters(keys, values),
        false,
        new ParameterizedTypeReference<CFResponse<ContestStandings>>() {
        }
    );
  }

  /**
   * Retrieves the submissions for a specified contest. Optionally, it can return submissions of a
   * specified user.
   *
   * @param contestId (Required) The ID of the contest. It is not the round number. It can be seen
   *                  in the contest URL. For example: /contest/566/status
   * @param handle    The Codeforces user handle. If specified, only submissions of this user will
   *                  be returned.
   * @param from      The 1-based index of the first submission to return.
   * @param count     The number of returned submissions.
   * @return The submissions for the specified contest and parameters.
   */
  public Mono<CFResponseList<Submission>> getContestStatus(
      String contestId,
      String handle,
      Integer from,
      Integer count) {
    Object[] values = {contestId, handle, from, count};
    String[] keys = {"contestId", "handles", "from", "count"};

    return this.makeListRequest(
        "contest.status",
        filterNullParameters(keys, values),
        false,
        new ParameterizedTypeReference<CFResponseList<Submission>>() {
        }
    );
  }

  /**
   * Retrieves all problems from the problemset. Problems can be filtered by tags.
   *
   * @param tags           A semicolon-separated list of tags. Only problems with matching tags will
   *                       be returned.
   * @param problemSetName The custom problemset's short name, like 'acmsguru'. If specified, only
   *                       problems from the specified problemset will be returned.
   * @return Two lists: a list of Problem objects and a list of ProblemStatistics objects. The list
   * of Problem objects contains the retrieved problems from the problemset. The list of
   * ProblemStatistics objects contains the statistics data about each problem.
   */
  public Mono<CFResponse<ProblemSet>> getProblemSetProblems(
      List<String> tags,
      String problemSetName) {
    List<Pair> params = new ArrayList<>();

    if (tags != null && !tags.isEmpty()) {
      params.add(new Pair("tags", String.join(";", tags)));
    }

    if (problemSetName != null) {
      params.add(new Pair("problemSetName", problemSetName));
    }

    return this.makeRequest(
        "problemset.problems",
        params,
        false,
        new ParameterizedTypeReference<CFResponse<ProblemSet>>() {
        }
    );
  }

  /**
   * Retrieves recent submissions.
   *
   * @param count          (Required) The number of submissions to return. Can be up to 1000.
   * @param problemSetName The custom problemset's short name, like 'acmsguru'. If specified, only
   *                       submissions from the specified problemset will be returned.
   * @return A list of Submission objects, sorted in decreasing order of submission id.
   */
  public Mono<CFResponseList<Submission>> getProblemSetRecentStatus(
      Integer count, String problemSetName) throws CodeforcesApiException {

    if (count == null || count == 0 || count > 1000) {
      throw new CodeforcesApiException("Please enter a valid count in range [0, 1000].");
    }
    List<Pair> params = new ArrayList<>();
    params.add(new Pair("count", count.toString()));

    if (problemSetName != null && !problemSetName.isBlank()) {
      params.add(new Pair("problemsetName", problemSetName));
    }

    return this.makeListRequest(
        "problemset.recentStatus",
        params,
        false,
        new ParameterizedTypeReference<CFResponseList<Submission>>() {
        }
    );
  }

  /**
   * Retrieves recent actions.
   *
   * @param maxCount (Required) The number of recent actions to return. Can be up to 100.
   * @return A List response having list of RecentAction objects representing the recent actions.
   */
  public Mono<CFResponseList<RecentAction>> getRecentActions(
      Integer maxCount) throws CodeforcesApiException {

    if (maxCount == null || maxCount == 0 || maxCount > 1000) {
      throw new CodeforcesApiException("Please enter a valid maxCount in range [0, 100].");
    }

    return this.makeListRequest(
        "recentActions",
        List.of(new Pair("maxCount", maxCount.toString())),
        false,
        new ParameterizedTypeReference<CFResponseList<RecentAction>>() {
        }
    );
  }

  /**
   * Retrieves a list of all user's blog entries.
   *
   * @param handle (Required) The Codeforces user handle.
   * @return A list of BlogEntry objects in short form.
   */
  public Mono<CFResponseList<BlogEntry>> getUserBlogEntries(
      String handle) throws CodeforcesApiException {

    if (handle == null || handle.isBlank()) {
      throw new CodeforcesApiException("Please enter a valid handle.");
    }

    return this.makeListRequest(
        "user.blogEntries",
        List.of(new Pair("handle", handle)),
        false,
        new ParameterizedTypeReference<CFResponseList<BlogEntry>>() {
        }
    );
  }

  /**
   * Returns authorized user's friends. Using this method requires authorization.
   *
   * @param onlyOnline Boolean. If true — only online friends are returned. Otherwise, all friends
   *                   are returned.
   */
  public Mono<CFResponseList<String>> getUserFriends(Boolean onlyOnline) {
    if (onlyOnline == null) {
      onlyOnline = false;
    }
    return this.makeListRequest(
        "user.friends",
        List.of(new Pair("onlyOnline", onlyOnline.toString())),
        true,
        new ParameterizedTypeReference<CFResponseList<String>>() {
        }
    );
  }

  /**
   * Returns information about one or several users.
   *
   * @param handles Semicolon-separated list of handles. No more than 10000 handles is accepted.
   */
  public Mono<CFResponseList<User>> getUserInfo(List<String> handles)
      throws CodeforcesApiException {

    if (handles == null || handles.isEmpty()) {
      throw new CodeforcesApiException("Please provide at least one handle.");
    }

    return this.makeListRequest(
        "user.info",
        List.of(new Pair("handles", String.join(";", handles))),
        false,
        new ParameterizedTypeReference<CFResponseList<User>>() {
        }
    );
  }

  /**
   * Retrieves the list of users who have participated in at least one rated contest.
   *
   * @param activeOnly     Boolean. If true, only users who participated in a rated contest during
   *                       the last month are returned. Otherwise, all users with at least one rated
   *                       contest are returned.
   * @param includeRetired Boolean. If true, the method returns all rated users. Otherwise, the
   *                       method returns only users who were online in the last month.
   * @param contestId      Id of the contest. It is not the round number. It can be seen in the
   *                       contest URL. For example: /contest/566/status
   * @return A list of User objects, sorted in decreasing order of rating.
   */
  public Mono<CFResponseList<User>> getUserRatedList(
      Boolean activeOnly, Boolean includeRetired, String contestId) {

    Object[] values = {activeOnly, includeRetired, contestId};
    String[] keys = {"activeOnly", "includeRetired", "contestId"};

    return this.makeListRequest(
        "user.ratedList",
        filterNullParameters(keys, values),
        false,
        new ParameterizedTypeReference<CFResponseList<User>>() {
        }
    );
  }

  /**
   * Retrieves the rating history of the specified user.
   *
   * @param handle (Required) The Codeforces user handle.
   * @return A list of RatingChange objects representing the rating history of the requested user.
   */
  public Mono<CFResponseList<RatingChange>> getUserRating(String handle)
      throws CodeforcesApiException {

    if (handle == null || handle.isBlank()) {
      throw new CodeforcesApiException("Please provide a valid handle");
    }

    return this.makeListRequest(
        "user.rating",
        List.of(new Pair("handle", handle)),
        false,
        new ParameterizedTypeReference<CFResponseList<RatingChange>>() {
        }
    );
  }

  /**
   * Retrieves the submissions of the specified user.
   *
   * @param handle (Required) The Codeforces user handle.
   * @param from   The 1-based index of the first submission to return.
   * @param count  The number of submissions to return.
   * @return A list of Submission objects, sorted in decreasing order of submission id.
   */
  public Mono<CFResponseList<Submission>> getUserStatus(String handle, Integer from, Integer count)
      throws CodeforcesApiException {

    if (handle == null || handle.isBlank()) {
      throw new CodeforcesApiException("Please provide a valid handle");
    }

    Object[] values = {handle, from, count};
    String[] keys = {"handle", "from", "count"};

    return this.makeListRequest(
        "user.status",
        filterNullParameters(keys, values),
        false,
        new ParameterizedTypeReference<CFResponseList<Submission>>() {
        }
    );
  }

}
