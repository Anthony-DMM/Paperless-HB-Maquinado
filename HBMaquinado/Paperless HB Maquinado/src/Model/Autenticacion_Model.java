/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Interfaces.LineaProduccion;
import View.Autenticacion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class Autenticacion_Model {

    public LineaProduccion datosLinea;
    private DBConexion conexion;

    public Autenticacion_Model(DBConexion conexion) {
        this.conexion = conexion;
    }

    public LineaProduccion validarLinea(String lineaProduccion, String procesoEsperado) {
        String supervisorAsignado = null, procesoMaquina = null;
        Connection con;
        con = conexion.conexionMySQL();
        try {
            CallableStatement cst = con.prepareCall("{call Supervisorname(?,?,?,?)}");
            cst.setString(1, lineaProduccion);
            cst.setString(2, procesoEsperado);
            cst.registerOutParameter(3, java.sql.Types.VARCHAR);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.executeQuery();
            supervisorAsignado = cst.getString(3);
            procesoMaquina = cst.getString(4);
            con.close();

            if (procesoMaquina == null) {
                return null;
            } else {
                LineaProduccion linea = LineaProduccion.getInstance(); // Obtener la instancia única
                linea.setLinea(lineaProduccion);
                linea.setSupervisor(supervisorAsignado);
                linea.setProceso(procesoMaquina);
                return linea;
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "No se encontró ningún supervisor asignado");
            Logger.getLogger(Captura_Orden_Manufactura_Model.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
