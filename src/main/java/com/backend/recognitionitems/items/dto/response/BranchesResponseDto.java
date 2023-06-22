package com.backend.recognitionitems.items.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchesResponseDto {
    private List<BranchResponseDto> topTenBranches;
}
