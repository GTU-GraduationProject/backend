package com.backend.recognitionitems.user.dto.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder=true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocalAdminResponseDto extends UserResponseDto {
    private Long brandId;
    private String brandName;
}
