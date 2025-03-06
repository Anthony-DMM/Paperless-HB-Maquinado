/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import javax.swing.JFrame;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class Navegador {
    public static void avanzarSiguienteVentana(JFrame ventanaActual, JFrame ventanaSiguiente) {
        ventanaActual.setVisible(false);
        ventanaSiguiente.setVisible(true);
    }

    public static void regresarVentanaAnterior(JFrame ventanaActual, JFrame ventanaAnterior) {
        ventanaActual.setVisible(false);
        ventanaAnterior.setVisible(true);
    }
    
    public static void avanzarDestruyendoVentana(JFrame ventanaActual, JFrame ventanaSiguiente) {
        ventanaActual.dispose();
        ventanaSiguiente.setVisible(true);
    }
    
    public static void regresarDestruyendoVentana(JFrame ventanaActual, JFrame ventanaAnterior) {
        ventanaActual.dispose();
        ventanaAnterior.setVisible(true);
    }
}