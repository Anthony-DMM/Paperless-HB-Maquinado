/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Utils;

import java.awt.Frame;
import java.util.Stack;
import javax.swing.JFrame;

/**
 *
 * @author ANTHONY-MARTINEZ
 */

public class Navegador {
    private final Stack<Frame> historial = new Stack<>();
    private static Navegador instancia;
    
    private Navegador() {
        
    }
    
    public static Navegador getInstance() {
        if(instancia == null){
            instancia = new Navegador();
        }
        return instancia;
    }
    
    public void avanzar(Frame vistaSiguiente, Frame vistaPasada){
        historial.add(vistaPasada);
        vistaPasada.setVisible(false);
        vistaSiguiente.setVisible(true);
    }
    
    public void regresar(Frame vistaActual){
        vistaActual.setVisible(false);
        historial.pop().setVisible(true);
    }
}