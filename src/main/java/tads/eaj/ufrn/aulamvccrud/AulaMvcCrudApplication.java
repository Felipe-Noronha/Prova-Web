package tads.eaj.ufrn.aulamvccrud;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import tads.eaj.ufrn.aulamvccrud.model.Usuario;
import tads.eaj.ufrn.aulamvccrud.repository.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class AulaMvcCrudApplication {

    @Bean
    CommandLineRunner commandLineRunner(UsuarioRepository usuarioRepository, PasswordEncoder encoder) {
        return args -> {
            usuarioRepository.deleteAll();
            List<Usuario> users = Stream.of(
                    new Usuario("", "Taniro", "123.456.789-10", "administrador", encoder.encode("administrador"), true),
                    new Usuario("", "Angela", "444.456.789-10", "usuario", encoder.encode("usuario"), false),
                    new Usuario("", "Felipe", "555.456.789-10", "usuario2", encoder.encode("usuario2"), false)
            ).collect(Collectors.toList());

            for (var e : users) {
                System.out.println(e);
            }
            usuarioRepository.saveAll(users);
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(AulaMvcCrudApplication.class, args);
    }

}
