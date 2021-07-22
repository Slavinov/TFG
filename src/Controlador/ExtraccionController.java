package Controlador;

import Modelo.DescriptorCoocurrencia;
import Modelo.FachadaModelo;
import Modelo.Imagen;
import Modelo.Workspace;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class del panel de visualización/extracción de descriptores.
 *
 * @author Stanislav
 */
public class ExtraccionController implements Initializable {
    private Stage stage;
    private FachadaModelo modelo;
    private Imagen imagen; //Imagen cuyos descriptores se están viendo/extrayendo
    private String modo; //Modo de extracción seleccionado
    private Workspace seleccion;
    
    @FXML
    private TreeView treeView;
    
    @FXML
    private ScrollPane scrollPane;

    @FXML 
    private Button extraerBtn;

    public Workspace getSeleccion() {
        return seleccion;
    }

    public void setSeleccion(Workspace seleccion) {
        this.seleccion = seleccion;
    }  
    
    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public FachadaModelo getModelo() {
        return modelo;
    }

    public void setModelo(FachadaModelo modelo) {
        this.modelo = modelo;
    }

    public Imagen getImagen() {
        return imagen;
    }

    public void setImagen(Imagen imagen) {
        this.imagen = imagen;
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Establecer el treeView
        TreeItem rootItem = new TreeItem("Textura");
        rootItem.setExpanded(true);
        rootItem.getChildren().add(new TreeItem("Coocurrencia"));
        //Añadir aquí otras categorías
        treeView.setRoot(rootItem);
        // Listener para el tree
        treeView.getSelectionModel().selectedItemProperty().addListener( 
        new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue,
                    Object newValue) {

                TreeItem<String> selectedItem = (TreeItem<String>) newValue;
                System.out.println("Selected Text : " + selectedItem.getValue());
                if(!selectedItem.getValue().equals("Textura")){
                    extraerBtn.setDisable(false);
                    //Crear la vista de detalles, cargando los datos desde el descriptor de este tipo desde el propio objeto de Imagen. El propio objeto únicamente se crea al extraer un nuevo descriptor o al abrir el programa en init, donde se leen los archivos de descriptores existentes!
                    if(selectedItem.getValue().equals("Coocurrencia")){
                        modo = "Coocurrencia";
                        //Vista de coocurrencia si existe
                        DescriptorCoocurrencia descriptor = null;
                        for(int i = 0; i< imagen.getDescriptores().size(); i++){
                            if(imagen.getDescriptores().get(i) instanceof DescriptorCoocurrencia){
                                descriptor = (DescriptorCoocurrencia) imagen.getDescriptores().get(i);
                            }
                        }
                        if(descriptor == null){
                            //Mensaje de descriptor inexistente
                            Pane pane = new Pane();
                            Label label = new Label("No existen descriptores de coocurrencia para esta imagen. Utiliza el botón de \"Extraer\" para obtenerlos.");
                            pane.getChildren().add(label);
                            scrollPane.setContent(pane);
                        }else{
                            //Se muestran todos los detalles del descriptor seleciconado
                            establecerDetallesCoocurrencia();
                        }
                            
                        
                        
                    }
                }
                
            }
        });
    }    
    
    //métodods de apoyo para actualizar la vista de detalles
    private void establecerDetallesCoocurrencia(){
        DescriptorCoocurrencia descriptor = null;
        for(int i = 0; i< imagen.getDescriptores().size(); i++){
            if(imagen.getDescriptores().get(i) instanceof DescriptorCoocurrencia){
                descriptor = (DescriptorCoocurrencia) imagen.getDescriptores().get(i);
            }
        }
        if(descriptor != null){
            extraerBtn.setText("Actualizar");
            GridPane gp = new GridPane();
            gp.setGridLinesVisible(true);
            //Se introducen todos los datos del descriptor en la tabla
            Label c1 = new Label("0º");
            Label c2 = new Label("45º");
            Label c3 = new Label("90º");
            Label c4 = new Label("135º");
            Label c5 = new Label("Media");
            Label c6 = new Label("Rango (max-min)");
            c1.setPadding(new Insets(2,2,2,2));
            c2.setPadding(new Insets(2,2,2,2));
            c3.setPadding(new Insets(2,2,2,2));
            c4.setPadding(new Insets(2,2,2,2));
            c5.setPadding(new Insets(2,2,2,2));
            c6.setPadding(new Insets(2,2,2,2));
            
            gp.add(c1, 1, 0);
            gp.add(c2, 2, 0);
            gp.add(c3, 3, 0);
            gp.add(c4, 4, 0);
            gp.add(c5, 5, 0);
            gp.add(c6, 6, 0);
            
            Label r1 = new Label("Energía");
            Label r2 = new Label("Inercia");
            Label r3 = new Label("Correlación");
            Label r4 = new Label("IDM");
            Label r5 = new Label("Entropía");
            r1.setPadding(new Insets(2,2,2,2));
            r2.setPadding(new Insets(2,2,2,2));
            r3.setPadding(new Insets(2,2,2,2));
            r4.setPadding(new Insets(2,2,2,2));
            r5.setPadding(new Insets(2,2,2,2));
            
            gp.add(r1, 0, 1);
            gp.add(r2, 0, 2);
            gp.add(r3, 0, 3);
            gp.add(r4, 0, 4);
            gp.add(r5, 0, 5);
            
            float[][] res = descriptor.devolverMatriz();
            int row = 1;
            int col = 1;
            for(int i = 0; i < 5; i++){
                for(int j = 0; j<6; j++){
                    //Label temp = new Label(Float.toString(res[i][j]));
                    TextField temp = new TextField(Float.toString(res[i][j]));
                    temp.setEditable(false);
                    temp.setPadding(new Insets(2,2,2,2));
                    gp.add(temp, col, row);
                    col++;
                }
                row++;
                col = 1;
            }
            gp.setPadding(new Insets(50,10,10,10));
            Pane pane = new Pane();
            Label dist = new Label("Distancia = " + descriptor.getDistancia());
            dist.setPadding(new Insets(10,10,10,10));
           
            pane.getChildren().add(dist);
            pane.getChildren().add(gp);
            scrollPane.setContent(pane);
        }
    }
    
    @FXML 
    public void extraer(ActionEvent event){
        //Al terminar de extraer, refresca la vista seleccionada
        treeView.setDisable(true);
        if(modo.equals("Coocurrencia")){
            System.out.println("Extrayendo Coocurrencia...");
            seleccion.extraerDescriptorCoocurrencia(imagen.getNombre());
            this.establecerDetallesCoocurrencia();
        }
        
        treeView.setDisable(false);
    }
    
    @FXML 
    public void cerrar(ActionEvent event){
        stage.close();
    }
}
