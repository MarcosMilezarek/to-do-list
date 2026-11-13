package devmarcos.todolist.login.controller;

import devmarcos.todolist.login.config.SecurityConfig;
import devmarcos.todolist.login.config.TokenService;
import devmarcos.todolist.user.dto.CreateUserDTO;
import devmarcos.todolist.user.model.Usuario;
import devmarcos.todolist.user.repository.UserRepository;
import devmarcos.todolist.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
class UserControllerTest {

    private static final String TOKEN = "token-valido";

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private AuthenticationManager authenticationManager;
    @MockitoBean
    private TokenService tokenService;
    @MockitoBean
    private UserRepository userRepository;

    private Usuario usuario;

    @BeforeEach
    void prepararUsuario() {
        usuario = new Usuario("Marcos", "marcos@exemplo.com", null, null, "hash");
        usuario.setId(1L);
    }

    @Test
    void registrarUsuarioSucesso() throws Exception {
        when(userService.createUser(any(CreateUserDTO.class))).thenReturn(usuario);

        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Marcos\", \"email\": \"marcos@exemplo.com\", \"senha\": \"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Marcos"))
                .andExpect(jsonPath("$.senha").doesNotExist());
    }

    @Test
    void registrarUsuarioCamposEmBranco() throws Exception {
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"\", \"email\": \"\", \"senha\": \"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erros.name").value("Nome é obrigatório"))
                .andExpect(jsonPath("$.erros.email").value("Email é obrigatório"))
                .andExpect(jsonPath("$.erros.senha").value("Senha é obrigatória"));

        verifyNoInteractions(userService);
    }

    @Test
    void loginSucesso() throws Exception {
        Authentication autenticado = new UsernamePasswordAuthenticationToken(usuario, null, List.of());
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(autenticado);
        when(tokenService.generateToken(usuario)).thenReturn("jwt-gerado");

        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"marcos@exemplo.com\", \"senha\": \"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-gerado"));
    }

    @Test
    void loginCredenciaisInvalidas() throws Exception {
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"marcos@exemplo.com\", \"senha\": \"errada\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Credenciais inválidas"));

        verifyNoInteractions(tokenService);
    }

    @Test
    void rotaInexistenteComToken() throws Exception {
        when(tokenService.validateToken(TOKEN)).thenReturn(usuario.getEmail());
        when(userRepository.findByEmail(usuario.getEmail())).thenReturn(usuario);

        mockMvc.perform(get("/auth/perfil").header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Caminho não existe"));
    }

    @Test
    void rotaProtegidaSemToken() throws Exception {
        mockMvc.perform(get("/auth/perfil"))
                .andExpect(status().isUnauthorized());
    }
}
