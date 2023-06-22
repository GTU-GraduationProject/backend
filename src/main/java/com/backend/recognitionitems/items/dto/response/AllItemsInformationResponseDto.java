package com.backend.recognitionitems.items.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AllItemsInformationResponseDto {
    private Long totalItems;
    private Long totalSoldItems;
    private Long totalRemainingItems;
}
