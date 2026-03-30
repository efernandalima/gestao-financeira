package br.com.financeiro.dto;

import br.com.financeiro.transacao.model.TipoTransacao;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoRequestDTO(
        @NotBlank(message = "Descrição é obrigatória")
        String descricao,

        @NotNull(message = "Valor é obrigatório")
        @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
        BigDecimal valor,

        @NotNull(message = "Data é obrigatória")
        LocalDate data,

        @NotNull(message = "Tipo é obrigatório")
        TipoTransacao tipo,

        @NotNull(message = "Categoria é obrigatória")
        Long categoriaId,

        String observacao
) {}