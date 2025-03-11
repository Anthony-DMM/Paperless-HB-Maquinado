/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Utils.Navegador;
import View.DibujoView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class DibujoController implements ActionListener {
    private final DibujoView dibujoView;
    private final Navegador navegador = Navegador.getInstance();
    
    public DibujoController(DibujoView dibujoView) {
        this.dibujoView = dibujoView;
        dibujoView.btnRegresar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        navegador.regresar(dibujoView);
    }
}
