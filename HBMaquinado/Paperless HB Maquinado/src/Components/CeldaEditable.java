/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Components;

import Controller.RegistroScrapController;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.DefaultCellEditor;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class CeldaEditable extends DefaultCellEditor {
    
    private JTextFieldNumerico textField;

    public CeldaEditable(RegistroScrapController registroScrapController) {
        super(new JTextFieldNumerico());
        textField = (JTextFieldNumerico) getComponent();

        textField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    stopCellEditing();
                    
                    registroScrapController.setear_total_columna();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }
}

