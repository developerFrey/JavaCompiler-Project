
package analizadorlexico;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Lexer {
    
    // Coleccion con las diferentes palabras reservadas
    public static final Set<String> PalabrasReservadas = new HashSet<>(
        Arrays.asList("module","sub","dim","as","if","then",
        "elseif","else","function","return","while","end")
    );

    public List<Token> analizar(List<String> lineas) {

        List<Token> tokens = new ArrayList<>();
        int numeroDeLinea = 1;

        for (String linea : lineas) {

            if (linea.trim().startsWith("'")) {
                tokens.add(new Token(linea, TiposTokens.Comentario, numeroDeLinea));
                numeroDeLinea++;
                continue;
            }

            String[] partes = linea.trim().split("\\s+");

            for (String p : partes) {

                if (p.isEmpty()) continue;

                if (PalabrasReservadas.contains(p.toLowerCase()))
                    tokens.add(new Token(p, TiposTokens.PalabraReservada, numeroDeLinea));

                else if (Utilidades.esIdentificador(p))
                    tokens.add(new Token(p, TiposTokens.Identificador, numeroDeLinea));

                else if (Utilidades.esNumero(p))
                    tokens.add(new Token(p, TiposTokens.Numero, numeroDeLinea));

                else
                    tokens.add(new Token(p, TiposTokens.Desconocido, numeroDeLinea));
            }

            numeroDeLinea++;
        }

        return tokens;
    }
}
