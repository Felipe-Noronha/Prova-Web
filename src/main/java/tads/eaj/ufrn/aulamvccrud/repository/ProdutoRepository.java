package tads.eaj.ufrn.aulamvccrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tads.eaj.ufrn.aulamvccrud.model.Produto;

import java.util.List;
import java.util.Optional;


public interface ProdutoRepository extends JpaRepository<Produto, String> {
      @Query("SELECT p FROM Produto p WHERE p.deleted IS NULL")
      List<Produto> findAllNotDeleted();
      Optional<Produto> findById(String id);
}

