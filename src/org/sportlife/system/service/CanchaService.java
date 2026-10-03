package org.sportlife.system.service;

import java.sql.SQLException;
import java.util.List;
import org.sportlife.system.model.Cancha;
import org.sportlife.system.repository.CanchaRepository;

/**
 * Servicio con la lógica de negocio de las canchas.
 *
 * @author Cristofer Ramos
 */
public class CanchaService {

    private final CanchaRepository canchaRepository;

    public CanchaService() {
        this.canchaRepository = new CanchaRepository();
    }

    public void crear(Cancha cancha, String idAdmin) throws SQLException {
        canchaRepository.insertar(cancha, idAdmin);
    }

    public void actualizar(Cancha cancha, String idAdmin) throws SQLException {
        canchaRepository.actualizar(cancha, idAdmin);
    }

    public void eliminar(int idCancha, String idAdmin) throws SQLException {
        canchaRepository.eliminar(idCancha, idAdmin);
    }

    public List<Cancha> listarTodas() throws SQLException {
        return canchaRepository.listarTodas();
    }

    public List<Cancha> listarDisponibles() throws SQLException {
        return canchaRepository.listarDisponibles();
    }
}