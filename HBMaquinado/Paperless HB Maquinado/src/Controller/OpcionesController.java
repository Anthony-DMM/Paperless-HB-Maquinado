/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.LineaProduccion;
import View.CapturaOrdenManufacturaView;
import View.OpcionesView;
import View.RegistroRBPView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class OpcionesController implements ActionListener{
    
    RegistroRBPView registroRBP;
    CapturaOrdenManufacturaView capturaOrdenManufactura;
    OpcionesView opciones;

    public OpcionesController() {
        this.registroRBP = RegistroRBPView.getInstance();
        this.capturaOrdenManufactura = CapturaOrdenManufacturaView.getInstance();
        this.opciones = OpcionesView.getInstance();
        
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
