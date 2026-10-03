package org.sportlife.system.utils;

/**
 * Utilidades de validación de texto, correo y teléfono.
 * Todos los métodos son estáticos porque no dependen del estado de la instancia.
 *
 * @author Cristofer Ramos
 */
public class Validations {

    private Validations() {
        // Constructor privado: clase de utilidades, no se instancia
    }

    public static boolean equalsText(String a, String b) {
        if (a == null || b == null) return false;
        return a.equals(b);
    }

    public static boolean emptyText(String text) {
        return text == null || text.isEmpty() || text.isBlank();
    }

    public static boolean validateLengthText(String text, int lengthMax) {
        if (text == null) return false;
        return text.length() <= lengthMax;
    }

    public static boolean validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) return false;
        return phone.trim().matches("^[0-9]{8}$");
    }

    public static boolean validateOnlyLetters(String text) {
        if (text == null || text.trim().isEmpty()) return false;
        return text.trim().matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
    }

    public static boolean validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) return false;
        return email.trim().matches("^[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*@[a-zA-Z]+(\\.[a-zA-Z]+)+$");
    }
}