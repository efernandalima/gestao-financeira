package br.com.financeiro.service;

import br.com.financeiro.model.TipoTransacao;
import br.com.financeiro.repository.TransacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RelatorioService {
    private final TransacaoService transacaoService;
    @Transactional(readOnly = true)
    public Map<String,Object> gerarRelatorioPorPeriodo(
            LocalDate inicio,
            LocalDate fim){
        BigDecimal totalReceitas = transacaoService.calcularTotalPorTipoEPeriodo(TipoTransacao.DESPESA,inicio,fim);
        BigDecimal totalDespesas = transacaoService.calcularTotalPorTipoEPeriodo(TipoTransacao.DESPESA,inicio,fim);
        BigDecimal saldo = transacaoService.calcularSaldo(inicio,fim);

        Map<String,Object> relatorio = new HashMap<>();
        relatorio.put("periodo",Map.of("inicio",inicio,"fim",fim));
        relatorio.put("totalReceitas",totalReceitas);
        relatorio.put("totalDespesas",totalDespesas);
        relatorio.put("saldo",saldo);
        relatorio.put("receitas", transacaoService
                .listarPorTipoEPeriodo(TipoTransacao.RECEITA, inicio, fim));
        relatorio.put("despesas", transacaoService
                .listarPorTipoEPeriodo(TipoTransacao.DESPESA, inicio, fim));

        return relatorio;
    }

}
