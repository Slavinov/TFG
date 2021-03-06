package Modelo;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
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
public class Cargador implements ICargador{
    //Hacer algún tipo de estructura o patrón de diseño, acomodando la carga de todos los tipos de archivos de entrada posibles (jpg, png, bnp....)
     
    @Override
    public Imagen procesarImagen(File entrada){ //Diferenciar entre escala de gris y no escala de gris?
        Imagen resultado = null;
        resultado = new Imagen(entrada.getName());
       
        //Conversión a escala de grises
        int width;
        int height;
        BufferedImage img=null;
        ColorModel color=null;
        try {
            img = ImageIO.read(entrada);
            color = img.getColorModel();
            
        } catch (IOException ex) {
            System.out.println("Error de formato de la imagen");
            return null;
        }
        
        width = img.getWidth();
        height = img.getHeight();
        int pix[][] = new int[height][width];
        int pix2[][] = new int[height][width];
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                if((color.getPixelSize() == 24) || (color.getPixelSize() == 32)){ //RGB o RGBA
                    int rgb = img.getRGB(j, i);
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = (rgb & 0xFF);
                    //PREPROCESAMIENTO Y CONVERSIÓN A ESCALA DE GRISES 
                    // Normalización y corrección de gamma:
                    float rr = (float) Math.pow(r / 255.0, 2.2);
                    float gg = (float) Math.pow(g / 255.0, 2.2);
                    float bb = (float) Math.pow(b / 255.0, 2.2);

                    // Luma (estándar ITU-R BT.709):
                    float lum =  (float) (0.2126 * rr + 0.7152 * gg + 0.0722 * bb);

                    // Gamma: 
                    int grayLevel = (int) (255.0 * Math.pow(lum, 1.0 / 2.2));
                    
                    pix[i][j] = grayLevel;
                }else if(color.getPixelSize() == 8){ //Escala de grises
                    int gray = img.getRGB(j, i)& 0xFF;
                    pix[i][j] = gray;
                    
                }else{//Profundidad de pixel no soportada
                    return null;
                }
                //Color de máscara por defecto: todo en blanco
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
    
    //Función para generar miniaturas 100x100 a partir de las imagenes
    @Override
    public Image generarMiniatura(File entrada){
        BufferedImage resultado = null;
        
        try {
            java.awt.Image temp = ImageIO.read(entrada).getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH);
            resultado = this.toBufferedImage(temp);
        } catch (IOException ex) {
            Logger.getLogger(Cargador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Image resultado2 = SwingFXUtils.toFXImage(resultado, null);
        
        return resultado2;
    }
    
    //Función de apoyo para cambiar entre formatos de la miniatura para hacela compatible con JavaFX.
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
