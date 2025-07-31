package com.musicosim.bandaapp.dto;

public class LoginRequest {
    private String email;
    private String password;

    // Getters y setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    private String deviceId;

public String getDeviceId() {
    return deviceId;
}

public void setDeviceId(String deviceId) {
    this.deviceId = deviceId;
}

}
