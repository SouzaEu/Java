package fiap.com.br.SentinelTrack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "fiap.com.br.SentinelTrack")
@EnableJpaRepositories(basePackages = "fiap.com.br.SentinelTrack.Infrastructure.repositories")
public class SentinelTrackApplication {

	public static void main(String[] args) {
		SpringApplication.run(SentinelTrackApplication.class, args);
	}

}
