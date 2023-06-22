package com.backend.recognitionitems.user;

import com.backend.recognitionitems.user.dto.User;
import com.backend.recognitionitems.user.dto.UserType;
import com.backend.recognitionitems.user.dto.request.AddUserRequestDto;
import com.backend.recognitionitems.user.dto.request.UpdateUserRequestDto;
import com.backend.recognitionitems.user.dto.response.*;
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
public class UserController {
    private final UserService userService;

    @Operation(summary = "Create local admin. General Admin can.")
    @PostMapping("/user/local-admin")
    public ResponseEntity<Void> addLocalAdmin(@RequestBody AddUserRequestDto addUserRequestDto) {
        try {
            userService.addUser(addUserRequestDto, UserType.LOCAL_ADMIN);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Operation(summary = "Edit local admin. General Admin can.")
    @PostMapping("/user/local-admin/{userId}")
    public ResponseEntity<User> updateLocalAdmin(@PathVariable Long userId,
                                                 @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        User existingUser = userService.getUserByUserId(userId);

        if (existingUser == null) {
            // there is no user
            return ResponseEntity.notFound().build();
        } else {
            if (!UserType.LOCAL_ADMIN.equals(existingUser.getRole())) {
                // there is user but it is not local admin
                return ResponseEntity.notFound().build();
            }
        }
        try {
            userService.updateUser(existingUser, updateUserRequestDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @Operation(summary = "List local admins. General Admin can.")
    @GetMapping("/user/local-admins")
    public ResponseEntity<List<LocalAdminResponseDto>> getAllLocalAdmins() {
        List<LocalAdminResponseDto> localAdmins = userService.getLocalAdmins();
        return ResponseEntity.ok(localAdmins);
    }

    @Operation(summary = "List local admins with no brands.")
    @GetMapping("/user/local-admins/no-brands")
    public ResponseEntity<List<LocalAdminResponseDto>> getAllLocalAdminsWithNoBrands() {
        List<LocalAdminResponseDto> localAdmins = userService.getLocalAdminsWithNoBrands();
        return ResponseEntity.ok(localAdmins);
    }

    @Operation(summary = "List local admin in details.")
    @GetMapping("/user/local-admins/{userId}")
    public ResponseEntity<LocalAdminResponseDto> getLocalAdmin(@PathVariable Long userId) {
        LocalAdminResponseDto localAdmin = userService.getLocalAdmin(userId);
        return ResponseEntity.ok(localAdmin);
    }

    @Operation(summary = "Delete local admin. General Admin can.")
    @DeleteMapping("/user/local-admin/{userId}")
    public ResponseEntity<Void> deleteLocalAdmin(@PathVariable Long userId) {
        boolean deleted = userService.deleteUser(userId);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //Product owner
    @Operation(summary = "Create product owner. General Admin can.")
    @PostMapping("/user/product-owner")
    public ResponseEntity<Void> addProductOwner(@RequestBody AddUserRequestDto addUserRequestDto) {
        try {
            userService.addUser(addUserRequestDto, UserType.PRODUCT_OWNER);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Operation(summary = "Edit product owner. General Admin can.")
    @PostMapping("/user/product-owner/{userId}")
    public ResponseEntity<User> updateProductOwner(@PathVariable Long userId,
                                                 @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        User existingUser = userService.getUserByUserId(userId);

        if (existingUser == null) {
            // there is no user
            return ResponseEntity.notFound().build();
        } else {
            if (!UserType.PRODUCT_OWNER.equals(existingUser.getRole())) {
                // there is user but it is not product owner
                return ResponseEntity.notFound().build();
            }
        }
        try {
            userService.updateUser(existingUser, updateUserRequestDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @Operation(summary = "List product owners. General Admin can.")
    @GetMapping("/user/product-owners")
    public ResponseEntity<List<ProductOwnerResponseDto>> getAllProductOwners() {
        List<ProductOwnerResponseDto> productOwners = userService.getProductOwners();
        return ResponseEntity.ok(productOwners);
    }

    @Operation(summary = "List product owners with no products.")
    @GetMapping("/user/product-owners/no-products")
    public ResponseEntity<List<ProductOwnerResponseDto>> getAllProductAdminsWithNoProducts() {
        List<ProductOwnerResponseDto> productOwners = userService.getProductOwnersWithNoProducts();
        return ResponseEntity.ok(productOwners);
    }

    @Operation(summary = "List product owners in details.")
    @GetMapping("/user/product-owners/{userId}")
    public ResponseEntity<ProductOwnerResponseDto> getProductOwner(@PathVariable Long userId) {
        ProductOwnerResponseDto productOwner = userService.getProductOwner(userId);
        return ResponseEntity.ok(productOwner);
    }

    @Operation(summary = "Delete product owner. General Admin can.")
    @DeleteMapping("/user/product-owner/{userId}")
    public ResponseEntity<String> deleteProductOwner(@PathVariable Long userId) {
        boolean deleted = userService.deleteUser(userId);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //Branch manager
    @Operation(summary = "Create branch manager. Local Admin can.")
    @PostMapping("/user/branch-manager")
    public ResponseEntity<Void> addBranchManager(@RequestBody AddUserRequestDto addUserRequestDto) {
        try {
            userService.addUser(addUserRequestDto, UserType.BRANCH_MANAGER);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Operation(summary = "Edit branch manager. Local Admin can.")
    @PostMapping("/user/branch-manager/{userId}")
    public ResponseEntity<User> updateBranchManager(@PathVariable Long userId,
                                                   @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        User existingUser = userService.getUserByUserId(userId);

        if (existingUser == null) {
            // there is no user
            return ResponseEntity.notFound().build();
        } else {
            if (!UserType.BRANCH_MANAGER.equals(existingUser.getRole())) {
                // there is user but it is not product owner
                return ResponseEntity.notFound().build();
            }
        }
        try {
            userService.updateUser(existingUser, updateUserRequestDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @Operation(summary = "List branch manager. Local Admin can.")
    @GetMapping("/user/branch-manager/list/{localAdminId}")
    public ResponseEntity<List<BranchManagerResponseDto>> getAllBranchManagers(@PathVariable Long localAdminId) {
        List<BranchManagerResponseDto> branchManagers = userService.getBranchManagers(localAdminId);
        return ResponseEntity.ok(branchManagers);
    }

    @Operation(summary = "List branch managers with no branches.")
    @GetMapping("/user/branch-managers/no-branches")
    public ResponseEntity<List<BranchManagerResponseDto>> getAllBranchManagersWithNoBranches() {
        List<BranchManagerResponseDto> branchManagers = userService.getBranchManagersWithNoBranches();
        return ResponseEntity.ok(branchManagers);
    }

    @Operation(summary = "List branch manager in details.")
    @GetMapping("/user/branch-manager/{userId}")
    public ResponseEntity<BranchManagerResponseDto> getBranchManager(@PathVariable Long userId) {
        BranchManagerResponseDto branchManager = userService.getBranchManager(userId);
        return ResponseEntity.ok(branchManager);
    }

    @Operation(summary = "Delete branch manager. Local Admin can.")
    @DeleteMapping("/user/branch-manager/{userId}")
    public ResponseEntity<String> deleteBranchManager(@PathVariable Long userId) {
        boolean deleted = userService.deleteUser(userId);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //Technical staff
    @Operation(summary = "Create technical staff. Local Admin can.")
    @PostMapping("/user/technical-staff")
    public ResponseEntity<Void> addTechnicalStaff(@RequestBody AddUserRequestDto addUserRequestDto) {
        try {
            userService.addUser(addUserRequestDto, UserType.TECHNICAL_STAFF);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Operation(summary = "Edit technical staff. Local Admin can.")
    @PostMapping("/user/technical-staff/{userId}")
    public ResponseEntity<User> updateTechnicalStaff(@PathVariable Long userId,
                                                    @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        User existingUser = userService.getUserByUserId(userId);

        if (existingUser == null) {
            // there is no user
            return ResponseEntity.notFound().build();
        } else {
            if (!UserType.TECHNICAL_STAFF.equals(existingUser.getRole())) {
                // there is user but it is not product owner
                return ResponseEntity.notFound().build();
            }
        }
        try {
            userService.updateUser(existingUser, updateUserRequestDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }
    @Operation(summary = "List technical staff. Local Admin can.")
    @GetMapping("/user/technical-staff/list/{localAdminId}") ///user/branch-manager/list/{localAdminId}
    public ResponseEntity<List<TechnicalStaffResponseDto>> getAllTechnicalStaffs(@PathVariable Long localAdminId) {
        List<TechnicalStaffResponseDto> technicalStaffs = userService.getTechnicalStaffs(localAdminId);
        return ResponseEntity.ok(technicalStaffs);
    }

    @Operation(summary = "List technical staffs with no branches.")
    @GetMapping("/user/technical-staffs/no-branches")
    public ResponseEntity<List<TechnicalStaffResponseDto>> getAllTechnicalStaffsWithNoBranches() {
        List<TechnicalStaffResponseDto> technicalStaffs = userService.getTechnicalStaffsWithNoBranches();
        return ResponseEntity.ok(technicalStaffs);
    }

    @Operation(summary = "List technical staff in details.")
    @GetMapping("/user/technical-staff/{userId}")
    public ResponseEntity<TechnicalStaffResponseDto> getTechnicalStaff(@PathVariable Long userId) {
        TechnicalStaffResponseDto technicalStaff = userService.getTechnicalStaff(userId);
        return ResponseEntity.ok(technicalStaff);
    }

    @Operation(summary = "Delete technical staff. Local Admin can.")
    @DeleteMapping("/user/technical-staff/{userId}")
    public ResponseEntity<String> deleteTechnicalStaff(@PathVariable Long userId) {
        boolean deleted = userService.deleteUser(userId);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //Cashier
    @Operation(summary = "Create cashier. Branch manager can.")
    @PostMapping("/user/cashier")
    public ResponseEntity<Void> addCashier(@RequestBody AddUserRequestDto addUserRequestDto) {
        try {
            userService.addUser(addUserRequestDto, UserType.CASHIER);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @Operation(summary = "Edit cashier. Branch manager can.")
    @PostMapping("/user/cashier/{userId}")
    public ResponseEntity<User> updateCashier(@PathVariable Long userId,
                                                     @RequestBody UpdateUserRequestDto updateUserRequestDto) {
        User existingUser = userService.getUserByUserId(userId);

        if (existingUser == null) {
            // there is no user
            return ResponseEntity.notFound().build();
        } else {
            if (!UserType.CASHIER.equals(existingUser.getRole())) {
                // there is user but it is not product owner
                return ResponseEntity.notFound().build();
            }
        }
        try {
            userService.updateUser(existingUser, updateUserRequestDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "List cashier. Branch manager can.")
    @GetMapping("/user/cashier/list/{userId}")
    public ResponseEntity<List<CashierResponseDto>> getAllCashiers(@PathVariable Long userId) {
        List<CashierResponseDto> cashiers = userService.getCashiers(userId);
        return ResponseEntity.ok(cashiers);
    }

    @Operation(summary = "List cashiers with no cashier checkouts.")
    @GetMapping("/user/cashier/no-cashier-checkouts")
    public ResponseEntity<List<CashierResponseDto>> getAllCashiersWithNoCashierCheckouts() {
        List<CashierResponseDto> cashiers = userService.getCashiersWithNoCashierCheckouts();
        return ResponseEntity.ok(cashiers);
    }

    @Operation(summary = "List cashier in details.")
    @GetMapping("/user/cashier/{userId}")
    public ResponseEntity<CashierResponseDto> getCashier(@PathVariable Long userId) {
        CashierResponseDto cashier = userService.getCashier(userId);
        return ResponseEntity.ok(cashier);
    }

    @Operation(summary = "Delete cashier. Branch manager can.")
    @DeleteMapping("/user/cashier/{userId}")
    public ResponseEntity<String> deleteCashier(@PathVariable Long userId) {
        boolean deleted = userService.deleteUser(userId);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
