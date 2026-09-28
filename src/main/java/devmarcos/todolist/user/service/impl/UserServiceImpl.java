package devmarcos.todolist.user.service.impl;

import devmarcos.todolist.user.repository.UserRepository;
import devmarcos.todolist.user.exception.UsuarioNaoEncontrado;
import devmarcos.todolist.user.dto.CreateUserDTO;
import devmarcos.todolist.user.model.Usuario;
import devmarcos.todolist.user.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario createUser(CreateUserDTO createUserDTO) {

        Usuario usuario = new Usuario(
                createUserDTO.name(),
                createUserDTO.email(),
                Instant.now(),
                null,
                passwordEncoder.encode(createUserDTO.senha())

        );

        return userRepository.save(usuario);

    }
    @Override
    public Usuario FindUserByEmail(String email) {
        Usuario usuario = userRepository.findByEmail(email);
        if (usuario == null) {
            throw new UsuarioNaoEncontrado(email);
        }
        return usuario;
    }
}

