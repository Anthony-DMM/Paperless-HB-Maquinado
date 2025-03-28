/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.DAS;
import Entities.Operador;
import Entities.RBP;
import Model.DASModel;
import Model.RegistroRBPModel;
import Utils.FechaHora;
import Utils.LimpiarCampos;
import Utils.MostrarMensaje;
import Utils.Navegador;
import Utils.ValidarCampos;
import View.CambioMOGView;
import View.DibujoView;
import View.ParoProcesoView;
import View.RegistroHoraxHoraView;
import View.RegistroRBPView;
import View.RegistroScrapView;
import java.awt.Color;
import java.awt.Frame;
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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroRBPController implements ActionListener, ItemListener {

    private final RegistroRBPView registroRBPView = RegistroRBPView.getInstance();
    private final RegistroRBPModel registroRBPModel = new RegistroRBPModel();
    private final RegistroHoraxHoraView registroHoraxHoraView = RegistroHoraxHoraView.getInstance();
    private final RegistroHoraxHoraController registroDASController = new RegistroHoraxHoraController(registroHoraxHoraView);
    private final CambioMOGView cambioMOGView = CambioMOGView.getInstance();
    private final CambioMOGController cambioMOGController = new CambioMOGController(cambioMOGView);
    private final DibujoView dibujoView = DibujoView.getInstance();
    private final DibujoController dibujoController = DibujoController.getInstance(dibujoView);
    private final RegistroScrapView registroScrapView = RegistroScrapView.getInstance();
    private final RegistroScrapController registroScrapController = new RegistroScrapController();
    private final Navegador navegador = Navegador.getInstance();
    
    private final DASModel dasModel;
    private boolean turnoValido = false;
    private boolean registroRealizado = false;

    LocalTime inicioTurno = LocalTime.parse("07:00:00");
    LocalTime finTurno = LocalTime.parse("18:59:59");
    LocalTime horaInicio;

    private final FechaHora fechaHora = new FechaHora();

    private final RBP datosRBP = RBP.getInstance();
    private final DAS datosDAS = DAS.getInstance();
    private final Operador datosOperador = Operador.getInstance();

    public RegistroRBPController() {
        this.dasModel = new DASModel();
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
        agregarAccion(registroRBPView.btnSiguiente, AccionBoton.SIGUIENTE);
        registroRBPView.cbxTurno.addItemListener(this);
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
        turno();
        registroRBPView.txtHoraInicio.setText(datosRBP.getHora());
        try {
            registroRBPView.txtFecha.setText(fechaHora.fechaActual("dd-MM-yyyy"));
        } catch (SQLException ex) {
            Logger.getLogger(RegistroRBPController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void validarDAS(Frame ventanaDestino) {
        if (registroRBPView.txtNumeroEmpleado.getText().isEmpty() || registroRBPView.txtNombreEmpleado.getText().isEmpty()) {
            MostrarMensaje.mostrarError("Favor de capturar los datos del empleado para continuar");
        } else if (registroRBPView.cbxTurno.getSelectedIndex() == 0) {
            MostrarMensaje.mostrarError("Favor de seleccionar un turno de trabajo");
        } else {
            if (turnoValido == false) {
                int opcion = JOptionPane.showConfirmDialog(null, "<html><font size='10' color='red'>¿Seguro que quieres elegir el turno "+datosDAS.getTurno()+"?<br>Una vez confirmado, no podrás modificarlo</font></html>");
                if (opcion == JOptionPane.YES_OPTION) {
                    try {
                        if(!dasModel.buscarDASExistente(datosDAS.getTurno())) {
                            dasModel.registrarDAS(datosOperador.getCódigo(), datosOperador.getCódigo(), datosOperador.getCódigo(), datosDAS.getTurno());
                        }
                        registroRBPView.cbxTurno.setEnabled(false);
                        registroRBPView.cbxTurno.setFocusable(false);
                        registroRBPView.cbxTurno.setBackground(new Color(240, 240, 240));
                        registroRBPView.cbxTurno.setForeground(Color.BLACK);
                        navegador.avanzar(ventanaDestino, registroRBPView);
                        turnoValido = true;
                    } catch (SQLException ex) {
                        Logger.getLogger(RegistroRBPController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            } else {
                navegador.avanzar(ventanaDestino, registroRBPView);
            }
        }    
    }

    private void turno() {
        horaInicio = fechaHora.stringHoraToLocalTime(datosRBP.getHora(), "HH:mm:ss");
        registroRBPView.cbxTurno.setSelectedItem(horaInicio.isAfter(inicioTurno) && horaInicio.isBefore(finTurno) ? "1" : "2");
        int turnoSeleccionado = registroRBPView.cbxTurno.getSelectedIndex();
        datosDAS.setTurno(turnoSeleccionado);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        int turnoSeleccionado = registroRBPView.cbxTurno.getSelectedIndex();
        datosDAS.setTurno(turnoSeleccionado);
    }

    public enum AccionBoton {
        DAS,
        PARO_LINEA,
        CAMBIO_MOG,
        DIBUJO,
        REGRESAR,
        SIGUIENTE
    }

    private void handleButtonAction(AccionBoton accion) {
        switch (accion) {
            case DAS:
                validarDAS(registroHoraxHoraView);
                break;
            case PARO_LINEA:
                ParoProcesoView paroProcesoView = new ParoProcesoView();
                ParoProcesoController paroProcesoController = new ParoProcesoController(paroProcesoView);
                validarDAS(paroProcesoView);
                break;
            case CAMBIO_MOG:
                validarDAS(cambioMOGView);
                break;
            case DIBUJO:
                validarDAS(dibujoView);
                break;
            case REGRESAR:
                dibujoView.reiniciarInstancia();
                navegador.regresar(registroRBPView);
                break;
            case SIGUIENTE:
                handleRegistrarPiezasProcesadas();
                break;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton botonOrigen = (JButton) e.getSource();
        AccionBoton accion = AccionBoton.valueOf(botonOrigen.getActionCommand());
        handleButtonAction(accion);
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
                    ValidarCampos.activarCampos(registroRBPView.txtFilas, registroRBPView.txtNiveles, registroRBPView.txtCanastas, registroRBPView.txtFilasCompletas, registroRBPView.txtNivelesCompletos, registroRBPView.txtSobrante, registroRBPView.txtPiezasxFila);
                } else {
                    LimpiarCampos.limpiarCampos(registroRBPView.txtNumeroEmpleado, registroRBPView.txtNumeroEmpleado);
                }
            } catch (SQLException ex) {
                Logger.getLogger(RegistroHoraxHoraController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void handleRegistrarPiezasProcesadas() {
        // Ejecuta la lógica de validarDAS
        if (registroRBPView.txtNumeroEmpleado.getText().isEmpty() || registroRBPView.txtNombreEmpleado.getText().isEmpty()) {
            MostrarMensaje.mostrarError("Favor de capturar los datos del empleado para continuar");
            return;
        } else if (registroRBPView.cbxTurno.getSelectedIndex() == 0) {
            MostrarMensaje.mostrarError("Favor de seleccionar un turno de trabajo");
            return;
        } else {
            if (turnoValido == false) {
                int opcion = JOptionPane.showConfirmDialog(null, "<html><font size='10' color='red'>¿Seguro que quieres elegir el turno " + datosDAS.getTurno() + "?<br>Una vez confirmado, no podrás modificarlo</font></html>");
                if (opcion == JOptionPane.YES_OPTION) {
                    try {
                        if (!dasModel.buscarDASExistente(datosDAS.getTurno())) {
                            dasModel.registrarDAS(datosOperador.getCódigo(), datosOperador.getCódigo(), datosOperador.getCódigo(), datosDAS.getTurno());
                        }
                        registroRBPView.cbxTurno.setEnabled(false);
                        registroRBPView.cbxTurno.setFocusable(false);
                        registroRBPView.cbxTurno.setBackground(new Color(240, 240, 240));
                        registroRBPView.cbxTurno.setForeground(Color.BLACK);
                        turnoValido = true;
                    } catch (SQLException ex) {
                        Logger.getLogger(RegistroRBPController.class.getName()).log(Level.SEVERE, null, ex);
                        return;
                    }
                } else {
                    return;
                }
            }
        }
        
        if(registroRBPView.txtPiezasxFila.getText().isEmpty() || registroRBPView.txtFilas.getText().isEmpty() || registroRBPView.txtNiveles.getText().isEmpty()
                || registroRBPView.txtCanastas.getText().isEmpty() || registroRBPView.txtFilasCompletas.getText().isEmpty() || registroRBPView.txtNivelesCompletos.getText().isEmpty()
                || registroRBPView.txtSobrante.getText().isEmpty()) {
            MostrarMensaje.mostrarAdvertencia("Para continuar ingrese todos los datos de Canastas completas y Canastas incompletas.");
            return;
        }

        // Continuar con la lógica de handleRegistrarPiezasProcesadas
        int piezasxFila = Integer.parseInt(registroRBPView.txtPiezasxFila.getText());
        int filas = Integer.parseInt(registroRBPView.txtFilas.getText());
        int niveles = Integer.parseInt(registroRBPView.txtNiveles.getText());
        int filasCompletas = Integer.parseInt(registroRBPView.txtFilasCompletas.getText());
        int nivelesCompletos = Integer.parseInt(registroRBPView.txtNivelesCompletos.getText());
        int sobrante = Integer.parseInt(registroRBPView.txtSobrante.getText());

        if (filasCompletas >= filas) {
            MostrarMensaje.mostrarError("El número de filas completas no puede ser igual o mayor al de filas. Revise los datos.");
        } else if (nivelesCompletos >= niveles) {
            MostrarMensaje.mostrarError("El número de niveles completos no puede ser igual o mayor al de niveles. Revise los datos.");
        } else if (Integer.parseInt(registroRBPView.txtSobrante.getText()) >= piezasxFila) {
            MostrarMensaje.mostrarError("El sobrante no puede ser igual o mayor a las piezas por fila. Revise los datos.");
        } else {
            int canastas = Integer.parseInt(registroRBPView.txtCanastas.getText());
            try {
                if (!dasModel.buscarDASExistente(datosDAS.getTurno())) {
                    dasModel.registrarDAS(datosOperador.getCódigo(), datosOperador.getCódigo(), datosOperador.getCódigo(), datosDAS.getTurno());
                }
                if (registroRealizado == false) {
                    registroRBPModel.registrarPiezasProcesadas(piezasxFila, filas, niveles, canastas, filasCompletas, nivelesCompletos, sobrante);
                    MostrarMensaje.mostrarInfo("Se han registrado las piezas producidas con éxito");
                    registroRealizado = true;
                } else {
                    registroRBPModel.actualizarPiezasProcesadas(piezasxFila, filas, niveles, canastas, filasCompletas, nivelesCompletos, sobrante);
                    MostrarMensaje.mostrarInfo("Se han modificado las piezas producidas con éxito");
                }
                navegador.avanzar(registroScrapView, registroRBPView);
            } catch (SQLException ex) {
                Logger.getLogger(RegistroRBPController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
