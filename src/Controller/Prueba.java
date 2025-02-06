 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Controller;

import Model.*;
import View.*;
import static com.sun.glass.ui.Cursor.setVisible;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import javax.swing.JOptionPane;
/**
 *
 * @author BRYAN-LOPEZ
 */
public class Prueba {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, ParseException, SQLException, InterruptedException {
        
        
        

        //Se inicializa las vistas
        Login login = new Login();
        First_windowRBP first_window_rbp = new First_windowRBP();
        DASRegisterTHMaquinado das_register_maquinado = new DASRegisterTHMaquinado();
        //StopView stopview = new StopView();
        Third_windowsRBP third_window_rbp = new Third_windowsRBP();
        Second_windowRBP second_window_rbp = new Second_windowRBP();
        ChoiceWindow choice_window= new ChoiceWindow();
        CambioDeMog cambio_de_mog= new CambioDeMog();
        PreviewsMaquinadoDAS previews_maquinado_das = new PreviewsMaquinadoDAS();
        PreviewsMaquinadoRBP1 preview_maquinado_rbp1 = new PreviewsMaquinadoRBP1();
        PreviewsMaquinadoRBPSecond preview_maquinado_rbp_second = new PreviewsMaquinadoRBPSecond();
        StopView stop_view = new StopView();
        
        
        // Se iniciazliza las clases de modelos
        
        Metods metods = new Metods();
        //Global global = new Global();
        LoginWindow login_window = new LoginWindow();
        //MetodStopview mtdstvi = new MetodStopview();
        FirstWindow first_window_model = new FirstWindow();
        DasWindow das_window = new DasWindow();
        SecondWindow second_window = new SecondWindow();
        MetodStopview metod_stop_view = new MetodStopview();
        Teclado_Stop_View teclado_stop_view = new Teclado_Stop_View(stop_view);
        Teclado_Das_Window teclado_das_window = new Teclado_Das_Window(das_register_maquinado);
        CleanViews clean_views = new CleanViews(second_window_rbp, first_window_rbp, das_register_maquinado, third_window_rbp,
            choice_window, login,  previews_maquinado_das, preview_maquinado_rbp1, preview_maquinado_rbp_second, stop_view, third_window_rbp);

        
        
        //Controladores de las vistas
        //Controlador del login
        ControllerLogin controller_login = new ControllerLogin(login, first_window_rbp, choice_window, login_window, metods);
        //Controlador principal
        Principal_Controller principal_controller = new Principal_Controller(metods,choice_window, login,login_window);
        //Controlador ventana se seleccion
        ControllerChoiceWindow controller_choice_window = new ControllerChoiceWindow(controller_login, choice_window,first_window_rbp, login, first_window_model);
        //Controlador de datos generales;
        ControllerFirstWindow controller_first_window = new ControllerFirstWindow(first_window_rbp, first_window_model, second_window_rbp, third_window_rbp, metods, 
                controller_login, stop_view,choice_window,das_register_maquinado,second_window, das_window,clean_views);

        //Controlador de la vista de llando
        ControllerSecondWindow controller_second_window = new ControllerSecondWindow(second_window_rbp,first_window_rbp,third_window_rbp,cambio_de_mog,controller_login, 
                controller_first_window,das_register_maquinado, second_window, metods,stop_view,controller_login,metod_stop_view,previews_maquinado_das,
                first_window_model,das_window);
        //Controlador de la vista de DAS
        ControllerDAS controller_das = new ControllerDAS(second_window_rbp, third_window_rbp, das_register_maquinado, controller_first_window, controller_login, metods, 
                das_window,second_window,first_window_model, controller_second_window,teclado_das_window,clean_views,previews_maquinado_das);
        ControllerStopView controller_stop_view = new ControllerStopView(preview_maquinado_rbp1, stop_view, second_window_rbp, metod_stop_view, metods, teclado_stop_view, 
                first_window_rbp, first_window_model, previews_maquinado_das);
        ControllerPreviewsMaquinadoDAS controller_previews_maquinado_das= new ControllerPreviewsMaquinadoDAS(preview_maquinado_rbp1,previews_maquinado_das,third_window_rbp);
        
    }
   
} 