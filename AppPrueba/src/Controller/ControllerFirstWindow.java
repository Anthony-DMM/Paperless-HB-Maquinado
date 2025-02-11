/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Model.*;
import View.ChoiceWindow;
import View.DASRegisterTHMaquinado;
import View.First_windowRBP;
import View.Second_windowRBP;
import View.StopView;
import View.Third_windowsRBP;
import View.Third_windowsRBP;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent; 
import javax.swing.event.DocumentListener;


/**
 *
 * @author DMM-ADMIN
 */
public class ControllerFirstWindow implements ActionListener, KeyListener, ItemListener {
    
    
    //Vistas
    First_windowRBP first_window_rbp;
    Second_windowRBP second_window_rbp;
    Third_windowsRBP third_windowRBP;
    StopView stopview;
    ChoiceWindow choicewindow;
    DASRegisterTHMaquinado das_register_maquinado;
    
    //Modelos
    FirstWindow first_window;
    SecondWindow second_window;
    DasWindow das_window;
    CleanViews clean_views;
    
    //Controladores
    ControllerLogin controller_login;
    
    
    //Variables
    String supervisorAsignado;
    int existeOrden;
    int ordenActivada;
    String lineaProduccionIngresada;
    String mog, orden, horainicio, horafinal;
    int pintarColumnaAmarillo = 2, contadorScrap = 2, horaAbrir = 0;
    int ban = 0, r = 0, val = 0, hrs = 0;
    int[] h = new int[12];
    int id_rbp;
    
    Metods metods;
    
    
    Timer timer;

    public ControllerFirstWindow(First_windowRBP first_window_rbp, FirstWindow first_window, Second_windowRBP second_window_rbp, Third_windowsRBP third_windowRBP, 
            Metods metods, ControllerLogin controller_login, StopView stopview, ChoiceWindow choicewindow, DASRegisterTHMaquinado das_register_maquinado, SecondWindow second_window, DasWindow das_window,CleanViews clean_views) {
        this.first_window_rbp = first_window_rbp;
        this.first_window = first_window;
        this.second_window_rbp = second_window_rbp;
        this.third_windowRBP = third_windowRBP;
        this.controller_login = controller_login;
        this.stopview = stopview;
        this.choicewindow = choicewindow;
        this.metods = metods;
        this.das_register_maquinado = das_register_maquinado;
        this.second_window = second_window;
        this.das_window = das_window;
        this.clean_views=clean_views;
        escuchadores();     
        
    }



    public int getHoraabrir() {
        return horaAbrir;
    }

    public void setHoraabrir(int horaabrir) {
        this.horaAbrir = horaabrir;
    }

    public int getPintarColumnaAmarillo() {
        return pintarColumnaAmarillo;
    }

    public void setPintarColumnaAmarillo(int pintarColumnaAmarillo) {
        this.pintarColumnaAmarillo = pintarColumnaAmarillo;
    }

    public int getContadorScrap() {
        return contadorScrap;
    }

    public void setContadorScrap(int contadorScrap) {
        this.contadorScrap = contadorScrap;
    }

    public String getMog() {
        return mog;
    }

    public void setMog(String mog) {
        this.mog = mog;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getHorainicio() {
        return horainicio;
    }

    public void setHorainicio(String horainicio) {
        this.horainicio = horainicio;
    }

    public void escuchadores() {
        first_window_rbp.back_fw.addActionListener(this);
        first_window_rbp.next_fw.addActionListener(this);
        second_window_rbp.jButtonBackSW.addActionListener(this);
        second_window_rbp.jButtonBackSW.addActionListener(this);
        first_window_rbp.linenumber_fw.addActionListener(this);
        first_window_rbp.manufacturingorder_fw.addActionListener(this);
        first_window_rbp.drawingnumber_fw.addActionListener(this);
        first_window_rbp.linenumber_fw.addKeyListener(this);                
        //first_window_rbp.linenumber_fw.addD(this);
        //fwrbp.jButtonAbrirHora.addActionListener(this);
                 
        
    }    

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().getClass().toString().equals("class javax.swing.JButton")) {
            JButton bt = (JButton) e.getSource();
            
            if (bt.equals(first_window_rbp.back_fw)) {
                choicewindow.setVisible(true);
                first_window_rbp.setVisible(false);
            }
            
            //*******************************************Este botón sirve para insertar la orden que se va a procesar
            
            
            if (bt.equals(first_window_rbp.next_fw)) {
                //cerrarEdge();
                mog = first_window_rbp.MOG_fw.getText();
                orden = first_window_rbp.manufacturingorder_fw.getText();
                if (mog.equals("") || orden.equals("")
                        || first_window_rbp.linenumber_fw.getText().equals("") || first_window_rbp.supervisor_fw.getText().equals("")
                        || first_window_rbp.article_fw.getText().equals("") || first_window_rbp.drawingnumber_fw.getText().equals("")
                        || first_window_rbp.process_fw.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "No se completaron todos los campos");
                }else{
                   //Se ingresan los campos "Orden de ruta, Modelo y STD" a la ventana DAS
                  das_register_maquinado.jTextFieldOrden.setText(first_window.getMog());
                  das_register_maquinado.jTextFieldModelo.setText(first_window.getModelo());
                  das_register_maquinado.jTextFieldSTD.setText(first_window.getSTD());
                    //Verifica si ya existe una orden activa en la base de datos paperless
                  existeOrden = first_window.validarOrdenExiste(orden, first_window_rbp, metods);               
                   if(existeOrden == 1){
                        id_rbp= das_window.obtenerIDRBP(orden, das_register_maquinado, metods);
                        if(id_rbp != 0){
                            String hora_ingresada;
                            String fecha_ingresada;
                            try {
                                hora_ingresada = metods.horaActual();
                                fecha_ingresada = metods.obtenerFecha();
                                first_window.registrarTablaTiempos(id_rbp,metods,hora_ingresada, fecha_ingresada,lineaProduccionIngresada);
                                first_window.registrarDas(id_rbp, first_window_rbp.linenumber_fw.getText(),metods);
                                if(first_window.getIDDAS()!=0){
                                    try{
                                        second_window.agregarFechaHora(metods, second_window_rbp, das_register_maquinado);
                                    }catch (SQLException ex) {
                                        JOptionPane.showMessageDialog(null, "Ocurrio un error al obtener la hora actual");
                                        Logger.getLogger(ControllerFirstWindow.class.getName()).log(Level.SEVERE, null, ex);
                                    }                                       
                                    first_window_rbp.setVisible(false);
                                    second_window_rbp.setVisible(true);
                                }else{
                                    JOptionPane.showMessageDialog(null, "Ocurrio un error al registrar el DAS, intenta ingresar de nuevo la orden de producción");
                                }
                            }catch(SQLException ex) {
                                JOptionPane.showConfirmDialog(null, "Error al cargar la hora actual del servidor");
                                Logger.getLogger(ControllerDAS.class.getName()).log(Level.WARNING, null, ex);
                            }
                                
                        }else{
                            JOptionPane.showMessageDialog(null, "Error al asignar la actividad diaria al RBP");
                        }
                   }else{
                       String hora_ingresada;
                       String fecha_ingresada;
                       try {
                            hora_ingresada = metods.horaActual();
                            fecha_ingresada = metods.obtenerFecha();
                            ordenActivada= first_window.insertarOrdenProduccion(orden, first_window_rbp, metods,lineaProduccionIngresada,hora_ingresada,fecha_ingresada);
                        } catch (SQLException ex) {
                            JOptionPane.showConfirmDialog(null, "Error al cargar la hora actual del servidor");
                            Logger.getLogger(ControllerDAS.class.getName()).log(Level.WARNING, null, ex);
                        }
     
                       if(ordenActivada == 1){
                            id_rbp= das_window.obtenerIDRBP(orden, das_register_maquinado, metods);
                            if(id_rbp != 0){
                                first_window.registrarDas(id_rbp, first_window_rbp.linenumber_fw.getText(),metods);
                                if(first_window.getIDDAS()!=0){
                                    try{
                                      second_window.agregarFechaHora(metods, second_window_rbp, das_register_maquinado);
                                    }catch (SQLException ex) {
                                        JOptionPane.showMessageDialog(null, "Ocurrio un error al obtener la hora actual");
                                        Logger.getLogger(ControllerFirstWindow.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                       
                                    first_window_rbp.setVisible(false);
                                    second_window_rbp.setVisible(true);
                                }else{
                                    JOptionPane.showMessageDialog(null, "Ocurrio un error al registrar el DAS, intenta ingresar de nuevo la orden de producción");
                                }
                            }else{
                                JOptionPane.showMessageDialog(null, "Error al asignar la actividad diaria al RBP");
                            }   
                       }
                   } 
                }
            }
            

        }
        
        //if para que el escuchador actue cuando se ingresa un valor a JTextField
        if (e.getSource().getClass().toString().equals("class javax.swing.JTextField")){
                JTextField text_field = (JTextField) e.getSource();
                if (text_field.equals(first_window_rbp.linenumber_fw)){
                    //Se asigna a la varibale lo que trae el componente de la vista first_window_rbp
                    lineaProduccionIngresada = first_window_rbp.linenumber_fw.getText();
                    //A la variable de supervisorAsignado se le asigna lo que traiga retornado en el método de validarLinea que esta en el modelo de first_window
                    supervisorAsignado = first_window.validarLinea(lineaProduccionIngresada, first_window_rbp, metods);
                    //Se coloca el valor del retorno al jLabel y si viene vacio se le manda el mensaje que el supervisor no se encontro
                    first_window_rbp.supervisor_fw.setText(supervisorAsignado != null ? supervisorAsignado : "Supervisor no encontrado");
                    
                }else if(text_field.equals(first_window_rbp.manufacturingorder_fw)){
                 String manufacturing = first_window_rbp.manufacturingorder_fw.getText();
                if (manufacturing.equals("") || manufacturing == null) {
                    JOptionPane.showMessageDialog(null, "Debes ingresar la orden de manufactura");
                } else{
                     try {
                         //CONSULTA AL ERP
                         String lineName = controller_login.getLineName();
                         first_window.consultaERP(metods, manufacturing, lineName, first_window_rbp, clean_views);
                         
                         //LLENADO DE LOS CAMPOS DE LA ORDEN DE MANUFACTURA
                         first_window_rbp.MOG_fw.setText(first_window.getMog());
                         first_window_rbp.article_fw.setText(first_window.getDescripcion());
                         first_window_rbp.drawingnumber_fw.setText(first_window.getNumeroDibujo());
                         first_window_rbp.process_fw.setText(lineName);
                         first_window_rbp.partNumber.setText(first_window.getNumeroParte());

                     } catch (SQLException ex) {
                         Logger.getLogger(ControllerFirstWindow.class.getName()).log(Level.SEVERE, null, ex);
                     }
                }
            }
        }
            
        
    }
    
    public String getLineaIngresada() {
        return lineaProduccionIngresada;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
    }
}
