package devmarcos.todolist.service;

import devmarcos.todolist.Model.Usuario;
import devmarcos.todolist.controller.CreateUserDTO;
import org.springframework.stereotype.Service;


public interface UserService {

    Usuario createUser(CreateUserDTO createUserDTO);
    Usuario findById(long id);

}
