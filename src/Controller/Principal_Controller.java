
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Model.*;
import View.*;
import java.awt.Event;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.util.concurrent.ScheduledExecutorService;
import javax.swing.InputMap;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 *
 * @author BRYAN-LOPEZ
 */
public class Principal_Controller {
    
    ChoiceWindow choice_window;
    Login login;
    Metods metods;
    LoginWindow login_window;
    ControllerLogin controller_login;
    public Principal_Controller(Metods metods,ChoiceWindow choice_window, Login login, LoginWindow login_window) throws SQLException, InterruptedException {
        this.metods = metods;
        this.choice_window = choice_window;
        this.login = login;
        this.login_window = login_window;
        login.setVisible(true);
        //mtd.abrirEdge();
        //twrbp.setVisible(true);
        //pegar();
        //escuchadores();
        //escuchadoresSWRBP();
        //swm.hourToday(swrbp);
        //cerrarEdge();
    }
    
    public final void evitar_pegar(JTextField jtextF) {
        InputMap map = jtextF.getInputMap(JTextField.WHEN_FOCUSED);
        map.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, Event.CTRL_MASK), "null");
    }
    
    public void pegar() {
        evitar_pegar(login.txt_user);

    }

}
