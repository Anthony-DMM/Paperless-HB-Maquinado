/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.RegistroRBPModel;
import Utils.Navegador;
import View.RegistroDASView;
import View.RegistroParoProcesoView;
import View.RegistroRBPView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTextField;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroRBPController implements ActionListener {
    //RegistroRBPModel registroRBPModel = new RegistroRBPModel();
    RegistroRBPView registroRBPView = RegistroRBPView.getInstance();
    Navegador navegador = Navegador.getInstance();
    
    public RegistroRBPController(RegistroRBPView registroRBPView) {
        this.registroRBPView = registroRBPView;
        addListeners();
    }
    
    private void addListeners() {
        registroRBPView.btnDAS.addActionListener(this);
        registroRBPView.btnParoLinea.addActionListener(this);
        registroRBPView.btnCambioMOG.addActionListener(this);
        registroRBPView.btnRegresar.addActionListener(this);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        if (source instanceof JButton) {
            try {
                handleButtonAction((JButton) source);
            } catch (SQLException ex) {
                Logger.getLogger(RegistroRBPController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(RegistroRBPController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void handleButtonAction(JButton button) throws SQLException, ParseException {
        if (button.equals(registroRBPView.btnDAS)) {
            RegistroDASView registroDASView = RegistroDASView.getInstance();
            RegistroDASController registroDASController = new RegistroDASController(registroDASView);
            Navegador.getInstance().avanzar(registroDASView, registroRBPView);
        } else if (button.equals(registroRBPView.btnParoLinea)) {
            RegistroParoProcesoView registroParoProcesoView = new RegistroParoProcesoView();
            RegistroParoProcesoController registroParoProcesoController = new RegistroParoProcesoController(registroParoProcesoView);
            Navegador.getInstance().avanzar(registroParoProcesoView, registroRBPView);
        } else if (button.equals(registroRBPView.btnRegresar)) {
            navegador.regresar(registroRBPView);
        }
    }
}
