package com.backend.recognitionitems.items.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductItemBranchDto {
    private Long branchId;
    private String branchName;
    private Long brandId;
    private String brandName;
}
