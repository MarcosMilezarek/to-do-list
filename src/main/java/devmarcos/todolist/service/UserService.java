package devmarcos.todolist.service;

import devmarcos.todolist.Model.Usuario;
import devmarcos.todolist.dto.CreateUserDTO;


public interface UserService {

    Usuario createUser(CreateUserDTO createUserDTO);
    Usuario FindUserByEmail(String email);

}
