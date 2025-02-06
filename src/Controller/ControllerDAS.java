/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.*;
//import Model.PreviewMetod;
//import Model.PreviewMetodAssy;
import View.DASRegisterTHMaquinado;
import View.PreviewsMaquinadoDAS;
import View.Second_windowRBP;
import View.Third_windowsRBP;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DMM-ADMIN
 */
public class ControllerDAS implements ActionListener, KeyListener, ItemListener {
    
   
    //Vistas
    Second_windowRBP second_window_rbp;
    Third_windowsRBP third_window_rbp;
    DASRegisterTHMaquinado das_register_th_maquinado;
    PreviewsMaquinadoDAS previews_maquinado_das;
    
    //Controladores
    ControllerFirstWindow controller_first_window;
    ControllerLogin controller_login;
    ControllerSecondWindow controller_second_window;
    
    
    //Modelos
    Metods metods;
    DasWindow das_window;
    SecondWindow second_window;
    FirstWindow first_window;
    Teclado_Das_Window teclado_das_window;
    CleanViews clean_views;
    
    //Variables globales
    int contadorScrap, pintarColumnaAmarillo,turno;
    String inspectorEncontrado;
    String verificadorEncontrado;
    String lineName;

    public ControllerDAS(Second_windowRBP second_window_rbp, Third_windowsRBP third_window_rbp, DASRegisterTHMaquinado das_register_th_maquinado, 
            ControllerFirstWindow controller_first_window, ControllerLogin controller_login,Metods metods, DasWindow das_window, SecondWindow second_window, FirstWindow first_window, ControllerSecondWindow controller_second_window,
            Teclado_Das_Window teclado_das_window,CleanViews clean_views, PreviewsMaquinadoDAS previews_maquinado_das) {
        this.second_window_rbp = second_window_rbp;
        this.third_window_rbp = third_window_rbp;
        this.controller_first_window = controller_first_window;
        this.das_register_th_maquinado = das_register_th_maquinado;
        this.controller_login = controller_login;
        this.metods=metods;
        this.das_window = das_window;
        this.second_window = second_window;
        this.first_window = first_window;
        this.controller_second_window = controller_second_window;
        this.teclado_das_window=teclado_das_window;
        this.clean_views = clean_views;
        this.previews_maquinado_das = previews_maquinado_das;
        escuchadores();
      
    }
    
     public void escuchadores() {
        das_register_th_maquinado.jButtonFinalizarDas.addActionListener(this);
        das_register_th_maquinado.jButtonSalir.addActionListener(this);
        das_register_th_maquinado.jTextFieldInspector.addActionListener(this);
        das_register_th_maquinado.jTextFieldSoporteRapido.addActionListener(this);
        das_register_th_maquinado.jTextFieldSoporteRapido.addKeyListener(this);
        das_register_th_maquinado.jTextFieldNoEmpleado.addActionListener(this);
        das_register_th_maquinado.jTextFieldNoEmpleado.addKeyListener(this);
        das_register_th_maquinado.jButtonAgregarxHora.addActionListener(this);
        das_register_th_maquinado.jCheckBoxNG.addItemListener(this);
        das_register_th_maquinado.jCheckBoxOK.addItemListener(this);
        das_register_th_maquinado.jTextFieldacumulado.addKeyListener(this); 
    }   

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource().getClass().toString().equals("class javax.swing.JButton")) {
            JButton bt = (JButton) e.getSource();
            
            if (bt.equals(das_register_th_maquinado.jButtonSalir)){
                //Se cierra la vista del DAS
                das_register_th_maquinado.setVisible(false);
                //Al cerrar el das se limpia los campos para agregar por hora
                das_register_th_maquinado.jTextFieldacumulado.setText("");
                das_register_th_maquinado.jTextFieldNoEmpleado.setText("");
                das_register_th_maquinado.jLabelnombreOperador.setText("");
                das_register_th_maquinado.jCheckBoxNG.setSelected(false); 
                das_register_th_maquinado.jCheckBoxOK.setSelected(false);
                //Se pone visible la ventada de second window después de limpiar y cerrar la ventana del DAS
                second_window_rbp.setVisible(true);
            }
            
            if(bt.equals(das_register_th_maquinado.jButtonFinalizarDas)){
                //Se declara las variables que se le van a mandar para finalizar el DAS
                int id_rbp;
                String inspector;
                String soporte_rapido;
                String lote;
                String fecha;
                if(das_register_th_maquinado.jLabelnombreInspector.getText().equals("")){
                   JOptionPane.showMessageDialog(null, "Escanea o Ingresa el código de inspector para continuar con el cierre del DAS"); 
                }else if(das_register_th_maquinado.jLabelnombreSoporteRapido.getText().equals("")){
                   JOptionPane.showMessageDialog(null, "Escanea o Ingresa el código de soporte rapido para continuar con el cierre del DAS"); 
                }else if(das_register_th_maquinado.jTextFieldLoteMaquinado.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Escanea o Ingresa el lote para continuar con el cierre del DAS");
                }else{ 
                       //Se asigna el id encontrado a la variable para mandarla al insert final
                       id_rbp = das_window.obtenerIDRBP(controller_first_window.getOrden(), das_register_th_maquinado, metods);
                       
                       if(id_rbp != 0){
                           int estatusDas;
                           estatusDas=das_window.validarEstatusDas(first_window.getIDDAS(), metods, das_register_th_maquinado);
                           
                            if(estatusDas==1){
                                JOptionPane.showMessageDialog(null, "EL DAS ACTUAL YA SE ENCUENTRA CERRADO, NO SE PUEDE CONTINUAR CON ESTA ACCIÓN", "Error",JOptionPane.ERROR_MESSAGE);
                            }else{
                                //Se asigna todos los valores de los campos para mandarlo al método de finalizar DAS
                                inspector= das_register_th_maquinado.jTextFieldInspector.getText();
                                soporte_rapido = das_register_th_maquinado.jTextFieldSoporteRapido.getText();
                                lote= das_register_th_maquinado.jTextFieldLoteMaquinado.getText();
                                fecha= das_register_th_maquinado.jLabelFechaDas.getText();
                                turno = Integer.parseInt(controller_second_window.getTurnoSeleccionado());
                           
                                //Se declara una variable tipo date
                                Date fechaDate;
                                //Se declara el formato en el cual se debe mandar la fecha
                                SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy");
                                //Se manda una alerta al presionar el botón del DAS 
                                int yesno = JOptionPane.showConfirmDialog(null, "<html><div style='text-align: center;'>¿Deseas finalizar el turno?<br>"+"<span style='color: red;'>ADVERTENCIA:</span> "
                                   +" RECUERDA QUE EL DAS SOLAMENTE SE DEBE REGISTRAR AL FINALIZAR EL TURNO</div></html>", "Finalizar", JOptionPane.YES_NO_OPTION);
                                if (yesno==0){
                                    try {  
                                        fechaDate = formato.parse(fecha);
                                        //Se manda el id y todos los otras variables para que se agreguen en la tabla del DAS
                                        das_window.finalizarDas(id_rbp, first_window.getIDDAS(), inspector , soporte_rapido , lote ,das_register_th_maquinado , metods , turno, controller_second_window.getNumeroOperador(),fechaDate,second_window_rbp,clean_views,previews_maquinado_das, second_window_rbp.jTextFieldTPiecesSW.getText(), second_window_rbp.jTextFieldNameSW.getText(),controller_first_window.getLineaIngresada());
                                    } catch (ParseException ex) {
                                        JOptionPane.showMessageDialog(null, "Ocurrió un error al registrar la fecha");
                                        Logger.getLogger(ControllerDAS.class.getName()).log(Level.SEVERE, null, ex);
                                    }  
                                }  
                            }
                           
           
                       }else{
                           JOptionPane.showMessageDialog(null, "Ocurrió un error al registrar el DAS ");
                       } 
                }
                
            }
            
            if(bt.equals(das_register_th_maquinado.jButtonAgregarxHora)){
                String hora;
                try {
                    //Se actualiza la hora para que se agregue a la tabla
                    hora = metods.horaActual();
                    //Se valida si es DAS ESTA CERRADO O NO
                    //Se actualiza la hora al momento de darle aceptar
                    das_register_th_maquinado.jLabelHour.setText(hora);
                } catch (SQLException ex) {
                    JOptionPane.showConfirmDialog(null, "Error al cargar la hora actual del servidor");
                    Logger.getLogger(ControllerDAS.class.getName()).log(Level.WARNING, null, ex);
                }
               
                //Se declaran las variables de la información que se guarda en la tabla dinámica
                String nombre;
                String acumulado;
                String calidad;

                //Se obtiene el texto de los campos y se asignan a cada variable
                nombre = das_register_th_maquinado.jLabelnombreOperador.getText();
                acumulado = das_register_th_maquinado.jTextFieldacumulado.getText();
                if (das_register_th_maquinado.jCheckBoxOK.isSelected()){
                    calidad = das_register_th_maquinado.jCheckBoxOK.getText();
                }else{
                    calidad = das_register_th_maquinado.jCheckBoxNG.getText();
                }
                hora = das_register_th_maquinado.jLabelHour.getText();
                
                //Se valida que los campos no vengan vacios
                if(nombre.trim().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Debes llenar el campo de Empleado que revisó");
                }else if(acumulado.trim().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Debes llenar el campo de Acumulado");
                }else if(!das_register_th_maquinado.jCheckBoxNG.isSelected() && !das_register_th_maquinado.jCheckBoxOK.isSelected()){
                    JOptionPane.showMessageDialog(null, "Debes seleccionar tipo de calidad OK/NG");
                }else{
                    // Agregar la fila con los datos a la tabla
                    int numero_empleado = Integer.parseInt(das_register_th_maquinado.jTextFieldNoEmpleado.getText());
                    int piezasxhora =Integer.parseInt(acumulado);
                    int estatusDas;
                    //Se valida que eel das que se esta manejando no este cerrado
                    estatusDas=das_window.validarEstatusDas(first_window.getIDDAS(), metods, das_register_th_maquinado);
                    if(estatusDas==1){
                        JOptionPane.showMessageDialog(null, "EL DAS ACTUAL YA SE ENCUENTRA CERRADO, NO SE PUEDES REALIZAR MÁS REGISTROS DE PIEZAS X HORA", "Error",JOptionPane.ERROR_MESSAGE);
                    }else{
                        das_window.registrarPiezasPorHora(hora,piezasxhora,calidad,first_window.getIDDAS(),numero_empleado,metods, das_register_th_maquinado);
                        das_window.obtener_piezas_procesadas_hora(first_window.getIDDAS(), metods, das_register_th_maquinado);
                    }
                    
                    if(das_register_th_maquinado.jCheckBoxNG.isSelected() || das_register_th_maquinado.jCheckBoxOK.isSelected()){
                       das_register_th_maquinado.jCheckBoxNG.setSelected(false); 
                       das_register_th_maquinado.jCheckBoxOK.setSelected(false); 
                    }
                }
                 
                
                
            }

        }
        
        if (e.getSource().getClass().toString().equals("class javax.swing.JPasswordField")){
            JTextField password_field = (JTextField) e.getSource();
            
            
            if(password_field.equals(das_register_th_maquinado.jTextFieldSoporteRapido    )){
                //String numeroVerificaroIngresado = first_window_rbp.linenumber_fw.getText();
                char[] passwordCharsVerificador = das_register_th_maquinado.jTextFieldSoporteRapido.getPassword(); 
                String numero_empleado = new String(passwordCharsVerificador); 
                // Convierte el arreglo de char a String 
                das_window.validarVerificador(numero_empleado,das_register_th_maquinado, metods );
                //String numeroInspectorIngresado = das_register_th_maquinado.jTextFieldInspector.getPassword();
                das_register_th_maquinado.jLabelnombreSoporteRapido.setText(das_window.getVerificador());
                
            }
            
            if(password_field.equals(das_register_th_maquinado.jTextFieldInspector)){
                char[] passwordCharsInspector = das_register_th_maquinado.jTextFieldInspector.getPassword(); 
                String numero_empleado = new String(passwordCharsInspector); 
                // Convierte el arreglo de char a String 
                das_window.validarInspector(numero_empleado,das_register_th_maquinado, metods );
                //String numeroInspectorIngresado = das_register_th_maquinado.jTextFieldInspector.getPassword();
                das_register_th_maquinado.jLabelnombreInspector.setText(das_window.getInspector());

            }
            
            if(password_field.equals(das_register_th_maquinado.jTextFieldNoEmpleado)){
                char[] passwordCharsOperador = das_register_th_maquinado.jTextFieldNoEmpleado.getPassword(); 
                String numero_empleado = new String(passwordCharsOperador); 
                // Convierte el arreglo de char a String 
                das_window.getOperator(numero_empleado,das_register_th_maquinado, metods );
                //String numeroInspectorIngresado = das_register_th_maquinado.jTextFieldInspector.getPassword();
                das_register_th_maquinado.jLabelnombreInspector.setText(das_window.getInspector());
                //second_window.getOperator(numero_empleado, second_window_rbp, metods);
                  
            }
            
        }
    }
    
    public void keyTyped(KeyEvent e) { // Implementación del método 
    } 
    @Override 
    public void keyPressed(KeyEvent e) { // Implementación del método 
    } 
    @Override public void keyReleased(KeyEvent e) {
        String lote = das_register_th_maquinado.jTextFieldacumulado.getText();
        if (!lote.matches("\\d*")) {
            JOptionPane.showMessageDialog(null, "Por favor, ingresa solo números.", "Entrada inválida", JOptionPane.ERROR_MESSAGE); 
            das_register_th_maquinado.jTextFieldacumulado.setText("");
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
       if (e.getSource() == das_register_th_maquinado.jCheckBoxNG){
           if (e.getStateChange() == ItemEvent.SELECTED){
               das_register_th_maquinado.jCheckBoxOK.setEnabled(false);
            }else{
               das_register_th_maquinado.jCheckBoxOK.setEnabled(true); 
           } 
       }else if(e.getSource() == das_register_th_maquinado.jCheckBoxOK){
            if (e.getStateChange() == ItemEvent.SELECTED){
               das_register_th_maquinado.jCheckBoxNG.setEnabled(false);
            }else{
               das_register_th_maquinado.jCheckBoxNG.setEnabled(true); 
           }
       }
    }

  
}
