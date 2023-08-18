package raven.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;

import javax.swing.SwingUtilities;
import raven.application.form.MainForm;
import raven.controllers.Json;
import raven.toast.Notifications;
import raven.views.Login;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


public class Application extends javax.swing.JFrame {
private class ResizeMouseListener extends MouseAdapter {
    private int startX, startY;

    @Override
    public void mousePressed(MouseEvent e) {
        startX = e.getX();
        startY = e.getY();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int width = getWidth();
        int height = getHeight();

        int offsetX = e.getX() - startX;
        int offsetY = e.getY() - startY;

        setSize(width + offsetX, height + offsetY);

        startX = e.getX();
        startY = e.getY();
    }
}

    private static Application app;
    private MainForm mainForm;
    public Application() {
        if (!authenticate()){
        initComponents();
        setSize(new Dimension(1200, 500));
        setLocationRelativeTo(null);
        
        mainForm = new MainForm();
        FlatAnimatedLafChange.showSnapshot();
        setContentPane(mainForm);
        mainForm.applyComponentOrientation(getComponentOrientation());
        mainForm.hideMenu();
        SwingUtilities.updateComponentTreeUI(mainForm);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
        Notifications.getInstance().setJFrame(this);
        ResizeMouseListener resizeMouseListener = new ResizeMouseListener();
        addMouseListener(resizeMouseListener);
        addMouseMotionListener(resizeMouseListener);

        setResizable(true);
        }else{openLoginScreen();}
    }
  public boolean authenticate() {
    String jsonFilePath = "token.json";

    ObjectMapper objectMapper = new ObjectMapper();
    File jsonFile = new File(jsonFilePath);

    try {
        if (!jsonFile.exists()) {
            // Cria um novo arquivo JSON com os valores iniciais
            createInitialJson(jsonFile, objectMapper);
        }

        // O arquivo JSON existe, então verifica os campos user_Token e app_Token
        JsonNode jsonObject = objectMapper.readTree(jsonFile);

        String userToken = jsonObject.get("user_Token").asText();
        String appToken = jsonObject.get("app_Token").asText();

        if (userToken.isEmpty() || appToken.isEmpty() || !Json.hasSessionToken() || !Json.checkSession()) {
          
            return true;
        }
        
        return false;
        
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
}

private void createInitialJson(File jsonFile, ObjectMapper objectMapper) throws IOException {
    ObjectNode initialJson = objectMapper.createObjectNode();
    initialJson.put("user_Token", "");
    initialJson.put("app_Token", "");
    initialJson.put("url", "https://suporte.techsize.com.br/apirest.php/");
    initialJson.put("inventory_install", "");

    objectMapper.writeValue(jsonFile, initialJson);
    System.out.println("Arquivo JSON criado com valores iniciais.");
}

private void openLoginScreen() {
    Login login = new Login();
    login.setVisible(true);
}

    public static void showForm(Component component) {
        component.applyComponentOrientation(app.getComponentOrientation());
        app.mainForm.showForm(component);
    }


    public static void logout() {
        FlatAnimatedLafChange.showSnapshot();
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void setSelectedMenu(int index, int subIndex) {
        app.mainForm.setSelectedMenu(index, subIndex);
    }
    public static void maximize() {
    int currentState = app.getExtendedState();
    if ((currentState & JFrame.MAXIMIZED_BOTH) == 0) {
        app.setExtendedState(currentState | JFrame.MAXIMIZED_BOTH); // Maximize
    } else {
        app.setExtendedState(currentState & ~JFrame.MAXIMIZED_BOTH); // Restore
    }
}
        public static void minimize() {
        app.setExtendedState(JFrame.ICONIFIED);
    }

          public static void close() {
        System.exit(0);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 719, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 521, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

    public static void main(String args[]) {
               FlatLaf.registerCustomDefaultsSource("raven.theme");
        FlatDarculaLaf.setup();
        java.awt.EventQueue.invokeLater(() -> {
            app = new Application();
            if (!app.authenticate()){
                app.setVisible(true);
            }
            

        });

    }

    // Variables declaration - do not modify                     
    // End of variables declaration                   
}