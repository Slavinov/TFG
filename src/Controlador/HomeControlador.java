package Controlador;

import Modelo.FachadaModelo;
import Modelo.Workspace;
import ValueObjects.ConfigVO;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.annotation.Resources;

/**
 * Controlador de la vista principal JavaFX
 * @author Stanislav
 */
public class HomeControlador implements Initializable{
    private ArrayList<Workspace> workspaces = new ArrayList<Workspace>(); 
    private Stage stage;
    private Desktop desktop = Desktop.getDesktop(); //isDesktopSupported() para comprobar en otros sistemas. Sirve para visualizar imagenes y tal. Mejor buscar visualizador de java o implementarlo en JavaFX.
    private FachadaModelo modelo; //Singleton?
    private Workspace seleccion; //Workspace seleccionado actualmente
    private String imagenSeleccionada; //Nombre de la imagen seleccionada en el workspace
    
    //Atributos de elementos de JavaFX
    @FXML
    private TextField nombre;
    
    @FXML
    private TreeView wsTree;
    //Getters/Setters
    public void setStage(Stage s){
        stage = s;
    }
    
    @FXML 
    private Button addImage;
    @FXML
    private Button extraerCor;
    @FXML 
    private Button compararBtn;
    
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private AnchorPane anchorPane;
    
    @FXML
    private MenuBar menuSuperior;
    
    @FXML
    private Pane barraHerramientas;
    /////////////MÉTODOS////////////////
    //Listeners
    
    
    //Init
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //AnchorPane.setTopAnchor(menuSuperior, 0.0);
        //AnchorPane.setLeftAnchor(menuSuperior, 0.0);
        //AnchorPane.setRightAnchor(menuSuperior, 0.0);
        //AnchorPane.setBottomAnchor(scrollPane, 50.0);
        //AnchorPane.setRightAnchor(scrollPane, 50.0);
        //AnchorPane.setTopAnchor(scrollPane, 250.0);
        //AnchorPane.setLeftAnchor(scrollPane, 220.0);
        //AnchorPane.setLeftAnchor(barraHerramientas, 0.0);
        //AnchorPane.setRightAnchor(barraHerramientas, 0.0);
        //Cargar datos
        modelo = new FachadaModelo();
        modelo.init();
        //menuSuperior.setPrefWidth(Double.MAX_VALUE);
        //menuSuperior.prefWidthProperty().bind(anchorPane.prefWidthProperty());
        //Establecer el path inicial?
        
        
        //Actualizar la vista con los workspaces cargados
        TreeItem rootItem = new TreeItem("Workspaces");
        for(int i = 0; i < modelo.getWorkspaces().size(); i++){
            rootItem.getChildren().add(new TreeItem(modelo.getWorkspaces().get(i).getNombre()));
        }
        wsTree.setRoot(rootItem);
        System.out.println("Initialize en el controlador");
        
        
        //Añadir listeners:
        wsTree.getSelectionModel().selectedItemProperty().addListener( 
        new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue,
                    Object newValue) {

                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                System.out.println("Selected Text : " + selectedItem.getValue());
                // do what ever you want
                if(!selectedItem.getValue().equals("Workspaces")){
                    System.out.println("Cambiando de seleccion");
                    //Se cambia el workspace seleccionado, cargando las imágenes y todo eso
                    seleccion = modelo.obtenerWorkspace(selectedItem.getValue());
                    if(seleccion != null){
                        addImage.setDisable(false);
                        compararBtn.setDisable(false);
                    
                    //LÓGICA DE VISUALIZACIÓN DE IMAGENES -> Se debería añadir también un context menu o algo a cada imagen
                    GridPane gp = new GridPane();
                    //Crear aquí otra caja personalizada que contenga el image view y el texto nombre de la imagen
                    for(int i = 0; i < seleccion.getImagenes().size(); i++){//Aquí null pointer exception al sacar las miniaturas                      
                        Pane entrada = new Pane();
                        entrada.getChildren().add(new ImageView(seleccion.getImagenes().get(i).getMiniatura()));
                        entrada.getChildren().add(new Label(seleccion.getImagenes().get(i).getNombre()));                      
                        gp.add(entrada, i, i);
                    }
                    scrollPane.setContent(gp);
                    //Añadir un listener y context menu para seleccionar imagenes en el scrollpane
                    
                    
                    gp.getChildren().forEach(item -> {
                        item.setOnMouseClicked(new EventHandler<MouseEvent>(){
                            @Override
                            public void handle(MouseEvent event) {
                                Object source = event.getSource();
                                if(source instanceof Pane){
                                    Pane temp = ((Pane)source);
                                    Label temp2 = (Label) temp.getChildren().get(1);
                                    System.out.println(temp2.getText());
                                    imagenSeleccionada = temp2.getText();
                                }
                                System.out.println("Clickao en mi");
                                if(imagenSeleccionada != null){
                                    extraerCor.setDisable(false);
                                }else{
                                    extraerCor.setDisable(true);
                                }
                            }
                            
                        });
                    });
                    
                    
                    
                    
                    }
                    
                }else{
                    if(seleccion == null){
                        addImage.setDisable(true);
                        compararBtn.setDisable(true);
                    }
                }
            }

      }
        );
    }
   
    @FXML
    public void establecerPathPorDefecto(ActionEvent event){
        //FileChooser fileChooser = new FileChooser();
        //fileChooser.setTitle("Selección de carpeta destino");
        //fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        //File file = fileChooser.showOpenDialog(stage);
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(stage);
        if(file != null){
            //Comprobar permisos antes de nada
            if(file.canWrite() && file.canRead()){
                modelo.setPath(file.getAbsolutePath());
                modelo.guardarConfig();
                System.out.println(file.getAbsolutePath());
            }else{
                //Mostrar aviso de permisos incorrectos, no modificar el path
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Permisos inválidos");
                alert.setHeaderText(null);
                alert.setContentText("La carpeta seleccionada no se puede utilizar a falta de permisos de lectura y escritura");
                alert.showAndWait();
            }
        }
    }
    
    @FXML
    public void resetearPath(ActionEvent event){
        modelo.setPath(null);
        modelo.guardarConfig();
    }

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
                }
            }else{
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Workspace existente");
                alert.setHeaderText(null);
                alert.setContentText("El nombre del Workspace introducido ya está en uso");
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Nombre inválido");
            alert.setHeaderText(null);
            alert.setContentText("El nombre del Workspace debe contener únicamente letras y/o números");
            alert.showAndWait();
        }
    }
    
    //Crear una nueva vista en plan popup (en marcadores)
    @FXML
    public void testAction(ActionEvent event){
        System.out.println("Funciono2!");
        
    } 
    
    @FXML
    public void abrirWorkspace(ActionEvent event){ //Meter alguna forma para reconocer que se trata de un workspace creado? Archivo de texto invisible o algo
        System.out.println("Abrir workspace");
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File file = directoryChooser.showDialog(stage);
        if(file != null){
            //Comprobar permisos antes de nada
            if(file.canWrite() && file.canRead()){
                Workspace abierto = modelo.abrirWorkspace(file.getName(),file.getAbsolutePath());
                if(abierto == null){
                    //Ya está abierto
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Apertura incorrecta");
                    alert.setHeaderText(null);
                    alert.setContentText("El Workspace seleccionado ya está abierto o el nombre es inválido");
                    alert.showAndWait();
                }else{
                    //Se guarda la carpeta y se procesan las imagenes presentes
                    abierto.setCarpeta(file);
                    abierto.detectarImagenes();
                    wsTree.getRoot().getChildren().add(new TreeItem(abierto.getNombre()));
                }
                System.out.println(file.getAbsolutePath());
            }else{
                //Mostrar aviso de permisos incorrectos, no modificar el path
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Permisos inválidos");
                alert.setHeaderText(null);
                alert.setContentText("La carpeta seleccionada no se puede utilizar a falta de permisos de lectura y escritura");
                alert.showAndWait();
            }
        }
        
    }
    
    @FXML
    public void cerrarWorkspace(ActionEvent event){ //Simplemente borrarlo de la base de datos y del tree
        System.out.println("Cerrar workspace");
        if(wsTree.getSelectionModel().getSelectedItem() != null){
            TreeItem entrada = (TreeItem) wsTree.getSelectionModel().getSelectedItem();
            System.out.println("Nombre del nodo: " + entrada.getValue().toString());
            modelo.cerrarWorkspace(entrada.getValue().toString());
            entrada.getParent().getChildren().remove(entrada);         
        }
    }
    
    @FXML 
    public void borrarWorkspace(ActionEvent event){ //Borrarlo de la base de datos y también el archivo de texto invisible que viene en la carpeta
        System.out.println("Borrar workspace");
    }
    
    //Añadir imágenes a un workspace seleccionado
    @FXML public void anhadirImagen(){
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
                        openFile(file, seleccion);
                    }
                }
    }
    
    
    //Función de soporte para cargar imágenes: copia la imagen en la carpeta del workspace y llama al método de procesado de imágenes del workspace
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
    public void extraerDescriptor(ActionEvent event){ 
        System.out.println("Extraer descriptor de la imagen: " + imagenSeleccionada);
        seleccion.extraerDescriptorCoocurrencia(imagenSeleccionada);
    }
    
    @FXML 
    public void compararCorrelacion(ActionEvent event){
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
                    seleccion.compararCoocurrencia(referencia);
                }
    }
    
    //Menús y aperturas de nuevas ventanas
}
