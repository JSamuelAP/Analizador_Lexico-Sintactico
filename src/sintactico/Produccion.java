package sintactico;

import java.util.ArrayList;

/**
 * Clase para las producciones de la gramática
 * @author Aldana Pérez José Samuel <20240446@leon.tecnm.mx>
 */
public class Produccion {
  private final String cadena;
  private final int numProduccion;
  private final Simbolo ladoIzquierdo;
  private final Simbolo[] ladoDerecho;

  /**
   * Instanciar una producción
   * @param cadena Línea leída de la gramática
   * @param numProduccion Número de la producción
   */
  public Produccion(String cadena, int numProduccion) {
    this.cadena = cadena.trim().replaceAll("\\s+", " ");
    this.numProduccion = numProduccion;
    this.ladoIzquierdo = calcularLadoIzquierdo();
    this.ladoDerecho = calcularLadoDerecho();
  }

  /**
   * Calcula el lado izquierdo de la producción
   * @return El simbolo en el lado izquierdo
   */
  private Simbolo calcularLadoIzquierdo() {
    String ladoIzquierdo = this.cadena.split("->")[0].trim();
    return new Simbolo(ladoIzquierdo, Simbolo.Tipo.NOTERMINAL, numProduccion);
  }

  /**
   * Calcula el lado derecho de la proucción
   * @return Arreglo de simbolos en el lado derecho
   */
  private Simbolo[] calcularLadoDerecho() {
    String ladoDerecho = this.cadena.split("->")[1].trim();
    ArrayList<Simbolo> simbolos = new ArrayList<>();

    for (String simbolo: ladoDerecho.split(" "))
      simbolos.add(new Simbolo(simbolo, null, numProduccion));

    return simbolos.toArray(new Simbolo[0]);
  }

  /**
   * @return Arreglo de simbolos en el lado derecho de la producción
   */
  public Simbolo [] getSimbolosLadoDerecho() { return this.ladoDerecho; }

  /**
   * @return El simbolo en el lado izquierdo de la producción
   */
  public Simbolo getSimboloLadoIzquierdo() { return this.ladoIzquierdo; }

  /**
   * @return El número de la producción
   */
  public int getNumProduccion() {
    return numProduccion;
  }

  /**
   * @return La producción en formato de texto
   */
  public String toString() { return this.numProduccion + ". " + this.cadena; }
}
