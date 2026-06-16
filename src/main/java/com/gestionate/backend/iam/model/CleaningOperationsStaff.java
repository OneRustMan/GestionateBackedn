package com.gestionate.backend.iam.model;

import com.gestionate.backend.shared.model.Municipality;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cleaning_staff_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CleaningOperationsStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "municipality_id", nullable = false)
    private Municipality municipality;

    @Column(name = "worker_code", nullable = false, unique = true, length = 30)
    private String workerCode;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Shift shift;
}
