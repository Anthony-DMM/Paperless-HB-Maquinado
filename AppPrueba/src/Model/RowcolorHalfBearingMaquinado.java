/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author DMM-ADMIN
 */
public class RowcolorHalfBearingMaquinado  extends DefaultTableCellRenderer{
    public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
   
    @Override
    public Component getTableCellRendererComponent (JTable table, Object value, boolean selected, boolean focused, int row, 
    int column){
        Component c  = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, selected, focused, row, column);
        DEFAULT_RENDERER.setHorizontalAlignment(CENTER); 
        
        if(row >= 0 && row <= 9){
            c.setBackground(Color.decode("#FDE9D9"));
        }else if(row >= 10 && row <= 26){
            c.setBackground(Color.decode("#FFFF00"));
        } else if(row > 27 && row < 104){
            c.setBackground(Color.decode("#DAEEF3"));
        }
        return c;
        
    }
}
