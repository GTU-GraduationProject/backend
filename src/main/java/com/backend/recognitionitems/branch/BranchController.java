package com.backend.recognitionitems.branch;

import com.backend.recognitionitems.branch.dto.Branch;
import com.backend.recognitionitems.branch.dto.request.AddBranchRequestDto;
import com.backend.recognitionitems.branch.dto.request.EditBranchRequestDto;
import com.backend.recognitionitems.branch.dto.response.BranchInfoResponseDto;
import com.backend.recognitionitems.branch.dto.response.BranchTechnicalStaffResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class BranchController {
    private final BranchService branchService;
    private final BranchRepository branchRepository;

    @Operation(summary = "Create branch. Local admin can.")
    @PostMapping("/branch")
    public ResponseEntity<String> addBranch(@RequestBody AddBranchRequestDto addBranchRequestDto) {
        try {
            branchService.addBranch(addBranchRequestDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Invalid request: " + ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "List branch. Local Admin can.")
    @GetMapping("/branch/branches/{localAdminId}")
    public ResponseEntity<List<BranchInfoResponseDto>> getAllBranches(@PathVariable Long localAdminId) {
        List<BranchInfoResponseDto> branches = branchService.getAllBranches(localAdminId);
        return ResponseEntity.ok(branches);
    }

    @Operation(summary = "Edit branch. Local Admin can.")
    @PostMapping("/branch/{branchId}")
    public ResponseEntity<Void> editBranch(@PathVariable Long branchId,
                                            @RequestBody EditBranchRequestDto editBranchRequestDto) {
        Branch existingBranch = branchRepository.findById(branchId).orElse(null);

        if (existingBranch == null) {
            // there is no branch
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        boolean edited = branchService.editBranch(existingBranch, editBranchRequestDto);
        if (edited) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Delete branch. Local Admin can.")
    @DeleteMapping("/branch/{branchId}")
    public ResponseEntity<String> deleteBranch(@PathVariable Long branchId) {
        boolean deleted = branchService.deleteBranch(branchId);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "Get specific branch info")
    @GetMapping("/branch/{branchId}")
    public ResponseEntity<BranchInfoResponseDto> getBranch(@PathVariable Long branchId) {
        BranchInfoResponseDto branch = branchService.getBranch(branchId);
        return ResponseEntity.ok(branch);
    }

    @Operation(summary = "Get technical staff info.")
    @GetMapping("/branch/technical-staff/{branchId}")
    public ResponseEntity<BranchTechnicalStaffResponseDto> getTechnicalStaff(@PathVariable Long branchId) {
        BranchTechnicalStaffResponseDto technicalStaff = branchService.getTechnicalStaff(branchId);
        return ResponseEntity.ok(technicalStaff);
    }
}
