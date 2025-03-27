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
import View.ManufacturaView;
import View.ValidarLineaView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class ValidarLineaController implements ActionListener {

    private final ValidarLineaView validarLineaView;
    private final ValidarLineaModel validarLineaModel = new ValidarLineaModel();
    private final Navegador navegador = Navegador.getInstance();
    private final ManufacturaView manufacturaView = ManufacturaView.getInstance();
    private final ManufacturaController manufacturaController = new ManufacturaController();

    public ValidarLineaController(ValidarLineaView validarLineaView) {
        this.validarLineaView = validarLineaView;
        configurarEventos();
    }

    private void configurarEventos() {
        validarLineaView.btnSalir.addActionListener(this);
        validarLineaView.txtLineaProduccion.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) validarLinea();
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == validarLineaView.btnSalir) CerrarAplicacion.cerrar();
    }

    private void validarLinea() {
        if (ValidarCampos.esCampoVacio(validarLineaView.txtLineaProduccion, "Ingrese una línea de producción")) return;

        String lineaProduccion = validarLineaView.txtLineaProduccion.getText().trim();
        LineaProduccion linea = validarLineaModel.validarLinea(lineaProduccion, "MAQUINADO");

        if (linea == null) {
            MostrarMensaje.mostrarError("La línea de producción no existe o no pertenece al área de MAQUINADO.");
            LimpiarCampos.limpiarCampo(validarLineaView.txtLineaProduccion);
            return;
        }
        navegador.avanzar(manufacturaView, validarLineaView);
        LimpiarCampos.limpiarCampo(validarLineaView.txtLineaProduccion);
    }
}