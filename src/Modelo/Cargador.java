package Modelo;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Clase auxiliar con la lógica para realizar la carga de imágenes 
 * @author Stanislav
 */
public class Cargador {
    //Hacer algún tipo de estructura o patrón de diseño, acomodando la carga de todos los tipos de archivos de entrada posibles (jpg, png, bnp....)
    
    public Imagen procesarImagen(File entrada){
        Imagen resultado = null;
        
        
        //Conversión a escala de grises
        int width;
        int height;
        BufferedImage img=null;
        try {
            img = ImageIO.read(entrada);
        } catch (IOException ex) {
            Logger.getLogger(Cargador.class.getName()).log(Level.SEVERE, null, ex);
        }
        width = img.getWidth();
        height = img.getHeight();
        int pix[][] = new int[height][width];
        int pix2[][] = new int[height][width];
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                int rgb = img.getRGB(j, i);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = (rgb & 0xFF);
                int gray = (r + g + b) / 3;
                //System.out.println(gray);
                pix[i][j] = gray;
                pix2[i][j] = 255;
            }
        }
        
        return resultado;
    }
}
