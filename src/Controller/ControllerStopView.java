/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.FirstWindow;
import Model.Teclado_Stop_View;
import Model.MetodStopview;
import Model.Metods;
import View.First_windowRBP;
import View.PreviewsMaquinadoDAS;
import View.PreviewsMaquinadoRBP1;
import View.Second_windowRBP;
import View.StopView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;


/**
 *
 * @author DAVID-GARCIA
 */
public class ControllerStopView implements ActionListener, ItemListener {
            //Vistas
            StopView stop_view;
            PreviewsMaquinadoRBP1 preview_maquinado_rbp1;
            Second_windowRBP second_window_rbp;
            First_windowRBP first_window_rbp;
            PreviewsMaquinadoDAS previews_maquinado_das;
            
            //Modelos
            MetodStopview metod_stop_view;
            Metods metods;
            FirstWindow first_window;
            
            //Controladores
            Teclado_Stop_View teclado_stop_view;
            ControllerLogin controller_login;
            
            //Variables
            String lineName;
            String fechaParo;
            String fecha_formato;
            int id_das;
            

    public ControllerStopView(PreviewsMaquinadoRBP1 preview_maquinado_rbp1, StopView stop_view, Second_windowRBP second_window_rbp, MetodStopview metod_stop_view,
            Metods metods, Teclado_Stop_View teclado_stop_view, First_windowRBP first_window_rbp, FirstWindow first_window, PreviewsMaquinadoDAS previews_maquinado_das){
            this.preview_maquinado_rbp1 = preview_maquinado_rbp1;
            this.stop_view = stop_view;
            this.second_window_rbp = second_window_rbp;
            this.metod_stop_view = metod_stop_view;
            this.metods = metods;
            this.teclado_stop_view = teclado_stop_view;
            this.first_window_rbp = first_window_rbp;
            this.first_window = first_window;
            this.previews_maquinado_das = previews_maquinado_das;
            escuchadores();
    }
    
    public void escuchadores() {
        stop_view.jButtonBackSW.addActionListener(this);
        stop_view.jComboBoxCatego.addItemListener(this);
        stop_view.jButtonFinalizarStw.addActionListener(this);
    }
    
   @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().getClass().toString().equals("class javax.swing.JButton")) {
            JButton boton = (JButton) e.getSource();
            
            if (boton.equals(stop_view.jButtonBackSW)) {
                stop_view.jTextFieldEspecificacion.setText("");
                stop_view.jComboBoxRazon.removeItemListener(this);
                stop_view.jComboBoxCatego.removeItemListener(this);
                stop_view.jComboBoxRazon.removeAllItems();
                stop_view.jComboBoxCatego.removeAllItems();
                stop_view.jComboBoxRazon.addItemListener(this);
                stop_view.jComboBoxCatego.addItemListener(this);
                stop_view.setVisible(false);
                second_window_rbp.setVisible(true);
            }
            
            if (boton.equals(stop_view.jButtonFinalizarStw)) {
                int totaltiempo;
                String horainicio, horafin, causa_paro, detalle, linea, numero_empleado;
               if (stop_view.jComboBoxCatego.getSelectedIndex() == 0 || stop_view.jComboBoxRazon.getSelectedIndex() == 0) {
                   JOptionPane.showMessageDialog(null, "Se deben seleccionar la categoría y razón");
                } else {
                    String especificacion=stop_view.jTextFieldEspecificacion.getText();
                    if(especificacion.equals("") || especificacion.equals(null)){
                        JOptionPane.showMessageDialog(null, "Se debe colocar comentario del tiempo de paro");
                    } else {
                    
                    try {
                        id_das = first_window.getIDDAS();
                        fecha_formato = metods.formatoFecha(second_window_rbp.jDateChooserDateSW.getDate());
                        numero_empleado = second_window_rbp.jTextFieldCodeSW.getText();
                        horainicio = stop_view.jTextFieldStartStW.getText();
                        horafin = metods.horaActual();
                        detalle = stop_view.jTextFieldEspecificacion.getText();
                        causa_paro = stop_view.jComboBoxRazon.getSelectedItem().toString();
                        linea = first_window_rbp.linenumber_fw.getText();
                        String fecha_hora_inicio = fecha_formato + " " + horainicio;
                        String fecha_hora_fin = metods.obtenerFechaHora();
                        String r = metods.minutosTotales(fecha_hora_inicio, fecha_hora_fin);
                        totaltiempo = Integer.parseInt(r);
                        
                       if (totaltiempo >= 5) {
                            metod_stop_view.registrarCausaParo(metods, numero_empleado, causa_paro, totaltiempo, detalle, horainicio, fecha_formato, horafin, id_das);
                            metod_stop_view.llenarTablaParos(metods, id_das, previews_maquinado_das);
                            JOptionPane.showMessageDialog(null, "Minutos totales: " + totaltiempo +"\n" + " Hora inicio: " + horainicio + "\n" + " Hora fin: " + horafin);
                        } else {
                            JOptionPane.showMessageDialog(null, "El tiempo en paro fue menor a 5 minutos, no se registrara en la hoja de actividad diaria");
                        }

                        stop_view.jTextFieldEspecificacion.setText("");
                        stop_view.jComboBoxRazon.removeItemListener(this);
                        stop_view.jComboBoxCatego.removeItemListener(this);
                        stop_view.jComboBoxRazon.removeAllItems();
                        stop_view.jComboBoxCatego.removeAllItems();
                        stop_view.jComboBoxRazon.addItemListener(this);
                        stop_view.jComboBoxCatego.addItemListener(this);
                        
                        stop_view.setVisible(false);
                        second_window_rbp.setVisible(true);
                    } catch (ParseException | SQLException ex) {
                            Logger.getLogger(ControllerStopView.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }           
        }
    }
        
    
    
    @Override
    public void itemStateChanged(ItemEvent e) {
        
        if (e.getSource() == stop_view.jComboBoxCatego) {
            if (stop_view.jComboBoxCatego.getSelectedItem().equals("Selecciona")) {
                stop_view.jComboBoxRazon.removeAllItems();
                stop_view.jComboBoxRazon.addItem("Selecciona");
            } else {
                String categoria = stop_view.jComboBoxCatego.getSelectedItem().toString();
                stop_view.jComboBoxRazon.removeAllItems();
                metod_stop_view.traerCausasParo(metods, stop_view.jComboBoxRazon, categoria);
            }
        }
    }
}

