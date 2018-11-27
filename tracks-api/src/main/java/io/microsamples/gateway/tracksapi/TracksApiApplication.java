package io.microsamples.gateway.tracksapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static io.github.benas.randombeans.api.EnhancedRandom.randomStreamOf;

@SpringBootApplication
@RestController
public class TracksApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TracksApiApplication.class, args);
	}

	@RequestMapping("/tracks")
	public List<Track> tracks(){
		return randomStreamOf(1000, Track.class).collect(Collectors.toList());
	}
	@RequestMapping("/v2/tracks")
	public List<Track> v2tracks(){
		return randomStreamOf(3, Track.class).collect(Collectors.toList());
	}
}
