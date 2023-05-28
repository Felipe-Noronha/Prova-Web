package tads.eaj.ufrn.aulamvccrud.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    Date deleted;
    String imageURI;
    @NotBlank
    @Size(max = 20, min = 4)
    String nome;
    @NotNull
    Float preco;
    String tipo;
    String material;
    Float tamanho;
    String cor;
    

//    public void tituloMaiusculo(){
//        this.titulo.toUpperCase();
//    }
}