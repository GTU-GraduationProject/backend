package com.backend.recognitionitems.branch.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchInfoResponseDto {
    private Long brandId;
    private String brandName;
    private String brandLogo;
    private Long branchId;
    private String branchName;
    private Long branchManagerId;
    private String branchManagerName;
    private String branchManagerSurname;
    private Long technicalStaffId;
    private String technicalStaffName;
    private String technicalStaffSurname;
}
