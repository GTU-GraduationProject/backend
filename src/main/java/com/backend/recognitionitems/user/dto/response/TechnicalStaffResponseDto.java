package com.backend.recognitionitems.user.dto.response;

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
public class TechnicalStaffResponseDto extends UserResponseDto {
    private Long brandId;
    private String brandName;
    private Long branchId;
    private String branchName;
}
