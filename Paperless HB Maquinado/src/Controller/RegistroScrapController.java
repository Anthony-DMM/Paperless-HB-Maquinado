/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Components.CeldaEditable;
import Entities.PiezasProducidas;
import Entities.RazonRechazo;
import Entities.Scrap;
import Model.RegistroScrapModel;
import Utils.MostrarMensaje;
import Utils.Navegador;
import Utils.RenderizarTablaColor;
import View.PreviaDASView;
import View.RegistroScrapView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Vector;
import java.util.logging.Level;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ANTHONY-MARTINEZ
 */

public class RegistroScrapController implements ActionListener {
    RegistroScrapModel registroScrapModel = new RegistroScrapModel();
    RegistroScrapView registroScrapView = RegistroScrapView.getInstance();
    private DefaultTableModel modelo = (DefaultTableModel) registroScrapView.tblScrap.getModel();
    Navegador navegador = Navegador.getInstance();
    private int columna_amarilla;
    private Map<Integer, Integer> scrap_por_razon = new HashMap<>();
    private List<RazonRechazo> razones_rechazo = new ArrayList<>();
    private List<Scrap> scraps = new ArrayList<>();
    private final PiezasProducidas datosPiezasProducidas = PiezasProducidas.getInstance();
    private boolean ha_registrado = false;
    private Map<Integer, Map<Integer, Integer>> registros_rechazo = new HashMap<>();
    private int id_registro_piezas_das = -1;
    
    public RegistroScrapController() {
        registroScrapView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                razones_rechazo = registroScrapModel.obtenerRazonesRechazo();
                scraps = registroScrapModel.obtenerScrapPrevio();
                llenar_tabla_razones();
                llenar_scrap_previo();
                setear_total_columnas();
                inicializar_escuchadores();
                hacer_columna_editable();
                setear_total_por_razon();
                colorear_campos(registroScrapView.tblScrap, columna_amarilla);
            }
        });
    }

    private void llenar_tabla_razones() {
        for (RazonRechazo razon_rechazo : razones_rechazo) {
            Vector<String> row = new Vector<>();
            row.add(razon_rechazo.getSubcategoria_rechazo());
            row.add(razon_rechazo.getRazon_rechazo());
            modelo.addRow(row);
        }
        Vector<String> row = new Vector<>();
        row.add(null);
        row.add("TOTAL");
        modelo.addRow(row);
    }

    public void llenar_scrap_previo() {
        for (int i = 0; i < modelo.getRowCount() - 1; i++) {
            String proceso = String.valueOf(modelo.getValueAt(i, 0));
            String razon = String.valueOf(modelo.getValueAt(i, 1));
            int id_razon;
            Optional<RazonRechazo> razon_ = razones_rechazo.stream()
                    .filter(razon_rechazo -> razon_rechazo.getRazon_rechazo().equals(razon) && razon_rechazo.getSubcategoria_rechazo().equals(proceso))
                    .findFirst();
            if (razon_.isPresent()) {
                id_razon = razon_.get().getId_razon_rechazo();
            } else {
                id_razon = 0;
            }
            columna_amarilla = 0;
            if (id_razon != 0) {
                for (Scrap scrap : scraps) {
                    if (columna_amarilla < scrap.getColumna()) {
                        columna_amarilla = scrap.getColumna();
                    }
                    if (columna_amarilla == 0) {
                        columna_amarilla = 2;
                    }
                    if (id_razon == scrap.getId_razon_rechazo()) {
                        modelo.setValueAt(scrap.getCantidad(), i, scrap.getColumna() + 1);
                    }
                }
                columna_amarilla = columna_amarilla + 2;
            }
        }
        System.out.print(columna_amarilla);
    }

    private void inicializar_escuchadores() {
        registroScrapView.btnSiguiente.addActionListener(this);
        registroScrapView.btnRegresar.addActionListener(this);
    }

    private void colorear_campos(JTable tabla, int columna_amarilla) {
        RenderizarTablaColor renderizado = new RenderizarTablaColor(columna_amarilla);
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(renderizado);

        }
        tabla.repaint();
    }

    private void hacer_columna_editable() {
        Vector<String> nombresColumnas = new Vector<>();
        for (int i = 0; i < modelo.getColumnCount(); i++) {
            nombresColumnas.add(modelo.getColumnName(i));
        }

        DefaultTableModel nuevoModelo = new DefaultTableModel(modelo.getDataVector(), nombresColumnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == columna_amarilla && row != getRowCount() - 1;
            }
        };
        registroScrapView.tblScrap.setModel(nuevoModelo);
        modelo = nuevoModelo;

        registroScrapView.tblScrap.getColumnModel().getColumn(columna_amarilla).setCellEditor(new CeldaEditable(this));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object origen = e.getSource();

        if (origen == registroScrapView.btnSiguiente) {
            if (ha_registrado) {
                actualizar_scrap();
            } else {
                registrar_scrap();
            }
            PreviaDASView previaDASView = new PreviaDASView();
            PreviaDASController previaDASController = new PreviaDASController(previaDASView);
            navegador.avanzar(previaDASView, registroScrapView);
        }

        if (origen == registroScrapView.btnRegresar) {
            navegador.regresar(registroScrapView);
        }
    }

    public void setear_total_columnas() {
        for (int i = 2; i < columna_amarilla; i++) {
            int total = hacer_sumatoria_columna(i);
            modelo.setValueAt(total, modelo.getRowCount() - 1, i);
        }

    }

    private void setear_total_por_razon() {
        for (int fila = 1; fila < modelo.getRowCount() - 1; fila++) {
            int sumatoria = hacer_sumatoria_fila(fila);
            if (sumatoria > 0) {
                modelo.setValueAt(sumatoria, fila, modelo.getColumnCount() - 1);
            }
        }
    }

    public void setear_total_columna() {
        int total = hacer_sumatoria_columna(columna_amarilla);
        modelo.setValueAt(total, modelo.getRowCount() - 1, columna_amarilla);
    }

    public int hacer_sumatoria_columna(int columna_a_sumar) {
        int sumatoria = 0;
        int rowCount = modelo.getRowCount();
        scrap_por_razon.clear();
        if (columna_a_sumar >= 0 && columna_a_sumar < modelo.getColumnCount()) {
            for (int j = 0; j < rowCount - 1; j++) {
                String numbersString = String.valueOf(modelo.getValueAt(j, columna_a_sumar));
                String proceso = String.valueOf(modelo.getValueAt(j, 0));
                String razon = String.valueOf(modelo.getValueAt(j, 1));
                int numero_scrap;
                int id_razon;
                try {
                    numero_scrap = Integer.parseInt(numbersString);
                    Optional<RazonRechazo> razon_ = razones_rechazo.stream()
                            .filter(razon_rechazo -> razon_rechazo.getRazon_rechazo().equals(razon) && razon_rechazo.getSubcategoria_rechazo().equals(proceso))
                            .findFirst();
                    if (razon_.isPresent()) {
                        id_razon = razon_.get().getId_razon_rechazo();
                    } else {
                        id_razon = 0;
                    }
                } catch (NumberFormatException e) {
                    numero_scrap = 0;
                    id_razon = 0;
                }
                sumatoria += numero_scrap;
                if (columna_a_sumar == columna_amarilla) {
                    scrap_por_razon.put(id_razon, numero_scrap);
                }
            }
        }
        if (scrap_por_razon.containsKey(0)) {
            scrap_por_razon.remove(0);
        }
        return sumatoria;

    }

    private int hacer_sumatoria_fila(int fila_a_sumar) {
        int sumatoria = 0;
        int rowCount = modelo.getRowCount();
        if (fila_a_sumar >= 1 && fila_a_sumar < rowCount) {
            for (int columna = 2; columna < modelo.getColumnCount() - 1; columna++) {
                try {
                    int valor_campo = (int) modelo.getValueAt(fila_a_sumar, columna);
                    if ((int) valor_campo > 0) {
                        sumatoria += (int) modelo.getValueAt(fila_a_sumar, columna);
                    }
                } catch (Exception e) {

                }
            }
        }
        return sumatoria;
    }

    public void registrar_scrap() {
        int total_scrap = 0;
        for (Map.Entry<Integer, Integer> entry : scrap_por_razon.entrySet()) {
            Integer id_razon = entry.getKey();
            Integer cantidad = entry.getValue();
            total_scrap += cantidad;
            registros_rechazo = registroScrapModel.llenarRazonRechazo(registros_rechazo, cantidad, id_razon, columna_amarilla - 1);
            datosPiezasProducidas.setColumnaTurno(columna_amarilla - 1);
        }
        ha_registrado = !ha_registrado;
        MostrarMensaje.mostrarInfo("Se ha registrado el scrap con éxito");
    }

    public void actualizar_scrap() {
        List<Integer> registrosAEliminar = new ArrayList<>();
        Map<Integer, Integer> registrosAActualizar = new HashMap<>();
        Map<Integer, Integer> registrosAInsertar = new HashMap<>();

        for (Map.Entry<Integer, Map<Integer, Integer>> entry : registros_rechazo.entrySet()) {
            for (Map.Entry<Integer, Integer> value : entry.getValue().entrySet()) {
                int idRazon = value.getKey();
                int cantidad = scrap_por_razon.getOrDefault(idRazon, -1);

                if (cantidad != -1) {
                    registrosAActualizar.put(entry.getKey(), cantidad);
                    scrap_por_razon.remove(idRazon);
                } else {
                    registrosAEliminar.add(entry.getKey());
                }
            }
        }

        for (Map.Entry<Integer, Integer> entry : scrap_por_razon.entrySet()) {
            registrosAInsertar.put(entry.getKey(), entry.getValue());
        }

        for (int registro : registrosAEliminar) {
            registroScrapModel.eliminarRazonRechazo(registro);
            registros_rechazo.remove(registro);
        }

        int totalScrap = 0;
        for (Map.Entry<Integer, Integer> entry : registrosAActualizar.entrySet()) {
            registroScrapModel.actualizarRazonRechazo(entry.getValue(), entry.getKey());
            totalScrap += entry.getValue();
        }

        for (Map.Entry<Integer, Integer> entry : registrosAInsertar.entrySet()) {
            int idRazon = entry.getKey();
            int cantidad = entry.getValue();
            totalScrap += cantidad;
            registros_rechazo = registroScrapModel.llenarRazonRechazo(registros_rechazo, cantidad, idRazon, columna_amarilla - 1);
        }
        MostrarMensaje.mostrarInfo("Se ha actualizado el scrap con éxito");
    }
}