package estructuras;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Clase para los archivos
 */
public class Archivo {
  private File archivo;

  /**
   * Instanciar un archivo
   * @param nombre Ruta y nombre del archivo
   */
  public Archivo(String nombre) {
    try {
      archivo = new File(nombre);
    } catch(NullPointerException e) {
      System.err.println("No se encontro el archivo " + nombre + "\n");
    }
  }

  /**
   * @return Arreglo de las líneas del archivo
   */
  public ArrayList<String> leer() {
    ArrayList<String> lineas = new ArrayList<>();

    try {
      Scanner lector = new Scanner(archivo);

      while(lector.hasNextLine()) lineas.add(lector.nextLine());

      lector.close();
    } catch(FileNotFoundException e) {
      System.err.println("No se encontro el archivo\n");
    }

    return lineas;
  }
}
