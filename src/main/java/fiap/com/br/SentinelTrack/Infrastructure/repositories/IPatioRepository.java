package fiap.com.br.SentinelTrack.Infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import fiap.com.br.SentinelTrack.Domain.models.Patio;

public interface IPatioRepository extends JpaRepository<Patio, Long> {

}
