package br.com.fiap.mottu.repositories;

import br.com.fiap.mottu.models.LocalizacaoMoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para Localização das Motos
 * Challenge 2025 - 4º Sprint - Integração IoT
 */
@Repository
public interface LocalizacaoMotoRepository extends JpaRepository<LocalizacaoMoto, Long> {

    // Buscar por placa da moto
    Optional<LocalizacaoMoto> findByPlacaMoto(String placaMoto);

    // Buscar motos por status
    List<LocalizacaoMoto> findByStatusMoto(String statusMoto);

    // Buscar motos disponíveis
    List<LocalizacaoMoto> findByStatusMotoOrderByNivelBateriaDesc(String statusMoto);

    // Buscar por zona
    List<LocalizacaoMoto> findByCodigoZona(String codigoZona);

    // Buscar por setor
    List<LocalizacaoMoto> findBySetor(String setor);

    // Buscar motos em uso por usuário
    List<LocalizacaoMoto> findByCpfUsuarioEmUso(String cpfUsuario);

    // Buscar motos com bateria baixa
    @Query("SELECT l FROM LocalizacaoMoto l WHERE l.nivelBateria < :nivel")
    List<LocalizacaoMoto> findMotosComBateriaBaixa(@Param("nivel") Integer nivelBateria);

    // Buscar motos com bateria crítica
    @Query("SELECT l FROM LocalizacaoMoto l WHERE l.nivelBateria < 10")
    List<LocalizacaoMoto> findMotosComBateriaCritica();

    // Contar motos por status
    @Query("SELECT l.statusMoto, COUNT(l) FROM LocalizacaoMoto l GROUP BY l.statusMoto")
    List<Object[]> countByStatus();

    // Contar motos por zona
    @Query("SELECT l.codigoZona, COUNT(l) FROM LocalizacaoMoto l GROUP BY l.codigoZona")
    List<Object[]> countByZona();

    // Buscar motos não atualizadas há X tempo
    @Query("SELECT l FROM LocalizacaoMoto l WHERE l.dataUltimaAtualizacao < :dataLimite")
    List<LocalizacaoMoto> findMotosNaoAtualizadasDesde(@Param("dataLimite") LocalDateTime dataLimite);

    // Buscar motos próximas a uma coordenada
    @Query("SELECT l FROM LocalizacaoMoto l WHERE " +
           "l.latitude IS NOT NULL AND l.longitude IS NOT NULL AND " +
           "ABS(l.latitude - :lat) < :raio AND ABS(l.longitude - :lon) < :raio")
    List<LocalizacaoMoto> findMotosProximas(@Param("lat") Double latitude, 
                                           @Param("lon") Double longitude, 
                                           @Param("raio") Double raio);

    // Buscar motos disponíveis em uma zona
    @Query("SELECT l FROM LocalizacaoMoto l WHERE l.codigoZona = :zona AND l.statusMoto = 'DISPONIVEL' ORDER BY l.nivelBateria DESC")
    List<LocalizacaoMoto> findMotosDisponiveisNaZona(@Param("zona") String zona);

    // Buscar estatísticas de bateria por zona
    @Query("SELECT l.codigoZona, AVG(l.nivelBateria), MIN(l.nivelBateria), MAX(l.nivelBateria) " +
           "FROM LocalizacaoMoto l WHERE l.nivelBateria IS NOT NULL GROUP BY l.codigoZona")
    List<Object[]> getEstatisticasBateriaPorZona();

    // Verificar se vaga está ocupada
    boolean existsByCodigoVagaAndStatusMoto(String codigoVaga, String statusMoto);

    // Buscar motos por andar
    List<LocalizacaoMoto> findByAndar(Integer andar);

    // Buscar última atualização por zona
    @Query("SELECT l.codigoZona, MAX(l.dataUltimaAtualizacao) FROM LocalizacaoMoto l GROUP BY l.codigoZona")
    List<Object[]> getUltimaAtualizacaoPorZona();

    // Dashboard - resumo geral
    @Query("SELECT " +
           "COUNT(CASE WHEN l.statusMoto = 'DISPONIVEL' THEN 1 END) as disponiveis, " +
           "COUNT(CASE WHEN l.statusMoto = 'EM_USO' THEN 1 END) as emUso, " +
           "COUNT(CASE WHEN l.statusMoto = 'MANUTENCAO' THEN 1 END) as manutencao, " +
           "COUNT(CASE WHEN l.nivelBateria < 20 THEN 1 END) as bateriaBaixa, " +
           "AVG(l.nivelBateria) as mediaBateria " +
           "FROM LocalizacaoMoto l")
    Object[] getResumoGeral();

    // Buscar motos para manutenção (bateria baixa ou não atualizadas)
    @Query("SELECT l FROM LocalizacaoMoto l WHERE " +
           "l.nivelBateria < 15 OR " +
           "l.dataUltimaAtualizacao < :dataLimite OR " +
           "l.statusMoto = 'MANUTENCAO'")
    List<LocalizacaoMoto> findMotasParaManutencao(@Param("dataLimite") LocalDateTime dataLimite);
}
