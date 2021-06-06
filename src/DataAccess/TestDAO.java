/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Stanislav
 */
public class TestDAO extends AbstractDAO{
    
    public TestDAO(Connection c) {
        super(c);
    }
    
    //Creación de tablas test
    public void initTest(){     
        try{
            Statement s = this.getConexion().createStatement();
            this.getStatements().add(s);
            s.execute("CREATE TABLE Test(num int, addr varchar(40))");
        
        }catch(SQLException se){  
            System.err.println("Error al crear tabla Test");
            System.err.println(se);
        }
    }
    
    public int getNumTest(){
        int resultado = 0;
        ResultSet rs = null;
        try{
            Statement s = this.getConexion().createStatement();
            rs = s.executeQuery(
                    "SELECT num, addr FROM Test ORDER BY num");
            while(rs.next()) {
                System.out.println("Entrada: num = " + rs.getInt("num") + " addr = " + rs.getString("addr"));
            }
        }catch(SQLException se){
            System.err.println("Error al seleccionar en Tabla Test");
            System.err.println(se);
        }
        
        return resultado;
    }
    
    public void insertTest(int n, String v){
        try{
            
            PreparedStatement psInsert;
            psInsert = this.getConexion().prepareStatement("insert into Test values(?,?)");
            this.getStatements().add(psInsert);
            psInsert.setInt(1, n);
            psInsert.setString(2, v);
            psInsert.executeUpdate();
            
        }catch(SQLException se){  
            System.err.println("Error al insertar en Tabla Test");
            System.err.println(se);
        }
    }
    
}
