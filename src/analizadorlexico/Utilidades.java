
package analizadorlexico;


public class Utilidades {
    
    public static boolean esIdentificador(String s) {
        return s.matches("^[A-Za-z][A-Za-z0-9_]*$");
    }

    public static boolean esNumero(String s) {
        return s.matches("\\d+");
    }
    
    public static boolean esStringLiteral(String s) {
        return s.matches("^\".*\"$");
    }

    public static String cuatroDigitos(int n) {
        return String.format("%04d", n);
    }
    
    
}
