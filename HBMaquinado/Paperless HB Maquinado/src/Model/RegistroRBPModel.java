/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import Entities.Operador;
import Entities.DAS;
import Entities.PiezasProducidas;
import Interfaces.LineaProduccion;
import Entities.RBP;
import Utils.MostrarMensaje;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroRBPModel {

    private final DBConexion conexion;
    private static final Logger LOGGER = Logger.getLogger(RegistroRBPModel.class.getName());
    private final LocalDate fechaF;
    private final RBP datosRBP = RBP.getInstance();
    private final DAS datosDAS = DAS.getInstance();
    private final PiezasProducidas datosPiezasProducidas = PiezasProducidas.getInstance();
    private final Operador datosOperador = Operador.getInstance();
    private final LineaProduccion lineaProduccion = LineaProduccion.getInstance();
    int idDAS = 0;
    int rangoCanasta1;
    int rangoCanasta2;
    int nivelesCompletosAnterior;
    int filasCompletasAnterior;
    int sobranteAnterior;

    public RegistroRBPModel() {
        this.conexion = new DBConexion();
        fechaF = LocalDate.now();
    }
    
    public boolean validarOperador(String numeroEmpleado) throws SQLException {
        int proceso = 1;
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call traerOperador(?,?,?,?)}")) {

            cst.setString(1, numeroEmpleado);
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.setInt(3, proceso);
            cst.registerOutParameter(4, java.sql.Types.VARCHAR);
            cst.executeQuery();

            int valor = cst.getInt(2);
            String empleadoEncontrado = cst.getString(4);

            if (valor == 0) {
                MostrarMensaje.mostrarError("No se encontró ningún empleado");
                return false;
            } else {
                datosOperador.setNombre(empleadoEncontrado);
                return true;
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener datos de la entidad", ex);
            throw ex;
        }
    }
    
    public void registrarDAS(String codigoSoporte, String codigoInspector, String codigoEmpleado) throws SQLException {
        int turno = 0;
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call llenarDas(?,?,?,?,?,?,?,?)}")) {

            LineaProduccion lineaProduccion = LineaProduccion.getInstance();

            cst.registerOutParameter(1, java.sql.Types.INTEGER);
            cst.setString(2, lineaProduccion.getLinea());
            cst.setString(3, codigoEmpleado);
            cst.setString(4, codigoSoporte);
            cst.setString(5, codigoInspector);
            cst.setDate(6, java.sql.Date.valueOf(fechaF));
            cst.setString(7, lineaProduccion.getProceso());
            cst.setInt(8, turno);

            cst.executeQuery();
            idDAS = cst.getInt(1);
            if (idDAS != 0) {
                datosDAS.setIdDAS(idDAS);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al registrar la producción por hora", ex);
            throw ex;
        }
    }

    public int obtenerDASExistente() throws SQLException {
        int turno = 0;
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call buscar_das(?,?,?)}")) {

            LineaProduccion lineaProduccion = LineaProduccion.getInstance();

            cst.setString(1, lineaProduccion.getLinea());
            cst.registerOutParameter(2, java.sql.Types.INTEGER);
            cst.setInt(3, turno);

            cst.executeQuery();
            idDAS = cst.getInt(2);

            if (idDAS != 0){
                datosDAS.setIdDAS(idDAS);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar DAS existente", ex);
            throw ex;
        }
        return idDAS;
    }
    
    public boolean obtenerPiezasProcesadasMaquinado() throws SQLException {
        try (Connection con = conexion.conexionMySQL(); CallableStatement cst = con.prepareCall("{call obtener_piezas_procesadas_maquinado(?)}")) {
            cst.setInt(1, datosRBP.getId());
            ResultSet rs = cst.executeQuery();
            
            if (rs.next()) {
                rangoCanasta1 = rs.getInt("rango_canasta_1");
                rangoCanasta2 = rs.getInt("rango_canasta_2");
                nivelesCompletosAnterior = rs.getInt("niveles_completos");
                filasCompletasAnterior = rs.getInt("filas_completas");
                sobranteAnterior = rs.getInt("sobrante");
                return true;
            } else {
                return false;
            }
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al buscar DAS existente", ex);
            throw ex;
        }
    }
    
    public void registrarPiezasProcesadas(int piezasFila, int filas, int niveles, int canastas, int filasCompletas, int nivelesCompletos, int sobrante) throws SQLException {
        if(obtenerPiezasProcesadasMaquinado()){
            if(filasCompletasAnterior == 0 && nivelesCompletosAnterior == 0 && sobranteAnterior == 0){
                rangoCanasta1 = rangoCanasta2 + 1;
                rangoCanasta2 = rangoCanasta1 + canastas - 1;
            } else {
                rangoCanasta1 = rangoCanasta2;
                rangoCanasta2 = rangoCanasta1 + canastas;
            }
        } else {
            rangoCanasta1 = 1;
            rangoCanasta2 = rangoCanasta1 + canastas;
        }
        
        int piezasPorFilasCompletas = filasCompletas * piezasFila;
        int piezasPorNivelesCompletos = nivelesCompletos * (filas * piezasFila);
        int piezasProcesadas = piezasFila * filas * niveles * canastas + piezasPorFilasCompletas + piezasPorNivelesCompletos + sobrante;
        try (Connection con = conexion.conexionMySQL();
                CallableStatement cst = con.prepareCall("{call insertar_piezas_procesadas_maquinado(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}")) {
            cst.setInt(1, datosRBP.getId());
            cst.setString(2, lineaProduccion.getLinea());
            cst.setInt(3, piezasProcesadas);
            cst.setInt(4, piezasFila);
            cst.setInt(5, filas);
            cst.setInt(6, niveles);
            cst.setInt(7, canastas);
            cst.setInt(8, nivelesCompletos);
            cst.setInt(9, filasCompletas);
            cst.setInt(10, 0);
            cst.setInt(11, sobrante);
            cst.setInt(12, rangoCanasta1);
            cst.setInt(13, rangoCanasta2);
            cst.setInt(14, datosDAS.getIdDAS());
            cst.registerOutParameter(15, java.sql.Types.INTEGER);
            cst.executeQuery();
            
            datosPiezasProducidas.setPiezasTotales(piezasProcesadas);
            
            int idRegistroPiezas = cst.getInt(15);
            if (idRegistroPiezas != 0) {
                datosPiezasProducidas.setIdRegistro(idRegistroPiezas);
            }
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al registrar la producción por hora", ex);
            throw ex;
        }
    }
    
    public void actualizarPiezasProcesadas(int piezasFila, int filas, int niveles, int canastas, int filasCompletas, int nivelesCompletos, int sobrante) throws SQLException {
        if(obtenerPiezasProcesadasMaquinado()){
            if(filasCompletasAnterior == 0 && nivelesCompletosAnterior == 0 && sobranteAnterior == 0){
                rangoCanasta1 = rangoCanasta2 + 1;
                rangoCanasta2 = rangoCanasta1 + canastas - 1;
            } else {
                rangoCanasta1 = rangoCanasta2;
                rangoCanasta2 = rangoCanasta1 + canastas;
            }
        } else {
            rangoCanasta1 = 1;
            rangoCanasta2 = rangoCanasta1 + canastas;
        }
        
        int piezasPorFilasCompletas = filasCompletas * piezasFila;
        int piezasPorNivelesCompletos = nivelesCompletos * (filas * piezasFila);
        int piezasProcesadas = piezasFila * filas * niveles * canastas + piezasPorFilasCompletas + piezasPorNivelesCompletos + sobrante;
        try (Connection con = conexion.conexionMySQL();
                CallableStatement cst = con.prepareCall("{call actualizar_piezas_procesadas_maquinado(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}")) {
            cst.setInt(1, datosRBP.getId());
            cst.setString(2, lineaProduccion.getLinea());
            cst.setInt(3, piezasProcesadas);
            cst.setInt(4, piezasFila);
            cst.setInt(5, filas);
            cst.setInt(6, niveles);
            cst.setInt(7, canastas);
            cst.setInt(8, nivelesCompletos);
            cst.setInt(9, filasCompletas);
            cst.setInt(10, 0);
            cst.setInt(11, sobrante);
            cst.setInt(12, rangoCanasta1);
            cst.setInt(13, rangoCanasta2);
            cst.setInt(14, datosDAS.getIdDAS());
            cst.setInt(15, datosPiezasProducidas.getIdRegistro());
            cst.executeQuery();
            
            datosPiezasProducidas.setPiezasTotales(piezasProcesadas);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al registrar la producción por hora", ex);
            throw ex;
        }
    }
}
