package com.backend.recognitionitems.items.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemProductDto {
    private Long productId;
    private String productName;
    private String productLogo;
    private Long productOwnerId;
    private String productOwnerName;
    private String productOwnerSurname;
}
