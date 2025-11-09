package fiap.com.br.SentinelTrack.Domain.repositories;

import fiap.com.br.SentinelTrack.Domain.models.Patio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatioRepository extends JpaRepository<Patio, Long> {
    List<Patio> findByNomeContainingIgnoreCase(String nome);
}
