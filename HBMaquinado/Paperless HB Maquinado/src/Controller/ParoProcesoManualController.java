/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.DAS;
import Entities.Operador;
import Entities.ParoProceso;
import Model.DASModel;
import Model.ParoProcesoModel;
import Utils.FechaHora;
import Utils.MostrarMensaje;
import Utils.Navegador;
import View.ParoProcesoManualView;
import View.ParoProcesoView;
import View.RegistroHoraxHoraView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class ParoProcesoManualController implements ActionListener {

    private final ParoProcesoModel registroParoProcesoModel = new ParoProcesoModel();
    private final ParoProcesoManualView registroParoProcesoManualView;
    private final RegistroHoraxHoraView registroDASView = RegistroHoraxHoraView.getInstance();
    private final DASModel dasModel = new DASModel();
    
    Navegador navegador = Navegador.getInstance();

    private FechaHora fechaHora = new FechaHora();
    private Timer timer;
    private Timestamp horaInicio;
    String tiempoTranscurrido;
    ParoProceso datosParoProceso = ParoProceso.getInstance();
    private final Map<String, Integer> causasMap = new HashMap<>();
    private final Map<String, Integer> andonesMap = new HashMap<>();
    private final Map<String, Integer> nivelesMap = new HashMap<>();
    
    String horaInicioFormateada;
    String horaFinFormateada;
    int duracionEnMinutos;
    
    private static final Logger LOGGER = Logger.getLogger(ParoProcesoManualController.class.getName());
    
    private final DAS datosDAS = DAS.getInstance();

    public ParoProcesoManualController(ParoProcesoManualView registroParoProcesoManualView) {
        this.registroParoProcesoManualView = registroParoProcesoManualView;
        addListeners();
        try {
            cargarDatos();
        } catch (SQLException ex) {
            Logger.getLogger(ParoProcesoManualController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void addListeners(){
        this.registroParoProcesoManualView.btnCancelar.addActionListener(this);
        this.registroParoProcesoManualView.btnFinalizar.addActionListener(this);
        this.registroParoProcesoManualView.cboxCategoria.addActionListener(this);
        
        registroParoProcesoManualView.txtHoraInicioHoras.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    registroParoProcesoManualView.txtHoraInicioMinutos.requestFocusInWindow();
                }
            }
        });

        registroParoProcesoManualView.txtHoraInicioMinutos.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    registroParoProcesoManualView.txtHoraFinHoras.requestFocusInWindow();
                }
            }
        });

        registroParoProcesoManualView.txtHoraFinHoras.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    registroParoProcesoManualView.txtHoraFinMinutos.requestFocusInWindow();
                }
            }
        });

        registroParoProcesoManualView.txtHoraFinMinutos.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    calcularDuracion();
                }
            }
        });
    }

    private void cargarDatos() throws SQLException {
        if (registroParoProcesoModel.obtenerCategoriasParoProceso()) {
            datosParoProceso = ParoProceso.getInstance();

            datosParoProceso.getListaCategorias().forEach(paro -> {
                registroParoProcesoManualView.cboxCategoria.addItem(paro.getCategoria());
            });
        }
        
        if (registroParoProcesoModel.obtenerAndonPorProceso()) {
            datosParoProceso = ParoProceso.getInstance();

            datosParoProceso.getListaAndones().forEach(paro -> {
                registroParoProcesoManualView.cboxAndon.addItem(paro.getDescripcion_andon());
                andonesMap.put(paro.getDescripcion_andon(), paro.getId_andon());
            });
        }
        
        if (registroParoProcesoModel.obtenerNivelMaquinado()) {
            datosParoProceso = ParoProceso.getInstance();

            datosParoProceso.getListaNiveles().forEach(paro -> {
                registroParoProcesoManualView.cboxNivel.addItem(String.valueOf(paro.getNivel()));
                nivelesMap.put(String.valueOf(paro.getNivel()), paro.getId_nivel());
            });
        }
            
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registroParoProcesoManualView.btnCancelar) {
            ParoProcesoView paroProcesoView = new ParoProcesoView();
            ParoProcesoController paroProcesoController = new ParoProcesoController(paroProcesoView);
            navegador.avanzar(paroProcesoView, registroParoProcesoManualView);
            navegador.getHistorial().remove(registroParoProcesoManualView);
        } else if (e.getSource() == registroParoProcesoManualView.btnFinalizar) {
            try {
                handleRegistrarParo();
            } catch (SQLException ex) {
                Logger.getLogger(ParoProcesoManualController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (e.getSource() == registroParoProcesoManualView.cboxCategoria) {
            try {
                handleCategoriaSeleccionada();
            } catch (SQLException ex) {
                Logger.getLogger(ParoProcesoManualController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void calcularDuracion() {
        try {
            // 1. Obtener las horas y minutos de los campos de texto
            int horaInicio = Integer.parseInt(registroParoProcesoManualView.txtHoraInicioHoras.getText());
            int minutosInicio = Integer.parseInt(registroParoProcesoManualView.txtHoraInicioMinutos.getText());
            int horaFin = Integer.parseInt(registroParoProcesoManualView.txtHoraFinHoras.getText());
            int minutosFin = Integer.parseInt(registroParoProcesoManualView.txtHoraFinMinutos.getText());

            // 2. Crear objetos LocalTime para horaInicio y horaFin
            LocalTime tiempoInicio = LocalTime.of(horaInicio, minutosInicio);
            LocalTime tiempoFin = LocalTime.of(horaFin, minutosFin);

            // 3. Formatear los objetos LocalTime a cadenas HH:mm:ss
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            horaInicioFormateada = tiempoInicio.format(formatter);
            horaFinFormateada = tiempoFin.format(formatter);

            // 4. Calcular la duración (como antes)
            int tiempoInicioEnMinutos = (horaInicio * 60) + minutosInicio;
            int tiempoFinEnMinutos = (horaFin * 60) + minutosFin;
            duracionEnMinutos = tiempoFinEnMinutos - tiempoInicioEnMinutos;

            if (duracionEnMinutos < 0) {
                duracionEnMinutos += 24 * 60;
            }

            // 5. Establecer la duración y las horas formateadas en los campos correspondientes
            registroParoProcesoManualView.txtDuracion.setText(String.valueOf(duracionEnMinutos));

        } catch (NumberFormatException e) {
            // Manejar el caso en que los campos de texto no contengan números válidos
            System.err.println("Error: Ingresa horas y minutos válidos.");
        }
    }


    private void handleRegistrarParo() throws SQLException {
        if (hayCamposVacios()) {
            MostrarMensaje.mostrarError("Para registrar un paro de línea debes capturar la hora de inicio, hora de fin y duración del paro, además debes seleccionar al menos una categoría y una causa");
        } else {
            if (duracionEnMinutos <= 0) {
                MostrarMensaje.mostrarInfo("El paro en proceso debe durar al menos 1 minuto");
            } else {
                String causaSeleccionada = (String) registroParoProcesoManualView.cboxCausa.getSelectedItem();
                int idCausaSeleccionada = causasMap.get(causaSeleccionada);
                String detalleCausa = registroParoProcesoManualView.txtDetalle.getText();
                
                Integer idAndonSeleccionado = 0;
                Integer idNivelSeleccionado = 0;

                String andonSeleccionado = (String) registroParoProcesoManualView.cboxAndon.getSelectedItem();
                if (andonSeleccionado != null) {
                    idAndonSeleccionado = andonesMap.get(andonSeleccionado);
                }

                String nivelSeleccionado = (String) registroParoProcesoManualView.cboxNivel.getSelectedItem();
                if (nivelSeleccionado != null) {
                    idNivelSeleccionado = nivelesMap.get(nivelSeleccionado);
                }
                
                if(!dasModel.buscarDASExistente(datosDAS.getTurno())) {
                    Operador datosOperador = Operador.getInstance();
                    dasModel.registrarDAS(datosOperador.getCódigo(), datosOperador.getCódigo(), datosOperador.getCódigo(), datosDAS.getTurno());
                    
                }
                registroParoProcesoModel.registrarParoProceso(idCausaSeleccionada, duracionEnMinutos, detalleCausa, horaInicioFormateada, horaFinFormateada, idAndonSeleccionado, idNivelSeleccionado);
                MostrarMensaje.mostrarInfo("Se ha registrado el paro en proceso");
                ParoProcesoView paroProcesoView = new ParoProcesoView();
                ParoProcesoController paroProcesoController = new ParoProcesoController(paroProcesoView);
                navegador.avanzar(paroProcesoView, registroParoProcesoManualView);
                navegador.getHistorial().remove(registroParoProcesoManualView);
            }
        }
    }
    
    private void handleCategoriaSeleccionada() throws SQLException {
        registroParoProcesoManualView.cboxCausa.removeAllItems();
        registroParoProcesoManualView.cboxCausa.addItem("Selecciona una causa");

        if (!"Seleccionar categoria".equals(registroParoProcesoManualView.cboxCategoria.getSelectedItem())) {
            String categoria = (String) registroParoProcesoManualView.cboxCategoria.getSelectedItem();
            causasMap.clear();

            if (registroParoProcesoModel.obtenerCausasPorCategoriaParoProceso(categoria)) {
                datosParoProceso.getListaCausas().forEach(paro -> {
                    registroParoProcesoManualView.cboxCausa.addItem(paro.getDescripcion());
                    causasMap.put(paro.getDescripcion(), paro.getIdcausas_paro());
                });
            }
            registroParoProcesoManualView.cboxCausa.setSelectedIndex(0);
        } else {
            registroParoProcesoManualView.cboxCausa.setSelectedIndex(0);
        }
    }
    
    private boolean hayCamposVacios() {
        return registroParoProcesoManualView.txtHoraInicioHoras.getText().isEmpty()
                || registroParoProcesoManualView.txtHoraInicioHoras.getText().isEmpty()
                || registroParoProcesoManualView.txtHoraInicioMinutos.getText().isEmpty()
                || registroParoProcesoManualView.txtHoraFinHoras.getText().isEmpty()
                || registroParoProcesoManualView.txtHoraFinMinutos.getText().isEmpty()
                || registroParoProcesoManualView.txtDuracion.getText().isEmpty()
                || registroParoProcesoManualView.cboxCategoria.getSelectedIndex()== 0
                || registroParoProcesoManualView.cboxCausa.getSelectedIndex() == 0;
    }
}
