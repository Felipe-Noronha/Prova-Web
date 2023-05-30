package tads.eaj.ufrn.aulamvccrud.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tads.eaj.ufrn.aulamvccrud.model.Produto;

import java.util.List;
import java.util.Optional;


public interface ProdutoRepository extends JpaRepository<Produto, String> {
      List<Produto> findAll();
      Optional<Produto> findById(String id);
}


/* 
public interface ProdutoRepository extends JpaRepository<Produto, String> {
      List<Produto> findAllNotDeleted();
      Optional<Produto> findByIdNotDeleted(String id);
}

*/
