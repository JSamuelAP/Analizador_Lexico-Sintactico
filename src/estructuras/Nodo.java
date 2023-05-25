package estructuras;

/**
 * Clase para los nodos de las listas y pilas
 * @param <T> Tipo de elemento a almacenar
 */
public class Nodo<T> {
  private T dato;
  private Nodo<T> siguiente;

  /**
   * Instanciar un nodo
   * @param dato Elemento a almacenar
   * @param siguiente Nodo que le sigue a este nodo
   */
  public Nodo(T dato, Nodo<T> siguiente) {
    this.dato = dato;
    this.siguiente = siguiente;
  }

  /**
   * @return Elemento almacenado
   */
  public T getDato() { return this.dato; }

  /**
   * @param dato Nuevo elemento a almacenar
   */
  public void setDato(T dato) { this.dato = dato; }

  /**
   * @return Nodo siguiente
   */
  public Nodo<T> getSiguiente() { return this.siguiente; }

  /**
   * @param siguiente Nuevo nodo al que se apunta
   */
  public void setSiguiente(Nodo<T> siguiente) {
    this.siguiente = siguiente;
  }
}