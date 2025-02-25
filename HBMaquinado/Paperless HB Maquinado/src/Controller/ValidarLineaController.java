/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.LineaProduccion;
import Model.ValidarLineaModel;
import Model.DBConexion;
import Utils.CerrarAplicacion;
import Utils.LimpiarCampos;
import Utils.MostrarMensaje;
import Utils.Navegador;
import Utils.ValidarCampos;
import View.ValidarLineaView;
import View.CapturaOrdenManufacturaView;
import View.OpcionesView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author ANTHONY-MARTINEZ
 */

public class ValidarLineaController implements ActionListener {
    ValidarLineaView validarLineaView;
    ValidarLineaModel validarLineaModel;
    CapturaOrdenManufacturaView capturaOrdenManufacturaView;

    public ValidarLineaController(ValidarLineaView validarLineaView, ValidarLineaModel validarLineaModel, CapturaOrdenManufacturaView capturaOrdenManufacturaView, DBConexion conexion) {
        this.validarLineaView = ValidarLineaView.getInstance();
        this.validarLineaModel = validarLineaModel;
        this.capturaOrdenManufacturaView = capturaOrdenManufacturaView;

        validarLineaView.getBtn_ingresar().addActionListener(this);
        validarLineaView.getBtn_salir().addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == validarLineaView.getBtn_ingresar()) {
            validarLinea();
        } else if (e.getSource() == validarLineaView.getBtn_salir()) {
            CerrarAplicacion.cerrar();
        }
    }
    
    private void validarLinea() {
        if (ValidarCampos.esCampoVacio(validarLineaView.getTxt_linea_produccion(), "Debe ingresar la línea de producción")) {
            return;
        }
        
        String lineaProduccion = validarLineaView.getTxt_linea_produccion().getText().trim();
        LineaProduccion linea = validarLineaModel.validarLinea(lineaProduccion, "MAQUINADO");

        if (linea == null) {
            MostrarMensaje.mostrarError("La línea de producción no existe o no pertenece al área de MAQUINADO.");
        } else {
            // Navegar a la siguiente ventana
            Navegador.avanzarSiguienteVentana(validarLineaView, capturaOrdenManufacturaView);
        }
        
        LimpiarCampos.limpiarCampo(validarLineaView.getTxt_linea_produccion());
    }
}