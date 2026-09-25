
package analizadorlexico;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;


public class ManejoArchivos {
    public List<String> readVBFile(String nombreArchivo) throws Exception {

        if (!nombreArchivo.endsWith(".vb"))
            throw new Exception("El archivo no tiene la extension .vb");

        List<String> lineas = new ArrayList<>();
        BufferedReader br = new BufferedReader(new FileReader(nombreArchivo));
        String linea;

        while ((linea = br.readLine()) != null) {
            lineas.add(linea);
        }

        br.close();
        return lineas;
    }

    public void writeLog(String nombreOriginal,
                         List<String> lineasNumeradas,
                         List<String> errores) throws Exception {

        String nombreDelLog = nombreOriginal.replace(".vb", "-errores.log");
        BufferedWriter bw = new BufferedWriter(new FileWriter(nombreDelLog));

        for (String l : lineasNumeradas)
            bw.write(l + "\n");

        bw.write("\n");

        for (String e : errores)
            bw.write(e + "\n");

        bw.close();
    }
}
