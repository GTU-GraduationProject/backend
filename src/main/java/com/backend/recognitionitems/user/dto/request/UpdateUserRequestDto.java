package com.backend.recognitionitems.user.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequestDto {
    private String name;
    private String surname;
    private String email;
}
