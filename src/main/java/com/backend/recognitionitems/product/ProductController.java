package com.backend.recognitionitems.product;

import com.backend.recognitionitems.product.dto.Product;
import com.backend.recognitionitems.product.dto.request.AddProductRequestDto;
import com.backend.recognitionitems.product.dto.request.EditProductRequestDto;
import com.backend.recognitionitems.product.dto.response.ProductResponseDto;
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
public class ProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;

    @Operation(summary = "Create product. General Admin can.")
    @PostMapping("/product")
    public ResponseEntity<String> addProduct(@RequestBody AddProductRequestDto addProductRequestDto) {
        try {
            productService.addProduct(addProductRequestDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Invalid request: " + ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "List product. General Admin can.")
    @GetMapping("/product/products")
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Edit product. General Admin can.")
    @PostMapping("/product/{productId}")
    public ResponseEntity<Void> editProduct(@PathVariable Long productId,
                                          @RequestBody EditProductRequestDto editProductRequestDto) {
        Product existingProduct = productRepository.findById(productId).orElse(null);

        if (existingProduct == null) {
            // there is no brand
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        boolean edited = productService.editProduct(existingProduct, editProductRequestDto);
        if (edited) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Delete product. General Admin can.")
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        boolean deleted = productService.deleteProduct(productId);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
