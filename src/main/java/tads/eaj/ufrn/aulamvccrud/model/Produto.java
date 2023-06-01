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
    LocalDateTime deleted;
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

    @Transient
    public String getPhotosImagePath() {
        if (imageURI == null || id == null) return null;
         
        return "/user-photos/" + id + "/" + imageURI;
    }

}