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
    String mog, descripcion, no_dibujo, proceso, no_parte;

    public MOG(String mog, String descripcion, String no_dibujo, String proceso, String no_parte) {
        this.mog = mog;
        this.descripcion = descripcion;
        this.no_dibujo = no_dibujo;
        this.proceso = proceso;
        this.no_parte = no_parte;
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

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    public String getNo_parte() {
        return no_parte;
    }

    public void setNo_parte(String no_parte) {
        this.no_parte = no_parte;
    }
    
    
}
