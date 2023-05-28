package tads.eaj.ufrn.aulamvccrud.service;

import org.springframework.stereotype.Service;
import tads.eaj.ufrn.aulamvccrud.model.Produto;
import tads.eaj.ufrn.aulamvccrud.repository.ProdutoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    private ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public void save(Produto p){
//        p.tituloMaiusculo();
        repository.save(p);
    }

    public List<Produto> findAll(){
        return repository.findAll();
    }

    public Optional<Produto> findById(String id){
        return repository.findById(id);
    }

    public void delete(String id){
        repository.deleteById(id);
    }

}








