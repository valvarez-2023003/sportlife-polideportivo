package org.sportlife.system.service;

import java.sql.SQLException;
import java.util.List;
import org.sportlife.system.model.Reserva;
import org.sportlife.system.repository.ReservaRepository;

/**
 * Servicio de reservas.
 *
 * @author Cristofer Ramos
 */
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService() {
        this.reservaRepository = new ReservaRepository();
    }

    public List<Reserva> listarPendientes() throws SQLException {
        return reservaRepository.listarPendientes();
    }

    public List<Reserva> listarTodas() throws SQLException {
        return reservaRepository.listarTodas();
    }

    public void confirmar(int idReserva) throws SQLException {
        reservaRepository.confirmar(idReserva);
    }

    public void rechazar(int idReserva) throws SQLException {
        reservaRepository.rechazar(idReserva);
    }

    public void cancelar(int idReserva) throws SQLException {
        reservaRepository.cancelar(idReserva);
    }

    public void actualizar(int idReserva, java.time.LocalDate fecha,
            java.time.LocalTime hIni, java.time.LocalTime hFin,
            String estado) throws SQLException {
        reservaRepository.actualizar(idReserva, fecha, hIni, hFin, estado);
    }

    public void eliminar(int idReserva) throws SQLException {
        reservaRepository.eliminar(idReserva);
    }
    public List<Reserva> listarPorUsuario(String idUser) throws SQLException {
    return reservaRepository.listarPorUsuario(idUser);
}

public void crear(Reserva reserva, String idUser) throws SQLException {
    reservaRepository.crear(reserva, idUser);
}
}
