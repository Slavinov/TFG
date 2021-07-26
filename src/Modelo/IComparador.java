package Modelo;

import java.util.ArrayList;

/**
 * Interfaz para la implementación de comparación, que depederá del método de extracción utilizado. 
 * Ayuda a cambiar de implementación de comparación en el futuro.
 * @author Stanislav 
 */
public interface IComparador {
    public ArrayList<Imagen> comparar (ArrayList<Imagen> entrada);
}
