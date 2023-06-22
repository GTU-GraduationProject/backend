package com.backend.recognitionitems.cashiercheckout;

import com.backend.recognitionitems.cashiercheckout.dto.CashierCheckout;
import com.backend.recognitionitems.cashiercheckout.dto.request.AddCashierRequestDto;
import com.backend.recognitionitems.cashiercheckout.dto.request.EditCashierCheckoutRequestDto;
import com.backend.recognitionitems.cashiercheckout.dto.response.CashierCheckoutResponseDto;
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
public class CashierCheckoutController {
    private final CashierCheckoutService cashierCheckoutService;
    private final CashierCheckoutRepository cashierCheckoutRepository;
    @Operation(summary = "Create cashier checkout. Branch manager can.")
    @PostMapping("/cashier-checkout")
    public ResponseEntity<String> addCashierCheckout(@RequestBody AddCashierRequestDto addCashierRequestDto) {
        try {
            cashierCheckoutService.addCashierCheckout(addCashierRequestDto);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Invalid request: " + ex.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "List cashier checkouts. Branch manager can.")
    @GetMapping("/cashier-checkout/cashier-checkouts")
    public ResponseEntity<List<CashierCheckoutResponseDto>> getAllCashierCheckouts() {
        List<CashierCheckoutResponseDto> cashierCheckouts = cashierCheckoutService.getAllCashierCheckouts();
        return ResponseEntity.ok(cashierCheckouts);
    }

    @Operation(summary = "Edit cashier checkout. Branch manager can.")
    @PostMapping("/cashier-checkout/{cashierCheckoutId}")
    public ResponseEntity<Void> editCashierCheckout(@PathVariable Long cashierCheckoutId,
                                           @RequestBody EditCashierCheckoutRequestDto editCashierCheckoutRequestDto) {
        CashierCheckout existingCashierCheckout = cashierCheckoutRepository.findById(cashierCheckoutId).orElse(null);

        if (cashierCheckoutId == null) {
            // there is no cashier checkout
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        boolean edited = cashierCheckoutService.editCashierCheckout(existingCashierCheckout, editCashierCheckoutRequestDto);
        if (edited) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Operation(summary = "Delete cashier checkout. Branch manager can.")
    @DeleteMapping("/cashier-checkout/{cashierCheckoutId}")
    public ResponseEntity<String> deleteCashierCheckout(@PathVariable Long cashierCheckoutId) {
        boolean deleted = cashierCheckoutService.deleteCashierCheckout(cashierCheckoutId);
        if (deleted) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
