package DataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
    TestDAO test;
    
    private FachadaDAO(){
        //Se crea la conexion
        try{
            conexion = DriverManager.getConnection(protocol+dbName+";create=true", null); //Con create=true se crea si no existe por primera vez
            test = new TestDAO(conexion);
        }catch(SQLException se){  
            System.err.println("Error al inicializar la conexión BD");
            System.err.println(se);
        }
    }  
    //Getter del Singleton
    public static FachadaDAO getFachada(){
        return fachada;
    }
    
    //Métodos DAO:
    public void initTest(){
        this.test.initTest();
    }
    public int getNumTest(){
        return this.test.getNumTest();
    }
    public void insertTest(int n, String v){
        this.test.insertTest(n, v);
    }
}
