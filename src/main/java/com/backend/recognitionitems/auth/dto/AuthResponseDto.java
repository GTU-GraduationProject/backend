package com.backend.recognitionitems.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDto {
    private String token;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String role;
}
