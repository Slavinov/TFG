package DataAccess;

import ValueObjects.WorkspaceVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Data Access Object para extraer y almacenar información persistente sobre los Workspaces utilizados por el programa.
 * @author Stanislav
 */
public class WorkspaceDAO extends AbstractDAO{
    //Constructor
    public WorkspaceDAO(Connection c) {
        super(c);
    }
    
    //Método para crear las tablas
    public void initWorkspace(){     
        try{
            Statement s = this.getConexion().createStatement();
            s.execute("CREATE TABLE Workspaces(nombre varchar(255) primary key, path varchar(255))");
            s.close();
            
        }catch(SQLException se){  
            System.err.println("Error al crear tabla Workspaces");
            System.err.println(se);
        }
    }
    
    //Método para insertar workspaces
    public void insertarWorkspace(WorkspaceVO entrada){
        try{        
            PreparedStatement psInsert;
            psInsert = this.getConexion().prepareStatement("insert into Workspaces values(?,?)");
            this.getStatements().add(psInsert);
            psInsert.setString(1, entrada.getNombre());
            psInsert.setString(2, entrada.getPath());
            psInsert.executeUpdate();
            
        }catch(SQLException se){  
            System.err.println("Error al insertar en Tabla Workspace");
            System.err.println(se);
        }
    }
    
    //Método para extraer workspaces
    public ArrayList<WorkspaceVO> recuperarWorkspaces(){
        ArrayList<WorkspaceVO> resultado = new ArrayList<WorkspaceVO>();
        
        
        ResultSet rs = null;
        try{
            Statement s = this.getConexion().createStatement();
            rs = s.executeQuery(
                    "SELECT nombre, path FROM Workspaces ORDER BY nombre");
            while(rs.next()) {
                //System.out.println("Entrada: num = " + rs.getInt("num") + " addr = " + rs.getString("addr"));
                resultado.add(new WorkspaceVO(rs.getString("nombre"), rs.getString("path")));
            }
        }catch(SQLException se){
            System.err.println("Error al seleccionar en Tabla Workspaces");
            System.err.println(se);
        }
        
        
        return resultado;
    }
    
    //Método/s para modificar workspaces? -> añadir/borrar imagenes? O se sobreescribe el workspace y ya???
    
    //Método para borrar un Workspace determinado
    public void borrarWorkspace(String nombre){
        try{        
            PreparedStatement psInsert;
            psInsert = this.getConexion().prepareStatement("delete from Workspaces where nombre=?");
            this.getStatements().add(psInsert);
            psInsert.setString(1, nombre);
            psInsert.executeUpdate();
            
        }catch(SQLException se){  
            System.err.println("Error al borrar en Tabla Workspace");
            System.err.println(se);
        }
    }
    
}
