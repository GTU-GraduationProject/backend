package com.backend.recognitionitems.product.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditProductRequestDto {
    private String productName;
    private Long productOwnerId;
    private String productLogo;
}
