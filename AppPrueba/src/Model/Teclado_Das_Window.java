/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import View.DASRegisterTHMaquinado;
import View.StopView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

/**
 *
 * @author BRYAN-LOPEZ
 */
public class Teclado_Das_Window implements ActionListener {
    DASRegisterTHMaquinado das_register_maquinado;

    public Teclado_Das_Window(DASRegisterTHMaquinado das_register_maquinado) {
        this.das_register_maquinado = das_register_maquinado;
        escuchadores();
    }

    
    public void escuchadores(){
        das_register_maquinado.letterBorrar.addActionListener(this);
        das_register_maquinado.numbre0.addActionListener(this);
        das_register_maquinado.numbre1.addActionListener(this);
        das_register_maquinado.numbre2.addActionListener(this);
        das_register_maquinado.numbre3.addActionListener(this);
        das_register_maquinado.numbre4.addActionListener(this);
        das_register_maquinado.numbre5.addActionListener(this);
        das_register_maquinado.numbre6.addActionListener(this);
        das_register_maquinado.numbre7.addActionListener(this);
        das_register_maquinado.numbre8.addActionListener(this);
        das_register_maquinado.numbre9.addActionListener(this);
    }

    

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().getClass().toString().equals("class javax.swing.JButton")){
            JButton bt=(JButton)e.getSource();
            
            if (bt.equals(das_register_maquinado.letterBorrar)) {
                if (das_register_maquinado.jTextFieldLoteMaquinado.getText().length() != 0) {
                    das_register_maquinado.jTextFieldLoteMaquinado.setText(das_register_maquinado.jTextFieldLoteMaquinado.getText().substring(0, das_register_maquinado.jTextFieldLoteMaquinado.getText().length() - 1));
                }
            }

           //////////////numeros///////////////////
           
           
           if (bt.equals(das_register_maquinado.numbre1)) { 
               if(!das_register_maquinado.numbre1.getText().equalsIgnoreCase(" ")){
                     das_register_maquinado.jTextFieldLoteMaquinado.setText(""+ das_register_maquinado.jTextFieldLoteMaquinado.getText()+das_register_maquinado.numbre1.getText());
                }else{
                    das_register_maquinado.jTextFieldLoteMaquinado.setText(das_register_maquinado.numbre1.getText()+" ");
                }
           }
           
           if (bt.equals(das_register_maquinado.numbre2)) {
               if(!das_register_maquinado.numbre2.getText().equalsIgnoreCase(" ")){
                     das_register_maquinado.jTextFieldLoteMaquinado.setText(""+ das_register_maquinado.jTextFieldLoteMaquinado.getText()+das_register_maquinado.numbre2.getText());
                }else{
                    das_register_maquinado.jTextFieldLoteMaquinado.setText(das_register_maquinado.numbre2.getText()+" ");
                }
           }
           if (bt.equals(das_register_maquinado.numbre3)) {
               if(!das_register_maquinado.numbre3.getText().equalsIgnoreCase(" ")){
                     das_register_maquinado.jTextFieldLoteMaquinado.setText(""+ das_register_maquinado.jTextFieldLoteMaquinado.getText()+das_register_maquinado.numbre3.getText());
                }else{
                    das_register_maquinado.jTextFieldLoteMaquinado.setText(das_register_maquinado.numbre3.getText()+" ");
                }
           }
           if (bt.equals(das_register_maquinado.numbre4)) {
               if(!das_register_maquinado.numbre4.getText() .equalsIgnoreCase(" ")){
                     das_register_maquinado.jTextFieldLoteMaquinado.setText(""+ das_register_maquinado.jTextFieldLoteMaquinado.getText()+das_register_maquinado.numbre4.getText());
                }else{
                    das_register_maquinado.jTextFieldLoteMaquinado.setText(das_register_maquinado.numbre4.getText()+" ");
                }
           }
           if (bt.equals(das_register_maquinado.numbre5)) {
               if(!das_register_maquinado.numbre5.getText().equalsIgnoreCase(" ")){
                     das_register_maquinado.jTextFieldLoteMaquinado.setText(""+ das_register_maquinado.jTextFieldLoteMaquinado.getText()+das_register_maquinado.numbre5.getText());
                }else{
                    das_register_maquinado.jTextFieldLoteMaquinado.setText(das_register_maquinado.numbre5.getText()+" ");
                }
           }
           if (bt.equals(das_register_maquinado.numbre6)) {
               if(!das_register_maquinado.numbre6.getText().equalsIgnoreCase(" ")){
                     das_register_maquinado.jTextFieldLoteMaquinado.setText(""+ das_register_maquinado.jTextFieldLoteMaquinado.getText()+das_register_maquinado.numbre6.getText());
                }else{
                    das_register_maquinado.jTextFieldLoteMaquinado.setText(das_register_maquinado.numbre6.getText()+" ");
                }
           }
           if (bt.equals(das_register_maquinado.numbre7)) {
               if(!das_register_maquinado.numbre7.getText().equalsIgnoreCase(" ")){
                     das_register_maquinado.jTextFieldLoteMaquinado.setText(""+ das_register_maquinado.jTextFieldLoteMaquinado.getText()+das_register_maquinado.numbre7.getText());
                }else{
                    das_register_maquinado.jTextFieldLoteMaquinado.setText(das_register_maquinado.numbre7.getText()+" ");
                }
           }
           if (bt.equals(das_register_maquinado.numbre8)) {
               if(!das_register_maquinado.numbre8.getText().equalsIgnoreCase(" ")){
                     das_register_maquinado.jTextFieldLoteMaquinado.setText(""+ das_register_maquinado.jTextFieldLoteMaquinado.getText()+das_register_maquinado.numbre8.getText());
                }else{
                    das_register_maquinado.jTextFieldLoteMaquinado.setText(das_register_maquinado.numbre8.getText()+" ");
                }
           }
           if (bt.equals(das_register_maquinado.numbre9)) {
               if(!das_register_maquinado.numbre9.getText().equalsIgnoreCase(" ")){
                     das_register_maquinado.jTextFieldLoteMaquinado.setText(""+ das_register_maquinado.jTextFieldLoteMaquinado.getText()+das_register_maquinado.numbre9.getText());
                }else{
                    das_register_maquinado.jTextFieldLoteMaquinado.setText(das_register_maquinado.numbre9.getText()+" "); 
                }
           }
           if (bt.equals(das_register_maquinado.numbre0)) {
               if(!das_register_maquinado.numbre0.getText().equalsIgnoreCase(" ")){
                     das_register_maquinado.jTextFieldLoteMaquinado.setText(""+ das_register_maquinado.jTextFieldLoteMaquinado.getText()+das_register_maquinado.numbre0.getText());
                }else{
                    das_register_maquinado.jTextFieldLoteMaquinado.setText(das_register_maquinado.numbre0.getText()+" ");
                }
           }
           
        }
    }
    
    

  
}
