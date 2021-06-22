package Controlador;

import Modelo.FachadaModelo;
import Modelo.Imagen;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
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
    private Label itemSeleccionado = null;
    private int maxCol = 2;
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
    private MenuItem cerrarItem;
    @FXML
    private MenuItem borrarItem;
    @FXML
    private MenuItem borrarItem2;
    @FXML
    private MenuItem descriptorItem;
    @FXML
    private Menu wsMenu;
    @FXML
    private Pane barraHerramientas;
    /////////////MÉTODOS////////////////
    //Listeners
    
    
    //Init
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Cargar datos
        modelo = new FachadaModelo();
        modelo.init();
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
                if(!selectedItem.getValue().equals("Workspaces")){
                    System.out.println("Cambiando de seleccion");
                    extraerCor.setDisable(true);
                    descriptorItem.setDisable(true);
                    borrarItem2.setDisable(true);
                    //Se cambia el workspace seleccionado, cargando las imágenes y todo eso
                    if(seleccion != null){
                        if(seleccion.getNombre() != selectedItem.getValue()){
                            extraerCor.setDisable(true);
                            
                            
                        }
                    }
                    seleccion = modelo.obtenerWorkspace(selectedItem.getValue());
                    if(seleccion != null){
                        borrarItem.setDisable(false);
                        cerrarItem.setDisable(false);
                        addImage.setDisable(false);
                        compararBtn.setDisable(false);
                        wsMenu.setDisable(false);
                    
                    //LÓGICA DE VISUALIZACIÓN DE IMAGENES -> Se debería añadir también un context menu o algo a cada imagen
                    GridPane gp = new GridPane();
                    gp.setPadding(new Insets(15, 15, 15, 15));
                    //Crear aquí otra caja personalizada que contenga el image view y el texto nombre de la imagen
                    int nFila = 0;
                    int nCol = 0;
                    maxCol = ((int)scrollPane.getWidth())/130;
                    if(maxCol < 2){
                        maxCol = 2;
                    }
                    System.out.println(maxCol);
                    for(int i = 0; i < seleccion.getImagenes().size(); i++){                     
                        if(nCol == maxCol){
                            nFila = nFila+1;
                            nCol = 0;
                        }
                        
                        Pane entrada = new Pane();
                        entrada.setPadding(new Insets(15, 5, 15, 5));
                        Label temp = new Label(seleccion.getImagenes().get(i).getNombre());
                        temp.setMaxWidth(120.0);
                        temp.setMinWidth(120.0);
                        temp.setWrapText(true);
                        temp.setTextAlignment(TextAlignment.CENTER);
                        temp.setAlignment(Pos.CENTER);
                        temp.getStyleClass().add("item");
                        temp.setPadding(new Insets(10,0,10,0));
                        temp.setGraphic(new ImageView(seleccion.getImagenes().get(i).getMiniatura()));
                        temp.setContentDisplay(ContentDisplay.TOP);
                        
                        //Menú de contexto con click derecho sobre el item:
                        ContextMenu contextMenu = new ContextMenu();
                        MenuItem item1 = new MenuItem("Abrir imagen");
                        item1.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                abrirImagenActual();
                            }
                        });
                        MenuItem item2 = new MenuItem("Descriptores");
                        item2.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                try {
                                    extraerDescriptor(null);
                                } catch (IOException ex) {
                                    Logger.getLogger(HomeControlador.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        });
                        // Add MenuItem to ContextMenu
                        contextMenu.getItems().addAll(item1, item2);
                        temp.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {
 
                            @Override
                            public void handle(ContextMenuEvent event) {

                                contextMenu.show(temp, event.getScreenX(), event.getScreenY());
                            }
                        });
                        ////////////FIN DE MENÚ DE CONTEXTO/////////////////
                        
                        entrada.getChildren().add(temp);
                        
                        //entrada.getChildren().add(new ImageView(seleccion.getImagenes().get(i).getMiniatura()));
                        //entrada.getChildren().add(new Label(seleccion.getImagenes().get(i).getNombre())); 
                        
                        gp.add(entrada, nCol, nFila);
                        nCol++;
                    }
                    scrollPane.setContent(gp);
                    //Listener del scrollpane para cambios de anchura: vuelve a recalcular el número de columnas
                    scrollPane.widthProperty().addListener((obs, oldVal, newVal) -> {
                        if(((int)scrollPane.getWidth())/130 != maxCol){
                            String nombreItemSeleccionado = ""; //Se toma la selección para preservarla
                            if(itemSeleccionado != null){
                                nombreItemSeleccionado = itemSeleccionado.getText();
                            }
                            //Se vuelve a recalcular todo
                            System.out.println("Recalculando posiciones de imagenes!");
                            maxCol = ((int)scrollPane.getWidth())/130;
                            if(maxCol < 2){
                                maxCol = 2;
                            }
                            int nFila2 = 0;
                            int nCol2 = 0;
                            GridPane gp2 = new GridPane();
                            gp2.setPadding(new Insets(15, 15, 15, 15));
                            for(int i = 0; i < seleccion.getImagenes().size(); i++){                     
                                if(nCol2 == maxCol){
                                    nFila2 = nFila2+1;
                                    nCol2 = 0;
                                }

                                Pane entrada = new Pane();
                                entrada.setPadding(new Insets(15, 5, 15, 5));
                                Label temp = new Label(seleccion.getImagenes().get(i).getNombre());
                                if(seleccion.getImagenes().get(i).getNombre().equals(nombreItemSeleccionado)){
                                    temp.setStyle("-fx-background-color: blue;");
                                    itemSeleccionado = temp;
                                }
                                temp.setMaxWidth(120.0);
                                temp.setMinWidth(120.0);
                                temp.setWrapText(true);
                                temp.setTextAlignment(TextAlignment.CENTER);
                                temp.setAlignment(Pos.CENTER);
                                temp.getStyleClass().add("item");
                                temp.setPadding(new Insets(10,0,10,0));
                                temp.setGraphic(new ImageView(seleccion.getImagenes().get(i).getMiniatura()));
                                temp.setContentDisplay(ContentDisplay.TOP);
                                
                                //Menú de contexto con click derecho sobre el item:
                                ContextMenu contextMenu = new ContextMenu();
                                MenuItem item1 = new MenuItem("Abrir imagen");
                                item1.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        abrirImagenActual();
                                    }
                                });
                                MenuItem item2 = new MenuItem("Descriptores");
                                item2.setOnAction(new EventHandler<ActionEvent>() {
                                    @Override
                                    public void handle(ActionEvent event) {
                                        try {
                                            extraerDescriptor(null);
                                        } catch (IOException ex) {
                                            Logger.getLogger(HomeControlador.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                });
                                // Add MenuItem to ContextMenu
                                contextMenu.getItems().addAll(item1, item2);
                                temp.setOnContextMenuRequested(new EventHandler<ContextMenuEvent>() {

                                    @Override
                                    public void handle(ContextMenuEvent event) {

                                        contextMenu.show(temp, event.getScreenX(), event.getScreenY());
                                    }
                                });
                                ////////////FIN DE MENÚ DE CONTEXTO/////////////////
                                
                                entrada.getChildren().add(temp);

                                //entrada.getChildren().add(new ImageView(seleccion.getImagenes().get(i).getMiniatura()));
                                //entrada.getChildren().add(new Label(seleccion.getImagenes().get(i).getNombre())); 

                                gp2.add(entrada, nCol2, nFila2);
                                nCol2++;
                            }
                            scrollPane.setContent(gp2);
                            gp2.getChildren().forEach(item -> {
                                item.setOnMouseClicked(new EventHandler<MouseEvent>(){
                                @Override
                                    public void handle(MouseEvent event) {
                                        //Deselecciona la imagen anterior
                                        if(itemSeleccionado != null){
                                            itemSeleccionado.setStyle(null);
                                        }
                                                                          
                                        Object source = event.getSource();
                                        if(source instanceof Pane){
                                            Pane temp = ((Pane)source);
                                            Label temp2 = (Label) temp.getChildren().get(0);
                                            System.out.println(temp2.getText());
                                            imagenSeleccionada = temp2.getText();
                                            
                                            //Selección del label con css
                                            itemSeleccionado = temp2;
                                            itemSeleccionado.setStyle("-fx-background-color: blue;");
                                            
                                        }
                                        System.out.println("Clickao en mi");
                                        if(imagenSeleccionada != null){
                                            extraerCor.setDisable(false);
                                            descriptorItem.setDisable(false);
                                            borrarItem2.setDisable(false);
                                        }else{
                                            extraerCor.setDisable(true);
                                            descriptorItem.setDisable(true);
                                            borrarItem2.setDisable(true);
                                        }
                                        
                                        if(event.getButton().equals(MouseButton.PRIMARY)){
                                            if(event.getClickCount() == 2){
                                                System.out.println("Doble click");
                                                abrirImagenActual();
                                            }
                                        }
                                    }
                                });
                            });
                        }
                    });
                    
                    //Añadir un listener y context menu para seleccionar imagenes en el scrollpane                 
                    gp.getChildren().forEach(item -> {
                        item.setOnMouseClicked(new EventHandler<MouseEvent>(){
                            @Override
                            public void handle(MouseEvent event) {
                                //Deselecciona la imagen anterior
                                if(itemSeleccionado != null){
                                    itemSeleccionado.setStyle(null);
                                }
                                
                                Object source = event.getSource();
                                if(source instanceof Pane){
                                    Pane temp = ((Pane)source);
                                    Label temp2 = (Label) temp.getChildren().get(0);
                                    System.out.println(temp2.getText());
                                    imagenSeleccionada = temp2.getText();
                                    
                                    //Selección del label con css
                                    itemSeleccionado = temp2;
                                    itemSeleccionado.setStyle("-fx-background-color: blue;");
                                }
                                System.out.println("Clickao en mi");
                                if(imagenSeleccionada != null){
                                    extraerCor.setDisable(false);
                                    descriptorItem.setDisable(false);
                                    borrarItem2.setDisable(false);
                                }else{
                                    extraerCor.setDisable(true);
                                    descriptorItem.setDisable(true);
                                    borrarItem2.setDisable(true);
                                }
                                
                                if(event.getButton().equals(MouseButton.PRIMARY)){
                                    if(event.getClickCount() == 2){
                                        System.out.println("Doble click");
                                        abrirImagenActual();        
                                    }
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
                    //Si hay error de apertura en imagenes mostrarlo:
                    if(abierto.getImagenesErroneas() != null){
                        String mensajeError ="";
                        for(int i=0; i<abierto.getImagenesErroneas().size(); i++){
                            mensajeError=mensajeError+abierto.getImagenesErroneas().get(i)+"\n";
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
                        abierto.setImagenesErroneas(null);
                    }
                    
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
                        new FileChooser.ExtensionFilter("BMP", "*.bmp")
                        //new FileChooser.ExtensionFilter("TIF", "*.tif")
                        
                );
                List<File> list = fileChooser.showOpenMultipleDialog(stage);

                if (list != null){
                    for (File file : list){
                        openFile(file, seleccion);
                    }
                    if(seleccion.getImagenesErroneas() != null){
                        System.out.println("Lanzando error de imagenes incorrectas en anhadirImagen()");
                        String mensajeError ="";
                        for(int i=0; i<seleccion.getImagenesErroneas().size(); i++){
                            mensajeError=mensajeError+seleccion.getImagenesErroneas().get(i)+"\n";
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
                        seleccion.setImagenesErroneas(null);
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
    public void comparar(ActionEvent event) throws IOException{
        if(seleccion.getImagenes().size() > 2){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/Comparacion.fxml"));
            Parent root = (Parent)loader.load();
            ComparacionController controller = (ComparacionController)loader.getController();
            Stage stageLocal = new Stage();
            stageLocal.initModality(Modality.APPLICATION_MODAL);
            stageLocal.initOwner(stage);
            stageLocal.setTitle("Modo de comparación");
            stageLocal.setResizable(false); //Evitar que se pueda cambiar de tam
            //Setters para todos los atributos
            controller.setModelo(modelo);
            controller.setStage(stageLocal);
            controller.setStagePadre(stage);
            controller.setSel(seleccion);
            stageLocal.setScene(new Scene(root));
            stageLocal.show();
        
        }else{
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Imágenes insuficientes");
            alert.setHeaderText(null);
            alert.setContentText("Debe haber 2 o más imágenes en el workspace para realizar una comparación");
            alert.showAndWait();
        }

    }
    
    @FXML 
    public void explorador(ActionEvent event){
        try {
            Runtime.getRuntime().exec("explorer.exe /select," + seleccion.getPath());
        } catch (IOException ex) {
            Logger.getLogger(HomeControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //Abre la versión completa de la imagen seleccionada actualmente
    private void abrirImagenActual(){
        System.out.println("Abriendo...");
        try {
            desktop.open(new File(seleccion.getPath()+"\\"+imagenSeleccionada));
        } catch (IOException ex) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Error al abrir imagen");
            alert.setHeaderText(null);
            alert.setContentText("La imagen no existe o no hay un visualizador de imágenes instalado en el sistema");
            alert.showAndWait();
        }
    }
    
    //Menús y aperturas de nuevas ventanas
    @FXML 
    public void nuevoWorkspace(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/NuevoWorkspace.fxml"));
        Parent root = (Parent)loader.load();
        NuevoWorkspaceController controller = (NuevoWorkspaceController)loader.getController();
        Stage stageLocal = new Stage();
        stageLocal.initModality(Modality.APPLICATION_MODAL);
        stageLocal.initOwner(stage);
        stageLocal.setTitle("Selecciona nombre para el Workspace");
        stageLocal.setResizable(false); //Evitar que se pueda cambiar de tam
        //Setters para todos los atributos
        controller.setModelo(modelo);
        controller.setStage(stageLocal);
        controller.setWsTree(wsTree);
        stageLocal.setScene(new Scene(root));
        stageLocal.show();
    }
    
    @FXML
    public void extraerDescriptor(ActionEvent event) throws IOException{ 
        //Se obtiene referencia a la imagen:
        Imagen objetivo = null;
        for(int i = 0; i<seleccion.getImagenes().size(); i++){
            if(seleccion.getImagenes().get(i).getNombre().equals(imagenSeleccionada)){
                objetivo = seleccion.getImagenes().get(i);
            }
        }
        
        if(objetivo != null){
            System.out.println("Extraer descriptor de la imagen: " + imagenSeleccionada);
            //seleccion.extraerDescriptorCoocurrencia(imagenSeleccionada);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/Extraccion.fxml"));
            Parent root = (Parent)loader.load();
            ExtraccionController controller = (ExtraccionController)loader.getController();
            Stage stageLocal = new Stage();
            stageLocal.initModality(Modality.APPLICATION_MODAL);
            stageLocal.initOwner(stage);
            stageLocal.setTitle("Descriptores de " + imagenSeleccionada);
            //stageLocal.setResizable(false); //Evitar que se pueda cambiar de tam
            //Setters para todos los atributos
            controller.setModelo(modelo);
            controller.setImagen(objetivo);
            controller.setStage(stageLocal);
            controller.setSeleccion(seleccion);
            stageLocal.setScene(new Scene(root));
            stageLocal.show();
        }
        
    }
    
    @FXML
    public void ajustes(ActionEvent event) throws IOException{     
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vista/Configuracion.fxml"));
        //Parent root = (Parent)loader.load();
        //ConfiguracionController controller = (ConfiguracionController)loader.getController();
        ConfiguracionController controller = new ConfiguracionController(modelo);
        loader.setController(controller);
        Parent root = (Parent)loader.load();
        Stage stageLocal = new Stage();
        stageLocal.initModality(Modality.APPLICATION_MODAL);
        stageLocal.initOwner(stage);
        stageLocal.setTitle("Ajustes");
        stageLocal.setResizable(false); //Evitar que se pueda cambiar de tam
        //Setters para todos los atributos
        //controller.setModelo(modelo);
        controller.setStage(stageLocal);
        stageLocal.setScene(new Scene(root));
        stageLocal.show();
    }
    
    @FXML
    public void salir(ActionEvent event) throws IOException{
        modelo.close();
        stage.close();
    }
}
