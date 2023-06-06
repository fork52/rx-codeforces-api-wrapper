package com.fork52.rxcodeforces.api;

import com.fork52.rxcodeforces.api.dto.Contest;
import com.fork52.rxcodeforces.api.dto.CFResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class RxCodeforcesApiApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RxCodeforcesApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Making request");
		CodeforcesWebClient codeforcesWebClient = CodeforcesWebClient.getInstance();
		CFResponse<Contest> contestCFResponse = codeforcesWebClient.getContests(false).block();
		log.info("Completed request.");
	}
}
