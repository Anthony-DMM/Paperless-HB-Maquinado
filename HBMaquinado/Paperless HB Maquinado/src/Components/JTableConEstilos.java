/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Components;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;

public class JTableConEstilos extends DefaultTableCellRenderer {

    private Font boldFont;
    private Font plainFont;

    public JTableConEstilos() {
        boldFont = new Font(getFont().getName(), Font.BOLD, getFont().getSize());
        plainFont = new Font(getFont().getName(), Font.PLAIN, getFont().getSize());
        setHorizontalAlignment(SwingConstants.CENTER); // Mantener el centrado
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (value != null) {
            try {
                // Intentar convertir el valor a un número
                Double.parseDouble(value.toString());
                c.setFont(boldFont);
            } catch (NumberFormatException e) {
                // Si no es un número, usar la fuente normal
                c.setFont(plainFont);
            }
        } else {
            c.setFont(plainFont); // Si el valor es null, usar fuente normal
        }

        return c;
    }
}