package com.dev.springsecurity.controller;

import com.dev.springsecurity.controller.dto.CreateUserDto;
import com.dev.springsecurity.entities.Role;
import com.dev.springsecurity.entities.User;
import com.dev.springsecurity.repository.RoleRepository;
import com.dev.springsecurity.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    private UserRepository userRepository;

    private RoleRepository roleRepository; //quando eu criar a instancia de um novo usuario, precisa definir sua role basic

    private BCryptPasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @PostMapping("/users")
    public ResponseEntity<Void> newUser (@RequestBody CreateUserDto dto){


 //Busca no banco de dados a role com o nome BASIC.Essa role será atribuída ao usuário que está sendo criado.
        Role basicRole = roleRepository.findByName(Role.Values.BASIC.name());

        var userFromDb = userRepository.findByUsername(dto.name());

        if (userFromDb.isPresent()){

            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY); //erro caso o usuario já exista


        }

        User user = new User();
        user.setName(dto.name());
        user.setPassaword(passwordEncoder.encode(dto.password())); //fazendo a criptografia da senha
        user.setRoles(Set.of(basicRole));

        userRepository.save(user);

        return ResponseEntity.ok().build();

    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_admin')") //role necessaria para permitir que usuarios do tipo ADMIN  chame essa requisição
    public ResponseEntity<List<User>> listUsers(){

        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }


}
