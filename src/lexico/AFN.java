package lexico;

import java.util.ArrayList;
import estructuras.Lista;
import estructuras.Nodo;

/**
 * Clase para el automata finito no determinista
 * @author Aldana Pérez José Samuel <20240446@leon.tecnm.mx>
 */
public class AFN {
  private byte estado;
  private byte i, f;
  private int l;
  private char c;
  private final ArrayList<String> lineas;
  private String cadena;
  private final Lista<Token> simbolos;
  private final Character [] caracteresSimples;
  private final String [] palabrasReservadas;
  private enum CategoriasLexicas {
    IDENTIFICADOR,
    NUMERO_ENTERO,
    NUMERO_PUNTO_FLOTANTE,
    CARACTER_SIMPLE,
    PALABRA_RESERVADA
  }

  /**
   * Instanciar un automata finito no determinista
   * @param lineas Arreglo de las líneas del programa a analizar
   */
  public AFN(ArrayList<String> lineas) {
    this.lineas = lineas;
    this.estado = 0;
    this.i = this.f = 0;
    this.l = 0;
    this.caracteresSimples = new Character[] {';', '=', '+', '-', '*', '(', ')', '{', '}', ','};
    this.palabrasReservadas = new String[] { "class", "float", "int", "read", "write" };
    this.simbolos = new Lista<>();
  }

  /**
   * Recorrer el archivo para evaluarlo por el automata (switch) hasta generar un token y devolverlo
   */
  public Token obtenerSiguientToken() {
    Token token = null;

    // Si se llegó a la última línea
    if (l == lineas.size()) return null;

    // Saltarse las lineas en blanco
    while(lineas.get(l).trim().isBlank()) l++;

    cadena = lineas.get(l).trim();
    estado = 0;

    // Recorrer caracter por caracter
    while(token == null) {
      c = cadena.charAt(f); // Caracter actual a evaluar

      switch(estado) {
        case 0: // Estado inicial
          if (esCaracterSimple(c)) estado = 1;
          else if (esLetraMayuscula(c)) estado = 2;
          else if (esDigito(c) && c != '0') estado = 4;
          else if (c == '0') estado = 5;
          else if (esLetraMinuscula(c)) estado = 8;
          else if (esEspacio(c)) i++;
          else generarError("Caracter desconocido: " + c, l);
          break;

        case 1: // Estado final caracteres simples
          token = generarToken(i, i, CategoriasLexicas.CARACTER_SIMPLE);
          break;

        case 2: // Identificadores
          if (esDigito(c) || esLetraMinuscula(c)) estado = 3;
          else if (esCaracterSimple(c) || esEspacio(c)) {
            if (esLetraMayuscula(cadena.charAt(f - 1)))
              generarError("Un identificador no puede ser solo una letra mayuscula: " + cadena.charAt(f - 1), l);
            else if (cadena.charAt(f - 1) == '_')
              generarError("Un identificador no puede terminar con _: " + cadena.substring(i, f), l);
            estado = 0;
          } else if (esLetraMayuscula(c)) {
            generarError("Solo puede haber una letra mayúscula al inicio del identificador: " + c, l);
            estado = 9;
          } else if (c != '_') {
            generarError("Caracter invalido en un identificador: " + c, l);
            estado = 9;
          }
          break;

        case 3: // Estado final identificadores
          if (c == '_') estado = 2;
          else if (esCaracterSimple(c) || esEspacio(c))
            token = generarToken(i, f, CategoriasLexicas.IDENTIFICADOR);
          else if (esLetraMayuscula(c)) {
            generarError("Solo puede haber una letra mayúscula al inicio del identificador: " + c, l);
            estado = 9;
          } else if (!esLetraMinuscula(c) && !esDigito(c)) {
            generarError("Caracter invalido en un identificador: " + c, l);
            estado = 9;
          }
          break;

        case 4: // Números enteros o punto flotante
          if (c == '.') estado = 6;
          else if (esCaracterSimple(c) || esEspacio(c))
            token = generarToken(i, f, CategoriasLexicas.NUMERO_ENTERO);
          else if (!esDigito(c)) {
            generarError("Caracter invalido en un número: " + c, l);
            estado = 10;
          }
          break;

        case 5: // Números punto flotante o Estado final entero 0
          if (c == '.') estado = 6;
          else if (esCaracterSimple(c) || esEspacio(c))
            token = generarToken(i, f, CategoriasLexicas.NUMERO_ENTERO);
          else if (esDigito(c)) {
            generarError("Un número no puede comenzar con 0 seguido de más digitos", l);
            estado = 11;
          }
          else {
            generarError("Caracter invalido en un número: " + c, l);
            estado = 10;
          }
          break;

        case 6: // Números punto flotante
          if (esDigito(c)) estado = 7;
          else if (esEspacio(c)) {
            generarError("Un número no puede terminar con punto decimal: " + cadena.substring(i, f), l);
            //estado = 0;
          } else if (esCaracterSimple(c)) {
            generarError("Un número no puede terminar con punto decimal: " + cadena.substring(i, f), l);
            estado = 1;
          }
          else if (c == '.') {
            generarError("Ya hay un punto decimal en el número", l);
            estado = 10;
          } else {
            generarError("Caracter invalido en un número: " + c, l);
            estado = 10;
          }
          break;

        case 7: // Estado final números punto flotante
          if (esCaracterSimple(c) || esEspacio(c))
            token = generarToken(i, f, CategoriasLexicas.NUMERO_PUNTO_FLOTANTE);
          else if (c == '.') {
            generarError("Ya hay un punto decimal en el número", l);
            estado = 10;
          } else if (!esDigito(c)) {
            generarError("Caracter invalido en un número: " + c, l);
            estado = 10;
          }
          break;

        case 8: // Estado final palabra reservada
          if (esCaracterSimple(c) || esEspacio(c)) {
            if (esPalabraReservada(cadena.substring(i, f)))
              token = generarToken(i, f, CategoriasLexicas.PALABRA_RESERVADA);
            else{
              generarError("Palabra reservada no identificada: " + cadena.substring(i, f), l);
              estado = 0;
            }
          }
          else if (!esLetraMinuscula(c)) {
            generarError("Caracter invalido en una palabra reservada: " + c, l);
            estado = 12;
          }
          break;

        case 9: // Lexico.Error Identificadores
          if (esCaracterSimple(c) || esEspacio(c)) estado = 0;
          else if (esLetraMayuscula(c))
            generarError("Solo puede haber una letra mayúscula al inicio del identificador: " + c, l);
          else if (c != '_' && !esLetraMinuscula(c) && !esDigito(c))
            generarError("Caracter invalido en un identificador: " + c, l);
          break;

        case 10: // Lexico.Error Números enteros y punto flotante
          // Se ejecuta lo mismo del caso 11:
        case 11: // Lexico.Error Números enteros
          if (esEspacio(c)) {
            this.i = this.f;
            this.f--;
            estado = 0;
          }
          else if (esCaracterSimple(c)) estado = 1;
          else if (c != '.' && !esDigito(c)) generarError("Caracter invalido en un número: " + c, l);
          break;

        case 12: // Lexico.Error Palabras reservadas
          if (esCaracterSimple(c) || esEspacio(c)) estado = 0;
          else if (!esLetraMinuscula(c)) generarError("Caracter invalido en una palabra reservada: " + c, l);
          break;
      }

      if (f == cadena.length() - 1) {
        // Generar token o error segun el estado al llegar al utlimo caracter de la linea
        if (estado == 1)
          token = generarToken(f, f, CategoriasLexicas.CARACTER_SIMPLE);
        else if (estado == 2)
          generarError("Un identificador no puede terminar con _: " + cadena.substring(i, f), l);
        else if (estado == 3)
          token = generarToken(i, (byte) (f + 1), CategoriasLexicas.IDENTIFICADOR);
        else if (estado == 4 || estado == 5)
          token = generarToken(i, (byte) (f + 1), CategoriasLexicas.NUMERO_ENTERO);
        else if (estado == 6)
          generarError("Un número no puede terminar con punto decimal: " + cadena.substring(i, f), l);
        else if (estado == 7)
          token = generarToken(i, (byte) (f + 1), CategoriasLexicas.NUMERO_PUNTO_FLOTANTE);
        else if (estado == 8)
          if (esPalabraReservada(cadena.substring(i, f + 1)))
            token = generarToken(i, (byte) (f + 1), CategoriasLexicas.PALABRA_RESERVADA);
          else {
            generarError("Palabra reservada no identificada: " + cadena.substring(i, f + 1), l);
          }
        else if (estado == 12)
          generarError("Caracter invalido en una palabra reservada: " + cadena.charAt(cadena.length() - 1), l);

        f = i = 0;
        l++;
      } else {
        cadena = lineas.get(l).trim();
        f++;
      }

    }

    return token;
  }

  /**
   * Crear un token dada una subcadena y una categoria lexica, y guardarlo en las listas
   * @param i Apuntador inicial de la subcadena a guardar
   * @param f Apuntador final de la subcadena a guardar
   * @param categoriaLexica Categoría léxico a la que pertenece
   */
  private Token generarToken(byte i, byte f, CategoriasLexicas categoriaLexica) {
    // Si i y f apuntan al mismo caracter, obtener ese caracter
    // Si no, obtener el substring de i hasta f - 1
    String subcadena = i == f ? String.valueOf(cadena.charAt(i)) : cadena.substring(i, f);

    Token token = new Token(subcadena, categoriaLexicaAAtributo(categoriaLexica, subcadena));
    System.out.println("Token generado: " + token);

    // Si es un identificador, guradarlo en la lista de simbolos si aun no existe
    if (categoriaLexica == CategoriasLexicas.IDENTIFICADOR) guardarSimbolo(token);

    estado = 0;
    this.i = this.f; // Mover i hasta donde apunta f
    this.f--; // Retrocedr f porque en la siguiente iteración el for lo aumentará

    return token;
  }

  /**
   * Recibe una categoría lexica y devuelve su atributo correspondiente
   * @param categoriaLexica Categoría léxica
   * @param lexema Texto del token
   * @return Número entero del atributo
   */
  private int categoriaLexicaAAtributo(CategoriasLexicas categoriaLexica, String lexema) {
    return switch (categoriaLexica) {
      case IDENTIFICADOR -> 295;
      case NUMERO_ENTERO -> 296;
      case NUMERO_PUNTO_FLOTANTE -> 297;
      case CARACTER_SIMPLE -> lexema.charAt(0);
      case PALABRA_RESERVADA -> switch (lexema) {
        case "class" -> 300;
        case "float" -> 301;
        case "int" -> 302;
        case "read" -> 303;
        case "write" -> 304;
        default -> 0;
      };
    };
  }

  /**
   * Guarda el error y lo imprime por consola. Maximo puede guardar 20 errores
   * @param mensaje Mensaje de error
   * @param linea Línea donde se produjo el error
   */
  private void generarError(String mensaje, int linea) {
    Error err = new Error(mensaje, linea + 1);
    System.err.println(err);
    this.i = this.f;
  }

  /**
   * Verifica si el indentificador a guardar ya existe en la lista, si no existe lo guarda
   * @param token Lexico.Token del simbolo a guardar
   */
  private void guardarSimbolo(Token token) {
    Nodo<Token> nodoActual = simbolos.getCabeza();
    boolean yaExiste = false;

    while (nodoActual != null) {
      if (nodoActual.getDato().getLexema().equals(token.getLexema())) {
        yaExiste = true;
        break;
      }
      nodoActual = nodoActual.getSiguiente();
    }

    if (!yaExiste) simbolos.agregar(token);
  }

  /**
   * @param c Carácter
   * @return True si es un caracter simple válido
   */
  private boolean esCaracterSimple(char c) {
    boolean encontrado = false;

    for (char caracterSimple: caracteresSimples)
      if (c == caracterSimple) {
        encontrado = true;
        break;
      }

    return encontrado;
  }

  /**
   * @param cadena palabra leída
   * @return True si la palabra es una palabra reservada válida
   */
  private boolean esPalabraReservada(String cadena) {
    boolean encontrado = false;

    for (String palabraReservada: palabrasReservadas)
      if (cadena.equals(palabraReservada)) {
        encontrado = true;
        break;
      }

    return encontrado;
  }

  /**
   * @param c Caracter
   * @return True si el caracter es una letra mayúscula (A - Z)
   */
  private boolean esLetraMayuscula(char c) { return c >= 'A' && c <= 'Z'; }

  /**
   * @param c Caracter
   * @return True si el caracter es una letra minúscula (a - z)
   */
  private boolean esLetraMinuscula(char c) { return c >= 'a' && c <= 'z'; }

  /**
   * @param c Caracter
   * @return True si el caracter es un dígito (0 - 9)
   */
  private boolean esDigito(char c) { return c >= '0' && c <= '9'; }

  /**
   * @param c Caracter
   * @return True si el caracter es un espacio en blanco
   */
  private boolean esEspacio(char c) { return c == ' ' || c == '\t' || c == '\n' || c == '\r'; }

  /**
   * @return Lista de los identificadores
   */
  public Lista<Token> getSimbolos() { return this.simbolos; }
}
