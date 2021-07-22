package Controlador;

import Modelo.FachadaModelo;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class de la ventana de ajustes. Permite establecer el path de guardado por defecto en el modelo y la distancia de comparación.
 *
 * @author Stanislav
 */
public class ConfiguracionController implements Initializable {
    private FachadaModelo modelo;
    private Stage stage;
    
    @FXML
    private TextField path; //Path por defecto actual
    
    @FXML
    private ComboBox distancia; // 1-6
    
    //Constructor personalizado:
    public ConfiguracionController(FachadaModelo m){
        this.modelo = m;
    }
    
    public FachadaModelo getModelo() {
        return modelo;
    }

    public void setModelo(FachadaModelo modelo) {
        this.modelo = modelo;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //Path establecido
        if(modelo.getPath() != null){
            path.setText(modelo.getPath());
        }else{
            path.setText(System.getProperty("user.home")+"\\Workspaces");
        }
        if(path.getPrefWidth() < (path.getText().length()*7)){
            path.setPrefWidth(path.getText().length()*7);
        }
        
        //Distancia coocurrencia
        distancia.getItems().addAll(
                "1",
                "2",
                "3",
                "4",
                "5",
                "6"
        );
        
        distancia.getSelectionModel().select(modelo.getDistancia()-1);
        //Listener para modificar la distancia 
        distancia.valueProperty().addListener(new ChangeListener<String>() {
            @Override 
            public void changed(ObservableValue ov, String t, String t1) {                
                System.out.println(t1);
                modelo.modificarDistancia(Integer.parseInt(t1));
            }    
        });
    }    
    
    @FXML 
    public void modifyPath(ActionEvent event){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(stage);
        if(file != null){
            //Comprobar permisos antes de nada
            if(file.canWrite() && file.canRead()){
                modelo.setPath(file.getAbsolutePath());
                modelo.guardarConfig();
                System.out.println(file.getAbsolutePath());
                path.setText(file.getAbsolutePath());
                if(path.getPrefWidth() < (path.getText().length()*7)){
                    path.setPrefWidth(path.getText().length()*7);
                }
            }else{
                //Mostrar aviso de permisos incorrectos, no modificar el path
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Permisos inválidos");
                alert.setHeaderText(null);
                alert.setContentText("La carpeta seleccionada no se puede utilizar a falta de permisos de lectura y escritura");
                alert.showAndWait();
            }
        }
    }
    
    @FXML 
    public void resetPath(ActionEvent event){
        modelo.setPath(null);
        modelo.guardarConfig();
        path.setText(System.getProperty("user.home")+"\\Workspaces");
        if(path.getPrefWidth() < (path.getText().length()*7)){
            path.setPrefWidth(path.getText().length()*7);
        }
    }
    
    @FXML 
    public void cerrar(ActionEvent event){
        stage.close();
    }
}
