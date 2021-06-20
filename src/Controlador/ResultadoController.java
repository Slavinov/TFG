/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.FachadaModelo;
import Modelo.Workspace;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Stanislav
 */
public class ResultadoController implements Initializable {
    private FachadaModelo modelo;
    private Stage stage;
    private Workspace sel;

    //Constructor personalizado, ya que se requiere acceder a la selección antes de nada para poder iniciar la vista correctamente
    public ResultadoController(Workspace w){
        sel = w;
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

    public Workspace getSel() {
        return sel;
    }

    public void setSel(Workspace sel) {
        this.sel = sel;
    }   
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar la vista con todas las imágenes resultado de sel.
        
    }    
    
}
