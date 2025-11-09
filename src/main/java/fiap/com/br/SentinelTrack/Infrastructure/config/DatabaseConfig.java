package fiap.com.br.SentinelTrack.Infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "br.com.fiap.mottu.Infrastructure.persistence")
public class DatabaseConfig {

}
