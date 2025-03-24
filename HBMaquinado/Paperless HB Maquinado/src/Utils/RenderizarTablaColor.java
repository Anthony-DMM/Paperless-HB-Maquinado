/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RenderizarTablaColor extends DefaultTableCellRenderer {

    private int columna_amarilla;

    public RenderizarTablaColor(int columna_amarilla) {
        this.columna_amarilla = columna_amarilla;
    }

@Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        int columna_total = 10;
        int ultima_fila = table.getRowCount() - 1;
        if (column == columna_amarilla && row != ultima_fila ) {
            c.setBackground(Color.YELLOW);
            return c;
        }

        if (column == columna_total  && value != null) {
            try {
                int doka = 500;
                double cantidad_scrap = Double.parseDouble(value.toString());

                if (cantidad_scrap >= doka) {
                    c.setBackground(Color.RED);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
            } catch (NumberFormatException e) {
                c.setBackground(Color.LIGHT_GRAY);
                c.setForeground(Color.BLACK);
            }
        } else {
            c.setBackground(Color.LIGHT_GRAY);
            c.setForeground(Color.BLACK);
        }

        return c;
    }
}
