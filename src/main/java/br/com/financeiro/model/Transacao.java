package br.com.financeiro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name ="transacoes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;
    @NotBlank(message ="Descrição é obrigatória")
    @Column (nullable = false, length = 100)
    private String descricao;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value ="0.01", message="Valor deve ser maior que zero")
    @Column (nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @NotNull(message= "Data é obrigatória")
    @Column (nullable = false)
    private LocalDate data;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Tipo é obrigatório")
    @Column (nullable = false)
    private TipoTransacao tipo;

    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    @NotNull(message ="Categoria é obrigatória")
    private Categoria categoria;

    @Column(length = 500)
    private String observacao;

}
