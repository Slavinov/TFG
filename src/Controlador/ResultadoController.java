/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controlador;

import Modelo.FachadaModelo;
import Modelo.Workspace;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.TextAlignment;
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
    private Desktop desktop = Desktop.getDesktop();
    private String imagenSeleccionada;
    private Label itemSeleccionado = null;
    @FXML
    private ScrollPane scrollPane;
    
    
    //Imagen de referencia + las 3 imágenes más similares
    @FXML
    private Label ref;
    @FXML
    private Label res1;
    @FXML
    private Label res2;
    @FXML
    private Label res3;
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
        
        //Inicializar los 3 labels de arriba:
        ref.setText(sel.getReferencia().getNombre());
        ref.setMaxWidth(120.0);
        ref.setMinWidth(120.0);
        ref.setWrapText(true);
        ref.setTextAlignment(TextAlignment.CENTER);
        ref.setAlignment(Pos.CENTER);
        ref.setPadding(new Insets(10,0,10,0));
        ref.setGraphic(new ImageView(sel.getReferencia().getMiniatura()));
        ref.setContentDisplay(ContentDisplay.TOP);
        
        res1.setText(sel.getResultadoComparacion().get(0).getNombre());
        res1.setMaxWidth(120.0);
        res1.setMinWidth(120.0);
        res1.setWrapText(true);
        res1.setTextAlignment(TextAlignment.CENTER);
        res1.setAlignment(Pos.CENTER);
        res1.setPadding(new Insets(10,0,10,0));
        res1.setGraphic(new ImageView(sel.getResultadoComparacion().get(0).getMiniatura()));
        res1.setContentDisplay(ContentDisplay.TOP);
        
        res2.setText(sel.getResultadoComparacion().get(1).getNombre());
        res2.setMaxWidth(120.0);
        res2.setMinWidth(120.0);
        res2.setWrapText(true);
        res2.setTextAlignment(TextAlignment.CENTER);
        res2.setAlignment(Pos.CENTER);
        res2.setPadding(new Insets(10,0,10,0));
        res2.setGraphic(new ImageView(sel.getResultadoComparacion().get(1).getMiniatura()));
        res2.setContentDisplay(ContentDisplay.TOP);
        
        if(sel.getResultadoComparacion().size() > 2){
            res3.setText(sel.getResultadoComparacion().get(2).getNombre());
            res3.setMaxWidth(120.0);
            res3.setMinWidth(120.0);
            res3.setWrapText(true);
            res3.setTextAlignment(TextAlignment.CENTER);
            res3.setAlignment(Pos.CENTER);
            res3.setPadding(new Insets(10,0,10,0));
            res3.setGraphic(new ImageView(sel.getResultadoComparacion().get(2).getMiniatura()));
            res3.setContentDisplay(ContentDisplay.TOP);        
        }
        
        //Inicializar el scrollPane
        GridPane gp = new GridPane();
        gp.setPadding(new Insets(15, 15, 15, 15));
        //Crear aquí otra caja personalizada que contenga el image view y el texto nombre de la imagen
        int nFila = 0;
        int nCol = 0;
        int maxCol = 680/130;
        
        System.out.println("num de columnas máximo: "+maxCol + " " + scrollPane.getWidth());
        for(int i = 0; i < sel.getResultadoComparacion().size(); i++){                     
            if(nCol == maxCol){
                nFila = nFila+1;
                nCol = 0;
            }

            Pane entrada = new Pane();
            entrada.setPadding(new Insets(15, 5, 15, 5));
            Label temp = new Label(sel.getResultadoComparacion().get(i).getNombre());
            temp.setMaxWidth(120.0);
            temp.setMinWidth(120.0);
            temp.setWrapText(true);
            temp.setTextAlignment(TextAlignment.CENTER);
            temp.setAlignment(Pos.CENTER);
            temp.getStyleClass().add("item");
            temp.setPadding(new Insets(10,0,10,0));
            temp.setGraphic(new ImageView(sel.getResultadoComparacion().get(i).getMiniatura()));
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
            MenuItem item2 = new MenuItem("Abrir ubicación");
            item2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    System.out.println("NO IMPLEMENTADO, DEBE IR A OTRA VENTANA");
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
        //Listener para clicks:
        
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
    
    private void abrirImagenActual(){
        System.out.println("Abriendo...");
        try {
            desktop.open(new File(sel.getPath()+"\\"+imagenSeleccionada));
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error al abrir imagen");
            alert.setHeaderText(null);
            alert.setContentText("La imagen no existe o no hay un visualizador de imágenes instalado en el sistema");
            alert.showAndWait();
        }
    }
    
    @FXML
    public void cerrar(ActionEvent event){
        stage.close();
    }
}
