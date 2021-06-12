package Modelo;

import java.io.File;

/**
 *
 * @author Stanislav
 */

//Aplicar herencia + polimorfismo para crear diferentes descriptores?
public abstract class Descriptor {
    //Desarrollar esto, hacer una estructura con herencia para permitir diferentes tipos de descriptores??
    private String nombreImagen; //Nobre de la imagen a la que pertenece;
    private File carpetaDescriptor; //Guarda la referencia a la carpeta del descriptor para facilitar escrituras
    
    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public File getCarpetaDescriptor() {
        return carpetaDescriptor;
    }

    public void setCarpetaDescriptor(File carpetaDescriptor) {
        this.carpetaDescriptor = carpetaDescriptor;
    }
    
    
    
}
