 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package supermercado;

import java.util.Vector;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 *
 * @author Antho
 */
public class Modelo implements TableModel{
    
    private Vector<Producto> productos;
        
    public Modelo (Vector<Producto> productos){
        this.productos = productos;
    }
    
    public Vector<Producto> getProductos() {
        return productos;
    }

    public void setProductos(Vector<Producto> productos) {
        this.productos = productos;
    }
    
    
    @Override
    public int getRowCount() {
        return productos.size();
    }
    
    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int columnIndex) {
        String titulo = null;
        switch(columnIndex)
        {
            case 0:{
                titulo = "ID";
                break;
            }
            
            case 1:{
                titulo = "Nombre";
                break;
            }
            
            case 2:{
                titulo = "Descripción";
                break;
            }
            
            case 3:{
                titulo = "Precio";
                break;
            }
            
            case 4:{
                titulo = "Existencias";
                break;
            }
        }
        
        return titulo;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Producto p = productos.get(rowIndex);
        String valor = null;
        
        switch(columnIndex)
        {
            case 0:{
                valor = String.valueOf(p.getCodigo());
                break;
            }
            
            case 1:{
                valor = p.getNombre();
                break;
            }
            
            case 2:{
                valor = p.getDescripcion();
                break;
            }
            
            case 3:{
                valor = String.valueOf(p.getPrecio());
                break;
            }
            
            case 4:{
                valor = String.valueOf(p.getExistencias());
                break;
            }
        }
        
        return valor;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Producto p = productos.get(rowIndex);
        switch(columnIndex)
        {
            case 0:{
                p.setCodigo(Integer.valueOf(aValue.toString()));
                break;
            }
            
            case 1:{
                p.setNombre(aValue.toString());
                break;
            }
            
            case 2:{
                p.setDescripcion(aValue.toString());
                break;
            }
            
            case 3:{
                p.setPrecio(Float.valueOf(aValue.toString()));
                break;
            }
            
            case 4:{
                p.setExistencias(Integer.valueOf(aValue.toString()));
                break;
            }
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        //
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        //
    }
}
