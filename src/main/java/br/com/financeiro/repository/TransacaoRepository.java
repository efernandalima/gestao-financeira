package br.com.financeiro.repository;

import br.com.financeiro.model.Transacao;
import br.com.financeiro.model.TipoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {

    List<Transacao> findByTipo(TipoTransacao tipo);

    List<Transacao> findByDataBetween(LocalDate inicio, LocalDate fim);

    List<Transacao> findByCategoriaId(Long categoriaId);

    List<Transacao> findByTipoAndDataBetween(
            TipoTransacao tipo,
            LocalDate inicio,
            LocalDate fim
    );

    @Query("SELECT COALESCE(SUM(t.valor), 0) FROM Transacao t " +
            "WHERE t.tipo = :tipo AND t.data BETWEEN :inicio AND :fim")
    BigDecimal calcularTotalPorTipoEPeriodo(
            @Param("tipo") TipoTransacao tipo,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

    @Query("SELECT COALESCE(SUM(CASE WHEN t.tipo = 'RECEITA' THEN t.valor ELSE -t.valor END), 0) " +
            "FROM Transacao t WHERE t.data BETWEEN :inicio AND :fim")
    BigDecimal calcularSaldo(
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );
}
