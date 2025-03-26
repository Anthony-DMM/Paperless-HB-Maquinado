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
public class PiezasProducidas {
    int idRegistro, piezasTotales, piezasBuenas, piezasRechazadas;
    
    private static PiezasProducidas instance;
    
    public static PiezasProducidas getInstance() {
        if (instance == null) {
            instance = PiezasProducidas.builder().build();
        }
        return instance;
    }

    public static void setInstance(PiezasProducidas piezasProducidas) {
        instance = piezasProducidas;
    }
}
