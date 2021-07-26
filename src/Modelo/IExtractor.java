package Modelo;

import java.io.File;

/**
 * Interfaz para la extracción de descriptores. La implementación variará según el tipo de descriptor usado.
 * @author Stanislav
 */
public interface IExtractor {
    //Devuelve los descriptores como objeto Descriptor. (La distancia es parte de la coocurrencia por lo que habría que pensar como eliminarla de ahí)
    public Descriptor devolver(Imagen i, int distancia);
    
    //Función para guardar descriptores en archivos de texto, el lugar y el formato dependen del tipo de descriptor.
    public void guardarDescriptor(Descriptor e);
    
    //Función para cargar los descriptores de una imagen a partir de los archivos de texto creados en la función guardarDescriptor.
    public void detectarDescriptores(Imagen entrada, File carpeta);
}
