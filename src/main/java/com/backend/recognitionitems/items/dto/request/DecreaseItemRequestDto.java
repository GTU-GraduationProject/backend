package com.backend.recognitionitems.items.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DecreaseItemRequestDto {
    private Long cashierId;
    private String itemName;
}
