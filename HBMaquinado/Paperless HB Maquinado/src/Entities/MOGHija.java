/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MOGHija extends MOG {
    private static MOGHija instance;
    private int idMogHija;

    public MOGHija(String mog, String modelo, String orden_manufactura, String descripcion, String no_dibujo, String no_parte, String std, String tm, String lote, double peso, int cantidad_planeada, int sequ, String atributoPropio, int idMogHija) {
        super(mog, modelo, orden_manufactura, descripcion, no_dibujo, no_parte, std, tm, lote, peso, cantidad_planeada, sequ);
        this.idMogHija = idMogHija;
    }

    public static MOGHija getInstance() {
        if (instance == null) {
            instance = new MOGHija("valorMog", "valorModelo", "valorOrden", "valorDescripcion", "valorDibujo", "valorParte", "valorStd", "valorTm", "", 0.0, 0, 0, "valorPropio", 0);
        }
        return instance;
    }

    public static void setInstance(MOGHija mogHija) {
        instance = mogHija;
    }
}