/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Interfaces.DAS;
import Interfaces.HoraxHora;
import Interfaces.MOG;
import Model.RegistroDASModel;
import Utils.FechaHora;
import Utils.LimpiarCampos;
import Utils.MostrarMensaje;
import Utils.Navegador;
import View.DibujoView;
import View.RegistroDASView;
import View.RegistroRBPView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroDASController implements ActionListener, ItemListener {

    private static final String EMPTY_FIELD_MESSAGE = "Por favor, complete todos los campos antes de continuar";

    private final RegistroDASModel registroDASModel;
    private final RegistroDASView registroDASView;
    Navegador navegador = Navegador.getInstance();
    RegistroRBPView registroRBPView = RegistroRBPView.getInstance();
    
    private final DibujoView dibujoView = DibujoView.getInstance();
    private final DibujoController dibujoController = DibujoController.getInstance(dibujoView);
    
    
    private final FechaHora fechaHora = new FechaHora();
    private LocalTime horaInicio;
    private final DAS datosDAS = DAS.getInstance();
    private Timer timer;

    public enum AccionBoton {
        REGISTRAR_PRODUCCION,
        REGRESAR,
        VER_DIBUJO
    }

    public RegistroDASController(RegistroDASView registroDASView) {
        this.registroDASView = RegistroDASView.getInstance();
        this.registroDASModel = new RegistroDASModel();
        inicializarHoraInicio();
        addListeners();
        inicializarTimer();

        registroDASView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                cargarDatos();
            }

            @Override
            public void windowClosing(WindowEvent e) {
                timer.stop();
            }
        });
    }

    private void inicializarHoraInicio() {
        try {
            horaInicio = LocalTime.now();
        } catch (Exception e) {
            Logger.getLogger(RegistroDASController.class.getName()).log(Level.SEVERE, "Error al obtener la hora de inicio", e);
            JOptionPane.showMessageDialog(null, "Error al inicializar la hora de inicio.");
        }
    }

    private void inicializarTimer() {
        timer = new Timer(1000, e -> actualizarHora());
        timer.start();
    }

    private void actualizarHora() {
        try {
            LocalTime horaActual = LocalTime.now();
            registroDASView.txtHora.setText(horaActual.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        } catch (Exception ex) {
            Logger.getLogger(RegistroDASController.class.getName()).log(Level.SEVERE, "Error al actualizar la hora", ex);
        }
    }

    private void cargarDatos() {
        try {
            MOG datosMOG = MOG.getInstance();
            registroDASView.txtFecha.setText(fechaHora.fechaActual("dd-MM-yyyy"));
            registroDASView.txtMOG.setText(datosMOG.getMog());
            registroDASView.txtModelo.setText(datosMOG.getModelo());
            registroDASView.txtSTD.setText(datosMOG.getStd());
            registroDASView.txtLote.setText(datosMOG.getTm());

            actualizarHora();
            turno();

            List<HoraxHora> piezas = registroDASModel.obtenerPiezasProcesadasHora();
            actualizarTabla(piezas);
        } catch (SQLException ex) {
            Logger.getLogger(RegistroDASController.class.getName()).log(Level.SEVERE, "Error al cargar los datos", ex);
            JOptionPane.showMessageDialog(null, "Error al cargar los datos.");
        }
    }

    private void addListeners() {
        addFieldListeners(registroDASView.getTxtCodigoSoporte(), this::handleCodigoSoporteCapturado);
        addFieldListeners(registroDASView.getTxtCodigoInspector(), this::handleCodigoInspectorCapturado);
        addFieldListeners(registroDASView.getTxtNumeroEmpleado(), this::handleNumeroEmpleadoCapturado);

        registroDASView.cbxOK.addItemListener(this);
        registroDASView.cbxNG.addItemListener(this);
        registroDASView.cbxTurno.addItemListener(this);
        registroDASView.btnRegistrarProduccion.addActionListener(this);
        registroDASView.btnRegresar.addActionListener(this);
        registroDASView.btnFinalizarDAS.addActionListener(this);
        registroDASView.btnDibujo.addActionListener(this);
    }

    private void addFieldListeners(JPasswordField passwordField, java.util.function.Consumer<JPasswordField> handler) {
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handler.accept(passwordField);
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            handleButtonAction((JButton) e.getSource());
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == registroDASView.cbxNG) {
            registroDASView.cbxOK.setEnabled(e.getStateChange() != ItemEvent.SELECTED);
        } else if (e.getSource() == registroDASView.cbxOK) {
            registroDASView.cbxNG.setEnabled(e.getStateChange() != ItemEvent.SELECTED);
        }
    }

    private void turno() {
        LocalTime inicioTurno = LocalTime.parse("06:59:59");
        LocalTime finTurno = LocalTime.parse("19:00:00");
        registroDASView.cbxTurno.setSelectedItem(horaInicio.isAfter(inicioTurno) && horaInicio.isBefore(finTurno) ? "1" : "2");
    }

    private void handleButtonAction(JButton button) {
        if (button.equals(registroDASView.btnRegistrarProduccion)) {
            handleRegistroProduccionButton();
        } else if (button.equals(registroDASView.btnRegresar)) {
            handleRegresarButton();
        } else if (button.equals(registroDASView.btnDibujo)) {
            handleVerDibujo();
        } else if (button.equals(registroDASView.btnFinalizarDAS)) {
            try {
                handleFinalizarDASButton();
            } catch (SQLException ex) {
                Logger.getLogger(RegistroDASController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void handleVerDibujo() {
        navegador.avanzar(dibujoView, registroDASView);
    }

    private void handleRegistroProduccionButton() {
        if (areFieldsEmpty()) {
            JOptionPane.showMessageDialog(null, EMPTY_FIELD_MESSAGE);
            return;
        }

        int acumulado;
        try {
            acumulado = Integer.parseInt(registroDASView.txtAcumulado.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "El valor acumulado debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String calidad = registroDASView.cbxOK.isSelected() ? "OK"
                : registroDASView.cbxNG.isSelected() ? "NG" : null;

        if (calidad == null) {
            JOptionPane.showMessageDialog(null, "Debes seleccionar una calidad (OK o NG).", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String numeroEmpleado = registroDASView.txtNumeroEmpleado.getText().trim();
        try {
            registroDASModel.registrarPiezasPorHora(numeroEmpleado, acumulado, calidad);
            LimpiarCampos.limpiarCampos(registroDASView.txtAcumulado);
            registroDASView.cbxOK.setSelected(false);
            registroDASView.cbxNG.setSelected(false);

            List<HoraxHora> piezas = registroDASModel.obtenerPiezasProcesadasHora();
            actualizarTabla(piezas);

            MostrarMensaje.mostrarInfo("El registro se ha realizado correctamente");
        } catch (IllegalArgumentException | IllegalStateException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al registrar la producción.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTabla(List<HoraxHora> piezas) {
        DefaultTableModel dtm = (DefaultTableModel) registroDASView.tblHoraxHora.getModel();
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

    private void handleCodigoSoporteCapturado(JPasswordField passwordField) {
        char[] passwordChars = passwordField.getPassword();
        String codigoSoporteIngresado = new String(passwordChars);

        if (codigoSoporteIngresado.isEmpty()) {
            MostrarMensaje.mostrarAdvertencia("Es necesario colocar el código de soporte rápido");
            LimpiarCampos.limpiarCampos(registroDASView.getTxtCodigoSoporte(), registroDASView.getTxtNombreSoporteRapido());
        } else {
            try {
                if (registroDASModel.validarSoporteRapido(codigoSoporteIngresado)) {
                    String codigoSoporte = codigoSoporteIngresado;
                    datosDAS.setCodigoSoporteRapido(codigoSoporte);
                    registroDASView.txtNombreSoporteRapido.setText(datosDAS.getNombreSoporteRapido());
                } else {
                    LimpiarCampos.limpiarCampos(registroDASView.getTxtCodigoSoporte(), registroDASView.getTxtNombreSoporteRapido());
                }
            } catch (SQLException ex) {
                Logger.getLogger(RegistroDASController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void handleCodigoInspectorCapturado(JPasswordField passwordField) {
        char[] passwordChars = passwordField.getPassword();
        String codigoInspectorIngresado = new String(passwordChars);

        if (codigoInspectorIngresado.isEmpty()) {
            MostrarMensaje.mostrarAdvertencia("Es necesario colocar el código de inspector");
            LimpiarCampos.limpiarCampos(registroDASView.getTxtCodigoInspector(), registroDASView.getTxtNombreInspector());
        } else {
            try {
                if (registroDASModel.validarInspector(codigoInspectorIngresado)) {
                    String codigoInspector = codigoInspectorIngresado;
                    datosDAS.setCodigoInspector(codigoInspector);
                    registroDASView.txtNombreInspector.setText(datosDAS.getNombreInspector());
                } else {
                    LimpiarCampos.limpiarCampos(registroDASView.getTxtCodigoInspector(), registroDASView.getTxtNombreInspector());
                }
            } catch (SQLException ex) {
                Logger.getLogger(RegistroDASController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void handleNumeroEmpleadoCapturado(JPasswordField passwordField) {
        char[] passwordChars = passwordField.getPassword();
        String numeroEmpleadoIngresado = new String(passwordChars);

        if (numeroEmpleadoIngresado.isEmpty()) {
            MostrarMensaje.mostrarAdvertencia("Es necesario colocar el código de empleado");
            LimpiarCampos.limpiarCampos(registroDASView.getTxtNumeroEmpleado(), registroDASView.getTxtNombreEmpleado());
        } else {
            try {
                if (registroDASModel.validarOperador(numeroEmpleadoIngresado)) {
                    String codigoEmpleado = numeroEmpleadoIngresado;
                    datosDAS.setCodigoEmpleado(codigoEmpleado);
                    registroDASView.txtNombreEmpleado.setText(datosDAS.getNombreEmpleado());
                } else {
                    LimpiarCampos.limpiarCampos(registroDASView.getTxtNumeroEmpleado(), registroDASView.getTxtNombreEmpleado());
                }
            } catch (SQLException ex) {
                Logger.getLogger(RegistroDASController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private boolean areFieldsEmpty() {
        char[] codigoSoporte = registroDASView.getTxtCodigoSoporte().getPassword();
        String codigoSoporteIngresado = new String(codigoSoporte);

        char[] codigoInspector = registroDASView.getTxtCodigoInspector().getPassword();
        String codigoInspectorIngresado = new String(codigoInspector);

        char[] codigoEmpleado = registroDASView.getTxtNumeroEmpleado().getPassword();
        String codigoEmpleadoIngresado = new String(codigoEmpleado);

        return codigoSoporteIngresado.isEmpty()
                || codigoInspectorIngresado.isEmpty()
                || codigoEmpleadoIngresado.isEmpty();
    }

    private void handleFinalizarDASButton() throws SQLException {
        int opcion = JOptionPane.showConfirmDialog(null, "¿Deseas finalizar el DAS? Recuerda que esta acción no se puede revertir");  
        if (opcion == JOptionPane.YES_OPTION) {
            navegador.avanzar(registroRBPView, registroDASView);
        }  
    }
    
    private void handleRegresarButton() {
        navegador.regresar(registroDASView);
    }
}
