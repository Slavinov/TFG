package Modelo;

import DataAccess.FachadaDAO;
import java.util.ArrayList;

/**
 *
 * @author Stanislav
 */
public class FachadaModelo {
    private ArrayList<Workspace> workspaces;
    private String path; //Path por defecto de los workspaces, establecido o no por el usuario
    private FachadaDAO baseDatos;
    
    public FachadaModelo(){
        workspaces = new ArrayList<Workspace>();
        baseDatos = FachadaDAO.getFachada();
    }

    public FachadaDAO getBaseDatos() {
        return baseDatos;
    }

    public void setBaseDatos(FachadaDAO baseDatos) {
        this.baseDatos = baseDatos;
    }
    
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    } 

    public ArrayList<Workspace> getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(ArrayList<Workspace> workspaces) {
        this.workspaces = workspaces;
    }
    
    /////////////////////MÉTODOS DEL MODELO/////////////////////////
    //Métodos generales
    public void init(){
        //Creación de la fachada BD, inicialización de DAOs
        
        //Extraer config y Workspaces, meter los workspaces en el arraylist y establecer los atributos de config. En caso de fallo? reintentar? -> AÑADIR UN ACTIVITY INDICATOR O ALGO
        
        //Detección de cambios en el directorio (watching directory for changes) en todos los workspaces (meter un listener?).
    }
    
    public void close(){
        //Guardar la configuración
        
        //Guardar los Workspaces en la base de datos
        
        //Cerrar la conexion a la base de datos
    }
    
    //Métodos de gestión de Workspaces
    public void crearWorkspace(){
        //Crear el workspace utilizando el constructor de ese
        
        //Guardar su referencia en la BD
        
        //En caso de añadir imágenes, tratarlas aquí (extraer info, descriptores...)
    }
    
    public void borrarWorkspace(){
        //Eliminar todo el contenido del workspace
        //Eliminar su referencia de la BD
    }
    
    public void abrirWorkspace(){
        //Escanear el contenido de la carpeta, tratando las imágenes encontradas
        
        //Añadir referencias a la BD
    }
    
    public void cerrarWorkspace(){
        //Borrar el Workspace de la base de datos y eliminarlo del ArrayList
    }
    
    //Métodos de guardado de estado
    public void guardarConfig(){
        //Guardar la configuración en la BD
    }
    
    public void guardarWorkspace(){
        //Guardar el workspace seleccionado en la BD
    }
}
