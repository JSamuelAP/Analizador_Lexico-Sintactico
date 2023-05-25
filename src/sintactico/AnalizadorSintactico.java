package sintactico;

import java.util.*;
import estructuras.Archivo;
import estructuras.Pila;
import lexico.AnalizadorLexico;
import lexico.Token;

/**
 * Clase para el analizador sintáctico.
 * Contiene los métodos para calcular las estructuras de la gramatica
 * @author Aldana Pérez José Samuel <20240446@leon.tecnm.mx>
 */
public class AnalizadorSintactico {
  private final Archivo gramatica;
  private Produccion [] producciones;
  private Simbolo [][] ladoDerecho;
  private Simbolo [] terminales;
  private Simbolo [] noTerminales;
  private final MatrizPredictiva matrizPredictiva;
  private Pila<Simbolo> pila;

  /**
   * Instanciar un analizador sintactico
   * @param archivoGramatica Ruta y nombre del archivo de texto con la gramática
   */
  public AnalizadorSintactico(String archivoGramatica) {
    this.gramatica = new Archivo(archivoGramatica);
    this.calcularEstructuras();
    this.matrizPredictiva = new MatrizPredictiva(terminales, noTerminales);
  }

  /**
   * Calcular las estructuras de los simbolos de la gramática
   */
  private void calcularEstructuras() {
    this.leerGramatica();
    this.calcularLadoDerecho();
    this.calcularSimbolosNoTerminales();
    this.calcularSimbolosTerminales();
  }

  /**
   * Leer gramática línea por línea y generar un arreglo de las producciones encontradas
   */
  private void leerGramatica() {
    ArrayList<String> lineas = this.gramatica.leer();
    this.producciones = new Produccion[lineas.size()];

    for (int i = 0; i < lineas.size(); i++)
      this.producciones[i] = new Produccion(lineas.get(i), i + 1);
  }

  /**
   * Por cada producción, guardar el arreglo de simbolos que están en el lado derecho
   * en la matriz de simbolos ladoDerecho.
   */
  private void calcularLadoDerecho () {
    this.ladoDerecho = new Simbolo[this.producciones.length][];

    for (int i = 0; i < producciones.length; i++)
      ladoDerecho[i] = producciones[i].getSimbolosLadoDerecho();
  }

  /**
   * Por cada producción, guardar el simbolo del lado izquierdo en el arreglo noTerminales.
   * Los simbolos repetidos no se guardan.
   */
  private void calcularSimbolosNoTerminales() {
    // Para facilitar la comprobación de que un simbolo ya ha sido guardado
    ArrayList<String> ladoIzquierdo = new ArrayList<>();
    // Para guardar los objetos simbolo y convertiro a arreglo estatico
    ArrayList<Simbolo> simbolosNoTerminales = new ArrayList<>();

    // Guardar el simboo si es que no existe en ladoIzquierdo
    for (Produccion produccion: producciones)
      if (!ladoIzquierdo.contains(produccion.getSimboloLadoIzquierdo().getCadena())) {
        ladoIzquierdo.add(produccion.getSimboloLadoIzquierdo().getCadena());
        simbolosNoTerminales.add(produccion.getSimboloLadoIzquierdo());
      }

    noTerminales = simbolosNoTerminales.toArray(new Simbolo[0]);

    // Ajustar los indices a su posición en el arreglo + 1
    for (int i = 0; i < noTerminales.length; i++) noTerminales[i].setIndice(i + 1);
  }

  /**
   * Por cada arreglo de simbolos del lado derecho, guardar aquellos que no existan en el arreglo noTerminales.
   * Los simbolos repetidos no se guardan.
   */
  private void calcularSimbolosTerminales() {
    ArrayList<String> simbolos = new ArrayList<>();
    ArrayList<Simbolo> simbolosTerminales = new ArrayList<>();

    // Por cada producción, los simbolos del lado derecho
    for (Simbolo[] simbolosLadoDerecho : ladoDerecho) {
      // Si es palabra vacía, saltar a la siguiente producción
      if (simbolosLadoDerecho[0].getCadena().equals("ε")) continue;

      // Por cada simbolo del lado derecho
      for (Simbolo simbolo : simbolosLadoDerecho)
        // Si no se ha guardado antes el simbolo
        if (!simbolos.contains(simbolo.getCadena()))
          // Si el simbolo no está en los no terminales, guardarlo
          if (!contiene(noTerminales, simbolo.getCadena())) {
            simbolos.add(simbolo.getCadena());
            // Indicar que es terminal
            simbolo.setTipo(Simbolo.Tipo.TERMINAL);
            simbolosTerminales.add(simbolo);
          } else // Si el simbolo está en los no terminales, indicar que es no terminal
            simbolo.setTipo(Simbolo.Tipo.NOTERMINAL);
    }

    terminales = simbolosTerminales.toArray(new Simbolo[0]);

    // Ajustar los indices a su posición en el arreglo + 1
    for (int i = 0; i < terminales.length; i++) terminales[i].setIndice(i + 1);
  }

  /**
   * Buscar si un arreglo ya contiene un simbolo
   * @param arreglo Arreglo de simbolos a examinar
   * @param simbolo Simbolo a buscar
   * @return true si arreglo contiene a simbolo
   */
  private boolean contiene(Simbolo[] arreglo, String simbolo) {
    boolean encontrado = false;

    for (Simbolo item: arreglo)
      if (item.getCadena().equals(simbolo)) {
        encontrado = true;
        break;
      }

    return encontrado;
  }

  /**
   * Calcula el simbolo inicial en la gramática
   * @return Simbolo inicial encontrado
   */
  public Simbolo calcularSimboloInicial() {
    Simbolo simboloInicial = null;
    boolean estaEnLadoDerecho = false;

    // Recorrer los simbolos no terminales
    for (Simbolo simboloNoTerminal: noTerminales) {
      // Recorrer los arreglos de simbolos del lado derecho
      for (Simbolo[] simbolos: ladoDerecho) {
        // Recorrer los simbolos del lado derecho
        for (Simbolo simbolo: simbolos) {
          // Si es un simbolo terminal, ignorarlo y saltar al siguiente
          if (!simbolo.esNoTerminal()) continue;
          // Si el simbolo no terminal se encuentra en el lado derecho
          // marcarlo y saltar a la siguiente producción
          if (simboloNoTerminal.getCadena().equals(simbolo.getCadena())) {
            estaEnLadoDerecho = true;
            break;
          }
        }
        // Si el simbolo no terminal se encuentra en el lado derecho, saltar al siguiente no terminal
        if (estaEnLadoDerecho) break;
      }
      // Si el simbolo no terminal no se encontro en el lado derecho, este simbolo es el inicial
      if (!estaEnLadoDerecho) {
        simboloInicial = simboloNoTerminal;
        break;
      }
      // Reiniciar para el siguiente no terminal
      estaEnLadoDerecho = false;
    }

    // Si no se encontro el simbolo inicial explicito, tomarlo de la primera producción
    if (simboloInicial == null) simboloInicial = producciones[0].getSimboloLadoIzquierdo();

    return simboloInicial;
  }

  /**
   * Realizar el analisis sintáctico mediante el algoritmo LLDriver
   * @param simboloInicial Simbolo inicial de la gramática
   * @param lexico Analizador léxico
   */
  public void analizar(Simbolo simboloInicial, AnalizadorLexico lexico) {
    Simbolo [] simbolosLadoDerecho;
    int produccion;

    // Insertar el simbolo inicial en la pila vacía
    pila = new Pila<>(simboloInicial);
    Simbolo x = pila.getTope();
    Token a = lexico.getSiguienteToken();

    while (!pila.estaVacia()) {
      if (x.esNoTerminal()) {
        produccion = matrizPredictiva.predecir(x, a);
        System.out.println("Siguiente producción: " + produccion);
        if (produccion != 0) {
          simbolosLadoDerecho = producciones[produccion - 1].getSimbolosLadoDerecho();
          // pop y ciclo de push para meter simbolos de derecha a izquierda
          pila.pop();
          for (int i = simbolosLadoDerecho.length - 1; i >= 0; i--)
            // Si es palabra vacía, no meter a la pila
            if (!simbolosLadoDerecho[i].getCadena().equals("ε"))
              pila.push(simbolosLadoDerecho[i]);
          // Reemplazar x con la parte alta de la pila
          x = pila.getTope();
        } else {
          imprimirError(x, a);
          return;
        }
      }
      else {
        if (
            x.getCadena().equals(a.getLexema()) ||
            (x.getCadena().equals("id")      && a.getAtributo() == 295) ||
            (x.getCadena().equals("enteros") && a.getAtributo() == 296) ||
            (x.getCadena().equals("reales")  && a.getAtributo() == 297)
        ) {
          System.out.println("Token " + a.getLexema() + " es un " + x.getCadena());
          pila.pop();
          if(!pila.estaVacia()) x = pila.getTope();
          a = lexico.getSiguienteToken();
        } else {
          imprimirError(x, a);
          return;
        }
      }
    }
  }

  /**
   * Muestra el error sintactico generado durante el analisis
   * @param x Simbolo esperado
   * @param a Token recibido
   */
  private void imprimirError(Simbolo x, Token a) {
    System.err.println("Error sintactico, se esperaba un <" + x.getCadena() + "> y se obtuvo un " + a.getLexema());
  }

  /**
   * Imprime los contenidos de las estructuras de los simbolos no terminales, terminales y lados derechos de la
   * gramática
   */
  public void imprimirEstructuras() {
    System.out.println("\nSímbolos no terminales: ");
    this.imprimirEstructura(this.noTerminales);

    System.out.println("\nSímbolos terminales: ");
    this.imprimirEstructura(this.terminales);

    System.out.println("\nLados derechos: ");
    for (Simbolo[] simbolos : ladoDerecho) {
      for (Simbolo simbolo : simbolos)
        System.out.print(simbolo + " ");
      System.out.println();
    }

    System.out.println("\nMatriz predictiva: ");
    matrizPredictiva.imprimirMatriz();
    System.out.println("\n");
  }

  /**
   * Imprime el contenido de un arreglo
   * @param estructura Arreglo a imprimir
   */
  private void imprimirEstructura(Object [] estructura) {
    for(Object linea: estructura) System.out.println(linea);
  }
}
