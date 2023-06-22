package com.backend.recognitionitems.items.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemInDetailResponseDto {
    private List<ItemResponseDto> items;
}
