package Modelo;

import java.util.ArrayList;

/**
 *
 * @author Stanislav
 */
public class Imagen {
    private String nombre;
    private String formato;
    private ArrayList<Descriptor> descriptores;
    
    public Imagen(String n){
        descriptores = new ArrayList<Descriptor>();
    }
    
    //Almacenar todo (formato,tipo,rows,cols,data(valores de pixeles))? probablemente. Desde Cargador -> extraer valores de pixeles, utilizando diferentes métodos dependiendo del formato quizás 
    
}