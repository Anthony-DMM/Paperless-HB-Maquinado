/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.MOGHija;
import Model.CambioMOGModel;
import Utils.LimpiarCampos;
import Utils.MostrarMensaje;
import Utils.Navegador;
import Utils.ValidarCampos;
import View.CambioMOGView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class CambioMOGController implements ActionListener {
    private final CambioMOGModel cambioMOGModel;
    private final CambioMOGView cambioMOGView;
    private final Navegador navegador = Navegador.getInstance();
    MOGHija datosMOGHija = MOGHija.getInstance();
    private static final Logger LOGGER = Logger.getLogger(CambioMOGController.class.getName());

    public CambioMOGController(CambioMOGView cambioMOGView) {
        this.cambioMOGView = cambioMOGView;
        this.cambioMOGModel = new CambioMOGModel();
        addListeners();
        cargarDatos();
    }
    
    private void addListeners() {
        this.cambioMOGView.txtMOG.addActionListener(this);
        this.cambioMOGView.btnConfirmarCambio.addActionListener(this);
        this.cambioMOGView.btnRegresar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == cambioMOGView.txtMOG) {
            handleValidarMOG();
        } else if (source == cambioMOGView.btnConfirmarCambio) {
            handleRegistrarCambio();
        } else if (source == cambioMOGView.btnRegresar) {
            navegador.regresar(cambioMOGView);
        }
    }
    
    private void cargarDatos() {
        List<Object[]> historialCambios = null;
        try {
            historialCambios = cambioMOGModel.obtenerHistorialCambios();
        } catch (SQLException ex) {
            Logger.getLogger(CambioMOGController.class.getName()).log(Level.SEVERE, null, ex);
        }
        actualizarTabla(historialCambios);  
    }
    
    private void actualizarTabla(List<Object[]> historialParos) {
        DefaultTableModel dtm = (DefaultTableModel) cambioMOGView.tblCambios.getModel();
        dtm.setRowCount(0);

        for (Object[] rowData : historialParos) {
            dtm.addRow(rowData);
        }
    }
    
    private void handleValidarMOG() {
        String MOGIngresada = cambioMOGView.txtMOG.getText();
        if (MOGIngresada.isEmpty()) {
            MostrarMensaje.mostrarError("Ingrese la MOG a la que se transferirán las piezas");
        }
        
        try {
            if (cambioMOGModel.buscarMOGExistente(MOGIngresada)) {
                cambioMOGView.txtPiezas.setText(String.valueOf(datosMOGHija.getCantidad_planeada()));
            } else {
                LimpiarCampos.limpiarCampo(cambioMOGView.txtMOG);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener datos de la orden", ex);
        }
    }
    
    private void handleRegistrarCambio() {
        if (cambioMOGView.txtMOG.getText().isEmpty() || cambioMOGView.txtPiezas.getText().isEmpty()) {
            MostrarMensaje.mostrarError("Para registrar el cambio es necesario capturar la información de la MOG de destino");
        } else {
            try {
                int idMOG = cambioMOGModel.insertarDatosMog();
                if (idMOG != 0) {
                    MostrarMensaje.mostrarInfo("Se ha registrado la MOG y el cambio exitosamente");
                    LimpiarCampos.limpiarCampos(cambioMOGView.txtMOG, cambioMOGView.txtPiezas);
                    List<Object[]> historialCambios = cambioMOGModel.obtenerHistorialCambios();
                    actualizarTabla(historialCambios);
                } else {
                    MostrarMensaje.mostrarError("Error al registrar la MOG. Verifique que los datos sean correctos.");
                }
            } catch (SQLException ex) {
                MostrarMensaje.mostrarError("Ocurrió un error al registrar la información. Verifique que los datos sean correctos.");
                Logger.getLogger(CambioMOGController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
