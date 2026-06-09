package com.gestionate.backend.shared.application.util;

import java.util.Locale;

public final class TextNormalizer {

    private TextNormalizer() {
    }

    public static boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    public static String normalizeText(String value) {
        if (value == null) {
            return null;
        }

        return value.trim().replaceAll("\\s+", " ");
    }

    public static String normalizeUpperText(String value) {
        if (value == null) {
            return null;
        }

        return normalizeText(value).toUpperCase(Locale.ROOT);
    }

    public static String normalizeEmail(String email) {
        if (email == null) {
            return null;
        }

        return email.trim().toLowerCase(Locale.ROOT);
    }
}
