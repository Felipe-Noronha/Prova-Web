package tads.eaj.ufrn.aulamvccrud.service;

import org.springframework.stereotype.Service;
import tads.eaj.ufrn.aulamvccrud.model.Produto;
import tads.eaj.ufrn.aulamvccrud.repository.ProdutoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
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
    

    /*
    public Optional<Produto> findById(String id){
        return repository.findByIdNotDeleted(id);
    }    


    public void delete(String id){
        Optional<Produto> produto = this.findById(id);
        if(produto.isPresent ()){
        LocalDate currentDate = LocalDate.now () ;
        Date currentDateTime = Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Produto p=produto.get();
        p.setDeleted(currentDateTime);
        save (p);
        }
        else {
            throw new RuntimeException ("Produto não encontrado");
        }
    }
    */

}








