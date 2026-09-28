package devmarcos.todolist.user.service;

import devmarcos.todolist.user.dto.CreateUserDTO;
import devmarcos.todolist.user.model.Usuario;



public interface UserService {

    Usuario createUser(CreateUserDTO createUserDTO);
    Usuario FindUserByEmail(String email);

}
