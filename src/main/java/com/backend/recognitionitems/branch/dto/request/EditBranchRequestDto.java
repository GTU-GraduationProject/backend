package com.backend.recognitionitems.branch.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EditBranchRequestDto {
    private String branchName;
    private Long branchManagerId;
    private Long technicalStaffId;
}
