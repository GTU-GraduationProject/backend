package com.backend.recognitionitems.items.dto.response;

import lombok.*;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopFiveItemsResponseDto {
    private List<TopItemResponseDto> topFiveItems;
}
