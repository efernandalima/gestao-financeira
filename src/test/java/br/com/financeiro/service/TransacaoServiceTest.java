package br.com.financeiro.service;

import br.com.financeiro.categoria.CategoriaService;
import br.com.financeiro.dto.TransacaoRequestDTO;
import br.com.financeiro.dto.TransacaoResponseDTO;
import br.com.financeiro.categoria.model.Categoria;
import br.com.financeiro.transacao.model.TipoTransacao;
import br.com.financeiro.transacao.model.Transacao;
import br.com.financeiro.transacao.TransacaoRepository;
import br.com.financeiro.transacao.TransacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransacaoServiceTest {

    @Mock
    private TransacaoRepository transacaoRepository;

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private TransacaoService transacaoService;

    private Categoria categoriaReceita;

    @BeforeEach
    void setUp() {
        categoriaReceita = new Categoria();
        categoriaReceita.setId(1L);
        categoriaReceita.setNome("Salário");
        categoriaReceita.setTipo(TipoTransacao.RECEITA);
    }

    @Test
    @DisplayName("Deve criar transação com sucesso quando tipos forem compatíveis")
    void criarDTO_DeveCriarQuandoTiposCompatíveis() {
        TransacaoRequestDTO dto = new TransacaoRequestDTO(
                "Salário Março",
                new BigDecimal("5000.00"),
                LocalDate.of(2026, 3, 30),
                TipoTransacao.RECEITA,
                1L,
                "Observação"
        );

        when(categoriaService.buscarPorId(1L)).thenReturn(categoriaReceita);

        Transacao transacaoSalva = new Transacao();
        transacaoSalva.setId(10L);
        transacaoSalva.setDescricao(dto.descricao());
        transacaoSalva.setValor(dto.valor());
        transacaoSalva.setData(dto.data());
        transacaoSalva.setTipo(dto.tipo());
        transacaoSalva.setCategoria(categoriaReceita);
        transacaoSalva.setObservacao(dto.observacao());

        when(transacaoRepository.save(any(Transacao.class))).thenReturn(transacaoSalva);

        TransacaoResponseDTO response = transacaoService.criarDTO(dto);

        ArgumentCaptor<Transacao> captor = ArgumentCaptor.forClass(Transacao.class);
        verify(transacaoRepository).save(captor.capture());
        Transacao transacaoEnviada = captor.getValue();

        assertThat(transacaoEnviada.getDescricao()).isEqualTo(dto.descricao());
        assertThat(transacaoEnviada.getValor()).isEqualTo(dto.valor());
        assertThat(transacaoEnviada.getTipo()).isEqualTo(TipoTransacao.RECEITA);
        assertThat(transacaoEnviada.getCategoria()).isEqualTo(categoriaReceita);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.descricao()).isEqualTo(dto.descricao());
        assertThat(response.categoriaNome()).isEqualTo("Salário");

        verify(categoriaService).buscarPorId(1L);
        verifyNoMoreInteractions(categoriaService, transacaoRepository);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando tipo da transação e da categoria forem diferentes")
    void criarDTO_DeveLancarExcecaoQuandoTiposIncompatíveis() {
        Categoria categoriaDespesa = new Categoria();
        categoriaDespesa.setId(2L);
        categoriaDespesa.setNome("Aluguel");
        categoriaDespesa.setTipo(TipoTransacao.DESPESA);

        TransacaoRequestDTO dto = new TransacaoRequestDTO(
                "Pagamento Aluguel",
                new BigDecimal("2000.00"),
                LocalDate.of(2026, 3, 30),
                TipoTransacao.RECEITA,
                2L,
                null
        );

        when(categoriaService.buscarPorId(2L)).thenReturn(categoriaDespesa);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> transacaoService.criarDTO(dto)
        );

        assertThat(exception.getMessage())
                .isEqualTo("O tipo da transação deve ser igual ao tipo da categoria");

        verify(categoriaService).buscarPorId(2L);
        verifyNoInteractions(transacaoRepository);
    }
    @Test
    @DisplayName("Deve listar transacoes por periodo retornando DTOs")
    void listarPorPeriodoDTO_DeveRetornarDTOs() {
        LocalDate inicio = LocalDate.of(2026, 3, 1);
        LocalDate fim = LocalDate.of(2026, 3, 31);

        Transacao t1 = new Transacao();
        t1.setId(1L);
        t1.setDescricao("Salario");
        t1.setValor(new BigDecimal("5000.00"));
        t1.setData(LocalDate.of(2026, 3, 5));
        t1.setTipo(TipoTransacao.RECEITA);
        t1.setCategoria(categoriaReceita);

        Transacao t2 = new Transacao();
        t2.setId(2L);
        t2.setDescricao("Bonus");
        t2.setValor(new BigDecimal("800.00"));
        t2.setData(LocalDate.of(2026, 3, 10));
        t2.setTipo(TipoTransacao.RECEITA);
        t2.setCategoria(categoriaReceita);

        when(transacaoRepository.findByDataBetween(inicio, fim))
                .thenReturn(List.of(t1, t2));

        var resultado = transacaoService.listarPorPeriodoDTO(inicio, fim);

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).id()).isEqualTo(1L);
        assertThat(resultado.get(0).categoriaNome()).isEqualTo("Salário");
        assertThat(resultado.get(1).descricao()).isEqualTo("Bonus");

        verify(transacaoRepository).findByDataBetween(inicio, fim);
        verifyNoMoreInteractions(transacaoRepository);
    }
    @Test
    @DisplayName("Deve lancar IllegalArgumentException quando data inicial for depois da final")
    void listarPorPeriodoDTO_DeveLancarQuandoPeriodoInvalido() {
        LocalDate inicio = LocalDate.of(2026, 4, 1);
        LocalDate fim = LocalDate.of(2026, 3, 31);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> transacaoService.listarPorPeriodoDTO(inicio, fim)
        );

        assertThat(exception.getMessage())
                .isEqualTo("Data inicial não pode ser posterior à data final");

        verifyNoInteractions(transacaoRepository);
    }
    @Test
    @DisplayName("Deve calcular saldo chamando o repository corretamente")
    void calcularSaldo_DeveChamarRepository() {
        LocalDate inicio = LocalDate.of(2026, 3, 1);
        LocalDate fim = LocalDate.of(2026, 3, 31);

        when(transacaoRepository.calcularSaldo(inicio, fim))
                .thenReturn(new BigDecimal("3000.00"));

        BigDecimal saldo = transacaoService.calcularSaldo(inicio, fim);

        assertThat(saldo).isEqualTo(new BigDecimal("3000.00"));

        verify(transacaoRepository).calcularSaldo(inicio, fim);
        verifyNoMoreInteractions(transacaoRepository);
    }
}