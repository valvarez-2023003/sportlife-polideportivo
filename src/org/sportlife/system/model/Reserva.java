package org.sportlife.system.model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Modelo que representa una reserva.
 *
 * @author Cristofer Ramos
 */
public class Reserva {

    private int idReserva;
    private int idCancha;
    private String codigoCancha;
    private String nombreCancha;
    private String tipoDeporte;
    private boolean techada;
    private String cliente;
    private String emailCliente;
    private LocalDate fechaReserva;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private int totalHoras;
    private double costoTotal;
    private String estadoReserva;

    public Reserva() {
    }

    // Getters y setters

    public int getIdReserva() { return idReserva; }
    public void setIdReserva(int idReserva) { this.idReserva = idReserva; }

    public int getIdCancha() { return idCancha; }
    public void setIdCancha(int idCancha) { this.idCancha = idCancha; }

    public String getCodigoCancha() { return codigoCancha; }
    public void setCodigoCancha(String codigoCancha) { this.codigoCancha = codigoCancha; }

    public String getNombreCancha() { return nombreCancha; }
    public void setNombreCancha(String nombreCancha) { this.nombreCancha = nombreCancha; }

    public String getTipoDeporte() { return tipoDeporte; }
    public void setTipoDeporte(String tipoDeporte) { this.tipoDeporte = tipoDeporte; }

    public boolean isTechada() { return techada; }
    public void setTechada(boolean techada) { this.techada = techada; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getEmailCliente() { return emailCliente; }
    public void setEmailCliente(String emailCliente) { this.emailCliente = emailCliente; }

    public LocalDate getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDate fechaReserva) { this.fechaReserva = fechaReserva; }

    public LocalTime getHoraInicio() { return horaInicio; }
    public void setHoraInicio(LocalTime horaInicio) { this.horaInicio = horaInicio; }

    public LocalTime getHoraFin() { return horaFin; }
    public void setHoraFin(LocalTime horaFin) { this.horaFin = horaFin; }

    public int getTotalHoras() { return totalHoras; }
    public void setTotalHoras(int totalHoras) { this.totalHoras = totalHoras; }

    public double getCostoTotal() { return costoTotal; }
    public void setCostoTotal(double costoTotal) { this.costoTotal = costoTotal; }

    public String getEstadoReserva() { return estadoReserva; }
    public void setEstadoReserva(String estadoReserva) { this.estadoReserva = estadoReserva; }

    public String getTechadaTexto() {
        return techada ? "Techada" : "Aire libre";
    }

    public String getHorarioTexto() {
        return horaInicio + " - " + horaFin;
    }
}