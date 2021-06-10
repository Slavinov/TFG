package Modelo;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Stanislav
 */
public class Workspace {
    private String nombre; //Nombre único del workspace, dado por el usuario al crearlo
    private String path; //Path a la Carpeta en la que se encuentra el workspace, se establece al crearse el workspace 
    private ArrayList<Imagen> imagenes;
    private File carpeta;
    
    public Workspace(String n){
        imagenes = new ArrayList<Imagen>();
        nombre = n;
        path = System.getProperty("user.home")+"\\Workspaces"; // path por defecto
        new File(path).mkdirs(); //Crea el directorio si no lo hay
        new File(path+"\\"+nombre).mkdirs(); //Crea el directorio si no lo hay
        path = path+"\\"+nombre;
    }
    
    public Workspace(String n, String p){
        imagenes = new ArrayList<Imagen>();
        nombre = n;
        path = p; 
        new File(path).mkdirs(); //Crea el directorio si no lo hay
        //new File(path+"\\"+nombre).mkdirs(); //Crea el directorio si no lo hay
        //path = path+"\\"+nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public File getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(File carpeta) {
        this.carpeta = carpeta;
    }
    
    
    
    //MÉTODOS
    
    
    //Carga la imagen extrayendo los datos a la clase Imagen y saca la miniatura
    public void anhadirImagen(File imagen){
        
    }
    
    public void eliminarImagen(String nombre){
        
    }
    
    public void detectarImagenes(){
        //Busca las imagenes en la carpeta, las procesa y añade a este workspace.
    }
    
    public void extraerDescriptor(String nombre){
        
    }
    
    public void compararCoocurrencia(File referencia){
        //Comprueba que existen todos los descriptores, en caso contrario los crea, luego realiza la extracción de la imagen de referencia, y al final realiza la comparación
    }
    
    public void compararLaws(File referencia){
        
    }
    //Guardar todos los workspaces creados? -> Comprobar por ejemplo al iniciar si existen y borrar las referencias de los que ya no existen y tal.
    //Detección de cambios en el directorio (watching directory for changes)
    //Debería haber algún tipo de seguimiento de los objetos guardados aquí?, ya que el usuario podría cambiar el nombre de los archivos o algo
    //Añadir elementos
    //Borrar elemento
    
    
}
