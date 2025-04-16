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
public class RBP {

    private static RBP instance;
    private int id, piezasProcesadas, piezasRecibidas, piezasWcCompletos, wcCompletos, piezasWcIncompletos, piezasCambioMOG, scrap;
    private int idRegistroPiezas, piezasFila, filas, niveles, canastas, nivelesCompletos, filasCompletas, piezasSobrantes;
    private String hora, fecha;

    public static RBP getInstance() {
        if (instance == null) {
            instance = RBP.builder().build();
        }
        return instance;
    }

    public static void setInstance(RBP rbp) {
        instance = rbp;
    }
}
