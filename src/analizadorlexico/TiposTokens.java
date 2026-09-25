package analizadorlexico;

// Diccionario o lista con las diferentes palabras sensibles que va a detectar el analizador
// Y categorizar las palabras encontradas

public enum TiposTokens {
    PalabraReservada,
    Identificador,
    Numero,
    String,
    Operador,
    Comentario,
    Desconocido
}