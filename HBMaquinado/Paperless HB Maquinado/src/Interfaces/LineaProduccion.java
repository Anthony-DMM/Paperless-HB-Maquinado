/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
@Data
@Builder
public class LineaProduccion {

    private static LineaProduccion instance;

    private String linea;
    private String proceso;
    private String supervisor;

    public static LineaProduccion getInstance() {
        if (instance == null) {
            instance = LineaProduccion.builder().build();
        }
        return instance;
    }

    public static void setInstance(LineaProduccion lineaProduccion) {
        instance = lineaProduccion;
    }
}
