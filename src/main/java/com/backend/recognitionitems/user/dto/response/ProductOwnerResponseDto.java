package com.backend.recognitionitems.user.dto.response;

import com.backend.recognitionitems.user.dto.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder=true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductOwnerResponseDto extends UserResponseDto {
    private Long productId;
    private String productName;
    private String productLogo;
}
