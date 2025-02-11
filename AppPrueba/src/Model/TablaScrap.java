/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import View.Third_windowsRBP;
import java.awt.Color;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author DAVID-GARCIA
 */
public class TablaScrap {
    
    public void tablaHalfBearingMaquinado(JTable table, int pintarColumnaAmarillo, Metods metods, String proceso, Third_windowsRBP third_window_rbp) {
        int countRows = 0, countRowsTotal = 0;

        Connection con;
        con = metods.conexionMySQL();
        DefaultTableModel dtm = new DefaultTableModel() {
            @Override
            public final boolean isCellEditable(int row, int column) {

                if (column == pintarColumnaAmarillo && row!=66) {
                    return true;
                } else {
                    return false;
                }
            }
            public final boolean isCellEditable2(int row, int column) {
                if (row == 66) {
                    return false;
                } else {
                    return true;
                }
            }

        };
        table.setModel(dtm);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setBackground(Color.decode("#8A8A89"));
        dtm.addColumn("ID");
        dtm.addColumn("RAZÓN DE RECHAZO");
        dtm.addColumn("1");
        dtm.addColumn("2");
        dtm.addColumn("3");
        dtm.addColumn("4");
        dtm.addColumn("5");
        dtm.addColumn("TOTAL");
        TableColumnModel columnModel = table.getColumnModel();
        table.setRowHeight(28);
        columnModel.getColumn(0).setPreferredWidth(1);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(10);
        columnModel.getColumn(3).setPreferredWidth(10);
        columnModel.getColumn(4).setPreferredWidth(10);
        columnModel.getColumn(5).setPreferredWidth(10);
        columnModel.getColumn(6).setPreferredWidth(10);
        columnModel.getColumn(7).setPreferredWidth(10);
        columnModel.getColumn(0).setWidth(100);
        columnModel.getColumn(1).setWidth(100);
        columnModel.getColumn(2).setWidth(100);
        columnModel.getColumn(3).setWidth(100);
        columnModel.getColumn(4).setWidth(100);
        columnModel.getColumn(5).setWidth(100);
        columnModel.getColumn(6).setWidth(100);
        columnModel.getColumn(7).setWidth(100);

        DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();
        tcr.setBackground(Color.decode("#CCCCCC"));
        tcr.setHorizontalAlignment(SwingConstants.CENTER);

        table.getColumnModel().getColumn(0).setCellRenderer(tcr);
        table.getColumnModel().getColumn(1).setCellRenderer(tcr);
        table.getColumnModel().getColumn(2).setCellRenderer(tcr);
        table.getColumnModel().getColumn(3).setCellRenderer(tcr);
        table.getColumnModel().getColumn(4).setCellRenderer(tcr);
        table.getColumnModel().getColumn(5).setCellRenderer(tcr);
        table.getColumnModel().getColumn(6).setCellRenderer(tcr);
        table.getColumnModel().getColumn(7).setCellRenderer(tcr);
        DefaultTableCellRenderer yellow = new DefaultTableCellRenderer();
        yellow.setBackground(Color.YELLOW);
        yellow.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(pintarColumnaAmarillo).setCellRenderer(yellow);
        DefaultTableCellRenderer left = new DefaultTableCellRenderer();
        table.getColumnModel().getColumn(1).setCellRenderer(left);
        try {

            CallableStatement cst = con.prepareCall("{call traerRazonRechazo(?)}");
            cst.setString(1, proceso);
            ResultSet r = cst.executeQuery();
            while (r .next()) {
                countRows ++;
                Object[] datos = new Object[2];
                datos[0] = r .getString("numero_razon");
                datos[1] = r .getString("nombre_rechazo");
                dtm.addRow(datos);
            }
            
            countRowsTotal = countRows * 2;
            tablaProcesosHalfBearingMaquinado(proceso, third_window_rbp, countRowsTotal);
            dtm.addRow(new Object[]{"", "Total", "", "", "", "", "", "", "", "", ""});
            con.close();
        } catch (SQLException ex) {
            System.err.println("Error" + ex.getMessage());
        }
    }
    
    public void tablaProcesosHalfBearingMaquinado(String proceso, Third_windowsRBP third_window_rbp, int countRowsTotal) {
        DefaultTableModel dtm = new DefaultTableModel();
        RowcolorHalfBearingMaquinado rc = new RowcolorHalfBearingMaquinado();
        third_window_rbp.jTableProcesos.setDefaultRenderer(Object.class, rc);
        third_window_rbp.jTableProcesos.setRowHeight(14);
        third_window_rbp.jTableProcesos.setShowHorizontalLines(false);
        third_window_rbp.jScrollPane_jTableProcesos.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        third_window_rbp.jScrollPane_Tabla.getVerticalScrollBar().setModel(third_window_rbp.jScrollPane_jTableProcesos.getVerticalScrollBar().getModel());
        dtm.addColumn(" ");
        third_window_rbp.jTableProcesos.setModel(dtm);

        Object[] dato = {" "};
        Object[] data = {"C", "O", "I", "L", "I", "N", "G"};
        Object[] data0 = {"P", "R", "E", "N", "S", "A"};
        Object[] data1 = {"M", "A", "Q", "U", "I", "N", "A", "D", "O"};


        for (int i = 0; i < countRowsTotal; i++) {
            dtm.addRow(dato);
        }

        for (int i = 0; i < data.length; i++) {
            dtm.setValueAt(data[i], 2 + i, 0);
        }

        for (int i = 0; i < data0.length; i++) {
            dtm.setValueAt(data0[i], 17 + i, 0);
        }

        for (int i = 0; i < data1.length; i++) {
            dtm.setValueAt(data1[i], 44 + i, 0);
        }


        dtm.addRow(new Object[]{""});
        dtm.addRow(new Object[]{""});
    }
    
    public void llenarScrapExistente(int id_rbp, JTable table, Metods metods) {
        int tama = table.getRowCount();
        Connection con;
        con = metods.conexionMySQL();
        try {
            CallableStatement cst = con.prepareCall("call getScrapTurnos(?)");
            cst.setInt(1, id_rbp);
            ResultSet rst = cst.executeQuery();
            while (rst.next()) {
                for (int i = 0; i < tama; i++) {
                    String h = table.getValueAt(i, 0).toString();
                    int row = rst.getInt("ID");
                    int colum = rst.getInt("Colum") + 1;
                    if (h.equals(String.valueOf(row))) {
                        table.setValueAt(rst.getString("Cantidad"), i, colum);
                    }
                }
            }
            con.close();
        } catch (SQLException ex) {
            System.err.println("Error " + ex.getMessage());
        }
    }

    
}
