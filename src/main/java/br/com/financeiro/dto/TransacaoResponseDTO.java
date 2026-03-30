package br.com.financeiro.dto;

import br.com.financeiro.transacao.model.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoResponseDTO(
        Long id,
        String descricao,
        BigDecimal valor,
        LocalDate data,
        TipoTransacao tipo,
        String categoriaNome,
        String observacao
) {}