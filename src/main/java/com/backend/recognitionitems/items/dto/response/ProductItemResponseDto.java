package com.backend.recognitionitems.items.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemResponseDto {
    private Long itemId;
    private String itemName;
    private Long itemTotalAmount;
    private Long itemRemainingAmount;
    private Long itemSoldAmount;
    private Boolean isReported;
    private ProductItemProductDto product;
    private ProductItemBranchDto branch;
}
