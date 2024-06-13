package com.appointment_scheduling_app.security.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {
    private Long userId;
    private String name;
    private String email;
    private String password;
    private String role;
}
