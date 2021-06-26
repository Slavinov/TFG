package Modelo;

import DataAccess.FachadaDAO;
import ValueObjects.ConfigVO;
import ValueObjects.WorkspaceVO;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Stanislav
 */
public class FachadaModelo {
    private ArrayList<Workspace> workspaces;
    private String path; //Path por defecto de los workspaces, establecido o no por el usuario
    private FachadaDAO baseDatos;
    private int distancia = 1;

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }
    
    public FachadaModelo(){
        workspaces = new ArrayList<Workspace>();
        baseDatos = FachadaDAO.getFachada();
    }

    public FachadaDAO getBaseDatos() {
        return baseDatos;
    }

    public void setBaseDatos(FachadaDAO baseDatos) {
        this.baseDatos = baseDatos;
    }
    
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    } 

    public ArrayList<Workspace> getWorkspaces() {
        return workspaces;
    }

    public void setWorkspaces(ArrayList<Workspace> workspaces) {
        this.workspaces = workspaces;
    }
    
    /////////////////////MÉTODOS DEL MODELO/////////////////////////
    //Métodos generales
    public void init(){
        //inicialización de DAOs
        baseDatos.initConfig();
        ConfigVO config = baseDatos.getConfig();
        this.path = config.getDefaultPath();
        this.distancia = config.getDistancia();
        baseDatos.initWorkspace();
        //Extraer config y Workspaces, meter los workspaces en el arraylist y establecer los atributos de config. En caso de fallo? dejar en blanco todo -> AÑADIR UN ACTIVITY INDICATOR O ALGO
        ArrayList<WorkspaceVO> wk = baseDatos.recuperarWorkspaces();     
        
        for(int i=0; i<wk.size();i++){
            //Antes de nada obtener la referencia a la carpeta obtenida, ya que sin ella no va nada, si no se puede conseguir se salta el workspace y ya
            File carpeta = new File(wk.get(i).getPath());
            if(carpeta != null && carpeta.isDirectory()){
                System.out.println("Detectado workspace: " + carpeta.getName());
                System.out.println("Nombre del workspace?: " + wk.get(i).getNombre());
                Workspace nuevo = new Workspace(wk.get(i).getNombre(),wk.get(i).getPath());
                nuevo.setDistanciaCoocurrencia(distancia);
                nuevo.setCarpeta(carpeta);
                this.workspaces.add(nuevo);
                
                workspaces.get(i).detectarImagenes();
            }
        }
        //Detección de cambios en el directorio (watching directory for changes) en todos los workspaces (meter un listener?).
    }
    
    public void close(){
        //Guardar la configuración
        
        //Guardar los Workspaces en la base de datos
        
        //Cerrar la conexion a la base de datos
        baseDatos.close();
    }
    
    //Métodos de gestión de Workspaces
    public Workspace crearWorkspace(String nombre){
        for(int i = 0; i< workspaces.size(); i++){
            if(workspaces.get(i).getNombre().equals(nombre)){
                return null;
            }
        }
        Workspace resultado = null;
        //Crear el workspace utilizando el constructor de ese
        WorkspaceVO work = new WorkspaceVO();
        if(this.path == null){
            resultado = new Workspace (nombre);
            this.workspaces.add(resultado);
            work.setNombre(nombre);
            work.setPath(resultado.getPath());
        }else{
            resultado = new Workspace(nombre,path+"\\"+nombre);
            this.workspaces.add(resultado);
            work.setNombre(nombre);
            work.setPath(this.path+"\\"+nombre);
        }
        //Guardar su referencia en la BD
        this.baseDatos.insertarWorkspace(work);
        //En caso de añadir imágenes, tratarlas aquí (extraer info, descriptores...)
        return resultado;
    }
    
    //Elimina recursivamente los contenidos del directorio
    private boolean deleteDirectory(File path) {
        if( path.exists() ) {
          File[] files = path.listFiles();
          for(int i=0; i<files.length; i++) {
             if(files[i].isDirectory()) {
               deleteDirectory(files[i]);
             }
             else {
               files[i].delete();
             }
          }
        }
        return( path.delete() );
    }
    public void borrarWorkspace(String nombre){
        //Eliminar todo el contenido del workspace
        //Eliminar su referencia de la BD
        boolean estado = false;
        this.baseDatos.borrarWorkspace(nombre);
        for(int i = 0; i< workspaces.size(); i++){
            if(workspaces.get(i).getNombre().equals(nombre)){
                File f = workspaces.get(i).getCarpeta();
                if(f != null){ 
                    System.out.println("Borrando todo en la carpeta");
                    estado = deleteDirectory(f);                  
                }
                workspaces.remove(i);
                break;
            }
        }
        if(estado == true){
            System.out.println("Todo borrado");
        }
        
    }
    
    public Workspace abrirWorkspace(String nombre, String path){
        //Escanear el contenido de la carpeta, tratando las imágenes encontradas, añadiendo su referencia a persistencia, añadir listener a la carpeta? -> Solo a la seleccionada?
        
        //Añadir referencias a la BD
        Workspace resultado = null;
        
        for(int i = 0; i< workspaces.size(); i++){
            if(workspaces.get(i).getNombre().equals(nombre)){
                return null;
            }
        }
        resultado = new Workspace(nombre, path);
        workspaces.add(resultado);
        WorkspaceVO temp = new WorkspaceVO();
        temp.setNombre(nombre);
        temp.setPath(path);
        this.baseDatos.insertarWorkspace(temp);
        
        return resultado;
    }
    
    public void cerrarWorkspace(String nombre){
        //Borrar el Workspace de la base de datos y eliminarlo del ArrayList
        this.baseDatos.borrarWorkspace(nombre);
        for(int i = 0; i< workspaces.size(); i++){
            if(workspaces.get(i).getNombre().equals(nombre)){
                workspaces.remove(i);
                break;
            }
        }
    }
    
    //Compreuba si hay un workspace con ese nombre
    public boolean existeWorkspace(String nombre){
        boolean existe = false;
        
        for(int i = 0; i< workspaces.size(); i++){
            if(workspaces.get(i).getNombre().equals(nombre)){
                existe = true;
            }
        }
        
        return existe;
    }
    
    //Añade una imagen a un workspace dado
    //public void anhadirImagen(String nombre, File imagen){
    //    
    //}
    
    //Métodos de guardado de estado
    public void guardarConfig(){
        //Guardar la configuración en la BD
        ConfigVO c = new ConfigVO();
        c.setDefaultPath(path);
        c.setDistancia(distancia);
        this.baseDatos.setConfig(c);   
    }
    
    public void guardarWorkspace(){
        //Guardar el workspace seleccionado en la BD
    }
    
    public Workspace obtenerWorkspace(String nombre){
        Workspace resultado = null;
        System.out.println("Buscando Workspace con el nombre: " + nombre);
        for(int i = 0; i< workspaces.size(); i++){
            System.out.println("Nombre del workspace actualmente buscado: " + workspaces.get(i).getNombre());
            if(workspaces.get(i).getNombre().equals(nombre)){
                resultado = workspaces.get(i);
                System.out.println("Encontrado Workspace con ese nombre!");
            }
        }
        
        return resultado;
    }
    
    public void modificarDistancia(int d){
        distancia = d;
        for(int i = 0; i < workspaces.size(); i++){
            workspaces.get(i).setDistanciaCoocurrencia(distancia);
        }
        this.guardarConfig();
        System.out.println("Distancia modificada! "+d);
    }
}
