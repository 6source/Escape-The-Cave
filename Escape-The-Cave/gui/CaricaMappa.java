package gui;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
public class CaricaMappa {

    public static char[][] caricaMatriceDaFile(String percorso) {

        List<char[]> righe = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(percorso))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                righe.add(linea.toCharArray());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return righe.toArray(new char[0][0]);
    }
}