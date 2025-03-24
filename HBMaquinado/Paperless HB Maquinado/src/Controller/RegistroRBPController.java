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

    private final RegistroRBPModel registroRBPModel = new RegistroRBPModel();
    private final RegistroRBPView registroRBPView;
    private final RegistroHoraxHoraView registroDASView = RegistroHoraxHoraView.getInstance();
    private final RegistroHoraxHoraController registroDASController = new RegistroHoraxHoraController(registroDASView);
    private final CambioMOGView cambioMOGView = CambioMOGView.getInstance();
    private final CambioMOGController cambioMOGController = new CambioMOGController(cambioMOGView);
    private final DibujoView dibujoView = DibujoView.getInstance();
    private final DibujoController dibujoController = DibujoController.getInstance(dibujoView);
    private final RegistroScrapView registroScrapView = RegistroScrapView.getInstance();
    private final RegistroScrapController registroScrapController = new RegistroScrapController();
    private final Navegador navegador = Navegador.getInstance();
    
    private final DASModel dasModel;
    private boolean turnoValido = false;

    LocalTime inicioTurno = LocalTime.parse("07:00:00");
    LocalTime finTurno = LocalTime.parse("18:59:59");
    LocalTime horaInicio;

    private final FechaHora fechaHora = new FechaHora();

    private final RBP datosRBP = RBP.getInstance();
    private final DAS datosDAS = DAS.getInstance();
    private final Operador datosOperador = Operador.getInstance();

    private static final Logger LOGGER = Logger.getLogger(ManufacturaController.class.getName());

    public RegistroRBPController(RegistroRBPView registroRBPView) {
        this.registroRBPView = registroRBPView;
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
                int opcion = JOptionPane.showConfirmDialog(null, "<html>¿Seguro que quieres elegir el turno "+datosDAS.getTurno()+"?<br>Una vez confirmado, no podrás modificarlo</html>");
                if (opcion == JOptionPane.YES_OPTION) {
                    try {
                        if(!dasModel.buscarDASExistente(datosDAS.getTurno())) {
                            Operador datosOperador = Operador.getInstance();
                            dasModel.registrarDAS(datosOperador.getCódigo(), datosOperador.getCódigo(), datosOperador.getCódigo(), datosDAS.getTurno());
                            navegador.avanzar(ventanaDestino, registroRBPView);
                        }
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
                validarDAS(registroDASView);
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
                Logger.getLogger(RegistroHoraxHoraController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void handleRegistrarPiezasProcesadas() {
        int piezasxFila = Integer.parseInt(registroRBPView.txtPiezasxFila.getText());
        int filas = Integer.parseInt(registroRBPView.txtFilas.getText());
        int niveles = Integer.parseInt(registroRBPView.txtNiveles.getText());
        int filasCompletas = Integer.parseInt(registroRBPView.txtFilasCompletas.getText());
        int nivelesCompletos = Integer.parseInt(registroRBPView.txtNivelesCompletos.getText());

        // Asegurar que los valores 0 se conviertan en 1 SOLO para el total de la canasta completa
        int piezasxFilaCanastaCompleta = (piezasxFila == 0) ? 1 : piezasxFila;
        int filasCanastaCompleta = (filas == 0) ? 1 : filas;
        int nivelesCanastaCompleta = (niveles == 0) ? 1 : niveles;

        int totalPiezasCanasta = piezasxFilaCanastaCompleta * filasCanastaCompleta * nivelesCanastaCompleta;
        int totalPiezasCanastaIncompleta = piezasxFila * filasCompletas * nivelesCompletos;

        if (registroRBPView.txtPiezasxFila.getText().isEmpty()
                || registroRBPView.txtFilas.getText().isEmpty()
                || registroRBPView.txtNiveles.getText().isEmpty()
                || registroRBPView.txtCanastas.getText().isEmpty()
                || registroRBPView.txtFilasCompletas.getText().isEmpty()
                || registroRBPView.txtNivelesCompletos.getText().isEmpty()
                || registroRBPView.txtSobrante.getText().isEmpty()) {
            MostrarMensaje.mostrarAdvertencia("Para continuar ingrese todos los datos en las secciones de Canastas completas y Canastas incompletas.");
        } else if (filasCompletas > filas) {
            MostrarMensaje.mostrarError("El número de filas en canastas incompletas no puede ser mayor al de canastas completas. Revise los datos.");
        } else if (nivelesCompletos > niveles) {
            MostrarMensaje.mostrarError("El número de niveles en canastas incompletas no puede ser mayor al de canastas completas. Revise los datos.");
        } else if (nivelesCompletos > filasCompletas) {
            MostrarMensaje.mostrarError("El número de niveles completos en canastas incompletas no puede ser mayor a las filas completas. Revise los datos.");
        } else if (Integer.parseInt(registroRBPView.txtSobrante.getText()) >= totalPiezasCanasta) {
            MostrarMensaje.mostrarError("La cantidad de piezas sobrantes no puede ser mayor o igual a las piezas necesarias para completar una canasta. Revise los datos.");
        } else {
            int sobrante = Integer.parseInt(registroRBPView.txtSobrante.getText());
            if (filasCompletas > 0 || nivelesCompletos > 0) {
                if (sobrante < piezasxFila) {
                    MostrarMensaje.mostrarError("<html>La cantidad de piezas sobrantes debe ser igual o mayor a las piezas por fila (" + piezasxFila + ") si hay filas o niveles completos. Revisa los datos.</html>");
                    return; // Salir del método si hay error
                }
            } else if (sobrante < totalPiezasCanastaIncompleta) {
                MostrarMensaje.mostrarError("<html>La cantidad de piezas sobrantes debe ser igual o mayor a las piezas ya colocadas en la canasta incompleta.<br>Por ejemplo, si tienes 25 piezas por fila, 2 filas y 1 nivel, necesitas al menos 50 piezas (25 x 2 x 1). Revisa los datos.</html>");
                return; // Salir del método si hay error
            }

            int canastas = Integer.parseInt(registroRBPView.txtCanastas.getText());
            try {
                if (!dasModel.buscarDASExistente(datosDAS.getTurno())) {
                    Operador datosOperador = Operador.getInstance();
                    dasModel.registrarDAS(datosOperador.getCódigo(), datosOperador.getCódigo(), datosOperador.getCódigo(), datosDAS.getTurno());
                }
                registroRBPModel.registrarPiezasProcesadas(piezasxFila, filas, niveles, canastas, filasCompletas, nivelesCompletos, sobrante);
                MostrarMensaje.mostrarInfo("Se han registrado las piezas producidas con éxito");
                navegador.avanzar(registroScrapView, registroRBPView);
            } catch (SQLException ex) {
                Logger.getLogger(RegistroRBPController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
