package com.backend.recognitionitems.brand;

import com.backend.recognitionitems.brand.dto.Brand;
import com.backend.recognitionitems.user.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    boolean existsByBrandName(String brandName);

    boolean existsByLocalAdmin(User user);

    Optional<Brand> findByLocalAdminId(Long id);

    List<Brand> findAllByLocalAdminId(Long userId);
}