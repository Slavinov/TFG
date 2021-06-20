package ValueObjects;

/**
 * Objeto de encapsulación para los datos de configuración persistentes generados por el usuario.
 * @author Stanislav
 */

public class ConfigVO {
    private String defaultPath;
    private int distancia;
    
    public ConfigVO(){
        
    }
    
    public ConfigVO(String p, int d){
        defaultPath = p;
        distancia = d;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    
    
    public String getDefaultPath() {
        return defaultPath;
    }

    public void setDefaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
    }
       
}
