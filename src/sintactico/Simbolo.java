package sintactico;

/**
 * Clase para los simbolos terminales y no terminales de la gramática
 * @author Aldana Pérez José Samuel <20240446@leon.tecnm.mx>
 */
public class Simbolo {
  private final String cadena;
  private Tipo tipo;
  private int indice;
  public enum Tipo {
    TERMINAL,
    NOTERMINAL
  }

  /**
   * Instanciar un simbolo
   * @param cadena El texto del simbolo
   * @param tipo Terminal o no terminal
   * @param indice Número de la producción donde se encuentra
   */
  public Simbolo (String cadena, Tipo tipo, int indice) {
    this.cadena = cadena;
    this.tipo = tipo;
    this.indice = indice;
  }

  /**
   * @return True si es un simbolo terminal
   */
  public boolean esTerminal() { return this.tipo == Tipo.TERMINAL; }

  /**
   * @return True si es un simbolo no terminal
   */
  public boolean esNoTerminal() { return this.tipo == Tipo.NOTERMINAL; }

  /**
   * @return Texto del simbolo
   */
  public String getCadena() { return this.cadena; }

  /**
   * @return terminal o no terminal
   */
  public Tipo getTipo() { return this.tipo; }

  /**
   * Indicar si es un simbolo terminal o no terminal
   * @param tipo terminal o no terminal
   */
  public void setTipo(Tipo tipo) { this.tipo = tipo; }

  /**
   * @return Número de producción dónde se encuentra
   */
  public int getIndice() { return this.indice; }

  /**
   * @param i número de su producción
   */
  public void setIndice(int i) { this.indice = i; }

  /**
   * @return El simbolo en formato de texto
   */
  @Override
  public String toString() { return this.cadena; }
}

