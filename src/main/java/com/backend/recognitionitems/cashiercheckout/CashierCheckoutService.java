package com.backend.recognitionitems.cashiercheckout;

import com.backend.recognitionitems.branch.BranchRepository;
import com.backend.recognitionitems.branch.dto.Branch;
import com.backend.recognitionitems.cashiercheckout.dto.CashierCheckout;
import com.backend.recognitionitems.cashiercheckout.dto.request.AddCashierRequestDto;
import com.backend.recognitionitems.cashiercheckout.dto.request.EditCashierCheckoutRequestDto;
import com.backend.recognitionitems.cashiercheckout.dto.response.CashierCheckoutBranchIdResponseDto;
import com.backend.recognitionitems.cashiercheckout.dto.response.CashierCheckoutResponseDto;
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
public class CashierCheckoutService {
    private final CashierCheckoutRepository cashierCheckoutRepository;
    private final UserRepository userRepository;
    private final BranchRepository branchRepository;

    @Transactional
    public void addCashierCheckout(AddCashierRequestDto addCashierRequestDto) {
        User cashier = userRepository.findById(addCashierRequestDto.getCashierId()).orElse(null);
        if (cashier == null) {
            throw new IllegalArgumentException("The cashier does not exist!");
        }

        Branch branch = branchRepository.findById(addCashierRequestDto.getBranchId()).orElse(null);
        if (branch == null) {
            throw new IllegalArgumentException("The branch does not exist!");
        }

        CashierCheckout cashierCheckoutExisting = cashierCheckoutRepository.findByCashierId(cashier.getId()).orElse(null);
        if (cashierCheckoutExisting != null) {
            throw new IllegalArgumentException("The cashier already has a cashier checkout!");
        }
        CashierCheckout cashierCheckout = CashierCheckout.builder()
                .cameraId(addCashierRequestDto.getCameraId())
                .cashier(cashier)
                .branch(branch)
                .build();

        cashierCheckoutRepository.save(cashierCheckout);
    }
    @Transactional
    public List<CashierCheckoutResponseDto> getAllCashierCheckouts(Long branchManagerId) {
        User branchManager = userRepository.findById(branchManagerId).orElse(null);
        if (branchManager == null) {
            return null;
        }
        Branch branch = branchRepository.findByBranchManagerId(branchManagerId).orElse(null);
        if (branch == null) {
            return null;
        }
        List<CashierCheckout> cashierCheckouts = branch.getCashierCheckouts();

        return cashierCheckouts.stream()
                .map(cashierCheckout ->
                        CashierCheckoutResponseDto.builder()
                                .cashierCheckoutId(cashierCheckout.getId())
                                .cameraId(cashierCheckout.getCameraId())
                                .cashierId(cashierCheckout.getCashier().getId())
                                .cashierName(cashierCheckout.getCashier().getName())
                                .cashierSurname(cashierCheckout.getCashier().getSurname())
                                .build()
                )
                .collect(Collectors.toList());
    }

    @Transactional
    public boolean editCashierCheckout(CashierCheckout existingCashierCheckout,
                                       EditCashierCheckoutRequestDto editCashierCheckoutRequestDto) {
        Optional<User> cashier = Optional.ofNullable(userRepository.findById(editCashierCheckoutRequestDto.getCashierId())
                .orElseThrow(() -> new IllegalArgumentException("Cashier does not exist!")));
        if (!cashier.get().getRole().equals(UserType.CASHIER)) {
            throw new IllegalArgumentException("Cashier does not exist!");
        }
        /*
        if (cashierCheckoutRepository.findByCashierId(cashier.get().getId()) != null) {
            throw new IllegalArgumentException("The branch manager does already has branch!");
        }
        */
        existingCashierCheckout.setCashier(cashier.get());
        existingCashierCheckout.setCameraId(editCashierCheckoutRequestDto.getCameraId());

        cashierCheckoutRepository.save(existingCashierCheckout);
        return true;
    }

    @Transactional
    public boolean deleteCashierCheckout(Long cashierCheckoutId) {
        Optional<CashierCheckout> cashierCheckout = cashierCheckoutRepository.findById(cashierCheckoutId);
        if (cashierCheckout.isPresent()) {
            cashierCheckoutRepository.delete(cashierCheckout.get());
            return true;
        } else {
            return false;
        }
    }

    public CashierCheckoutBranchIdResponseDto getBranchId(Long userId) {
        Branch branch = branchRepository.findById(userId).orElse(null);
        if (branch == null) {
            return null;
        }

        return CashierCheckoutBranchIdResponseDto.builder()
                .branchId(branch.getId())
                .build();
    }
}
