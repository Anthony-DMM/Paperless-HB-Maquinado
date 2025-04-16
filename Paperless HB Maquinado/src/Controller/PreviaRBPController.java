/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Components.JTableConEstilos;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

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
        previaRBPModel.obtenerTotalesRBP();
        previaRBPView.lblModelo.setText(datosMOG.getModelo());
        previaRBPView.lblParte.setText(datosMOG.getNo_parte());
        previaRBPView.lblCantidadPlaneada.setText(String.valueOf(datosMOG.getCantidad_planeada()));
        previaRBPView.lblDibujo.setText(datosMOG.getNo_dibujo());
        previaRBPView.lblOrdenRuta.setText(datosMOG.getMog());
        previaRBPView.lblOrdenManufactura.setText(datosMOG.getOrden_manufactura());
        previaRBPView.lblPiezasProcesadas.setText(String.valueOf(datosRBP.getPiezasProcesadas()));
        previaRBPView.lblPiezasRecibidas.setText(String.valueOf(datosRBP.getPiezasRecibidas()));
        previaRBPView.lblPiezasWcCompletos.setText(String.valueOf(datosRBP.getPiezasWcCompletos()));
        previaRBPView.lblWcCompletos.setText(String.valueOf(datosRBP.getWcCompletos()));
        previaRBPView.lblPiezasWcIncompletos.setText(String.valueOf(datosRBP.getPiezasWcIncompletos()));
        previaRBPView.lblCambioMOG.setText(String.valueOf(datosRBP.getPiezasCambioMOG()));
        
        int totalPiezasAprobadas = (datosRBP.getPiezasWcCompletos() * datosRBP.getWcCompletos()) + datosRBP.getPiezasWcIncompletos();
        int totalValidacionDiferencia = datosRBP.getPiezasProcesadas() - totalPiezasAprobadas - datosRBP.getScrap() - datosRBP.getPiezasCambioMOG();
        int totalDiferenciaPiezas = datosRBP.getPiezasRecibidas() - datosRBP.getPiezasProcesadas();
        
        previaRBPView.lblPiezasAprobadas.setText(String.valueOf(totalPiezasAprobadas));
        previaRBPView.lblValidacionDiferencia.setText(String.valueOf(totalValidacionDiferencia));
        previaRBPView.lblDiferenciaPiezas.setText(String.valueOf(totalDiferenciaPiezas));
        
        
        List<Object[]> registroPiezasProducidas = null;
        try {
            registroPiezasProducidas = previaRBPModel.obtenerRegistroProduccion();
        } catch (SQLException ex) {
            Logger.getLogger(CambioMOGController.class.getName()).log(Level.SEVERE, null, ex);
        }
        actualizarTabla(registroPiezasProducidas);
        
        List<Map<String, Object>> registroTarjetasDinamicas = null;
        try {
            registroTarjetasDinamicas = previaRBPModel.obtenerTarjetasProcesadas();
        } catch (SQLException ex) {
            Logger.getLogger(CambioMOGController.class.getName()).log(Level.SEVERE, null, ex);
        }
        actualizarTablaTarjetas(registroTarjetasDinamicas);
    }
    
    private void actualizarTabla(List<Object[]> registroPiezasProducidas) {
        DefaultTableModel dtm = (DefaultTableModel) previaRBPView.tblPiezasProcesadas.getModel();
        dtm.setRowCount(0);

        for (Object[] rowData : registroPiezasProducidas) {
            dtm.addRow(rowData);
        }
    }
    
    private void actualizarTablaTarjetas(List<Map<String, Object>> data) {
        DefaultTableModel dtm = (DefaultTableModel) previaRBPView.tblTarjetasProcesadas.getModel();
        JTable tablaTarjetas = previaRBPView.tblTarjetasProcesadas;
        dtm.setRowCount(0);

        if (data == null || data.isEmpty()) {
            return;
        }

        // Obtener los nombres de las columnas del primer mapa y ordenarlos numéricamente
        Map<String, Object> firstRow = data.get(0);
        List<String> columnNamesList = new ArrayList<>(firstRow.keySet());
        columnNamesList.sort((s1, s2) -> {
            try {
                int n1 = extraerNumeroColumna(s1);
                int n2 = extraerNumeroColumna(s2);
                return Integer.compare(n1, n2);
            } catch (NumberFormatException e) {
                return s1.compareTo(s2);
            }
        });
        String[] columnNames = columnNamesList.toArray(new String[0]);
        dtm.setColumnIdentifiers(columnNames);

        // Crear una instancia del TableCellRenderer personalizado
        JTableConEstilos boldNumberRenderer = new JTableConEstilos();

        // Obtener el TableColumnModel para iterar sobre las columnas
        TableColumnModel columnModel = tablaTarjetas.getColumnModel();

        // Aplicar el renderer a cada columna
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setCellRenderer(boldNumberRenderer);
        }

        // Llenar el modelo con los datos y añadir filas de "X" condicionalmente
        for (Map<String, Object> rowData : data) {
            Object[] numberRow = new Object[columnNames.length];
            for (int i = 0; i < columnNames.length; i++) {
                numberRow[i] = rowData.get(columnNames[i]);
            }
            dtm.addRow(numberRow);

            Object[] xRow = new Object[columnNames.length];
            for (int i = 0; i < columnNames.length; i++) {
                if (numberRow[i] != null) {
                    xRow[i] = "X";
                } else {
                    xRow[i] = null;
                }
            }
            dtm.addRow(xRow);
        }

        tablaTarjetas.setRowHeight(35);
    }

    // (El método extraerNumeroColumna permanece igual)
    private int extraerNumeroColumna(String columnName) {
        String numberPart = columnName.substring(columnName.indexOf("_") + 1);
        return Integer.parseInt(numberPart);
    }
}
