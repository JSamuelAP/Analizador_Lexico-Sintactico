import lexico.AnalizadorLexico;
import sintactico.AnalizadorSintactico;

public class Main {
  public static void main(String[] args) {
    AnalizadorLexico lexico = new AnalizadorLexico("src/Archivos/Ejemplo.txt");
    AnalizadorSintactico sintactico = new AnalizadorSintactico("src/Sintactico/Gramatica.txt");

    lexico.imprimirArchivo();
    sintactico.imprimirEstructuras();
    sintactico.analizar(sintactico.calcularSimboloInicial(), lexico);
    lexico.imprimirSimbolos();
  }
}
