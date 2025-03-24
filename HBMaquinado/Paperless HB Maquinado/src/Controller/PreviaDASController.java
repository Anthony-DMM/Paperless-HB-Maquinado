/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Entities.DAS;
import Entities.Operador;
import Entities.RBP;
import Interfaces.LineaProduccion;
import Model.PreviaDASModel;
import View.PreviaDASView;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class PreviaDASController {
    private final LineaProduccion datosLineaProduccion = LineaProduccion.getInstance();
    private final DAS datosDAS = DAS.getInstance();
    private final RBP datosRBP = RBP.getInstance();
    private final Operador datosOperador = Operador.getInstance();
    
    private final PreviaDASView previaDASView = PreviaDASView.getInstance();
    private final PreviaDASModel previaDASModel = new PreviaDASModel();

    public PreviaDASController() {
        previaDASView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                cargarDatos();
            }
        });
    }
    
    private void cargarDatos() {
        previaDASView.lblLinea.setText(datosLineaProduccion.getLinea());
        previaDASView.lblGrupo.setText(String.valueOf(datosLineaProduccion.getGrupo()));

        String fecha = datosRBP.getFecha();
        if (fecha != null && !fecha.isEmpty()) {
            String[] partes = fecha.split("-");
            if (partes.length == 3) {
                previaDASView.lblAnio.setText(partes[0]);
                previaDASView.lblMes.setText(partes[1]);
                previaDASView.lblDia.setText(partes[2]);
            } else {
                previaDASView.lblDia.setText("Formato incorrecto");
            }
        } else {
            previaDASView.lblDia.setText("Fecha no disponible");
        }

        previaDASView.lblTurno.setText(String.valueOf(datosDAS.getTurno()));
        previaDASView.lblNombreKeeper.setText(datosDAS.getNombreSoporteRapido());
        previaDASView.lblNombreOperador.setText(datosOperador.getNombre());
        previaDASView.lblNombreInspector.setText(datosDAS.getNombreInspector());
        previaDASView.lblCantidadPiezasMeta.setText(String.valueOf(datosDAS.getPiezasMeta()));
    }
    
}
