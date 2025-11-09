package fiap.com.br.SentinelTrack.Infrastructure.repositories;

import java.util.List;
import org.springframework.stereotype.Repository;
import fiap.com.br.SentinelTrack.Domain.models.Patio;
import fiap.com.br.SentinelTrack.Domain.repositories.PatioRepository;

@Repository
public class PatioRepositoryImpl implements PatioRepository {

    private final IPatioRepository jpaRepository;

    public PatioRepositoryImpl(IPatioRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Patio> findAll() {
        return jpaRepository.findAll();
    }

    @Override
    public Patio save(Patio patio) {
        return jpaRepository.save(patio);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }
}
