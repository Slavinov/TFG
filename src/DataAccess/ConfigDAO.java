package DataAccess;

import ValueObjects.ConfigVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Data Access Object para almacenar y recuperar la configuración generada por el usuario entre sesiones.
 * @author Stanislav
 */
public class ConfigDAO extends AbstractDAO{
    //Constructor 
    public ConfigDAO(Connection c) {
        super(c);
    }
    
    //Método para crear la tabla y establecer la configuración por defecto
    public void initConfig(){     
        try{
            Statement s = this.getConexion().createStatement();
            s.execute("CREATE TABLE Config(defaultPath varchar(255), id int primary key)");
            s.close();
            
            Statement i = this.getConexion().createStatement();
            i.executeUpdate("insert into Config values(null, 0)");
            i.close();
        }catch(SQLException se){  
            System.err.println("Error al crear tabla Config");
            System.err.println(se);
        }
    }
    
    //Método para modificar configuración: modifica la fila de configuración
    public void setConfig(ConfigVO entrada){
        try{
            //Inserción de nueva configuración
            PreparedStatement psInsert;
            psInsert = this.getConexion().prepareStatement("update Config SET defaultPath=? WHERE id=0");
            this.getStatements().add(psInsert);
            psInsert.setString(1, entrada.getDefaultPath());
            psInsert.executeUpdate();
            
        }catch(SQLException se){  
            System.err.println("Error al modificar la Tabla Config");
            System.err.println(se);
        }
    }
    
    //Método para extraer config
    public ConfigVO getConfig(){
        ConfigVO resultado = new ConfigVO();
        ResultSet rs = null;
        try{
            Statement s = this.getConexion().createStatement();
            rs = s.executeQuery(
                    "SELECT defaultPath FROM Config");
            while(rs.next()) {
                resultado.setDefaultPath(rs.getString("defaultPath"));
                //System.out.println("Entrada: num = " + rs.getInt("num") + " addr = " + rs.getString("addr"));
            }
        }catch(SQLException se){
            System.err.println("Error al seleccionar en Tabla Config");
            System.err.println(se);
        }
        
        return resultado;
    }
}
