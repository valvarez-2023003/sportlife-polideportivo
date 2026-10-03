package org.sportlife.system.utils;

/**
 * Enumeración de los tipos de vista (FXML) del sistema. Cada valor encapsula:
 * archivo, tamaño, título y si es redimensionable.
 *
 * @author Cristofer Ramos
 */
public enum TipoVista {

    LOGIN("LoginView.fxml", 779, 528, "LOGIN - SPORTLIFE", false),
    REGISTRO("RegisterView.fxml", 950, 600, "REGISTRO - SPORTLIFE", false),
    GERENTE("GerenteView.fxml", 900, 600, "SPORTLIFE - PANEL GERENTE", true),
    ADMINISTRADOR("BarraLateralAdmin.fxml", 1000, 600, "SPORTLIFE - PANEL ADMINISTRADOR", true),
    RECEPCIONISTA("BarraLateralRecepcionista.fxml", 1000, 600, "SPORTLIFE - PANEL RECEPCIONISTA", true),
    FORMULARIO("FormularioView.fxml", 1000, 500, "SPORTLIFE - FORMULARIO CLIENTE", false);

    private final String archivo;
    private final int ancho;
    private final int alto;
    private final String titulo;
    private final boolean redimensionable;

    TipoVista(String archivo, int ancho, int alto, String titulo, boolean redimensionable) {
        this.archivo = archivo;
        this.ancho = ancho;
        this.alto = alto;
        this.titulo = titulo;
        this.redimensionable = redimensionable;
    }

    public String getArchivo() {
        return archivo;
    }

    public int getAncho() {
        return ancho;
    }

    public int getAlto() {
        return alto;
    }

    public String getTitulo() {
        return titulo;
    }

    public boolean isRedimensionable() {
        return redimensionable;
    }

    /**
     * Busca un tipo de vista por nombre textual (case-insensitive). Acepta
     * alias como "admin", "login", "register", etc.
     */
    public static TipoVista fromName(String nombre) {
        if (nombre == null) {
            return LOGIN;
        }

        String n = nombre.trim().toLowerCase();
        return switch (n) {
            case "login", "loginview" ->
                LOGIN;
            case "register", "registerview" ->
                REGISTRO;
            case "gerente" ->
                GERENTE;
            case "administrador", "admin" ->
                ADMINISTRADOR;
            case "recepcionista" ->
                RECEPCIONISTA;
            case "user", "formulario" ->
                FORMULARIO;
            default -> {
                System.err.println("Tipo de vista desconocido: [" + nombre + "]. Usando LOGIN por defecto.");
                yield LOGIN;
            }
        };
    }
}
