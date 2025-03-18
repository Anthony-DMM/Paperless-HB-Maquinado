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
import View.RegistroDASView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.sql.Timestamp;
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
    private final RegistroDASView registroDASView = RegistroDASView.getInstance();
    private final DASModel dasModel;
    
    Navegador navegador = Navegador.getInstance();

    private FechaHora fechaHora = new FechaHora();
    private Timer timer;
    private Timestamp horaInicio;
    String tiempoTranscurrido;
    ParoProceso datosParoProceso = ParoProceso.getInstance();
    private final Map<String, Integer> causasMap = new HashMap<>();
    private final Map<String, Integer> andonesMap = new HashMap<>();
    private final Map<String, Integer> nivelesMap = new HashMap<>();
    
    private static final Logger LOGGER = Logger.getLogger(ParoProcesoManualController.class.getName());
    
    private final DAS datosDAS = DAS.getInstance();

    public ParoProcesoManualController(ParoProcesoManualView registroParoProcesoManualView) {
        this.registroParoProcesoManualView = registroParoProcesoManualView;
        this.dasModel = new DASModel();

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
                    calcularDuracionEnMinutos(registroParoProcesoManualView.txtHoraInicioHoras.getText(), registroParoProcesoManualView.txtHoraInicioMinutos.getText(), registroParoProcesoManualView.txtHoraFinHoras.getText(), registroParoProcesoManualView.txtHoraFinMinutos.getText());
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
            Navegador.getInstance().regresar(registroParoProcesoManualView);
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
    
    public static int calcularDuracionEnMinutos(String horaInicioStr, String minutosInicioStr, String horaFinStr, String minutosFinStr) {
        // 1. Convertir las cadenas de hora y minutos a enteros
        int horaInicio = Integer.parseInt(horaInicioStr);
        int minutosInicio = Integer.parseInt(minutosInicioStr);
        int horaFin = Integer.parseInt(horaFinStr);
        int minutosFin = Integer.parseInt(minutosFinStr);

        // 2. Calcular el tiempo total en minutos para la hora de inicio
        int tiempoInicioEnMinutos = (horaInicio * 60) + minutosInicio;

        // 3. Calcular el tiempo total en minutos para la hora de fin
        int tiempoFinEnMinutos = (horaFin * 60) + minutosFin;

        // 4. Calcular la diferencia entre el tiempo total de fin y el tiempo total de inicio
        int duracionEnMinutos = tiempoFinEnMinutos - tiempoInicioEnMinutos;

        // 5. Manejar el caso donde la hora de fin es menor que la hora de inicio
        if (duracionEnMinutos < 0) {
            duracionEnMinutos += 24 * 60; // Asumir que la diferencia máxima es de 24 horas
        }

        return duracionEnMinutos;
    }

    private void handleRegistrarParo() throws SQLException {
        if (hayCamposVacios()) {
            MostrarMensaje.mostrarError("Es necesario completar todos los campos para registrar el paro");
        } else {
            int minutosTranscurridos = 5;//obtenerMinutosTranscurridos(tiempoTranscurrido);
            if (minutosTranscurridos < 0) {
                MostrarMensaje.mostrarInfo("El paro en proceso no se registrará ya que su duración es menor a 5 minutos");
                Navegador.getInstance().regresar(registroParoProcesoManualView);
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
                
                String horaFin = fechaHora.timestampToString(horaInicio, "HH:mm:ss");
                String horaInicioFormateada = fechaHora.timestampToString(horaInicio, "HH:mm:ss");
                
                if(!dasModel.buscarDASExistente(datosDAS.getTurno())) {
                    Operador datosOperador = Operador.getInstance();
                    dasModel.registrarDAS(datosOperador.getCódigo(), datosOperador.getCódigo(), datosOperador.getCódigo(), datosDAS.getTurno());
                    
                }
                registroParoProcesoModel.registrarParoProceso(idCausaSeleccionada, minutosTranscurridos, detalleCausa, horaInicioFormateada, horaFin, idAndonSeleccionado, idNivelSeleccionado);
                MostrarMensaje.mostrarInfo("Se ha registrado el paro en proceso");
            }
        }
    }
    
    private void handleCategoriaSeleccionada() throws SQLException {
        if (!"Seleccionar categoria".equals(registroParoProcesoManualView.cboxCategoria.getSelectedItem())) {
            String categoria = (String) registroParoProcesoManualView.cboxCategoria.getSelectedItem();

            registroParoProcesoManualView.cboxCausa.removeAllItems();
            causasMap.clear();

            if (registroParoProcesoModel.obtenerCausasPorCategoriaParoProceso(categoria)) {
                datosParoProceso.getListaCausas().forEach(paro -> {
                    registroParoProcesoManualView.cboxCausa.addItem(paro.getDescripcion());
                    causasMap.put(paro.getDescripcion(), paro.getIdcausas_paro());
                });
            }
        } else {
            registroParoProcesoManualView.cboxCausa.removeAllItems();
        }
    }
    
    private boolean hayCamposVacios() {
        return registroParoProcesoManualView.txtHoraInicioHoras.getText().isEmpty()
                || registroParoProcesoManualView.cboxCategoria.getSelectedItem() == null
                || registroParoProcesoManualView.cboxCausa.getSelectedItem() == null
                || registroParoProcesoManualView.txtDetalle.getText().isEmpty();
    }
}
