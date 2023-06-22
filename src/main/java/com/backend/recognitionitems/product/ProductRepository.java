package com.backend.recognitionitems.product;

import com.backend.recognitionitems.brand.dto.Brand;
import com.backend.recognitionitems.product.dto.Product;
import com.backend.recognitionitems.user.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByProductOwner(User user);

    boolean existsByProductName(String productName);

    Optional<Product> findByProductOwnerId(Long id);
}
