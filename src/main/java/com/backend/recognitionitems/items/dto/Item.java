package com.backend.recognitionitems.items.dto;

import com.backend.recognitionitems.branch.dto.Branch;
import com.backend.recognitionitems.product.dto.Product;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "items")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_sequence")
    @SequenceGenerator(name = "item_sequence", sequenceName = "item_sequence",
            allocationSize = 1, initialValue = 10000)
    private Long id;

    @NotBlank
    private String itemName;

    @NotNull
    private Long itemTotalAmount;

    @NotNull
    private Long itemRemainingAmount;

    @NotNull
    private Long itemSoldAmount;

    @NotNull
    private Boolean isReported;

    @Column(columnDefinition = "bytea")
    private byte[] photosZip;

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
}
