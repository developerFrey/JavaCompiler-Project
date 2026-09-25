
package analizadorlexico;

public class Token {
    
    private String lexema;
    private TiposTokens tipo;
    private int linea;

    public Token(String lexema, TiposTokens tipo, int linea) {
        
        this.lexema = lexema;
        this.tipo = tipo;
        this.linea = linea;
    }

    public String getLexema() { return lexema; }
    
    public TiposTokens getTipo() { return tipo; }
    
    public int getLinea() { return linea; }
    
}
