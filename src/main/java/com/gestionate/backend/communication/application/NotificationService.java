package com.gestionate.backend.communication.application;

import com.gestionate.backend.communication.domain.model.Notification;
import com.gestionate.backend.communication.domain.repository.NotificationRepository;
import com.gestionate.backend.iam.application.IEmailService;
import com.gestionate.backend.iam.domain.model.User;
import com.gestionate.backend.reports.domain.model.ReportStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService implements INotificationService {

    private final NotificationRepository notificationRepository;
    private final IEmailService emailService;

    @Override
    @Transactional
    public void notifyReportStatusChanged(
            User citizenUser,
            String reportCode,
            ReportStatus reportStatus) {
        String title = buildTitle(reportStatus);
        String message = buildMessage(reportCode, reportStatus);

        Notification notification = Notification.builder()
                .user(citizenUser)
                .title(title)
                .message(message)
                .sentAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);

        try {
            emailService.sendReportStatusNotification(
                    citizenUser.getEmail(),
                    title,
                    message);
        } catch (Exception exception) {
            log.warn(
                    "No se pudo enviar la notificación por correo para el reporte {} al usuario {}. Motivo: {}",
                    reportCode,
                    citizenUser.getEmail(),
                    exception.getMessage());
        }
    }

    private String buildTitle(ReportStatus reportStatus) {
        return switch (reportStatus) {
            case RECEIVED -> "Reporte registrado correctamente";
            case DERIVED -> "Reporte derivado a limpieza pública";
            case ORDER_COMPLETED -> "Reporte atendido correctamente";
        };
    }

    private String buildMessage(String reportCode, ReportStatus reportStatus) {
        return switch (reportStatus) {
            case RECEIVED -> "Tu reporte " + reportCode + " fue recibido por la municipalidad.";
            case DERIVED -> "Tu reporte " + reportCode + " fue derivado al Área operativa de limpieza pública.";
            case ORDER_COMPLETED -> "Tu reporte " + reportCode + " fue atendido correctamente.";
        };
    }
}
