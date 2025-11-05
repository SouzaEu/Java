package fiap.com.br.SentinelTrack.Application.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fiap.com.br.SentinelTrack.Domain.models.Patio;
import fiap.com.br.SentinelTrack.Domain.repositories.PatioRepository;

@Service
public class PatioService {

    private final PatioRepository repository;

    public PatioService(PatioRepository repository) {
        this.repository = repository;
    }

    public List<Patio> listar() {
        return repository.findAll();
    }

    public Patio salvar(Patio patio) {
        return repository.save(patio);
    }

    public void deletar(Long id) {
        repository.deleteById(id);
    }
}
