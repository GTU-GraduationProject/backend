package com.backend.recognitionitems.branch;

import com.backend.recognitionitems.branch.dto.Branch;
import com.backend.recognitionitems.branch.dto.request.AddBranchRequestDto;
import com.backend.recognitionitems.branch.dto.request.EditBranchRequestDto;
import com.backend.recognitionitems.branch.dto.response.BranchInfoResponseDto;
import com.backend.recognitionitems.branch.dto.response.BranchTechnicalStaffResponseDto;
import com.backend.recognitionitems.brand.BrandRepository;
import com.backend.recognitionitems.brand.dto.Brand;
import com.backend.recognitionitems.user.UserRepository;
import com.backend.recognitionitems.user.dto.User;
import com.backend.recognitionitems.user.dto.UserType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    @Transactional
    public void addBranch(AddBranchRequestDto addBranchRequestDto) {
        Brand brand = brandRepository.findByLocalAdminId(addBranchRequestDto.getLocalAdminId()).orElse(null);
        if (brand == null) {
            throw new IllegalArgumentException("The local admin does not have brand!");
        }

        Optional<User> branchManager = Optional.ofNullable(userRepository.findById(addBranchRequestDto.getBranchManagerId())
                .orElseThrow(() -> new IllegalArgumentException("Branch manager does not exist!")));;
        if (!branchManager.get().getRole().equals(UserType.BRANCH_MANAGER)) {
            throw new IllegalArgumentException("Branch manager does not exist!");
        }

        boolean branchManagerHasBrand = branchRepository.existsByBranchManager(branchManager.get());
        if (branchManagerHasBrand) {
            throw new IllegalArgumentException("The branch manager already has a branch!");
        }

        Optional<User> technicalStaff = Optional.ofNullable(userRepository.findById(addBranchRequestDto.getTechnicalStaffId())
                .orElseThrow(() -> new IllegalArgumentException("Technical staff does not exist!")));;
        if (!technicalStaff.get().getRole().equals(UserType.TECHNICAL_STAFF)) {
            throw new IllegalArgumentException("Technical staff does not exist!");
        }

        boolean technicalStaffHasBranch = branchRepository.existsByTechnicalStaff(technicalStaff.get());
        if (technicalStaffHasBranch) {
            throw new IllegalArgumentException("The technical staff already has a branch!");
        }

        Branch branch = Branch.builder()
                .branchName(addBranchRequestDto.getBranchName())
                .brand(brand)
                .branchManager(branchManager.get())
                .technicalStaff(technicalStaff.get())
                .build();

        branchRepository.save(branch);
    }
    @Transactional
    public List<BranchInfoResponseDto> getAllBranches(Long localAdminId) {
        Brand brand = brandRepository.findByLocalAdminId(localAdminId).orElse(null);
        if (brand == null) {
            return null;
        }

        List<Branch> branches = branchRepository.findAllByBrandId(brand.getId());
        return branches.stream()
                .map(branch ->
                        BranchInfoResponseDto.builder()
                                .brandId(branch.getBrand().getId())
                                .brandName(branch.getBrand().getBrandName())
                                .brandLogo(branch.getBrand().getBrandLogo())
                                .branchId(branch.getId())
                                .branchName(branch.getBranchName())
                                .branchManagerId(branch.getBranchManager().getId())
                                .branchManagerName(branch.getBranchManager().getName())
                                .branchManagerSurname(branch.getBranchManager().getSurname())
                                .technicalStaffId(branch.getTechnicalStaff().getId())
                                .technicalStaffName(branch.getTechnicalStaff().getName())
                                .technicalStaffSurname(branch.getTechnicalStaff().getSurname())
                                .build()
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean editBranch(Branch existingBranch, EditBranchRequestDto editBranchRequestDto) {
        Optional<User> branchManager = Optional.ofNullable(userRepository.findById(editBranchRequestDto.getBranchManagerId())
                .orElseThrow(() -> new IllegalArgumentException("Branch manager does not exist!")));
        if (!branchManager.get().getRole().equals(UserType.BRANCH_MANAGER)) {
            throw new IllegalArgumentException("Branch manager does not exist!");
        }
        /*
        if (branchRepository.existsByBranchManagerId(branchManager.get().getId())) {
            throw new IllegalArgumentException("The branch manager does already has branch!");
        }
        */
        Optional<User> technicalStaff = Optional.ofNullable(userRepository.findById(editBranchRequestDto.getTechnicalStaffId())
                .orElseThrow(() -> new IllegalArgumentException("Technical staff does not exist!")));
        if (!technicalStaff.get().getRole().equals(UserType.TECHNICAL_STAFF)) {
            throw new IllegalArgumentException("Technical staff does not exist!");
        }
        /*
        if (branchRepository.existsByTechnicalStaffId(technicalStaff.get().getId())) {
            throw new IllegalArgumentException("The technical staff does already has branch!");
        }
        */
        existingBranch.setBranchManager(branchManager.get());
        existingBranch.setTechnicalStaff(technicalStaff.get());
        existingBranch.setBranchName(editBranchRequestDto.getBranchName());

        branchRepository.save(existingBranch);
        return true;
    }

    @Transactional
    public boolean deleteBranch(Long branchId) {
        Optional<Branch> branch = branchRepository.findById(branchId);
        if (branch.isPresent()) {
            branchRepository.delete(branch.get());
            return true;
        } else {
            return false;
        }
    }
    @Transactional
    public BranchInfoResponseDto getBranch(Long branchId) {
        Branch branch = branchRepository.findById(branchId).orElse(null);
        if (branch == null) {
            return null;
        }
        return BranchInfoResponseDto.builder()
                .brandId(branch.getBrand().getId())
                .brandName(branch.getBrand().getBrandName())
                .branchId(branch.getId())
                .branchName(branch.getBranchName())
                .branchManagerId(branch.getBranchManager().getId())
                .branchManagerName(branch.getBranchManager().getName())
                .branchManagerSurname(branch.getBranchManager().getSurname())
                .technicalStaffId(branch.getTechnicalStaff().getId())
                .technicalStaffName(branch.getTechnicalStaff().getName())
                .technicalStaffSurname(branch.getTechnicalStaff().getSurname())
                .build();
    }

    public BranchTechnicalStaffResponseDto getTechnicalStaff(Long branchId) {
        Branch branch = branchRepository.findById(branchId).orElse(null);
        if (branch == null) {
            return null;
        }
        return BranchTechnicalStaffResponseDto.builder()
                .technicalStaffId(branch.getTechnicalStaff().getId())
                .technicalStaffName(branch.getTechnicalStaff().getName())
                .technicalStaffSurname(branch.getTechnicalStaff().getSurname())
                .build();
    }
}
