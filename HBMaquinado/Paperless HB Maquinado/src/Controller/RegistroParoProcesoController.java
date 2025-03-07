/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Interfaces.ParoProceso;
import Model.RegistroParoProcesoModel;
import Utils.FechaHora;
import Utils.MostrarMensaje;
import Utils.Navegador;
import View.RegistroDASView;
import View.RegistroParoProcesoView;
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
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroParoProcesoController implements ActionListener {

    private final RegistroParoProcesoModel registroParoProcesoModel = new RegistroParoProcesoModel();
    private final RegistroParoProcesoView registroParoProcesoView;
    private final RegistroDASView registroDASView = RegistroDASView.getInstance();

    private FechaHora fechaHora = new FechaHora();
    private Timer timer;
    private Timestamp horaInicio;
    String tiempoTranscurrido;
    ParoProceso datosParoProceso = ParoProceso.getInstance();
    private final Map<String, Integer> causasMap = new HashMap<>();

    public RegistroParoProcesoController(RegistroParoProcesoView registroParoProcesoView) throws SQLException, ParseException {
        this.registroParoProcesoView = registroParoProcesoView;

        this.registroParoProcesoView.btnFinalizar.addActionListener(this);
        this.registroParoProcesoView.cboxCategoria.addActionListener(this);

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    actualizarTiempoTranscurrido();
                } catch (SQLException ex) {
                    Logger.getLogger(RegistroParoProcesoController.class.getName()).log(Level.SEVERE, null, ex);
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
                    Logger.getLogger(RegistroParoProcesoController.class.getName()).log(Level.SEVERE, null, ex);
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
        if (e.getSource() == registroParoProcesoView.btnFinalizar) {
            try {
                handleRegistrarParo();
            } catch (SQLException ex) {
                Logger.getLogger(RegistroParoProcesoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getSource() == registroParoProcesoView.cboxCategoria) {
            try {
                handleCategoriaSeleccionada();
            } catch (SQLException ex) {
                Logger.getLogger(RegistroParoProcesoController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void handleRegistrarParo() throws SQLException {
        if (hayCamposVacios()) {
            MostrarMensaje.mostrarError("Es necesario completar todos los campos para registrar el paro");
        } else {
            int minutosTranscurridos = obtenerMinutosTranscurridos(tiempoTranscurrido);
            if (minutosTranscurridos < 5) {
                MostrarMensaje.mostrarInfo("El paro en proceso no se registrará ya que su duración es menor a 5 minutos");
                Navegador.regresarDestruyendoVentana(registroParoProcesoView, registroDASView);
            } else {
                String causaSeleccionada = (String) registroParoProcesoView.cboxCausa.getSelectedItem();
                String detalleCausa = registroParoProcesoView.txtDetalle.getText();
                int idCausaSeleccionada = causasMap.get(causaSeleccionada);
                String horaFin = fechaHora.horaActual();
                String horaInicioFormateada = fechaHora.timestampToString(horaInicio, "HH:mm:ss");
                registroParoProcesoModel.registrarParoProceso(idCausaSeleccionada, minutosTranscurridos, detalleCausa, horaInicioFormateada, horaFin);
                MostrarMensaje.mostrarInfo("Se ha registrado el paro en proceso");
                Navegador.regresarDestruyendoVentana(registroParoProcesoView, registroDASView);
            }
        }
    }

    private void handleCategoriaSeleccionada() throws SQLException {
        if (!"Seleccionar categoria".equals(registroParoProcesoView.cboxCategoria.getSelectedItem())) {
            String categoria = (String) registroParoProcesoView.cboxCategoria.getSelectedItem();

            registroParoProcesoView.cboxCausa.removeAllItems();
            causasMap.clear(); // Limpiar el mapa antes de llenarlo nuevamente

            if (registroParoProcesoModel.obtenerCausasPorCategoriaParoProceso(categoria)) {
                datosParoProceso.getListaCausas().forEach(paro -> {
                    registroParoProcesoView.cboxCausa.addItem(paro.getDescripcion());
                    causasMap.put(paro.getDescripcion(), paro.getIdcausas_paro()); // Almacenar la relación descripción -> ID
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

    private boolean hayCamposVacios() {
        return registroParoProcesoView.txtHoraInicio.getText().isEmpty()
                || registroParoProcesoView.txtTiempo.getText().isEmpty()
                || registroParoProcesoView.cboxCategoria.getSelectedItem() == null
                || registroParoProcesoView.cboxCausa.getSelectedItem() == null
                || registroParoProcesoView.txtDetalle.getText().isEmpty();
    }
}
