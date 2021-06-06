package DataAccess;

import java.sql.Statement;
import java.util.ArrayList;

/**
 * Prototipo de DAO
 * @author Stanislav
 */
public abstract class AbstractDAO {
    private java.sql.Connection conexion;   
    private ArrayList<Statement> statements = new ArrayList<Statement>();
    public AbstractDAO(java.sql.Connection c){
        conexion = c;
    }

    public ArrayList<Statement> getStatements() {
        return statements;
    }

    public void setStatements(ArrayList<Statement> statements) {
        this.statements = statements;
    }
    
     
    
    protected java.sql.Connection getConexion(){
        return this.conexion;
    }
    
    protected void setConexion(java.sql.Connection conexion){
        this.conexion=conexion;
    }
}
