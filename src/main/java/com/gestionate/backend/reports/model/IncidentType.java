package com.gestionate.backend.reports.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "incident_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IncidentType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 60)
    private IncidentTypeName name;

    @Column(nullable = false)
    private Boolean active;
}
