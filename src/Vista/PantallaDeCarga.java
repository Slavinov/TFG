package Vista;

import Controlador.HomeControlador;
import Modelo.FachadaModelo;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Preloader;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Pantalla de carga. Una vez que recibe la notificación de que se cargó el modelo, pasa a la pantalla principal, que continúa en HomeControlador.
 * @author Stanislav
 */
public class PantallaDeCarga extends Preloader {
    ProgressBar bar;
    Stage stage;
    FachadaModelo modelo = null;
    Label l;
    public FachadaModelo getModelo() {
        return modelo;
    }

    public void setModelo(FachadaModelo modelo) {
        this.modelo = modelo;
    }

    public Label getL() {
        return l;
    }

    public void setL(Label l) {
        this.l = l;
    }

    
    
    private Scene createPreloaderScene() {
        bar = new ProgressBar(-1);
        bar.setPrefWidth(300);
        l = new Label("Abriendo workspaces...");
        l.setPadding(new Insets(0,0,20,0));
        l.setFont(new Font("Arial",23));
        BorderPane p = new BorderPane();     
        p.setCenter(bar);
        HBox box = new HBox(l);
        box.setAlignment(Pos.CENTER);
        p.setBottom(box);
        return new Scene(p, 400, 150);        
    }
    
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setScene(createPreloaderScene());        
        stage.show();
    }
    
    @Override
    public void handleProgressNotification(ProgressNotification pn) {
        bar.setProgress(pn.getProgress());
    }
 
    
    //Inicio de la pantalla principal
    @Override
    public void handleStateChangeNotification(StateChangeNotification evt) {
        if (evt.getType() == StateChangeNotification.Type.BEFORE_START) {
            stage.setTitle("Comparación de similaridad");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/HomeVista.fxml"));
            HomeControlador controlador = new HomeControlador(modelo);
            loader.setController(controlador);
            //Parent root = FXMLLoader.load(getClass().getResource("/Vista/HomeVista.fxml"));
            Parent root;
            try {              
                root = (Parent)loader.load();            
                HomeControlador controller = (HomeControlador)loader.getController();
                controller.setStage(stage);
                stage.setScene(new Scene(root));
                stage.show();
                
            } catch (IOException ex) {
                Logger.getLogger(PantallaDeCarga.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }    
}
