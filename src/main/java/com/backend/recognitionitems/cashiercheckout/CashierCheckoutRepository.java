package com.backend.recognitionitems.cashiercheckout;

import com.backend.recognitionitems.cashiercheckout.dto.CashierCheckout;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CashierCheckoutRepository extends JpaRepository<CashierCheckout, Long> {
    Optional<CashierCheckout> findByCashierId(Long id);
}
