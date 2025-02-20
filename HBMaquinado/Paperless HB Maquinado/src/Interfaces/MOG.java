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
public class MOG {
    String mog, modelo, orden_manufactura, descripcion, no_dibujo, no_parte, std, tm;
    double peso;
    int cantidad_planeada, sequ;
}
