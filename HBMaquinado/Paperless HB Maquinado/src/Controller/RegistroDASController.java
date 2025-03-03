/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.DAS;
import Entities.HoraxHora;
import Entities.LineaProduccion;
import Entities.MOG;
import Model.CapturaOrdenManufacturaModel;
import Model.RegistroDASModel;
import Utils.FechaHora;
import Utils.LimpiarCampos;
import Utils.Navegador;
import View.CapturaOrdenManufacturaView;
import View.OpcionesView;
import View.RegistroDASView;
import View.ValidarLineaView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.text.ParseException;
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
    private static final String INVALID_MOG_MESSAGE = "Ingrese una orden de manufactura";
    private static final String INVALID_SOPORTE_RAPIDO_MESSAGE = "Ingrese un código de soporte rápido";
    private static final String INVALID_INSPECTOR_MESSAGE = "Ingrese un código de inspector";
    private static final String INVALID_OPERADOR_MESSAGE = "Ingrese un número de empleado";
    private Timer timer;

    private RegistroDASModel registroDASModel;
    private RegistroDASView registroDASView;
    private FechaHora fechaHora = new FechaHora();
    DAS datosLinea = DAS.getInstance();

    public RegistroDASController(RegistroDASModel registroDASModel, RegistroDASView registroDASView) {
        this.registroDASModel = registroDASModel;
        this.registroDASView = RegistroDASView.getInstance();

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
        String hora = fechaHora.horaActual();
        registroDASView.txtHora.setText(hora);
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
            
            List<HoraxHora> piezas = registroDASModel.obtenerPiezasProcesadasHora();
            actualizarTabla(piezas);
        } catch (SQLException | ParseException ex) {
            System.out.println("Error al cargar los datos: " + ex.getMessage());
            ex.printStackTrace();
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
        registroDASView.btnRegistrarProduccion.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source instanceof JTextField) {
            //handleTextFieldAction((JTextField) source);
        } else if (source instanceof JButton) {
            handleButtonAction((JButton) source);
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
    
    private void handleButtonAction(JButton button) {
        if (button.equals(registroDASView.getBtnRegistrarProduccion())) {
            handleRegistroProduccionButton();
        } else if (button.equals(registroDASView.getBtnFinalizarDAS())) {
            //handleRegresarButton();
        } else if (button.equals(registroDASView.getBtnRegresar())) {
            //handleRegresarButton();
        }
    }
    
    private void handleRegistroProduccionButton() {
        String numero_empleado = registroDASView.txtNumeroEmpleado.getText().trim();

        if (numero_empleado.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El número de empleado no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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

        try {
            // Intentar registrar las piezas
            registroDASModel.registrarPiezasPorHora(numero_empleado, acumulado, calidad);

            // Si el registro es exitoso, limpiar los campos
            LimpiarCampos.limpiarCampos(registroDASView.txtNumeroEmpleado, registroDASView.txtNombreEmpleado, registroDASView.txtAcumulado);
            registroDASView.cbxOK.setSelected(false);
            registroDASView.cbxNG.setSelected(false);

            // Actualizar la tabla con los nuevos datos
            List<HoraxHora> piezas = registroDASModel.obtenerPiezasProcesadasHora();
            actualizarTabla(piezas);

            JOptionPane.showMessageDialog(null, "Registro exitoso.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            // Manejar errores de validación (acumulado menor que el último acumulado)
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException | ParseException ex) {
            // Manejar errores de base de datos o parseo
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Ocurrió un error al registrar la producción.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /*private void handleRegistroProduccionButton() {
        String numero_empleado = registroDASView.txtNumeroEmpleado.getText().trim();

        if (numero_empleado.isEmpty()) {
            JOptionPane.showMessageDialog(null, "El número de empleado no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

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

        try {
            registroDASModel.registrarPiezasPorHora(numero_empleado, acumulado, calidad);
            LimpiarCampos.limpiarCampos(registroDASView.txtNumeroEmpleado, registroDASView.txtNombreEmpleado, registroDASView.txtAcumulado);
            registroDASView.cbxNG.setSelected(false);
            registroDASView.cbxOK.setSelected(false);

            // Actualizar la tabla con los nuevos datos
            List<HoraxHora> piezas = registroDASModel.obtenerPiezasProcesadasHora();
            actualizarTabla(piezas);
        } catch (SQLException | ParseException ex) {
            JOptionPane.showMessageDialog(null, "Ocurrió un error al registrar la producción.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }*/
    
    private void actualizarTabla(List<HoraxHora> piezas) {
        DefaultTableModel dtm = (DefaultTableModel) registroDASView.tblHoraxHora.getModel();
        dtm.setRowCount(0); // Limpiar la tabla

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
                    registroDASView.txtNombreSoporteRapido.setText(datosLinea.getSoporteRapido());
                } else {
                    LimpiarCampos.limpiarCampos(registroDASView.getTxtCodigoSoporte(), registroDASView.getTxtNombreSoporteRapido());
                }
            } catch (Exception ex) {
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
                    registroDASView.txtNombreInspector.setText(datosLinea.getInspector());
                } else {
                    LimpiarCampos.limpiarCampos(registroDASView.getTxtCodigoInspector(), registroDASView.getTxtNombreInspector());
                }
            } catch (Exception ex) {
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
                    registroDASView.txtNombreEmpleado.setText(datosLinea.getEmpleado());
                } else {
                    LimpiarCampos.limpiarCampos(registroDASView.getTxtNumeroEmpleado(), registroDASView.getTxtNombreEmpleado());
                }
            } catch (Exception ex) {
                Logger.getLogger(RegistroDASController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

