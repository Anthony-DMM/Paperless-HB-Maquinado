/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.Operador;
import Entities.RBP;
import Model.RegistroRBPModel;
import Utils.FechaHora;
import Utils.LimpiarCampos;
import Utils.MostrarMensaje;
import Utils.Navegador;
import Utils.ValidarCampos;
import View.DibujoView;
import View.ParoProcesoView;
import View.RegistroDASView;
import View.RegistroRBPView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroRBPController implements ActionListener {

    private final RegistroRBPModel registroRBPModel = new RegistroRBPModel();
    private final RegistroRBPView registroRBPView;
    private final RegistroDASView registroDASView = RegistroDASView.getInstance();
    private final RegistroDASController registroDASController = new RegistroDASController(registroDASView);
    private final DibujoView dibujoView = DibujoView.getInstance();
    private final DibujoController dibujoController = DibujoController.getInstance(dibujoView);
    private final Navegador navegador = Navegador.getInstance();
    
    private final FechaHora fechaHora = new FechaHora();
    private LocalTime horaInicio;
    
    private final RBP datosRBP = RBP.getInstance();
    private final Operador datosOperador = Operador.getInstance();
    
    private static final Logger LOGGER = Logger.getLogger(ManufacturaController.class.getName());
    
    public RegistroRBPController(RegistroRBPView registroRBPView) {
        this.registroRBPView = registroRBPView;
        addListeners();
        
        registroRBPView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                cargarDatos();
            }
        });
    }
    
    private void addListeners() {
        agregarAccion(registroRBPView.btnDAS, AccionBoton.DAS);
        agregarAccion(registroRBPView.btnDibujo, AccionBoton.DIBUJO);
        agregarAccion(registroRBPView.btnParoLinea, AccionBoton.PARO_LINEA);
        agregarAccion(registroRBPView.btnCambioMOG, AccionBoton.CAMBIO_MOG);
        agregarAccion(registroRBPView.btnRegresar, AccionBoton.REGRESAR);
        registroRBPView.txtNumeroEmpleado.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleNumeroEmpleadoCapturado();
                }
            }
        });
    }
    
    private void cargarDatos() {
        RBP rbp = RBP.getInstance();
        registroRBPView.txtHoraInicio.setText(rbp.getHora());
        try {
            registroRBPView.txtFecha.setText(fechaHora.fechaActual("dd-MM-yyyy"));
        } catch (SQLException ex) {
            Logger.getLogger(RegistroRBPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public enum AccionBoton {
        DAS,
        PARO_LINEA,
        CAMBIO_MOG,
        DIBUJO,
        REGRESAR
    }

    private void handleButtonAction(AccionBoton accion) {
        switch (accion) {
            case DAS:
                if (new String(registroRBPView.txtNumeroEmpleado.getPassword()).isEmpty() && registroRBPView.txtNombreEmpleado.getText().isEmpty()) {
                    MostrarMensaje.mostrarAdvertencia("Para realizar el llenado del DAS es necesario capturar el número de empleado");
                } else {
                    navegador.avanzar(registroDASView, registroRBPView);
                }
                break;
            case PARO_LINEA:
                if (new String(registroRBPView.txtNumeroEmpleado.getPassword()).isEmpty() && registroRBPView.txtNombreEmpleado.getText().isEmpty()) {
                    MostrarMensaje.mostrarAdvertencia("Para registrar el paro en proceso es necesario capturar el número de empleado");
                } else {
                    ParoProcesoView paroProcesoView = new ParoProcesoView();
                    ParoProcesoController paroProcesoController = new ParoProcesoController(paroProcesoView);
                    navegador.avanzar(paroProcesoView, registroRBPView);
                }
                break;
            case CAMBIO_MOG:
                //navegador.avanzar(registroDASView, registroRBPView);
                break;
            case DIBUJO:
                navegador.avanzar(dibujoView, registroRBPView);
                break;
            case REGRESAR:
                navegador.regresar(registroRBPView);
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton botonOrigen = (JButton) e.getSource();
        AccionBoton accion = AccionBoton.valueOf(botonOrigen.getActionCommand());
        handleButtonAction(accion);
    }

    private void inicializarHoraInicio() {
        try {
            horaInicio = LocalTime.now();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al obtener la hora de inicio", e);
            MostrarMensaje.mostrarError("Error al inicializar la hora de inicio.");
        }
    }

    private void agregarAccion(JButton boton, AccionBoton accion) {
        boton.setActionCommand(accion.toString());
        boton.addActionListener(this);
    }
    
    private void handleNumeroEmpleadoCapturado() {
        char[] passwordChars = registroRBPView.txtNumeroEmpleado.getPassword();
        String numeroEmpleadoIngresado = new String(passwordChars);

        if (numeroEmpleadoIngresado.isEmpty()) {
            MostrarMensaje.mostrarAdvertencia("Es necesario colocar el código de empleado");
            LimpiarCampos.limpiarCampos(registroRBPView.txtNumeroEmpleado, registroRBPView.txtNumeroEmpleado);
        } else {
            try {
                if (registroRBPModel.validarOperador(numeroEmpleadoIngresado)) {
                    String codigoEmpleado = numeroEmpleadoIngresado;
                    datosOperador.setCódigo(codigoEmpleado);
                    registroRBPView.txtNombreEmpleado.setText(datosOperador.getNombre());
                    ValidarCampos.activarCampos(registroRBPView.txtPiezasxFila, registroRBPView.txtFilas, registroRBPView.txtNiveles, registroRBPView.txtCanastas, registroRBPView.txtFilasCompletas, registroRBPView.txtNivelesCompletos, registroRBPView.txtSobrante);
                } else {
                    LimpiarCampos.limpiarCampos(registroRBPView.txtNumeroEmpleado, registroRBPView.txtNumeroEmpleado);
                }
            } catch (SQLException ex) {
                Logger.getLogger(RegistroDASController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
    
    /*private void handleDatosDAS(String codigoSoporte, String codigoInspector, String codigoEmpleado) throws SQLException {
        int DASExistente = registroDASModel.obtenerDASExistente();
        if (DASExistente == 0) {
            registroDASModel.registrarDAS(codigoSoporte, codigoInspector, codigoEmpleado);
        }
    }*/
