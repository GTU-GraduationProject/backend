package com.backend.recognitionitems.brand;

import com.backend.recognitionitems.brand.dto.Brand;
import com.backend.recognitionitems.brand.dto.request.AddBrandRequestDto;
import com.backend.recognitionitems.brand.dto.request.EditBrandRequestDto;
import com.backend.recognitionitems.brand.dto.response.BrandResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;
    private final BrandRepository brandRepository;
    @Operation(summary = "Create brand. General Admin can.")
    @PostMapping("/brand")
    public ResponseEntity<String> addBrand(@RequestBody AddBrandRequestDto addBrandRequestDto) {
        try {
            brandService.addBrand(addBrandRequestDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Invalid request: " + ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "List brand. General Admin can.")
    @GetMapping("/brand/brands")
    public ResponseEntity<List<BrandResponseDto>> getAllBrands() {
        List<BrandResponseDto> brands = brandService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

    @Operation(summary = "Edit brand. General Admin can.")
    @PostMapping("/brand/{brandId}")
    public ResponseEntity<Void> editBrand(@PathVariable Long brandId,
                                                 @RequestBody EditBrandRequestDto editBrandRequestDto) {
        Brand existingBrand = brandRepository.findById(brandId).orElse(null);

        if (existingBrand == null) {
            // there is no brand
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        boolean edited = brandService.editBrand(existingBrand, editBrandRequestDto);
        if (edited) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Delete brand. General Admin can.")
    @DeleteMapping("/brand/{brandId}")
    public ResponseEntity<String> deleteBrand(@PathVariable Long brandId) {
        boolean deleted = brandService.deleteBrand(brandId);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(summary = "List brand for specific local admin. Local Admin can.")
    @GetMapping("/brand/{userId}")
    public ResponseEntity<List<BrandResponseDto>> getAllBrands(@PathVariable Long userId) {
        List<BrandResponseDto> brands = brandService.getAllBrands(userId);
        return ResponseEntity.ok(brands);
    }
}
