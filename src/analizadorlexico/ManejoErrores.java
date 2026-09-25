
package analizadorlexico;

import java.util.ArrayList;
import java.util.List;


public class ManejoErrores {
    private List<String> errores = new ArrayList<>();
    private int contadorDeErrores = 100;

    public void addError(int linea, String mensaje) {
        
        String formatted = "Error " + contadorDeErrores +
                ". Línea " + String.format("%04d", linea) +
                ". " + mensaje;
        errores.add(formatted);
        contadorDeErrores++;
    }

    public List<String> getErrors() {
        return errores;
    }
}
