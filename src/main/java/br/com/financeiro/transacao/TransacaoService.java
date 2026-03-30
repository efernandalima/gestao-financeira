package br.com.financeiro.transacao;

import br.com.financeiro.categoria.CategoriaService;
import br.com.financeiro.dto.TransacaoRequestDTO;
import br.com.financeiro.dto.TransacaoResponseDTO;
import br.com.financeiro.shared.exception.ResourceNotFoundException;
import br.com.financeiro.categoria.model.Categoria;
import br.com.financeiro.transacao.model.TipoTransacao;
import br.com.financeiro.transacao.model.Transacao;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // ---------- MÉTODOS ANTIGOS (entity) – ainda usados internamente ----------

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

        validarCompatibilidadeTipo(transacao.getTipo(),
                transacao.getCategoria().getTipo());

        return transacaoRepository.save(transacao);
    }

    @Transactional
    public Transacao atualizar(Long id, Transacao transacaoAtualizada) {
        Transacao transacaoExistente = buscarPorId(id);

        categoriaService.buscarPorId(transacaoAtualizada.getCategoria().getId());

        validarCompatibilidadeTipo(transacaoAtualizada.getTipo(),
                transacaoAtualizada.getCategoria().getTipo());

        transacaoExistente.setDescricao(transacaoAtualizada.getDescricao());
        transacaoExistente.setValor(transacaoAtualizada.getValor());
        transacaoExistente.setData(transacaoAtualizada.getData());
        transacaoExistente.setTipo(transacaoAtualizada.getTipo());
        transacaoExistente.setCategoria(transacaoAtualizada.getCategoria());
        transacaoExistente.setObservacao(transacaoAtualizada.getObservacao());

        return transacaoRepository.save(transacaoExistente);
    }

    // ---------- NOVOS MÉTODOS (DTO) – usados pelo controller ----------

    @Transactional(readOnly = true)
    public Page<TransacaoResponseDTO> listarTodasPaginadoDTO(Pageable pageable) {
        return transacaoRepository.findAll(pageable)
                .map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> listarTodasDTO() {
        return listarTodas().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public TransacaoResponseDTO buscarPorIdDTO(Long id) {
        return toResponseDTO(buscarPorId(id));
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> listarPorTipoDTO(TipoTransacao tipo) {
        return listarPorTipo(tipo).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> listarPorPeriodoDTO(LocalDate inicio, LocalDate fim) {
        return listarPorPeriodo(inicio, fim).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> listarPorCategoriaDTO(Long categoriaId) {
        return listarPorCategoria(categoriaId).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> listarPorTipoEPeriodoDTO(
            TipoTransacao tipo, LocalDate inicio, LocalDate fim) {
        return listarPorTipoEPeriodo(tipo, inicio, fim).stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional
    public TransacaoResponseDTO criarDTO(TransacaoRequestDTO dto) {
        Transacao transacao = toEntity(dto);

        validarCompatibilidadeTipo(transacao.getTipo(),
                transacao.getCategoria().getTipo());

        Transacao salva = transacaoRepository.save(transacao);
        return toResponseDTO(salva);
    }

    @Transactional
    public TransacaoResponseDTO atualizarDTO(Long id, TransacaoRequestDTO dto) {
        Transacao existente = buscarPorId(id);

        updateEntityFromDTO(dto, existente);

        validarCompatibilidadeTipo(existente.getTipo(),
                existente.getCategoria().getTipo());

        Transacao salva = transacaoRepository.save(existente);
        return toResponseDTO(salva);
    }

    // ---------- CÁLCULOS E VALIDAÇÕES ----------

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

    private void validarCompatibilidadeTipo(TipoTransacao tipoTransacao,
                                            TipoTransacao tipoCategoria) {
        if (!tipoTransacao.equals(tipoCategoria)) {
            throw new IllegalArgumentException(
                    "O tipo da transação deve ser igual ao tipo da categoria");
        }
    }

    // ---------- MAPEAMENTO ENTITY <-> DTO ----------

    private TransacaoResponseDTO toResponseDTO(Transacao entity) {
        return new TransacaoResponseDTO(
                entity.getId(),
                entity.getDescricao(),
                entity.getValor(),
                entity.getData(),
                entity.getTipo(),
                entity.getCategoria().getNome(),
                entity.getObservacao()
        );
    }

    private Transacao toEntity(TransacaoRequestDTO dto) {
        Categoria categoria = categoriaService.buscarPorId(dto.categoriaId());

        Transacao transacao = new Transacao();
        transacao.setDescricao(dto.descricao());
        transacao.setValor(dto.valor());
        transacao.setData(dto.data());
        transacao.setTipo(dto.tipo());
        transacao.setObservacao(dto.observacao());
        transacao.setCategoria(categoria);

        return transacao;
    }

    private void updateEntityFromDTO(TransacaoRequestDTO dto, Transacao entity) {
        Categoria categoria = categoriaService.buscarPorId(dto.categoriaId());

        entity.setDescricao(dto.descricao());
        entity.setValor(dto.valor());
        entity.setData(dto.data());
        entity.setTipo(dto.tipo());
        entity.setObservacao(dto.observacao());
        entity.setCategoria(categoria);
    }
}