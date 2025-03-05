/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Entities.ParoProceso;
import Utils.FechaHora;
import Utils.MostrarMensaje;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroParoProcesoModel {
    private static final Logger LOGGER = Logger.getLogger(CapturaOrdenManufacturaModel.class.getName());
    private DBConexion conexion;
    FechaHora fechaHora = new FechaHora();
    
    public RegistroParoProcesoModel(DBConexion conexion) {
        this.conexion = conexion;
    }
    
    public boolean obtenerCategoriasParoProceso() throws SQLException{
        int proceso = 1;
        try (Connection con = conexion.conexionMySQL();
            CallableStatement cst = con.prepareCall("{call obtenerCategoriasParoProceso(?)}")){
            
            cst.setInt(1, proceso);
            ResultSet resultSet = cst.executeQuery();
            
            if (!resultSet.isBeforeFirst()) {
                MostrarMensaje.mostrarError("No se pudieron obtener las categorías de paro en proceso");
                return false;
            } else {
                List<ParoProceso> listaParos = new ArrayList<>();
                while (resultSet.next()) {
                    ParoProceso paroProceso = ParoProceso.builder()
                            .categoria(resultSet.getString("categoria"))
                            .build();
                    listaParos.add(paroProceso);
                }

                ParoProceso paroProceso = ParoProceso.getInstance();
                paroProceso.setListaCategorias(listaParos);
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener datos del empleado", ex);
            throw ex;
        }
    }
    
    public boolean obtenerCausasPorCategoriaParoProceso(String categoria) throws SQLException{
        int proceso = 1;
        try (Connection con = conexion.conexionMySQL();
            CallableStatement cst = con.prepareCall("{call obtenerCausasPorCategoriaParoProceso(?,?)}")){
            
            cst.setInt(1, proceso);
            cst.setString(2, categoria);
            ResultSet resultSet = cst.executeQuery();
            
            if (!resultSet.isBeforeFirst()) {
                return false;
            } else {
                List<ParoProceso> listaParos = new ArrayList<>();
                while (resultSet.next()) {
                    ParoProceso paroProceso = ParoProceso.builder()
                            .idcausas_paro(resultSet.getInt("idcausas_paro"))
                            .descripcion(resultSet.getString("descripcion"))
                            .build();
                    listaParos.add(paroProceso);
                }

                ParoProceso paroProceso = ParoProceso.getInstance();
                paroProceso.setListaCausas(listaParos);
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener datos del empleado", ex);
            throw ex;
        }
    }
}