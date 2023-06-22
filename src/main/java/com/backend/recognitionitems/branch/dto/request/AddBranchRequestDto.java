package com.backend.recognitionitems.branch.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddBranchRequestDto {
    private Long localAdminId;
    private String branchName;
    private Long branchManagerId;
    private Long technicalStaffId;
}
