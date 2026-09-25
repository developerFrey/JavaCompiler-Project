
package analizadorlexico;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Validador {
    
    //Funcion para validar los comentarios
    public void validarComentarios(List<String> lineas, ManejoErrores manejadorErrores) {

        int numeroDeLinea = 1;

        for (String line : lineas) {

            String lineaSinEspacios = line.trim();

            //Si la linea empieza con compillas es comentario valido
            if (lineaSinEspacios.startsWith("'")) {
                numeroDeLinea++;
                continue;
            }

            //De lo contrrario es comentario invalido
            if (line.contains("'")) {
                manejadorErrores.addError(numeroDeLinea,
                    "Comentario inválido.");
            }

            numeroDeLinea++;
        }
    }

    //Funcion para validar los identificadores
    public void validarIdentificador(List<Token> tokens,
                                     ManejoErrores manejadorErrores) {
        
        //Se recorre la lista de tokens
        for (Token t : tokens) {
            //Se obtiene el tipo
            if (t.getTipo() == TiposTokens.Identificador) {
                
                //Si no es un identificador valido, error
                if (!Utilidades.esIdentificador(t.getLexema())) {
                    
                    manejadorErrores.addError(t.getLinea(),
                       "Identificador inválido: " + t.getLexema());
                }
            }
        }
    }
    
    //Funcion para validar Dim
    public void validarDim(List<String> lineas, ManejoErrores manejadorErrores) {

        boolean moduleFound = false;
        Map<String, String> variablesDeclaradas = new HashMap<>();        

        int numeroDeLinea = 1;

        for (String linea : lineas) {

            String lineaSinEspacios = linea.trim();

            if (lineaSinEspacios.toLowerCase().startsWith("module "))
                moduleFound = true;

            if (lineaSinEspacios.toLowerCase().startsWith("dim ")) {

                if (!moduleFound) {
                    manejadorErrores.addError(numeroDeLinea, "Dim antes de Module");
                }

                String[] parts = lineaSinEspacios.split("\\s+");
                
                //Se valida la estructura minima, que debe tener cuatro partes
                if (parts.length < 4) {
                    manejadorErrores.addError(numeroDeLinea, "Formato Dim inválido");
                } else {
                    
                    //Se extraen las partes
                    String varName = parts[1];
                    String asWord = parts[2];
                    String type = parts[3];
                    
                    //Se validan las partes del DIM
                    if (!Utilidades.esIdentificador(varName)) {
                        manejadorErrores.addError(numeroDeLinea, "Identificador inválido en Dim");
                        
                    } else if (Lexer.PalabrasReservadas.contains(varName.toLowerCase())) {
                        
                        manejadorErrores.addError(numeroDeLinea, "No se puede usar palabra reservada como identificador: " + varName);
                    
                    }
                    
                    // Se valida la palabra As
                    if (!asWord.equalsIgnoreCase("As")) {
                        manejadorErrores.addError(numeroDeLinea, "Falta palabra As");
                    }
                    
                    // Se validan los tipos de datos permitidos
                    if (!type.matches("(?i)Integer|String|Boolean|Byte")) {
                        manejadorErrores.addError(numeroDeLinea, "Tipo de dato inválido");
                    }
                    
                    // Si hay asignación
                    if (parts.length > 4) {

                        if (!parts[4].equals("=")) {
                            manejadorErrores.addError(numeroDeLinea, "Formato incorrecto después del tipo de dato");
                        } 
                        else {

                            // Si la asignacion es de String
                            if (type.equalsIgnoreCase("String")) {

                                // Tomar todo lo que viene después del =
                                String asignacionCompleta =
                                    lineaSinEspacios.substring(lineaSinEspacios.indexOf("=") + 1).trim();
                                
                                //Si no esta entre comillas, error
                                if (!Utilidades.esStringLiteral(asignacionCompleta)) {
                                    manejadorErrores.addError(numeroDeLinea,
                                        "Asignación inválida para tipo String");
                                }

                            } 
                            else {

                                // Si la asignacion es numerica

                                if (parts.length < 6) {
                                    manejadorErrores.addError(numeroDeLinea, "Asignación incompleta");
                                } 
                                else {

                                    for (int i = 5; i < parts.length; i++) {

                                        String value = parts[i];

                                        if (Lexer.PalabrasReservadas.contains(value.toLowerCase())) {
                                            manejadorErrores.addError(numeroDeLinea,
                                                "No se puede usar palabra reservada en asignación: " + value);
                                        }

                                        if (value.equals("+") || value.equals("-") ||
                                            value.equals("*") || value.equals("/")) {
                                            continue;
                                        }

                                        if (Utilidades.esNumero(value)) {
                                            continue;
                                        }
                                        
                                        //Se valida si la variable es un identificador
                                        if (Utilidades.esIdentificador(value)) {
                                            
                                            //Se valida si no se declaro anteriormente
                                            if (!variablesDeclaradas.containsKey(value)) {
                                                manejadorErrores.addError(numeroDeLinea,
                                                    "Variable no declarada en operación: " + value);
                                                continue;
                                            }

                                            String tipoVariable = variablesDeclaradas.get(value);

                                            // Se Validan compatibilidad de tipos
                                            if (!tipoVariable.equalsIgnoreCase(type)) {
                                                manejadorErrores.addError(numeroDeLinea,
                                                    "Tipo incompatible en operación: " + value);
                                            }

                                            continue;
                                        }

                                        manejadorErrores.addError(numeroDeLinea,
                                            "Valor inválido en asignación: " + value);
                                    }
                                }
                            }
                        }
                    }
                    // Guardar variable como declarada
                    variablesDeclaradas.put(varName, type);
                }
            }

            numeroDeLinea++;
        }
    }
    
    //Funcion para validar Module
    public void validarModule(List<String> lineas, ManejoErrores manejadorErrores) {

        boolean importsEncontrados = false;
        int numeroDeLinea = 1;

        for (String linea : lineas) {

            String lineaSinEspacios = linea.trim();

            if (lineaSinEspacios.toLowerCase().startsWith("imports "))
                importsEncontrados = true;

            if (lineaSinEspacios.toLowerCase().startsWith("module")) {

                if (!importsEncontrados)
                    manejadorErrores.addError(numeroDeLinea, "Module antes de Imports");

                if (!lineaSinEspacios.matches("Module [A-Za-z][A-Za-z0-9_]*")) {
                    manejadorErrores.addError(numeroDeLinea,
                     "Formato Module inválido o identificador incorrecto");
                }
            }

            numeroDeLinea++;
        }
    }

    //Funcion para validar ConsoleWriteLine
    public void validarConsole(List<String> lineas, ManejoErrores manejadorErrores) {

        int numeroDeLinea = 1;

        for (String linea : lineas) {

            String lineaSinEspacios = linea.trim();

            if (lineaSinEspacios.startsWith("Console.WriteLine")) {

                if (!lineaSinEspacios.contains("(") || !lineaSinEspacios.contains(")")) {
                    manejadorErrores.addError(numeroDeLinea, "Faltan paréntesis");
                }

                int open = lineaSinEspacios.indexOf("(");
                int close = lineaSinEspacios.lastIndexOf(")");
                
                // Se extrae el contenido dentro del parentesis
                String contenido = lineaSinEspacios.substring(open + 1, close).trim();

                if (contenido.isEmpty()) {
                    manejadorErrores.addError(numeroDeLinea, "Console.WriteLine vacío");
                }
                
                // Se valida si las comillas estan mal cerradas
                if (lineaSinEspacios.contains("\"") &&
                   !lineaSinEspacios.matches(".*\".*\".*")) {
                    manejadorErrores.addError(numeroDeLinea,
                     "Cadena sin cerrar comillas");
                }
            }

            numeroDeLinea++;
        }
    }
    
    //Funcion para validar End Module
    public void validarEndModule(List<String> lineas, ManejoErrores manejadorErrores) {

        if (lineas.isEmpty()) return;

        int lastIndex = lineas.size() - 1;
        boolean endModuleAparecio = false;

        for (int i = 0; i < lineas.size(); i++) {

            String linea = lineas.get(i).trim();

            // Se detecta cualquier variación del End Module
            if (linea.matches("(?i)^End\\s+Module.*$")) {

                endModuleAparecio = true;

                // Se validan los espacios entre la palabra End Module
                if (linea.matches("(?i)^End\\s{2,}Module.*$")) {
                    manejadorErrores.addError(
                        i + 1,
                        "End Module tiene más de un espacio entre palabras"
                    );
                }

                // Se valida el texto o codigo adicional en End Module
                if (linea.matches("(?i)^End\\s+Module\\s+.+$")) {
                    manejadorErrores.addError(
                        i + 1,
                        "End Module no debe contener texto o código adicional"
                    );
                }

                // Se valida la manera exacta de escritura del End Module
                if (!linea.matches("(?i)^End Module$")) {
                    // Solo se marca si no fue ya detectado como error específico
                    if (!linea.matches("(?i)^End\\s{2,}Module.*$") &&
                        !linea.matches("(?i)^End\\s+Module\\s+.+$")) {

                        manejadorErrores.addError(
                            i + 1,
                            "Formato incorrecto de End Module"
                        );
                    }
                }

                // Se valida que End Module sea la ultima linea del Codigo
                if (i != lastIndex) {
                    manejadorErrores.addError(
                        i + 1,
                        "End Module debe ser la última línea del archivo"
                    );
                }
            }
        }

        // Se valida que exista el End Module
        if (!endModuleAparecio) {
            manejadorErrores.addError(
                lineas.size(),
                "Falta declaración obligatoria End Module al final del archivo"
            );
        }
    }
    
    //Funcion para validar ciclo While
    public void validarWhile(List<String> lineas, ManejoErrores manejadorErrores) {

        Map<String, String> variables = obtenerVariables(lineas);

        for (int i = 0; i < lineas.size(); i++) {

            String linea = lineas.get(i).trim();

            if (linea.toLowerCase().startsWith("while ")) {

                // Se Valida y extrae la condición
                String condicion = linea.substring(6).trim();

                String[] partes = condicion.split("\\s+");
                
                // Se valida que tenga las 3 partes obligatorias para un While
                if (partes.length != 3) {
                    
                    manejadorErrores.addError(i + 1, "Condición While inválida");
                    continue;
                }

                String variable = partes[0];
                String operador = partes[1];
                String valor = partes[2];
                
                //Se valida que se utilizen variables declaradas anteriormente
                if (!variables.containsKey(variable)) {
                    
                    manejadorErrores.addError(i + 1, "Variable no declarada en While");
                    
                } else if (!variables.get(variable).equalsIgnoreCase("Integer")) {
                    
                    manejadorErrores.addError(i + 1, "Variable debe ser Integer en While");
                }
                
                //Se valida que se usen los operadores correctos
                if (!operador.matches("[<>=]")) {
                    
                    manejadorErrores.addError(i + 1, "Operador inválido en While");
                }
                
                //Se valida que se usen numeros enteros
                if (!Utilidades.esNumero(valor)) {
                    
                    manejadorErrores.addError(i + 1, "Valor no es entero en While");
                }

                // Se busca el End While
                boolean endEncontrado = false;
                boolean hayCodigo = false;

                for (int j = i + 1; j < lineas.size(); j++) {

                    String inner = lineas.get(j).trim();
                    
                    //Se valida si se encontro el End While
                    if (inner.equalsIgnoreCase("End While")) {
                        endEncontrado = true;
                        break;
                    }
                    
                    //Se valida que la linea no este vacia y no sea un comentario
                    if (!inner.isEmpty() && !inner.startsWith("'")) {
                        hayCodigo = true;
                    }
                }

                if (!endEncontrado) {
                    manejadorErrores.addError(i + 1, "While sin End While");
                }

                if (!hayCodigo) {
                    manejadorErrores.addError(i + 1, "While sin código interno");
                }
            }
        }
    }
    
    //Funcion para validar ciclo For
    public void validarFor(List<String> lineas, ManejoErrores manejadorErrores) {

        for (int i = 0; i < lineas.size(); i++) {

            String linea = lineas.get(i).trim();

            if (linea.toLowerCase().startsWith("for ")) {

                if (!linea.toLowerCase().contains(" to ")) {
                    manejadorErrores.addError(i + 1, "Falta TO en For");
                }

                String[] partes = linea.split("\\s+");
                
                //Se valida que tenga las partes obligatorias para un For correcto
                if (partes.length < 6) {
                    
                    manejadorErrores.addError(i + 1, "Formato For inválido");
                    continue;
                }
                
                //Se extraen los numeros del for para validarlos
                String valorInicial = partes[3];
                String valorFinal = partes[5];
                
                //Se valida que el numero inicial y final del for sean enteros
                if (!Utilidades.esNumero(valorInicial)) {
                    
                    manejadorErrores.addError(i + 1, "Valor inicial no es entero");
                }

                if (!Utilidades.esNumero(valorFinal)) {
                    
                    manejadorErrores.addError(i + 1, "Valor final no es entero");
                }

                boolean nextEncontrado = false;
                boolean hayCodigo = false;
                
                //Se recorren las lineas dentro del For
                for (int j = i + 1; j < lineas.size(); j++) {

                    String inner = lineas.get(j).trim();
                    
                    //Se valida que haya Next
                    if (inner.equalsIgnoreCase("Next")) {
                        
                        nextEncontrado = true;
                        break;
                    }
                    
                    //Se valida que la linea no este vacia y no sea un comentario
                    if (!inner.isEmpty() && !inner.startsWith("'")) {
                        hayCodigo = true;
                    }
                }

                if (!nextEncontrado) {
                    manejadorErrores.addError(i + 1, "For sin Next");
                }

                if (!hayCodigo) {
                    manejadorErrores.addError(i + 1, "For sin código interno");
                }
            }
        }
    }
    
    //Funcion para validar If
    public void validarIf(List<String> lineas, ManejoErrores manejadorErrores) {

        for (int i = 0; i < lineas.size(); i++) {

            String linea = lineas.get(i).trim();

            if (linea.toLowerCase().startsWith("if")) {
                
                //Se convierte la linea en minusculas para no tener problemas en validaciones
                String lineaLower = linea.toLowerCase();

                // Se valida que exista el Then
                if (!lineaLower.contains("then")) {
                    
                    manejadorErrores.addError(i + 1, "Falta THEN en IF");
                    continue;
                }

                // Se extrae la condicion ignorando minusculas y mayusculas
                String[] partes = linea.split("(?i)then", 2);
                
                //Se valida que la condicion tenga una estructura correcta o da error
                if (partes.length < 2) {
                    
                    manejadorErrores.addError(i + 1, "Estructura IF inválida");
                    continue;
                }
                
                //Se elimina el if de la condicion para validar despues la condicion
                String condicion = partes[0]
                        .replaceFirst("(?i)if", "")
                        .trim();

                // Se valida la falta del condicional
                if (condicion.isEmpty()) {
                    
                    manejadorErrores.addError(i + 1, "Falta condición en IF");
                }

                // Validacion del bloque

                boolean elseEncontrado = false;
                boolean endIfEncontrado = false;
                boolean codigoThen = false;
                boolean codigoElse = false;

                boolean enElse = false;
                
                //Ayuda a manejar el If dentro de otro If
                int nivelIf = 1;

                for (int j = i + 1; j < lineas.size(); j++) {

                    String inner = lineas.get(j).trim();
                    String innerLower = inner.toLowerCase();

                    // Se controla el IF anidado
                    if (innerLower.startsWith("if")) {
                        nivelIf++;
                    }

                    // Si detecta END IF baja el nivel de If
                    if (innerLower.equals("end if")) {
                        nivelIf--;

                        if (nivelIf == 0) {
                            endIfEncontrado = true;
                            break;
                        }
                        continue;
                    }

                    // Se valida y detecta el ELSE
                    if (nivelIf == 1 && innerLower.equals("else")) {
                        elseEncontrado = true;
                        enElse = true;
                        continue;
                    }

                    // Se valida el código despues de Then y Else que no este vacio o sea comentario
                    if (!inner.isEmpty() && !inner.startsWith("'")) {

                        if (nivelIf == 1) {
                            if (!enElse) {
                                codigoThen = true;
                            } else {
                                codigoElse = true;
                            }
                        }
                    }
                }

                // Se validan errores finales

                if (!endIfEncontrado) {
                    manejadorErrores.addError(i + 1, "IF sin END IF");
                }

                if (!elseEncontrado) {
                    manejadorErrores.addError(i + 1, "IF sin ELSE");
                }

                if (!codigoThen) {
                    manejadorErrores.addError(i + 1, "IF sin código en THEN");
                }

                if (!codigoElse) {
                    manejadorErrores.addError(i + 1, "IF sin código en ELSE");
                }
            }
        }
    }
    
    //Funcion auxiliar para obtener variables
    public Map<String, String> obtenerVariables(List<String> lineas) {
        
        //Se guardan las variables declaradas, variable y tipo
        Map<String, String> variables = new HashMap<>();
        
        //Se recorre la linea
        for (String linea : lineas) {

            linea = linea.trim();
            
            //Se valida si la linea empieza con Dim
            if (linea.toLowerCase().startsWith("dim ")) {
                
                //Se separan las partes del Dim
                String[] partes = linea.split("\\s+");

                if (partes.length >= 4) {
                    variables.put(partes[1], partes[3]);
                }
            }
        }

        return variables;
    }
}
