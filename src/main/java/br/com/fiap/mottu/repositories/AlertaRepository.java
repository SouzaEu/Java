package br.com.fiap.mottu.repositories;

import br.com.fiap.mottu.models.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository para Sistema de Alertas
 * Challenge 2025 - 4º Sprint - Integração IoT
 */
@Repository
public interface AlertaRepository extends JpaRepository<Alerta, String> {

    // Buscar alertas ativos
    List<Alerta> findByAtivoOrderByDataCriacaoDesc(String ativo);

    // Buscar por tipo de alerta
    List<Alerta> findByTipoAlerta(String tipoAlerta);

    // Buscar por nível de severidade
    List<Alerta> findByNivelSeveridade(String nivelSeveridade);

    // Buscar alertas críticos ativos
    @Query("SELECT a FROM Alerta a WHERE a.ativo = 'S' AND a.nivelSeveridade IN ('HIGH', 'CRITICAL') ORDER BY a.dataCriacao DESC")
    List<Alerta> findAlertasCriticosAtivos();

    // Buscar alertas por moto
    List<Alerta> findByPlacaMotoOrderByDataCriacaoDesc(String placaMoto);

    // Buscar alertas por dispositivo
    List<Alerta> findByIdDispositivoOrderByDataCriacaoDesc(String idDispositivo);

    // Buscar alertas por zona
    List<Alerta> findByCodigoZonaOrderByDataCriacaoDesc(String codigoZona);

    // Buscar alertas resolvidos por funcionário
    List<Alerta> findByIdResolvidoPorOrderByDataResolucaoDesc(Long idFuncionario);

    // Buscar alertas por período
    @Query("SELECT a FROM Alerta a WHERE a.dataCriacao BETWEEN :inicio AND :fim ORDER BY a.dataCriacao DESC")
    List<Alerta> findAlertasPorPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    // Contar alertas por status
    @Query("SELECT " +
           "COUNT(CASE WHEN a.ativo = 'S' THEN 1 END) as ativos, " +
           "COUNT(CASE WHEN a.ativo = 'N' THEN 1 END) as resolvidos " +
           "FROM Alerta a")
    Object[] countByStatus();

    // Contar alertas por tipo
    @Query("SELECT a.tipoAlerta, COUNT(a) FROM Alerta a GROUP BY a.tipoAlerta")
    List<Object[]> countByTipo();

    // Contar alertas por severidade
    @Query("SELECT a.nivelSeveridade, COUNT(a) FROM Alerta a WHERE a.ativo = 'S' GROUP BY a.nivelSeveridade")
    List<Object[]> countBySeveridadeAtivos();

    // Buscar alertas não resolvidos há mais de X horas
    @Query("SELECT a FROM Alerta a WHERE a.ativo = 'S' AND a.dataCriacao < :dataLimite")
    List<Alerta> findAlertasNaoResolvidosDesde(@Param("dataLimite") LocalDateTime dataLimite);

    // Buscar alertas IoT ativos
    @Query("SELECT a FROM Alerta a WHERE a.ativo = 'S' AND a.tipoAlerta = 'iot' ORDER BY a.nivelSeveridade DESC, a.dataCriacao DESC")
    List<Alerta> findAlertasIotAtivos();

    // Estatísticas de resolução por funcionário
    @Query("SELECT a.idResolvidoPor, COUNT(a), AVG(EXTRACT(EPOCH FROM (a.dataResolucao - a.dataCriacao))/3600) " +
           "FROM Alerta a WHERE a.dataResolucao IS NOT NULL GROUP BY a.idResolvidoPor")
    List<Object[]> getEstatisticasResolucaoPorFuncionario();

    // Alertas por zona e período
    @Query("SELECT a.codigoZona, COUNT(a) FROM Alerta a WHERE a.dataCriacao >= :inicio GROUP BY a.codigoZona")
    List<Object[]> countAlertasPorZonaDesde(@Param("inicio") LocalDateTime inicio);

    // Tempo médio de resolução por tipo
    @Query("SELECT a.tipoAlerta, AVG(EXTRACT(EPOCH FROM (a.dataResolucao - a.dataCriacao))/3600) " +
           "FROM Alerta a WHERE a.dataResolucao IS NOT NULL GROUP BY a.tipoAlerta")
    List<Object[]> getTempoMedioResolucaoPorTipo();

    // Dashboard - resumo de alertas
    @Query("SELECT " +
           "COUNT(CASE WHEN a.ativo = 'S' THEN 1 END) as ativos, " +
           "COUNT(CASE WHEN a.ativo = 'S' AND a.nivelSeveridade = 'CRITICAL' THEN 1 END) as criticos, " +
           "COUNT(CASE WHEN a.ativo = 'S' AND a.nivelSeveridade = 'HIGH' THEN 1 END) as altos, " +
           "COUNT(CASE WHEN a.ativo = 'S' AND a.tipoAlerta = 'iot' THEN 1 END) as iot, " +
           "COUNT(CASE WHEN a.dataResolucao IS NOT NULL AND DATE(a.dataResolucao) = CURRENT_DATE THEN 1 END) as resolvidosHoje " +
           "FROM Alerta a")
    Object[] getResumoAlertas();

    // Buscar últimos alertas (para dashboard)
    List<Alerta> findTop10ByOrderByDataCriacaoDesc();

    // Verificar se existe alerta ativo para dispositivo
    boolean existsByIdDispositivoAndAtivo(String idDispositivo, String ativo);

    // Verificar se existe alerta ativo para moto
    boolean existsByPlacaMotoAndAtivoAndTipoAlerta(String placaMoto, String ativo, String tipoAlerta);

    // Buscar alertas similares (mesmo tipo, zona e período)
    @Query("SELECT a FROM Alerta a WHERE " +
           "a.tipoAlerta = :tipo AND " +
           "a.codigoZona = :zona AND " +
           "a.dataCriacao >= :inicio AND " +
           "a.idAlerta != :idExcluir")
    List<Alerta> findAlertasSimilares(@Param("tipo") String tipoAlerta,
                                     @Param("zona") String zona,
                                     @Param("inicio") LocalDateTime inicio,
                                     @Param("idExcluir") String idExcluir);
}
