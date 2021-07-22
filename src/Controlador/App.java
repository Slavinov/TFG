package Controlador;

import Modelo.FachadaModelo;
import Modelo.InterfazCVIPTools;
import Vista.PantallaDeCarga;
import java.io.PrintWriter;
import java.io.StringWriter;
import javafx.application.Application;
import javafx.application.Preloader.StateChangeNotification;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

/**
 * Entrada al programa, inicia la interfaz gráfica llamando a la pantalla de carga y a continuación crea un hilo para cargar los datos del modelo.
 * Una vez terminada la carga pasa a la ventana principal, controlada por HomeControlador.java
 * @author Stanislav
 */
public class App extends Application{
    FachadaModelo modelo = null;
    PantallaDeCarga pc = new PantallaDeCarga();
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    
    //Se carga el modelo en otro hilo, para permitir mostrar la pantalla de carga
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
        //Se comprueba que se puede acceder al dll. En caso contrario se produce error.
        try{
            InterfazCVIPTools prueba = InterfazCVIPTools.INSTANCE;
            System.out.println(prueba.toString());
        }catch(Throwable ex){
            System.out.println("Error instanciando la librería CVIPTools.dll");
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Ocurrió un error");
            alert.setHeaderText("Se produjo una excepción durante la carga de CVIPTools.dll.");
            alert.setContentText("La librería no existe o el entorno de ejecución es incompatible con esta versión de la librería.");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exceptionText = sw.toString();
            Label label = new Label("La excepción exacta producida fue:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);
            alert.showAndWait();           
            throw new Exception("Error de CVIPTools");
        }
                  
        primaryStage.setTitle("Cargando...");       
        pc.start(primaryStage);
        //Nuevo hilo para cargar el modelo, cuando termine iniciará el resto de la interfaz:
        Service service = new ProcessService();
        service.start();
        service.setOnSucceeded(e -> { //Se terminó de cargar el modelo!
            System.out.println("El hilo terminó con éxito!");
            pc.setModelo(modelo);
            pc.handleStateChangeNotification(new StateChangeNotification(StateChangeNotification.Type.BEFORE_START)); //Se avisa a la pantalla de carga que terminó de cargar los datos
        });
                                       
    }
    
    public static void main(String[] args) {   
        System.setProperty("jna.boot.library.path", ".\\");
        System.setProperty("jna.library.path", ".\\");
        launch(args);
    }
    
}
