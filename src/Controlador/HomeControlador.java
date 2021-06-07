package Controlador;

import Modelo.FachadaModelo;
import Modelo.Workspace;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
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
    
    //Atributos de elementos de JavaFX
    @FXML
    private TextField nombre;
    
    //Getters/Setters
    public void setStage(Stage s){
        stage = s;
    }
    
    /////////////MÉTODOS////////////////
    
    
    //Init
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Cargar datos
        modelo = new FachadaModelo();
        modelo.init();
        
        //Establecer el path inicial?
        
        
        //Actualizar la vista con los workspaces cargados
        
        System.out.println("Initialize en el controlador");
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
            System.out.println(file.getAbsolutePath());
        }
    }
    
    @FXML
    public void resetearPath(ActionEvent event){
        
    }

    @FXML
    public void crearWorkspace(ActionEvent event){
        System.out.println("Funciono!");
        //Creación de nuevo Workspace
        workspaces.add(new Workspace(nombre.getText()));      
        //Selector de archivos
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selección de imágenes");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Todo", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg")
        );
        List<File> list = fileChooser.showOpenMultipleDialog(stage);
        
        if (list != null){
            for (File file : list){
                openFile(file);
            }
        }
    }
    
    //Crear una nueva vista en plan popup (en marcadores)
    @FXML
    public void testAction(ActionEvent event){
        System.out.println("Funciono2!");
        
    }
    
    
    
    
    private void openFile(File file) {
        try {
            //desktop.open(file); //Mostrar la imagen
            System.out.println(file.getName());
            File d = new File(workspaces.get(workspaces.size()-1).getPath()+"\\"+workspaces.get(workspaces.size()-1).getNombre()+"\\"+file.getName());
            Path destino = d.toPath();
            //file.renameTo(new File(workspaces.get(workspaces.size()-1).getPath()+"\\"+workspaces.get(workspaces.size()-1).getNombre()+"\\"+file.getName())); //Se mueve imagen a la carpeta destino del workspace
            Files.copy(file.toPath(), destino , StandardCopyOption.REPLACE_EXISTING); //Se copia al destino
        } catch (IOException ex) {
            Logger.getLogger(
                HomeControlador.class.getName()).log(
                    Level.SEVERE, null, ex
                );
        }
    }
    
}
