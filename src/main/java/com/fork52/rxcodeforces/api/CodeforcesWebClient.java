package com.fork52.rxcodeforces.api;

import com.fork52.rxcodeforces.api.dto.Contest;
import com.fork52.rxcodeforces.api.dto.CFResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class CodeforcesWebClient {
    final private static WebClient codeforcesWebClient;

    static {
        // Increasing buffer size
        // References: https://stackoverflow.com/questions/59735951/databufferlimitexception-exceeded-limit-on-max-bytes-to-buffer-webflux-error
        final int size = 1 << 24;
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                .build();

        codeforcesWebClient = WebClient.builder()
                .exchangeStrategies(strategies)
                .baseUrl("https://codeforces.com/api")
                .build();
    }

    @SuppressWarnings("unchecked")
    public static Mono<CFResponse<Contest>> getContests(Boolean gym){
        CFResponse<Contest> contestCFResponse = new CFResponse<>();
        return (Mono<CFResponse<Contest>>) CodeforcesWebClient.codeforcesWebClient
                .get()
                .uri(
                    uriBuilder -> uriBuilder
                        .path("/contest.list")
                        .queryParam("gym", gym)
                        .build()
                )
                .retrieve()
                .bodyToMono(contestCFResponse.getClass());
    }
}
