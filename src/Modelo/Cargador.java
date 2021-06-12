package Modelo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 * Clase auxiliar con la lógica para realizar la carga de imágenes 
 * @author Stanislav
 */
public class Cargador {
    //Hacer algún tipo de estructura o patrón de diseño, acomodando la carga de todos los tipos de archivos de entrada posibles (jpg, png, bnp....)
     
    public Imagen procesarImagen(File entrada){ //Diferenciar entre escala de gris y no escala de gris?
        Imagen resultado = null;
        resultado = new Imagen(entrada.getName());
       
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
        
        //Asignación de valores
        resultado.setAltura(height);
        resultado.setAnchura(width);
        resultado.setValores(pix);
        resultado.setValoresBlancos(pix2);
        return resultado;
    }
    
    //Función para generar miniaturas 50x50 a partir de las imagenes
    public Image generarMiniatura(File entrada){
        BufferedImage resultado = null;
        
        try {
            java.awt.Image temp = ImageIO.read(entrada).getScaledInstance(50, 50, BufferedImage.SCALE_SMOOTH);
            resultado = this.toBufferedImage(temp);
        } catch (IOException ex) {
            Logger.getLogger(Cargador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Image resultado2 = SwingFXUtils.toFXImage(resultado, null);
        
        return resultado2;
    }
    
    
    private BufferedImage toBufferedImage(java.awt.Image img)
    {
    if (img instanceof BufferedImage)
    {
        return (BufferedImage) img;
    }

    // Create a buffered image with transparency
    BufferedImage bimage;
        bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

    // Draw the image on to the buffered image
    Graphics2D bGr = bimage.createGraphics();
    bGr.drawImage(img, 0, 0, null);
    bGr.dispose();

    // Return the buffered image
    return bimage;
}
}
