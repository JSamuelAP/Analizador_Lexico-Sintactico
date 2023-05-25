package sintactico;

import lexico.Token;

/**
 * Clase para la matriz predictiva
 * @author Aldana Pérez José Samuel <20240446@leon.tecnm.mx>
 */
public class MatrizPredictiva {
  private final int[][] matriz;
  private final Simbolo [] simbolosTerminales;
  private final Simbolo [] simbolosNoTerminales;

  /**
   * Instanciar una matriz predictiva
   * @param simbolosTerminales Arreglo de los simbolos terminales
   * @param simbolosNoTerminales Arreglo de los simbolos no terminales
   */
  public MatrizPredictiva(Simbolo[] simbolosTerminales, Simbolo[] simbolosNoTerminales) {
    this.matriz = new int[][] {
      { 1,  0, 0, 0,  0, 0, 0,  0,  0, 0,  0,  0,  0,  0,  0,  0,  0,  0},
      { 0,  2, 0, 0,  0, 0, 2,  0,  0, 2,  0,  0,  0,  2,  2,  0,  0,  0},
      { 0,  3, 0, 4,  0, 0, 3,  0,  0, 3,  0,  0,  0,  3,  3,  0,  0,  0},
      { 0,  6, 0, 0,  0, 0, 7,  0,  0, 8,  0,  0,  0,  5,  5,  0,  0,  0},
      { 0,  9, 0, 0,  0, 0, 0,  0,  0, 0,  0,  0,  0,  0,  0,  0,  0,  0},
      { 0,  0, 0, 0, 11, 0, 0,  0, 11, 0, 10,  0,  0,  0,  0,  0,  0,  0},
      { 0, 12, 0, 0,  0, 0, 0, 12,  0, 0,  0, 12, 12,  0,  0,  0,  0,  0},
      { 0,  0, 0, 0, 13, 0, 0,  0, 14, 0,  0,  0,  0,  0,  0,  0,  0,  0},
      { 0, 15, 0, 0,  0, 0, 0, 15,  0, 0,  0, 15, 15,  0,  0,  0,  0,  0},
      { 0,  0, 0, 0, 17, 0, 0,  0, 17, 0,  0,  0,  0,  0,  0, 16, 16, 16},
      { 0, 19, 0, 0,  0, 0, 0, 18,  0, 0,  0, 20, 21,  0,  0,  0,  0,  0},
      { 0,  0, 0, 0,  0, 0, 0,  0,  0, 0,  0,  0,  0, 22, 23,  0,  0,  0},
      { 0,  0, 0, 0,  0, 0, 0,  0,  0, 0,  0,  0,  0,  0,  0, 24, 25, 26},
      {27,  0, 0, 0,  0, 0, 0,  0,  0, 0,  0,  0,  0,  0,  0,  0,  0,  0}
    };
    this.simbolosTerminales = simbolosTerminales;
    this.simbolosNoTerminales = simbolosNoTerminales;
  }

  /**
   * Calcula el número de la siguiente producción
   * @param x Simbolo no terminal
   * @param a Token (Simbolo terminal)
   * @return La intersección entre x y a
   */
  public int predecir(Simbolo x, Token a) {
    int fila = getIndiceDeNoTerminal(x);
    int columna = 0;

    for (Simbolo terminal: simbolosTerminales) {
      if (terminal.getCadena().equals(a.getLexema()) ||
         (terminal.getCadena().equals("id")      && a.getAtributo() == 295) ||
         (terminal.getCadena().equals("enteros") && a.getAtributo() == 296) ||
         (terminal.getCadena().equals("reales")  && a.getAtributo() == 297)
      ) {
        columna = terminal.getIndice();
        break;
      }
    }

    return matriz[fila - 1][columna - 1];
  }

  /**
   * Calcular el indice del simbolo en el arreglo de los no terminales
   * @param x Simbolo a obtener su indice
   * @return El número de x en el areglo
   */
  private int getIndiceDeNoTerminal(Simbolo x) {
    for (Simbolo simboloNoTerminal: simbolosNoTerminales)
      if (simboloNoTerminal.getCadena().equals(x.getCadena())) return simboloNoTerminal.getIndice();

    return 0;
  }

  /**
   * Imprimir el contenido de la matriz predictiva
   */
  public void imprimirMatriz() {
    for (int i = 0; i < matriz.length; i++) {
      for (int j = 0; j < matriz[i].length; j++) System.out.printf("%2d  ", matriz[i][j]);
      System.out.println();
    }
  }
}
