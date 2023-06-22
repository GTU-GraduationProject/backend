package com.backend.recognitionitems.product;

import com.backend.recognitionitems.product.dto.request.AddProductRequestDto;
import com.backend.recognitionitems.product.dto.Product;
import com.backend.recognitionitems.product.dto.request.EditProductRequestDto;
import com.backend.recognitionitems.product.dto.response.ProductResponseDto;
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
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    private final String defaultLogo = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAGQAAABkCAQAAADa613fAAAAaElEQVR42u3PQREAAAwCoNm/9CL496ABuREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREREWkezG8AZQ6nfncAAAAASUVORK5CYII=";

    @Transactional
    public void addProduct(AddProductRequestDto addProductRequestDto) {
        Optional<User> productOwner = Optional.ofNullable(userRepository.findById(addProductRequestDto.getProductOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Product owner does not exist!")));;
        if (!productOwner.get().getRole().equals(UserType.PRODUCT_OWNER)) {
            throw new IllegalArgumentException("Product owner does not exist!");
        }

        boolean productOwnerHasProduct = productRepository.existsByProductOwner(productOwner.get());
        if (productOwnerHasProduct) {
            throw new IllegalArgumentException("The product owner already has a product!");
        }
        Product product = Product.builder()
                .productName(addProductRequestDto.getProductName())
                .isReported(true)
                .productLogo(addProductRequestDto.getProductLogo())
                .productOwner(productOwner.get())
                .build();

        if (productRepository.existsByProductName(product.getProductName())) {
            throw new IllegalArgumentException("Product already exists!");
        }

        productRepository.save(product);
    }
    @Transactional
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product ->
                        ProductResponseDto.builder()
                                .productId(product.getId())
                                .productName(product.getProductName())
                                .productLogo(product.getProductLogo() == null ? defaultLogo : product.getProductLogo())
                                .productOwnerId(product.getProductOwner().getId())
                                .productOwnerName(product.getProductOwner().getName())
                                .productOwnerSurname(product.getProductOwner().getSurname())
                            .build()
                )
                .collect(Collectors.toList());
    }
    @Transactional
    public boolean editProduct(Product existingProduct, EditProductRequestDto editProductRequestDto) {
        Optional<User> productOwner = Optional.ofNullable(userRepository.findById(editProductRequestDto.getProductOwnerId())
                .orElseThrow(() -> new IllegalArgumentException("Product owner does not exist!")));
        if (!productOwner.get().getRole().equals(UserType.PRODUCT_OWNER)) {
            throw new IllegalArgumentException("Product owner does not exist!");
        }

        if (existingProduct.getProductName().equals(editProductRequestDto.getProductName())) {
            throw new IllegalArgumentException("Product name already exists!");
        }

        if (productRepository.existsByProductName(editProductRequestDto.getProductName())) {
            throw new IllegalArgumentException("Product name already exists!");
        }

        existingProduct.setProductName(editProductRequestDto.getProductName());
        existingProduct.setProductLogo(editProductRequestDto.getProductLogo());
        existingProduct.setProductOwner(productOwner.get());
        /*
        if (!existingProduct.getProductOwner().getId().equals(editProductRequestDto.getProductOwnerId())) {
            Product hasProduct = productRepository.findByProductOwnerId(productOwner.get().getId())
                    .orElse(null);
            if (hasProduct == null) {
                existingProduct.setProductOwner(productOwner.get());
            } else {
                System.out.println("the product owner already has product");
                return false;
            }
        }
         */
        productRepository.save(existingProduct);
        return true;
    }
    @Transactional
    public boolean deleteProduct(Long productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            productRepository.delete(product.get());
            return true;
        } else {
            return false;
        }
    }
}
