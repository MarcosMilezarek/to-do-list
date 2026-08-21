package devmarcos.todolist.controller;

import devmarcos.todolist.Model.Usuario;
import devmarcos.todolist.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping
    public ResponseEntity<Usuario> createUser(@RequestBody @Valid CreateUserDTO createUserDTO) {
        Usuario user = userService.createUser(createUserDTO);

        return ResponseEntity.ok(user);
    }

}
