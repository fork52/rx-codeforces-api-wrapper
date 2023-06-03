package com.fork52.rxcodeforces.api;

import com.fork52.rxcodeforces.api.dto.ContestResponse;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RxCodeforcesApiApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(RxCodeforcesApiApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		ContestResponse contestResponse = CodeforcesWebClient.getContests().block();
		System.out.println(contestResponse);
	}
}
