package com.backend.recognitionitems.brand.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BrandResponseDto {
    private Long brandId;
    private String brandName;
    private String brandLogo;
    private Long localAdminId;
    private String localAdminName;
    private String localAdminSurname;
}
