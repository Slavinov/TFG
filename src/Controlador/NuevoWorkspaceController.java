package Controlador;

import Modelo.FachadaModelo;
import Modelo.Workspace;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class para la ventana de darle nombre a un nuevo workspace.
 *
 * @author Stanislav
 */
public class NuevoWorkspaceController implements Initializable {
    private FachadaModelo modelo;
    private Stage stage;
    private HomeControlador controladorPadre;
    
    @FXML
    private TextField nombre;
    
    private TreeView wsTree; 

    public HomeControlador getControladorPadre() {
        return controladorPadre;
    }

    public void setControladorPadre(HomeControlador controladorPadre) {
        this.controladorPadre = controladorPadre;
    }
    
    
    public FachadaModelo getModelo() {
        return modelo;
    }    

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public TreeView getWsTree() {
        return wsTree;
    }

    public void setWsTree(TreeView wsTree) {
        this.wsTree = wsTree;
    }

    
    
    public void setModelo(FachadaModelo modelo) {
        this.modelo = modelo;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // TODO
    }    
    
    //Función de creación de nuevo workspace
    @FXML
    public void crearWorkspace(ActionEvent event){
        //Comprobar que el nombre no esté repetido y que no esté en blanco el campo y que contenga como mucho _   
        Pattern pattern = Pattern.compile("[a-z0-9_]+", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(nombre.getText());
        boolean valido = matcher.find();
        
        if(valido && !(nombre.getText().equals("Workspaces"))){
            if(!modelo.existeWorkspace(nombre.getText())){
                System.out.println("Funciono!");
                //Creación de nuevo Workspace
                    //workspaces.add(new Workspace(nombre.getText()));
                Workspace nuevoWorkspace = modelo.crearWorkspace(nombre.getText());
                if(nuevoWorkspace != null){
                    wsTree.getRoot().getChildren().add(new TreeItem(nuevoWorkspace.getNombre()));
                    //Selector de archivos
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Selección de imágenes");
                    fileChooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("Todo", "*.*"),
                            new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                            new FileChooser.ExtensionFilter("PNG", "*.png"),
                            new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                            new FileChooser.ExtensionFilter("TIF", "*.tif")

                    );
                    List<File> list = fileChooser.showOpenMultipleDialog(stage);

                    if (list != null){
                        for (File file : list){
                            openFile(file, nuevoWorkspace);
                        }
                        if(nuevoWorkspace.getImagenesErroneas() != null){
                        String mensajeError ="";
                        for(int i=0; i<nuevoWorkspace.getImagenesErroneas().size(); i++){
                            mensajeError=mensajeError+nuevoWorkspace.getImagenesErroneas().get(i)+"\n";
                        }
                        Alert alert = new Alert(AlertType.INFORMATION);
                        alert.setTitle("Error al cargar imágenes");
                        alert.setHeaderText(null);
                        alert.setContentText("Una o más imágenes no se pudieron cargar. Comprueba que los formatos estén soportados.");
                        TextArea textArea = new TextArea(mensajeError);
                        textArea.setEditable(false);
                        textArea.setWrapText(true);
                        Label label = new Label("Las imagenes que fallaron en cargar son:");
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
                        nuevoWorkspace.setImagenesErroneas(null);
                    }
                    }
                    stage.close();
                }              
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Workspace existente");
                alert.setHeaderText(null);
                alert.setContentText("El nombre del Workspace introducido ya está en uso");
                alert.showAndWait();
            }
        }else{
            
            if(nombre.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Nombre vacío");
                alert.setHeaderText(null);
                alert.setContentText("Introduce un nombre para el workspace");
                alert.showAndWait();
            }else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Nombre inválido");
                alert.setHeaderText(null);
                alert.setContentText("El nombre del Workspace debe contener únicamente letras y/o números");
                alert.showAndWait();
            }
        }
    }
    
    private void openFile(File file, Workspace ws) {
        try {
            //desktop.open(file); //Mostrar la imagen
            System.out.println(file.getName());
            File d = new File(ws.getPath()+"\\"+file.getName());
            Path destino = d.toPath();
            //file.renameTo(new File(workspaces.get(workspaces.size()-1).getPath()+"\\"+workspaces.get(workspaces.size()-1).getNombre()+"\\"+file.getName())); //Se mueve imagen a la carpeta destino del workspace
            Files.copy(file.toPath(), destino , StandardCopyOption.REPLACE_EXISTING); //Se copia al destino
            //Se procesa en el ws
            ws.anhadirImagen(d);
            
        } catch (IOException ex) {
            Logger.getLogger(
                HomeControlador.class.getName()).log(
                    Level.SEVERE, null, ex
                );
        }
    }
    
    @FXML
    public void cerrarVentana(ActionEvent event){
        stage.close();
    }
    
}
