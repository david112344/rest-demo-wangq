package com.hero;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;


@EnableJpaAuditing
@SpringBootApplication
public class GithubRestApplication {

    public static void main(String[] args) {
        SpringApplication.run(GithubRestApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of("system");
    }
}
