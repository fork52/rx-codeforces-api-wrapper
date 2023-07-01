package com.fork52.rxcodeforces.api;

import com.fork52.rxcodeforces.api.dto.*;
import com.fork52.rxcodeforces.api.util.Pair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a CodeforcesWebClient
 * */
@Slf4j
public class CodeforcesWebClient {
    private final WebClient cfWebClient;
    private CFAuthenticator cfAuthenticator;

    public CodeforcesWebClient(){
        final int size = 1 << 20;
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();

        this.cfWebClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .baseUrl("https://codeforces.com/api/")
                .build();

        this.cfAuthenticator = null;
    }

     public CodeforcesWebClient(String apiKey, String apiSecret){
        this();
        this.cfAuthenticator = new CFAuthenticator(apiKey, apiSecret);
    }

    /**
     * Prepares the uriBuildler.
     * */
    private UriBuilder getUriBuilder(UriBuilder uriBuilder, String path, List<Pair> params, Boolean isAuthorized){
        uriBuilder = uriBuilder.path(path);
        List<Pair> queryParams = new ArrayList<>(params);

        if (Boolean.TRUE.equals(isAuthorized)) {
            long unixTime = System.currentTimeMillis() / 1000L;
            queryParams.add(new Pair("apiKey", cfAuthenticator.getApiKey()));
            queryParams.add(new Pair("time", String.valueOf(unixTime)));
            queryParams.add(new Pair("apiSig", cfAuthenticator.getApiSignature(path, queryParams)));
        }

        for(Pair p: queryParams){
            uriBuilder = uriBuilder.queryParam(p.getKey(), p.getValue());
        }

        return uriBuilder;
    }

    /**
     * Returns paramList after filtering out null parameters.
     */
    private List<Pair> filterNullParameters(String[] keys, Object[] values){
        List<Pair> paramList = new ArrayList<>();
        for(int i = 0; i < values.length; i++){
            if(values[i] != null){
                paramList.add(new Pair(keys[i], values[i].toString()));
            }
        }
        return paramList;
    }

    /**
     * makeRequest to Codeforces api
     * @param path
     *      Endpoint path
     * @param params
     *      List of parameters. Each parameter is a pair
     * @param isAuthorized
     *      Boolean value showing whether authorization is required.
     * */
    @SuppressWarnings("unchecked")
    private <T> Mono<CFResponseList<T>> makeListRequest(String path, List<Pair> params, Boolean isAuthorized){
        CFResponseList<T> cfResponseList = new CFResponseList<>();
        return (Mono<CFResponseList<T>>) this.cfWebClient
                .get()
                .uri(
                        uriBuilder -> getUriBuilder(uriBuilder, path, params, isAuthorized)
                                .build()
                )
                .retrieve()
                .bodyToMono(cfResponseList.getClass());
    }

    /**
     * makeRequest to Codeforces api
     * @param path
     *      Endpoint path
     * @param params
     *      List of parameters. Each parameter is a pair
     * @param isAuthorized
     *      Boolean value showing whether authorization is required.
     * */
    @SuppressWarnings("unchecked")
    private <T> Mono<CFResponse<T>> makeRequest(String path, List<Pair> params, Boolean isAuthorized){
        CFResponse<T> cfResponse = new CFResponse<>();
        return (Mono<CFResponse<T>>) this.cfWebClient
                .get()
                .uri(
                        uriBuilder -> getUriBuilder(uriBuilder, path, params, isAuthorized)
                                .build()
                )
                .retrieve()
                .bodyToMono(cfResponse.getClass());
    }

    /**
     * Returns a list of comments to the specified blog entry.
     * @param blogEntryId
     *      Id of the blog entry. It can be seen in blog entry URL. For example: /blog/entry/79
     * */
    public Mono<CFResponseList<Comment>> getBlogEntryComments(String blogEntryId){
        return this.makeListRequest(
                "blogEntry.comments",
                List.of(new Pair("blogEntryId",  blogEntryId)),
                false
        );
    }

    /**
     * Returns blog entry.
     * @param blogEntryId
     *      Id of the blog entry. It can be seen in blog entry URL. For example: /blog/entry/79
     * */
    public Mono<CFResponse<BlogEntry>> getBlogEntryView(String blogEntryId){
        return this.makeRequest(
                "blogEntry.view",
                List.of(new Pair("blogEntryId",  blogEntryId)),
                false
        );
    }

    /**
     * Returns list of hacks in the specified contests.
     * Full information about hacks is available only after some time after the contest end.
     * During the contest user can see only own hacks.
     * @param contestId (Required)
     *      Id of the contest. It is not the round number. It can be seen in contest URL. For example: /contest/566/status
     * */
    public Mono<CFResponseList<Hack>> getContestHacks(String contestId){
        return this.makeListRequest(
                "contest.hacks",
                List.of(new Pair("contestId",  contestId)),
                false
        );
    }

    /**
     * Returns information about all available contests.
     * @param gym
     *      Boolean. If true — than gym contests are returned. Otherwise, regular contests are returned.
     * */
    public Mono<CFResponseList<Contest>> getContestList(Boolean gym){
        return this.makeListRequest(
                "contest.list",
                List.of(new Pair("gym", gym.toString())),
                false
        );
    }

    /**
     * Returns rating changes after the contest.
     * @param contestId
     *     Id of the contest. It is not the round number. It can be seen in contest URL. For example: /contest/566/status
     * */
    public Mono<CFResponseList<RatingChange>> getContestRatingChanges(String contestId){
        return this.makeListRequest(
                "contest.ratingChanges",
                List.of(new Pair("contestId", contestId)),
                false
        );
    }

    /**
     * Returns the requested part of the contest standings.
     * @param contestId (Required) The ID of the contest. It is not the round number. It can be seen in the contest URL. For example: /contest/566/status
     * @param from The 1-based index of the standings row to start the ranklist.
     * @param count The number of standing rows to return.
     * @param handles The semicolon-separated list of handles. No more than 10000 handles are accepted.
     * @param room If specified, only participants from this room will be shown in the result. If not specified, all participants will be shown.
     * @param showUnofficial If true, all participants (including virtual and out of competition) are shown. Otherwise, only official contestants are shown.
     * @return The ContestStandings for the specified contest and parameters.
     * */
    public Mono<CFResponse<ContestStandings>> getContestStandings(
            String contestId,
            Integer from,
            Integer count,
            List<String> handles,
            String room,
            Boolean showUnofficial
        )
    {
        Object[] values = {contestId ,   from,  count,   handles,   room,   showUnofficial };
        String[] keys =   {"contestId", "from", "count", "handles", "room", "showUnofficial"};

        return this.makeRequest(
                "contest.standings",
                filterNullParameters(keys, values),
                false
        );
    }

    /**
     * Returns information about one or several users.
     * @param handles
     *      Semicolon-separated list of handles. No more than 10000 handles is accepted.
     * */
    public Mono<CFResponseList<User>> getUserInfo(List<String> handles){
        return this.makeListRequest(
                "user.info",
                List.of(new Pair("handles",  String.join(";", handles))),
                false
        );
    }

    /**
     * Returns authorized user's friends. Using this method requires authorization.
     * @param onlyOnline
     *      Boolean. If true — only online friends are returned. Otherwise, all friends are returned.
     * */
    public Mono<CFResponseList<String>> getUserFriends(Boolean onlyOnline){
        return this.makeListRequest(
                "user.friends",
                List.of(new Pair("onlyOnline",  onlyOnline.toString())),
                true
        );
    }

}
