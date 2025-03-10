/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.util.Stack;
import javax.swing.JFrame;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class Navegador {

    private static Stack<JFrame> historial = new Stack<>();
    private static JFrame ventanaActual;

    public static void inicializar(JFrame primeraVentana) {
        ventanaActual = primeraVentana;
        historial.push(primeraVentana);
    }

    public static void avanzarSiguienteVentana(JFrame ventanaSiguiente) {
        if (ventanaActual != null) {
            ventanaActual.setVisible(false);
        }
        ventanaSiguiente.setVisible(true);
        ventanaActual = ventanaSiguiente;
        historial.push(ventanaSiguiente);
    }

    public static void regresarVentanaAnterior() {
        if (!historial.isEmpty()) {
            historial.pop();
            if (!historial.isEmpty()) {
                JFrame ventanaAnterior = historial.peek();
                ventanaActual.dispose();
                ventanaAnterior.setVisible(true);
                ventanaActual = ventanaAnterior;
            }
        }
    }
}