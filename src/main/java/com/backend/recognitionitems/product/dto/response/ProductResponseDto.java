package com.backend.recognitionitems.product.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponseDto {
    private Long productId;
    private String productName;
    private String productLogo;
    private Long productOwnerId;
    private String productOwnerName;
    private String productOwnerSurname;
}
