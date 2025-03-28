/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Config.VistaSingleton;
import Utils.FiltroCampos;
import java.awt.Color;
import javax.swing.text.AbstractDocument;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class RegistroRBPView extends javax.swing.JFrame {

    /**
     * Creates new form Registro_RBP
     */
    private RegistroRBPView() {
        initComponents();
        setLocationRelativeTo(null);
        lblTituloRBP.setText("<html><center>RBP - REGISTRO BÁSICO DE<br> PRODUCCIÓN ''HB MAQUINADO''</center></html>");
        lblNumeroEmpleado.setText("<html>Número<br>empleado:</html>");
        lblEmpleado.setText("<html>Nombre<br>empleado:</html>");
        btnDAS.setText("<html><center>Hora<br>x Hora</center></html>");
        btnDibujo.setText("<html><center>Ver<br>Dibujo</center></html>");
        lblPiezasxFila.setText("<html><center>Piezas<br>por fila</center></html>");
        lblFilasCompletas.setText("<html><center>Filas<br>completas</center></html>");
        lblNivelesCompletos.setText("<html><center>Niveles<br>completos</center></html>");
        lblAvisoTurno.setText("<html>Revise el turno que aparece en la pantalla. ¿Es el turno en el que está trabajando?<br>Si no lo es, seleccione el turno correcto.</html>");
        btnRegresar.setBackground(Color.BLACK);
        btnSiguiente.setBackground(Color.BLACK);
        btnDAS.setBackground(Color.BLACK);
        btnParoLinea.setBackground(Color.RED);
        btnCambioMOG.setBackground(Color.BLACK);
        cbxTurno.setBackground(Color.YELLOW);
        btnParoLinea.setText("<html><center>Paro<br>de Línea</center></html>");
        btnCambioMOG.setText("<html><center>Cambio<br>de MOG</center></html>");
        
        ((AbstractDocument) txtNumeroEmpleado.getDocument()).setDocumentFilter(new FiltroCampos.FiltroNumerosYSignoNumeral());
        ((AbstractDocument) txtPiezasxFila.getDocument()).setDocumentFilter(new FiltroCampos.FiltroSoloNumeros());
        ((AbstractDocument) txtFilas.getDocument()).setDocumentFilter(new FiltroCampos.FiltroSoloNumeros());
        ((AbstractDocument) txtNiveles.getDocument()).setDocumentFilter(new FiltroCampos.FiltroSoloNumeros());
        ((AbstractDocument) txtCanastas.getDocument()).setDocumentFilter(new FiltroCampos.FiltroSoloNumeros());
        ((AbstractDocument) txtFilasCompletas.getDocument()).setDocumentFilter(new FiltroCampos.FiltroSoloNumeros());
        ((AbstractDocument) txtNivelesCompletos.getDocument()).setDocumentFilter(new FiltroCampos.FiltroSoloNumeros());
        ((AbstractDocument) txtSobrante.getDocument()).setDocumentFilter(new FiltroCampos.FiltroSoloNumeros());
        
    }
    
    public static RegistroRBPView getInstance() {
        return VistaSingleton.getInstance(RegistroRBPView.class);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblNumeroEmpleado = new javax.swing.JLabel();
        lblEmpleado = new javax.swing.JLabel();
        txtNombreEmpleado = new javax.swing.JTextField();
        lblTituloRBP = new javax.swing.JLabel();
        txtFecha = new javax.swing.JTextField();
        btnDibujo = new javax.swing.JButton();
        jLabelCanastasCom = new javax.swing.JLabel();
        txtPiezasxFila = new javax.swing.JTextField();
        lblPiezasxFila = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtFilas = new javax.swing.JTextField();
        txtNiveles = new javax.swing.JTextField();
        jLabel212 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txtCanastas = new javax.swing.JTextField();
        jLabelCanastasCom1 = new javax.swing.JLabel();
        txtFilasCompletas = new javax.swing.JTextField();
        lblFilasCompletas = new javax.swing.JLabel();
        lblNivelesCompletos = new javax.swing.JLabel();
        txtNivelesCompletos = new javax.swing.JTextField();
        txtSobrante = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        btnRegresar = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();
        btnDAS = new javax.swing.JButton();
        btnParoLinea = new javax.swing.JButton();
        btnCambioMOG = new javax.swing.JButton();
        txtNumeroEmpleado = new javax.swing.JPasswordField();
        lblAvisoTurno = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtHoraInicio = new javax.swing.JTextField();
        cbxTurno = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtNombreEmpleado2 = new javax.swing.JTextField();
        txtNombreEmpleado3 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(900, 1500));

        jLabel3.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        jLabel3.setText("Fecha:");

        lblNumeroEmpleado.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N

        lblEmpleado.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N

        txtNombreEmpleado.setEditable(false);
        txtNombreEmpleado.setBackground(new java.awt.Color(204, 204, 204));
        txtNombreEmpleado.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        txtNombreEmpleado.setFocusable(false);

        lblTituloRBP.setFont(new java.awt.Font("Arial", 1, 44)); // NOI18N
        lblTituloRBP.setForeground(new java.awt.Color(0, 102, 0));
        lblTituloRBP.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        txtFecha.setEditable(false);
        txtFecha.setBackground(new java.awt.Color(204, 204, 204));
        txtFecha.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        txtFecha.setFocusable(false);

        btnDibujo.setBackground(new java.awt.Color(0, 0, 0));
        btnDibujo.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        btnDibujo.setForeground(new java.awt.Color(255, 255, 255));
        btnDibujo.setFocusable(false);

        jLabelCanastasCom.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        jLabelCanastasCom.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCanastasCom.setText("Canastas Completas");
        jLabelCanastasCom.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtPiezasxFila.setEditable(false);
        txtPiezasxFila.setBackground(new java.awt.Color(204, 204, 204));
        txtPiezasxFila.setFont(new java.awt.Font("Arial", 0, 40)); // NOI18N
        txtPiezasxFila.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPiezasxFila.setFocusable(false);
        txtPiezasxFila.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPiezasxFilaKeyTyped(evt);
            }
        });

        lblPiezasxFila.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        lblPiezasxFila.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel9.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Filas");
        jLabel9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        txtFilas.setEditable(false);
        txtFilas.setBackground(new java.awt.Color(204, 204, 204));
        txtFilas.setFont(new java.awt.Font("Arial", 0, 40)); // NOI18N
        txtFilas.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtFilas.setFocusable(false);
        txtFilas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFilasKeyTyped(evt);
            }
        });

        txtNiveles.setEditable(false);
        txtNiveles.setBackground(new java.awt.Color(204, 204, 204));
        txtNiveles.setFont(new java.awt.Font("Arial", 0, 40)); // NOI18N
        txtNiveles.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNiveles.setFocusable(false);
        txtNiveles.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNivelesKeyTyped(evt);
            }
        });

        jLabel212.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        jLabel212.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel212.setText("Niveles");

        jLabel11.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Canastas");

        txtCanastas.setEditable(false);
        txtCanastas.setBackground(new java.awt.Color(204, 204, 204));
        txtCanastas.setFont(new java.awt.Font("Arial", 0, 40)); // NOI18N
        txtCanastas.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtCanastas.setFocusable(false);
        txtCanastas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCanastasKeyTyped(evt);
            }
        });

        jLabelCanastasCom1.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        jLabelCanastasCom1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelCanastasCom1.setText("Canastas Incompletas");
        jLabelCanastasCom1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        txtFilasCompletas.setEditable(false);
        txtFilasCompletas.setBackground(new java.awt.Color(204, 204, 204));
        txtFilasCompletas.setFont(new java.awt.Font("Arial", 0, 40)); // NOI18N
        txtFilasCompletas.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtFilasCompletas.setFocusable(false);
        txtFilasCompletas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtFilasCompletasKeyTyped(evt);
            }
        });

        lblFilasCompletas.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        lblFilasCompletas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFilasCompletas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        lblNivelesCompletos.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        lblNivelesCompletos.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        txtNivelesCompletos.setEditable(false);
        txtNivelesCompletos.setBackground(new java.awt.Color(204, 204, 204));
        txtNivelesCompletos.setFont(new java.awt.Font("Arial", 0, 40)); // NOI18N
        txtNivelesCompletos.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtNivelesCompletos.setFocusable(false);
        txtNivelesCompletos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNivelesCompletosKeyTyped(evt);
            }
        });

        txtSobrante.setEditable(false);
        txtSobrante.setBackground(new java.awt.Color(204, 204, 204));
        txtSobrante.setFont(new java.awt.Font("Arial", 0, 40)); // NOI18N
        txtSobrante.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtSobrante.setFocusable(false);
        txtSobrante.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtSobranteKeyTyped(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Sobrante");

        btnRegresar.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        btnRegresar.setForeground(new java.awt.Color(255, 255, 255));
        btnRegresar.setText("Regresar");
        btnRegresar.setFocusable(false);
        btnRegresar.setMaximumSize(new java.awt.Dimension(120, 60));
        btnRegresar.setMinimumSize(new java.awt.Dimension(120, 60));
        btnRegresar.setPreferredSize(new java.awt.Dimension(350, 80));

        btnSiguiente.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        btnSiguiente.setForeground(new java.awt.Color(255, 255, 255));
        btnSiguiente.setText("Siguiente");
        btnSiguiente.setFocusable(false);
        btnSiguiente.setMaximumSize(new java.awt.Dimension(120, 60));
        btnSiguiente.setMinimumSize(new java.awt.Dimension(120, 60));
        btnSiguiente.setPreferredSize(new java.awt.Dimension(120, 80));

        btnDAS.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        btnDAS.setForeground(new java.awt.Color(255, 255, 255));
        btnDAS.setFocusable(false);
        btnDAS.setMaximumSize(new java.awt.Dimension(120, 60));
        btnDAS.setMinimumSize(new java.awt.Dimension(120, 60));
        btnDAS.setPreferredSize(new java.awt.Dimension(350, 80));
        btnDAS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDASActionPerformed(evt);
            }
        });

        btnParoLinea.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        btnParoLinea.setForeground(new java.awt.Color(255, 255, 255));
        btnParoLinea.setText("PARO DE LÍNEA");
        btnParoLinea.setFocusable(false);
        btnParoLinea.setMaximumSize(new java.awt.Dimension(120, 60));
        btnParoLinea.setMinimumSize(new java.awt.Dimension(120, 60));
        btnParoLinea.setPreferredSize(new java.awt.Dimension(350, 80));

        btnCambioMOG.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        btnCambioMOG.setForeground(new java.awt.Color(255, 255, 255));
        btnCambioMOG.setText("Cambio de MOG");
        btnCambioMOG.setFocusable(false);
        btnCambioMOG.setMaximumSize(new java.awt.Dimension(120, 60));
        btnCambioMOG.setMinimumSize(new java.awt.Dimension(120, 60));
        btnCambioMOG.setPreferredSize(new java.awt.Dimension(350, 80));
        btnCambioMOG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCambioMOGActionPerformed(evt);
            }
        });

        txtNumeroEmpleado.setBackground(new java.awt.Color(255, 255, 0));
        txtNumeroEmpleado.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        txtNumeroEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtNumeroEmpleadoKeyTyped(evt);
            }
        });

        lblAvisoTurno.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        lblAvisoTurno.setForeground(new java.awt.Color(255, 51, 0));

        jLabel4.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        jLabel4.setText("Hora inicio:");

        jLabel5.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        jLabel5.setText("Turno:");

        txtHoraInicio.setEditable(false);
        txtHoraInicio.setBackground(new java.awt.Color(204, 204, 204));
        txtHoraInicio.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        txtHoraInicio.setFocusable(false);

        cbxTurno.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        cbxTurno.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccionar turno", "1", "2" }));
        cbxTurno.setBorder(null);

        jLabel6.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        jLabel6.setText("Scrap total:");

        txtNombreEmpleado2.setEditable(false);
        txtNombreEmpleado2.setBackground(new java.awt.Color(204, 204, 204));
        txtNombreEmpleado2.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        txtNombreEmpleado2.setFocusable(false);

        txtNombreEmpleado3.setEditable(false);
        txtNombreEmpleado3.setBackground(new java.awt.Color(204, 204, 204));
        txtNombreEmpleado3.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        txtNombreEmpleado3.setFocusable(false);

        jLabel7.setFont(new java.awt.Font("Arial", 0, 30)); // NOI18N
        jLabel7.setText("Piezas recibidas:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblTituloRBP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtPiezasxFila, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(lblPiezasxFila, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtFilas, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNiveles)
                    .addComponent(jLabel212, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(txtCanastas, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)))
            .addComponent(jLabelCanastasCom, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabelCanastasCom1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtFilasCompletas, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(lblFilasCompletas, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblNivelesCompletos, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(txtNivelesCompletos, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE))
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtSobrante, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnCambioMOG, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnParoLinea, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(btnDAS, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtHoraInicio)
                    .addComponent(cbxTurno, 0, 261, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 62, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNombreEmpleado2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(txtNombreEmpleado3, javax.swing.GroupLayout.Alignment.TRAILING)))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnDibujo, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblNumeroEmpleado, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                            .addComponent(lblEmpleado, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtFecha)
                            .addComponent(txtNumeroEmpleado)
                            .addComponent(txtNombreEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 455, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(lblAvisoTurno, javax.swing.GroupLayout.PREFERRED_SIZE, 800, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(lblTituloRBP)
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtFecha, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblNumeroEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                            .addComponent(txtNumeroEmpleado))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombreEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, 59, Short.MAX_VALUE)
                            .addComponent(lblEmpleado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(btnDibujo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(50, 50, 50)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtHoraInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtNombreEmpleado2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtNombreEmpleado3, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cbxTurno)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addComponent(lblAvisoTurno)
                .addGap(50, 50, 50)
                .addComponent(jLabelCanastasCom)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel212, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPiezasxFila, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(11, 11, 11)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPiezasxFila, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFilas, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNiveles, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCanastas, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addComponent(jLabelCanastasCom1)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblNivelesCompletos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel12, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(lblFilasCompletas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txtNivelesCompletos, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                            .addComponent(txtSobrante)
                            .addComponent(txtFilasCompletas)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnDAS, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15)
                        .addComponent(btnParoLinea, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15)
                .addComponent(btnCambioMOG, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 270, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDASActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDASActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDASActionPerformed

    private void btnCambioMOGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCambioMOGActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCambioMOGActionPerformed

    private void txtNumeroEmpleadoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroEmpleadoKeyTyped
        if(txtNumeroEmpleado.getText().length() >= 12)
        {
            evt.consume();
        }
    }//GEN-LAST:event_txtNumeroEmpleadoKeyTyped

    private void txtPiezasxFilaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPiezasxFilaKeyTyped
        if(txtPiezasxFila.getText().length() >= 3)
        {
            evt.consume();
        }
    }//GEN-LAST:event_txtPiezasxFilaKeyTyped

    private void txtFilasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFilasKeyTyped
        if(txtFilas.getText().length() >= 2)
        {
            evt.consume();
        }
    }//GEN-LAST:event_txtFilasKeyTyped

    private void txtNivelesKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNivelesKeyTyped
        if(txtNiveles.getText().length() >= 2)
        {
            evt.consume();
        }
    }//GEN-LAST:event_txtNivelesKeyTyped

    private void txtCanastasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCanastasKeyTyped
        if(txtCanastas.getText().length() >= 3)
        {
            evt.consume();
        }
    }//GEN-LAST:event_txtCanastasKeyTyped

    private void txtFilasCompletasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFilasCompletasKeyTyped
        if(txtFilasCompletas.getText().length() >= 2)
        {
            evt.consume();
        }
    }//GEN-LAST:event_txtFilasCompletasKeyTyped

    private void txtNivelesCompletosKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNivelesCompletosKeyTyped
        if(txtNivelesCompletos.getText().length() >= 2)
        {
            evt.consume();
        }
    }//GEN-LAST:event_txtNivelesCompletosKeyTyped

    private void txtSobranteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSobranteKeyTyped
        if(txtSobrante.getText().length() >= 3)
        {
            evt.consume();
        }
    }//GEN-LAST:event_txtSobranteKeyTyped

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
            java.util.logging.Logger.getLogger(RegistroRBPView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(RegistroRBPView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(RegistroRBPView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(RegistroRBPView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new RegistroRBPView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCambioMOG;
    public javax.swing.JButton btnDAS;
    public javax.swing.JButton btnDibujo;
    public javax.swing.JButton btnParoLinea;
    public javax.swing.JButton btnRegresar;
    public javax.swing.JButton btnSiguiente;
    public javax.swing.JComboBox<String> cbxTurno;
    public javax.swing.JLabel jLabel11;
    public javax.swing.JLabel jLabel12;
    public javax.swing.JLabel jLabel212;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    public javax.swing.JLabel jLabel9;
    public javax.swing.JLabel jLabelCanastasCom;
    public javax.swing.JLabel jLabelCanastasCom1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblAvisoTurno;
    private javax.swing.JLabel lblEmpleado;
    public javax.swing.JLabel lblFilasCompletas;
    public javax.swing.JLabel lblNivelesCompletos;
    private javax.swing.JLabel lblNumeroEmpleado;
    public javax.swing.JLabel lblPiezasxFila;
    public javax.swing.JLabel lblTituloRBP;
    public javax.swing.JTextField txtCanastas;
    public javax.swing.JTextField txtFecha;
    public javax.swing.JTextField txtFilas;
    public javax.swing.JTextField txtFilasCompletas;
    public javax.swing.JTextField txtHoraInicio;
    public javax.swing.JTextField txtNiveles;
    public javax.swing.JTextField txtNivelesCompletos;
    public javax.swing.JTextField txtNombreEmpleado;
    public javax.swing.JTextField txtNombreEmpleado2;
    public javax.swing.JTextField txtNombreEmpleado3;
    public javax.swing.JPasswordField txtNumeroEmpleado;
    public javax.swing.JTextField txtPiezasxFila;
    public javax.swing.JTextField txtSobrante;
    // End of variables declaration//GEN-END:variables
}
