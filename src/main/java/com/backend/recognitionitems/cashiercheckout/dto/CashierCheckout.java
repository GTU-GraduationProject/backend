package com.backend.recognitionitems.cashiercheckout.dto;

import com.backend.recognitionitems.branch.dto.Branch;
import com.backend.recognitionitems.user.dto.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "cashier_checkouts")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class CashierCheckout {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cashier_checkout_sequence")
    @SequenceGenerator(name = "cashier_checkout_sequence", sequenceName = "cashier_checkout_sequence",
            allocationSize = 1, initialValue = 10000)
    private Long id;

    @NotBlank
    private String cameraId;

    @OneToOne
    @JoinColumn(name = "cashier_id")
    private User cashier;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
}
