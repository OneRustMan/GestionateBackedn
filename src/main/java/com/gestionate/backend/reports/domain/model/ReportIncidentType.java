package com.gestionate.backend.reports.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "report_incident_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportIncidentType {

    @EmbeddedId
    private ReportIncidentTypeId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("reportId")
    @JoinColumn(name = "report_id", nullable = false)
    private Report report;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("incidentTypeId")
    @JoinColumn(name = "incident_type_id", nullable = false)
    private IncidentType incidentType;

    public ReportIncidentType(Report report, IncidentType incidentType) {
        this.report = report;
        this.incidentType = incidentType;
        this.id = new ReportIncidentTypeId(report.getId(), incidentType.getId());
    }
}
