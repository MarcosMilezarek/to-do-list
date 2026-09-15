package devmarcos.todolist.service;

import devmarcos.todolist.Model.Usuario;
import devmarcos.todolist.dto.CreateUserDTO;
import devmarcos.todolist.exception.UsuarioNaoEncontrado;
import devmarcos.todolist.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Usuario createUser(CreateUserDTO createUserDTO) {

        Usuario usuario = new Usuario(
                createUserDTO.name(),
                createUserDTO.email(),
                Instant.now(),
                null
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

