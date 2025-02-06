/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

//import View.Second_Window_Bush;
import View.First_windowRBP;
import View.PreviewsMaquinadoDAS;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author DMM-ADMIN
 */
public class MetodStopview {
    
    public void traerCategoria(Metods metods, JComboBox combobox,String lineName){
        try {
            Connection c;
            c=metods.conexionMySQL();
            CallableStatement cst = c.prepareCall("{call traerCategoria(?)}");
            cst.setString(1, lineName);
            cst.execute();
            ResultSet r = cst.executeQuery();
            combobox.addItem("Selecciona categoría");
            while(r.next()){
                combobox.addItem(r.getString("categoria"));
            }
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(MetodStopview.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void traerCausasParo(Metods metods, JComboBox combobox,String categoria){
        try {
            Connection c;
            c=metods.conexionMySQL();
            CallableStatement cst = c.prepareCall("{call traerCausaParoProceso(?)}");
            cst.setString(1, categoria);
            cst.execute();
            ResultSet r = cst.executeQuery();
            combobox.addItem("Selecciona causa");
            while(r.next()){
                combobox.addItem(r.getString("descripcion_completa"));
            }
            c.close();
        } catch (SQLException ex) {
            Logger.getLogger(MetodStopview.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void registrarCausaParo(Metods metods,String numero_empleado, String causa_paro,int tiempo_paro,String detalle,
    String horaInicio,String fecha, String horafin,int iddas) throws ParseException{
        Connection con;
        con=metods.conexionMySQL();
        try {
            CallableStatement cst = con.prepareCall("{call registrarCausaParo(?,?,?,?,?,?,?,?)}");
            cst.setInt(1,tiempo_paro);
            cst.setString(2,detalle);
            cst.setString(3, horaInicio);
            cst.setString(4, horafin);
            cst.setString(5, fecha);
            cst.setString(6, numero_empleado);
            cst.setInt(7, iddas);
            cst.setString(8, causa_paro);
            cst.execute();
            
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(Metods.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void llenarTablaParos(Metods metods, int id_das, PreviewsMaquinadoDAS previews_maquinado_das){

            DefaultTableModel dtm = (DefaultTableModel) previews_maquinado_das.jTableParosLinea.getModel();
            dtm.setRowCount(0);
            previews_maquinado_das.jTableParosLinea.getColumnModel().getColumn(0);
            previews_maquinado_das.jTableParosLinea.getColumnModel().getColumn(1);
            previews_maquinado_das.jTableParosLinea.getColumnModel().getColumn(2);
            previews_maquinado_das.jTableParosLinea.getColumnModel().getColumn(3);
            previews_maquinado_das.jTableParosLinea.getColumnModel().getColumn(4);
            
            try {
                Connection con = metods.conexionMySQL();
                CallableStatement cst = con.prepareCall("{call getParosLinea(?)}");
                cst.setInt(1, id_das);
                ResultSet r = cst.executeQuery();
                while (r.next()) {
                    String[] data = new String[dtm.getColumnCount()];
                    data[0] = r.getString("hora_inicio");
                    data[1] = r.getString("hora_fin");
                    data[2] = r.getString("tiempo_paro");
                    data[3] = r.getString("numero_causa_paro");
                    data[4] = r.getString("detalle");
                    dtm.addRow(data);
                }
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(MetodStopview.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
   /* public void llenarTablaParos(Metods m, int idDas, Second_Window_Bush vw){
        if(idDas==0){
            
        }else{
            DefaultTableModel dtm = new DefaultTableModel();
            vw.jTableCausasP.setRowHeight(20);
            dtm.addColumn("Hora");
            dtm.addColumn("Tiempo de Paro");
            dtm.addColumn("Razón");
            dtm.addColumn("Detalle");
            vw.jTableCausasP.setModel(dtm);
            vw.jTableCausasP.getColumnModel().getColumn(0).setPreferredWidth(8);
            vw.jTableCausasP.getColumnModel().getColumn(1).setPreferredWidth(25);
            vw.jTableCausasP.getColumnModel().getColumn(2).setPreferredWidth(10);
            vw.jTableCausasP.getColumnModel().getColumn(3).setPreferredWidth(240);
            try {
                Connection c = m.conexionMySQL();
                CallableStatement cst = c.prepareCall("{call getParosLinea(?)}");
                cst.setInt(1, idDas);
                ResultSet r = cst.executeQuery();
                while (r.next()) {
                    String[] data = new String[4];
                    data[0] = r.getString("hora_inicio");
                    data[1] = r.getString("tiempo");
                    data[2] = r.getString("numero_causas_paro");
                    data[3] = r.getString("detalle");
                    dtm.addRow(data);
                }
            } catch (SQLException ex) {
                Logger.getLogger(MetodStopview.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void limpiarTabla(Second_Window_Bush vw){
        DefaultTableModel dtm = new DefaultTableModel();
            vw.jTableCausasP.setRowHeight(20);
            dtm.addColumn("Hora");
            dtm.addColumn("Tiempo de Paro");
            dtm.addColumn("Razón");
            dtm.addColumn("Detalle");
            vw.jTableCausasP.setModel(dtm);
            vw.jTableCausasP.getColumnModel().getColumn(0).setPreferredWidth(8);
            vw.jTableCausasP.getColumnModel().getColumn(1).setPreferredWidth(25);
            vw.jTableCausasP.getColumnModel().getColumn(2).setPreferredWidth(10);
            vw.jTableCausasP.getColumnModel().getColumn(3).setPreferredWidth(240);
    }*/
}
