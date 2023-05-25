package estructuras;

/**
 * Clase para la lista dinámica
 * @param <T> Tipo de dato a almacenar
 */
public class Lista<T> {
  private Nodo<T> cabeza;
  private Nodo<T> cola;
  private byte longitud;

  /**
   * Instanciar una lista vacía
   */
  public Lista() {
    cabeza = cola = null;
    longitud = 0;
  }

  /**
   * @param dato Elemento a agregar a la lista
   */
  public void agregar(T dato) {
    Nodo<T> nuevoNodo = new Nodo<>(dato, null);

    if(estaVacia()) cabeza = cola = nuevoNodo;
    else {
      cola.setSiguiente(nuevoNodo);
      cola = nuevoNodo;
    }
    
    longitud++;
  }

  /**
   * @return True si la pila está vacía
   */
  public boolean estaVacia() { return longitud == 0; }

  /**
   * @return Nodo inicial de la pila
   */
  public Nodo<T> getCabeza() { return cabeza; }

  /**
   * @return Número de elementos de la lista
   */
  public byte getLongitud() { return longitud; }
}