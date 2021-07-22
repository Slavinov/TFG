package Modelo;

import java.util.ArrayList;
import javafx.scene.image.Image;

/**
 *
 * @author Stanislav
 */
public class Imagen {
    private String nombre;
    private String formato; //Irrelevante
    private ArrayList<Descriptor> descriptores = new ArrayList<Descriptor>(); 
    private int[][] valores;
    private int[][] valoresBlancos; //Necesario para crear la máscara
    private int altura;
    private int anchura;
    private int bandas; 
    private Image miniatura;
    private float distanciaUltimaComparativa; //Distancia hasta la imagen de referencia obtenida en la última comparativa
    
    
    public Imagen(String n){
        nombre = n;
    }
    

    public float getDistanciaUltimaComparativa() {
        return distanciaUltimaComparativa;
    }

    public void setDistanciaUltimaComparativa(float distanciaUltimaComparativa) {
        this.distanciaUltimaComparativa = distanciaUltimaComparativa;
    }

    
    
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
