package com.backend.recognitionitems.brand.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddBrandRequestDto {
    private String brandName;
    private Long localAdminId;
    private String brandLogo;
}
