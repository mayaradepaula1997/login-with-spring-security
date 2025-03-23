package com.dev.springsecurity.entities;

import com.dev.springsecurity.controller.dto.LoginRequest;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id")
    private UUID userId;

    @Column(unique = true) //campo unico
    private String username;

    private String password;

    //Um usuário pode ter varios perfis e um perfil pode esta vinculado a varios usuários
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_users_roles", //tabela intermediária que será usada para armazenar a associação entre as duas entidades (chave estrangeira)
            joinColumns = @JoinColumn(name = "user_id"), //nome de uma das colunas
            inverseJoinColumns = @JoinColumn(name ="role_id") //nome de uma das colunas

    )
    private Set<Role> roles;


    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username= name;
    }

    public String getPassaword() {
        return password;
    }

    public void setPassaword(String passaword) {
        this.password = passaword;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    //Método que compara (MATCHES) a senha "limpa" com a senha criptografada
    public boolean isLoginCorrect(LoginRequest loginRequest, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(loginRequest.password(), this.password);

        //loginRequest.password(): Esse é o valor da senha informada pelo usuário no momento do login (em texto simples).
        //this.password: Essa é a senha armazenada no banco de dados, mas codificada (em forma de hash).
    }



}