/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import View.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 *
 * @author DMM-ADMIN
 */
public class FirstWindow {
    
    String no, numeroParte = "", mog, hbo, modelo, dibujo, std, estandar, estandarSinColor, tm, art;
    double peso;
    int cant, id_das,id_tiempos;

    
    public void consultaERP(Metods metods, String hb, String pro, First_windowRBP first_window_rbp, CleanViews clean_views) throws SQLException {
        String jLabelProcess = first_window_rbp.jLabelProcess.getText();
        String procesoValido = jLabelProcess.replace(":", ""); 
        no = null;
        numeroParte = null;
        mog = null;
        hbo = null;
        modelo = null;
        dibujo = null;
        String numero_parte = null;
        Statement sen;
        Connection cone;
        cone = metods.oracle();
        if (pro.equals("MAQUINADO")) {

            art = "HB";

        }
          if(first_window_rbp.manufacturingorder_fw.getText().contains("HBL")&& procesoValido=="HBL"){
                    JOptionPane.showMessageDialog(null, "La orden de manufactura INGRESADA EN DE " + " " + pro);
                //JOptionPane.showMessageDialog(null, "La orden de manufactura no pertenece al proceso de MAQUINADO");
                //clean_views.CleanOrderManufacturing();
                }
        ResultSet res;
        
        sen = cone.createStatement();
        ///////EJECUTANDO SENTENCIA PARA CONSULTAR EN ERP////////
        sen.executeUpdate("ALTER SESSION SET CURRENT_SCHEMA = BAANLN");

        res = sen.executeQuery("SELECT ttcibd001500.T$ITEM, ttidms602500.T$RPNO,ttcibd001500.T$SEAK,\n"
                + "ttidms602500.T$PDNO,ttidms602500.T$OQTY,ttcibd001500.T$SEAB, ttcibd001500.T$WGHT,"
                + "ttidms602500.T$SEQN, ttcibd001500.t$dscc, ttidms602500.T$CLOT\n"
                + "FROM ttcibd001500 INNER JOIN ttidms602500 ON ttidms602500.T$ITEM=ttcibd001500.T$ITEM \n"
                + "WHERE ttidms602500.T$PDNO='" + hb + "'");

        while (res.next()) {
            no = res.getString(1);
            if (hb.contains("HBL")) {
                numeroParte = no.replace("-TH", "");
                numeroParte = numeroParte.trim();
            } 

            mog = res.getString(2);
            modelo = res.getString(3);
            hbo = res.getString(4);
           // cantidad_planeada = res.getInt(5);
            dibujo = res.getString(6);
            peso = res.getDouble(7);
            estandar = res.getString(9);
            tm = res.getString(10);
            numero_parte = no.substring(no.length() - 2); 
            ////////////////////////////////////////////////////////HASTA AQUÍ//////////////////////////////////////
            estandarSinColor = estandar.replace("Café", "").replace("CAFE", "").replace("Rojo", "").replace("Azul", "")
                    .replace("Verde", "").replace("Rosa", "").replace("Amarillo", "").replace("Negro", "")
                    .replace("Cafe", "").replace("CAFÉ", "").replace("ROJO", "").replace("AZUL", "").replace("VERDE", "")
                    .replace("ROSA", "").replace("AMARILLO", "").replace("NEGRO", "").replace("YELLOW", "")
                    .replace("BROWN", "").replace("GREEN", "").replace("MORADO", "").replace("BLUE", "")
                    .replace("BLANCO", "").replace("PURPLE", "").replace("Purple", "").replace("Blue", "")
                    .replace("PINK", "").replace("Pink", "").replace("White", "").replace("WHITE", "").replace("RED", "")
                    .replace("BLACK", "").replace("Black", "").replace("Blanca", "").replace("BLANCA", "")
                    .replace("Morado", "").replace("-", "");


            std = estandarSinColor.trim();
        }
            first_window_rbp.MOG_fw.setText(mog);
            first_window_rbp.article_fw.setText(modelo);
            first_window_rbp.drawingnumber_fw.setText(dibujo);
            first_window_rbp.process_fw.setText(pro);
            first_window_rbp.partNumber.setText(numeroParte);

            if(no == null){
                clean_views.CleanOrderManufacturing();
                JOptionPane.showMessageDialog(null, "No se encontró la orden de manufactura");
            } else if(numeroParte == null){
                clean_views.CleanOrderManufacturing();
                JOptionPane.showMessageDialog(null, "La orden de manufactura no pertenece al proceso de MAQUINADO");
              //  JOptionPane.showMessageDialog(null, "La orden de manufactura no pertenece al proceso de" + " " + pro);
            } else {
                if(procesoValido == "HBL" && numeroParte != "TH"){
                    JOptionPane.showMessageDialog(null, "La orden de manufactura no pertenece al proceso de" + " " + pro);
                //JOptionPane.showMessageDialog(null, "La orden de manufactura no pertenece al proceso de MAQUINADO");
                clean_views.CleanOrderManufacturing();
                }
            } 
        cone.close();
        

    }
     
    public String validarLinea(String linea_produccion,First_windowRBP first_window_rbp, Metods metods){
         
        String supervisorAsignado = null;
        int existe = 0;
        Connection con;
        con=metods.conexionMySQL();
        try {
            //Realizas la llamada al procedimiento almacenado
            CallableStatement cst = con.prepareCall("{call validarLinea(?,?,?)}");
            //Le mandas la linea de producción registrada en la vista y mandada desde el controlador
            cst.setString(1, linea_produccion);
            //Explicas quu el parametro 2 y 3 son de salida para poder recibir los datos mandados del procedimiento almacenado
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            //Ejecutas el query
            cst.executeQuery();
            //Asignas el parametro 3 a la variable de supervisorAsignado
            supervisorAsignado=cst.getString(3);
            //Se asigna el parámetro 2 a la variable de existe para saber si el procedimiento almacenado envia el dato
            existe=cst.getInt(2);
            if(existe==0){
                JOptionPane.showMessageDialog(null, "NO SE ENCONTRO LA LÍNEA DE PRODUCCIÓN");
                first_window_rbp.linenumber_fw.setText(null);
            }
            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "NO SE ENCONTRO NINGÚN SUPERVISOR ASIGNADO");
            Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return supervisorAsignado;
     }
     
    //Este método valida si la orden escaneada ya esta registrada en la base de datos
    public int validarOrdenExiste(String orden_ingresada,First_windowRBP first_window_rbp, Metods metods){
        int existe = 0;
        Connection con;
        con=metods.conexionMySQL();
        try {
            //Realizas la llamada al procedimiento almacenado
            CallableStatement cst = con.prepareCall("{call validarOrdenEscaneada(?,?)}");
            //Le mandas la orden de producción escaneada en la vista y mandada desde el controlador
            cst.setString(1, orden_ingresada);
            //Explicas quu el parametro 2 es de salida para poder recibir los datos mandados del procedimiento almacenado
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            //Ejecutas el query
            cst.executeQuery();
            //Se asigna el parámetro 2 a la variable de existe para saber si el procedimiento almacenado envia el dato
            existe=cst.getInt(2);
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return existe;
     }
    
    //Inserta la orden de producción y genera el ID para el rbp y así asignarlo al DAS
    public int insertarOrdenProduccion(String orden_ingresada,First_windowRBP first_window_rbp, Metods metods,String linea_ingresda,String hora, String fecha){
        int activada=0;
        Connection con;
        con=metods.conexionMySQL();
        try{
            
            CallableStatement cst= con.prepareCall("call insertarDatosGenerales(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            
            cst.setString(1,mog);
            cst.setString(2,modelo);
            cst.setString(3,dibujo);
            cst.setString(4,numeroParte);
            //SE MANDA LA ORDEN DE MANUFACTURA INGRESADA
            cst.setString(5,orden_ingresada);
            cst.setString(6,std);
            cst.setDouble(7,peso);
            cst.setInt(8,cant);
            cst.setString(9,art +" "+ modelo);
            cst.registerOutParameter(10, java.sql.Types.INTEGER);
            cst.setString(11, linea_ingresda);
            cst.setString(12, fecha);
            cst.setString(13, hora);
            cst.registerOutParameter(14, java.sql.Types.INTEGER);
            
            cst.executeQuery();
            id_tiempos=cst.getInt(14);
            activada=cst.getInt(10);
            con.close();
           
            
        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null, "Error al registrar la orden de producción a trabajar");
            Logger.getLogger(FirstWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return activada;        
    }
    
    public void registrarDas(int id_rbp_econtrado, String linea_produccion, Metods metods){
      Connection con;
      con=metods.conexionMySQL();
        try{
            CallableStatement cst= con.prepareCall("call registrarDas(?,?,?)");
            cst.setInt(1,id_rbp_econtrado);
            cst.setString(2,linea_produccion);
            cst.registerOutParameter(3, java.sql.Types.INTEGER);
            cst.executeQuery();
            id_das=cst.getInt(3);
            con.close();

        }catch (SQLException ex){
            JOptionPane.showMessageDialog(null, "Ocurrió un error al iniciar el DAS del día");
            Logger.getLogger(FirstWindow.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    public void registrarTablaTiempos(int id_rbp, Metods metods, String hora, String fecha, String linea_ingresada){
        Connection con;
        con=metods.conexionMySQL();
        try {
            //Realizas la llamada al procedimiento almacenado
            CallableStatement cst = con.prepareCall("{call registrarTablaTiempos(?,?,?,?,?)}");
            //Asignamos los parámetros de entrada y salida
            cst.setInt(1, id_rbp);
            cst.setString(2, linea_ingresada);
            cst.setString(3, fecha);
            cst.setString(4, hora);
            cst.registerOutParameter(5, java.sql.Types.INTEGER);
            //Ejecutas el query
            cst.executeQuery();

            id_tiempos=cst.getInt(5);            
          
            con.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error al asignar la fecha/hora en el RBP escaneado","",JOptionPane.ERROR_MESSAGE);
            Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    public String getMog() {
        return mog;
    }
    public String getDescripcion() {
        return modelo;
    }
    public String getNumeroDibujo() {
        return dibujo;
    }
    public String getNumeroParte() {
        return numeroParte;
    }
    public String getSTD() {
        return std;
    }
    public int getIDDAS(){
        return id_das;
    }
    public int getIDTiempos(){
        return id_tiempos;
    }
    public String getModelo() {
        return modelo;
    }
}


