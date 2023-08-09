package com.ssafy.stargate;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.LocalDateTime;
import java.util.TimeZone;

@SpringBootApplication
@EnableJpaAuditing
public class StargateApplication {
	public static void main(String[] args) {
		SpringApplication.run(StargateApplication.class, args);

		LocalDateTime now = LocalDateTime.now();
		System.out.println("현재 시간" + now);
	}

	@PostConstruct
	public void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
	}
}
