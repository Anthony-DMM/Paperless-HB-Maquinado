/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
import View.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author BRYAN-LOPEZ
 */
public class LoginWindow {
    
     Metods metods= new Metods();
    public String get_line(String user,ChoiceWindow ch,Login lg,Metods mtd){
        String lineName=null;
        int yesno = 0;
        Connection con;
        con=mtd.conexionMySQL();
        try {
            CallableStatement cst = con.prepareCall("{call login(?,?,?)}");
            cst.setString(1, user);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.executeQuery();
            System.out.println("TRAE INSPECTOR"+cst.getString(2));
            lineName=cst.getString(3);
            yesno=cst.getInt(2);
            System.out.println("TRAE PROCESO"+lineName);
            metods.setLn(lineName);
            if(yesno==1){
                ch.setVisible(true);
                lg.setVisible(false);
                lg.txt_user.setText(null);
            }else{
                JOptionPane.showMessageDialog(null, "Datos incorrectos");
                lg.txt_user.setText(null);
            }
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(LoginWindow.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lineName;
    }
    
}
