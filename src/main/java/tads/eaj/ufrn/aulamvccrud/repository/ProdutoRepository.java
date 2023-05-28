package tads.eaj.ufrn.aulamvccrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tads.eaj.ufrn.aulamvccrud.model.Produto;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, String> {
      List<Produto> findProdutoById(String id);
}
