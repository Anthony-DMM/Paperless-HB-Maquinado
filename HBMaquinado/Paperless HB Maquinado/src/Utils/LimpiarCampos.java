/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import javax.swing.JTextField;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class LimpiarCampos {
    public static void limpiarCampo(JTextField campo) {
        campo.setText("");
    }

    public static void limpiarCampos(JTextField... campos) {
        for (JTextField campo : campos) {
            campo.setText("");
        }
    }
}
