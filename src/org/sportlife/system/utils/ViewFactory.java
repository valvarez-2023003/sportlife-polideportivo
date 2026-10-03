package org.sportlife.system.utils;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.sportlife.system.ClasePrincipal;

/**
 * Factoría encargada de cargar vistas FXML y cambiar la escena del Stage principal.
 *
 * @author Cristofer Ramos
 */
public class ViewFactory {

    private static final String PATH_VIEW = "/org/sportlife/system/view/";

    /**
     * Carga un archivo FXML y devuelve su Scene con el tamaño indicado.
     */
    public Scene loadFileFXML(String nombreFXML, int ancho, int alto) {
        String pathOfFile = PATH_VIEW + nombreFXML;
        try {
            URL urlFile = ClasePrincipal.class.getResource(pathOfFile);
            if (urlFile == null) {
                throw new IOException("No se encontró el archivo FXML en la ruta: " + pathOfFile);
            }
            FXMLLoader loaderFXML = new FXMLLoader(urlFile);
            return new Scene(loaderFXML.load(), ancho, alto);
        } catch (IOException e) {
            throw new UncheckedIOException("Error al cargar el archivo FXML: " + pathOfFile, e);
        }
    }

    /**
     * Cambia la escena del Stage principal a la vista indicada.
     */
    public void loadView(TipoVista tipoVista) {
        if (tipoVista == null) {
            System.err.println("loadView: el tipo de vista es nulo.");
            return;
        }

        try {
            Scene scene = loadFileFXML(tipoVista.getArchivo(), tipoVista.getAncho(), tipoVista.getAlto());

            Stage stage = SceneManager.getInstanciaSceneManager().getStagePrincipal();
            stage.setTitle(tipoVista.getTitulo());
            stage.setResizable(tipoVista.isRedimensionable());

            SceneManager.getInstanciaSceneManager().changeScene(scene);

            System.out.println(">>> Vista cargada: " + tipoVista.name() + " (" + tipoVista.getArchivo() + ")");

        } catch (Exception e) {
            System.err.println("=== ERROR CARGANDO LA VISTA: " + tipoVista.name() + " ===");
            e.printStackTrace();
        }
    }

    /**
     * Carga una vista por su nombre textual (case-insensitive).
     * Método de conveniencia para llamadas desde controladores.
     */
    public void loadView(String nombreVista) {
        loadView(TipoVista.fromName(nombreVista));
    }

    // ==========================================================
    //  MÉTODOS DE CONVENIENCIA (atajos)
    // ==========================================================

    public void viewLogin()       { loadView(TipoVista.LOGIN); }
    public void viewRegister()    { loadView(TipoVista.REGISTRO); }
    public void viewAdmin()       { loadView(TipoVista.ADMINISTRADOR); }
    public void viewGerente()     { loadView(TipoVista.GERENTE); }
    public void viewRecepcionista(){ loadView(TipoVista.RECEPCIONISTA); }
    public void viewFormulario()  { loadView(TipoVista.FORMULARIO); }
}