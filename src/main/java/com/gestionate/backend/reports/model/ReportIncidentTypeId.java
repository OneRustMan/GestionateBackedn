package com.gestionate.backend.reports.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReportIncidentTypeId implements Serializable {

    @Column(name = "report_id", nullable = false)
    private Long reportId;

    @Column(name = "incident_type_id", nullable = false)
    private Long incidentTypeId;
}
