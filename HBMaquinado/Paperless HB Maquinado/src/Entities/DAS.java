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
public class DAS {
    private static DAS instance;
    
    private String soporteRapido, inspector, empleado;
    
    public static DAS getInstance() {
        if (instance == null) {
            instance = DAS.builder().build();
        }
        return instance;
    }
    
    public static void setInstance(DAS das) {
        instance = das;
    }
}
