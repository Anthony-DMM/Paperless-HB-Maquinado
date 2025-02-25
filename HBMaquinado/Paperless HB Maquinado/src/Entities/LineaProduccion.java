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
public class LineaProduccion {
    private static LineaProduccion instance;

    private String linea;
    private String proceso;
    private String supervisor;

    // Método estático para obtener la instancia única
    public static LineaProduccion getInstance() {
        if (instance == null) {
            instance = LineaProduccion.builder().build();
        }
        return instance;
    }

    // Método para actualizar la instancia
    public static void setInstance(LineaProduccion lineaProduccion) {
        instance = lineaProduccion;
    }
}