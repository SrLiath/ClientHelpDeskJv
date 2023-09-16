/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package raven.application.form.other;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import org.json.JSONObject;
import raven.controllers.Json;

/**
 *
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class DetailChatFileSent extends javax.swing.JPanel {

    /**
     * Creates new form DetailChat
     */
    public DetailChatFileSent(String nome, String nomeFile, JSONObject json) {
        initComponents();
        nomeChat.setText(nomeFile);
        ImageIcon iconMax = new ImageIcon("src/raven/Interface/images/icons/images/cil-file.png");
        Image imgMax = iconMax.getImage();
        Image imgScaleMax = imgMax.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ImageIcon scaledIconMax = new ImageIcon(imgScaleMax);
        File.setIcon(scaledIconMax);
        File.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Json.downloadFile(json);
                }
            });
        nomeChat.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Json.downloadFile(json);

                }
            });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nomeChat = new javax.swing.JLabel();
        File = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(400, 112));

        nomeChat.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        nomeChat.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        nomeChat.setText("Tecnico Responsavel");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(230, Short.MAX_VALUE)
                .addComponent(nomeChat, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(File, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(File, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(nomeChat, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(6, 6, 6))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel File;
    private javax.swing.JLabel nomeChat;
    // End of variables declaration//GEN-END:variables
}
