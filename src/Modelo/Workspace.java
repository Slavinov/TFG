package Modelo;

import java.io.File;

/**
 *
 * @author Stanislav
 */
public class Workspace {
    private String nombre; //Nombre único del workspace, dado por el usuario al crearlo
    private String path; //Carpeta en la que se encuentra el workspace, se establece al crearse el workspace 
    
    
    public Workspace(String n){
        nombre = n;
        path = System.getProperty("user.home")+"\\Workspaces"; // path por defecto
        new File(path).mkdirs(); //Crea el directorio si no lo hay
        new File(path+"\\"+nombre).mkdirs(); //Crea el directorio si no lo hay
    }
    
    public Workspace(String n, String p){
        nombre = n;
        path = p; 
        new File(path).mkdirs(); //Crea el directorio si no lo hay
        new File(path+"\\"+nombre).mkdirs(); //Crea el directorio si no lo hay
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
    
    //Guardar todos los workspaces creados? -> Comprobar por ejemplo al iniciar si existen y borrar las referencias de los que ya no existen y tal.
    //Detección de cambios en el directorio (watching directory for changes)
    //Debería haber algún tipo de seguimiento de los objetos guardados aquí?, ya que el usuario podría cambiar el nombre de los archivos o algo
    //Añadir elementos
    //Borrar elemento
    
    
}
