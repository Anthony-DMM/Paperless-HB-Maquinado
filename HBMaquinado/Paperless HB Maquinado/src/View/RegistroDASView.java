/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Config.VistaSingleton;
import Utils.FiltroCampos;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.text.AbstractDocument;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroDASView extends javax.swing.JFrame {

    /**
     * Creates new form RegistroDASView
     */
    private RegistroDASView() {
        initComponents();
        setLocationRelativeTo(null);
        btnRegistrarProduccion.setBackground(Color.BLACK);
        btnRegresar.setBackground(Color.BLACK);
        btnFinalizarDAS.setBackground(Color.BLACK);
        btnParoProceso.setBackground(Color.RED);
        btnParoProceso.setText("<html>Paro en<br>Proceso</html>");
        
        ((AbstractDocument) txtCodigoSoporte.getDocument()).setDocumentFilter(new FiltroCampos.FiltroNumerosYSignoNumeral());
        ((AbstractDocument) txtCodigoInspector.getDocument()).setDocumentFilter(new FiltroCampos.FiltroNumerosYSignoNumeral());
        ((AbstractDocument) txtNumeroEmpleado.getDocument()).setDocumentFilter(new FiltroCampos.FiltroNumerosYSignoNumeral());
        ((AbstractDocument) txtLote.getDocument()).setDocumentFilter(new FiltroCampos.FiltroNumerosYLetras());
        ((AbstractDocument) txtAcumulado.getDocument()).setDocumentFilter(new FiltroCampos.FiltroSoloNumeros());
        
        
        JTextField dateTextField = (JTextField) jdcFecha.getDateEditor().getUiComponent();
        dateTextField.setDisabledTextColor(Color.BLACK);
        
        
        JTableHeader header = tblHoraxHora.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);

    
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setFont(new Font("Arial", Font.PLAIN, 14));
        renderer.setHorizontalAlignment(JLabel.CENTER);

        
        for (int i = 0; i < tblHoraxHora.getColumnCount(); i++) {
            tblHoraxHora.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        tblHoraxHora.setRowHeight(35);
    }
    
    public static RegistroDASView getInstance() {
        return VistaSingleton.getInstance(RegistroDASView.class);
    }

    public JButton getBtnFinalizarDAS() {
        return btnFinalizarDAS;
    }

    public void setBtnFinalizarDAS(JButton btnFinalizarDAS) {
        this.btnFinalizarDAS = btnFinalizarDAS;
    }

    public JButton getBtnRegistrarProduccion() {
        return btnRegistrarProduccion;
    }

    public void setBtnRegistrarProduccion(JButton btnRegistrarProduccion) {
        this.btnRegistrarProduccion = btnRegistrarProduccion;
    }

    public JButton getBtnRegresar() {
        return btnRegresar;
    }

    public void setBtnRegresar(JButton btnRegresar) {
        this.btnRegresar = btnRegresar;
    }

    public JPasswordField getTxtCodigoSoporte() {
        return txtCodigoSoporte;
    }

    public void setTxtCodigoSoporte(JPasswordField txtCodigoSoporte) {
        this.txtCodigoSoporte = txtCodigoSoporte;
    }

    public JPasswordField getTxtCodigoInspector() {
        return txtCodigoInspector;
    }

    public void setTxtCodigoInspector(JPasswordField txtCodigoInspector) {
        this.txtCodigoInspector = txtCodigoInspector;
    }

    public JPasswordField getTxtNumeroEmpleado() {
        return txtNumeroEmpleado;
    }

    public void setTxtNumeroEmpleado(JPasswordField txtNumeroEmpleado) {
        this.txtNumeroEmpleado = txtNumeroEmpleado;
    }

    public JTextField getTxtNombreEmpleado() {
        return txtNombreEmpleado;
    }

    public void setTxtNombreEmpleado(JTextField txtNombreEmpleado) {
        this.txtNombreEmpleado = txtNombreEmpleado;
    }

    public JTextField getTxtNombreInspector() {
        return txtNombreInspector;
    }

    public void setTxtNombreInspector(JTextField txtNombreInspector) {
        this.txtNombreInspector = txtNombreInspector;
    }

    public JTextField getTxtNombreSoporteRapido() {
        return txtNombreSoporteRapido;
    }

    public void setTxtNombreSoporteRapido(JTextField txtNombreSoporteRapido) {
        this.txtNombreSoporteRapido = txtNombreSoporteRapido;
    }

    public JButton getBtnParoProceso() {
        return btnParoProceso;
    }

    public void setBtnParoProceso(JButton btnParoProceso) {
        this.btnParoProceso = btnParoProceso;
    }
    
    
    
    
    
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jDateChooserDateSW = new com.toedter.calendar.JDateChooser();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jdcFecha = new com.toedter.calendar.JDateChooser();
        jTextField1 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtNombreSoporteRapido = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        txtNombreInspector = new javax.swing.JTextField();
        txtCodigoInspector = new javax.swing.JPasswordField();
        txtCodigoSoporte = new javax.swing.JPasswordField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtNumeroEmpleado = new javax.swing.JPasswordField();
        txtNombreEmpleado = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtMOG = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtModelo = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtSTD = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtLote = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        txtHora = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        txtAcumulado = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cbxOK = new javax.swing.JCheckBox();
        cbxNG = new javax.swing.JCheckBox();
        btnRegistrarProduccion = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoraxHora = new javax.swing.JTable();
        btnRegresar = new javax.swing.JButton();
        btnFinalizarDAS = new javax.swing.JButton();
        btnParoProceso = new javax.swing.JButton();

        jDateChooserDateSW.setBackground(new java.awt.Color(204, 204, 204));
        jDateChooserDateSW.setEnabled(false);
        jDateChooserDateSW.setFont(new java.awt.Font("Arial", 0, 48)); // NOI18N
        jDateChooserDateSW.setPreferredSize(new java.awt.Dimension(100, 40));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(810, 1550));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 44)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("ACTIVIDAD DIARIA \"HB MAQUINADO\"");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 26)); // NOI18N
        jLabel5.setText("Grupo:");

        jLabel6.setFont(new java.awt.Font("Arial", 0, 26)); // NOI18N
        jLabel6.setText("Fecha:");

        jdcFecha.setBackground(new java.awt.Color(204, 204, 204));
        jdcFecha.setEnabled(false);
        jdcFecha.setFont(new java.awt.Font("Arial", 0, 40)); // NOI18N
        jdcFecha.setPreferredSize(new java.awt.Dimension(100, 40));

        jTextField1.setEditable(false);
        jTextField1.setBackground(new java.awt.Color(204, 204, 204));
        jTextField1.setFont(new java.awt.Font("Arial", 0, 40)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Arial", 0, 26)); // NOI18N
        jLabel7.setText(" Código de Soporte Rápido:");
        jLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel8.setFont(new java.awt.Font("Arial", 0, 26)); // NOI18N
        jLabel8.setText(" Nombre de Soporte Rápido:");
        jLabel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtNombreSoporteRapido.setEditable(false);
        txtNombreSoporteRapido.setBackground(new java.awt.Color(204, 204, 204));
        txtNombreSoporteRapido.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        txtNombreSoporteRapido.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel9.setFont(new java.awt.Font("Arial", 0, 26)); // NOI18N
        jLabel9.setText(" Código de Inspector:");
        jLabel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel10.setFont(new java.awt.Font("Arial", 0, 26)); // NOI18N
        jLabel10.setText(" Nombre de Inspector:");
        jLabel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtNombreInspector.setEditable(false);
        txtNombreInspector.setBackground(new java.awt.Color(204, 204, 204));
        txtNombreInspector.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        txtNombreInspector.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtCodigoInspector.setBackground(new java.awt.Color(255, 255, 0));
        txtCodigoInspector.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        txtCodigoInspector.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtCodigoInspector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoInspectorActionPerformed(evt);
            }
        });
        txtCodigoInspector.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoInspectorKeyTyped(evt);
            }
        });

        txtCodigoSoporte.setBackground(new java.awt.Color(255, 255, 0));
        txtCodigoSoporte.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        txtCodigoSoporte.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtCodigoSoporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCodigoSoporteActionPerformed(evt);
            }
        });
        txtCodigoSoporte.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCodigoSoporteKeyTyped(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Arial", 0, 26)); // NOI18N
        jLabel11.setText(" Número de Empleado:");
        jLabel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel12.setFont(new java.awt.Font("Arial", 0, 26)); // NOI18N
        jLabel12.setText(" Nombre de Empleado:");
        jLabel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtNumeroEmpleado.setBackground(new java.awt.Color(255, 255, 0));
        txtNumeroEmpleado.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        txtNumeroEmpleado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        txtNumeroEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroEmpleadoActionPerformed(evt);
            }
        });
        txtNumeroEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumeroEmpleadoKeyTyped(evt);
            }
        });

        txtNombreEmpleado.setEditable(false);
        txtNombreEmpleado.setBackground(new java.awt.Color(204, 204, 204));
        txtNombreEmpleado.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        txtNombreEmpleado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Arial", 0, 22)); // NOI18N
        jLabel2.setText("Registro de Producción");

        jLabel13.setFont(new java.awt.Font("Arial", 0, 26)); // NOI18N
        jLabel13.setText(" Número de Orden de Producción:");
        jLabel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtMOG.setEditable(false);
        txtMOG.setBackground(new java.awt.Color(204, 204, 204));
        txtMOG.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        txtMOG.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel14.setFont(new java.awt.Font("Arial", 0, 26)); // NOI18N
        jLabel14.setText(" Modelo:");
        jLabel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtModelo.setEditable(false);
        txtModelo.setBackground(new java.awt.Color(204, 204, 204));
        txtModelo.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        txtModelo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel15.setFont(new java.awt.Font("Arial", 0, 26)); // NOI18N
        jLabel15.setText(" STD:");
        jLabel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtSTD.setEditable(false);
        txtSTD.setBackground(new java.awt.Color(204, 204, 204));
        txtSTD.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        txtSTD.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel16.setFont(new java.awt.Font("Arial", 0, 26)); // NOI18N
        jLabel16.setText(" Lote:");
        jLabel16.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtLote.setEditable(false);
        txtLote.setBackground(new java.awt.Color(204, 204, 204));
        txtLote.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        txtLote.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 22)); // NOI18N
        jLabel3.setText("Registro Hora x Hora");

        jLabel20.setFont(new java.awt.Font("Arial", 0, 26)); // NOI18N
        jLabel20.setText(" Hora:");
        jLabel20.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtHora.setEditable(false);
        txtHora.setBackground(new java.awt.Color(204, 204, 204));
        txtHora.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        txtHora.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel21.setFont(new java.awt.Font("Arial", 0, 26)); // NOI18N
        jLabel21.setText(" Acumulado:");
        jLabel21.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtAcumulado.setBackground(new java.awt.Color(255, 255, 0));
        txtAcumulado.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        txtAcumulado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel22.setFont(new java.awt.Font("Arial", 0, 26)); // NOI18N
        jLabel22.setText(" Calidad:");
        jLabel22.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel2.setBackground(new java.awt.Color(255, 255, 0));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        cbxOK.setBackground(new java.awt.Color(255, 255, 0));
        cbxOK.setFont(new java.awt.Font("Arial", 0, 28)); // NOI18N
        cbxOK.setText("OK");
        cbxOK.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cbxOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxOKActionPerformed(evt);
            }
        });

        cbxNG.setBackground(new java.awt.Color(255, 255, 0));
        cbxNG.setFont(new java.awt.Font("Arial", 0, 28)); // NOI18N
        cbxNG.setText("NG");
        cbxNG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cbxNG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxNGActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(cbxOK, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(cbxNG, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(24, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(cbxNG, javax.swing.GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
                .addComponent(cbxOK, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnRegistrarProduccion.setFont(new java.awt.Font("Arial", 0, 32)); // NOI18N
        btnRegistrarProduccion.setForeground(new java.awt.Color(255, 255, 255));
        btnRegistrarProduccion.setText("Registrar producción");
        btnRegistrarProduccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarProduccionActionPerformed(evt);
            }
        });

        tblHoraxHora.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Hora", "Piezas x Hora", "Acumulado", "Ok / Ng", "Nombre"
            }
        ));
        jScrollPane1.setViewportView(tblHoraxHora);

        btnRegresar.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        btnRegresar.setForeground(new java.awt.Color(255, 255, 255));
        btnRegresar.setText("Regresar");
        btnRegresar.setMaximumSize(new java.awt.Dimension(120, 60));
        btnRegresar.setMinimumSize(new java.awt.Dimension(120, 60));
        btnRegresar.setPreferredSize(new java.awt.Dimension(350, 80));

        btnFinalizarDAS.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        btnFinalizarDAS.setForeground(new java.awt.Color(255, 255, 255));
        btnFinalizarDAS.setText("Finalizar DAS");
        btnFinalizarDAS.setMaximumSize(new java.awt.Dimension(120, 60));
        btnFinalizarDAS.setMinimumSize(new java.awt.Dimension(120, 60));
        btnFinalizarDAS.setPreferredSize(new java.awt.Dimension(120, 80));

        btnParoProceso.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        btnParoProceso.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(18, 18, 18)
                        .addComponent(jdcFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1))
                    .addComponent(txtNombreSoporteRapido, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCodigoSoporte, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnFinalizarDAS, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jScrollPane1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombreInspector, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCodigoInspector, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombreEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNumeroEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(txtLote, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(txtMOG, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(txtModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(txtSTD, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, 275, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtAcumulado)
                            .addComponent(txtHora)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnParoProceso, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnRegistrarProduccion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jLabel1)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jdcFecha, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(txtCodigoSoporte))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreSoporteRapido))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCodigoInspector, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombreInspector, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNumeroEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNombreEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addComponent(jLabel2)
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtMOG))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtModelo))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSTD))
                .addGap(0, 0, 0)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtLote))
                .addGap(30, 30, 30)
                .addComponent(jLabel3)
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                            .addComponent(txtHora))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAcumulado))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(btnParoProceso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(37, 37, 37)
                .addComponent(btnRegistrarProduccion, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFinalizarDAS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(45, 45, 45)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtCodigoInspectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoInspectorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoInspectorActionPerformed

    private void txtCodigoSoporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCodigoSoporteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCodigoSoporteActionPerformed

    private void txtNumeroEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroEmpleadoActionPerformed

    private void txtCodigoSoporteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoSoporteKeyTyped
        if(txtCodigoSoporte.getText().length() >= 12)
        {
            evt.consume();
        }
    }//GEN-LAST:event_txtCodigoSoporteKeyTyped

    private void txtCodigoInspectorKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCodigoInspectorKeyTyped
        if(txtCodigoInspector.getText().length() >= 12)
        {
            evt.consume();
        }
    }//GEN-LAST:event_txtCodigoInspectorKeyTyped

    private void txtNumeroEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroEmpleadoKeyTyped
        if(txtNumeroEmpleado.getText().length() >= 12)
        {
            evt.consume();
        }
    }//GEN-LAST:event_txtNumeroEmpleadoKeyTyped

    private void btnRegistrarProduccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarProduccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegistrarProduccionActionPerformed

    private void cbxOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxOKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxOKActionPerformed

    private void cbxNGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxNGActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxNGActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(RegistroDASView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegistroDASView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegistroDASView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegistroDASView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegistroDASView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnFinalizarDAS;
    public javax.swing.JButton btnParoProceso;
    public javax.swing.JButton btnRegistrarProduccion;
    public javax.swing.JButton btnRegresar;
    public javax.swing.JCheckBox cbxNG;
    public javax.swing.JCheckBox cbxOK;
    public com.toedter.calendar.JDateChooser jDateChooserDateSW;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    public com.toedter.calendar.JDateChooser jdcFecha;
    public javax.swing.JTable tblHoraxHora;
    public javax.swing.JTextField txtAcumulado;
    public javax.swing.JPasswordField txtCodigoInspector;
    public javax.swing.JPasswordField txtCodigoSoporte;
    public javax.swing.JTextField txtHora;
    public javax.swing.JTextField txtLote;
    public javax.swing.JTextField txtMOG;
    public javax.swing.JTextField txtModelo;
    public javax.swing.JTextField txtNombreEmpleado;
    public javax.swing.JTextField txtNombreInspector;
    public javax.swing.JTextField txtNombreSoporteRapido;
    public javax.swing.JPasswordField txtNumeroEmpleado;
    public javax.swing.JTextField txtSTD;
    // End of variables declaration//GEN-END:variables
}
