package Controlador;

import Modelo.FachadaModelo;
import Modelo.Workspace;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class para la ventana de comparación, en la que da opción a hacer la comparación utilizando diferentes descriptores.
 *
 * @author Stanislav
 */
public class ComparacionController implements Initializable {
    private FachadaModelo modelo;
    private Stage stage;
    private Stage stagePadre; //Necesario para transmitirle la paternidad de la ventana de resultados antes de cerrarse
    private Workspace sel;
    private HomeControlador controllerPadre; //Para establecer la carga
    
    @FXML
    private ComboBox seleccion;

    public Workspace getSel() {
        return sel;
    }

    public void setSel(Workspace sel) {
        this.sel = sel;
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

    public Stage getStagePadre() {
        return stagePadre;
    }

    public void setStagePadre(Stage stagePadre) {
        this.stagePadre = stagePadre;
    }

    public HomeControlador getControllerPadre() {
        return controllerPadre;
    }

    public void setControllerPadre(HomeControlador controllerPadre) {
        this.controllerPadre = controllerPadre;
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        seleccion.getItems().addAll(
                "Coocurrencia"
        );
        seleccion.getSelectionModel().selectFirst();
    }    
    
    @FXML 
    public void comparar(ActionEvent event) throws IOException{
        if(seleccion.getSelectionModel().getSelectedItem().equals("Coocurrencia")){
            System.out.println("Realizando comparación con descriptores de coocurrencia...");
            //Realizar comparativa y mostrar ventana con las imágenes resultantes!
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Selección de imagen de referencia");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Todo", "*.*"),
                    new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                    new FileChooser.ExtensionFilter("PNG", "*.png"),
                    new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                    new FileChooser.ExtensionFilter("TIF", "*.tif")
            );
            File referencia = fileChooser.showOpenDialog(stage);
            if(referencia != null){
                
                try{
                    int res = sel.compararCoocurrencia(referencia);

                    //Ahora abrir ventana de resultados y llenarla
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/Resultado.fxml"));
                    ResultadoController controller = new ResultadoController(sel);
                    loader.setController(controller);
                    Parent root = (Parent)loader.load();
                    Stage stageLocal = new Stage();
                    stageLocal.initModality(Modality.APPLICATION_MODAL);
                    stageLocal.initOwner(this.stagePadre);
                    stageLocal.setTitle("Resultado de comparación utilizando descriptores de " +seleccion.getValue().toString());
                    stageLocal.setResizable(false); //Evitar que se pueda cambiar de tam
                    //Setters para todos los atributos
                    controller.setStage(stageLocal);
                    controller.setModelo(modelo);
                    stageLocal.setScene(new Scene(root));
                    stageLocal.show();
                    stage.close(); //Se cierra esta ventana ahora
                }catch(Throwable ex){
                    Alert alert = new Alert(AlertType.ERROR);
                    alert.setTitle("Ocurrió un error");
                    alert.setHeaderText("Se produjo una excepción durante la comparación.");
                    alert.setContentText("Prueba a realizar la comparación con otra imagen de referencia o utiliza otro workspace diferente.");
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
                }
            }
        }
    }
    
    @FXML 
    public void cerrar(ActionEvent event){
        stage.close();
    }
}
