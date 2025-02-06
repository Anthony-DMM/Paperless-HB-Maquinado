/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import View.*;
import javax.swing.table.DefaultTableModel;




/**
 *
 * @author DAVID-GARCIA
 */
public class CleanViews {
    
    //Vistas
    Second_windowRBP second_window_rbp;
    First_windowRBP first_window_rbp;
    DASRegisterTHMaquinado das_register_maquinado;
    Third_windowsRBP third_window_rbp;
    ChoiceWindow choice_window;
    Login login;
    PreviewsMaquinadoDAS preview_maquinado_das;
    PreviewsMaquinadoRBP1 preview_maquinado_rbp1;
    PreviewsMaquinadoRBPSecond preview_maquinado_rbp_second;
    StopView stop_view;
    Third_windowsRBP third_windowRBP;
    
    
    public CleanViews( Second_windowRBP second_window_rbp, First_windowRBP first_window_rbp, DASRegisterTHMaquinado das_register_maquinado,
    Third_windowsRBP third_window_rbp,  ChoiceWindow choice_window, Login login, PreviewsMaquinadoDAS preview_maquinado_das, PreviewsMaquinadoRBP1 preview_maquinado_rbp1,
    PreviewsMaquinadoRBPSecond preview_maquinado_rbp_second, StopView stop_view, Third_windowsRBP third_windowRBP){
        
        this.second_window_rbp = second_window_rbp;
        this.first_window_rbp = first_window_rbp;
        this.das_register_maquinado = das_register_maquinado;
        this.third_window_rbp = third_window_rbp;
        this.choice_window = choice_window;
        this.login = login;
        this.preview_maquinado_das = preview_maquinado_das;
        this.preview_maquinado_rbp1 = preview_maquinado_rbp1;
        this.preview_maquinado_rbp_second = preview_maquinado_rbp_second;
        this.stop_view = stop_view;
        this.third_windowRBP = third_windowRBP;
    }
    
    public void CleanFirstWindow(){
    
        first_window_rbp.linenumber_fw.setText("");
        first_window_rbp.supervisor_fw.setText("");
        first_window_rbp.manufacturingorder_fw.setText("");
        first_window_rbp.MOG_fw.setText("");
        first_window_rbp.article_fw.setText("");
        first_window_rbp.drawingnumber_fw.setText("");
        first_window_rbp.process_fw.setText("");
        first_window_rbp.partNumber.setText("");


    }
    
    public void CleanOrderManufacturing(){
            first_window_rbp.MOG_fw.setText("");
            first_window_rbp.MOG_fw.setText("");
            first_window_rbp.article_fw.setText("");
            first_window_rbp.drawingnumber_fw.setText("");
            first_window_rbp.process_fw.setText("");
            first_window_rbp.partNumber.setText("");
            first_window_rbp.manufacturingorder_fw.setText(null);
    } 
    
    public void CleanSecondWindow(){
        second_window_rbp.jTextFieldCodeSW.setText("");
        second_window_rbp.jTextFieldNameSW.setText("");
        second_window_rbp.jDateChooserDateSW.setDate(null);
        second_window_rbp.jTextFieldCodeSW.setText(null);
        second_window_rbp.jTextFieldNameSW.setText(null);
        second_window_rbp.jTextFieldStartSW.setText(null);
        second_window_rbp.jTextFieldEndSW.setText(null);
        second_window_rbp.jTextFieldTotalSW.setText(null);
        second_window_rbp.jComboBoxTurn.removeAllItems();
        second_window_rbp.jComboBoxTurn.addItem("Selecciona");
        second_window_rbp.jComboBoxTurn.addItem("Turno 1");
        second_window_rbp.jComboBoxTurn.addItem("Turno 2");
        second_window_rbp.jComboBoxTurn.addItem("Turno 3");
        second_window_rbp.jTextFieldRSW1.setText(null);
        second_window_rbp.jTextFieldFSW1.setText(null);
        second_window_rbp.jTextFieldCSW1.setText(null);
        second_window_rbp.jTextFieldCantSW1.setText(null);
        second_window_rbp.jTextFieldRSW2.setText(null);
        second_window_rbp.jTextFieldFSW2.setText(null);
        second_window_rbp.jTextFieldCSW2.setText(null);
        second_window_rbp.jTextFieldRSW3.setText(null);
        second_window_rbp.jTextFieldFSW3.setText(null);
        second_window_rbp.jTextFieldSSW4.setText(null);

    }
    
    public void CleanDasWindow(){
        das_register_maquinado.jTextFieldacumulado.setText("");
        das_register_maquinado.jTextFieldNoEmpleado.setText("");
        das_register_maquinado.jLabelnombreOperador.setText("");
        das_register_maquinado.jTextFieldLoteMaquinado.setText("");
        das_register_maquinado.jCheckBoxNG.setSelected(false); 
        das_register_maquinado.jCheckBoxOK.setSelected(false); 
        DefaultTableModel dtm = (DefaultTableModel) das_register_maquinado.jTablePzasxHora.getModel();
        dtm.setRowCount(0);
    }
    
}
