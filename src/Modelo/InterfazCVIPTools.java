package Modelo;

import com.sun.jna.Library;
import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import java.util.Arrays;
import java.util.List;

/**
 * Interfaz puente a la librería CVIPTools escrita en C. Expone los métodos utilizados para extraer descriptores y define los tipos de datos utilizados para enviar y recibir datos.
 * Se utilizan tipos de datos de Java para definir todo, luego jna se encargará de asignar memoria y ajustar los valores a los tipos nativos de C una vez que se ejecute el programa
 */
public interface InterfazCVIPTools extends Library{
    //Instancia de esta misma interfaz, que puede utilizarse para acceder a los métodos de C. Native.load busca el .dll (o .so en linux) indicado para utilizarlo junto a esta interfaz.
    InterfazCVIPTools INSTANCE = (InterfazCVIPTools)Native.load("CVIPTools", InterfazCVIPTools.class);
    
    //Enums utilizados por CVIPTools, son equivalentes a un int en C, por lo que se utiliza int en Java.
    
    public static interface CVIP_TYPE{
        public static final int CVIP_BYTE = 0;
        public static final int CVIP_SHORT = 1;
        public static final int CVIP_INTEGER = 2;
        public static final int CVIP_FLOAT = 3;
        public static final int CVIP_DOUBLE = 4;
    }
    
    public static interface FORMAT{
        public static final int REAL = 0;
        public static final int COMPLEX = 1;
    }
    
    //Hay demasiados formatos en CVIPTools, únicamente usaremos los soportados por este programa que son los más comunes (TIF,BMP,JPG,PNG).
    public static interface IMAGE_FORMAT{
        public static final int TIF = 4;       
        public static final int JPG = 24;
        public static final int BMP = 31;
        public static final int PNG = 33;
    }
    
    //El formato pasado a las funciones de extracción siempre será el de escala de gris para las funciones de textura. Se deja el resto para futuras expansiones.
    public static interface COLOR_FORMAT{
        public static final int BINARY = 0;
        public static final int GRAY_SCALE = 1;
        public static final int RGB = 2;
        public static final int HSL = 3;
        public static final int HSV = 4;
        public static final int SCT = 5;
        public static final int CCT = 6;
        public static final int LUV = 7;
        public static final int LAB = 8;
        public static final int XYZ = 9;
    }
    
    
    //Estructuras de datos utilizadas por CVIPTools. Son estructuras clásicas de C, que gracias a jna podemos representar aquí de forma parecida. Luego jna se encarga de ajustar la memoria al pasarlo a C.
    
    //Estructura del historial de cambios. Está como placeholder ya que no necesitamos utilizarla en este programa.
    public class HISTORY extends Structure{
        public int temp;
        public int temp2;
        public int temp3;
        public HISTORY(){
            super();
        }
         @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("ftag", "packetP", "next");
        }
    }
    
    //Estructura para la matriz de datos principal de la imagen (contiene básicamente la información de cada píxel de la imagen)
    public class MATRIX extends Structure{
        public int data_type;
        public int data_format;
        public int rows;
        public int cols;
        public Pointer rptr; //Puntero a la matriz en sí, que no es más que un **void en C
        public Integer iptr;
        
        public MATRIX(){
            super();                   
        }
        
        public MATRIX(MATRIX m){
            super();
            data_type = m.data_type;
            data_format = m.data_format;
            rows = m.rows;
            cols = m.cols;
            rptr = m.rptr;
            iptr = null;
            read();
        }
           
        public MATRIX(Pointer p){
            super(p);
            read();
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("data_type", "data_format", "rows", "cols", "rptr", "iptr");
        }
        public static class ByValue extends MATRIX implements Structure.ByValue { }
        public static class ByReference extends MATRIX implements Structure.ByReference { 
            public ByReference() { }
            public ByReference(Pointer p) { 
                super(p); 
                read(); 
            }
            public ByReference(MATRIX p) { 
                super(p); 
                read(); 
            }
        }
               
        //Método para reservar memoria y guardar en el heap la matriz de valores reales. (El método de asignación utilizado aquí es el de una matriz contigua de C)
        public void setRptr(int[][] datos){
            rptr = new Memory(datos.length*8); //Equivalente a un malloc normal y corriente 
            //Un poco de aritmética de punteros, no me acuerdo como lo hice exactamente pero funciona
            Pointer principal = new Memory(datos.length*datos[0].length*4);
            for (int i = 0; i < datos.length; i++){
                //rptr[i] = new Memory(datos[0].length * Integer.SIZE);
                Pointer secundario = principal.share(i*datos[0].length*4);
                for(int j=0; j< datos[0].length; j++){
                    secundario.setInt(j*4, datos[i][j]);
                    //System.out.println("Datos: " + datos[i][j] + " Puntero: " + rptr[i].getInt(j*Integer.SIZE));                
                }
                rptr.setPointer(i*8,secundario); //creación de offset              
            }         
        }       
    }
    
    //Estructura para la imagen entera. Contiene las 2 estructuras definidas arriba de HISTORY Y MATRIX
    public class IMAGE extends Structure{
        public int image_format;
        public int color_space;
        public int bands;
        public Pointer image_ptr;
        //public Integer story; //Puntero a la estructura de History (innecesario??)
        
        public IMAGE(){
            super();
        }
        
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("image_format", "color_space", "bands", "image_ptr");
        }
        
        public void setMatrix(MATRIX m){
            image_ptr = new Memory(8); //Para 1 puntero siempre, ya que trabajamos con 1 banda
            Pointer principal = m.getPointer();
            image_ptr.setPointer(0, principal);
        }
        
    }
    
    //Estructura que devuelve la función de texture2. Contiene los descriptores de textura extraídos mediante el uso de matrices de correlación.
    public class TEXTURE2 extends Structure{
        public float[] energy = new float[6];
        public float[] inertia = new float[6];
        public float[] correlation = new float[6];
        public float[] IDM = new float[6];
        public float[] entropy = new float[6];
        
        public TEXTURE2(){
            super();
        }
        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("energy", "inertia", "correlation", "IDM", "entropy");
        }
        public class ByValue extends TEXTURE2 implements Structure.ByValue { }
        public class ByReference extends TEXTURE2 implements Structure.ByReference { }
    }
    
        
    /////////////////////////////MÉTODOS DE EXTRACCIÖN//////////////////////////////////
    //Método de extracción de textura utilizando matrices de correlación:
    TEXTURE2 texture2(IMAGE inputImage, IMAGE labeledImage, int band, int row, int col, int distance, int energy, int inertia, int correlation, int invDiff, int entropy, int zerorowcol);
}   
