/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.LineaProduccion;
import Utils.Navegador;
import View.ManufacturaView;
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
    ManufacturaView capturaOrdenManufactura;
    OpcionesView opciones;

    public OpcionesController() {
        this.capturaOrdenManufactura = ManufacturaView.getInstance();
        this.registroDASView = RegistroDASView.getInstance();
        this.opciones = OpcionesView.getInstance();
        
        opciones.getBtnRegistrar().addActionListener(this);
        opciones.getBtnCambiarModelo().addActionListener(this);
        opciones.getBtnRegresar().addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == opciones.btnRegistrar) {
            Navegador.avanzarSiguienteVentana(opciones, registroDASView);
        } else if (e.getSource() == opciones.btnCambiarModelo) {
            JOptionPane.showMessageDialog(null, "Opción no disponible por el momento");
        } else if (e.getSource() == opciones.btnRegresar) {
            Navegador.regresarVentanaAnterior(opciones, capturaOrdenManufactura);
        }
    }
    
}
