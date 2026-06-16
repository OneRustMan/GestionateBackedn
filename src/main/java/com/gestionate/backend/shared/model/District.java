package com.gestionate.backend.shared.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "districts", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "name", "province" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class District {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 100)
    private String province;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}
