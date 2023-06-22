package com.backend.recognitionitems.items.dto.response;

import com.backend.recognitionitems.user.dto.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemResponseDto {
    private Long itemId;
    private String itemName;
    private Long itemTotalAmount;
    private Long itemRemainingAmount;
    private Long itemSoldAmount;
    private Boolean isReported;
    private Long productId;
    private String productName;
    private Long productOwnerId;
    private String productOwnerName;
    private String productOwnerSurname;
    private Long branchId;
    private String branchName;
    private Long brandId;
    private String brandName;
}
