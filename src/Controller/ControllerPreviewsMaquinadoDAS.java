/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import View.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 *
 * @author BRYAN-LOPEZ
 */
public class ControllerPreviewsMaquinadoDAS implements ActionListener {
    
    //VISTAS
    PreviewsMaquinadoRBP1 previews_maquinado_rbp1;
    PreviewsMaquinadoDAS previews_maquinado_das;
    Third_windowsRBP third_window_rbp;
    
    public ControllerPreviewsMaquinadoDAS(PreviewsMaquinadoRBP1 previews_maquinado_rbp1,PreviewsMaquinadoDAS previews_maquinado_das,Third_windowsRBP third_window_rbp){
        this.previews_maquinado_rbp1= previews_maquinado_rbp1;
        this.previews_maquinado_das = previews_maquinado_das;
        this.third_window_rbp = third_window_rbp;
        
        escuchadores();
        
    }
    
    public void escuchadores(){
        previews_maquinado_das.jButtonAtrasDAS.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().getClass().toString().equals("class javax.swing.JButton")) {
            JButton bt = (JButton) e.getSource();
            
            if(bt.equals(previews_maquinado_das.jButtonAtrasDAS)){
                previews_maquinado_das.setVisible(false);
                third_window_rbp.setVisible(true);
            }
            
            if(bt.equals(previews_maquinado_das.jButtonSiguienteDAS)){
                previews_maquinado_das.setVisible(false);
                previews_maquinado_rbp1.setVisible(true);
            }
        }
    }
    
}
