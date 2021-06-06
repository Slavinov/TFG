package Controlador;

import Modelo.Workspace;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * Controlador de la vista principal JavaFX
 * @author Stanislav
 */
public class HomeControlador {
    private ArrayList<Workspace> workspaces = new ArrayList<Workspace>(); 
    private Stage stage;
    private Desktop desktop = Desktop.getDesktop(); //isDesktopSupported() para comprobar en otros sistemas. Sirve para visualizar imagenes y tal. Mejor buscar visualizador de java.
    
    @FXML
    private TextField nombre;
    
    public void setStage(Stage s){
        stage = s;
    }
    //Métodos
    @FXML
    public void crearWorkspace(ActionEvent event){
        System.out.println("Funciono!");
        //Creación de nuevo Workspace
        workspaces.add(new Workspace(nombre.getText()));      
        //Selector de archivos
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selección de imágenes");
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
