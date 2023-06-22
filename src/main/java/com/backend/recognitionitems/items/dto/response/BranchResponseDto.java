package com.backend.recognitionitems.items.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchResponseDto {
    private Long topTotalAmount;
    private Long topTotalRemainingAmount;
    private Long topTotalSoldAmount;
    private Boolean isReported;
    private Long productId;
    private String productName;
    private String productLogo;
    private Long productOwnerId;
    private String productOwnerName;
    private String productOwnerSurname;
    private Long branchId;
    private String branchName;
    private Long brandId;
    private String brandName;
}
