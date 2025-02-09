package com.dev.springsecurity.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_roles")
public class Role {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //O valor será gerado pelo banco de dados por meio de uma coluna auto-incremento.
    private Long roleId;

    private String name;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
