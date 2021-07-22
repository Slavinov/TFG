package ValueObjects;


/**
 * Objeto de encapsulación para los datos de un Workspace específico
 * @author Stanislav
 */
public class WorkspaceVO {
    private String nombre;
    private String path;
    //private ArrayList<Imagen> imagenes;
    
    public WorkspaceVO(){
        
    }
    
    public WorkspaceVO(String n, String p){
        nombre = n;
        path = p;
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
      
    
    
}
