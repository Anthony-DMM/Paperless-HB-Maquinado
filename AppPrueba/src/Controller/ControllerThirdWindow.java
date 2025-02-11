/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;


import Model.FirstWindow;
import Model.Metods;
import Model.Teclado_Third_Window_RBP;
import View.First_windowRBP;
import View.PreviewsMaquinadoDAS;
import View.Second_windowRBP;
import View.Third_windowsRBP;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

/**
 *
 * @author DAVID-GARCIA
 */
public class ControllerThirdWindow implements ActionListener, MouseListener {
    //Vistas
    Second_windowRBP second_window_rbp;
    First_windowRBP first_window_rbp;
    PreviewsMaquinadoDAS previews_maquinado_das;
    Third_windowsRBP third_window_rbp;
 
    //Modelos
    Metods metods;
    Teclado_Third_Window_RBP teclado_third_window;
    FirstWindow first_window;
    
    //Controladores
    ControllerFirstWindow controller_first_window;

    //Variables


    //Teclado
    JPopupMenu pop, pop2;
    
    public ControllerThirdWindow(Second_windowRBP second_window_rbp, Metods metods, PreviewsMaquinadoDAS previews_maquinado_das, Third_windowsRBP third_window_rbp,
         ControllerFirstWindow controller_first_window, FirstWindow first_window) {
        this.second_window_rbp = second_window_rbp;
        this.metods=metods;
        this.previews_maquinado_das = previews_maquinado_das;
        this.third_window_rbp = third_window_rbp;
        this.controller_first_window = controller_first_window;
        this.first_window = first_window;
        escuchadores();
    }

    public void escuchadores() {
    third_window_rbp.Tabla.addMouseListener(this);
    third_window_rbp.back_tw.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    if (e.getSource().getClass().toString().equals("class javax.swing.JButton")) {
            JButton bt = (JButton) e.getSource();
            
            if (bt.equals(third_window_rbp.back_tw)) {
                third_window_rbp.setVisible(false);
                second_window_rbp.setVisible(true);
            }}
    }
    
        @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource().getClass().toString().equals("class javax.swing.JTable")) {
            JTable jt = (JTable) e.getSource();
     if (jt.equals(third_window_rbp.Tabla)) {
                if (pop2 != null) {
                    pop2.setVisible(false);
                    pop2 = null;
                }
                pop2 = new JPopupMenu();
                teclado_third_window = new Teclado_Third_Window_RBP(third_window_rbp.Tabla, first_window.getContadorScrap(), third_window_rbp);
                pop2.add(teclado_third_window);
                pop2.setVisible(true);
                pop2.setLocation(30, 30);
                third_window_rbp.Tabla.setComponentPopupMenu(pop2);
                pop2.setLocation(third_window_rbp.back_tw.getLocationOnScreen().x + 320,
                        third_window_rbp.back_tw.getLocationOnScreen().y - 25);

            }
        }
    } 
     @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
