package fiap.com.br.SentinelTrack.Application.services;

import fiap.com.br.SentinelTrack.Api.exception.PatioNotFoundException;
import fiap.com.br.SentinelTrack.Application.dto.CreatePatioDTO;
import fiap.com.br.SentinelTrack.Application.dto.PatioDTO;
import fiap.com.br.SentinelTrack.Application.dto.UpdatePatioDTO;
import fiap.com.br.SentinelTrack.Application.mapper.PatioMapperV2;
import fiap.com.br.SentinelTrack.Domain.models.Patio;
import fiap.com.br.SentinelTrack.Domain.repositories.PatioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários profissionais para PatioServiceV2
 * Demonstra boas práticas de teste com Mockito e AssertJ
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PatioServiceV2 - Testes Unitários")
class PatioServiceV2Test {

    @Mock
    private PatioRepository repository;

    @Mock
    private PatioMapperV2 mapper;

    @InjectMocks
    private PatioServiceV2 patioService;

    private Patio patioEntity;
    private PatioDTO patioDTO;
    private CreatePatioDTO createPatioDTO;
    private UpdatePatioDTO updatePatioDTO;

    @BeforeEach
    void setUp() {
        // Arrange - Dados de teste
        patioEntity = new Patio();
        patioEntity.setId(1L);
        patioEntity.setNome("Pátio Teste");
        patioEntity.setEndereco("Rua Teste, 123");
        patioEntity.setAreaM2(new BigDecimal("1000.00"));
        patioEntity.setIdLocalidade(1L);

        patioDTO = new PatioDTO();
        patioDTO.setId(1L);
        patioDTO.setNome("Pátio Teste");
        patioDTO.setEndereco("Rua Teste, 123");
        patioDTO.setAreaM2(new BigDecimal("1000.00"));
        patioDTO.setIdLocalidade(1L);

        createPatioDTO = new CreatePatioDTO();
        createPatioDTO.setNome("Pátio Novo");
        createPatioDTO.setEndereco("Rua Nova, 456");
        createPatioDTO.setAreaM2(new BigDecimal("2000.00"));
        createPatioDTO.setIdLocalidade(2L);

        updatePatioDTO = new UpdatePatioDTO();
        updatePatioDTO.setNome("Pátio Atualizado");
        updatePatioDTO.setEndereco("Rua Atualizada, 789");
        updatePatioDTO.setAreaM2(new BigDecimal("3000.00"));
        updatePatioDTO.setIdLocalidade(3L);
    }

    @Test
    @DisplayName("Deve listar todos os pátios com sucesso")
    void deveListarTodosOsPatios() {
        // Arrange
        List<Patio> patios = Arrays.asList(patioEntity);
        when(repository.findAll()).thenReturn(patios);
        when(mapper.toDTO(patioEntity)).thenReturn(patioDTO);

        // Act
        List<PatioDTO> resultado = patioService.listarTodos();

        // Assert
        assertThat(resultado)
            .isNotNull()
            .hasSize(1)
            .containsExactly(patioDTO);

        verify(repository).findAll();
        verify(mapper).toDTO(patioEntity);
    }

    @Test
    @DisplayName("Deve buscar pátio por ID com sucesso")
    void deveBuscarPatioPorId() {
        // Arrange
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(patioEntity));
        when(mapper.toDTO(patioEntity)).thenReturn(patioDTO);

        // Act
        PatioDTO resultado = patioService.buscarPorId(id);

        // Assert
        assertThat(resultado)
            .isNotNull()
            .isEqualTo(patioDTO);

        verify(repository).findById(id);
        verify(mapper).toDTO(patioEntity);
    }

    @Test
    @DisplayName("Deve lançar exceção quando pátio não encontrado por ID")
    void deveLancarExcecaoQuandoPatioNaoEncontrado() {
        // Arrange
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> patioService.buscarPorId(id))
            .isInstanceOf(PatioNotFoundException.class)
            .hasMessage("Pátio não encontrado com ID: " + id);

        verify(repository).findById(id);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Deve criar novo pátio com sucesso")
    void deveCriarNovoPatioComSucesso() {
        // Arrange
        Patio novoPatioEntity = new Patio();
        novoPatioEntity.setId(2L);
        
        when(mapper.toEntity(createPatioDTO)).thenReturn(patioEntity);
        when(repository.save(patioEntity)).thenReturn(novoPatioEntity);
        when(mapper.toDTO(novoPatioEntity)).thenReturn(patioDTO);

        // Act
        PatioDTO resultado = patioService.criar(createPatioDTO);

        // Assert
        assertThat(resultado)
            .isNotNull()
            .isEqualTo(patioDTO);

        verify(mapper).toEntity(createPatioDTO);
        verify(repository).save(patioEntity);
        verify(mapper).toDTO(novoPatioEntity);
    }

    @Test
    @DisplayName("Deve atualizar pátio existente com sucesso")
    void deveAtualizarPatioExistente() {
        // Arrange
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(patioEntity));
        when(repository.save(patioEntity)).thenReturn(patioEntity);
        when(mapper.toDTO(patioEntity)).thenReturn(patioDTO);

        // Act
        PatioDTO resultado = patioService.atualizar(id, updatePatioDTO);

        // Assert
        assertThat(resultado)
            .isNotNull()
            .isEqualTo(patioDTO);

        verify(repository).findById(id);
        verify(mapper).updateEntity(patioEntity, updatePatioDTO);
        verify(repository).save(patioEntity);
        verify(mapper).toDTO(patioEntity);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar pátio inexistente")
    void deveLancarExcecaoAoAtualizarPatioInexistente() {
        // Arrange
        Long id = 999L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> patioService.atualizar(id, updatePatioDTO))
            .isInstanceOf(PatioNotFoundException.class)
            .hasMessage("Pátio não encontrado com ID: " + id);

        verify(repository).findById(id);
        verifyNoMoreInteractions(repository);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Deve deletar pátio existente com sucesso")
    void deveDeletarPatioExistente() {
        // Arrange
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        // Act
        assertThatCode(() -> patioService.deletar(id))
            .doesNotThrowAnyException();

        // Assert
        verify(repository).existsById(id);
        verify(repository).deleteById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar pátio inexistente")
    void deveLancarExcecaoAoDeletarPatioInexistente() {
        // Arrange
        Long id = 999L;
        when(repository.existsById(id)).thenReturn(false);

        // Act & Assert
        assertThatThrownBy(() -> patioService.deletar(id))
            .isInstanceOf(PatioNotFoundException.class)
            .hasMessage("Pátio não encontrado com ID: " + id);

        verify(repository).existsById(id);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve buscar pátios por nome com sucesso")
    void deveBuscarPatiosPorNome() {
        // Arrange
        String nome = "Teste";
        List<Patio> patios = Arrays.asList(patioEntity);
        when(repository.findByNomeContainingIgnoreCase(nome)).thenReturn(patios);
        when(mapper.toDTO(patioEntity)).thenReturn(patioDTO);

        // Act
        List<PatioDTO> resultado = patioService.buscarPorNome(nome);

        // Assert
        assertThat(resultado)
            .isNotNull()
            .hasSize(1)
            .containsExactly(patioDTO);

        verify(repository).findByNomeContainingIgnoreCase(nome);
        verify(mapper).toDTO(patioEntity);
    }

    @Test
    @DisplayName("Deve retornar todos os pátios quando nome for vazio")
    void deveRetornarTodosQuandoNomeVazio() {
        // Arrange
        String nome = "";
        List<Patio> patios = Arrays.asList(patioEntity);
        when(repository.findAll()).thenReturn(patios);
        when(mapper.toDTO(patioEntity)).thenReturn(patioDTO);

        // Act
        List<PatioDTO> resultado = patioService.buscarPorNome(nome);

        // Assert
        assertThat(resultado)
            .isNotNull()
            .hasSize(1);

        verify(repository).findAll();
        verify(repository, never()).findByNomeContainingIgnoreCase(anyString());
    }

    @Test
    @DisplayName("Deve verificar se pátio existe")
    void deveVerificarSePatioExiste() {
        // Arrange
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);

        // Act
        boolean existe = patioService.existe(id);

        // Assert
        assertThat(existe).isTrue();
        verify(repository).existsById(id);
    }

    @Test
    @DisplayName("Deve contar total de pátios")
    void deveContarTotalDePatios() {
        // Arrange
        long total = 5L;
        when(repository.count()).thenReturn(total);

        // Act
        long resultado = patioService.contarTodos();

        // Assert
        assertThat(resultado).isEqualTo(total);
        verify(repository).count();
    }
}
