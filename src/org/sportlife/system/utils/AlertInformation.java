package org.sportlife.system.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Utilidad para mostrar alertas al usuario.
 * Todos los métodos son estáticos porque no dependen del estado de la instancia.
 *
 * @author Cristofer Ramos
 */
public class AlertInformation {

    private AlertInformation() {
        // Constructor privado: clase de utilidades, no se instancia
    }

    /**
     * Muestra una alerta al usuario.
     *
     * @param tipoAlerta tipo de alerta: "INFO", "WARNING", "ERROR", "CONFIRMATION", "NONE"
     * @param titulo     título de la ventana
     * @param encabezado texto del encabezado
     * @param mensaje    mensaje de contenido
     */
    public static void viewAlert(String tipoAlerta, String titulo, String encabezado, String mensaje) {
        AlertType tipo = switch (tipoAlerta.toUpperCase()) {
            case "INFO", "INFORMATION"     -> AlertType.INFORMATION;
            case "WARNING", "WARN"         -> AlertType.WARNING;
            case "ERROR", "ERR"            -> AlertType.ERROR;
            case "CONFIRMATION", "CONFIRM" -> AlertType.CONFIRMATION;
            case "NONE"                    -> AlertType.NONE;
            default -> {
                System.err.println("Tipo de alerta desconocido: " + tipoAlerta);
                yield AlertType.INFORMATION;
            }
        };

        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}