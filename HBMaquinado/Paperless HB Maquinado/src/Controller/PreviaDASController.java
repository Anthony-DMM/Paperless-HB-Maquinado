/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.DAS;
import Entities.DetalleParo;
import Entities.Operador;
import Entities.RBP;
import Interfaces.HoraxHora;
import Interfaces.LineaProduccion;
import Model.ParoProcesoModel;
import Model.PreviaDASModel;
import Model.RegistroHoraxHoraModel;
import Utils.Navegador;
import View.PreviaDASView;
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
public class PreviaDASController implements ActionListener{
    private final LineaProduccion datosLineaProduccion = LineaProduccion.getInstance();
    private final DAS datosDAS = DAS.getInstance();
    private final RBP datosRBP = RBP.getInstance();
    private final Operador datosOperador = Operador.getInstance();
    
    private final PreviaDASView previaDASView = PreviaDASView.getInstance();
    private final PreviaDASModel previaDASModel = new PreviaDASModel();
    private final RegistroHoraxHoraModel registroHoraxHoraModel = new RegistroHoraxHoraModel();
    private final ParoProcesoModel paroProcesoModel = new ParoProcesoModel();
    
    private final Navegador navegador = Navegador.getInstance();

    public PreviaDASController() {
        previaDASView.btnSiguiente.addActionListener(this);
        previaDASView.btnRegresar.addActionListener(this);
        
        previaDASView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                cargarDatos();
            }
        });
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        
        if (source == previaDASView.btnSiguiente) {
            navegador.avanzar(previaDASView, previaDASView);
        } else if (source == previaDASView.btnRegresar) {
            navegador.regresar(previaDASView);
        }
    }
    
    private void cargarDatos() {
        previaDASView.lblLinea.setText(datosLineaProduccion.getLinea());
        previaDASView.lblGrupo.setText(String.valueOf(datosLineaProduccion.getGrupo()));

        String fecha = datosRBP.getFecha();
        if (fecha != null && !fecha.isEmpty()) {
            String[] partes = fecha.split("-");
            if (partes.length == 3) {
                previaDASView.lblAnio.setText(partes[0]);
                previaDASView.lblMes.setText(partes[1]);
                previaDASView.lblDia.setText(partes[2]);
            } else {
                previaDASView.lblDia.setText("Formato incorrecto");
            }
        } else {
            previaDASView.lblDia.setText("Fecha no disponible");
        }

        previaDASView.lblTurno.setText(String.valueOf(datosDAS.getTurno()));
        previaDASView.lblNombreKeeper.setText(datosDAS.getNombreSoporteRapido());
        previaDASView.lblNombreOperador.setText(datosOperador.getNombre());
        previaDASView.lblNombreInspector.setText(datosDAS.getNombreInspector());
        previaDASView.lblCantidadPiezasMeta.setText(String.valueOf(datosDAS.getPiezasMeta()));
        
        List<Object[]> registroProduccion1 = null;
        try {
            registroProduccion1 = previaDASModel.obtenerRegistroProduccion();
        } catch (SQLException ex) {
            Logger.getLogger(CambioMOGController.class.getName()).log(Level.SEVERE, null, ex);
            previaDASView.lblCantidadProcesada.setText("Error");
        }
        actualizarTabla(registroProduccion1);
        
        List<Object[]> registroProduccion2 = null;
        try {
            registroProduccion2 = previaDASModel.obtenerRegistroProduccion2();
        } catch (SQLException ex) {
            Logger.getLogger(CambioMOGController.class.getName()).log(Level.SEVERE, null, ex);
        }
        actualizarTabla2(registroProduccion2);
        
        try {
            List<Object[]> registroProduccion3 = previaDASModel.obtenerRegistroProduccion3();
                String pzasProcesadas = (registroProduccion3.get(0)[0] != null) ? registroProduccion3.get(0)[0].toString() : "0";
                String pzasBuenas = (registroProduccion3.get(0)[1] != null) ? registroProduccion3.get(0)[1].toString() : "0";
                String pzasRechazadas = (registroProduccion3.get(0)[2] != null) ? registroProduccion3.get(0)[2].toString() : "0";

                previaDASView.lblCantidadProcesada.setText(pzasProcesadas);
                previaDASView.lblCantidadPzsBuenas.setText(pzasBuenas);
                previaDASView.lblCantidadPzsRechazadas.setText(pzasRechazadas);
                
                float desempenio = (Float.parseFloat(pzasBuenas) / datosDAS.getPiezasMeta()) * 100;
                DecimalFormat df = new DecimalFormat("0.00");
                String desempenioFormateado = df.format(desempenio);
                previaDASView.lblDesempeño.setText(desempenioFormateado+"%");
                actualizarTabla3(registroProduccion3);
        } catch (SQLException ex) {
            Logger.getLogger(PreviaDASController.class.getName()).log(Level.SEVERE, null, ex);
            previaDASView.lblCantidadProcesada.setText("Error SQL");
            previaDASView.lblCantidadPzsBuenas.setText("Error SQL");
            previaDASView.lblDesempeño.setText("Error SQL");
        } catch (NullPointerException ex){
            Logger.getLogger(PreviaDASController.class.getName()).log(Level.SEVERE, null, ex);
            previaDASView.lblCantidadProcesada.setText("Error Null");
            previaDASView.lblCantidadPzsBuenas.setText("Error Null");
            previaDASView.lblDesempeño.setText("Error Null");
        }
        
        List<HoraxHora> registroHoraxHora;
        try {
            registroHoraxHora = registroHoraxHoraModel.obtenerPiezasProcesadasHora();
            actualizarTablaHoraxHora(registroHoraxHora);
        } catch (SQLException ex) {
            Logger.getLogger(PreviaDASController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        List<DetalleParo> historialParos;
        try {
            historialParos = paroProcesoModel.obtenerHistorialParos();
            actualizarTablaParos(historialParos);
        } catch (SQLException ex) {
            Logger.getLogger(PreviaDASController.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
    
    private void actualizarTabla(List<Object[]> registroProduccion1) {
        DefaultTableModel dtm = (DefaultTableModel) previaDASView.tblRegistroProduccion1.getModel();
        dtm.setRowCount(0);

        for (Object[] rowData : registroProduccion1) {
            dtm.addRow(rowData);
        }
    }
    
    private void actualizarTabla2(List<Object[]> registroProduccion2) {
        DefaultTableModel dtm = (DefaultTableModel) previaDASView.tblRegistroProduccion2.getModel();
        dtm.setRowCount(0);

        for (Object[] rowData : registroProduccion2) {
            dtm.addRow(rowData);
        }
    }
    
    private void actualizarTabla3(List<Object[]> registroProduccion3) {
        DefaultTableModel dtm = (DefaultTableModel) previaDASView.tblRegistroProduccion3.getModel();
        dtm.setRowCount(0);

        for (Object[] rowData : registroProduccion3) {
            dtm.addRow(rowData);
        }
    }
    
    private void actualizarTablaHoraxHora(List<HoraxHora> piezas) {
        DefaultTableModel dtm = (DefaultTableModel) previaDASView.tblHoraxHora.getModel();
        dtm.setRowCount(0);

        for (HoraxHora pieza : piezas) {
            Object[] rowData = {
                pieza.getHora(),
                pieza.getPiezasXHora(),
                pieza.getAcumulado(),
                pieza.getOkNg(),
                pieza.getNombre()
            };
            dtm.addRow(rowData);
        }
    }
    
    private void actualizarTablaParos(List<DetalleParo> historialParos) {
        DefaultTableModel dtm = (DefaultTableModel) previaDASView.tblParos.getModel();
        dtm.setRowCount(0);

        for (DetalleParo paro : historialParos) {
            Object[] rowData = {
                paro.getDescripcion(),
                paro.getHora_inicio(),
                paro.getHora_fin(),
                paro.getTiempo(),
                paro.getAndon(),
                paro.getEscalacion(),
                paro.getDetalle()
            };
            dtm.addRow(rowData);
        }
    }
}
