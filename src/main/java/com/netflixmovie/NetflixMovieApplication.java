package com.netflixmovie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NetflixMovieApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetflixMovieApplication.class, args);
	}
}
