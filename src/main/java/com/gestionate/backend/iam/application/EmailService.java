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
}
