package devmarcos.todolist.controller;

import devmarcos.todolist.Model.Usuario;
import devmarcos.todolist.dto.CreateUserDTO;
import devmarcos.todolist.dto.UsuarioResumoDTO;
import devmarcos.todolist.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResumoDTO> createUser(@RequestBody @Valid CreateUserDTO createUserDTO) {
        Usuario user = userService.createUser(createUserDTO);
        return ResponseEntity.ok(UsuarioResumoDTO.from(user));
    }
    @GetMapping("/login/{email}")
    public ResponseEntity<UsuarioResumoDTO> loginUser(@PathVariable(name = "email") String email) {
        Usuario user = userService.FindUserByEmail(email);
        return ResponseEntity.ok(UsuarioResumoDTO.from(user));
    }
}
