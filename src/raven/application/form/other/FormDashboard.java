package raven.application.form.other;


import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Image;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import raven.application.Application;
import raven.controllers.Json;
import javax.swing.table.DefaultTableModel;
import org.json.JSONException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import org.json.JSONArray;
import org.json.JSONObject;
import raven.application.Application;
import raven.controllers.Dicio;
import raven.controllers.Local;

/**
 *
 * @author Raven
 */
public class FormDashboard extends javax.swing.JPanel {
    private JPanel panelBody;
    public FormDashboard() throws IOException, URISyntaxException, ParseException {
        initComponents();

        int newWidth = 20;
        int newHeight = 20;
        ImageIcon terminal = new ImageIcon("src/raven/Interface/images/icons/images/cil-terminal.png");
        Image imgt = terminal.getImage();
        Image imgScalet = imgt.getScaledInstance(14, 14, Image.SCALE_SMOOTH);
        ImageIcon scaledIcont = new ImageIcon(imgScalet);
        terminalt.setIcon(scaledIcont);
        //Btn fechar minimizar e maximizar
        ImageIcon iconx = new ImageIcon("src/raven/Interface/images/icons/images/x.png");
        Image imgx = iconx.getImage();
        Image imgScalex = imgx.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIconx = new ImageIcon(imgScalex);
        btnX.setIcon(scaledIconx);
        btnX.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Application.close();
            }
        });
        ImageIcon iconMin = new ImageIcon("src/raven/Interface/images/icons/images/ma.png");
        Image imgMin = iconMin.getImage();
        Image imgScaleMin = imgMin.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIconMin = new ImageIcon(imgScaleMin);
        btnMin.setIcon(scaledIconMin);
        btnMin.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Application.minimize();
            }
        });
        ImageIcon iconMax = new ImageIcon("src/raven/Interface/images/icons/images/mx.png");
        Image imgMax = iconMax.getImage();
        Image imgScaleMax = imgMax.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIconMax = new ImageIcon(imgScaleMax);
        btnMax.setIcon(scaledIconMax);
        btnMax.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Application.maximize();
            }
        });
        hostBar.putClientProperty(FlatClientProperties.STYLE,"background: $Panel.left.background;");
        bottomBar.putClientProperty(FlatClientProperties.STYLE,"background: $Panel.left.background;");

        hostname.setText(Local.getMachineName());
String apiResponsex = Json.makeGetRequestPanel(Json.getUrl() + "search/Ticket/", Json.getS(), Json.getA(), Json.getU(), "1");
System.out.println("aqui " + apiResponsex);
try {
    JSONObject jsonResponse = new JSONObject(apiResponsex);
    JSONArray jsonArray = jsonResponse.getJSONArray("data");
    SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    
    for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonData = jsonArray.getJSONObject(i);
        DefaultTableModel model = (DefaultTableModel) chamados.getModel();

        
        // Parse and format the data
        String id = jsonData.optString("2", "");
        String titulo = jsonData.optString("1", "");
        String[] partes = titulo.split("/");
        titulo = partes[0];
        String status = jsonData.optString("12", "");
        String tempoAtendimento = jsonData.optString("5", "");
        String tempoSolucaoStr = jsonData.optString("18", "");
        String categoria = jsonData.optString("7", "");

        String dataCriacaoStr = jsonData.optString("15", "");
        String ultimaAtualizacaoStr = jsonData.optString("19", "");
        
        Date dataCriacao = null;
        Date ultimaAtualizacao = null;
       
        
        try {
            dataCriacao = inputDateFormat.parse(dataCriacaoStr);
            ultimaAtualizacao = inputDateFormat.parse(ultimaAtualizacaoStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedTempoSolucao;
        if(!tempoSolucaoStr.isEmpty()){
          Date tempoSolucao = null;
          tempoSolucao = inputDateFormat.parse(tempoSolucaoStr); 
          formattedTempoSolucao = (tempoSolucao != null) ? outputDateFormat.format(tempoSolucao) : ""; 
        }else{formattedTempoSolucao = "";}

        String formattedDataCriacao = (dataCriacao != null) ? outputDateFormat.format(dataCriacao) : "";
        String formattedUltimaAtualizacao = (ultimaAtualizacao != null) ? outputDateFormat.format(ultimaAtualizacao) : "";

        // Add the data to the table model
        model.addRow(new Object[]{id, titulo, status, tempoAtendimento, formattedTempoSolucao, categoria, formattedDataCriacao, formattedUltimaAtualizacao});
      }
} catch (JSONException e) {
    e.printStackTrace();
}
 // Listener to handle row selection events
chamados.addMouseListener(new MouseAdapter() {
    @Override
    public void mouseClicked(MouseEvent e) {
        int row = chamados.rowAtPoint(e.getPoint());
        int col = chamados.columnAtPoint(e.getPoint());

        if (row >= 0 && col >= 0) {
Object id = chamados.getValueAt(row, 0);

            try {
                Application.showForm(new Detail((String) id));
            } catch (IOException ex) {
                Logger.getLogger(FormDashboard.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(FormDashboard.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(FormDashboard.class.getName()).log(Level.SEVERE, null, ex);
            }


        }
    }
});


Bar.putClientProperty(FlatClientProperties.STYLE,"background: $Bar.background;");
jButton2.addActionListener(new java.awt.event.ActionListener() {
    public void actionPerformed(java.awt.event.ActionEvent evt) {
        try {
            updateTableData("1");
        } catch (IOException ex) {
            Logger.getLogger(FormDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(FormDashboard.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(FormDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
});

// Inside the initComponents() method, after initializing filtroPanel and jButton2
filtroPanel.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String selectedFilter = (String) filtroPanel.getSelectedItem();
            System.out.println(selectedFilter);
            updateTableData(selectedFilter); // Call the updateTableData method with selected filter
        } catch (IOException | URISyntaxException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            Logger.getLogger(FormDashboard.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
});

    }
    



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Bar = new javax.swing.JPanel();
        btnMin = new javax.swing.JLabel();
        btnMax = new javax.swing.JLabel();
        btnX = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        terminalt = new javax.swing.JLabel();
        filtroBar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        filtroPanel = new javax.swing.JComboBox<>();
        hostBar = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        hostname = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        chamados = new javax.swing.JTable();
        bottomBar = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(818, 486));

        btnMin.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N

        btnMax.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N

        btnX.setFont(new java.awt.Font("Roboto Black", 1, 24));
        btnX.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnXMouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setText("Cliente Help Desk");

        javax.swing.GroupLayout BarLayout = new javax.swing.GroupLayout(Bar);
        Bar.setLayout(BarLayout);
        BarLayout.setHorizontalGroup(
            BarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BarLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(terminalt, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnMin, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnMax, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnX, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        BarLayout.setVerticalGroup(
            BarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(BarLayout.createSequentialGroup()
                .addGroup(BarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(terminalt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(BarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMin, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                        .addComponent(btnMax, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel1.setText("Filtros:");

        jButton2.setText("Atualizar");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        filtroPanel.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Novo", "Em Andamento (atribuído)", "Em Andamento (planejado)", "Pendente", "Solucionado", "Fechado" }));
        filtroPanel.setSelectedItem("Novo");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(9, 193, 255));
        jLabel5.setText("Hostname:");

        jLabel6.setText("TechSize Client");

        hostname.setText("host");

        javax.swing.GroupLayout hostBarLayout = new javax.swing.GroupLayout(hostBar);
        hostBar.setLayout(hostBarLayout);
        hostBarLayout.setHorizontalGroup(
            hostBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hostBarLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(hostname)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(19, 19, 19))
        );
        hostBarLayout.setVerticalGroup(
            hostBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hostBarLayout.createSequentialGroup()
                .addGroup(hostBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(hostname))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout filtroBarLayout = new javax.swing.GroupLayout(filtroBar);
        filtroBar.setLayout(filtroBarLayout);
        filtroBarLayout.setHorizontalGroup(
            filtroBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filtroBarLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filtroPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 499, Short.MAX_VALUE)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(hostBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        filtroBarLayout.setVerticalGroup(
            filtroBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filtroBarLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(hostBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(filtroBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jButton2)
                    .addComponent(filtroPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        Color corFundox = new Color(39, 44, 54); // Vermelho
        hostBar.setBackground(corFundox);

        chamados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
            },
            new String [] {
                "Id", "Titulo", "Status", "Tempo atendimento", "Tempo solução", "Categoria", "Data criação", "Ultima atualização"
            }
        ));
        chamados.setToolTipText("");
        jScrollPane1.setViewportView(chamados);
        // Create a custom cell renderer for the "Status" column
        chamados.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(SwingConstants.CENTER);
            }

            @Override
            public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Check if the row has content
                boolean hasContent = false;
                for (int col = 0; col < table.getColumnCount(); col++) {
                    if (table.getValueAt(row, col) != null) {
                        hasContent = true;
                        break;
                    }
                }

                // Set the background color based on whether the row has content
                if (hasContent) {
                    component.setBackground(new Color(0xC3D7EF));
                    // Set the left border for the first column
                    if (column == 0 && component instanceof JComponent) {
                        Border border = BorderFactory.createMatteBorder(0, 3, 0, 0, new Color(0x00E300));
                        ((JComponent) component).setBorder(border);
                    }
                } else {
                    component.setBackground(table.getBackground());
                    if (component instanceof JComponent) {
                        ((JComponent) component).setBorder(null); // Remove border for cells without content
                    }
                }

                // Set the text color to black
                component.setForeground(Color.BLACK);

                // Replace the status value with the corresponding string from the map
                if (column == 2 && value != null) { // Assuming "Status" column is at index 2
                    int statusValue = Integer.parseInt(value.toString());
                    String statusString = Dicio.getStatus(statusValue);
                    if (statusString != null) {
                        setText(statusString);
                    }
                }

                return component;
            }
        });
        chamados.setRowHeight(40);
        jScrollPane1.setViewportView(chamados);
        chamados.setEnabled(false);
        chamados.setBorder(null);

        jLabel3.setText("Chamados");

        jLabel4.setText("v1.0.1");

        javax.swing.GroupLayout bottomBarLayout = new javax.swing.GroupLayout(bottomBar);
        bottomBar.setLayout(bottomBarLayout);
        bottomBarLayout.setHorizontalGroup(
            bottomBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bottomBarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addContainerGap())
        );
        bottomBarLayout.setVerticalGroup(
            bottomBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bottomBarLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(bottomBarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 818, Short.MAX_VALUE)
                    .addComponent(bottomBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                .addGap(1, 1, 1)
                .addComponent(bottomBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        bottomBar.setBackground(corFundox);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Bar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(filtroBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Bar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(filtroBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        Color corFundo = new Color(66, 71, 76); // Vermelho
        Bar.setBackground(corFundo);
    }// </editor-fold>//GEN-END:initComponents

    private void btnXMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnXMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnXMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Bar;
    private javax.swing.JPanel bottomBar;
    private javax.swing.JLabel btnMax;
    private javax.swing.JLabel btnMin;
    private javax.swing.JLabel btnX;
    private javax.swing.JTable chamados;
    private javax.swing.JPanel filtroBar;
    private javax.swing.JComboBox<String> filtroPanel;
    private javax.swing.JPanel hostBar;
    private javax.swing.JLabel hostname;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel terminalt;
    // End of variables declaration//GEN-END:variables
    private void updateTableData(String filtro) throws IOException, URISyntaxException, ParseException {
    // Get the selected filter option
    String selectedFilter = (String) filtroPanel.getSelectedItem();
    
    // Clear the current table data
    DefaultTableModel model = (DefaultTableModel) chamados.getModel();
    model.setRowCount(0);
    filtro = Dicio.getStatusReverse(filtro);

String apiResponsex = Json.makeGetRequestPanel(Json.getUrl() + "search/Ticket/", Json.getS(), Json.getA(), Json.getU(), filtro);
System.out.println("aqui 2" + apiResponsex);
try {
    JSONObject jsonResponse = new JSONObject(apiResponsex);
    JSONArray jsonArray = jsonResponse.getJSONArray("data");
    SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonData = jsonArray.getJSONObject(i);


        
        // Parse and format the data
        String id = jsonData.optString("2", "");
        String titulo = jsonData.optString("1", "");
        String[] partes = titulo.split("/");
        titulo = partes[0];
        String status = jsonData.optString("12", "");
        String tempoAtendimento = jsonData.optString("5", "");
        String tempoSolucaoStr = jsonData.optString("18", "");
        String categoria = jsonData.optString("7", "");

        String dataCriacaoStr = jsonData.optString("15", "");
        String ultimaAtualizacaoStr = jsonData.optString("19", "");
        
        Date dataCriacao = null;
        Date ultimaAtualizacao = null;
       
        
        try {
            dataCriacao = inputDateFormat.parse(dataCriacaoStr);
            ultimaAtualizacao = inputDateFormat.parse(ultimaAtualizacaoStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String formattedTempoSolucao;
        if(!tempoSolucaoStr.isEmpty()){
          Date tempoSolucao = null;
          tempoSolucao = inputDateFormat.parse(tempoSolucaoStr); 
          formattedTempoSolucao = (tempoSolucao != null) ? outputDateFormat.format(tempoSolucao) : ""; 
        }else{formattedTempoSolucao = "";}

        String formattedDataCriacao = (dataCriacao != null) ? outputDateFormat.format(dataCriacao) : "";
        String formattedUltimaAtualizacao = (ultimaAtualizacao != null) ? outputDateFormat.format(ultimaAtualizacao) : "";

        
        model.addRow(new Object[]{id, titulo, status, tempoAtendimento, formattedTempoSolucao, categoria, formattedDataCriacao, formattedUltimaAtualizacao});
      }
} catch (JSONException e) {
    e.printStackTrace();
}

}
}

