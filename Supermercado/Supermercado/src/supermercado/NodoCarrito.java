/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermercado;

/**
 *
 * @author Antho
 */
public class NodoCarrito {
    Carrito dato;
    NodoCarrito siguiente;

    public NodoCarrito(Carrito dato) {
        this.dato = dato;
        siguiente = null;
    }

    public Carrito getDato() {
        return dato;
    }

    public void setDato(Carrito dato) {
        this.dato = dato;
    }

    public NodoCarrito getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(NodoCarrito siguiente) {
        this.siguiente = siguiente;
    }
    
}
