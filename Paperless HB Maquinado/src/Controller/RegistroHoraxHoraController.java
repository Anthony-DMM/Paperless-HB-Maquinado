/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.Operador;
import Entities.DAS;
import Interfaces.HoraxHora;
import Interfaces.LineaProduccion;
import Entities.MOG;
import Model.DASModel;
import Model.RegistroHoraxHoraModel;
import Utils.FechaHora;
import Utils.LimpiarCampos;
import Utils.MostrarMensaje;
import Utils.Navegador;
import View.DibujoView;
import View.RegistroHoraxHoraView;
import View.RegistroRBPView;
import java.awt.Color;
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
public class RegistroHoraxHoraController implements ActionListener, ItemListener {

    private final RegistroHoraxHoraModel registroDASModel;
    private final RegistroHoraxHoraView registroDASView;
    private final DASModel dasModel;
    Navegador navegador = Navegador.getInstance();
    RegistroRBPView registroRBPView = RegistroRBPView.getInstance();
    
    private final DibujoView dibujoView = DibujoView.getInstance();
    private final DibujoController dibujoController = DibujoController.getInstance(dibujoView);
    
    
    private final FechaHora fechaHora = new FechaHora();
    private LocalTime horaInicio;
    private final DAS datosDAS = DAS.getInstance();
    private final LineaProduccion lineaProduccion = LineaProduccion.getInstance();
    private Timer timer;

    public enum AccionBoton {
        REGISTRAR_PRODUCCION,
        REGRESAR,
        VER_DIBUJO
    }

    public RegistroHoraxHoraController(RegistroHoraxHoraView registroDASView) {
        this.registroDASView = RegistroHoraxHoraView.getInstance();
        this.registroDASModel = new RegistroHoraxHoraModel();
        this.dasModel = new DASModel();
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
            Logger.getLogger(RegistroHoraxHoraController.class.getName()).log(Level.SEVERE, "Error al obtener la hora de inicio", e);
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
            Logger.getLogger(RegistroHoraxHoraController.class.getName()).log(Level.SEVERE, "Error al actualizar la hora", ex);
        }
    }

    private void cargarDatos() {
        try {
            MOG datosMOG = MOG.getInstance();
            registroDASView.txtFecha.setText(fechaHora.fechaActual("dd-MM-yyyy"));
            registroDASView.txtMOG.setText(datosMOG.getMog());
            registroDASView.txtModelo.setText(datosMOG.getModelo());
            registroDASView.txtSTD.setText(datosMOG.getStd());
            actualizarHora();
            
            registroDASView.txtGrupoMaquina.setText(String.valueOf(lineaProduccion.getGrupo()));
        
            List<HoraxHora> piezas = registroDASModel.obtenerPiezasProcesadasHora();
            actualizarTabla(piezas);
        } catch (SQLException ex) {
            Logger.getLogger(RegistroHoraxHoraController.class.getName()).log(Level.SEVERE, "Error al cargar los datos", ex);
            JOptionPane.showMessageDialog(null, "Error al cargar los datos.");
        }
    }

    private void addListeners() {
        addFieldListeners(registroDASView.getTxtCodigoSoporte(), this::handleCodigoSoporteCapturado);
        addFieldListeners(registroDASView.getTxtCodigoInspector(), this::handleCodigoInspectorCapturado);

        registroDASView.cbxOK.addItemListener(this);
        registroDASView.cbxNG.addItemListener(this);
        
        registroDASView.btnRegistrarProduccion.addActionListener(this);
        registroDASView.btnRegresar.addActionListener(this);
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

    

    private void handleButtonAction(JButton button) {
        if (button.equals(registroDASView.btnRegistrarProduccion)) {
            handleRegistroProduccionButton();
        } else if (button.equals(registroDASView.btnRegresar)) {
            handleRegresarButton();
        } else if (button.equals(registroDASView.btnDibujo)) {
            handleVerDibujo();
        }
    }
    
    private void handleVerDibujo() {
        navegador.avanzar(dibujoView, registroDASView);
    }

    private void handleRegistroProduccionButton() {
        
        if (registroDASView.txtPiezasMeta.getText().isEmpty()) {
            MostrarMensaje.mostrarError("Favor de capturar la cantidad de piezas meta");
            return;
        }
        
        char[] codigoInspector = registroDASView.getTxtCodigoInspector().getPassword();
        String codigoInspectorIngresado = new String(codigoInspector);
        if (codigoInspectorIngresado.isEmpty() || registroDASView.txtNombreInspector.getText().isEmpty()) {
            MostrarMensaje.mostrarError("Favor de capturar los datos del inspector");
            return;
        }
        
        if (registroDASView.txtLote.getText().isEmpty()) {
            MostrarMensaje.mostrarError("Favor de capturar el lote");
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
        
        int piezasMeta = Integer.parseInt(registroDASView.txtPiezasMeta.getText());
        String lote = registroDASView.txtLote.getText();
        
        try {
            registroDASModel.registrarPiezasPorHora(codigoInspectorIngresado, acumulado, calidad, piezasMeta, lote);
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
                Logger.getLogger(RegistroHoraxHoraController.class.getName()).log(Level.SEVERE, null, ex);
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
                Logger.getLogger(RegistroHoraxHoraController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private boolean areFieldsEmpty() {
        char[] codigoSoporte = registroDASView.getTxtCodigoSoporte().getPassword();
        String codigoSoporteIngresado = new String(codigoSoporte);

        char[] codigoInspector = registroDASView.getTxtCodigoInspector().getPassword();
        String codigoInspectorIngresado = new String(codigoInspector);

        return codigoSoporteIngresado.isEmpty()
                || codigoInspectorIngresado.isEmpty();
    }
    
    private void handleRegresarButton() {
        navegador.regresar(registroDASView);
    }
}
