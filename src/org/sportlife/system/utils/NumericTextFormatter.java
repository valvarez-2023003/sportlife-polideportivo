package org.sportlife.system.utils;

import javafx.scene.control.TextFormatter;
import javafx.util.converter.DoubleStringConverter;

/**
 * Fábrica de TextFormatter para campos numéricos decimales.
 * Evita duplicar el regex de validación en cada controlador.
 *
 * @author Cristofer Ramos
 */
public final class NumericTextFormatter {

    private NumericTextFormatter() {
        // Constructor privado: clase de utilidades
    }

    /**
     * Crea un TextFormatter que solo acepta números decimales positivos,
     * con la cantidad máxima de dígitos enteros y decimales indicada.
     *
     * @param maxEnteros   cantidad máxima de dígitos antes del punto decimal
     * @param maxDecimales cantidad máxima de dígitos después del punto decimal
     * @return un TextFormatter configurado con esas restricciones
     */
    public static TextFormatter<Double> createDecimalFormatter(int maxEnteros, int maxDecimales) {
        String regex = "\\d{0," + maxEnteros + "}(\\.\\d{0," + maxDecimales + "})?";

        return new TextFormatter<>(
            new DoubleStringConverter(),
            0.0,
            change -> {
                String newText = change.getControlNewText();
                if (newText.isEmpty()) return change;
                if (newText.matches(regex)) return change;
                return null;  // rechaza el cambio
            }
        );
    }
}