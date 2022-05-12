package es.kairos.db_ejercicio2.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/users/")
    public void saveUser(@RequestBody Usuario user) {
        user.setEncodedPassword(bCryptPasswordEncoder.encode(user.getEncodedPassword()));
        usuarioRepository.save(user);
    }

    @GetMapping("/users/")
    public List<Usuario> getAllUsers() {
        return usuarioRepository.findAll();
    }

    @GetMapping("/users/{username}")
    public Usuario getUser(@PathVariable String username) {
        return usuarioRepository.findByName(username);
    }


}
