package br.com.financeiro.service;

import br.com.financeiro.exception.ResourceNotFoundException;
import br.com.financeiro.model.Categoria;
import br.com.financeiro.model.TipoTransacao;
import br.com.financeiro.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<Categoria> listarTodas() {
        return categoriaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Categoria buscarPorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria não encontrada com ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Categoria> listarPorTipo(TipoTransacao tipo) {
        return categoriaRepository.findByTipo(tipo);
    }

    @Transactional
    public Categoria criar(Categoria categoria) {
        if (categoriaRepository.existsByNome(categoria.getNome())) {
            throw new IllegalArgumentException(
                    "Já existe uma categoria com o nome: " + categoria.getNome());
        }
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public Categoria atualizar(Long id, Categoria categoriaAtualizada) {
        Categoria categoriaExistente = buscarPorId(id);

        if (!categoriaExistente.getNome().equals(categoriaAtualizada.getNome())
                && categoriaRepository.existsByNome(categoriaAtualizada.getNome())) {
            throw new IllegalArgumentException(
                    "Já existe uma categoria com o nome: " + categoriaAtualizada.getNome());
        }

        categoriaExistente.setNome(categoriaAtualizada.getNome());
        categoriaExistente.setDescricao(categoriaAtualizada.getDescricao());
        categoriaExistente.setTipo(categoriaAtualizada.getTipo());

        return categoriaRepository.save(categoriaExistente);
    }

    @Transactional
    public void deletar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Categoria não encontrada com ID: " + id);
        }
        categoriaRepository.deleteById(id);
    }
}
