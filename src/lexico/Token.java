package lexico;

/**
 * Clase para las tokens del analizador léxico
 * @author Aldana Pérez José Samuel <20240446@leon.tecnm.mx>
 */
public class Token {
  private final String lexema;
  private final int atributo;
  private final String categoriaLexica;

  /**
   * Instanciar un token
   * @param lexema Cadena léida
   * @param atributo Número entero de la categoría léxica
   */
  public Token(String lexema, int atributo) {
    this.lexema = lexema;
    this.atributo = atributo;
    this.categoriaLexica = atributoACategoriaLexia(atributo);
  }

  /**
   * Recibe un atributo en forma de entero y devuelve en String su categoría lexica correspondiente
   * @param atributo Número entero
   * @return Categoria lexica
   */
  public String atributoACategoriaLexia(int atributo) {
    String categoria = "desconocida";

    if (atributo >= 0 && atributo <= 255) categoria = "Caracter simple";
    else if (atributo == 295) categoria = "Identificador";
    else if (atributo == 296) categoria = "Número entero natural";
    else if (atributo == 297) categoria = "Número de punto flotante";
    else if (atributo >= 300 && atributo <= 304) categoria = "Palabra reservada";

    return categoria;
  }

  /**
   * @return Cadena del token
   */
  public String getLexema() { return this.lexema; }

  /**
   * @return Número entero de la categoría léxica
   */
  public int getAtributo() { return this.atributo; }

  /**
   * @return Token en formato de texto
   */
  @Override
  public String toString() {
    return String.format("%-10s %-25s %s", this.lexema, this.categoriaLexica, this.atributo);
  }
}
