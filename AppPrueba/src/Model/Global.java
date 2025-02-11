/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * Clase creada para tener variables "GLOBALES" 
 * durante la ejecucion de la aplicacion
 * @author RICARDO-DIAZ
 * @version v.1.0 30/april/2021
 */
public class Global {
    
    //REMPLAZAR TODOS LOS ***  System.out.println  *** POR    ***   if(Global.DEBUGMODE)System.out.println  ***  
    public final static boolean DEBUGMODE=true; ///****CAMBIAR A FALSE EN MODO PRODUCCION****///
    public  static String   VERSION="v.1.1.2"; //Versionamiento
    private static int      VAR=0;           //Variable globlal
    private static int      ID_USUARIO=0;   //ID unico
    private static int      TIPO_USUARIO=0; //Aduana,Supervisor,Operador,Lider
    private static String   MOG="";         //MOG
    private static int      RBP=0;         //RBP

    //Metodo para obtener el valor de la variable VAR.
    public static int getVar(){return Global.VAR;}
    //Si nunca se va a cambiar la variable, borrar el metodo.
    public static void setVar(int var){Global.VAR = VAR;}

    //Metodo para obtener el valor de la variable ID_USUARIO.
    public static int getID_USUARIO(){return Global.ID_USUARIO;}
    //Si nunca se va a cambiar la variable, borrar el metodo.
    public static void setID_USUARIO(int var){Global.ID_USUARIO = ID_USUARIO;}

    //Metodo para obtener el valor de la variable TIPO_USUARIO.
    public static int getTIPO_USUARIO(){return Global.TIPO_USUARIO;}
    //Si nunca se va a cambiar la variable, borrar el metodo.
    public static void setTIPO_USUARIO(int var){Global.TIPO_USUARIO = TIPO_USUARIO;}

    //Metodo para obtener el valor de la variable MOG.
    public static String getMOG(){return Global.MOG;}
    //Si nunca se va a cambiar la variable, borrar el metodo.
    public static void setMOG(String var){Global.MOG = MOG;}

    //Metodo para obtener el valor de la variable RBP.
    public static int getRBP(){return Global.RBP;}
    //Si nunca se va a cambiar la variable, borrar el metodo.
    public static void setRBP(int var){Global.RBP = RBP;}
    
}
