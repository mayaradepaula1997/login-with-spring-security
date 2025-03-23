package com.dev.springsecurity.config;

import com.dev.springsecurity.entities.Role;
import com.dev.springsecurity.entities.User;
import com.dev.springsecurity.repository.RoleRepository;
import com.dev.springsecurity.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.Set;

@Configuration
public class AdminUserConfig implements CommandLineRunner { //Assim que o projeto iniciar ele cadastra o usuario admin

    private RoleRepository roleRepository;

    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder;


    public AdminUserConfig(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        Role roleAdmin =  roleRepository.findByName(Role.Values.ADMIN.name()); //Busca no BD uma role com o nome ADMIN. Essa role é associada ao usuário admin

        Optional<User> userAdmin = userRepository.findByUsername("admin");
        userAdmin.ifPresentOrElse(
                User -> {
                    System.out.println("admin já existe");
                },
                () ->{
                    User user = new User();
                    user.setName("admin");
                    user.setPassaword(passwordEncoder.encode("123"));
                    user.setRoles(Set.of(roleAdmin));
                    userRepository.save(user);
                }
        );

    }
}
