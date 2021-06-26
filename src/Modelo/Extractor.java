package Modelo;

import static Modelo.InterfazCVIPTools.COLOR_FORMAT.GRAY_SCALE;
import static Modelo.InterfazCVIPTools.CVIP_TYPE.CVIP_INTEGER;
import static Modelo.InterfazCVIPTools.FORMAT.REAL;
import static Modelo.InterfazCVIPTools.IMAGE_FORMAT.JPG;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase puente con los métodos principales de extracción de descriptores a partir de una imagen, añadir herencia quizas¿?
 * @author Stanislav
 */
public class Extractor {
    public DescriptorCoocurrencia devolverCoocurrencia(Imagen i, int distancia){
        DescriptorCoocurrencia resultado = new DescriptorCoocurrencia();
        resultado.setDistancia(distancia);
        InterfazCVIPTools lib = InterfazCVIPTools.INSTANCE;
        
        resultado.setNombreImagen(i.getNombre());
        
        //Se realiza la extracción entera utilizando la interfaz a CVIPTools más los datos de la imagen i
        /////CVIPTOOLS/////
        //Estructura matriz principal
        InterfazCVIPTools.MATRIX m1 = new InterfazCVIPTools.MATRIX();
        m1.data_type = CVIP_INTEGER;
        m1.data_format = REAL;
        m1.rows = i.getAltura();
        m1.cols = i.getAnchura();
        m1.setRptr(i.getValores());
        m1.iptr = null;
        lib.test(m1);
        //Estructura imagen principal
        InterfazCVIPTools.IMAGE i1 = new InterfazCVIPTools.IMAGE();
        i1.image_format = JPG; //La funciónd e texture2 no comprueba el tipo de imagen así que se utiliza jpg como placeholder para todo
        i1.color_space = GRAY_SCALE;
        i1.bands = 1;
        i1.setMatrix(m1);
        
        //Estructura matriz máscara
        InterfazCVIPTools.MATRIX m2 = new InterfazCVIPTools.MATRIX();
        m2.data_type = CVIP_INTEGER;
        m2.data_format = REAL;
        m2.rows = i.getAltura();
        m2.cols = i.getAnchura();
        m2.setRptr(i.getValoresBlancos());
        m2.iptr = null;
        lib.test(m2);
        //Estructura imagen máscara
        InterfazCVIPTools.IMAGE i2 = new InterfazCVIPTools.IMAGE();
        i2.image_format = JPG;
        i2.color_space = GRAY_SCALE;
        i2.bands = 1;
        i2.setMatrix(m2);
        
        //Estructura resultante que devuelve CVIPTools con los valores concretos de la textura
        InterfazCVIPTools.TEXTURE2 res = new InterfazCVIPTools.TEXTURE2();
        res = lib.texture2(i1, i2, 0, i.getAltura(), i.getAnchura(), distancia, 1, 1, 1, 1, 1, 1);
        
        //Se establecen los valores obtenidos en el resutlado:
        resultado.setCorrelation(res.correlation);
        resultado.setEnergy(res.energy);
        resultado.setEntropy(res.entropy);
        resultado.setIDM(res.IDM);
        resultado.setInertia(res.inertia);
        
        return resultado;
    }
    
    //Otros descriptores
    
    //Guardado de descriptores en archivos de texto:
    
    //Coocurrencia
    public void guardarDescriptorCoocurrencia(DescriptorCoocurrencia entrada){
        File file = new File(entrada.getCarpetaDescriptor().getAbsolutePath()+"\\"+entrada.getNombreImagen()+".txt");             
        FileWriter f;
        
        try{
            f = new FileWriter(file,false);
            //Se escriben los contenidos
            f.write("--Distancia--\n");
            f.write(Integer.toString(entrada.getDistancia())+"\n");
            f.write("--Energia (0º,45º,90º,135º,media,rango(max-min))--\n");
            for(int i=0; i<6; i++){
              f.write(Float.toString(entrada.getEnergy()[i])+"\n");
            }
            f.write("--Inercia (0º,45º,90º,135º,media,rango(max-min))--\n");
            for(int i=0; i<6; i++){
                f.write(Float.toString(entrada.getInertia()[i])+"\n");
            }
            f.write("--Correlacion (0º,45º,90º,135º,media,rango(max-min))--\n");
            for(int i=0; i<6; i++){
                f.write(Float.toString(entrada.getCorrelation()[i])+"\n");
            }
            f.write("--IDM (0º,45º,90º,135º,media,rango(max-min))--\n");
            for(int i=0; i<6; i++){
                f.write(Float.toString(entrada.getIDM()[i])+"\n");
            }
            f.write("--Entropia (0º,45º,90º,135º,media,rango(max-min))--\n");
            for(int i=0; i<6; i++){
                if(i==5){
                    f.write(Float.toString(entrada.getEntropy()[i]));
                    break;
                }
                f.write(Float.toString(entrada.getEntropy()[i])+"\n");
            }
            f.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    
    //Leer descriptores
    public void detectarDescriptores(Imagen entrada, File carpeta){
        System.out.println("Detectando descriptores...");
        boolean error = false;
        String path = carpeta.getAbsolutePath()+"\\"+"Coocurrencia";
        Path path2 = Paths.get(path);
        //Coocurrencia----
        if(Files.exists(path2)){ //Primero comprobar si existe la carpeta, si no existe pues no hay descriptores
            //Lo siguiente es comprobar si existe el archivo
            File f = new File(path+"\\"+entrada.getNombre()+".txt");
            if(f.exists()){
                DescriptorCoocurrencia resultado = new DescriptorCoocurrencia();
                resultado.setNombreImagen(entrada.getNombre());
                resultado.setCarpetaDescriptor(new File(carpeta.getAbsolutePath()+"\\"+"Coocurrencia"));
                //Se extrae el descriptor del archivo de testo y se carga en el objeto de imagen
                try (BufferedReader br = new BufferedReader(new FileReader(f))) {
                    String line;
                    if((line = br.readLine())!=null){
                        if((line = br.readLine())!=null){
                            resultado.setDistancia(Integer.parseInt(line));
                        }else{error=true;}
                    }else{error=true;}
                    //Energía
                    br.readLine();
                    float[] energia = new float[6];
                    for(int i = 0; i < 6; i++){
                        if((line = br.readLine()) != null){
                            energia[i] = Float.parseFloat(line);
                        }else{error=true;}
                    }
                    //Inercia
                    br.readLine();
                    float[] inercia = new float[6];
                    for(int i = 0; i < 6; i++){
                        if((line = br.readLine()) != null){
                            inercia[i] = Float.parseFloat(line);
                        }else{error=true;}
                    }
                    br.readLine();
                    //Correlacion
                    float[] correlacion = new float[6];
                    for(int i = 0; i < 6; i++){
                        if((line = br.readLine()) != null){
                            correlacion[i] = Float.parseFloat(line);
                        }else{error=true;}
                    }
                    //IDM
                    br.readLine();
                    float[] idm = new float[6];
                    for(int i = 0; i < 6; i++){
                        if((line = br.readLine()) != null){
                            idm[i] = Float.parseFloat(line);
                        }else{error=true;}
                    }
                    //Entropía
                    br.readLine();
                    float[] entropia = new float[6];
                    for(int i = 0; i < 6; i++){
                        if((line = br.readLine()) != null){
                            entropia[i] = Float.parseFloat(line);
                        }else{error=true;}
                    }
                    br.close();
                    
                    //Se guardan los resultados en el archivo
                    resultado.setEnergy(energia);
                    resultado.setInertia(inercia);
                    resultado.setCorrelation(correlacion);
                    resultado.setIDM(idm);
                    resultado.setEntropy(entropia);
                } catch (Exception ex) {
                    Logger.getLogger(Extractor.class.getName()).log(Level.SEVERE, null, ex);
                    error = true;
                }
                
                if(error == false){
                    System.out.println("Añadiendo descriptor de coocurrencia...");
                    //Se añade el descriptor a la imagen
                    entrada.getDescriptores().add(resultado);
                }
                error=false;
            }
        }
        
        //Otros descriptores...
    }
}
