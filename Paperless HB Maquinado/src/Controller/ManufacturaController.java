/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Interfaces.LineaProduccion;
import Entities.MOG;
import Model.ManufacturaModel;
import Utils.LimpiarCampos;
import Utils.MostrarMensaje;
import Utils.Navegador;
import Utils.ValidarCampos;
import View.DibujoView;
import View.ManufacturaView;
import View.RegistroRBPView;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManufacturaController implements ActionListener {

    private static final Logger LOGGER = Logger.getLogger(ManufacturaController.class.getName());

    private final ManufacturaView manufacturaView = ManufacturaView.getInstance();
    private final ManufacturaModel manufacturaModel = new ManufacturaModel();
    private final RegistroRBPView registroRBPView = RegistroRBPView.getInstance();
    private final RegistroRBPController registroRBPController = new RegistroRBPController();

    private final Navegador navegador = Navegador.getInstance();
    private final LineaProduccion datosLineaProduccion = LineaProduccion.getInstance();

    public ManufacturaController() {
        addListeners();
    }

    private void addListeners() {
        manufacturaView.btnSiguiente.addActionListener(this);
        manufacturaView.btnRegresar.addActionListener(this);
        manufacturaView.btnCorregir.addActionListener(this);

        manufacturaView.txtCodigoSupervisor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleCodigoSupervisorCapturado();
                }
            }
        });
        
        manufacturaView.txtMogCapturada.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleMogCapturada();
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source == manufacturaView.btnSiguiente) {
            handleSiguienteButton();
        } else if (source == manufacturaView.btnRegresar) {
            navegador.regresar(manufacturaView);
            handleCorregirDatos();
        } else if (source == manufacturaView.btnCorregir) {
            handleCorregirDatos();
        }
    }

    private void handleMogCapturada() {
        String ordenIngresada = manufacturaView.txtMogCapturada.getText().trim();
        if (ordenIngresada.isEmpty()) {
            MostrarMensaje.mostrarError("Ingrese una orden de manufactura");
            return;
        }

        if (manufacturaModel.obtenerDatosOrden(ordenIngresada)) {
            ValidarCampos.bloquearCampo(manufacturaView.txtMogCapturada);
            llenarCamposMOG();
        } else {
            limpiarCamposMOG();
        }
    }

    private void handleCodigoSupervisorCapturado() {
        String codigoIngresado = new String(manufacturaView.getTxtCodigoSupervisor().getPassword()).trim();

        if (codigoIngresado.isEmpty()) {
            MostrarMensaje.mostrarError("Ingrese un código de supervisor");
            return;
        }

        if (manufacturaModel.validarSupervisor(codigoIngresado)) {
            manufacturaView.txtSupervisorAsignado.setText(datosLineaProduccion.getSupervisor());
            ValidarCampos.activarCampoFocus(manufacturaView.txtMogCapturada);
            ValidarCampos.bloquearCampo(manufacturaView.txtCodigoSupervisor);
        } else {
            LimpiarCampos.limpiarCampo(manufacturaView.txtCodigoSupervisor);
            LimpiarCampos.limpiarCampo(manufacturaView.txtSupervisorAsignado);
        }
    }

    private void handleCorregirDatos() {
        LimpiarCampos.limpiarCampos(manufacturaView.txtCodigoSupervisor, manufacturaView.txtSupervisorAsignado);
        limpiarCamposMOG();
        ValidarCampos.activarCampoFocus(manufacturaView.txtCodigoSupervisor);
        ValidarCampos.bloquearCampo(manufacturaView.txtMogCapturada);
    }

    private void handleSiguienteButton() {
        if (areFieldsEmpty()) {
            MostrarMensaje.mostrarError("Por favor, complete todos los campos para continuar");
            return;
        }
        
        manufacturaModel.ejecutarTransacciones();
        navegador.avanzar(registroRBPView, manufacturaView);
        handleCorregirDatos();
    }

    private boolean areFieldsEmpty() {
        return new String(manufacturaView.getTxtCodigoSupervisor().getPassword()).trim().isEmpty()
                || manufacturaView.getTxtMogCapturada().getText().trim().isEmpty()
                || manufacturaView.getTxtSupervisorAsignado().getText().trim().isEmpty()
                || manufacturaView.getTxtMog().getText().trim().isEmpty()
                || manufacturaView.getTxtModelo().getText().trim().isEmpty()
                || manufacturaView.getTxtCantidadPlaneada().getText().trim().isEmpty()
                || manufacturaView.getTxtDibujo().getText().trim().isEmpty()
                || manufacturaView.getTxtParte().getText().trim().isEmpty()
                || manufacturaView.getTxtProceso().getText().trim().isEmpty();
    }

    private void llenarCamposMOG() {
        MOG datosMOG = MOG.getInstance();
        LineaProduccion datosLinea = LineaProduccion.getInstance();

        manufacturaView.txtMog.setText(datosMOG.getMog());
        manufacturaView.txtModelo.setText(datosMOG.getModelo());
        manufacturaView.txtDibujo.setText(datosMOG.getNo_dibujo());
        manufacturaView.txtCantidadPlaneada.setText(String.valueOf(datosMOG.getCantidad_planeada()));
        manufacturaView.txtParte.setText(datosMOG.getNo_parte());
        manufacturaView.txtProceso.setText(datosLinea.getProceso());
    }

    private void limpiarCamposMOG() {
        LimpiarCampos.limpiarCampos(manufacturaView.getTxtMogCapturada(),
                manufacturaView.txtMog,
                manufacturaView.txtModelo,
                manufacturaView.txtCantidadPlaneada,
                manufacturaView.txtDibujo,
                manufacturaView.txtParte,
                manufacturaView.txtProceso);
    }
}
