package es.kairos.db_ejercicio2;

import es.kairos.db_ejercicio2.user.Usuario;
import es.kairos.db_ejercicio2.user.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DatabaseUsersLoader {

    @Autowired
    private UsuarioRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    private void initDatabase() {

        userRepository.save(new Usuario("user", passwordEncoder.encode("pass"), "USER"));
        userRepository.save(new Usuario("admin", passwordEncoder.encode("adminpass"), "USER", "ADMIN"));

    }
}
