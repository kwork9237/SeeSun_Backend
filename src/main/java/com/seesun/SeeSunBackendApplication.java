package com.seesun;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class SeeSunBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeeSunBackendApplication.class, args);
	}

	// order 테이블에서 시간관련 부분이 기본 영국시간대로 설정되어있어 현재 한국 시간으로 변경하기 위해 작성함.
	@PostConstruct
	public void started() {
		// 어플리케이션 실행 시, 강제로 시간대를 'Asia/Seoul'로 맞춤
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
		System.out.println("현재 시간: " + new java.util.Date()); // 로그로 한국 시간 찍히는지 확인
	}

}
