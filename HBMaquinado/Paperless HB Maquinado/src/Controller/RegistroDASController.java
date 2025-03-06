/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.DAS;
import Entities.HoraxHora;
import Entities.MOG;
import Model.RegistroDASModel;
import Utils.FechaHora;
import Utils.LimpiarCampos;
import Utils.MostrarMensaje;
import Utils.Navegador;
import View.OpcionesView;
import View.RegistroDASView;
import View.RegistroParoProcesoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroDASController implements ActionListener, ItemListener {
    private static final String EMPTY_FIELD_MESSAGE = "Por favor, complete todos los campos antes de continuar";
    private static final String INVALID_SOPORTE_RAPIDO_MESSAGE = "Ingrese un código de soporte rápido";
    private static final String INVALID_INSPECTOR_MESSAGE = "Ingrese un código de inspector";
    private static final String INVALID_OPERADOR_MESSAGE = "Ingrese un número de empleado";
    private Timer timer;

    private final RegistroDASModel registroDASModel;
    private final RegistroDASView registroDASView;
    private final OpcionesView opcionesView = OpcionesView.getInstance();
    private final FechaHora fechaHora = new FechaHora();
    String horaActual, horaInicio, codigoSoporte, codigoInspector, codigoEmpleado;
    DAS datosDAS = DAS.getInstance();

    public RegistroDASController(RegistroDASModel registroDASModel, RegistroDASView registroDASView) throws SQLException {
        this.registroDASModel = registroDASModel;
        this.registroDASView = RegistroDASView.getInstance();
        horaInicio = fechaHora.horaActual();
        addListeners();
        
        timer = new Timer(1000, e -> {
            try {
                actualizarHora();
            } catch (SQLException ex) {
                Logger.getLogger(RegistroDASController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        timer.start();
        
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
    
    private void actualizarHora() throws SQLException {
        horaActual = fechaHora.horaActual();
        registroDASView.txtHora.setText(horaActual);
    }
    
    private void cargarDatos() {
        try {
            MOG datosMOG = MOG.getInstance();
            String fechaString = fechaHora.fechaActual("dd-MM-yyyy");
            Date fechaDate = fechaHora.stringToDate(fechaString, "dd-MM-yyyy");
            registroDASView.jdcFecha.setDate(fechaDate);
            registroDASView.txtMOG.setText(datosMOG.getMog());
            registroDASView.txtModelo.setText(datosMOG.getModelo());
            registroDASView.txtSTD.setText(datosMOG.getStd());
            registroDASView.txtLote.setText(datosMOG.getTm());

            actualizarHora();
            turno();
            
            List<HoraxHora> piezas = registroDASModel.obtenerPiezasProcesadasHora();
            actualizarTabla(piezas);
        } catch (SQLException | ParseException ex) {
            System.out.println("Error al cargar los datos: " + ex.getMessage());
        }
    }

    private void addListeners() {
        registroDASView.getTxtCodigoSoporte().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleCodigoSoporteCapturado((JPasswordField) e.getSource());
                }
            }
        });
        registroDASView.getTxtCodigoInspector().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleCodigoInspectorCapturado((JPasswordField) e.getSource());
                }
            }
        });
        registroDASView.getTxtNumeroEmpleado().addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleNumeroEmpleadoCapturado((JPasswordField) e.getSource());
                }
            }
        });
        registroDASView.cbxOK.addItemListener(this);
        registroDASView.cbxNG.addItemListener(this);
        registroDASView.cbxTurno.addItemListener(this);
        registroDASView.btnRegistrarProduccion.addActionListener(this);
        registroDASView.btnParoProceso.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source instanceof JTextField) {
            //handleTextFieldAction((JTextField) source);
        } else if (source instanceof JButton) {
            try {
                try {
                    handleButtonAction((JButton) source);
                } catch (SQLException ex) {
                    Logger.getLogger(RegistroDASController.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (ParseException ex) {
                Logger.getLogger(RegistroDASController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void itemStateChanged(ItemEvent e) {
       if (e.getSource() == registroDASView.cbxNG){
           if (e.getStateChange() == ItemEvent.SELECTED){
               registroDASView.cbxOK.setEnabled(false);
            }else{
               registroDASView.cbxOK.setEnabled(true); 
           } 
       }else if(e.getSource() == registroDASView.cbxOK){
            if (e.getStateChange() == ItemEvent.SELECTED){
               registroDASView.cbxNG.setEnabled(false);
            }else{
               registroDASView.cbxNG.setEnabled(true); 
           }
       }
    }
    
    private void turno() {
        LocalTime horaCapturada = LocalTime.parse(horaInicio);
        LocalTime inicioTurno = LocalTime.parse("06:59:59");
        LocalTime finTurno = LocalTime.parse("19:00:00");
        if(horaCapturada.isAfter(inicioTurno) && horaCapturada.isBefore(finTurno)){
            registroDASView.cbxTurno.setSelectedItem("1");
        } else {
            registroDASView.cbxTurno.setSelectedItem("2");
        }
    }
    
    private void handleButtonAction(JButton button) throws ParseException, SQLException {
        if (button.equals(registroDASView.getBtnRegistrarProduccion())) {
            handleRegistroProduccionButton();
        } else if (button.equals(registroDASView.getBtnParoProceso())) {
            handleParoProcesoButton();
        } else if (button.equals(registroDASView.getBtnFinalizarDAS())) {
            //handleRegresarButton();
        } else if (button.equals(registroDASView.getBtnRegresar())) {
            handleRegresarButton();
        }
    }
    
    private void handleRegistroProduccionButton() throws ParseException {

        if (areFieldsEmpty()) {
            JOptionPane.showMessageDialog(null, EMPTY_FIELD_MESSAGE);
        } else {
            int acumulado;
            try {
                acumulado = Integer.parseInt(registroDASView.txtAcumulado.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "El valor acumulado debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String calidad;
            if (registroDASView.cbxOK.isSelected()) {
                calidad = "OK";
            } else if (registroDASView.cbxNG.isSelected()) {
                calidad = "NG";
            } else {
                JOptionPane.showMessageDialog(null, "Debes seleccionar una calidad (OK o NG).", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String numero_empleado = registroDASView.txtNumeroEmpleado.getText().trim();
            try {
                handleDatosDAS(codigoSoporte, codigoInspector, codigoEmpleado);
                registroDASModel.registrarPiezasPorHora(numero_empleado, acumulado, calidad);
                LimpiarCampos.limpiarCampos(registroDASView.txtNumeroEmpleado, registroDASView.txtNombreEmpleado, registroDASView.txtAcumulado);
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
            JOptionPane.showMessageDialog(null, INVALID_SOPORTE_RAPIDO_MESSAGE);
            LimpiarCampos.limpiarCampos(registroDASView.getTxtCodigoSoporte(), registroDASView.getTxtNombreSoporteRapido());
        } else {
            try {
                if (registroDASModel.validarSoporteRapido(codigoSoporteIngresado)) {
                    codigoSoporte = codigoSoporteIngresado;
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
            JOptionPane.showMessageDialog(null, INVALID_INSPECTOR_MESSAGE);
            LimpiarCampos.limpiarCampos(registroDASView.getTxtCodigoInspector(), registroDASView.getTxtNombreInspector());
        } else {
            try {
                if (registroDASModel.validarInspector(codigoInspectorIngresado)) {
                    codigoInspector = codigoInspectorIngresado;
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
            JOptionPane.showMessageDialog(null, INVALID_OPERADOR_MESSAGE);
            LimpiarCampos.limpiarCampos(registroDASView.getTxtNumeroEmpleado(), registroDASView.getTxtNombreEmpleado());
        } else {
            try {
                if (registroDASModel.validarOperador(numeroEmpleadoIngresado)) {
                    codigoEmpleado = numeroEmpleadoIngresado;
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

        return codigoSoporteIngresado.isEmpty() ||
               codigoInspectorIngresado.isEmpty() ||
               codigoEmpleadoIngresado.isEmpty();
    }
    
    private void handleDatosDAS(String codigoSoporte, String codigoInspector, String codigoEmpleado) throws SQLException {
        int DASExistente = registroDASModel.obtenerDASExistente();
        if (DASExistente == 0){
            registroDASModel.registrarDAS(codigoSoporte, codigoInspector, codigoEmpleado);
        }
    }
    
    private void handleParoProcesoButton() throws SQLException, ParseException {
        if(areFieldsEmpty()){
            MostrarMensaje.mostrarError("Para continuar necesario colocar el código de inspector, soporte rápido y empleado");
        } else {
            handleDatosDAS(codigoSoporte, codigoInspector, codigoEmpleado);
            RegistroParoProcesoView paroProcesoView = new RegistroParoProcesoView();
            RegistroParoProcesoController paroProcesoController = new RegistroParoProcesoController(paroProcesoView);
            Navegador.avanzarSiguienteVentana(registroDASView, paroProcesoView);
        }
    }
    
    private void handleRegresarButton() {
        Navegador.regresarVentanaAnterior(registroDASView, opcionesView);
    }
}

