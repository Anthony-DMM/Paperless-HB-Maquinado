/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;


import Model.Metods;
import Model.*;
import View.*;
import View.DASRegisterTHMaquinado;
import View.First_windowRBP;
import View.Second_windowRBP;
import View.Third_windowsRBP;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author DMM-ADMIN
 */
public class ControllerSecondWindow implements ActionListener, KeyListener, MouseListener, FocusListener, ItemListener {

    Second_windowRBP second_window_rbp;
    PreviewsMaquinadoDAS previws_maquinado_das;
    First_windowRBP fwrbp;
    CambioDeMog cambio_mog;
    DASRegisterTHMaquinado das_register_maquinado;
    FirstWindow first_window;
    Third_windowsRBP third_window_rbp;
    ControllerLogin cntLog;
    ControllerFirstWindow controller_first_window;
    StopView stop_view;
    Metods metods;
    SecondWindow second_window;
    ControllerLogin controller_login;
    MetodStopview metod_stop_view;
    DasWindow das_window;
    String lineName;
    int valida=0;
    String _TIPOMATERIAL;
    int turno;
    String turno_seleccionado, numero_empleado;

    public String getTIPOMATERIAL() {
        return _TIPOMATERIAL;
    }

    public void setTIPOMATERIAL(String _TIPOMATERIAL) {
        this._TIPOMATERIAL = _TIPOMATERIAL;
    }

    public int getValida() {
        return valida;
    }

    public void setValida(int valida) {
        this.valida = valida;
    }
    
    int contadorScrap, pintarColumnaAmarillo,id_operador;
    
    public ControllerSecondWindow(Second_windowRBP swrbp,  First_windowRBP fwrbp, Third_windowsRBP third_window_rbp, CambioDeMog cambio_mog,
            ControllerLogin cntLog, ControllerFirstWindow controller_first_window, DASRegisterTHMaquinado das_register_maquinado, 
            SecondWindow second_window, Metods metods,StopView stop_view,ControllerLogin controller_login,MetodStopview metod_stop_view,
            PreviewsMaquinadoDAS previws_maquinado_das,FirstWindow first_window, DasWindow das_window) {
        this.second_window_rbp = swrbp;
        this.fwrbp = fwrbp;
        this.third_window_rbp = third_window_rbp;
        this.cntLog = cntLog;
        this.metods=metods;
        this.controller_first_window=controller_first_window;
        this.das_register_maquinado = das_register_maquinado;
        this.cambio_mog = cambio_mog;
        this.second_window = second_window;
        this.stop_view = stop_view;
        this.controller_login = controller_login;
        this.metod_stop_view = metod_stop_view;
        this.previws_maquinado_das = previws_maquinado_das;
        this.das_window = das_window;
        this.first_window = first_window;
        escuchadores();
    }

    public void escuchadores() {
        second_window_rbp.jTextFieldRSW1.addKeyListener(this);
        second_window_rbp.jTextFieldFSW1.addKeyListener(this);
        second_window_rbp.jTextFieldRSW1.addMouseListener(this);
        second_window_rbp.btnStop.addActionListener(this);
        second_window_rbp.jButtonNextSW.addActionListener(this);
        second_window_rbp.jButtonDAS.addActionListener(this);

        //////////////////////MAQUINADO///////////////////////
        second_window_rbp.jTextFieldCSW2.addActionListener(this);
        second_window_rbp.jTextFieldRSW1.addFocusListener(this);
        second_window_rbp.jTextFieldFSW1.addFocusListener(this);
        second_window_rbp.jTextFieldCSW1.addFocusListener(this);
        second_window_rbp.jTextFieldCantSW1.addFocusListener(this);
        second_window_rbp.jTextFieldCSW2.addFocusListener(this);
        second_window_rbp.jTextFieldFSW3.addFocusListener(this);
        second_window_rbp.jTextFieldSSW4.addFocusListener(this);
        second_window_rbp.jTextFieldSobraInicial.addFocusListener(this);
        //////////////////////KEY LISTENER/////////////////////////
        second_window_rbp.jTextFieldCodeSW.addKeyListener(this);
        second_window_rbp.jTextFieldCodeSW.addActionListener(this);
        second_window_rbp.jTextFieldRSW1.addKeyListener(this);
        second_window_rbp.jButtonBackSW.addActionListener(this);
        ////////////////////validar lote////////////////////////////
       /*vl.jButtonCerrar.addActionListener(this);
        vl.jTextFieldLoteMaterial.addActionListener(this);
        vla.jTextFieldLoteMaterial1.addActionListener(this);
        swbsh.jButtonValdiarMat.addActionListener(this);*/
    }

    @Override
    public void actionPerformed(ActionEvent e) {
         if (e.getSource().getClass().toString().equals("class javax.swing.JPasswordField")) {
            JPasswordField password_field = (JPasswordField) e.getSource();
            if(password_field.equals(second_window_rbp.jTextFieldCodeSW)){
                
                char[] passwordCharsOperador = second_window_rbp.jTextFieldCodeSW.getPassword(); 
                numero_empleado = new String(passwordCharsOperador);
                    second_window.getOperator(numero_empleado, second_window_rbp, metods);
            } 
        } 
        
        if (e.getSource().getClass().toString().equals("class javax.swing.JButton")) {
            JButton bt = (JButton) e.getSource();
            
            if (bt.equals(second_window_rbp.jButtonBackSW)) {
                fwrbp.setVisible(true);
                second_window_rbp.setVisible(false);
            }


            ///////////////////////MAQUINADO///////////////////////////////
            if (bt.equals(second_window_rbp.jButtonNextSW)) {
                
                int estatusDas;
                estatusDas=das_window.validarEstatusDas(first_window.getIDDAS(), metods, das_register_maquinado);
                
                if(estatusDas==1){
                    String numero_empleado = second_window_rbp.jTextFieldCodeSW.getText();
                    String incompletas_niveles_completos = second_window_rbp.jTextFieldCSW2.getText();
                    String completas_niveles = second_window_rbp.jTextFieldCSW1.getText();
                    String incompletas_filas_completas = second_window_rbp.jTextFieldFSW3.getText();
                    String incompletas_filas = second_window_rbp.jTextFieldFSW2.getText();
                    String incompletas_sobrante = second_window_rbp.jTextFieldSSW4.getText();
                    String completas_piezas_x_fila = second_window_rbp.jTextFieldRSW1.getText();
                
                    int p, l, v, b, s, ll;
                
                if (second_window_rbp.jComboBoxTurn.getSelectedItem().toString().equals("") || numero_empleado == null || numero_empleado.equals("") 
                        || incompletas_niveles_completos.equals("") || completas_niveles.equals("") || incompletas_filas_completas.equals("") || incompletas_filas.equals("") 
                        || incompletas_sobrante.equals("") || completas_piezas_x_fila.equals("")) {
                    JOptionPane.showMessageDialog(null, "Debes llenar todos los campos");
                } else {
                    
                    if (das_register_maquinado.jLabelnombreSoporteRapido.getText().equals(null) || das_register_maquinado.jLabelnombreSoporteRapido.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Debes llenar la hoja de actividad diaria primero");
                    } else {
                        if (incompletas_niveles_completos.equals("") || incompletas_filas_completas.equals("") || incompletas_sobrante.equals("") || completas_piezas_x_fila.equals("")) {
                            p = 0;
                            v = 0;
                            s = 0;
                            ll = 0;
                        } else {
                            p = Integer.parseInt(incompletas_niveles_completos);
                            v = Integer.parseInt(incompletas_filas_completas);
                            s = Integer.parseInt(incompletas_sobrante);
                            ll = Integer.parseInt(completas_piezas_x_fila);
                        }
                        if (completas_niveles.equals("") || incompletas_filas.equals("")) {
                            l = 0;
                            b = 0;
                        } else {
                            l = Integer.parseInt(completas_niveles);
                            b = Integer.parseInt(incompletas_filas);
                        }
                        if (p >= l || v >= b || s >= ll) {
                            JOptionPane.showMessageDialog(null, "El valor de niveles/filas incompletas/sobrante no "
                                    + "puede ser mayor que los completos");
                            if (p >= l) {
                                second_window_rbp.jTextFieldCSW2.setText("");
                            }
                            if (v >= b) {
                                second_window_rbp.jTextFieldFSW3.setText("");
                            }
                            if (s >= ll) {
                                second_window_rbp.jTextFieldSSW4.setText("");
                            }
                        } else {
                            third_window_rbp.setVisible(true);
                            if (second_window_rbp.jTextFieldCantMOG.getText().equals("")) {
                                second_window_rbp.jTextFieldCantMOG.setText("0");
                            }
                            second_window_rbp.setVisible(false);
                        }
                    }
                }
                    
                }else{
                   JOptionPane.showMessageDialog(null, "EL DAS ACTUAL NO SE SE ENCUENTRA FINALIZADO, COMPLETA EL PROCESO PARA CONTINUAR", "Error",JOptionPane.ERROR_MESSAGE); 
                }

                
            }
            
            if(bt.equals(second_window_rbp.jButtonDAS)){
                if(second_window_rbp.jComboBoxTurn.getSelectedItem().equals("Selecciona")){
                    JOptionPane.showMessageDialog(null, "Seleccione un turno para avanzar al DAS");
                } else if (second_window_rbp.jTextFieldNameSW.getText().equals("") || second_window_rbp.jTextFieldNameSW.getText() == null) {
                    JOptionPane.showMessageDialog(null, "Debes completar el campo de nombre de empleado ingresando el código" 
                            + "de empleado");
                }else{
                        second_window.traerGrupoAsignado(metods,controller_first_window.getLineaIngresada(),das_register_maquinado);
                        turno_seleccionado = (String)second_window_rbp.jComboBoxTurn.getSelectedItem();
                        second_window_rbp.setVisible(false); 
                    try {
                        String hora = second_window.horaActual(metods);
                        das_register_maquinado.jLabelHour.setText(hora);
                        das_register_maquinado.setVisible(true);
                    } catch (SQLException ex) {
                        Logger.getLogger(ControllerSecondWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
            if (bt.equals(second_window_rbp.btnStop)) {
                if (second_window_rbp.jTextFieldNameSW.getText().equals("") || second_window_rbp.jTextFieldNameSW.getText() == null) {
                    JOptionPane.showMessageDialog(null, "Debes completar el campo de nombre de empleado ingresando el código"
                            + "de empleado");
                } else {
                        try {
                        String horaActual = metods.horaActual();
                        stop_view.jTextFieldStartStW.setText(horaActual);
                        lineName = controller_login.getLineName();
                        stop_view.jComboBoxCatego.removeItemListener(this);
                        stop_view.jComboBoxRazon.removeItemListener(this);
                        metod_stop_view.traerCategoria(metods, stop_view.jComboBoxCatego, lineName);
                        stop_view.jComboBoxCatego.addItemListener(this);
                        stop_view.jComboBoxRazon.addItemListener(this);
                        stop_view.setVisible(true);
                        second_window_rbp.setVisible(false);
                        } catch (SQLException ex) {
                            Logger.getLogger(ControllerSecondWindow.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
     public void keyReleased(KeyEvent e) {
        String piezas_x_fila = second_window_rbp.jTextFieldRSW1.getText();
        String filas = second_window_rbp.jTextFieldFSW1.getText();
        String incompletas_niveles_completos = second_window_rbp.jTextFieldCSW2.getText();
        String completas_niveles = second_window_rbp.jTextFieldCSW1.getText();
        String incompletas_filas_completas = second_window_rbp.jTextFieldFSW3.getText();
        String incompletas_sobrante = second_window_rbp.jTextFieldSSW4.getText();
        String sobrante = second_window_rbp.jTextFieldCantSW1.getText();
        second_window_rbp.jTextFieldRSW2.setText(piezas_x_fila);
        second_window_rbp.jTextFieldFSW2.setText(filas);
        second_window_rbp.jTextFieldRSW3.setText(piezas_x_fila);

        if (!piezas_x_fila.matches("\\d*")) { 
            JOptionPane.showMessageDialog(null, "Por favor, ingresa solo números.", "Entrada inválida", JOptionPane.ERROR_MESSAGE); 
            second_window_rbp.jTextFieldRSW1.setText("");
            second_window_rbp.jTextFieldRSW2.setText("");
            second_window_rbp.jTextFieldRSW3.setText("");
        } 
        if (!filas.matches("\\d*")) {
            JOptionPane.showMessageDialog(null, "Por favor, ingresa solo números.", "Entrada inválida", JOptionPane.ERROR_MESSAGE); 
            second_window_rbp.jTextFieldFSW1.setText("");
            second_window_rbp.jTextFieldFSW2.setText("");
        } 
        if(!incompletas_niveles_completos.matches("\\d*")){
            JOptionPane.showMessageDialog(null, "Por favor, ingresa solo números.", "Entrada inválida", JOptionPane.ERROR_MESSAGE); 
            second_window_rbp.jTextFieldCSW2.setText("");
        }
        if(!completas_niveles.matches("\\d*")){
            JOptionPane.showMessageDialog(null, "Por favor, ingresa solo números.", "Entrada inválida", JOptionPane.ERROR_MESSAGE); 
            second_window_rbp.jTextFieldCSW1.setText("");
        }
        if(!incompletas_filas_completas.matches("\\d*")){
            JOptionPane.showMessageDialog(null, "Por favor, ingresa solo números.", "Entrada inválida", JOptionPane.ERROR_MESSAGE); 
            second_window_rbp.jTextFieldFSW3.setText("");
        }
        if(!incompletas_sobrante.matches("\\d*")){
            JOptionPane.showMessageDialog(null, "Por favor, ingresa solo números.", "Entrada inválida", JOptionPane.ERROR_MESSAGE); 
            second_window_rbp.jTextFieldSSW4.setText("");
        }
        if(!sobrante.matches("\\d*")){
            JOptionPane.showMessageDialog(null, "Por favor, ingresa solo números.", "Entrada inválida", JOptionPane.ERROR_MESSAGE); 
            second_window_rbp.jTextFieldCantSW1.setText("");
        }   
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {

    }

    @Override
    public void itemStateChanged(ItemEvent e) {

    }
    
    public String getTurnoSeleccionado() {
        return turno_seleccionado;
    }
    public String getNumeroOperador() {
        return numero_empleado;
    }
}
