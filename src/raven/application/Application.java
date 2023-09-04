package raven.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import java.awt.AWTException;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import javax.swing.SwingUtilities;
import raven.application.form.MainForm;
import raven.application.form.other.FormDashboard;
import raven.controllers.Json;
import raven.toast.Notifications;
import raven.views.Login;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


public class Application extends javax.swing.JFrame {
    private int mouseX, mouseY;
    private static Application app;
    private MainForm mainForm;
    private static Dimension originalSize = new Dimension(1000, 520);
     
    public Application() throws InterruptedException {
       ImageIcon icon = new ImageIcon("Interface/images/icone_sistema.png"); // Atualize o caminho conforme o local do ícone
        setIconImage(icon.getImage());
        if (!authenticate()){
        startServerThread();
        initComponents();
        setSize(new Dimension(1000, 520));
        setLocationRelativeTo(null);
        setTitle("Client Help Desk");
        mainForm = new MainForm();
        FlatAnimatedLafChange.showSnapshot();
        setContentPane(mainForm);
        
        mainForm.applyComponentOrientation(getComponentOrientation());
        mainForm.hideMenu();
        SwingUtilities.updateComponentTreeUI(mainForm);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
        Notifications.getInstance().setJFrame(this);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int newX = getLocation().x + e.getX() - mouseX;
                int newY = getLocation().y + e.getY() - mouseY;
                setLocation(newX, newY);
            }
        });
        tray();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                close();
            }
        });
        setResizable(true);
        }else{openLoginScreen();}

    }
    public static void startServerThread() {
        Thread serverThread = new Thread(() -> {
            try {
                ServerSocket serverSocket = new ServerSocket(25786);
                System.out.println("Servidor aberto na porta 34786");
                
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Recebida uma solicitação de cliente");
                    
                    // Enviar a resposta "aberto" para o cliente
                    OutputStream outputStream = clientSocket.getOutputStream();
                    PrintWriter writer = new PrintWriter(outputStream, true);
                    writer.println("aberto");
                    
                    // Executar a função teste()
                    app.setState(JFrame.NORMAL);
                    app.setVisible(true);
                    
                    // Fechar conexão com o cliente
                    writer.close();
                    clientSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        serverThread.start();
    }
 public void tray() {
        if (SystemTray.isSupported()) {
            try {
                SystemTray tray = SystemTray.getSystemTray();
                String iconPath = "/raven/Interface/images/icon.png"; // Atualize o caminho da imagem conforme necessário
                InputStream iconStream = getClass().getResourceAsStream(iconPath);
                BufferedImage originalImage = ImageIO.read(iconStream);

                // Definir as dimensões desejadas para o ícone do tray
                int trayIconWidth = 16;
                int trayIconHeight = 16;

                // Redimensionar a imagem para o tamanho do ícone do tray
                Image resizedIcon = originalImage.getScaledInstance(trayIconWidth, trayIconHeight, Image.SCALE_SMOOTH);


                    PopupMenu popup = new PopupMenu();
                    MenuItem openItem = new MenuItem("Abrir");
                    openItem.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            setState(JFrame.NORMAL);
                            setVisible(true);
                        }
                    });
                    popup.add(openItem);

                    MenuItem exitItem = new MenuItem("Sair");
                    exitItem.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            System.exit(0);
                        }
                    });
                    popup.add(exitItem);
                    TrayIcon trayIcon = new TrayIcon(resizedIcon, "Client Help Desk", popup);
                    trayIcon.setImageAutoSize(true);
                    trayIcon.addMouseListener(new MouseAdapter() {
            private long lastClickTime = 0;

            @Override
            public void mouseClicked(MouseEvent e) {
                long currentTime = System.currentTimeMillis();

                if (currentTime - lastClickTime <= 500) { // Detecta um duplo clique dentro de 500ms
                    if (e.getButton() == MouseEvent.BUTTON1) { // Botão esquerdo do mouse
                        // Lidar com o duplo clique aqui
                            setState(JFrame.NORMAL);
                            setVisible(true);
                            setState(JFrame.NORMAL);
                            setVisible(true);
                    }
                }

                lastClickTime = currentTime;
            }
        });

                    try {
                        tray.add(trayIcon);
                        System.out.println("Ícone adicionado à bandeja do sistema.");
                    } catch (AWTException e) {
                        System.err.println("Erro ao adicionar ícone na bandeja do sistema: " + e.getMessage());
                        e.printStackTrace();
                    }
                }catch(IOException e) {
                e.printStackTrace();
    }} else {
            System.err.println("Bandeja do sistema não suportada.");
        }
    }



public boolean authenticate() throws InterruptedException {
    String jsonFilePath = "token.json";

    ObjectMapper objectMapper = new ObjectMapper();
    File jsonFile = new File(jsonFilePath);

    for (int attempt = 0; attempt < 3; attempt++) {
        try {
            if (!jsonFile.exists()) {
                // Cria um novo arquivo JSON com os valores iniciais
                createInitialJson(jsonFile, objectMapper);
            }

            // O arquivo JSON existe, então verifica os campos user_Token e app_Token
            JsonNode jsonObject = objectMapper.readTree(jsonFile);

            String userToken = jsonObject.get("user_Token").asText();
            String appToken = jsonObject.get("app_Token").asText();
            
            // Verifica as condições de autenticação e retorna true se for autenticado
            if ( Json.getSession()) {
                return false;
            } else {
                if (attempt < 2) {
                    
                    Thread.sleep(1000); // 1 segundo
                } else {
                    return true; // Terceira tentativa falhou, retorna false
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    return false; // Todas as tentativas falharam
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
        if (app.getSize().equals(originalSize)) {
            GraphicsEnvironment environment = GraphicsEnvironment.getLocalGraphicsEnvironment();
            GraphicsDevice screenDevice = environment.getDefaultScreenDevice();
            Rectangle screenBounds = screenDevice.getDefaultConfiguration().getBounds();

            // Calculate available height (screen height minus taskbar height)
            Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(screenDevice.getDefaultConfiguration());
            int availableHeight = screenBounds.height - screenInsets.bottom;

            // Maximize the JFrame size
            app.setSize(screenBounds.width, availableHeight);
            app.setLocationRelativeTo(null);
        } else {
            // Restore original size and location
            app.setSize(originalSize);
            app.setLocationRelativeTo(null);
        }
    }
        public static void minimize() {
        app.setExtendedState(JFrame.ICONIFIED);
    }

          public static void close() {
        app.setState(JFrame.ICONIFIED);
               app.setVisible(false); // Ocultar a janela

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
                   try {
                       app = new Application();
                   } catch (InterruptedException ex) {
                       Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
                   }
                   try {
                       if (!app.authenticate()){
                           app.setVisible(true);
                           try {
                               showForm(new FormDashboard());
                           } catch (IOException ex) {
                               Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
                           } catch (URISyntaxException ex) {
                               Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
                           } catch (ParseException ex) {
                               Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
                           }
                       }      } catch (InterruptedException ex) {
                       Logger.getLogger(Application.class.getName()).log(Level.SEVERE, null, ex);
                   }
            

        });

    }

    // Variables declaration - do not modify                     
    // End of variables declaration                   
}
