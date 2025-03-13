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
public class Operador {
    private String nombre;
    private String código;
    
    private static Operador instance;
    
    public static Operador getInstance() {
        if (instance == null) {
            instance = Operador.builder().build();
        }
        return instance;
    }

    public static void setInstance(Operador operador) {
        instance = operador;
    }
}
