package com.backend.recognitionitems.brand.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditBrandRequestDto {
    private String brandName;
    private String brandLogo;
    private Long localAdminId;
}
