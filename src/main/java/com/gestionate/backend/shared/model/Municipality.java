package com.gestionate.backend.shared.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "municipalities", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "district_id", "name" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Municipality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "district_id", nullable = false)
    private District district;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(nullable = false)
    private Boolean active;
}
