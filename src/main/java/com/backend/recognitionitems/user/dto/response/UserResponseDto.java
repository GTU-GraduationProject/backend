package com.backend.recognitionitems.user.dto.response;

import com.backend.recognitionitems.user.dto.UserType;
import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder=true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
    private Long userId;
    private String name;
    private String surname;
    private String email;
    private String password;
    private UserType role;
}
