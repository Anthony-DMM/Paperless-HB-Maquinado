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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        validarLineaView.getBtnIngresar().addActionListener(this);
        validarLineaView.getBtnSalir().addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == validarLineaView.getBtnIngresar()) {
            validarLinea();
        } else if (e.getSource() == validarLineaView.getBtnSalir()) {
            CerrarAplicacion.cerrar();
        }
    }
    
    private void validarLinea() {
        if (ValidarCampos.esCampoVacio(validarLineaView.getTxtLineaProduccion(), "Debe ingresar la línea de producción")) {
            return;
        }
        
        String lineaProduccion = validarLineaView.getTxtLineaProduccion().getText().trim();
        LineaProduccion linea = validarLineaModel.validarLinea(lineaProduccion, "MAQUINADO");

        if (linea == null) {
            MostrarMensaje.mostrarError("La línea de producción no existe o no pertenece al área de MAQUINADO.");
            LimpiarCampos.limpiarCampo(validarLineaView.getTxtLineaProduccion());
        } else {
            Navegador.avanzarSiguienteVentana(validarLineaView, capturaOrdenManufacturaView);
        }
    }
}