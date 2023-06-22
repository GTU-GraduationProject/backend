package com.backend.recognitionitems.brand.dto;

import com.backend.recognitionitems.branch.dto.Branch;
import com.backend.recognitionitems.user.dto.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "brands")
@Getter
@Setter
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brand_sequence")
    @SequenceGenerator(name = "brand_sequence", sequenceName = "brand_sequence",
            allocationSize = 1, initialValue = 10000)
    private Long id;

    @NotBlank
    private String brandName;

    @NotBlank
    @Lob
    private String brandLogo;

    @OneToOne
    @JoinColumn(name = "local_admin_id")
    private User localAdmin;

    @OneToMany(mappedBy = "brand", cascade = CascadeType.ALL)
    private List<Branch> branches;
}

