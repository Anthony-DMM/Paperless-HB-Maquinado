/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import View.*;
import java.awt.Color;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author BRYAN-LOPEZ
 */
public class DasWindow {

    String inspectorEncontrado= null;
    String verificadorEncontrado= null;
    
    public void validarInspector(String numero_empleado, DASRegisterTHMaquinado das_register_th_maquinado ,Metods metods){
        Connection con;
        con=metods.conexionMySQL();
        try {
            //Realizas la llamada al procedimiento almacenado
            CallableStatement cst = con.prepareCall("{call traerInspector(?,?)}");
            //Le mandas la linea de producción registrada en la vista y mandada desde el controlador
            cst.setString(1, numero_empleado);
            //Explicas quu el parametro 2 son de salida para poder recibir los datos mandados del procedimiento almacenado
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            //Ejecutas el query
            cst.executeQuery();
            //Asignas el parametro 2 a la variable de inspectorEncontrado
            inspectorEncontrado=cst.getString(2);
            //Se valida si la consulta viene vacia o si encontro al empleado ingresado
            if(inspectorEncontrado==null){
                JOptionPane.showMessageDialog(null, "El número de empleado no se ha podido encontrar y/o no es inspector");
                das_register_th_maquinado.jTextFieldInspector.setText(null);
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void validarVerificador(String numero_empleado, DASRegisterTHMaquinado das_register_th_maquinado ,Metods metods){
        Connection con;
        con=metods.conexionMySQL();
        try {
            //Realizas la llamada al procedimiento almacenado
            CallableStatement cst = con.prepareCall("{call traerSoporteRapido(?,?)}");
            //Le mandas la linea de producción registrada en la vista y mandada desde el controlador
            cst.setString(1, numero_empleado);
            //Explicas quu el parametro 2 son de salida para poder recibir los datos mandados del procedimiento almacenado
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            //Ejecutas el query
            cst.executeQuery();
            //Asignas el parametro 2 a la variable de inspectorEncontrado
            verificadorEncontrado=cst.getString(2);
            //Se valida si la consulta viene vacia o si encontro al empleado ingresado
            if(verificadorEncontrado==null){
                JOptionPane.showMessageDialog(null, "El número de empleado no se ha podido encontrar y/o no es inspector");
                das_register_th_maquinado.jTextFieldSoporteRapido.setText(null);
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void getOperator(String numero_empleado, DASRegisterTHMaquinado das_register_th_maquinado, Metods metods) {
        String nombre_operador = "";
        Connection con;
        con = metods.conexionMySQL();
        try {
            CallableStatement cst = con.prepareCall("{call buscarOperador(?,?)}");
            cst.setString(1, numero_empleado);
            cst.registerOutParameter(2, java.sql.Types.VARCHAR);
            cst.executeQuery();
            nombre_operador=cst.getString(2);

            if (nombre_operador == null) {
                JOptionPane.showMessageDialog(null, "Datos incorrectos");
                das_register_th_maquinado.jTextFieldNoEmpleado.setText("");
                das_register_th_maquinado.jTextFieldNoEmpleado.requestFocus();
            } else {
                das_register_th_maquinado.jLabelnombreOperador.setText(nombre_operador);
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(DasWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int obtenerIDRBP(String orden_manufactura, DASRegisterTHMaquinado das_register_th_maquinado,Metods metods){
        int id_rbp=0;
        Connection con;
        con = metods.conexionMySQL();
        try{
            CallableStatement cst = con.prepareCall("{call buscarIDRBP(?,?)}");
            cst.setString(1,orden_manufactura);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.executeQuery();
            id_rbp=cst.getInt(2);
            con.close();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Ocurrio un error al buscar la orden de manufactura");
            Logger.getLogger(DasWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    
        
        return id_rbp;
    }
    
    public void finalizarDas(int id_rbp, int id_das, String inspector, String soporte_rapido, String lote, DASRegisterTHMaquinado das_register_th_maquinado,Metods metods, int turno, String id_operador, Date fecha, Second_windowRBP secon_window_rbp, CleanViews clean_view,PreviewsMaquinadoDAS previews_maquinado_das, String piezasiniciales, String nombre_operador, String linea){
        Connection con;
        con = metods.conexionMySQL();
        try{
            CallableStatement cst = con.prepareCall("{call actualizarDAS(?,?,?,?,?,?,?,?)}");
            cst.setInt(1, id_rbp);
            cst.setDate(2, new java.sql.Date(fecha.getTime()));
            cst.setString(3, soporte_rapido);
            cst.setString(4, inspector);
            cst.setString(5, lote);
            cst.setInt(6, turno);
            cst.setInt(7, id_das);
            cst.setString(8, id_operador);
            cst.executeQuery();
            JOptionPane.showMessageDialog(null, "LA ACTIVIDAD DIARIA HA SIDO REGISTRADA CON EXITO");
            
            
            clean_view.CleanDasWindow();            
            das_register_th_maquinado.setVisible(false);
            secon_window_rbp.setVisible(true);
            dasPreviewFinal(id_das, metods, previews_maquinado_das,das_register_th_maquinado,id_operador,lote,piezasiniciales,turno,nombre_operador,linea);
            dasPreviewPiezasxHoraFinal(id_das, metods, previews_maquinado_das);
            con.close();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Error al finalizar el DAS");
            Logger.getLogger(DasWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
    
    public void registrarPiezasPorHora(String hora, int acumulado, String calidad, int id_das, int numero_empleado, Metods metods, DASRegisterTHMaquinado das_register_th_maquinado){
        Connection con;
        con = metods.conexionMySQL();
        try{
            CallableStatement cst = con.prepareCall("{call  piezasxhora(?,?,?,?,?)}");
            cst.setInt(1,id_das);
            cst.setInt(2,acumulado);
            cst.setString(3,calidad);
            cst.setString(4,hora);
            cst.setInt(5,numero_empleado);
            cst.executeQuery();
            //id_rbp=cst.getInt(2);
            JOptionPane.showMessageDialog(null, "LAS PIEZAS PROCESADAS EN LA HORA: "+ hora + " FUERON REGISTRADAS CON ÉXITO");
            con.close();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "OCURRIO UN ERROR AL REGISTRAR LAS PIEZAS POR HORA");
            Logger.getLogger(DasWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void obtener_piezas_procesadas_hora(int id_das, Metods metods,DASRegisterTHMaquinado das_register_th_maquinado){
    
        DefaultTableModel dtm = (DefaultTableModel) das_register_th_maquinado.jTablePzasxHora.getModel();
        dtm.setRowCount(0);
        das_register_th_maquinado.jTablePzasxHora.getColumnModel().getColumn(0);
        das_register_th_maquinado.jTablePzasxHora.getColumnModel().getColumn(1);
        das_register_th_maquinado.jTablePzasxHora.getColumnModel().getColumn(2);
        das_register_th_maquinado.jTablePzasxHora.getColumnModel().getColumn(3);
        das_register_th_maquinado.jTablePzasxHora.getColumnModel().getColumn(4);
        Connection con;
        con = metods.conexionMySQL();
        try{
            CallableStatement cst = con.prepareCall("{call  obtener_piezas_x_hora(?)}");
            cst.setInt(1, id_das);
            ResultSet r = cst.executeQuery();
             while (r.next()) {
                    String[] data = new String[dtm.getColumnCount()];
                    data[0] = r.getString("hora");
                    data[1] = r.getString("piezas_x_hora");
                    data[2] = r.getString("acumulado");
                    data[3] = r.getString("ok_ng");
                    data[4] = r.getString("nombre");
                    dtm.addRow(data);
                }
             das_register_th_maquinado.jTextFieldacumulado.setText("");
             das_register_th_maquinado.jTextFieldNoEmpleado.setText("");
             das_register_th_maquinado.jLabelnombreOperador.setText("");
             das_register_th_maquinado.jCheckBoxNG.setSelected(false);
             das_register_th_maquinado.jCheckBoxOK.setSelected(false);
            con.close();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "OCURRIO UN ERROR AL LLENAR LA TABLA DE PIEZAS POR HORA");
            Logger.getLogger(DasWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int validarEstatusDas(int id_das, Metods metods, DASRegisterTHMaquinado das_register_maquinado){
        int estatus=0;
        Connection con;
        con= metods.conexionMySQL();
        try{
            CallableStatement cst= con.prepareCall("{call traerEstatus(?,?)}");
            cst.setInt(1, id_das);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.executeQuery();
            estatus = cst.getInt(2);
            con.close();
            
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Ocurrió un error al validar el estatus del DAS");
            Logger.getLogger(DasWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return estatus;
    }
    
    public void dasPreviewFinal(int id_das, Metods metods, PreviewsMaquinadoDAS previews_maquinado_das, DASRegisterTHMaquinado das_register_th_maquinado, String id_operador, String lote,String piezasiniciales,int turno,String nombre_operador, String linea){
        int grupo;

            grupo = Integer.parseInt(das_register_th_maquinado.jLabelGrupo.getText());
            
            String[] partes = das_register_th_maquinado.jLabelFechaDas.getText().split("-"); 
            String anio = partes[2];
            String mes = partes[1];
            String dia = partes[0];

            previews_maquinado_das.jLabelLineaDAS.setText(linea);
            if(turno==1){
                previews_maquinado_das.jLabelturnouno.setBackground(Color.GREEN);
                previews_maquinado_das.jLabelturnouno.setOpaque(true);
            }else if (turno==2){
                previews_maquinado_das.jLabelturnodos.setBackground(Color.GREEN);
                previews_maquinado_das.jLabelturnodos.setOpaque(true);
            }
            previews_maquinado_das.jTextFieldOperador.setText(nombre_operador);
            previews_maquinado_das.jTextFieldSoporteRapido.setText(das_register_th_maquinado.jLabelnombreSoporteRapido.getText());
            previews_maquinado_das.jTextFieldInspector.setText(das_register_th_maquinado.jLabelnombreInspector.getText());
            previews_maquinado_das.jLabelDia.setText(dia);
            previews_maquinado_das.jLabelMes.setText(mes);
            previews_maquinado_das.jLabelAnio.setText(anio);
            if(grupo==1){
                previews_maquinado_das.jLabelGrupo1.setBackground(Color.GREEN);
                previews_maquinado_das.jLabelGrupo1.setOpaque(true);
            }else if(grupo==2){
                previews_maquinado_das.jLabelGrupo2.setBackground(Color.GREEN);
                previews_maquinado_das.jLabelGrupo2.setOpaque(true);
            }
            DefaultTableModel tablaproduccion = (DefaultTableModel) previews_maquinado_das.jTableProduccion.getModel();
            tablaproduccion.setRowCount(0);
            previews_maquinado_das.jTableProduccion.getColumnModel().getColumn(0);
            previews_maquinado_das.jTableProduccion.getColumnModel().getColumn(1);
            previews_maquinado_das.jTableProduccion.getColumnModel().getColumn(2);
            previews_maquinado_das.jTableProduccion.getColumnModel().getColumn(3);;
            String[] data = new String[tablaproduccion.getColumnCount()];
            data[0] = das_register_th_maquinado.jTextFieldOrden.getText();
            data[1] = das_register_th_maquinado.jTextFieldModelo.getText();
            data[2] = das_register_th_maquinado.jTextFieldSTD.getText();
            data[3] = lote;
            tablaproduccion.addRow(data);
            previews_maquinado_das.jLabelMeta.setText(piezasiniciales);

    
    };
    
    public void dasPreviewPiezasxHoraFinal(int id_das, Metods metods, PreviewsMaquinadoDAS previews_maquinado_das){
        DefaultTableModel dtm = (DefaultTableModel) previews_maquinado_das.jTablePiezasHora.getModel();
        dtm.setRowCount(0);
        previews_maquinado_das.jTablePiezasHora.getColumnModel().getColumn(0);
        previews_maquinado_das.jTablePiezasHora.getColumnModel().getColumn(1);
        previews_maquinado_das.jTablePiezasHora.getColumnModel().getColumn(2);
        previews_maquinado_das.jTablePiezasHora.getColumnModel().getColumn(3);
        previews_maquinado_das.jTablePiezasHora.getColumnModel().getColumn(4);
        Connection con;
        con = metods.conexionMySQL();
        try{
            CallableStatement cst = con.prepareCall("{call  obtener_piezas_x_hora(?)}");
            cst.setInt(1, id_das);
            ResultSet r = cst.executeQuery();
            while (r.next()) {
                String[] data = new String[dtm.getColumnCount()];
                data[0] = r.getString("hora");
                data[1] = r.getString("piezas_x_hora");
                data[2] = r.getString("acumulado");
                data[3] = r.getString("ok_ng");
                data[4] = r.getString("nombre");
                dtm.addRow(data);
            }
            int totalpiezas=0;
            for (int i = 0; i < dtm.getRowCount(); i++) {
                String piezasXHoraStr = (String) dtm.getValueAt(i, 1);
                int piezasXHora = Integer.parseInt(piezasXHoraStr);
                totalpiezas += piezasXHora;
            }
            String totalpiezasSring = Integer.toString(totalpiezas);
            previews_maquinado_das.jLabelCantProcesada.setText(totalpiezasSring);
            previews_maquinado_das.jTextFieldtotalpzas.setText(totalpiezasSring);
            previews_maquinado_das.jTextFieldtototalacumulado.setText(totalpiezasSring);
            con.close();
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "OCURRIO UN ERROR AL LLENAR LA TABLA DE PIEZAS POR HORA");
            Logger.getLogger(DasWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getInspector(){
        return inspectorEncontrado;
    }
    
    public String getVerificador(){
        return verificadorEncontrado;
    }
    
    
    ////////////////////////////////////////////////////////////Cambiar variable verificador///////////////////////////////////////////////////////////
}
