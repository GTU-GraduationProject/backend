package com.backend.recognitionitems.user.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddUserRequestDto {
    private String name;
    private String surname;
    private String password;
    private String email;
}
