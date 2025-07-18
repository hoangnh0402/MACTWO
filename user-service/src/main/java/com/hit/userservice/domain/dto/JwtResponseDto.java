package com.hit.userservice.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JwtResponseDto {

    private String token;
    private String type = "Bearer";
    private Integer id;
    private String email;
    private List<String> roles;

    public JwtResponseDto(String accessToken, Integer id, String email, List<String> roles) {
        this.token = accessToken;
        this.id = id;
        this.email = email;
        this.roles = roles;
    }
}
