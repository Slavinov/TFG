package Modelo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;

/**
 *
 * @author Stanislav
 */
public class Workspace {
    private String nombre; //Nombre único del workspace, dado por el usuario al crearlo
    private String path; //Path a la Carpeta en la que se encuentra el workspace, se establece al crearse el workspace 
    private ArrayList<Imagen> imagenes;
    private File carpeta; //Carpeta dodnde se encuentra el workspace
    private Image miniatura; //Miniatura que se muestra en la interfaz
    
    /*
        A continuación Clases con lógica necesaria para cargar imágenes, extraer descriptores y compararlos, por ahora solo descriptores de co-ocurrencia. 
        Para añadir más implementaciones sería adecuado crear las clases correspondientes que implementen ICargador, IExtractor y IComparador y hacer un método que cambie entre implementaciones o crearlas antes de usarlos.  
    */
    private ICargador cargador = new Cargador();  //En el futuro renombrar las tres clases (p. ej CargadorCoocurrencia...). 
    private IExtractor extrator = new Extractor();
    private IComparador comparador = new Comparador();
    
    //Atributos puramente para facilitar la representación gráfica de los mismos (resultado de la última comparativa y la imagen de referencia usada en esa misma comparación)
    private ArrayList<Imagen> resultadoComparacion; //resultado ordenado de la última comparativa, reemplazar el array normal con este en la representación
    private Imagen referencia; //Imagen de referencia para mostrarla en la interfaz
    
    //Gestión de errores:
    private ArrayList<String> imagenesErroneas;
    
    //Opciones de descriptores
    private int distanciaCoocurrencia = 1;
    
    public Workspace(String n){
        imagenes = new ArrayList<Imagen>();
        nombre = n;
        path = System.getProperty("user.home")+"\\Workspaces"; // path por defecto
        new File(path).mkdirs(); //Crea el directorio si no lo hay
        carpeta = new File(path+"\\"+nombre);
        carpeta.mkdirs(); //Crea el directorio si no lo hay
        path = path+"\\"+nombre;
    }
    
    public Workspace(String n, String p){
        imagenes = new ArrayList<Imagen>();
        nombre = n;
        path = p; 
        carpeta = new File(path);
        carpeta.mkdirs(); //Crea el directorio si no lo hay
        //new File(path+"\\"+nombre).mkdirs(); //Crea el directorio si no lo hay
        //path = path+"\\"+nombre;
    }

    public ArrayList<String> getImagenesErroneas() {
        return imagenesErroneas;
    }

    public void setImagenesErroneas(ArrayList<String> imagenesErroneas) {
        this.imagenesErroneas = imagenesErroneas;
    }  
    
    public int getDistanciaCoocurrencia() {
        return distanciaCoocurrencia;
    }

    public void setDistanciaCoocurrencia(int distanciaCoocurrencia) {
        this.distanciaCoocurrencia = distanciaCoocurrencia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ArrayList<Imagen> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList<Imagen> imagenes) {
        this.imagenes = imagenes;
    }

    public File getCarpeta() {
        return carpeta;
    }

    public void setCarpeta(File carpeta) {
        this.carpeta = carpeta;
    }

    public Image getMiniatura() {
        return miniatura;
    }

    public void setMiniatura(Image miniatura) {
        this.miniatura = miniatura;
    }

    public ICargador getCargador() {
        return cargador;
    }

    public void setCargador(Cargador cargador) {
        this.cargador = cargador;
    }

    public IExtractor getExtrator() {
        return extrator;
    }

    public void setExtrator(Extractor extrator) {
        this.extrator = extrator;
    }

    public ArrayList<Imagen> getResultadoComparacion() {
        return resultadoComparacion;
    }

    public void setResultadoComparacion(ArrayList<Imagen> resultadoComparacion) {
        this.resultadoComparacion = resultadoComparacion;
    }

    public IComparador getComparador() {
        return comparador;
    }

    public void setComparador(Comparador comparador) {
        this.comparador = comparador;
    }

    public Imagen getReferencia() {
        return referencia;
    }

    public void setReferencia(Imagen referencia) {
        this.referencia = referencia;
    }
    
    
    
    //MÉTODOS
    
    
    //Carga la imagen extrayendo los datos a la clase Imagen y saca la miniatura
    public void anhadirImagen(File imagen){
        try {
            //Comprobar que se trata efectivamente de una imagen 
            ImageIO.read(imagen).toString(); //Produce excepción si no es una imagen o no está soportado el formato de la misma
            
            try{
                Imagen objetivo = cargador.procesarImagen(imagen);
                miniatura = cargador.generarMiniatura(imagen);
                objetivo.setMiniatura(miniatura);
                System.out.println("Imagen añadida!");
                this.imagenes.add(objetivo);
                this.extrator.detectarDescriptores(objetivo,carpeta); //Detecta archivos de descriptores existentes y añade descriptores
                
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        
        } catch (Throwable ex) {
            if(this.imagenesErroneas == null){
                this.imagenesErroneas = new ArrayList<String>();
            }
            this.imagenesErroneas.add(imagen.getName());
            //Se elimina la imagen de la carpeta si existe
            File erroneo = new File(this.path+"\\"+imagen.getName());
            try {
                Files.deleteIfExists(erroneo.toPath());
            } catch (IOException ex1) {
                Logger.getLogger(Workspace.class.getName()).log(Level.SEVERE, null, ex1);
            }
            System.out.println("No es una imagen");
        }
    }
    
    
    public void detectarImagenes(){
        //Busca las imagenes en la carpeta, las procesa y añade a este workspace.
        System.out.println("Detectando imagenes...");
        //Se itera la carpeta del workspace ignorando las subcarpetas:
        for(File fileEntry : carpeta.listFiles()){
            if(!fileEntry.isDirectory()){
                anhadirImagen(fileEntry);                
            }
        }
    }
    
    
    //Obtiene el descriptor de coocurrencia de la imagen dada y lo guarda en un archivo de texto en la carpeta requerida
    //Fase 1, extraerlo sin más
    //Fase 2, guardarlos en un archivo de texto y luego recuperarlo si existe
    public void extraerDescriptorCoocurrencia(String nombre){
        //Se obtiene la imagen a través del nombre:
        Imagen objetivo = null;
        for(int i = 0; i<this.imagenes.size(); i++){
            if(this.imagenes.get(i).getNombre().equals(nombre)){
                objetivo = this.imagenes.get(i);
            }
        }
        
        if(objetivo != null){
            //Crea la carpeta de Coocurrencia si no existe:
            new File(carpeta.getAbsolutePath()+"\\"+"Coocurrencia").mkdir(); 
            //Obtiene la referencia
            File carpetaDescriptor = new File(carpeta.getAbsolutePath()+"\\"+"Coocurrencia");
            //Asociar internamente de alguna forma los descriptores a las imagenes
            DescriptorCoocurrencia descriptor = (DescriptorCoocurrencia) this.extrator.devolver(objetivo,this.distanciaCoocurrencia);
            descriptor.setCarpetaDescriptor(carpetaDescriptor);
            
            //En primer lugar se comprueba que no exista un descriptor de ese mimso tipo, si existe se reemplaza para evitar generar descriptores infinitos:
            for(int i = 0; i < objetivo.getDescriptores().size(); i++){
                if(objetivo.getDescriptores().get(i) instanceof DescriptorCoocurrencia){
                    objetivo.getDescriptores().remove(i);
                    break;
                }
            }
            objetivo.getDescriptores().add(descriptor); //Introduce el descriptor dentro del objeto imagen
            
            //Escribir el descriptor en un archivo de texto
            this.extrator.guardarDescriptor(descriptor);
        }
    }
    
    public int compararCoocurrencia(File referencia){
        //Comprueba que existen todos los descriptores, en caso contrario los crea, luego realiza la extracción de la imagen de referencia, y al final realiza la comparación
        boolean existe = false;
        this.referencia = null;
        for(int i=0; i<this.imagenes.size(); i++){
            for(int j=0; j<this.imagenes.get(i).getDescriptores().size(); j++){
                if(this.imagenes.get(i).getDescriptores().get(j) instanceof DescriptorCoocurrencia){
                    //comprobar que la distancia es correcta, si no lo es no poner true
                    if(((DescriptorCoocurrencia)this.imagenes.get(i).getDescriptores().get(j)).getDistancia() == this.distanciaCoocurrencia){
                        System.out.println("Ya existe descriptor x!");
                        existe = true;
                    }
                }
            }
            if(existe == false){
                //No existe descriptor, hay que crearlo
                this.extraerDescriptorCoocurrencia(this.imagenes.get(i).getNombre());
            }
            existe = false;
        }
        
        //En este punto están creados todos los descriptores bases, toca sacar el descriptor referencia
        //Se añade la imagen de referencia al final de la lista de imagenes para reutilizar la función de extracción normal, y se trabajará con ella desde esa posición.
        int tamAntiguo = this.imagenes.size(); //Para comprobar que efectivamente se inserta la imagen y no realizar la comparación en caso contrario
        this.anhadirImagen(referencia);
        if(this.imagenes.size() == (tamAntiguo+1)){
            this.referencia = this.imagenes.get(this.imagenes.size()-1);
            
            this.extraerDescriptorCoocurrencia(this.referencia.getNombre());
            
            this.resultadoComparacion = comparador.comparar(this.imagenes); //COMPARACIÓN EN SÍ
            this.imagenes.remove(this.imagenes.size()-1); //Se elimina la imagen de referenca de las imagenes para evitar que se use para comparar a otros
        } 
        
        System.out.println("Imagen más similar a la referencia: " + resultadoComparacion.get(0).getNombre());
        if(resultadoComparacion.size() == this.imagenes.size()){
            System.out.println("Se realizó la comparativa con éxito!");
            return 1;
        }else{
            return resultadoComparacion.size();
        }
        
    }
    
    //borra la imagen dada del workspace y del sistema de archivos
    public void borrarImagen(String nombre){
        Imagen  objetivo = null;
        for(int i=0; i<this.imagenes.size(); i++){
            if(this.imagenes.get(i).getNombre().equals(nombre)){
                objetivo = this.imagenes.get(i);
            }
        }
        
        if(objetivo != null){
            //Se eliminan los descriptores si los hay
            for(int i = 0; i < objetivo.getDescriptores().size(); i++){               
                File file = new File(objetivo.getDescriptores().get(i).getCarpetaDescriptor().getAbsolutePath()+"\\"+objetivo.getNombre()+".txt");   
                try {
                    Files.deleteIfExists(file.toPath());
                } catch (IOException ex) {
                    Logger.getLogger(Workspace.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            //Se elimina la propia imagen
            File file2 = new File(this.path+"\\"+objetivo.getNombre());
            try {
                    Files.deleteIfExists(file2.toPath());
                } catch (IOException ex) {
                    Logger.getLogger(Workspace.class.getName()).log(Level.SEVERE, null, ex);
                }
            //Se elimina del arraylist
            this.imagenes.remove(objetivo);
            
        }
    }
    
    
    
}
