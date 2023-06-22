package com.backend.recognitionitems.brand;

import com.backend.recognitionitems.brand.dto.Brand;
import com.backend.recognitionitems.brand.dto.request.AddBrandRequestDto;
import com.backend.recognitionitems.brand.dto.request.EditBrandRequestDto;
import com.backend.recognitionitems.brand.dto.response.BrandResponseDto;
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
public class BrandService {
    private final BrandRepository brandRepository;
    private final UserRepository userRepository;
    @Transactional
    public void addBrand(AddBrandRequestDto addBrandRequestDto) {
        Optional<User> localAdmin = Optional.ofNullable(userRepository.findById(addBrandRequestDto.getLocalAdminId())
                .orElseThrow(() -> new IllegalArgumentException("Local admin does not exist!")));
        if (!localAdmin.get().getRole().equals(UserType.LOCAL_ADMIN)) {
            throw new IllegalArgumentException("Local admin does not exist!");
        }

        boolean localAdminHasBrand = brandRepository.existsByLocalAdmin(localAdmin.get());
        if (localAdminHasBrand) {
            throw new IllegalArgumentException("The local admin already has a brand!");
        }

        Brand brand = Brand.builder()
                .brandName(addBrandRequestDto.getBrandName())
                .brandLogo(addBrandRequestDto.getBrandLogo())
                .localAdmin(localAdmin.get())
                .build();
        if (brandRepository.existsByBrandName(brand.getBrandName())) {
            throw new IllegalArgumentException("Brand already exists!");
        }

        brandRepository.save(brand);
    }
    @Transactional
    public List<BrandResponseDto> getAllBrands() {
        List<Brand> brands = brandRepository.findAll();
        return brands.stream()
                .map(brand ->
                        BrandResponseDto.builder()
                            .brandId(brand.getId())
                            .brandName(brand.getBrandName())
                            .brandLogo(brand.getBrandLogo())
                            .localAdminId(brand.getLocalAdmin().getId())
                            .localAdminName(brand.getLocalAdmin().getName())
                            .localAdminSurname(brand.getLocalAdmin().getSurname())
                            .build()
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean editBrand(Brand existingBrand, EditBrandRequestDto editBrandRequestDto) {
        Optional<User> localAdmin = Optional.ofNullable(userRepository.findById(editBrandRequestDto.getLocalAdminId())
                .orElseThrow(() -> new IllegalArgumentException("Local admin does not exist!")));
        if (!localAdmin.get().getRole().equals(UserType.LOCAL_ADMIN)) {
            throw new IllegalArgumentException("Local admin does not exist!");
        }

        existingBrand.setBrandName(editBrandRequestDto.getBrandName());
        if (!existingBrand.getBrandName().equals(editBrandRequestDto.getBrandName()) &&
                brandRepository.existsByBrandName(existingBrand.getBrandName())) {
            throw new IllegalArgumentException("Brand name already exists!");
        }
        existingBrand.setBrandLogo(editBrandRequestDto.getBrandLogo());
        existingBrand.setLocalAdmin(localAdmin.get());
        /*
        if (!existingBrand.getLocalAdmin().getId().equals(editBrandRequestDto.getLocalAdminId())) {
            Brand hasBrand = brandRepository.findByLocalAdminId(localAdmin.get().getId())
                    .orElse(null);
            if (hasBrand == null) {

            } else {
                System.out.println("the local admin already has brand");
                return false;
            }
        }
        */
        brandRepository.save(existingBrand);
        return true;
    }
    @Transactional
    public boolean deleteBrand(Long brandId) {
        Optional<Brand> brand = brandRepository.findById(brandId);
        if (brand.isPresent()) {
            brandRepository.delete(brand.get());
            return true;
        } else {
            return false;
        }
    }
    @Transactional
    public List<BrandResponseDto> getAllBrands(Long userId) {
        List<Brand> brands = brandRepository.findAllByLocalAdminId(userId);
        return brands.stream()
                .map(brand ->
                        BrandResponseDto.builder()
                                .brandId(brand.getId())
                                .brandName(brand.getBrandName())
                                .brandLogo(brand.getBrandLogo())
                                .localAdminId(brand.getLocalAdmin().getId())
                                .localAdminName(brand.getLocalAdmin().getName())
                                .localAdminSurname(brand.getLocalAdmin().getSurname())
                                .build()
                )
                .collect(Collectors.toList());
    }
}
