package com.coolgirl.monologue;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class MonologueApplication {
    public static void main(String[] a) { SpringApplication.run(MonologueApplication.class, a); }

    @Bean WebMvcConfigurer cors() {
        return new WebMvcConfigurer() {
            public void addCorsMappings(CorsRegistry r) {
                r.addMapping("/api/**").allowedOrigins("http://localhost:5173").allowedMethods("*");
            }
        };
    }

    @Bean CommandLineRunner seed(QuoteRepository repo) {
        return x -> {
            if (repo.count() > 0) return;
            repo.save(Quote.of("I took a deep breath and listened to the old brag of my heart: I am, I am, I am.", "Sylvia Plath", "The Bell Jar", "https://in.pinterest.com/pin/450852612718744036/", "PUBLISHED", false));
            repo.save(Quote.of("Whatever our souls are made of, his and mine are the same.", "Emily Brontë", "Wuthering Heights", "https://images.unsplash.com/photo-1455390582262-044cdead277a?w=700", "PUBLISHED", false));
            repo.save(Quote.of("I am not afraid of storms, for I am learning how to sail my ship.", "Louisa May Alcott", "Little Women", "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=700", "PUBLISHED", false));
            repo.save(Quote.of("Be soft. Do not let the world make you hard.", "Atticus", "Poem", "https://images.unsplash.com/photo-1518895949257-7621c3c786d7?w=700", "PUBLISHED", false));
            repo.save(Quote.of("I'm the cool girl only on the days I choose to be. Today I'm just writing.", "Me", "Monologue no. 1", "https://images.unsplash.com/photo-1516979187457-637abb4f9353?w=700", "PUBLISHED", true));
        };
    }
}
