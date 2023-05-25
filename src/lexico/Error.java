package lexico;

/**
 * Clase para los errores léxicos
 */
public class Error {
  private final String mensaje;
  private final int linea;

  /**
   * Instanciar un error léxico
   * @param mensaje Motivo del error
   * @param linea Línea en el programa donde se generó el error
   */
  public Error(String mensaje, int linea) {
    this.mensaje = mensaje;
    this.linea = linea;
  }

  /**
   * @return Error léxico en formato de texto
   */
  @Override
  public String toString() { return "Error lexico en la línea " + linea + ", " + mensaje; }
}
