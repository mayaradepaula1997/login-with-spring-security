package com.dev.springsecurity.entities;

import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    @Column(unique = true) //campo unico
    private String name;
    private String passaword;

    @ManyToMany //Um usuário pode ter varios perfis e um perfil pode esta vinculado a varios usuários
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassaword() {
        return passaword;
    }

    public void setPassaword(String passaword) {
        this.passaword = passaword;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}