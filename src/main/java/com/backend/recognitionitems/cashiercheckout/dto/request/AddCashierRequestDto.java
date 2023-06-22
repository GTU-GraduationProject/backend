package com.backend.recognitionitems.cashiercheckout.dto.request;

import com.backend.recognitionitems.branch.dto.Branch;
import com.backend.recognitionitems.user.dto.User;
import lombok.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddCashierRequestDto {
    private String cameraId;
    private Long cashierId;
    private Long branchId;
}
