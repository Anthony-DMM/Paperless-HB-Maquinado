/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.DAS;
import Entities.DetalleParo;
import Entities.MOG;
import Entities.Operador;
import Entities.RBP;
import Interfaces.HoraxHora;
import Interfaces.LineaProduccion;
import Model.PreviaRBPModel;
import Utils.Navegador;
import View.PreviaRBPView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class PreviaRBPController implements ActionListener {
    private final LineaProduccion datosLineaProduccion = LineaProduccion.getInstance();
    private final DAS datosDAS = DAS.getInstance();
    private final RBP datosRBP = RBP.getInstance();
    private final MOG datosMOG = MOG.getInstance();
    private final Operador datosOperador = Operador.getInstance();
    
    private final PreviaRBPView previaRBPView = PreviaRBPView.getInstance();
    private final PreviaRBPModel previaRBPModel = new PreviaRBPModel();
    
    private final Navegador navegador = Navegador.getInstance();
    
    public PreviaRBPController() {
        previaRBPView.btnSiguiente.addActionListener(this);
        previaRBPView.btnRegresar.addActionListener(this);
        
        previaRBPView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                cargarDatos();
            }
        });
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source == previaRBPView.btnSiguiente) {
            //navegador.avanzar(previaRBPView, previaRBPView);
        } else if (source == previaRBPView.btnRegresar) {
            navegador.regresar(previaRBPView);
        }
    }
    
    private void cargarDatos() {
        previaRBPView.lblModelo.setText(datosMOG.getModelo());
        previaRBPView.lblParte.setText(datosMOG.getNo_parte());
        previaRBPView.lblCantidadPlaneada.setText(String.valueOf(datosMOG.getCantidad_planeada()));
        previaRBPView.lblDibujo.setText(datosMOG.getNo_dibujo());
        previaRBPView.lblOrdenRuta.setText(datosMOG.getMog());
        previaRBPView.lblOrdenManufactura.setText(datosMOG.getOrden_manufactura());
        
        List<Object[]> registroPiezasProducidas = null;
        try {
            registroPiezasProducidas = previaRBPModel.obtenerRegistroProduccion();
        } catch (SQLException ex) {
            Logger.getLogger(CambioMOGController.class.getName()).log(Level.SEVERE, null, ex);
        }
        actualizarTabla(registroPiezasProducidas);
    }
    
    private void actualizarTabla(List<Object[]> registroPiezasProducidas) {
        DefaultTableModel dtm = (DefaultTableModel) previaRBPView.tblPiezasProcesadas.getModel();
        dtm.setRowCount(0);

        for (Object[] rowData : registroPiezasProducidas) {
            dtm.addRow(rowData);
        }
    }
}
