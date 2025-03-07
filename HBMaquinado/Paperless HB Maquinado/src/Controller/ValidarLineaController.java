/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Interfaces.LineaProduccion;
import Model.ValidarLineaModel;
import Utils.CerrarAplicacion;
import Utils.LimpiarCampos;
import Utils.MostrarMensaje;
import Utils.Navegador;
import Utils.ValidarCampos;
import View.ValidarLineaView;
import View.ManufacturaView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class ValidarLineaController implements ActionListener {

    ValidarLineaView validarLineaView;
    ValidarLineaModel validarLineaModel = new ValidarLineaModel();

    public ValidarLineaController(ValidarLineaView validarLineaView) {
        this.validarLineaView = validarLineaView;

        this.validarLineaView.getBtnIngresar().addActionListener(this);
        this.validarLineaView.getBtnSalir().addActionListener(this);
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
            ManufacturaView manufacturaView = ManufacturaView.getInstance();
            ManufacturaController manufacturaController = new ManufacturaController(manufacturaView);
            Navegador.avanzarSiguienteVentana(validarLineaView, manufacturaView);
        }
    }
}
