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
public class Cargador {
    //Hacer algún tipo de estructura o patrón de diseño, acomodando la carga de todos los tipos de archivos de entrada posibles (jpg, png, bnp....)
     
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
                    //PREPROCESAMIENTO
                    //int gray = (r + g + b) / 3; 
                    // Normalización y corrección de gamma:
                    float rr = (float) Math.pow(r / 255.0, 2.2);
                    float gg = (float) Math.pow(g / 255.0, 2.2);
                    float bb = (float) Math.pow(b / 255.0, 2.2);

                    // Cálculo de luminancia:
                    float lum =  (float) (0.2126 * rr + 0.7152 * gg + 0.0722 * bb);
                    //int gray = (int) (0.2126 * r + 0.7152 * g + 0.0722 * b);
                    //int gray = (int) (0.3 * r + 0.59 * g + 0.11 * b);
                    // Gamma compand and rescale to byte range:
                    int grayLevel = (int) (255.0 * Math.pow(lum, 1.0 / 2.2));
                    //int gray = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                    //if(entrada.getName().equals("naranja3.jpg")){
                    //System.out.println("Gray: " +gray+" GrayLevel: " +grayLevel +" Gay :" + gay);
                    //}
                    //int gay = (grayLevel << 16) + (grayLevel << 8) + grayLevel;
                    //if(entrada.getName().equals("Ca.PNG")){
                      //  System.out.println("jajajajajaja:" +gray);
                   // }
                   //if(entrada.getName().equals("igor-bogdanov.jpg")){
                       // System.out.println("res igor: " +gray);
                    //}
                    pix[i][j] = grayLevel;
                }else if(color.getPixelSize() == 8){ //Escala de grises
                    int gray = img.getRGB(j, i)& 0xFF;
                    pix[i][j] = gray;
                    
                    //if(entrada.getName().equals("igor.jpg")){
                        //System.out.println("res igor: " +gray);
                    //}
                    //if(entrada.getName().equals("test2Gir.jpg")){
                      //  System.out.println("Gir i="+i+" j="+j+" res: " +gray);
                   // }
                    //if(entrada.getName().equals("test2Reves.jpg")){
                      //  System.out.println("Reves i="+i+" j="+j+" res: " +gray);
                    //}
                }else{//Profundidad de pixel no soportada
                    return null;
                }
                //Color de máscara por defecto: todo en blanco
                pix2[i][j] = 255;
            }
        }
        if(entrada.getName().equals("naranja3.jpg")){
                        System.out.println("res igor-bogdan: " +pix[0][0]);
                        System.out.println("res igor-bogdan: " +pix[1][0]);
                        System.out.println("res igor-bogdan: " +pix[2][0]);
                        System.out.println("res igor-bogdan: " +pix[0][3]);
                        System.out.println("res igor-bogdan: " +pix[0][4]);
                        System.out.println("res igor-bogdan: " +pix[5][5]);
                        System.out.println("res igor-bogdan: " +pix[6][6]);
                    }
        if(entrada.getName().equals("naranja3gris.jpg")){
                        System.out.println("res igor: " +pix[0][0]);
                        System.out.println("res igor: " +pix[1][0]);
                        System.out.println("res igor: " +pix[2][0]);
                        System.out.println("res igor: " +pix[0][3]);
                        System.out.println("res igor: " +pix[0][4]);
                        System.out.println("res igor: " +pix[5][5]);
                        System.out.println("res igor: " +pix[6][6]);
                    }
        if(entrada.getName().equals("igor-bogdanov.jpg")){
            
            BufferedImage result = new BufferedImage(
            img.getWidth(),
            img.getHeight(),
            BufferedImage.TYPE_BYTE_GRAY);
            
            for(int i= 0; i<height;i++){
                for(int j=0; j<width;j++){
                    //Integer a = new Integer(pix[i][j]);
                    int gway = (pix[i][j] << 16) + (pix[i][j] << 8) + pix[i][j];
                    if(i==0 && j==0){
                        System.out.println("Pix color: "+pix[i][j]);
                        System.out.println("Gway color: "+gway);
                        System.out.println("Gg: "+img.getRGB(j, i));
                    }
                    if(i==0 && j==3){
                        System.out.println("Pix color: "+pix[i][j]);
                        System.out.println("Gway color: "+gway);
                        System.out.println("Gg: "+img.getRGB(j, i));
                    }
                    result.setRGB(j, i, gway);
                    
                }
            }
            
            File f = new File("igor.jpg");
            try {
                ImageIO.write(result, "jpg", f);
            } catch (IOException ex) {
                System.out.println("Error o algo");
                Logger.getLogger(Cargador.class.getName()).log(Level.SEVERE, null, ex);
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
            java.awt.Image temp = ImageIO.read(entrada).getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH);
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
