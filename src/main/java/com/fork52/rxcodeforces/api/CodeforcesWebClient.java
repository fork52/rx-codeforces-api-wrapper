package com.fork52.rxcodeforces.api;

import com.fork52.rxcodeforces.api.dto.CFResponse;
import com.fork52.rxcodeforces.api.dto.Comment;
import com.fork52.rxcodeforces.api.dto.Contest;
import com.fork52.rxcodeforces.api.dto.User;
import com.fork52.rxcodeforces.api.util.Pair;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a CodeforcesWebClient
 * */
public class CodeforcesWebClient {
    private final WebClient cfWebClient;
    private CFAuthenticator cfAuthenticator;

    public CodeforcesWebClient(){
        final int size = 1 << 15;
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
     * makeRequest to Codeforces api
     * @param path
     *      Endpoint path
     * @param params
     *      List of parameters. Each parameter is a pair
     * @param isAuthorized
     *      Boolean value showing whether authorization is required.
     * */
    @SuppressWarnings("unchecked")
    public <T> Mono<CFResponse<T>> makeRequest(String path, List<Pair> params, Boolean isAuthorized){
        CFResponse<T> contestCFResponse = new CFResponse<>();
        List<Pair> queryParams = new ArrayList<>();

        return (Mono<CFResponse<T>>) this.cfWebClient
                .get()
                .uri(
                    uriBuilder -> {
                        uriBuilder = uriBuilder.path(path);

                        queryParams.addAll(params);

                        if (Boolean.TRUE.equals(isAuthorized)) {
                            long unixTime = System.currentTimeMillis() / 1000L;
                            queryParams.add(new Pair("apiKey", cfAuthenticator.getApiKey()));
                            queryParams.add(new Pair("time", String.valueOf(unixTime)));
                            queryParams.addAll(params);
                            queryParams.add(new Pair("apiSig", cfAuthenticator.getApiSignature(path, queryParams)));
                        }


                        for(Pair p: queryParams){
                            uriBuilder = uriBuilder.queryParam(p.getKey(), p.getValue());
                        }

                        return uriBuilder.build();
                    }
                )
                .retrieve()
                .bodyToMono(contestCFResponse.getClass());
    }


    /**
     * Returns information about all available contests.
     * @param gym
     *      Boolean. If true — than gym contests are returned. Otherwise, regular contests are returned.
     * */
    public Mono<CFResponse<Contest>> getContestList(Boolean gym){
        return this.makeRequest(
                "/contest.list",
                List.of(new Pair("gym", gym.toString())),
                false
        );
    }

    /**
     * Returns information about one or several users.
     * @param handles
     *      Semicolon-separated list of handles. No more than 10000 handles is accepted.
     * */
    public Mono<CFResponse<User>> getUserInfo(List<String> handles){
        return this.makeRequest(
                "/user.info",
                List.of(new Pair("handles",  String.join(";", handles))),
                false
        );
    }


    /**
     * Returns a list of comments to the specified blog entry.
     * @param blogEntryId
     *      Id of the blog entry. It can be seen in blog entry URL. For example: /blog/entry/79
     * */
    public Mono<CFResponse<Comment>> getBlogEntryComments(String blogEntryId){
        return this.makeRequest(
                "/blogEntry.comments",
                List.of(new Pair("blogEntryId",  blogEntryId)),
                false
        );
    }
}
