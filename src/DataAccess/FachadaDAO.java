package DataAccess;

import ValueObjects.ConfigVO;
import ValueObjects.WorkspaceVO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Singleton que centraliza la interacción con la BD Derby. Inicia la conexion a la BD, la crea si hace falta, y sirve de intermediario con los DAOs.
 * @author Stanislav
 */
public class FachadaDAO {
    //Configuración de Derby
    private String framework = "embedded"; 
    private String protocol = "jdbc:derby:";
    
    //Instancia de la fachada
    private static final FachadaDAO fachada = new FachadaDAO();
    
    //BD
    String dbName = "derbyBD";
    Connection conexion;
    ConfigDAO configDAO;
    WorkspaceDAO workspaceDAO;
    
    private FachadaDAO(){
        //Se crea la conexion
        try{
            conexion = DriverManager.getConnection(protocol+dbName+";create=true", null); //Con create=true se crea si no existe por primera vez
            configDAO = new ConfigDAO(conexion);
            workspaceDAO = new WorkspaceDAO(conexion);
        }catch(SQLException se){  
            System.err.println("Error al inicializar la conexión BD");
            System.err.println(se);
        }
    }  
    //Getter del Singleton
    public static FachadaDAO getFachada(){
        return fachada;
    }
    
    //Config
    public void initConfig(){
        this.configDAO.initConfig();
    }
    
    public void setConfig(ConfigVO entrada){
        this.configDAO.setConfig(entrada);
    }
    
    public ConfigVO getConfig(){
        return this.configDAO.getConfig();
    }
    
    //Workspaces
    public void initWorkspace(){
        this.workspaceDAO.initWorkspace();
    }
    
    public void insertarWorkspace(WorkspaceVO ws){
        this.workspaceDAO.insertarWorkspace(ws);
    }
    
    public ArrayList<WorkspaceVO> recuperarWorkspaces(){
        return this.workspaceDAO.recuperarWorkspaces();
    }
    
    public void borrarWorkspace(String nombre){
        this.workspaceDAO.borrarWorkspace(nombre);
    }
    
    
    //close
    public void close(){
        try {
            this.conexion.close();
        } catch (SQLException ex) {
            System.out.println("Error al cerrar la bd");
        }
    }
}
