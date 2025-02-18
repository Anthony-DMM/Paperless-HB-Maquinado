/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class MOG {
    String mog, descripcion, no_dibujo, no_parte, std;
    int cantidad_planeada;

    public MOG(String mog, String descripcion, String no_dibujo, String no_parte, String std, int cantidad_planeada) {
        this.mog = mog;
        this.descripcion = descripcion;
        this.no_dibujo = no_dibujo;
        this.no_parte = no_parte;
        this.std = std;
        this.cantidad_planeada = cantidad_planeada;
    }

    public String getMog() {
        return mog;
    }

    public void setMog(String mog) {
        this.mog = mog;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNo_dibujo() {
        return no_dibujo;
    }

    public void setNo_dibujo(String no_dibujo) {
        this.no_dibujo = no_dibujo;
    }

    public String getNo_parte() {
        return no_parte;
    }

    public void setNo_parte(String no_parte) {
        this.no_parte = no_parte;
    }

    public String getStd() {
        return std;
    }

    public void setStd(String std) {
        this.std = std;
    }

    public int getCantidad_planeada() {
        return cantidad_planeada;
    }

    public void setCantidad_planeada(int cantidad_planeada) {
        this.cantidad_planeada = cantidad_planeada;
    }
    
}
