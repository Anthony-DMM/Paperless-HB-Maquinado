/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Interfaces.LineaProduccion;
import View.Captura_Orden_Manufactura;
import View.Opciones;
import View.Registro_RBP;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class Opciones_Controller implements ActionListener{
    
    Registro_RBP registroRBP;
    Captura_Orden_Manufactura capturaOrdenManufactura;
    Opciones opciones;

    public Opciones_Controller() {
        this.registroRBP = Registro_RBP.getInstance();
        this.capturaOrdenManufactura = Captura_Orden_Manufactura.getInstance();
        this.opciones = Opciones.getInstance();
        
        opciones.getBtn_registrar().addActionListener(this);
        opciones.getBtn_cambiar_modelo().addActionListener(this);
        opciones.getBtn_regresar().addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == opciones.btn_registrar) {
            registroRBP.setVisible(true);
            opciones.setVisible(false);
        } else if (e.getSource() == opciones.btn_cambiar_modelo) {
            JOptionPane.showMessageDialog(null, "Opción no disponible por el momento");
        } else if (e.getSource() == opciones.btn_regresar) {
            capturaOrdenManufactura.setVisible(true);
            opciones.setVisible(false);
        }
    }
    
}
