package br.com.financeiro.service;

import br.com.financeiro.exception.ResourceNotFoundException;
import br.com.financeiro.model.Transacao;
import br.com.financeiro.model.TipoTransacao;
import br.com.financeiro.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransacaoService {

    private final TransacaoRepository transacaoRepository;
    private final CategoriaService categoriaService;

    @Transactional(readOnly = true)
    public List<Transacao> listarTodas() {
        return transacaoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Transacao buscarPorId(Long id) {
        return transacaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Transação não encontrada com ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Transacao> listarPorTipo(TipoTransacao tipo) {
        return transacaoRepository.findByTipo(tipo);
    }

    @Transactional(readOnly = true)
    public List<Transacao> listarPorPeriodo(LocalDate inicio, LocalDate fim) {
        validarPeriodo(inicio, fim);
        return transacaoRepository.findByDataBetween(inicio, fim);
    }

    @Transactional(readOnly = true)
    public List<Transacao> listarPorCategoria(Long categoriaId) {
        categoriaService.buscarPorId(categoriaId);
        return transacaoRepository.findByCategoriaId(categoriaId);
    }

    @Transactional(readOnly = true)
    public List<Transacao> listarPorTipoEPeriodo(
            TipoTransacao tipo,
            LocalDate inicio,
            LocalDate fim) {
        validarPeriodo(inicio, fim);
        return transacaoRepository.findByTipoAndDataBetween(tipo, inicio, fim);
    }

    @Transactional
    public Transacao criar(Transacao transacao) {
        categoriaService.buscarPorId(transacao.getCategoria().getId());

        if (!transacao.getTipo().equals(transacao.getCategoria().getTipo())) {
            throw new IllegalArgumentException(
                    "O tipo da transação deve ser igual ao tipo da categoria");
        }

        return transacaoRepository.save(transacao);
    }

    @Transactional
    public Transacao atualizar(Long id, Transacao transacaoAtualizada) {
        Transacao transacaoExistente = buscarPorId(id);

        categoriaService.buscarPorId(transacaoAtualizada.getCategoria().getId());

        if (!transacaoAtualizada.getTipo().equals(
                transacaoAtualizada.getCategoria().getTipo())) {
            throw new IllegalArgumentException(
                    "O tipo da transação deve ser igual ao tipo da categoria");
        }

        transacaoExistente.setDescricao(transacaoAtualizada.getDescricao());
        transacaoExistente.setValor(transacaoAtualizada.getValor());
        transacaoExistente.setData(transacaoAtualizada.getData());
        transacaoExistente.setTipo(transacaoAtualizada.getTipo());
        transacaoExistente.setCategoria(transacaoAtualizada.getCategoria());
        transacaoExistente.setObservacao(transacaoAtualizada.getObservacao());

        return transacaoRepository.save(transacaoExistente);
    }

    @Transactional
    public void deletar(Long id) {
        if (!transacaoRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Transação não encontrada com ID: " + id);
        }
        transacaoRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotalPorTipoEPeriodo(
            TipoTransacao tipo,
            LocalDate inicio,
            LocalDate fim) {
        validarPeriodo(inicio, fim);
        return transacaoRepository.calcularTotalPorTipoEPeriodo(tipo, inicio, fim);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularSaldo(LocalDate inicio, LocalDate fim) {
        validarPeriodo(inicio, fim);
        return transacaoRepository.calcularSaldo(inicio, fim);
    }

    private void validarPeriodo(LocalDate inicio, LocalDate fim) {
        if (inicio.isAfter(fim)) {
            throw new IllegalArgumentException(
                    "Data inicial não pode ser posterior à data final");
        }
    }
}
