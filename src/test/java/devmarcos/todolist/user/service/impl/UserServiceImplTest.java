package devmarcos.todolist.user.service.impl;

import devmarcos.todolist.user.dto.CreateUserDTO;
import devmarcos.todolist.user.exception.UsuarioNaoEncontrado;
import devmarcos.todolist.user.model.Usuario;
import devmarcos.todolist.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void criarUsuarioComSenhaCodificada() {
        UserServiceImpl service = new UserServiceImpl(userRepository, passwordEncoder);
        CreateUserDTO dto = new CreateUserDTO("Marcos", "marcos@exemplo.com", "123456");

        when(passwordEncoder.encode("123456")).thenReturn("senha-codificada");
        when(userRepository.save(any(Usuario.class))).thenAnswer(invocacao -> invocacao.getArgument(0));

        Usuario resultado = service.createUser(dto);

        ArgumentCaptor<Usuario> captor = ArgumentCaptor.forClass(Usuario.class);
        verify(userRepository).save(captor.capture());
        Usuario salvo = captor.getValue();
        assertEquals("senha-codificada", salvo.getSenha());
        assertNotEquals("123456", salvo.getSenha());
        assertEquals("Marcos", salvo.getNome());
        assertEquals("marcos@exemplo.com", salvo.getEmail());
        assertNotNull(salvo.getCreationTime());
        assertSame(salvo, resultado);
    }

    @Test
    void findUserByEmailSucesso() {
        UserServiceImpl service = new UserServiceImpl(userRepository, passwordEncoder);
        Usuario usuario = new Usuario("Marcos", "marcos@exemplo.com", null, null, "hash");

        when(userRepository.findByEmail("marcos@exemplo.com")).thenReturn(usuario);

        assertSame(usuario, service.FindUserByEmail("marcos@exemplo.com"));
    }

    @Test
    void findUserByEmailNaoEncontrado() {
        UserServiceImpl service = new UserServiceImpl(userRepository, passwordEncoder);

        when(userRepository.findByEmail("ninguem@exemplo.com")).thenReturn(null);

        UsuarioNaoEncontrado erro = assertThrows(UsuarioNaoEncontrado.class,
                () -> service.FindUserByEmail("ninguem@exemplo.com"));
        assertEquals("Usuário com email ninguem@exemplo.com não encontrado.", erro.getMessage());
    }
}
