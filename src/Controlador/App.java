package Controlador;

import Modelo.FachadaModelo;
import Vista.PantallaDeCarga;
import javafx.application.Application;
import javafx.application.Preloader.StateChangeNotification;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 * Entrada al programa, inicia la interfaz gráfica llamando a la pantalla de carga y a continuación crea un hilo para cargar los datos del modelo.
 * @author Stanislav
 */
public class App extends Application{
    FachadaModelo modelo = null;
    PantallaDeCarga pc = new PantallaDeCarga();
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    
    //Se carga el modelo en otro hilo
    class ProcessService extends Service<Void> {
        @Override
        protected Task<Void> createTask() {
            return new Task<Void>() {
                @Override
                protected Void call() throws Exception {
                    modelo = new FachadaModelo();
                    modelo.init();
                    return null;
                }
            };
        }
    }
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Cargando...");       
        pc.start(primaryStage);
        //Nuevo hilo para cargar el modelo, cuando termine iniciará el resto de la interfaz:
        Service service = new ProcessService();
        service.start();
        service.setOnSucceeded(e -> { //Se terminó de cargar el modelo!
            System.out.println("El hilo terminó con éxito!");
            pc.setModelo(modelo);
            pc.handleStateChangeNotification(new StateChangeNotification(StateChangeNotification.Type.BEFORE_START));
        });

    }
    
    public static void main(String[] args) {       
        System.out.println(System.getProperty("user.home")+"\\Workspaces"); //user.dir es el directorio del programa, puede ser interesante también
        for (String format : ImageIO.getWriterFormatNames()) {
            System.out.println("format = " + format);
        }
        
        launch(args);
    }
    
}
