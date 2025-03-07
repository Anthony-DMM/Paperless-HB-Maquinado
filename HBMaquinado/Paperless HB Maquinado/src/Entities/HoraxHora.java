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
public class HoraxHora {

    private static HoraxHora instance;
    private String hora;
    private int piezasXHora;
    private int acumulado;
    private String okNg;
    private String nombre;

    public static HoraxHora getInstance() {
        if (instance == null) {
            instance = HoraxHora.builder().build();
        }
        return instance;
    }

    public static void setInstance(HoraxHora horaxHora) {
        instance = horaxHora;
    }
}
