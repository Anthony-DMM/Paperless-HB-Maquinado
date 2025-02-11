/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author DMM-ADMIN
 */
public class MetodosConnection {

    Connection con;
    
    public Connection conexionMySQL() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.155.16:3306"
                    + "/rbppaperlessbush?user=adminpaperless"
                    + "&password=paperless2018");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("error de conexion");
            JOptionPane.showMessageDialog(null, "error de conexion " + e);
        }
        return con;
    }

    public Connection conexionMySQL2() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://192.168.155.16:3306"
                    + "/rbppaperlesshalfp?user=adminpaperless"
                    + "&password=paperless2018");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("error de conexion");
            JOptionPane.showMessageDialog(null, "error de conexion " + e);
        }
        return con;
    }
    
   
    public void reporte(JTable tabla) {
        String h;
        double h2;
        String fecv;
        Date h3;
        JFileChooser file = new JFileChooser();
        file.showSaveDialog(file);
        File guarda = file.getSelectedFile();
        File archivoXLS = new File(guarda + ".xls");
        try {
            if (archivoXLS.exists()) {
                archivoXLS.delete();
            } else {
                archivoXLS.createNewFile();
                Workbook libro = new HSSFWorkbook();
                FileOutputStream archivo = new FileOutputStream(archivoXLS);
                Sheet excelHoja = libro.createSheet("Reporte Bitácora");
                int a = tabla.getRowCount();
                int b = tabla.getColumnCount();
                for (int f = -1; f < a; f++) {
                    Row fila = excelHoja.createRow(f + 1);
                    for (int c = 0; c < b; c++) {
                        Cell celda = fila.createCell(c);
                        if (f == -1) {
                            celda.setCellValue(tabla.getColumnName(c));
                        } else {
                            Object k = tabla.getValueAt(f, c);
                            if (k == null) {
                                h = null;
                                celda.setCellValue(h);
                            } else {
                                try{
                                    h2=Double.parseDouble(String.valueOf(k));
                                    celda.setCellValue(h2);
                                }catch (Exception ex){        
                                    h = String.valueOf(k);
                                    celda.setCellValue(h);
                                }
                            }
                        }
                    }
                }
                if (file.accept(guarda)) {
                    libro.write(archivo);
                    archivo.close();
                    Desktop.getDesktop().open(archivoXLS);
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo abrir el archivo, intente de nuevo");
                }
            }
        } catch (Exception e) {
            System.err.println("Error " + e.getMessage());
        }
    }
}
