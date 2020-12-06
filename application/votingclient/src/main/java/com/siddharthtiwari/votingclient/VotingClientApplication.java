package com.siddharthtiwari.votingclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VotingClientApplication {

	private static final String TOPIC_NAME = "votes";

	public static void main(String[] args) {
		new SpringApplicationBuilder(VotingClientApplication.class)
				.web(WebApplicationType.NONE)
				.run(args);
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

}
