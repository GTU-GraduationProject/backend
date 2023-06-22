package com.backend.recognitionitems.product.dto;

import com.backend.recognitionitems.branch.dto.Branch;
import com.backend.recognitionitems.user.dto.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
    @SequenceGenerator(name = "product_sequence", sequenceName = "product_sequence",
            allocationSize = 1, initialValue = 10000)
    private Long id;

    @NotBlank
    private String productName;

    private Boolean isReported;

    @NotBlank
    @Lob
    private String productLogo;

    @ManyToOne
    @JoinColumn(name = "product_owner_id")
    private User productOwner;

    @ManyToMany
    @JoinTable(
            name = "branch_product",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "branch_id")
    )
    private List<Branch> branches;
}
