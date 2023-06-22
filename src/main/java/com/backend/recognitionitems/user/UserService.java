package com.backend.recognitionitems.user;

import com.backend.recognitionitems.branch.BranchRepository;
import com.backend.recognitionitems.branch.dto.Branch;
import com.backend.recognitionitems.brand.BrandRepository;
import com.backend.recognitionitems.brand.dto.Brand;
import com.backend.recognitionitems.cashiercheckout.CashierCheckoutRepository;
import com.backend.recognitionitems.cashiercheckout.dto.CashierCheckout;
import com.backend.recognitionitems.product.ProductRepository;
import com.backend.recognitionitems.product.dto.Product;
import com.backend.recognitionitems.user.dto.User;
import com.backend.recognitionitems.user.dto.UserType;
import com.backend.recognitionitems.user.dto.request.AddUserRequestDto;
import com.backend.recognitionitems.user.dto.request.UpdateUserRequestDto;
import com.backend.recognitionitems.user.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final CashierCheckoutRepository cashierCheckoutRepository;
    private final String defaultLogo = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAQAAADa613fAAAAaElEQVR42u3PQREAAAwCoNm/9CL496ABuREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREWkezG8AZQ6nfncAAAAASUVORK5CYII=";

    @Transactional
    public User getUser(String username) {
        return userRepository.findById(Long.valueOf(username)).orElseThrow(
                () ->
                new UsernameNotFoundException("User not found [username: " + username + "]"));
    }
    @Transactional
    public User getUserByUserId(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("User not found [user id: " + userId + "]"));
    }
    @Transactional
    public void addUser(AddUserRequestDto addUserRequestDto, UserType userType) {
        User user = User.builder()
                .name(addUserRequestDto.getName())
                .surname(addUserRequestDto.getSurname())
                .password(addUserRequestDto.getPassword())
                .email(addUserRequestDto.getEmail())
                .role(userType)
                .build();
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already exists!");
        }
        userRepository.save(user);
    }
    @Transactional
    public void updateUser(User existingUser, UpdateUserRequestDto updateUserRequestDto) {
        existingUser.setName(updateUserRequestDto.getName());
        existingUser.setSurname(updateUserRequestDto.getSurname());
        existingUser.setEmail(updateUserRequestDto.getEmail());
        userRepository.save(existingUser);
    }
    @Transactional
    public List<User> getAllUsers(UserType userType) {
        return userRepository.findByRole(userType);
    }
    @Transactional
    public boolean deleteUser(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        } else {
            return false;
        }
    }
    @Transactional
    public List<LocalAdminResponseDto> getLocalAdmins() {
        List<User> users = userRepository.findByRole(UserType.LOCAL_ADMIN);
        return users.stream()
                .map(user -> {
                    Brand brand = brandRepository.findByLocalAdminId(user.getId()).orElse(null);
                    return LocalAdminResponseDto.builder()
                            .userId(user.getId())
                            .name(user.getName())
                            .surname(user.getSurname())
                            .email(user.getEmail())
                            .password(user.getPassword())
                            .role(user.getRole())
                            .brandId(brand == null ? null : brand.getId())
                            .brandName(brand == null ? null : brand.getBrandName())
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public List<LocalAdminResponseDto> getLocalAdminsWithNoBrands() {
        List<User> users = userRepository.findByRole(UserType.LOCAL_ADMIN);
        List<LocalAdminResponseDto> localAdmins = new ArrayList<>();

        for (User user : users) {
            Brand brand = brandRepository.findByLocalAdminId(user.getId()).orElse(null);

            if (brand == null) {
                LocalAdminResponseDto localAdmin = new LocalAdminResponseDto();
                localAdmin.setUserId(user.getId());
                localAdmin.setName(user.getName());
                localAdmin.setSurname(user.getSurname());
                localAdmin.setEmail(user.getEmail());
                localAdmin.setPassword(user.getPassword());
                localAdmin.setRole(user.getRole());
                localAdmin.setBrandId(null);
                localAdmin.setBrandName(null);
                localAdmins.add(localAdmin);
            }
        }

        return localAdmins;
    }
    @Transactional
    public List<ProductOwnerResponseDto> getProductOwners() {
        List<User> users = userRepository.findByRole(UserType.PRODUCT_OWNER);
        return users.stream()
                .map(user -> {
                    Product product = productRepository.findByProductOwnerId(user.getId()).orElse(null);
                    return ProductOwnerResponseDto.builder()
                            .userId(user.getId())
                            .name(user.getName())
                            .surname(user.getSurname())
                            .email(user.getEmail())
                            .password(user.getPassword())
                            .role(user.getRole())
                            .productId(product == null ? null : product.getId())
                            .productName(product == null ? null : product.getProductName())
                            .productLogo(product == null ? defaultLogo : product.getProductLogo())
                            .build();
                })
                .collect(Collectors.toList());
    }
    @Transactional
    public List<ProductOwnerResponseDto> getProductOwnersWithNoProducts() {
        List<User> users = userRepository.findByRole(UserType.PRODUCT_OWNER);
        List<ProductOwnerResponseDto> productOwners = new ArrayList<>();

        for (User user : users) {
            Product product = productRepository.findByProductOwnerId(user.getId()).orElse(null);

            if (product == null) {
                ProductOwnerResponseDto productOwner = new ProductOwnerResponseDto();
                productOwner.setUserId(user.getId());
                productOwner.setName(user.getName());
                productOwner.setSurname(user.getSurname());
                productOwner.setEmail(user.getEmail());
                productOwner.setPassword(user.getPassword());
                productOwner.setRole(user.getRole());
                productOwner.setProductId(null);
                productOwner.setProductName(null);
                productOwner.setProductLogo(defaultLogo);
                productOwners.add(productOwner);
            }
        }

        return productOwners;
    }
    @Transactional
    public List<BranchManagerResponseDto> getBranchManagers(Long localAdminId) {
        Brand brand = brandRepository.findByLocalAdminId(localAdminId).orElse(null);
        if (brand == null) {
            return null;
        }

        List<Branch> branches = brand.getBranches();

        List<User> users = branches.stream().map(
                branch -> branch.getBranchManager()).collect(Collectors.toList());

        return users.stream()
                .map(user -> {
                    Branch branch = branchRepository.findByBranchManagerId(user.getId()).orElse(null);
                    return BranchManagerResponseDto.builder()
                                .userId(user.getId())
                                .name(user.getName())
                                .surname(user.getSurname())
                                .email(user.getEmail())
                                .password(user.getPassword())
                                .role(user.getRole())
                                .brandId((branch == null || (branch.getBrand() == null)) ? null : branch.getBrand().getId())
                                .brandName((branch == null || (branch.getBrand() == null)) ? null : branch.getBrand().getBrandName())
                                .branchId(branch == null ? null : branch.getId())
                                .branchName(branch == null ? null : branch.getBranchName())
                                .build();
                })
                .collect(Collectors.toList());
    }
    @Transactional
    public List<BranchManagerResponseDto> getBranchManagersWithNoBranches() {
        List<User> users = userRepository.findByRole(UserType.BRANCH_MANAGER);
        List<BranchManagerResponseDto> branchManagers = new ArrayList<>();

        for (User user : users) {
            Branch branch = branchRepository.findByBranchManagerId(user.getId()).orElse(null);

            if (branch == null) {
                BranchManagerResponseDto branchManager = new BranchManagerResponseDto();
                branchManager.setUserId(user.getId());
                branchManager.setName(user.getName());
                branchManager.setSurname(user.getSurname());
                branchManager.setEmail(user.getEmail());
                branchManager.setPassword(user.getPassword());
                branchManager.setRole(user.getRole());
                branchManager.setBranchId(null);
                branchManager.setBranchName(null);
                branchManagers.add(branchManager);
            }
        }

        return branchManagers;
    }
    @Transactional
    public List<TechnicalStaffResponseDto> getTechnicalStaffs(Long localAdminId) {
        Brand brand = brandRepository.findByLocalAdminId(localAdminId).orElse(null);
        if (brand == null) {
            return null;
        }

        List<Branch> branches = brand.getBranches();

        List<User> users = branches.stream().map(
                branch -> branch.getTechnicalStaff()).collect(Collectors.toList());

        return users.stream()
                .map(user -> {
                    Branch branch = branchRepository.findByTechnicalStaffId(user.getId()).orElse(null);
                    return TechnicalStaffResponseDto.builder()
                            .userId(user.getId())
                            .name(user.getName())
                            .surname(user.getSurname())
                            .email(user.getEmail())
                            .password(user.getPassword())
                            .role(user.getRole())
                            .brandId(branch == null ? null : branch.getBrand().getId())
                            .brandName(branch == null ? null : branch.getBrand().getBrandName())
                            .branchId(branch == null ? null : branch.getId())
                            .branchName(branch == null ? null : branch.getBranchName())
                            .build();
                })
                .collect(Collectors.toList());
    }
    @Transactional
    public List<TechnicalStaffResponseDto> getTechnicalStaffsWithNoBranches() {
        List<User> users = userRepository.findByRole(UserType.TECHNICAL_STAFF);
        List<TechnicalStaffResponseDto> technicalStaffs = new ArrayList<>();

        for (User user : users) {
            Branch branch = branchRepository.findByTechnicalStaffId(user.getId()).orElse(null);

            if (branch == null) {
                TechnicalStaffResponseDto technicalStaff = new TechnicalStaffResponseDto();
                technicalStaff.setUserId(user.getId());
                technicalStaff.setName(user.getName());
                technicalStaff.setSurname(user.getSurname());
                technicalStaff.setEmail(user.getEmail());
                technicalStaff.setPassword(user.getPassword());
                technicalStaff.setRole(user.getRole());
                technicalStaff.setBranchId(null);
                technicalStaff.setBranchName(null);
                technicalStaffs.add(technicalStaff);
            }
        }

        return technicalStaffs;
    }

    @Transactional
    public List<CashierResponseDto> getCashiers(Long userId) {
        Branch branch = branchRepository.findById(userId).orElse(null);
        if (branch == null) {
            return null;
        }
        List<User> users = branch.getCashiers();
        return users.stream()
                .map(user -> {
                    CashierCheckout cashierCheckout = cashierCheckoutRepository.findByCashierId(user.getId()).orElse(null);
                    return CashierResponseDto.builder()
                            .userId(user.getId())
                            .name(user.getName())
                            .surname(user.getSurname())
                            .email(user.getEmail())
                            .password(user.getPassword())
                            .role(user.getRole())
                            .brandId(cashierCheckout == null ? null : cashierCheckout.getBranch().getBrand().getId())
                            .brandName(cashierCheckout == null ? null : cashierCheckout.getBranch().getBrand().getBrandName())
                            .branchId(cashierCheckout == null ? null : cashierCheckout.getBranch().getId())
                            .branchName(cashierCheckout == null ? null : cashierCheckout.getBranch().getBranchName())
                            .cashierCheckoutId(cashierCheckout == null ? null : cashierCheckout.getId())
                            .build();
                })
                .collect(Collectors.toList());
    }
    @Transactional
    public List<CashierResponseDto> getCashiersWithNoCashierCheckouts() {
        List<User> users = userRepository.findByRole(UserType.CASHIER);
        List<CashierResponseDto> cashiers = new ArrayList<>();

        for (User user : users) {
            CashierCheckout cashierCheckout = cashierCheckoutRepository.findByCashierId(user.getId()).orElse(null);

            //TODO: check this function
            if (cashierCheckout == null) {
                CashierResponseDto cashier = new CashierResponseDto();
                cashier.setUserId(user.getId());
                cashier.setName(user.getName());
                cashier.setSurname(user.getSurname());
                cashier.setEmail(user.getEmail());
                cashier.setPassword(user.getPassword());
                cashier.setRole(user.getRole());
                cashier.setBrandId(null);
                cashier.setBrandName(null);
                cashier.setBranchId(null);
                cashier.setBranchName(null);
                cashiers.add(cashier);
            }
        }

        return cashiers;
    }
    @Transactional
    public LocalAdminResponseDto getLocalAdmin(Long userId) {
        User user = userRepository.findByIdAndRole(userId, UserType.LOCAL_ADMIN);
        Brand brand = brandRepository.findByLocalAdminId(user.getId()).orElse(null);
        return LocalAdminResponseDto.builder()
                            .userId(user.getId())
                            .name(user.getName())
                            .surname(user.getSurname())
                            .email(user.getEmail())
                            .password(user.getPassword())
                            .role(user.getRole())
                            .brandId(brand == null ? null : brand.getId())
                            .brandName(brand == null ? null : brand.getBrandName())
                            .build();
    }
    @Transactional
    public ProductOwnerResponseDto getProductOwner(Long userId) {
        User user = userRepository.findByIdAndRole(userId, UserType.PRODUCT_OWNER);
        Product product = productRepository.findByProductOwnerId(user.getId()).orElse(null);
        return ProductOwnerResponseDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .productId(product == null ? null : product.getId())
                .productName(product == null ? null : product.getProductName())
                .productLogo(product == null ? defaultLogo : product.getProductLogo())
                .build();
    }
    @Transactional
    public BranchManagerResponseDto getBranchManager(Long userId) {
        User user = userRepository.findByIdAndRole(userId, UserType.BRANCH_MANAGER);
        Branch branch = branchRepository.findByBranchManagerId(user.getId()).orElse(null);
        return BranchManagerResponseDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .brandId((branch == null || (branch.getBrand() == null)) ? null : branch.getBrand().getId())
                .brandName((branch == null || (branch.getBrand() == null)) ? null : branch.getBrand().getBrandName())
                .branchId(branch == null ? null : branch.getId())
                .branchName(branch == null ? null : branch.getBranchName())
                .build();
    }
    @Transactional
    public TechnicalStaffResponseDto getTechnicalStaff(Long userId) {
        User user = userRepository.findByIdAndRole(userId, UserType.TECHNICAL_STAFF);
        Branch branch = branchRepository.findByBranchManagerId(user.getId()).orElse(null);
        return TechnicalStaffResponseDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .brandId((branch == null || (branch.getBrand() == null)) ? null : branch.getBrand().getId())
                .brandName((branch == null || (branch.getBrand() == null)) ? null : branch.getBrand().getBrandName())
                .branchId(branch == null ? null : branch.getId())
                .branchName(branch == null ? null : branch.getBranchName())
                .build();
    }

        @Transactional
    public CashierResponseDto getCashier(Long userId) {
        User user = userRepository.findByIdAndRole(userId, UserType.CASHIER);
        CashierCheckout cashierCheckout = cashierCheckoutRepository.findByCashierId(user.getId()).orElse(null);
        return CashierResponseDto.builder()
                .userId(user.getId())
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .brandId(cashierCheckout == null ? null : cashierCheckout.getBranch().getBrand().getId())
                .brandName(cashierCheckout == null ? null :  cashierCheckout.getBranch().getBrand().getBrandName())
                .branchId(cashierCheckout == null ? null : cashierCheckout.getBranch().getId())
                .branchName(cashierCheckout == null ? null : cashierCheckout.getBranch().getBranchName())
                .cashierCheckoutId(cashierCheckout == null ? null : cashierCheckout.getId())
                .build();
    }
}
