/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class ValidarCampos {

    public static boolean esCampoVacio(JTextField campo, String mensajeError) {
        if (campo.getText().trim().isEmpty()) {
            mostrarError(mensajeError);
            return true;
        }
        return false;
    }

    public static boolean esCampoVacio(JPasswordField campo, String mensajeError) {
        if (new String(campo.getPassword()).trim().isEmpty()) {
            mostrarError(mensajeError);
            return true;
        }
        return false;
    }
    
    public static void activarCampo(JTextField textField) {
        textField.setEditable(true);
        textField.setFocusable(true);
        textField.setBackground(Color.YELLOW);
    }
    
    public static void activarCampoFocus(JTextField textField) {
        textField.setEditable(true);
        textField.setFocusable(true);
        textField.setBackground(Color.YELLOW);
        textField.requestFocusInWindow();
    }
    
    public static void activarCampos(JTextField... campos) {
        for (JTextField campo : campos) {
            campo.setEditable(true);
            campo.setFocusable(true);
            campo.setBackground(Color.YELLOW);
            campo.requestFocusInWindow();
        }
    }
    
    public static void bloquearCampo(JTextField textField) {
        textField.setEditable(false);
        textField.setFocusable(false);
        textField.setBackground(Color.LIGHT_GRAY);
    }

    private static void mostrarError(String mensajeError) {
        JOptionPane.showMessageDialog(null, mensajeError, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
