/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import View.StopView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;

/**
 *
 * @author DMM-ADMIN
 */
public class Teclado_Stop_View implements FocusListener,ActionListener,MouseListener{
    
    StopView stop_view;

    public Teclado_Stop_View(StopView stop_view) {
        this.stop_view = stop_view;
        escuchadores();
    }

    
    public void escuchadores(){
        stop_view.letterQ.addActionListener(this);
        stop_view.letterW.addActionListener(this);
        stop_view.letterE.addActionListener(this);
        stop_view.letterR.addActionListener(this);
        stop_view.letterT.addActionListener(this);
        stop_view.letterY.addActionListener(this);
        stop_view.letterU.addActionListener(this);
        stop_view.letterI.addActionListener(this);
        stop_view.letterO.addActionListener(this);
        stop_view.letterP.addActionListener(this);
        stop_view.letterA.addActionListener(this);
        stop_view.letterS.addActionListener(this);
        stop_view.letterD.addActionListener(this);
        stop_view.letterG.addActionListener(this);
        stop_view.letterH.addActionListener(this);
        stop_view.letterJ.addActionListener(this);
        stop_view.letterF.addActionListener(this);
        stop_view.letterL.addActionListener(this);
        stop_view.letterK.addActionListener(this);
        stop_view.letterZ.addActionListener(this);
        stop_view.letterX.addActionListener(this);
        stop_view.letterC.addActionListener(this);
        stop_view.letterV.addActionListener(this);
        stop_view.letterB.addActionListener(this);
        stop_view.letterN.addActionListener(this);
        stop_view.letterM.addActionListener(this);
        stop_view.letterÑ.addActionListener(this);
        stop_view.letterComa.addActionListener(this);
        stop_view.letterPunto.addActionListener(this);
        stop_view.letterBorrar.addActionListener(this);
        stop_view.numbre0.addActionListener(this);
        stop_view.numbre1.addActionListener(this);
        stop_view.numbre2.addActionListener(this);
        stop_view.numbre3.addActionListener(this);
        stop_view.numbre4.addActionListener(this);
        stop_view.numbre5.addActionListener(this);
        stop_view.numbre6.addActionListener(this);
        stop_view.numbre7.addActionListener(this);
        stop_view.numbre8.addActionListener(this);
        stop_view.numbre9.addActionListener(this);
        stop_view.letterEspacio.addActionListener(this);
        stop_view.jButtonFinalizarStw.addActionListener(this);
    }

    
    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().getClass().toString().equals("class javax.swing.JButton")){
            JButton bt=(JButton)e.getSource();
            if (bt.equals(stop_view.letterQ)) {
                if (!stop_view.letterQ.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterQ.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterQ.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterW)) {
                if (!stop_view.letterW.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterW.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterW.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterE)) {
                if (!stop_view.letterE.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterE.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterE.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterR)) {
                if (!stop_view.letterR.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterR.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterR.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterT)) {
                if (!stop_view.letterT.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterT.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterT.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterY)) {
                if (!stop_view.letterY.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterY.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterY.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterU)) {
                if (!stop_view.letterU.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterU.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterU.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterI)) {
                if (!stop_view.letterI.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterI.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterI.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterO)) {
                if (!stop_view.letterO.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterO.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterO.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterP)) {
                if (!stop_view.letterP.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterP.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterP.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterA)) {
                if (!stop_view.letterA.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterA.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterA.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterS)) {
                if (!stop_view.letterS.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterS.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterS.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterD)) {
                if (!stop_view.letterD.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterD.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterD.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterF)) {
                if (!stop_view.letterF.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterF.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterF.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterG)) {
                if (!stop_view.letterG.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterG.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterG.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterH)) {
                if (!stop_view.letterH.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterH.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterH.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterJ)) {
                if (!stop_view.letterJ.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterJ.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterJ.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterK)) {
                if (!stop_view.letterK.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterK.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterK.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterL)) {
                if (!stop_view.letterL.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterL.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterL.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterÑ)) {
                if (!stop_view.letterÑ.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterÑ.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterÑ.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterZ)) {
                if (!stop_view.letterZ.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterZ.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterZ.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterX)) {
                if (!stop_view.letterX.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterX.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterX.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterC)) {
                if (!stop_view.letterC.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterC.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterC.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterV)) {
                if (!stop_view.letterV.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterV.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterV.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterB)) {
                if (!stop_view.letterB.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterB.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterB.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterN)) {
                if (!stop_view.letterN.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterN.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterN.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterM)) {
                if (!stop_view.letterM.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterM.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterM.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterPunto)) {
                if (!stop_view.letterPunto.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterPunto.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterPunto.getText() + " ");
                }
            }
            if (bt.equals(stop_view.letterComa)) {
                if (!stop_view.letterComa.getText().equalsIgnoreCase(" ")) {
                    stop_view.jTextFieldEspecificacion.setText("" + stop_view.jTextFieldEspecificacion.getText() + stop_view.letterComa.getText());
                } else {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.letterComa.getText() + " ");
                }
            }
            
            if (bt.equals(stop_view.letterEspacio)) {
                stop_view.jTextFieldEspecificacion.setText(stop_view.jTextFieldEspecificacion.getText() + " ");
            }
            if (bt.equals(stop_view.letterBorrar)) {
                if (stop_view.jTextFieldEspecificacion.getText().length() != 0) {
                    stop_view.jTextFieldEspecificacion.setText(stop_view.jTextFieldEspecificacion.getText().substring(0, stop_view.jTextFieldEspecificacion.getText().length() - 1));
                }
            }
           //////////////numeros///////////////////
           
           
           if (bt.equals(stop_view.numbre1)) {
               if(!stop_view.numbre1.getText().equalsIgnoreCase(" ")){
                     stop_view.jTextFieldEspecificacion.setText(""+ stop_view.jTextFieldEspecificacion.getText()+stop_view.numbre1.getText());
                }else{
                    stop_view.jTextFieldEspecificacion.setText(stop_view.numbre1.getText()+" ");
                }
           }
           
           if (bt.equals(stop_view.numbre2)) {
               if(!stop_view.numbre2.getText().equalsIgnoreCase(" ")){
                     stop_view.jTextFieldEspecificacion.setText(""+ stop_view.jTextFieldEspecificacion.getText()+stop_view.numbre2.getText());
                }else{
                    stop_view.jTextFieldEspecificacion.setText(stop_view.numbre2.getText()+" ");
                }
           }
           if (bt.equals(stop_view.numbre3)) {
               if(!stop_view.numbre3.getText().equalsIgnoreCase(" ")){
                     stop_view.jTextFieldEspecificacion.setText(""+ stop_view.jTextFieldEspecificacion.getText()+stop_view.numbre3.getText());
                }else{
                    stop_view.jTextFieldEspecificacion.setText(stop_view.numbre3.getText()+" ");
                }
           }
           if (bt.equals(stop_view.numbre4)) {
               if(!stop_view.numbre4.getText().equalsIgnoreCase(" ")){
                     stop_view.jTextFieldEspecificacion.setText(""+ stop_view.jTextFieldEspecificacion.getText()+stop_view.numbre4.getText());
                }else{
                    stop_view.jTextFieldEspecificacion.setText(stop_view.numbre4.getText()+" ");
                }
           }
           if (bt.equals(stop_view.numbre5)) {
               if(!stop_view.numbre5.getText().equalsIgnoreCase(" ")){
                     stop_view.jTextFieldEspecificacion.setText(""+ stop_view.jTextFieldEspecificacion.getText()+stop_view.numbre5.getText());
                }else{
                    stop_view.jTextFieldEspecificacion.setText(stop_view.numbre5.getText()+" ");
                }
           }
           if (bt.equals(stop_view.numbre6)) {
               if(!stop_view.numbre6.getText().equalsIgnoreCase(" ")){
                     stop_view.jTextFieldEspecificacion.setText(""+ stop_view.jTextFieldEspecificacion.getText()+stop_view.numbre6.getText());
                }else{
                    stop_view.jTextFieldEspecificacion.setText(stop_view.numbre6.getText()+" ");
                }
           }
           if (bt.equals(stop_view.numbre7)) {
               if(!stop_view.numbre7.getText().equalsIgnoreCase(" ")){
                     stop_view.jTextFieldEspecificacion.setText(""+ stop_view.jTextFieldEspecificacion.getText()+stop_view.numbre7.getText());
                }else{
                    stop_view.jTextFieldEspecificacion.setText(stop_view.numbre7.getText()+" ");
                }
           }
           if (bt.equals(stop_view.numbre8)) {
               if(!stop_view.numbre8.getText().equalsIgnoreCase(" ")){
                     stop_view.jTextFieldEspecificacion.setText(""+ stop_view.jTextFieldEspecificacion.getText()+stop_view.numbre8.getText());
                }else{
                    stop_view.jTextFieldEspecificacion.setText(stop_view.numbre8.getText()+" ");
                }
           }
           if (bt.equals(stop_view.numbre9)) {
               if(!stop_view.numbre9.getText().equalsIgnoreCase(" ")){
                     stop_view.jTextFieldEspecificacion.setText(""+ stop_view.jTextFieldEspecificacion.getText()+stop_view.numbre9.getText());
                }else{
                    stop_view.jTextFieldEspecificacion.setText(stop_view.numbre9.getText()+" ");
                }
           }
           if (bt.equals(stop_view.numbre0)) {
               if(!stop_view.numbre0.getText().equalsIgnoreCase(" ")){
                     stop_view.jTextFieldEspecificacion.setText(""+ stop_view.jTextFieldEspecificacion.getText()+stop_view.numbre0.getText());
                }else{
                    stop_view.jTextFieldEspecificacion.setText(stop_view.numbre0.getText()+" ");
                }
           }
           
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
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
