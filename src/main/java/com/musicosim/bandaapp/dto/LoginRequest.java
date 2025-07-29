package com.musicosim.bandaapp.dto;
public class LoginRequest {
    private String npi;
    private String password;

    // Getters y setters
    public String getNpi() {
        return npi;
    }

    public void setNpi(String npi) {
        this.npi = npi;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}