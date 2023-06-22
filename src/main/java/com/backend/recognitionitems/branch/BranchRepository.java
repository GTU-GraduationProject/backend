package com.backend.recognitionitems.branch;

import com.backend.recognitionitems.branch.dto.Branch;
import com.backend.recognitionitems.user.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    Optional<Branch> findByBranchManagerId(Long id);

    boolean existsByBranchManager(User user);

    boolean existsByTechnicalStaff(User user);

    Optional<Branch> findByTechnicalStaffId(Long id);

    boolean existsByBranchManagerId(Long id);

    boolean existsByTechnicalStaffId(Long id);

    List<Branch> findAllByBrandId(Long id);
}
