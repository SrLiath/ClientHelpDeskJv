package raven.application.form.other;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import raven.application.Application;
import raven.controllers.Dicio;
import raven.controllers.Json;
import raven.controllers.Local;

import org.json.JSONObject;


public class FormInbox extends javax.swing.JPanel {
    private List<File> selectedFiles;
    private JPanel fileListPanel;

    public FormInbox() throws IOException, URISyntaxException {
        initComponents();
        Json.generateConfig(Json.getUrl() + "ITILCategory", Json.getS(), Json.getA(), Json.getU());
        int newWidth = 20;
        int newHeight = 20;

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
    
    leftPanel.putClientProperty(FlatClientProperties.STYLE,"background: $Menu.hbbar.left;");
    rightPanel.putClientProperty(FlatClientProperties.STYLE,"background: $Menu.hbbar.right;");
    Bar.putClientProperty(FlatClientProperties.STYLE,"background: $Bar.background;");
            try {
    JSONTokener tokener = new JSONTokener(new InputStreamReader(new FileInputStream("config.json"), "UTF-8"));
    JSONArray jsonArray = new JSONArray(tokener);

    Set<String> categoriaSet = new HashSet<>();
    Map<String, Set<String>> subCategoriaMap = new HashMap<>();
    Map<String, Set<String>> acaoMap = new HashMap<>();

    for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        String completename = jsonObject.getString("completename");

        String[] parts = completename.split(" > ");
        if (parts.length >= 1) {
            categoriaSet.add(parts[0]);
        }
        if (parts.length >= 2) {
            subCategoriaMap.computeIfAbsent(parts[0], key -> new HashSet<>()).add(parts[1]);
        }
        if (parts.length >= 3) {
            acaoMap.computeIfAbsent(parts[0] + " > " + parts[1], key -> new HashSet<>()).add(parts[2]);
        }
    }

    if (!categoriaSet.isEmpty()) {
        categoriaChamado.addItem("---");
        categoriaSet.forEach(categoriaChamado::addItem);
    }

categoriaChamado.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedCategoria = (String) categoriaChamado.getSelectedItem();

        subCategoria.removeAllItems();
        subCategoria.addItem("---");

        if (selectedCategoria != null && !selectedCategoria.equals("---")) {
            Set<String> subCategoriaItems = subCategoriaMap.getOrDefault(selectedCategoria, Collections.emptySet());
            subCategoriaItems.forEach(subCategoria::addItem);
        }
    }
});

subCategoria.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        String selectedSubCategoria = (String) subCategoria.getSelectedItem();

        acaoChamado.removeAllItems();
        acaoChamado.addItem("---");

        String selectedCategoria = (String) categoriaChamado.getSelectedItem();
        String categoryAndSubCategory = selectedCategoria + " > " + selectedSubCategoria;

        if (selectedSubCategoria != null && !selectedSubCategoria.equals("---")) {
            Set<String> acaoItems = acaoMap.getOrDefault(categoryAndSubCategory, Collections.emptySet());
            acaoItems.forEach(acaoChamado::addItem);
        }
    }
});

} catch (Exception e) {
    e.printStackTrace();
}
        selectedFiles = new ArrayList<>();
        fileListPanel = new JPanel();
        fileListPanel.setLayout(new BoxLayout(fileListPanel, BoxLayout.Y_AXIS));

        filesSelected.setViewportView(fileListPanel);            
    adicionarAnexo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(FormInbox.this);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedAttachment = fileChooser.getSelectedFile();
                    selectedFiles.add(selectedAttachment);
                    addFileToPanel(selectedAttachment);
                }
            }
        });
    }

    
        private void addFileToPanel(File file) {
        JPanel filePanel = new JPanel();
        filePanel.setLayout(new BorderLayout());

        JLabel fileNameLabel = new JLabel(file.getName());

        // Set a fixed height for the remove button
        int buttonHeight = fileNameLabel.getPreferredSize().height;
        JButton removeButton = new JButton("x");
        removeButton.setPreferredSize(new Dimension(buttonHeight, buttonHeight));

        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedFiles.remove(file);
                fileListPanel.remove(filePanel);
                fileListPanel.revalidate();
                fileListPanel.repaint();
            }
        });

        filePanel.add(fileNameLabel, BorderLayout.CENTER);
        filePanel.add(removeButton, BorderLayout.EAST);

        // Set background color for the file panel
        filePanel.setBackground(new Color(0, 175, 239));

        fileListPanel.add(filePanel);
        fileListPanel.revalidate();
        fileListPanel.repaint();
    }

private boolean isInputValid() {
    boolean isValid = true;
    
    if (tituloChamado.getText().trim().isEmpty()) {
        isValid = false;
        highlightField(tituloChamado);
    } else {
        removeHighlight(tituloChamado);
    }
    
    if (solicitanteChamado.getText().trim().isEmpty()) {
        isValid = false;
        highlightField(solicitanteChamado);
    } else {
        removeHighlight(solicitanteChamado);
    }
    
    if (categoriaChamado.getSelectedItem() == null || categoriaChamado.getSelectedItem().equals("---")) {
        isValid = false;
        highlightField(categoriaChamado);
    } else {
        removeHighlight(categoriaChamado);
    }
    
    if (descricaoChamado.getText().trim().isEmpty()) {
        isValid = false;
        highlightField(descricaoChamado);
    } else {
        removeHighlight(descricaoChamado);
    }
    
    return isValid;
}

private JSONObject createJsonObject() throws IOException, URISyntaxException {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("requester_full_name", tituloChamado.getText().trim());
    jsonObject.put("name", solicitanteChamado.getText().trim() + "/" + Local.getMachineName());
    System.out.println(categoriaChamado.getSelectedItem().toString() + subCategoria.getSelectedItem().toString() + acaoChamado.getSelectedItem().toString());
    jsonObject.put("itilcategories_id", Dicio.makeCategoryReverse(categoriaChamado.getSelectedItem().toString(), subCategoria.getSelectedItem().toString(), acaoChamado.getSelectedItem().toString()));          // Include acao
    jsonObject.put("urgency", Dicio.urgenciaReverse(urgencia.getSelectedItem().toString()));        // Include urgencia
    jsonObject.put("content", descricaoChamado.getText().trim());
    jsonObject.put("_users_id_requester", Json.getOwnId());
    jsonObject.put("priority", "3");
    jsonObject.put("impact", "3");
    jsonObject.put("status", "1");
    jsonObject.put("type", "1");
    JSONObject complete = new JSONObject();
    complete.put("input", jsonObject);
    return complete;
}

private void highlightField(javax.swing.JTextField field) {
    field.setBackground(Color.RED);
    field.setToolTipText("Este campo é obrigatório");
}

private void removeHighlight(javax.swing.JTextField field) {
    field.setBackground(UIManager.getColor("TextField.background"));
    field.setToolTipText(null);
}

private void highlightField(javax.swing.JComboBox<?> comboBox) {
    comboBox.setBackground(Color.RED);
    comboBox.setToolTipText("Este campo é obrigatório");
}

private void removeHighlight(javax.swing.JComboBox<?> comboBox) {
    comboBox.setBackground(UIManager.getColor("ComboBox.background"));
    comboBox.setToolTipText(null);
}

private void highlightField(javax.swing.JTextArea textArea) {
    textArea.setBackground(Color.RED);
    textArea.setToolTipText("Este campo é obrigatório");
}

private void removeHighlight(javax.swing.JTextArea textArea) {
    textArea.setBackground(UIManager.getColor("TextArea.background"));
    textArea.setToolTipText(null);
}
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Bar = new javax.swing.JPanel();
        btnMin = new javax.swing.JLabel();
        btnMax = new javax.swing.JLabel();
        btnX = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        leftPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tituloChamado = new javax.swing.JTextField();
        solicitanteChamado = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        descricaoChamado = new javax.swing.JTextArea();
        adicionarChamado = new javax.swing.JToggleButton();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        rightPanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        adicionarAnexo = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        categoriaChamado = new javax.swing.JComboBox<>();
        subCategoria = new javax.swing.JComboBox<>();
        acaoChamado = new javax.swing.JComboBox<>();
        urgencia = new javax.swing.JComboBox<>();
        filesSelected = new javax.swing.JScrollPane();

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
        jLabel2.setText("Suporte - TechSize");

        javax.swing.GroupLayout BarLayout = new javax.swing.GroupLayout(Bar);
        Bar.setLayout(BarLayout);
        BarLayout.setHorizontalGroup(
            BarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BarLayout.createSequentialGroup()
                .addContainerGap()
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
                .addGroup(BarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMin, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(btnMax, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Chamado");

        tituloChamado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tituloChamadoActionPerformed(evt);
            }
        });

        solicitanteChamado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                solicitanteChamadoActionPerformed(evt);
            }
        });

        descricaoChamado.setColumns(20);
        descricaoChamado.setRows(5);
        jScrollPane1.setViewportView(descricaoChamado);

        adicionarChamado.setText("Adicionar");
        adicionarChamado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adicionarChamadoActionPerformed(evt);
            }
        });

        jLabel8.setText("Título:");

        jLabel9.setText("Nome do solicitante:");

        jLabel10.setText("Descrição:");

        javax.swing.GroupLayout leftPanelLayout = new javax.swing.GroupLayout(leftPanel);
        leftPanel.setLayout(leftPanelLayout);
        leftPanelLayout.setHorizontalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addGroup(leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(solicitanteChamado, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                        .addComponent(tituloChamado, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap(60, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(adicionarChamado, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(165, 165, 165))
        );
        leftPanelLayout.setVerticalGroup(
            leftPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftPanelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGap(34, 34, 34)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tituloChamado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9)
                .addGap(4, 4, 4)
                .addComponent(solicitanteChamado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addGap(4, 4, 4)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(adicionarChamado)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setText("Categoria");

        jLabel4.setText("Subcategoria");

        jLabel5.setText("Ação");

        jLabel6.setText("Urgencia");

        adicionarAnexo.setText("Adicionar Anexo");
        adicionarAnexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adicionarAnexoActionPerformed(evt);
            }
        });

        jLabel7.setText("Arquivo(s) (20MB máx)");

        urgencia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Muito Alta", "Alta", "Média", "Baixa", "Muito Baixa" }));
        urgencia.setSelectedItem("Média");

        javax.swing.GroupLayout rightPanelLayout = new javax.swing.GroupLayout(rightPanel);
        rightPanel.setLayout(rightPanelLayout);
        rightPanelLayout.setHorizontalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(rightPanelLayout.createSequentialGroup()
                        .addComponent(filesSelected)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rightPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(adicionarAnexo)
                            .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(subCategoria, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(categoriaChamado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(acaoChamado, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(urgencia, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel7))
                        .addGap(26, 26, 26))))
        );
        rightPanelLayout.setVerticalGroup(
            rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(rightPanelLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(categoriaChamado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(subCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(acaoChamado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(rightPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(urgencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(adicionarAnexo, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filesSelected, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(76, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Bar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(rightPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Bar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rightPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(leftPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );

        Color corFundo = new Color(66, 71, 76); // Vermelho
        Bar.setBackground(corFundo);
        Color panelColorRight = new Color(44, 49, 60);
        leftPanel.setBackground(panelColorRight);
        Color panelColorLeft = new Color(57, 65, 80); // Vermelho
        rightPanel.setBackground(panelColorLeft);
    }// </editor-fold>//GEN-END:initComponents

    private void btnXMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnXMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnXMouseClicked

    private void adicionarChamadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adicionarChamadoActionPerformed
                if (isInputValid()) {
        JSONObject jsonObject = null;
                    try {
                        jsonObject = createJsonObject();
                        System.out.println(jsonObject);
                    } catch (IOException ex) {
                        Logger.getLogger(FormInbox.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (URISyntaxException ex) {
                        Logger.getLogger(FormInbox.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    String response = Json.sendTicket(Json.getUrl() + "Ticket/", jsonObject.toString());
             if (response != null) {
                 if (!selectedFiles.isEmpty()){
                JSONObject tecnicoDetalhesJSON = new JSONObject(response);
                String id = tecnicoDetalhesJSON.optString("id", "");
                for (File path : selectedFiles) {
    try {
        String filePath = path.getPath();
        String name = path.getName();
         String fileNameWithoutExtension = name.replaceFirst("[.][^.]+$", ""); 
        String uploadManifest = "{\"input\": {\"name\": \"Documento: Chamado - "+ solicitanteChamado.getText().trim() + "/" + Local.getMachineName() +"\",\"items_id\": \""+ id +"\", \"itemtype\":\"Ticket\", \"_filename\": [\""+ name +"\"]}}";

        Json.uploadFile(Json.getS(), Json.getA(), Json.getUrl(), uploadManifest, filePath);
        System.out.println(response);
        

    } catch (Exception e) {
        e.printStackTrace();
    }
}


                 }
                
                // Chamado aberto com sucesso
                JOptionPane.showMessageDialog(this, "Chamado aberto com sucesso", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                        try {
                            Application.showForm(new FormDashboard());
                        } catch (IOException ex) {
                            Logger.getLogger(FormInbox.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (URISyntaxException ex) {
                            Logger.getLogger(FormInbox.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ParseException ex) {
                Logger.getLogger(FormInbox.class.getName()).log(Level.SEVERE, null, ex);
            }
            } else {
                // Erro interno, contate um administrador
                JOptionPane.showMessageDialog(this, "Tente novamente mais tarde ou contate um administrador, erro interno", "Erro", JOptionPane.ERROR_MESSAGE);
            }

   
        System.out.println(jsonObject.toString());
    }
    }//GEN-LAST:event_adicionarChamadoActionPerformed

    private void tituloChamadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tituloChamadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tituloChamadoActionPerformed

    private void solicitanteChamadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_solicitanteChamadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_solicitanteChamadoActionPerformed

    private void adicionarAnexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adicionarAnexoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_adicionarAnexoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Bar;
    private javax.swing.JComboBox<String> acaoChamado;
    private javax.swing.JButton adicionarAnexo;
    private javax.swing.JToggleButton adicionarChamado;
    private javax.swing.JLabel btnMax;
    private javax.swing.JLabel btnMin;
    private javax.swing.JLabel btnX;
    private javax.swing.JComboBox<String> categoriaChamado;
    private javax.swing.JTextArea descricaoChamado;
    private javax.swing.JScrollPane filesSelected;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel leftPanel;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JTextField solicitanteChamado;
    private javax.swing.JComboBox<String> subCategoria;
    private javax.swing.JTextField tituloChamado;
    private javax.swing.JComboBox<String> urgencia;
    // End of variables declaration//GEN-END:variables


}
