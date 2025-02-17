/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermercado;

import java.util.Vector;

/**
 *
 * @author Antho
 */
public class Carrito {
    NodoProducto productos = null;
    Producto ultimo;

    public NodoProducto getProductos() {
        return productos;
    }

    public void setProductos(NodoProducto productos) {
        this.productos = productos;
    }

    public NodoProducto push (NodoProducto p, Producto dato){
        if (p==null) {
            NodoProducto nuevo = new NodoProducto(dato); 
            p = nuevo;
        }else{
            p.setSiguiente(push(p.getSiguiente(),dato));
        }
        return p;
    }
    
    public NodoProducto pop (NodoProducto p){
        if (p.getSiguiente()==null) {
            ultimo = p.getDato();
            p = null;
        }else{
            p.setSiguiente(pop(p.getSiguiente()));
        }
        return p;
    }
    
    public int count (NodoProducto p){
        if (p==null) {
            return 0;
        }else{
            return 1 + count(p.getSiguiente());
        }
    }

    public String toString(NodoProducto p){
        if(p == null){
            return "";
        }else{
            return p.getDato()+" "+toString(p.getSiguiente());
        }
    } 
    
    public Vector<Producto> toArray(NodoProducto p, Vector<Producto> prod){
        
      if(p!=null){   
          prod.add(p.getDato());
          toArray(p.getSiguiente(), prod);
      }
      return prod;
    }
}
