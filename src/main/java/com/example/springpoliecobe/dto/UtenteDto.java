package com.example.springpoliecobe.dto;


public class UtenteDto {

    private String username;

    private String password;

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    //private Integer role;

    /*public Integer getRole() {
        return role;
    }

    public void setRole(final Integer role) {
        this.role = role;
    }*/

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

}
