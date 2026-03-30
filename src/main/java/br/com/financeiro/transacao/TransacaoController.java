package br.com.financeiro.transacao;

import br.com.financeiro.dto.TransacaoRequestDTO;
import br.com.financeiro.dto.TransacaoResponseDTO;
import br.com.financeiro.transacao.model.TipoTransacao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transacoes")
@RequiredArgsConstructor
@Tag(name = "Transações", description = "Gerenciamento de receitas e despesas")
public class TransacaoController {

    private final TransacaoService transacaoService;

    @GetMapping
    @Operation(summary = "Listar transações paginadas")
    public ResponseEntity<Page<TransacaoResponseDTO>> listarTodas(
            @PageableDefault(size = 10, sort = "data", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return ResponseEntity.ok(transacaoService.listarTodasPaginadoDTO(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar transação por ID")
    public ResponseEntity<TransacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(transacaoService.buscarPorIdDTO(id));
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar transações por tipo")
    public ResponseEntity<List<TransacaoResponseDTO>> listarPorTipo(
            @PathVariable TipoTransacao tipo) {
        return ResponseEntity.ok(transacaoService.listarPorTipoDTO(tipo));
    }

    @GetMapping("/periodo")
    @Operation(summary = "Listar transações por período")
    public ResponseEntity<List<TransacaoResponseDTO>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(transacaoService.listarPorPeriodoDTO(inicio, fim));
    }

    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Listar transações por categoria")
    public ResponseEntity<List<TransacaoResponseDTO>> listarPorCategoria(
            @PathVariable Long categoriaId) {
        return ResponseEntity.ok(transacaoService.listarPorCategoriaDTO(categoriaId));
    }

    @GetMapping("/tipo/{tipo}/periodo")
    @Operation(summary = "Listar transações por tipo e período")
    public ResponseEntity<List<TransacaoResponseDTO>> listarPorTipoEPeriodo(
            @PathVariable TipoTransacao tipo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(
                transacaoService.listarPorTipoEPeriodoDTO(tipo, inicio, fim));
    }

    @GetMapping("/total/{tipo}")
    @Operation(summary = "Calcular total por tipo e período")
    public ResponseEntity<BigDecimal> calcularTotal(
            @PathVariable TipoTransacao tipo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(
                transacaoService.calcularTotalPorTipoEPeriodo(tipo, inicio, fim));
    }

    @GetMapping("/saldo")
    @Operation(summary = "Calcular saldo no período")
    public ResponseEntity<BigDecimal> calcularSaldo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(transacaoService.calcularSaldo(inicio, fim));
    }

    @PostMapping
    @Operation(summary = "Criar nova transação")
    public ResponseEntity<TransacaoResponseDTO> criar(
            @Valid @RequestBody TransacaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transacaoService.criarDTO(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar transação")
    public ResponseEntity<TransacaoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TransacaoRequestDTO dto) {
        return ResponseEntity.ok(transacaoService.atualizarDTO(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar transação")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        transacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}