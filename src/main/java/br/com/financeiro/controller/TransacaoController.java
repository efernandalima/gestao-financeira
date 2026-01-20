package br.com.financeiro.controller;

import br.com.financeiro.model.TipoTransacao;
import br.com.financeiro.model.Transacao;
import br.com.financeiro.service.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    @Operation(summary = "Listar todas as transações")
    public ResponseEntity<List<Transacao>> listarTodas() {
        return ResponseEntity.ok(transacaoService.listarTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar transação por ID")
    public ResponseEntity<Transacao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(transacaoService.buscarPorId(id));
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Listar transações por tipo")
    public ResponseEntity<List<Transacao>> listarPorTipo(
            @PathVariable TipoTransacao tipo) {
        return ResponseEntity.ok(transacaoService.listarPorTipo(tipo));
    }

    @GetMapping("/periodo")
    @Operation(summary = "Listar transações por período")
    public ResponseEntity<List<Transacao>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(transacaoService.listarPorPeriodo(inicio, fim));
    }

    @GetMapping("/categoria/{categoriaId}")
    @Operation(summary = "Listar transações por categoria")
    public ResponseEntity<List<Transacao>> listarPorCategoria(
            @PathVariable Long categoriaId) {
        return ResponseEntity.ok(transacaoService.listarPorCategoria(categoriaId));
    }

    @GetMapping("/tipo/{tipo}/periodo")
    @Operation(summary = "Listar transações por tipo e período")
    public ResponseEntity<List<Transacao>> listarPorTipoEPeriodo(
            @PathVariable TipoTransacao tipo,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim) {
        return ResponseEntity.ok(
                transacaoService.listarPorTipoEPeriodo(tipo, inicio, fim));
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
    public ResponseEntity<Transacao> criar(@Valid @RequestBody Transacao transacao) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transacaoService.criar(transacao));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar transação")
    public ResponseEntity<Transacao> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Transacao transacao) {
        return ResponseEntity.ok(transacaoService.atualizar(id, transacao));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar transação")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        transacaoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
