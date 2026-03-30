package br.com.financeiro.categoria;

import br.com.financeiro.categoria.model.Categoria;
import br.com.financeiro.shared.exception.ResourceNotFoundException;
import br.com.financeiro.transacao.model.TipoTransacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriaServiceTest {

    private CategoriaRepository categoriaRepository;
    private CategoriaService categoriaService;

    @BeforeEach
    void setUp() {
        categoriaRepository = mock(CategoriaRepository.class);
        categoriaService = new CategoriaService(categoriaRepository);
    }

    @Test
    @DisplayName("Deve listar todas as categorias")
    void listarTodas_DeveRetornarListaCategorias() {
        Categoria cat1 = new Categoria(1L, "Salário", "Receitas fixas", TipoTransacao.RECEITA);
        Categoria cat2 = new Categoria(2L, "Aluguel", "Despesas fixas", TipoTransacao.DESPESA);

        when(categoriaRepository.findAll()).thenReturn(List.of(cat1, cat2));

        List<Categoria> resultado = categoriaService.listarTodas();

        assertEquals(2, resultado.size());
        assertEquals("Salário", resultado.get(0).getNome());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve buscar categoria por ID quando existir")
    void buscarPorId_Existente_DeveRetornarCategoria() {
        Categoria categoria = new Categoria(1L, "Salário", "Receitas fixas", TipoTransacao.RECEITA);
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Categoria resultado = categoriaService.buscarPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Salário", resultado.getNome());
        verify(categoriaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao buscar categoria inexistente")
    void buscarPorId_Inexistente_DeveLancarExcecao() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> categoriaService.buscarPorId(99L)
        );

        assertTrue(ex.getMessage().contains("Categoria não encontrada"));
        verify(categoriaRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve criar nova categoria")
    void criar_DeveSalvarCategoria() {
        Categoria nova = new Categoria(null, "Salário", "Receitas fixas", TipoTransacao.RECEITA);
        Categoria salva = new Categoria(1L, "Salário", "Receitas fixas", TipoTransacao.RECEITA);

        when(categoriaRepository.save(any(Categoria.class))).thenReturn(salva);

        Categoria resultado = categoriaService.criar(nova);

        assertNotNull(resultado.getId());
        assertEquals("Salário", resultado.getNome());

        ArgumentCaptor<Categoria> captor = ArgumentCaptor.forClass(Categoria.class);
        verify(categoriaRepository, times(1)).save(captor.capture());
        assertEquals(TipoTransacao.RECEITA, captor.getValue().getTipo());
    }

    @Test
    @DisplayName("Deve atualizar categoria existente")
    void atualizar_DeveAtualizarCategoria() {
        Categoria existente = new Categoria(1L, "Salário", "Receitas", TipoTransacao.RECEITA);
        Categoria atualizada = new Categoria(null, "Salário CLT", "Receitas fixas", TipoTransacao.RECEITA);

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(existente));
        when(categoriaRepository.save(any(Categoria.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Categoria resultado = categoriaService.atualizar(1L, atualizada);

        assertEquals(1L, resultado.getId());
        assertEquals("Salário CLT", resultado.getNome());
        assertEquals("Receitas fixas", resultado.getDescricao());
        verify(categoriaRepository, times(1)).findById(1L);
        verify(categoriaRepository, times(1)).save(existente);
    }

    @Test
    @DisplayName("Deve deletar categoria existente")
    void deletar_Existente_DeveChamarDelete() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);

        categoriaService.deletar(1L);

        verify(categoriaRepository, times(1)).existsById(1L);
        verify(categoriaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException ao deletar categoria inexistente")
    void deletar_Inexistente_DeveLancarExcecao() {
        when(categoriaRepository.existsById(99L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> categoriaService.deletar(99L)
        );

        assertTrue(ex.getMessage().contains("Categoria não encontrada"));
        verify(categoriaRepository, times(1)).existsById(99L);
        verify(categoriaRepository, never()).deleteById(anyLong());
    }
}