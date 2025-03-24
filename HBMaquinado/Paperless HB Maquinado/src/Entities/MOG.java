/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
@Data
@Builder
public class MOG {
    private static MOG instance;

    private String mog,
            modelo,
            orden_manufactura,
            descripcion,
            no_dibujo,
            no_parte,
            std,
            tm,
            lote;
    private double peso;
    private int cantidad_planeada, sequ;

    public static MOG getInstance() {
        if (instance == null) {
            instance = MOG.builder().build();
        }
        return instance;
    }

    public static void setInstance(MOG mog) {
        instance = mog;
    }
}
