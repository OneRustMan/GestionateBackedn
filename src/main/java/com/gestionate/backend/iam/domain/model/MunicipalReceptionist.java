package com.gestionate.backend.iam.domain.model;

import com.gestionate.backend.shared.domain.model.Municipality;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "receptionist_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MunicipalReceptionist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "municipality_id", nullable = false)
    private Municipality municipality;

    @Enumerated(EnumType.STRING)
    @Column(name = "municipal_unit", nullable = false, length = 50)
    private MunicipalUnit municipalUnit;

    @Column(name = "worker_code", nullable = false, unique = true, length = 30)
    private String workerCode;
}
