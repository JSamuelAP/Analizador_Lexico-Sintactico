package lexico;

import java.util.ArrayList;
import estructuras.Archivo;
import estructuras.Lista;
import estructuras.Nodo;

/**
 * Clase para el analizador léxico
 */
public class AnalizadorLexico {
  private final Archivo archivo;
  ArrayList<String> lineas;
  private final AFN automata;
  private Lista<Token> simbolos;

  /**
   * Instanciar un analizador léxico
   * @param nombreArchivo Ruta y nombre del archivo a analizar
   */
  public AnalizadorLexico(String nombreArchivo) {
    this.archivo = new Archivo(nombreArchivo);
    this.lineas = archivo.leer();
    automata = new AFN(lineas);
  }

  /**
   * @return Siguiente token válido en el programa
   */
  public Token getSiguienteToken() { return automata.obtenerSiguientToken(); }

  /**
   * Mostrar los identificadores encontrados en el programa
   */
  public void imprimirSimbolos() {
    simbolos = automata.getSimbolos();
    Nodo<Token> nodoActual = simbolos.getCabeza();

    System.out.println("\nIdentificadores: ");
    while (nodoActual != null) {
      System.out.println(nodoActual.getDato().getLexema());
      nodoActual = nodoActual.getSiguiente();
    }
  }

  /**
   * Mostrar el programa a analizar
   */
  public void imprimirArchivo() {
    System.out.println("\nPrograma a analizar: ");
    for (String linea: lineas) {
      System.out.println(linea);
    }
  }
}
