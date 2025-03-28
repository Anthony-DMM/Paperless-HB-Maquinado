/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Config.VistaSingleton;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 *
 * @author ANTHONY-MARTINEZ
 */
public class PreviaDASView extends javax.swing.JFrame {

    /**
     * Creates new form PreviaDAS
     */
    public PreviaDASView() {
        initComponents();
        setLocationRelativeTo(null);
        jLabel18.setOpaque(true);
        btnRegresar.setBackground(Color.BLACK);
        btnSiguiente.setBackground(Color.BLACK);
        lblCantidadProcesadaNombre.setText("<html><center>Cantidad<br>Procesada</center></html>");
        lblPiezasMetaNombre.setText("<html><center>Cantidad de<br>Piezas (Meta)</center></html>");
        lblPiezasBuenasNombre.setText("<html><center>Cantidad<br>Piezas Buenas</center></html>");
        lblPiezasRechazadasNombre.setText("<html><center>Cantidad Piezas<br>Rechazadas</center></html>");
        lblDesempeñoNombre.setText("<html><center>% de<br>Desempeño</center></html>");
        
        JTableHeader header = tblRegistroProduccion1.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 16));
        header.setBackground(Color.WHITE);
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getWidth(), 44));
    
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) header.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);

        
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setFont(new Font("Arial", Font.PLAIN, 14));
        renderer.setHorizontalAlignment(JLabel.CENTER);

        
        for (int i = 0; i < tblRegistroProduccion1.getColumnCount(); i++) {
            tblRegistroProduccion1.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        tblRegistroProduccion1.setRowHeight(35);
        
        JTableHeader header2 = tblRegistroProduccion2.getTableHeader();
        header2.setFont(new Font("Arial", Font.BOLD, 16));
        header2.setBackground(Color.WHITE);
        header2.setForeground(Color.BLACK);

    
        DefaultTableCellRenderer headerRenderer2 = (DefaultTableCellRenderer) header2.getDefaultRenderer();
        headerRenderer2.setHorizontalAlignment(JLabel.CENTER);

        
        DefaultTableCellRenderer renderer2 = new DefaultTableCellRenderer();
        renderer2.setFont(new Font("Arial", Font.PLAIN, 14));
        renderer2.setHorizontalAlignment(JLabel.CENTER);

        
        for (int i = 0; i < tblRegistroProduccion2.getColumnCount(); i++) {
            tblRegistroProduccion2.getColumnModel().getColumn(i).setCellRenderer(renderer2);
        }

        tblRegistroProduccion2.setRowHeight(35);
        
        JTableHeader header3 = tblRegistroProduccion3.getTableHeader();
        header3.setFont(new Font("Arial", Font.BOLD, 16));
        header3.setBackground(Color.WHITE);
        header3.setForeground(Color.BLACK);
        header3.setPreferredSize(new Dimension(header.getWidth(), 40));

    
        DefaultTableCellRenderer headerRenderer3 = (DefaultTableCellRenderer) header3.getDefaultRenderer();
        headerRenderer3.setHorizontalAlignment(JLabel.CENTER);

        
        DefaultTableCellRenderer renderer3 = new DefaultTableCellRenderer();
        renderer3.setFont(new Font("Arial", Font.PLAIN, 14));
        renderer3.setHorizontalAlignment(JLabel.CENTER);

        
        for (int i = 0; i < tblRegistroProduccion3.getColumnCount(); i++) {
            tblRegistroProduccion3.getColumnModel().getColumn(i).setCellRenderer(renderer3);
        }

        tblRegistroProduccion3.setRowHeight(35);
        
        JTableHeader header4 = tblHoraxHora.getTableHeader();
        header4.setFont(new Font("Arial", Font.BOLD, 16));
        header4.setBackground(Color.WHITE);
        header4.setForeground(Color.BLACK);
        header4.setPreferredSize(new Dimension(header.getWidth(), 40));

    
        DefaultTableCellRenderer headerRenderer4 = (DefaultTableCellRenderer) header4.getDefaultRenderer();
        headerRenderer4.setHorizontalAlignment(JLabel.CENTER);

        
        DefaultTableCellRenderer renderer4 = new DefaultTableCellRenderer();
        renderer4.setFont(new Font("Arial", Font.PLAIN, 14));
        renderer4.setHorizontalAlignment(JLabel.CENTER);

        
        for (int i = 0; i < tblHoraxHora.getColumnCount(); i++) {
            tblHoraxHora.getColumnModel().getColumn(i).setCellRenderer(renderer4);
        }

        tblHoraxHora.setRowHeight(35);
        
        JTableHeader header5 = tblParos.getTableHeader();
        header5.setFont(new Font("Arial", Font.BOLD, 16));
        header5.setBackground(Color.WHITE);
        header5.setForeground(Color.BLACK);
        header5.setPreferredSize(new Dimension(header.getWidth(), 40));

    
        DefaultTableCellRenderer headerRenderer5 = (DefaultTableCellRenderer) header5.getDefaultRenderer();
        headerRenderer5.setHorizontalAlignment(JLabel.CENTER);

        
        DefaultTableCellRenderer renderer5 = new DefaultTableCellRenderer();
        renderer5.setFont(new Font("Arial", Font.PLAIN, 14));
        renderer5.setHorizontalAlignment(JLabel.CENTER);

        
        for (int i = 0; i < tblParos.getColumnCount(); i++) {
            tblParos.getColumnModel().getColumn(i).setCellRenderer(renderer5);
        }

        tblParos.setRowHeight(35);
    }
    
    public static PreviaDASView getInstance() {
        return VistaSingleton.getInstance(PreviaDASView.class);
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
        jLabel1 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblLinea = new javax.swing.JLabel();
        lblGrupo = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        lblDia = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblMes = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        lblAnio = new javax.swing.JLabel();
        lblTurno = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        lblNombreKeeper = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        lblNombreInspector = new javax.swing.JLabel();
        lblNombreOperador = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblPiezasMetaNombre = new javax.swing.JLabel();
        lblCantidadPiezasMeta = new javax.swing.JLabel();
        lblCantidadProcesada = new javax.swing.JLabel();
        lblCantidadProcesadaNombre = new javax.swing.JLabel();
        lblCantidadPzsBuenas = new javax.swing.JLabel();
        lblPiezasBuenasNombre = new javax.swing.JLabel();
        lblPiezasRechazadasNombre = new javax.swing.JLabel();
        lblCantidadPzsRechazadas = new javax.swing.JLabel();
        lblDesempeñoNombre = new javax.swing.JLabel();
        lblDesempeño = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblRegistroProduccion1 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblRegistroProduccion2 = new javax.swing.JTable();
        jScrollPane6 = new javax.swing.JScrollPane();
        tblRegistroProduccion3 = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoraxHora = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblParos = new javax.swing.JTable();
        btnRegresar = new javax.swing.JButton();
        btnSiguiente = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(800, 1500));

        jLabel1.setFont(new java.awt.Font("Arial", 1, 40)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DAS - ACTIVIDAD DIARIA \"MAQUINADO\"");
        jLabel1.setPreferredSize(new java.awt.Dimension(700, 14));

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Línea");
        jLabel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel7.setOpaque(true);

        lblLinea.setBackground(new java.awt.Color(255, 255, 255));
        lblLinea.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        lblLinea.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblLinea.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblGrupo.setBackground(new java.awt.Color(255, 255, 255));
        lblGrupo.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        lblGrupo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGrupo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("Grupo");
        jLabel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel8.setOpaque(true);

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Día");
        jLabel9.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel9.setOpaque(true);

        lblDia.setBackground(new java.awt.Color(255, 255, 255));
        lblDia.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        lblDia.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDia.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Mes");
        jLabel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel10.setOpaque(true);

        lblMes.setBackground(new java.awt.Color(255, 255, 255));
        lblMes.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        lblMes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblMes.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Año");
        jLabel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel11.setOpaque(true);

        lblAnio.setBackground(new java.awt.Color(255, 255, 255));
        lblAnio.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        lblAnio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAnio.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblTurno.setBackground(new java.awt.Color(255, 255, 255));
        lblTurno.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        lblTurno.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTurno.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Circular el Turno Trabajado");
        jLabel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel12.setOpaque(true);

        jLabel13.setBackground(new java.awt.Color(255, 255, 255));
        jLabel13.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Nombre Keeper");
        jLabel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel13.setOpaque(true);

        lblNombreKeeper.setBackground(new java.awt.Color(255, 255, 255));
        lblNombreKeeper.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        lblNombreKeeper.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNombreKeeper.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel14.setBackground(new java.awt.Color(255, 255, 255));
        jLabel14.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Nombre Inspector");
        jLabel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel14.setOpaque(true);

        lblNombreInspector.setBackground(new java.awt.Color(255, 255, 255));
        lblNombreInspector.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        lblNombreInspector.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNombreInspector.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblNombreOperador.setBackground(new java.awt.Color(255, 255, 255));
        lblNombreOperador.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        lblNombreOperador.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblNombreOperador.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel15.setBackground(new java.awt.Color(255, 255, 255));
        jLabel15.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Nombre Operador");
        jLabel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jLabel15.setOpaque(true);

        jLabel16.setBackground(new java.awt.Color(255, 255, 255));
        jLabel16.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        jLabel16.setText("Totales de Producción");

        lblPiezasMetaNombre.setBackground(new java.awt.Color(255, 255, 255));
        lblPiezasMetaNombre.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblPiezasMetaNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPiezasMetaNombre.setToolTipText("");
        lblPiezasMetaNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblPiezasMetaNombre.setOpaque(true);

        lblCantidadPiezasMeta.setBackground(new java.awt.Color(255, 255, 255));
        lblCantidadPiezasMeta.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        lblCantidadPiezasMeta.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCantidadPiezasMeta.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblCantidadProcesada.setBackground(new java.awt.Color(255, 255, 255));
        lblCantidadProcesada.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        lblCantidadProcesada.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCantidadProcesada.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblCantidadProcesadaNombre.setBackground(new java.awt.Color(255, 255, 255));
        lblCantidadProcesadaNombre.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblCantidadProcesadaNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCantidadProcesadaNombre.setToolTipText("");
        lblCantidadProcesadaNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblCantidadProcesadaNombre.setOpaque(true);

        lblCantidadPzsBuenas.setBackground(new java.awt.Color(255, 255, 255));
        lblCantidadPzsBuenas.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        lblCantidadPzsBuenas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCantidadPzsBuenas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblPiezasBuenasNombre.setBackground(new java.awt.Color(255, 255, 255));
        lblPiezasBuenasNombre.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblPiezasBuenasNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPiezasBuenasNombre.setToolTipText("");
        lblPiezasBuenasNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblPiezasBuenasNombre.setOpaque(true);

        lblPiezasRechazadasNombre.setBackground(new java.awt.Color(255, 255, 255));
        lblPiezasRechazadasNombre.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblPiezasRechazadasNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPiezasRechazadasNombre.setToolTipText("");
        lblPiezasRechazadasNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblPiezasRechazadasNombre.setOpaque(true);

        lblCantidadPzsRechazadas.setBackground(new java.awt.Color(255, 255, 255));
        lblCantidadPzsRechazadas.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        lblCantidadPzsRechazadas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCantidadPzsRechazadas.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblDesempeñoNombre.setBackground(new java.awt.Color(255, 255, 255));
        lblDesempeñoNombre.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        lblDesempeñoNombre.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDesempeñoNombre.setToolTipText("");
        lblDesempeñoNombre.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblDesempeñoNombre.setOpaque(true);

        lblDesempeño.setBackground(new java.awt.Color(255, 255, 255));
        lblDesempeño.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        lblDesempeño.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDesempeño.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel17.setBackground(new java.awt.Color(255, 255, 255));
        jLabel17.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        jLabel17.setText("Registro de Producción");

        tblRegistroProduccion1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "No. MOG", "Modelo", "STD", "Lote"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tblRegistroProduccion1);

        tblRegistroProduccion2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Hora Inicio", "Hora Fin"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tblRegistroProduccion2);

        tblRegistroProduccion3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Piezas Procesadas", "Piezas Buenas", "Piezas Rechazadas"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(tblRegistroProduccion3);

        jLabel18.setBackground(new java.awt.Color(255, 255, 255));
        jLabel18.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("Tiempo de Proceso");
        jLabel18.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(138, 162, 145)));

        jLabel20.setBackground(new java.awt.Color(255, 255, 255));
        jLabel20.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        jLabel20.setText("Registro de Piezas Hora x Hora");

        tblHoraxHora.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Hora", "Piezas x Hora", "Acumulado", "Ok / Ng", "Nombre"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblHoraxHora);

        jLabel21.setBackground(new java.awt.Color(255, 255, 255));
        jLabel21.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        jLabel21.setText("Registro de Paros de Línea");

        tblParos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Descripción", "Hora inicio", "Hora fin", "Tiempo", "Andón", "Nivel", "Detalle"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblParos);

        btnRegresar.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        btnRegresar.setForeground(new java.awt.Color(255, 255, 255));
        btnRegresar.setText("Regresar");
        btnRegresar.setMaximumSize(new java.awt.Dimension(120, 60));
        btnRegresar.setMinimumSize(new java.awt.Dimension(120, 60));
        btnRegresar.setPreferredSize(new java.awt.Dimension(350, 80));

        btnSiguiente.setFont(new java.awt.Font("Arial", 0, 36)); // NOI18N
        btnSiguiente.setForeground(new java.awt.Color(255, 255, 255));
        btnSiguiente.setText("Siguiente");
        btnSiguiente.setMaximumSize(new java.awt.Dimension(120, 60));
        btnSiguiente.setMinimumSize(new java.awt.Dimension(120, 60));
        btnSiguiente.setPreferredSize(new java.awt.Dimension(120, 80));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                            .addComponent(lblLinea, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                            .addComponent(lblGrupo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDia, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblMes, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                            .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblAnio, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                        .addGap(50, 50, 50)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblTurno, javax.swing.GroupLayout.PREFERRED_SIZE, 270, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblNombreKeeper, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                            .addComponent(lblNombreOperador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNombreInspector, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblCantidadPiezasMeta, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPiezasMetaNombre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblCantidadProcesada, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblCantidadProcesadaNombre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblCantidadPzsBuenas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPiezasBuenasNombre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblCantidadPzsRechazadas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPiezasRechazadasNombre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, 0)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(lblDesempeño, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDesempeñoNombre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jScrollPane6)
                    .addComponent(jLabel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(lblLinea, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(lblGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(lblDia, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(lblMes, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(lblAnio, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(lblTurno, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(lblNombreKeeper, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, 0)
                            .addComponent(lblNombreOperador, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(lblNombreInspector, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblPiezasMetaNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(lblCantidadPiezasMeta, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblCantidadProcesadaNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(lblCantidadProcesada, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblPiezasBuenasNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(lblCantidadPzsBuenas, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lblDesempeñoNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(lblDesempeño, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lblPiezasRechazadasNombre, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                        .addGap(0, 0, 0)
                        .addComponent(lblCantidadPzsRechazadas, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(40, 40, 40)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addGap(0, 0, 0)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(30, 30, 30)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSiguiente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRegresar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(PreviaDASView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PreviaDASView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PreviaDASView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PreviaDASView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PreviaDASView().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnRegresar;
    public javax.swing.JButton btnSiguiente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    public javax.swing.JScrollPane jScrollPane4;
    public javax.swing.JScrollPane jScrollPane5;
    public javax.swing.JScrollPane jScrollPane6;
    public javax.swing.JLabel lblAnio;
    public javax.swing.JLabel lblCantidadPiezasMeta;
    public javax.swing.JLabel lblCantidadProcesada;
    private javax.swing.JLabel lblCantidadProcesadaNombre;
    public javax.swing.JLabel lblCantidadPzsBuenas;
    public javax.swing.JLabel lblCantidadPzsRechazadas;
    public javax.swing.JLabel lblDesempeño;
    private javax.swing.JLabel lblDesempeñoNombre;
    public javax.swing.JLabel lblDia;
    public javax.swing.JLabel lblGrupo;
    public javax.swing.JLabel lblLinea;
    public javax.swing.JLabel lblMes;
    public javax.swing.JLabel lblNombreInspector;
    public javax.swing.JLabel lblNombreKeeper;
    public javax.swing.JLabel lblNombreOperador;
    private javax.swing.JLabel lblPiezasBuenasNombre;
    private javax.swing.JLabel lblPiezasMetaNombre;
    private javax.swing.JLabel lblPiezasRechazadasNombre;
    public javax.swing.JLabel lblTurno;
    public javax.swing.JTable tblHoraxHora;
    public javax.swing.JTable tblParos;
    public javax.swing.JTable tblRegistroProduccion1;
    public javax.swing.JTable tblRegistroProduccion2;
    public javax.swing.JTable tblRegistroProduccion3;
    // End of variables declaration//GEN-END:variables
}
