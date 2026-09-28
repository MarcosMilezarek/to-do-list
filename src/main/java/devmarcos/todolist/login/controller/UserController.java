package devmarcos.todolist.login.controller;

import devmarcos.todolist.login.config.TokenService;
import devmarcos.todolist.login.dto.LoginDTO;
import devmarcos.todolist.login.dto.LoginResponseDTO;
import devmarcos.todolist.user.dto.CreateUserDTO;
import devmarcos.todolist.user.dto.UsuarioResumoDTO;
import devmarcos.todolist.user.model.Usuario;
import devmarcos.todolist.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public UserController(UserService userService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<UsuarioResumoDTO> createUser(@RequestBody @Valid CreateUserDTO createUserDTO) {
        Usuario user = userService.createUser(createUserDTO);
        return ResponseEntity.ok(UsuarioResumoDTO.from(user));
    }

    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody @Valid LoginDTO loginDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(loginDTO.login(), loginDTO.senha());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        LoginResponseDTO dto = new LoginResponseDTO(tokenService.generateToken((Usuario) auth.getPrincipal()));
        return ResponseEntity.ok().body(dto);
    }
}
