package com.fork52.rxcodeforcesapi;

import com.fork52.rxcodeforcesapi.dto.Contest;
import com.fork52.rxcodeforcesapi.dto.ContestResponse;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

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

    public static Mono<ContestResponse> getContests(){
        return CodeforcesWebClient.codeforcesWebClient
                .get()
                .uri("/contest.list")
                .retrieve()
                .bodyToMono(ContestResponse.class);
    }
}
