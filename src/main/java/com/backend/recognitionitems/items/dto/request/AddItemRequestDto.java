package com.backend.recognitionitems.items.dto.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddItemRequestDto {
    private String itemName;
    private Long itemTotalAmount;
    private byte[] photosZip;
    private Long branchId; // itemi bir branch'e atayabilmek için gerekiyor
    private Long productId;
}
