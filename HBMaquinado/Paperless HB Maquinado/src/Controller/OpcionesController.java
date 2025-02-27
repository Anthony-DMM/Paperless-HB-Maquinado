/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.LineaProduccion;
import Utils.Navegador;
import View.CapturaOrdenManufacturaView;
import View.OpcionesView;
import View.RegistroDASView;
import View.RegistroRBPView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class OpcionesController implements ActionListener{
    
    RegistroDASView registroDASView; 
    CapturaOrdenManufacturaView capturaOrdenManufactura;
    OpcionesView opciones;

    public OpcionesController() {
        this.capturaOrdenManufactura = CapturaOrdenManufacturaView.getInstance();
        this.registroDASView = RegistroDASView.getInstance();
        this.opciones = OpcionesView.getInstance();
        
        opciones.getBtn_registrar().addActionListener(this);
        opciones.getBtn_cambiar_modelo().addActionListener(this);
        opciones.getBtn_regresar().addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == opciones.btn_registrar) {
            Navegador.avanzarSiguienteVentana(opciones, registroDASView);
        } else if (e.getSource() == opciones.btn_cambiar_modelo) {
            JOptionPane.showMessageDialog(null, "Opción no disponible por el momento");
        } else if (e.getSource() == opciones.btn_regresar) {
            Navegador.regresarVentanaAnterior(opciones, capturaOrdenManufactura);
        }
    }
    
}
