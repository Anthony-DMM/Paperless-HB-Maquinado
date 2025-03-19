/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.DAS;
import Entities.DetalleParo;
import Entities.Operador;
import Entities.ParoProceso;
import Model.DASModel;
import Model.ParoProcesoModel;
import Utils.FechaHora;
import Utils.MostrarMensaje;
import Utils.Navegador;
import Utils.ValidarCampos;
import View.ParoProcesoManualView;
import View.RegistroDASView;
import View.ParoProcesoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class ParoProcesoController implements ActionListener {

    private final ParoProcesoModel registroParoProcesoModel = new ParoProcesoModel();
    private final ParoProcesoView registroParoProcesoView;
    private final RegistroDASView registroDASView = RegistroDASView.getInstance();
    private final DASModel dasModel;
    
    Navegador navegador = Navegador.getInstance();

    private FechaHora fechaHora = new FechaHora();
    private Timer timer;
    private Timestamp horaInicio;
    String tiempoTranscurrido;
    ParoProceso datosParoProceso = ParoProceso.getInstance();
    private final Map<String, Integer> causasMap = new HashMap<>();
    private final Map<String, Integer> andonesMap = new HashMap<>();
    private final Map<String, Integer> nivelesMap = new HashMap<>();
    
    private static final Logger LOGGER = Logger.getLogger(ParoProcesoController.class.getName());
    
    private final DAS datosDAS = DAS.getInstance();

    public ParoProcesoController(ParoProcesoView registroParoProcesoView) {
        this.registroParoProcesoView = registroParoProcesoView;
        this.dasModel = new DASModel();

        this.registroParoProcesoView.btnCancelar.addActionListener(this);
        this.registroParoProcesoView.btnFinalizar.addActionListener(this);
        this.registroParoProcesoView.cboxCategoria.addActionListener(this);
        this.registroParoProcesoView.btnParoManual.addActionListener(this);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    actualizarTiempoTranscurrido();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error al actualizar el tiempo transcurrido", ex);
                }
            }
        }, 0, 1000);

        registroParoProcesoView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                try {
                    horaInicio = fechaHora.obtenerTimestampActual();
                    cargarDatos();
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error al cargar datos o obtener hora de inicio", ex);
                }
            }

            @Override
            public void windowClosing(WindowEvent e) {
                timer.cancel();
            }
        });
    }

    private void cargarDatos() throws SQLException {
        String horaInicioFormateada = fechaHora.horaActual();
        registroParoProcesoView.txtHoraInicio.setText(horaInicioFormateada);
        registroParoProcesoView.txtTiempo.setText("00:00:00");

        if (registroParoProcesoModel.obtenerCategoriasParoProceso()) {
            datosParoProceso = ParoProceso.getInstance();

            datosParoProceso.getListaCategorias().forEach(paro -> {
                registroParoProcesoView.cboxCategoria.addItem(paro.getCategoria());
            });
        }
        
        if (registroParoProcesoModel.obtenerAndonPorProceso()) {
            datosParoProceso = ParoProceso.getInstance();

            datosParoProceso.getListaAndones().forEach(paro -> {
                registroParoProcesoView.cboxAndon.addItem(paro.getDescripcion_andon());
                andonesMap.put(paro.getDescripcion_andon(), paro.getId_andon());
            });
        }
        
        if (registroParoProcesoModel.obtenerNivelMaquinado()) {
            datosParoProceso = ParoProceso.getInstance();

            datosParoProceso.getListaNiveles().forEach(paro -> {
                registroParoProcesoView.cboxNivel.addItem(String.valueOf(paro.getNivel()));
                nivelesMap.put(String.valueOf(paro.getNivel()), paro.getId_nivel());
            });
        }
        if(dasModel.buscarDASExistente(datosDAS.getTurno())){
            List<DetalleParo> historialParos = registroParoProcesoModel.obtenerHistorialParos();
            actualizarTabla(historialParos);
        }
            
    }

    private void actualizarTiempoTranscurrido() throws SQLException {
        Timestamp horaActualTimestamp = fechaHora.obtenerTimestampActual();

        if (horaInicio != null && horaActualTimestamp != null) {
            LocalDateTime horaInicioLocal = horaInicio.toLocalDateTime();
            LocalDateTime horaActualLocal = horaActualTimestamp.toLocalDateTime();

            Duration duracion = Duration.between(horaInicioLocal, horaActualLocal);

            long segundosTotales = duracion.getSeconds();
            long horas = segundosTotales / 3600;
            long minutos = (segundosTotales % 3600) / 60;
            long segundos = segundosTotales % 60;

            tiempoTranscurrido = String.format("%02d:%02d:%02d", horas, minutos, segundos);
            registroParoProcesoView.txtTiempo.setText(tiempoTranscurrido);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registroParoProcesoView.btnCancelar) {
            Navegador.getInstance().regresar(registroParoProcesoView);
        } else if (e.getSource() == registroParoProcesoView.btnFinalizar) {
            try {
                handleRegistrarParo();
            } catch (SQLException ex) {
                Logger.getLogger(ParoProcesoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getSource() == registroParoProcesoView.cboxCategoria) {
            try {
                handleCategoriaSeleccionada();
            } catch (SQLException ex) {
                Logger.getLogger(ParoProcesoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getSource() == registroParoProcesoView.btnParoManual) {
            handleParoManual();
        }
    }

    private void handleRegistrarParo() throws SQLException {
        if (hayCamposVacios()) {
            MostrarMensaje.mostrarError("Es necesario completar todos los campos para registrar el paro");
        } else {
            int minutosTranscurridos = obtenerMinutosTranscurridos(tiempoTranscurrido);
            if (minutosTranscurridos < 0) {
                MostrarMensaje.mostrarInfo("El paro en proceso no se registrará ya que su duración es menor a 5 minutos");
                Navegador.getInstance().regresar(registroParoProcesoView);
            } else {
                String causaSeleccionada = (String) registroParoProcesoView.cboxCausa.getSelectedItem();
                int idCausaSeleccionada = causasMap.get(causaSeleccionada);
                String detalleCausa = registroParoProcesoView.txtDetalle.getText();
                
                Integer idAndonSeleccionado = 0;
                Integer idNivelSeleccionado = 0;

                String andonSeleccionado = (String) registroParoProcesoView.cboxAndon.getSelectedItem();
                if (andonSeleccionado != null) {
                    idAndonSeleccionado = andonesMap.get(andonSeleccionado);
                }

                String nivelSeleccionado = (String) registroParoProcesoView.cboxNivel.getSelectedItem();
                if (nivelSeleccionado != null) {
                    idNivelSeleccionado = nivelesMap.get(nivelSeleccionado);
                }
                
                String horaFin = fechaHora.horaActual();
                String horaInicioFormateada = fechaHora.timestampToString(horaInicio, "HH:mm:ss");
                
                if(!dasModel.buscarDASExistente(datosDAS.getTurno())) {
                    Operador datosOperador = Operador.getInstance();
                    dasModel.registrarDAS(datosOperador.getCódigo(), datosOperador.getCódigo(), datosOperador.getCódigo(), datosDAS.getTurno());
                    
                }
                registroParoProcesoModel.registrarParoProceso(idCausaSeleccionada, minutosTranscurridos, detalleCausa, horaInicioFormateada, horaFin, idAndonSeleccionado, idNivelSeleccionado);
                List<DetalleParo> historialParos = registroParoProcesoModel.obtenerHistorialParos();
                actualizarTabla(historialParos);
                MostrarMensaje.mostrarInfo("Se ha registrado el paro en proceso");
            }
        }
    }

    private void handleParoManual() {
        ParoProcesoManualView paroProcesoManualView = new ParoProcesoManualView();
        ParoProcesoManualController paroProcesoManualController = new ParoProcesoManualController(paroProcesoManualView);
        navegador.avanzar(paroProcesoManualView, registroParoProcesoView);
        navegador.getHistorial().remove(registroParoProcesoView);
    }
    
    private void handleCategoriaSeleccionada() throws SQLException {
        if (!"Seleccionar categoria".equals(registroParoProcesoView.cboxCategoria.getSelectedItem())) {
            String categoria = (String) registroParoProcesoView.cboxCategoria.getSelectedItem();

            registroParoProcesoView.cboxCausa.removeAllItems();
            causasMap.clear();

            if (registroParoProcesoModel.obtenerCausasPorCategoriaParoProceso(categoria)) {
                datosParoProceso.getListaCausas().forEach(paro -> {
                    registroParoProcesoView.cboxCausa.addItem(paro.getDescripcion());
                    causasMap.put(paro.getDescripcion(), paro.getIdcausas_paro());
                });
            }
        } else {
            registroParoProcesoView.cboxCausa.removeAllItems();
        }
    }

    private int obtenerMinutosTranscurridos(String tiempoTranscurrido) {
        if (tiempoTranscurrido != null && !tiempoTranscurrido.isEmpty()) {
            String[] partes = tiempoTranscurrido.split(":");
            if (partes.length == 3) {
                try {
                    return Integer.parseInt(partes[1]);
                } catch (NumberFormatException e) {
                    return 0;
                }
            }
        }
        return 0;
    }
    
    private void actualizarTabla(List<DetalleParo> historialParos) {
        DefaultTableModel dtm = (DefaultTableModel) registroParoProcesoView.tblParos.getModel();
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

    private boolean hayCamposVacios() {
        return registroParoProcesoView.txtHoraInicio.getText().isEmpty()
                || registroParoProcesoView.txtTiempo.getText().isEmpty()
                || registroParoProcesoView.cboxCategoria.getSelectedItem() == null
                || registroParoProcesoView.cboxCausa.getSelectedItem() == null
                || registroParoProcesoView.txtDetalle.getText().isEmpty();
    }
}
