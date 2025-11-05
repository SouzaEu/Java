package fiap.com.br.SentinelTrack.Domain.repositories;

import java.util.List;
import fiap.com.br.SentinelTrack.Domain.models.Patio;

public interface PatioRepository {
    List<Patio> findAll();
    Patio save(Patio patio);
    void deleteById(Long id);
}
