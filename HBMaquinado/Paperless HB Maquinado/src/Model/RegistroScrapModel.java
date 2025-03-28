/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Entities.RBP;
import Entities.RazonRechazo;
import Entities.Scrap;
import Interfaces.LineaProduccion;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroScrapModel {
    private final DBConexion conexion;
    private final RBP datosRBP = RBP.getInstance();
    private final LineaProduccion lineaProduccion = LineaProduccion.getInstance();

    public RegistroScrapModel() {
        this.conexion = new DBConexion();
    }
    
    public List<RazonRechazo> obtenerRazonesRechazo() {
        List<RazonRechazo> razones_rechazo = new ArrayList<>();
        try (Connection con = conexion.conexionMySQL();
                CallableStatement cst = con.prepareCall("{call get_razon_rechazo_categoria(?)}")) {
            cst.setString(1, lineaProduccion.getProceso());
            ResultSet resultado = cst.executeQuery();
            while (resultado.next()) {
                RazonRechazo razon_rechazo = new RazonRechazo();
                razon_rechazo.setRazon_rechazo(resultado.getString("nombre_rechazo"));
                razon_rechazo.setSubcategoria_rechazo(resultado.getString("subcategoria"));
                razon_rechazo.setId_razon_rechazo(resultado.getInt("id_razon_rechazo"));
                razones_rechazo.add(razon_rechazo);
            }
        } catch (Exception e) {
            System.err.println(e);
        }
        return razones_rechazo;
    }
    
    public Map<Integer, Map<Integer, Integer>> llenarRazonRechazo(Map<Integer, Map<Integer, Integer>> registros, int cantidad, int razon_rechazo_id, int columna) {
        try (Connection con = conexion.conexionMySQL();
                CallableStatement cst = con.prepareCall("{call insertar_scrap(?,?,?,?,?)}")) {
            cst.setInt(1, cantidad);
            cst.setInt(2, razon_rechazo_id);
            cst.setInt(3, datosRBP.getId());
            cst.setInt(4, columna);
            cst.registerOutParameter(5, java.sql.Types.INTEGER);
            cst.execute();
            int id_registro = cst.getInt(5);
            Map<Integer, Integer> razon_rechazo = new HashMap<>();
            razon_rechazo.put(razon_rechazo_id, cantidad);
            registros.put(id_registro, razon_rechazo);
        } catch (Exception e) {
            System.out.println(e);
        }
        return registros;
    }
    
    public void actualizarRazonRechazo(int cantidad, int id_defecto) {
        try (Connection con = conexion.conexionMySQL();
                CallableStatement cst = con.prepareCall("{call actualizar_defecto1(?, ?)}")) {
            cst.setInt(1, id_defecto);
            cst.setInt(2, cantidad);
            cst.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void eliminarRazonRechazo(int id_defecto) {
        try (Connection con = conexion.conexionMySQL();
                CallableStatement cst = con.prepareCall("{call eliminar_defecto(?)}")) {
            cst.setInt(1, id_defecto);
            cst.execute();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public List<Scrap> obtenerScrapPrevio() {
        List<Scrap> scraps = new ArrayList<>();
        try (Connection con = conexion.conexionMySQL();
                CallableStatement cst = con.prepareCall("{call get_scrap_por_rbp(?)}")) {
            cst.setInt(1, datosRBP.getId());
            ResultSet result = cst.executeQuery();
            while(result.next()) {
                Scrap scrap = new Scrap();
                scrap.setCantidad(result.getInt("cantidad_defecto"));
                scrap.setColumna(result.getInt("columna"));
                scrap.setId_razon_rechazo(result.getInt("razon_rechazo_id_razon_rechazo"));
                scraps.add(scrap);
            }
        } catch (Exception e) {
            System.err.print(e);
        }
        return scraps;
    }
}
