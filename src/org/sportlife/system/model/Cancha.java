package org.sportlife.system.model;

/**
 * Modelo que representa una cancha deportiva.
 *
 * @author Cristofer Ramos
 */
public class Cancha {

    private int idCancha;           // id_cancha (auto-incremental en BD)
    private String codigo;          // código que el admin ingresa (ej: "F1", "T2")
    private String nombre;          // descripción de la cancha
    private String tipoDeporte;     // Fútbol, Basquet, Tenis, Vóley
    private boolean techada;        // true = techada, false = aire libre
    private double precioPorHora;   // precio en quetzales por hora
    private String estado;          // Disponible / En mantenimiento

    public Cancha() {
    }

    public Cancha(int idCancha, String codigo, String nombre, String tipoDeporte,
                  boolean techada, double precioPorHora, String estado) {
        this.idCancha = idCancha;
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipoDeporte = tipoDeporte;
        this.techada = techada;
        this.precioPorHora = precioPorHora;
        this.estado = estado;
    }

    // Getters y setters
    public int getIdCancha() { return idCancha; }
    public void setIdCancha(int idCancha) { this.idCancha = idCancha; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTipoDeporte() { return tipoDeporte; }
    public void setTipoDeporte(String tipoDeporte) { this.tipoDeporte = tipoDeporte; }

    public boolean isTechada() { return techada; }
    public void setTechada(boolean techada) { this.techada = techada; }

    public double getPrecioPorHora() { return precioPorHora; }
    public void setPrecioPorHora(double precioPorHora) { this.precioPorHora = precioPorHora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    /**
     * Devuelve el texto del techo para mostrar en la UI.
     */
    public String getTechadaTexto() {
        return techada ? "Techada" : "Aire libre";
    }
}