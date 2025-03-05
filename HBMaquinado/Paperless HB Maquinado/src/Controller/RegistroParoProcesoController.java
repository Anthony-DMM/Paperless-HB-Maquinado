/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

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
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroParoProcesoController implements ActionListener {

    private RegistroParoProcesoModel registroParoProcesoModel;
    private RegistroParoProcesoView registroParoProcesoView;
    private RegistroDASView registroDASView = RegistroDASView.getInstance();
    private FechaHora fechaHora = new FechaHora();
    private Timer timer;
    private Timestamp horaInicio;

    public RegistroParoProcesoController(RegistroParoProcesoModel registroParoProcesoModel, RegistroParoProcesoView registroParoProcesoView) {
        this.registroParoProcesoModel = registroParoProcesoModel;
        this.registroParoProcesoView = registroParoProcesoView;

        this.registroParoProcesoView.btnRegresar.addActionListener(this);
        this.registroParoProcesoView.btnFinalizar.addActionListener(this);

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

            String tiempoTranscurrido = String.format("%02d:%02d:%02d", horas, minutos, segundos);
            registroParoProcesoView.txtTiempo.setText(tiempoTranscurrido);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registroParoProcesoView.btnRegresar) {
            handleRegresar();
        } else if (e.getSource() == registroParoProcesoView.btnFinalizar) {
            handleRegistrarParo();
        }
    }

    private void handleRegistrarParo() {
        if (hayCamposVacios()) {
            MostrarMensaje.mostrarError("Es necesario completar todos los campos para registrar el paro");
        } else {

        }
    }

    private void handleRegresar() {
        Navegador.regresarVentanaAnterior(registroParoProcesoView, registroDASView);
    }

    private boolean hayCamposVacios() {
        return registroParoProcesoView.txtHoraInicio.getText().isEmpty()
                || registroParoProcesoView.txtTiempo.getText().isEmpty()
                || registroParoProcesoView.cboxCategoria.getSelectedItem() == null
                || registroParoProcesoView.cboxCausa.getSelectedItem() == null
                || registroParoProcesoView.cboxAndon.getSelectedItem() == null
                || registroParoProcesoView.cboxNivel.getSelectedItem() == null
                || registroParoProcesoView.txtDetalle.getText().isEmpty();
    }
}
