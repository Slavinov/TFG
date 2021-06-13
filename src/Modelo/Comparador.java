package Modelo;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Lógica para comparar descriptores, devolver un arraylist ordenado de más parecidos a menos 
 * @author Stanislav
 */

//Toma como entrada un arraylist de imágenes, con la imágen de referencia en la última posición del array. Lo devuelve ordenado según la similaridad a esa imagen de referencia, eliminándola.
public class Comparador {
    public class Pair implements Comparable<Pair> {
        public final int index;
        public final float value;

        public Pair(int index, float value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public int compareTo(Pair other) {
            //multiplied to -1 as the author need descending sort order
            return Float.valueOf(this.value).compareTo(other.value);
        }
    }
    
    public ArrayList<Imagen> compararCoocurrencia(ArrayList<Imagen> entrada){
        ArrayList<Imagen> resultado = new ArrayList<Imagen>();
        DescriptorCoocurrencia temp;
        
        //Lógica de comparación - se calcula la distancia euclídea entre vectores de 5 dimensiones dados por los 5 descriptores de coocurrencia
        
        /*
            Se usan arrays de floats para representar vectores. Cada posición del array es:
            [0] = Energía
            [1] = Inercia
            [2] = Correlacion
            [3] = IDM 
            [4] = Entropía
        */
        float referencia[] = new float[5];//Descriptor de referencia
        for(int i = 0; i < entrada.get(entrada.size()-1).getDescriptores().size(); i++){
            if(entrada.get(entrada.size()-1).getDescriptores().get(i) instanceof DescriptorCoocurrencia){
                referencia[0] = ((DescriptorCoocurrencia)entrada.get(entrada.size()-1).getDescriptores().get(i)).getEnergy()[4];
                referencia[1] = ((DescriptorCoocurrencia)entrada.get(entrada.size()-1).getDescriptores().get(i)).getInertia()[4];
                referencia[2] = ((DescriptorCoocurrencia)entrada.get(entrada.size()-1).getDescriptores().get(i)).getCorrelation()[4];
                referencia[3] = ((DescriptorCoocurrencia)entrada.get(entrada.size()-1).getDescriptores().get(i)).getIDM()[4];
                referencia[4] = ((DescriptorCoocurrencia)entrada.get(entrada.size()-1).getDescriptores().get(i)).getEntropy()[4];
            }
        }
        
        //Ahora se extrae el resto de desctiptores en vectores
        float resultados[] = new float[entrada.size()-1]; //Aquí se guardarán todas las distancias calculadas
        float vectorActual[] = new float[5]; //Vector para almacenar los descriptores de la imagen actual que se compara con la referencia. Se reutiliza con todas las imagenes
                
        for(int i = 0; i < entrada.size()-1; i++){
            for(int j=0; j<entrada.get(i).getDescriptores().size(); j++){
                if(entrada.get(i).getDescriptores().get(j) instanceof DescriptorCoocurrencia){
                    vectorActual[0] = ((DescriptorCoocurrencia)entrada.get(i).getDescriptores().get(j)).getEnergy()[4];
                    vectorActual[1] = ((DescriptorCoocurrencia)entrada.get(i).getDescriptores().get(j)).getInertia()[4];
                    vectorActual[2] = ((DescriptorCoocurrencia)entrada.get(i).getDescriptores().get(j)).getCorrelation()[4];
                    vectorActual[3] = ((DescriptorCoocurrencia)entrada.get(i).getDescriptores().get(j)).getIDM()[4];
                    vectorActual[4] = ((DescriptorCoocurrencia)entrada.get(i).getDescriptores().get(j)).getEntropy()[4];
                    
                    //Resultado parcial
                    resultados[i] = ndistance(vectorActual, referencia);
                    
                }
            }
        }
        
        //Aquí tenemos todas las distancias, únicamente falta ordenar las imágenes según las distancias
        Pair[] comparativa = new Pair[resultados.length]; //Pares valor/índice original
        for(int i = 0; i<resultados.length; i++){
            comparativa[i] = new Pair(i,resultados[i]);
        }
        Arrays.sort(comparativa); //Se realiza la comparación
        
        
        //Se puebla el resultado ordenado
        for(int i = 0; i< entrada.size()-1; i++){
            //Muestra los pares para comprobar si funciona bien:
            System.out.println("Par índice/valor: " + comparativa[i].index +"/"+comparativa[i].value);
            
            resultado.add(entrada.get(comparativa[i].index));
        }
        
        return resultado;
    }
    
    //funcion para calcular distancia euclídea entre dos vectores
    public static float ndistance(float[] a, float[] b) {
        float total = 0, diff;
        for (int i = 0; i < a.length; i++) {
            diff = b[i] - a[i];
            total += diff * diff;
        }
        return (float) Math.abs(Math.sqrt(total));
    }
}
