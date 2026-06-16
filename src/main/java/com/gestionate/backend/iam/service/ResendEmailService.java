package com.gestionate.backend.iam.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Profile("prod")
@Service
@RequiredArgsConstructor
public class ResendEmailService implements IEmailService {

    private static final String RESEND_EMAILS_URL = "https://api.resend.com/emails";

    private final ObjectMapper objectMapper;

    @Value("${resend.api-key}")
    private String resendApiKey;

    @Value("${resend.from}")
    private String resendFrom;

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            ObjectNode payload = objectMapper.createObjectNode();
            payload.put("from", resendFrom);
            payload.putArray("to").add(to);
            payload.put("subject", subject);
            payload.put("text", body);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(RESEND_EMAILS_URL))
                    .header("Authorization", "Bearer " + resendApiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(payload)))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() < 200 || response.statusCode() >= 300) {
                throw new IllegalStateException(
                        "No se pudo enviar el correo con Resend. Status: "
                                + response.statusCode()
                                + ". Response: "
                                + response.body());
            }

        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("El envío de correo fue interrumpido.", exception);
        } catch (Exception exception) {
            throw new IllegalStateException("No se pudo enviar el correo con Resend.", exception);
        }
    }

    @Override
    public void sendPasswordResetCode(String to, String code) {
        String subject = "Código de recuperación de contraseña - Gestionate";

        String body = """
                Hola,

                Recibimos una solicitud para recuperar tu contraseña.

                Tu código de recuperación es:

                %s

                Este código vencerá en 10 minutos.

                Si no solicitaste este cambio, puedes ignorar este mensaje.

                Equipo Gestionate
                """.formatted(code);

        sendEmail(to, subject, body);
    }

    @Override
    public void sendReportStatusNotification(String to, String title, String messageContent) {
        String body = """
                Hola,

                %s

                Puedes revisar el estado de tu reporte desde la plataforma Gestionate.

                Equipo Gestionate
                """.formatted(messageContent);

        sendEmail(to, title, body);
    }

    @Override
    public void sendContactAutoReply(String to, String name, String registerUrl) {
        String subject = "Gracias por contactarte con Gestionate";

        String body = """
                Hola %s,

                Gracias por contactarte con Gestionate.

                Gestionate es una plataforma que permite reportar incidencias urbanas relacionadas con residuos, desmonte, objetos voluminosos, residuos en áreas públicas y otros problemas que afectan a la comunidad.

                Desde la plataforma podrás registrar reportes, adjuntar evidencias, indicar la ubicación de la incidencia y hacer seguimiento al estado de atención por parte de la municipalidad y el área operativa.

                Te invitamos a registrarte para crear tus reportes y recibir actualizaciones sobre su atención.

                Regístrate aquí:
                %s

                Equipo Gestionate
                """
                .formatted(name, registerUrl);

        sendEmail(to, subject, body);
    }
}
