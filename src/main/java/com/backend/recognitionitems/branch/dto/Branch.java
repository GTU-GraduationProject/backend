package com.backend.recognitionitems.branch.dto;

import com.backend.recognitionitems.brand.dto.Brand;
import com.backend.recognitionitems.cashiercheckout.dto.CashierCheckout;
import com.backend.recognitionitems.items.dto.Item;
import com.backend.recognitionitems.product.dto.Product;
import com.backend.recognitionitems.user.dto.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "branches")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "branch_sequence")
    @SequenceGenerator(name = "branch_sequence", sequenceName = "branch_sequence",
            allocationSize = 1, initialValue = 10000)
    private Long id;

    @NotBlank
    private String branchName;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @OneToOne
    @JoinColumn(name = "branch_manager_id")
    private User branchManager;

    @OneToOne
    @JoinColumn(name = "technical_staff_id")
    private User technicalStaff;

    @OneToMany
    @JoinColumn(name = "branch_id")
    private List<User> cashiers;

    @ManyToMany(mappedBy = "branches")
    private List<Product> products;

    @OneToMany(mappedBy = "branch")
    private List<Item> items;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL)
    private List<CashierCheckout> cashierCheckouts;
}
