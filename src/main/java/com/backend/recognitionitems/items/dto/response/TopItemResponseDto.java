package com.backend.recognitionitems.items.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopItemResponseDto {
    private String itemName;
    private Long itemTotalAmount;
    private Long itemRemainingAmount;
    private Long itemSoldAmount;
}
