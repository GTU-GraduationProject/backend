package com.backend.recognitionitems.cashiercheckout.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CashierCheckoutResponseDto {
    private Long cashierCheckoutId;
    private String cameraId;
    private Long cashierId;
    private String cashierName;
    private String cashierSurname;
}
