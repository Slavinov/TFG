package Modelo;

import java.io.File;
import javafx.scene.image.Image;

/**
 * Interfaz de la carga de imágenes, por si tiene que cambiar para otras implementaciones de extracción de descriptores en el futuro.
 * @author Stanislav
 */
public interface ICargador {
    //Devuelve un objeto Imagen a partir de un archivo.
    public Imagen procesarImagen(File entrada);
    
    //Crea la miniatura de la imagen pasada para usarla en la interfaz gráfica
    public Image generarMiniatura(File entrada);
    
}
