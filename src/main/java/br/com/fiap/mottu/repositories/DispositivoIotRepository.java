package br.com.fiap.mottu.repositories;

import br.com.fiap.mottu.models.DispositivoIot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository para Dispositivos IoT
 * Challenge 2025 - 4º Sprint - Integração IoT
 */
@Repository
public interface DispositivoIotRepository extends JpaRepository<DispositivoIot, String> {

    // Buscar por tipo de dispositivo
    List<DispositivoIot> findByTipoDispositivo(String tipoDispositivo);

    // Buscar por status
    List<DispositivoIot> findByStatus(String status);

    // Buscar dispositivos online
    List<DispositivoIot> findByStatusOrderByNomeDispositivo(String status);

    // Buscar por zona
    List<DispositivoIot> findByCodigoZona(String codigoZona);

    // Buscar dispositivos por tipo e status
    List<DispositivoIot> findByTipoDispositivoAndStatus(String tipoDispositivo, String status);

    // Buscar dispositivos offline há mais de X minutos
    @Query("SELECT d FROM DispositivoIot d WHERE d.dataUltimaComunicacao < :dataLimite")
    List<DispositivoIot> findDispositivosOfflineDesde(@Param("dataLimite") LocalDateTime dataLimite);

    // Buscar sensores por zona
    @Query("SELECT d FROM DispositivoIot d WHERE d.codigoZona = :zona AND d.tipoDispositivo LIKE 'sensor_%'")
    List<DispositivoIot> findSensoresPorZona(@Param("zona") String zona);

    // Buscar atuadores por zona
    @Query("SELECT d FROM DispositivoIot d WHERE d.codigoZona = :zona AND d.tipoDispositivo LIKE 'atuador_%'")
    List<DispositivoIot> findAtuadoresPorZona(@Param("zona") String zona);

    // Contar dispositivos por status
    @Query("SELECT d.status, COUNT(d) FROM DispositivoIot d GROUP BY d.status")
    List<Object[]> countByStatus();

    // Contar dispositivos por tipo
    @Query("SELECT d.tipoDispositivo, COUNT(d) FROM DispositivoIot d GROUP BY d.tipoDispositivo")
    List<Object[]> countByTipo();

    // Buscar dispositivos próximos a uma coordenada
    @Query("SELECT d FROM DispositivoIot d WHERE " +
           "d.latitude IS NOT NULL AND d.longitude IS NOT NULL AND " +
           "ABS(d.latitude - :lat) < :raio AND ABS(d.longitude - :lon) < :raio")
    List<DispositivoIot> findDispositivosProximos(@Param("lat") Double latitude, 
                                                  @Param("lon") Double longitude, 
                                                  @Param("raio") Double raio);

    // Buscar dispositivo por nome (case insensitive)
    Optional<DispositivoIot> findByNomeDispositivoIgnoreCase(String nomeDispositivo);

    // Verificar se existe dispositivo ativo na zona
    boolean existsByCodigoZonaAndStatus(String codigoZona, String status);

    // Buscar últimos dispositivos criados
    List<DispositivoIot> findTop10ByOrderByDataCriacaoDesc();

    // Atualizar última comunicação
    @Query("UPDATE DispositivoIot d SET d.dataUltimaComunicacao = :data, d.status = 'ONLINE' WHERE d.idDispositivo = :id")
    void atualizarUltimaComunicacao(@Param("id") String idDispositivo, @Param("data") LocalDateTime data);
}
