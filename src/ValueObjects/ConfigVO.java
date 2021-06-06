package ValueObjects;

/**
 * Objeto de encapsulación para los datos de configuración persistentes generados por el usuario.
 * @author Stanislav
 */

public class ConfigVO {
    private String defaultPath;
    
    public ConfigVO(String p){
        defaultPath = p;
    }

    public String getDefaultPath() {
        return defaultPath;
    }

    public void setDefaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
    }
       
}
