package org.sportlife.system;

import javafx.application.Application;
import javafx.stage.Stage;
import org.sportlife.system.utils.SceneManager;
import org.sportlife.system.utils.ViewFactory;

/**
 *
 * @author Cristofer Ramos
 */

public class ClasePrincipal extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stageRoot){
        SceneManager.getInstanciaSceneManager().setStagePrincipal(stageRoot);

        ViewFactory viewFactory = new ViewFactory();
        viewFactory.viewLogin();
    }
}