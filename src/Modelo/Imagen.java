package Modelo;

import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 *
 * @author Stanislav
 */
public class Imagen {
    private String nombre;
    private String formato; //Irrelevante?
    private ArrayList<Descriptor> descriptores = new ArrayList<Descriptor>(); //Irrelevante?
    private int[][] valores;
    private int[][] valoresBlancos; //Necesario para crear la máscara
    private int altura;
    private int anchura;
    private int bandas; //Interesante?
    private Image miniatura;
    
    
    public Imagen(String n){
        nombre = n;
    }
    //Almacenar todo (formato,tipo,rows,cols,data(valores de pixeles))? probablemente. Desde Cargador -> extraer valores de pixeles, utilizando diferentes métodos dependiendo del formato quizás 

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public ArrayList<Descriptor> getDescriptores() {
        return descriptores;
    }

    public void setDescriptores(ArrayList<Descriptor> descriptores) {
        this.descriptores = descriptores;
    }

    public int[][] getValores() {
        return valores;
    }

    public void setValores(int[][] valores) {
        this.valores = valores;
    }

    public int[][] getValoresBlancos() {
        return valoresBlancos;
    }

    public void setValoresBlancos(int[][] valoresBlancos) {
        this.valoresBlancos = valoresBlancos;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public int getAnchura() {
        return anchura;
    }

    public void setAnchura(int anchura) {
        this.anchura = anchura;
    }

    public int getBandas() {
        return bandas;
    }

    public void setBandas(int bandas) {
        this.bandas = bandas;
    }

    public Image getMiniatura() {
        return miniatura;
    }

    public void setMiniatura(Image miniatura) {
        this.miniatura = miniatura;
    }
    
    
}
