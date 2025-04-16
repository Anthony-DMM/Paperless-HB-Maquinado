/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entities;

import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
@Data
@Builder
public class DetalleParo {
    private static DetalleParo instance;
    
    String descripcion, hora_inicio, hora_fin, andon, escalacion, detalle;
    int tiempo;

    public static DetalleParo getInstance() {
        if (instance == null) {
            instance = DetalleParo.builder().build();
        }
        return instance;
    }

    public static void setInstance(DetalleParo detalleParo) {
        instance = detalleParo;
    }
}
