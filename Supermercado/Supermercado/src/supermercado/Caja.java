/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermercado;

/**
 *
 * @author Antho
 */
public class Caja {
    Carrito primero;
    NodoCarrito carritos = null;

    public NodoCarrito getCarritos() {
        return carritos;
    }

    public void setCarritos(NodoCarrito carritos) {
        this.carritos = carritos;
    }
    
    public NodoCarrito push (NodoCarrito c, Carrito dato){
        if (c==null) {
            NodoCarrito nuevo = new NodoCarrito(dato); 
            c = nuevo;
        }else{
            c.setSiguiente(push(c.getSiguiente(),dato));
        }
        return c;
    }
    
    public NodoCarrito pop (NodoCarrito p){
        if (p.getSiguiente()!=null) {
            primero = p.getDato();
            p = p.getSiguiente();
        }else{
            p = null;
        }
        return p;
    }
    
    public int count (NodoCarrito p){
        if (p==null) {
            return 0;
        }else{
            return 1 + count(p.getSiguiente());
        }
    }

    public String toString(NodoCarrito p){
        if(p == null){
            return "";
        }else{
            return p.getDato()+" "+toString(p.getSiguiente());
        }
    }
}
