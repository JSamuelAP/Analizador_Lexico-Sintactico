package estructuras;

import java.util.NoSuchElementException;

/**
 * Clase para la pila dinámica
 * @param <T> Tipo de dato que almacenará la pila
 */
public class Pila<T> {
  private Nodo<T> tope;
  private int longitud;

  /**
   * Instanciar una pila vacía
   */
  public Pila() {
    tope = null;
    longitud = 0;
  }

  /**
   * Instanciar una pila con un elemento
   * @param dato Elemento a almacenar
   */
  public Pila(T dato) {
    tope = new Nodo<>(dato, null);
    longitud = 1;
  }

  /**
   * @param dato Elemento a meter a la pila
   */
  public void push(T dato) {
    tope = new Nodo<>(dato, tope);
    longitud++;
  }

  /**
   * Sacar el último elemento ingresado en la pila
   * @return Elemento sacado
   */
  public T pop() {
    if(estaVacia()) throw new NoSuchElementException("La pila está vacía");

    Nodo<T> nodoEliminado = tope;
    tope = tope.getSiguiente();
    nodoEliminado.setSiguiente(null);
    longitud--;

    return nodoEliminado.getDato();
  }

  /**
   * @return El elemento que está en la cima de la pila
   */
  public T getTope() {
    if(estaVacia()) throw new NoSuchElementException("La pila está vacía");

    return this.tope.getDato();
  }

  /**
   * @return True si la pila tiene al menos un elemento
   */
  public boolean estaVacia() {
    return longitud == 0;
  }

  /**
   * @return Número de elementos que contiene la pila
   */
  public int getLongitud() { return longitud; }
}