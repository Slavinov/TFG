package Controlador;

import DataAccess.FachadaDAO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Entrada al programa, inicia la interfaz gráfica pasando el control a HomeControlador
 * @author Stanislav
 */
public class App extends Application{

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/HomeVista.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("/Vista/HomeVista.fxml"));
        Parent root = (Parent)loader.load();
        HomeControlador controller = (HomeControlador)loader.getController();
        controller.setStage(primaryStage);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        /////////BANCO DE PRUEBAS////////////
            FachadaDAO f = FachadaDAO.getFachada();
            f.initTest();
            f.insertTest(2322, "heheee");
            f.getNumTest();
        /////////FIN BANCO DE PRUEBAS//////////
        
        
        System.out.println(System.getProperty("user.home")+"\\Workspaces"); //user.dir es el directorio del programa, puede ser interesante también
        launch(args);
        ///////////////////FASE 1////////////////
        //HomeControlador controlador = new HomeControlador();
    }
    
}
