/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import View.Third_windowsRBP;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
/**
 *
 * @author DMM-ADMIN
 */
public class Teclado_Third_Window_RBP  extends JPanel{
  JTable txt;
    String teclas[]={"1","2","3","4","5","6","7","8","9","0","<","cerrar"};
    ArrayList<JButton> botones=new ArrayList<JButton>();
    JPanel pletras,pespacio;

    public Teclado_Third_Window_RBP(JTable t,int columm1,Third_windowsRBP th){
        txt=t;
        int fila=txt.getSelectedRow();
        int column=columm1 + 1;
        pletras=new JPanel();
        setLayout(new BorderLayout());
        pletras.setLayout(new GridLayout(4,30,10,10));
       

        ActionListener accion=new ActionListener(){

         @Override
         public void actionPerformed(ActionEvent e) {
            JButton b=(JButton)e.getSource();
            String k;
            if(fila<=65){
                if(!b.getText().equalsIgnoreCase(" ")){
                    if(txt.getValueAt(fila, column)==null){
                        k= b.getText();
                    }else{
                        k=txt.getValueAt(fila, column)+ b.getText();
                    }

                    txt.setValueAt(k,fila,column);
                
                }else{
                    String valor=txt.getValueAt(fila, column)+" ";
                    txt.setValueAt(valor,fila,column);
                }

                if(b.getText().equals("<")){
                    if(txt.getValueAt(fila, column).toString().length()!=0){
                        txt.setValueAt("0", fila, column);
                    }
                }
                if(b.getText().equals("cerrar")){
                    StringBuilder sb = new StringBuilder();
                    String h=(String) txt.getValueAt(fila, column);
                    String p = null;

                    if(h!=""){
                        for (int i=0; i<h.length(); i++){
                            char letra = h.charAt(i);
                            if(letra!='c' && letra!='e' && letra!='r' && letra!='a'){
                                sb.append(h.charAt(i));
                                p=sb.toString();
                           }else{

                           }
                        }
                        txt.setValueAt(p,fila,column);
                        //pespacio.setEnabled(false);
                        Window win=SwingUtilities.getWindowAncestor(pespacio);
                        win.setVisible(false);
                    }  
                }
            }
        }

        };
       // TableModel mdl = (DefaultTableModel)th.Tabla.getModel();
        //mdl.getValueAt(th.Tabla.getSelectedRow(), th.Tabla.getSelectedColumn());
        
        for(int i=0;i<12;i++){
         if(teclas[i].equalsIgnoreCase(" ")){
            JLabel l=new JLabel();
            pletras.add(l);
         }
            JButton b=new JButton(teclas[i]);
            b.addActionListener(accion);
            pletras.add(b);
            botones.add(b);
        }

        pespacio=new JPanel(new GridLayout(1,3));
        JButton borrar=new JButton(" ");
        borrar.addActionListener(accion);
        pespacio.add(new JLabel());
        pespacio.add(new JLabel());
        add(pletras);
        add(pespacio,BorderLayout.SOUTH); 
       }
}
