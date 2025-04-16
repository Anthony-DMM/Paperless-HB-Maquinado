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
public class ParoProceso {

    private static ParoProceso instance;

    String descripcion, categoria, descripcion_andon;
    int idcausas_paro, numero_causas_paro, id_andon, id_nivel, nivel;

    private List<ParoProceso> listaCausas;
    private List<ParoProceso> listaCategorias;
    private List<ParoProceso> listaAndones;
    private List<ParoProceso> listaNiveles;

    public static ParoProceso getInstance() {
        if (instance == null) {
            instance = ParoProceso.builder().build();
        }
        return instance;
    }

    public static void setInstance(ParoProceso paroProceso) {
        instance = paroProceso;
    }
}
