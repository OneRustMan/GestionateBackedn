package com.gestionate.backend.iam.application;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService implements IEmailService {

    private final JavaMailSender javaMailSender;

    @Value("${mail.from}")
    private String mailFrom;

    @Override
    public void sendPasswordResetCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mailFrom);
        message.setTo(to);
        message.setSubject("Código de recuperación de contraseña - Gestionate");
        message.setText("""
                Hola,

                Recibimos una solicitud para recuperar tu contraseña.

                Tu código de recuperación es:

                %s

                Este código vencerá en 10 minutos.

                Si no solicitaste este cambio, puedes ignorar este mensaje.

                Equipo Gestionate
                """.formatted(code));

        javaMailSender.send(message);
    }

    @Override
    public void sendReportStatusNotification(String to, String title, String messageContent) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mailFrom);
        message.setTo(to);
        message.setSubject(title);
        message.setText("""
                Hola,

                %s

                Puedes revisar el estado de tu reporte desde la plataforma Gestionate.

                Equipo Gestionate
                """.formatted(messageContent));

        javaMailSender.send(message);
    }

    @Override
    public void sendContactAutoReply(String to, String name, String registerUrl) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mailFrom);
        message.setTo(to);
        message.setSubject("Gracias por contactarte con Gestionate");
        message.setText(
                """
                        Hola %s,

                        Gracias por contactarte con Gestionate.

                        Gestionate es una plataforma que permite reportar incidencias urbanas relacionadas con residuos, desmonte, objetos voluminosos, residuos en áreas públicas y otros problemas que afectan a la comunidad.

                        Desde la plataforma podrás registrar reportes, adjuntar evidencias, indicar la ubicación de la incidencia y hacer seguimiento al estado de atención por parte de la municipalidad y el área operativa.

                        Te invitamos a registrarte para crear tus reportes y recibir actualizaciones sobre su atención.

                        Regístrate aquí:
                        %s

                        Equipo Gestionate
                        """
                        .formatted(name, registerUrl));

        javaMailSender.send(message);
    }
}
