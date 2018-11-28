package io.microsamples.gateway.tracksgateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@SpringBootApplication
@RestController
@Slf4j
public class TracksGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(TracksGatewayApplication.class, args);
    }

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("down-tracks", p -> p.host("down-api.pathfinder.io")
                        .filters(f -> f.setPath("/tracks")
                                .hystrix(c -> c
                                        .setFallbackUri("forward:/fallback")
                                        .setName("fallback")))
                        .uri("http://broken.url.com"))
                .route("busy-tracks", p -> p.host("busy-api.pathfinder.io")
                        .filters(f -> f.setPath("/tracks")
                                .requestRateLimiter().rateLimiter(RedisRateLimiter.class,
                                        c -> c.setBurstCapacity(1).setReplenishRate(1))
                                .configure(c -> c.setKeyResolver(exchange -> Mono.just(exchange.getRequest().getHeaders().getFirst("X-API-KEY")))))
                        .uri("http://localhost:8081"))
                .route("tracks", p -> p.host("limited-api.pathfinder.io")
                        .filters(f -> f.setPath("/v2/tracks"))
                        .uri("http://localhost:8081"))
                .route("all", p -> p.host("api.pathfinder.io")
                        .filters(f -> f.setPath("/tracks"))
                        .uri("http://localhost:8081")).
                        build();
    }

    @RequestMapping("/fallback")
    public List<Track> fallback() {
        Random random = new Random();
        Track track = Track.builder().id(UUID.randomUUID())
                .latitude(random.nextDouble())
                .longitude(random.nextDouble())
                .build();

        log.info("Generating fallback track {}", track);
        return Arrays.asList(track);
    }
}
