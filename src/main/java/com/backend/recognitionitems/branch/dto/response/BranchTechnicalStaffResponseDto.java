package com.backend.recognitionitems.branch.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BranchTechnicalStaffResponseDto {
    private Long technicalStaffId;
    private String technicalStaffName;
    private String technicalStaffSurname;
}
