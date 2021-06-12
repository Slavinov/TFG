package Modelo;

import static Modelo.InterfazCVIPTools.COLOR_FORMAT.GRAY_SCALE;
import static Modelo.InterfazCVIPTools.CVIP_TYPE.CVIP_INTEGER;
import static Modelo.InterfazCVIPTools.FORMAT.REAL;
import static Modelo.InterfazCVIPTools.IMAGE_FORMAT.JPG;

/**
 * Clase puente con los métodos principales de extracción de descriptores a partir de una imagen, añadir herencia quizas¿?
 * @author Stanislav
 */
public class Extractor {
    public DescriptorCoocurrencia devolverCoocurrencia(Imagen i){
        DescriptorCoocurrencia resultado = new DescriptorCoocurrencia();
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
        System.out.println(lib.test(m1));
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
        System.out.println(lib.test(m2));
        //Estructura imagen máscara
        InterfazCVIPTools.IMAGE i2 = new InterfazCVIPTools.IMAGE();
        i2.image_format = JPG;
        i2.color_space = GRAY_SCALE;
        i2.bands = 1;
        i2.setMatrix(m2);
        
        //Estructura resultante que devuelve CVIPTools con los valores concretos de la textura
        InterfazCVIPTools.TEXTURE2 res = new InterfazCVIPTools.TEXTURE2();
        res = lib.texture2(i1, i2, 0, i.getAltura(), i.getAnchura(), 1, 1, 1, 1, 1, 1, 1);
        
        //Se establecen los valores obtenidos en el resutlado:
        resultado.setCorrelation(res.correlation);
        resultado.setEnergy(res.energy);
        resultado.setEntropy(res.entropy);
        resultado.setIDM(res.IDM);
        resultado.setInertia(res.inertia);
        
        System.out.println("Descriptor obtenido con éxito!" + resultado.getEnergy()[4]); //Debe dar 0.32708114 para car.bmp
       
        return resultado;
    }
}
