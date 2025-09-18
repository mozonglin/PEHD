package com.example.pehd.dto;

import com.example.pehd.entity.User;

public class LoginResponse {
    
    private User user;
    private String accessToken;
    private String refreshToken;
    private long expiresIn;
    
    // Constructors
    public LoginResponse() {}
    
    public LoginResponse(User user, String accessToken, String refreshToken, long expiresIn) {
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }
    
    // Getters and Setters
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getAccessToken() { return accessToken; }
    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    
    public long getExpiresIn() { return expiresIn; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }
} 