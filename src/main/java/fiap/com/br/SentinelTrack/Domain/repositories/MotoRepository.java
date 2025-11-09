package fiap.com.br.SentinelTrack.Domain.repositories;

import fiap.com.br.SentinelTrack.Domain.models.Moto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MotoRepository extends JpaRepository<Moto, Long> {
    Optional<Moto> findByPlaca(String placa);
    List<Moto> findByPatioId(Long patioId);
    List<Moto> findByStatus(String status);
    List<Moto> findByModeloContainingIgnoreCase(String modelo);
}
