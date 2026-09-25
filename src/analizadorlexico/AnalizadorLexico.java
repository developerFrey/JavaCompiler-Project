/*
 * Universidad Estatal a Distancia
 * Programador: Felipe Brenes Conejo
 * Proyecto 2
 * Curso: Compiladores (03307)
 * Periodo: Primer Cuatrimestre 2026
 *
 * Para el desarrollo del presente proyecto, se realizaron diferentes investigaciones,
 * en diferentes sitios web, videos explicativos de la plataforma YouTube,
 * material dado por la UNED, asi como colaborar u apoyarse en la inteligencia artificial,
 * para recomendaciones y explicaciones.
 * Prompt utilizado especificamente: 
 * "Explicame paso por paso cual es la mejor manera y la mas eficiente de crear,
 * u estructurar un analizador lexico y sintactico en java, sin librerias externas y con buenas practicas de programacion."
 *
 */

package analizadorlexico;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class AnalizadorLexico {
   
    public static void main(String[] args) {
        // TODO code application logic here
        try {

            Scanner sc = new Scanner(System.in);
            System.out.print("Nombre archivo .vb: ");
            String file = sc.nextLine();

            ManejoArchivos manejadorArchivos = new ManejoArchivos();
            ManejoErrores manejadorErrores = new ManejoErrores();
            Lexer lexer = new Lexer();
            Validador validador = new Validador();

            List<String> lineas = manejadorArchivos.readVBFile(file);

            List<String> numerador = new ArrayList<>();
            for (int i = 0; i < lineas.size(); i++) {
                numerador.add(Utilidades.cuatroDigitos(i+1) + " " + lineas.get(i));
            }

            List<Token> tokens = lexer.analizar(lineas);
            
            //llamado a las funciones para validar identficadores y otras palabras reservadas
            validador.validarComentarios(lineas, manejadorErrores);
            validador.validarIdentificador(tokens, manejadorErrores);
            validador.validarDim(lineas, manejadorErrores);
            validador.validarModule(lineas, manejadorErrores);
            validador.validarEndModule(lineas, manejadorErrores);
            validador.validarConsole(lineas, manejadorErrores);
            validador.validarWhile(lineas, manejadorErrores);
            validador.validarFor(lineas, manejadorErrores);
            validador.validarIf(lineas, manejadorErrores);
            
            manejadorArchivos.writeLog(file, numerador, manejadorErrores.getErrors());

            System.out.println("Proceso finalizado.");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
}
