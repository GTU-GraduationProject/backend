package com.backend.recognitionitems.cashiercheckout.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditCashierCheckoutRequestDto {
    private String cameraId;
    private Long cashierId;
}
