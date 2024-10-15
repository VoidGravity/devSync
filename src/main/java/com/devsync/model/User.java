package com.devsync.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", length = 50)
    private String firstName;

    @Column(name = "last_name", length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "modification_tokens", nullable = false)
    private Integer modificationTokens = 2;

    @Column(name = "deletion_tokens", nullable = false)
    private Integer deletionTokens = 1;

    @Column(name = "last_token_reset")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastTokenReset;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setManagerRole(Role role){
        this.role = role;
    }
    public Enum<Role> getManagerRole(){
        return this.role;
    }
    public Integer getModificationTokens() {
        return modificationTokens;
    }

    public void setModificationTokens(Integer modificationTokens) {
        this.modificationTokens = modificationTokens;
    }

    public Integer getDeletionTokens() {
        return deletionTokens;
    }

    public void setDeletionTokens(Integer deletionTokens) {
        this.deletionTokens = deletionTokens;
    }

    public Date getLastTokenReset() {
        return lastTokenReset;
    }

    public void setLastTokenReset(Date lastTokenReset) {
        this.lastTokenReset = lastTokenReset;
    }
/*
 TODO [Reverse Engineering] create field to map the 'manager_role' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "manager_role", columnDefinition = "manager_role not null")
    private Object managerRole;
*/
}