package com.fork52.rxcodeforces.api;

import com.fork52.rxcodeforces.api.dto.CFResponse;
import com.fork52.rxcodeforces.api.dto.Comment;
import com.fork52.rxcodeforces.api.dto.Contest;
import com.fork52.rxcodeforces.api.dto.User;
import com.fork52.rxcodeforces.api.util.Pair;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

public class CodeforcesWebClient {
    private volatile static CodeforcesWebClient uniqueInstance;
    private WebClient codeforcesWebClient;

    private CodeforcesWebClient(){}

    public static CodeforcesWebClient getInstance(){
        if(uniqueInstance == null){
            synchronized (CodeforcesWebClient.class) {
                if(uniqueInstance == null){
                    uniqueInstance = new CodeforcesWebClient();

                    // Increasing buffer size
                    // References: https://stackoverflow.com/questions/59735951/databufferlimitexception-exceeded-limit-on-max-bytes-to-buffer-webflux-error
                    final int size = 1 << 20;
                    final ExchangeStrategies strategies = ExchangeStrategies.builder()
                            .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                            .build();

                    uniqueInstance.codeforcesWebClient = WebClient.builder()
                            .exchangeStrategies(strategies)
                            .baseUrl("https://codeforces.com/api")
                            .build();
                }
            }
        }
        return uniqueInstance;
    }

    @SuppressWarnings("unchecked")
    public <T> Mono<CFResponse<T>> makeRequest(String path, List<Pair> params, Boolean isAuthorized){
        CFResponse<T> contestCFResponse = new CFResponse<>();
        return (Mono<CFResponse<T>>) this.codeforcesWebClient
                .get()
                .uri(
                        uriBuilder -> {
                            uriBuilder = uriBuilder.path(path);
                            for(Pair p: params){
                                uriBuilder = uriBuilder.queryParam(p.getKey(), p.getValue());
                            }
                            return uriBuilder.build();
                        }
                )
                .retrieve()
                .bodyToMono(contestCFResponse.getClass());
    }

    public Mono<CFResponse<Contest>> getContestList(Boolean gym){
        return this.makeRequest(
                "/contest.list",
                List.of(new Pair("gym", gym.toString())),
                false
        );
    }

    public Mono<CFResponse<User>> getUserInfo(List<String> handles){
        return this.makeRequest(
                "/user.info",
                List.of(new Pair("handles",  String.join(";", handles))),
                false
        );
    }
    public Mono<CFResponse<Comment>> getBlogEntryComments(String blogEntryId){
        return this.makeRequest(
                "/blogEntry.comments",
                List.of(new Pair("blogEntryId",  blogEntryId)),
                false
        );
    }
}
