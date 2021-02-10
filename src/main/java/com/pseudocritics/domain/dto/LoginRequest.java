package com.pseudocritics.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequest {

    private String emailOrUserName;
    private String password;

    @JsonProperty("rememberMe")
    private boolean rememberMe;
    @JsonProperty("isMobile")
    private boolean isMobile;

    public String getEmailOrUserName() { return emailOrUserName; }

    public String getPassword() { return password; }

    public boolean isRememberMe() { return rememberMe; }

    public boolean isMobile() { return isMobile; }

}
