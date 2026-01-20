package br.com.financeiro.repository;

import br.com.financeiro.model.Categoria;
import br.com.financeiro.model.TipoTransacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Optional<Categoria> findByNome(String nome);
    List<Categoria> findByTipo(TipoTransacao tipo);
    boolean existsByNome(String nome);

}
