package atualizador;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.file.Paths;
import java.security.*;
import java.util.*;
import org.json.*;

public class Atualizador {
    private JFrame frame;
    private JPanel mainPanel;
    private JProgressBar progressBar;

    static String updateJsonUrl = "http://techapp.techsize.com.br"; // Link update
    static int portToCheck = 25786;
  private static String getJarFolder() {
        ProtectionDomain protectionDomain = Atualizador.class.getProtectionDomain();
        CodeSource codeSource = protectionDomain.getCodeSource();
        URL location = codeSource.getLocation();
        
        String path = location.getPath();
        if (path.endsWith(".jar")) {
            int lastSlashIndex = path.lastIndexOf('/');
            if (lastSlashIndex >= 0) {
                path = path.substring(0, lastSlashIndex + 1);
            }
        }
        
        // Remova o / do início do caminho
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        
        return path;
    }
    public Atualizador() {

        frame = new JFrame("TechSize Atualizador");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 370);
        frame.setLocationRelativeTo(null);
        mainPanel = new JPanel(new BorderLayout());
        frame.setUndecorated(true);
        progressBar = new JProgressBar(0, 100);
        progressBar.setString("Fazendo atualizações");
        progressBar.setStringPainted(true);
                ImageIcon icon = new ImageIcon("Interface/images/icone_sistema.png"); // Atualize o caminho conforme o local do ícone
        frame.setIconImage(icon.getImage());
        CustomImageLabel backgroundLabel = new CustomImageLabel("monokai.jpg");
        backgroundLabel.setLayout(new BorderLayout());
        backgroundLabel.add(progressBar, BorderLayout.SOUTH);

        ImageIcon logoIcon = new ImageIcon("logo.png");
        Image logoImage = logoIcon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);
        logoIcon = new ImageIcon(logoImage);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setHorizontalAlignment(JLabel.CENTER);

        backgroundLabel.add(logoLabel, BorderLayout.CENTER);

        mainPanel.add(backgroundLabel, BorderLayout.CENTER);

        frame.add(mainPanel);
        frame.setVisible(true);

        Thread serverThread = new Thread(() -> {
            try {
                runServer();
                SwingUtilities.invokeLater(() -> frame.dispose());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        serverThread.start();
    }

    private void runServer() {
        try {
String hostToCheck = "localhost";
            if (isPortOpen(hostToCheck, portToCheck)) {
                System.exit(0);
            }
            // Download update.json
            System.out.println("Fetching " + updateJsonUrl + " ...");
            URL updateJsonUrlObject = new URL(updateJsonUrl);
            HttpURLConnection connection = (HttpURLConnection) updateJsonUrlObject.openConnection();
            connection.setRequestMethod("GET");

            // Check for HTTP errors
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("Error while fetching update.json. HTTP error code: " + connection.getResponseCode());
                return;
            }

            // Get response content
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder responseTextBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                responseTextBuilder.append(line);
            }
            reader.close();

            String responseText = responseTextBuilder.toString();

            // Check if the content is not empty
            if (!responseText.isEmpty()) {
                System.out.println("Decoding update.json ...");
                try {
                    JSONObject updateData = new JSONObject(responseText);
                    // Update files based on the data from update.json
                    if (updateData.has("file_hashes")) {
                        System.out.println("Updating files ...");
                        updateFiles(updateData.getJSONObject("file_hashes"), getJarFolder(),"");
                    } else {
                        System.out.println("'file_hashes' not found in update.json");
                    }
                } catch (JSONException e) {
                    System.out.println("Error decoding update.json: " + e.getMessage());
                    System.out.println("Response content:");
                    System.out.println(responseText);
                }
            } else {
                System.out.println("update.json is empty");
            }

            if (!isPortOpen(hostToCheck, portToCheck)) {
                try {
                    Process process = Runtime.getRuntime().exec("java\\bin\\java.exe -jar GLPI.jar");
                    System.exit(0);
                } catch (IOException e) {
                    System.out.println("Error executing GLPI.exe: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error while fetching update.json: " + e.getMessage());
        }
    }






    private boolean isPortOpen(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 1000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private String calculateFileHash(String filePath) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            FileInputStream fis = new FileInputStream(filePath);
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                digest.update(buffer, 0, bytesRead);
            }
            fis.close();
            byte[] hashBytes = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                hexString.append(String.format("%02x", hashByte));
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateFiles(JSONObject updateData, String baseFolder, String currentPath) {
    for (String item : updateData.keySet()) {
        Object data = updateData.get(item);
        File currentFile = new File(baseFolder, item);
        String folder = currentFile.getParent();
        String absolutePath = currentFile.getAbsolutePath();

        String newPath = currentPath.isEmpty() ? item : currentPath + "/" + item;
        String remoteUrl = updateJsonUrl + "/files/" + newPath;

 if (data instanceof JSONObject) {
    if (!currentFile.exists()) {
        currentFile.mkdirs();
    }
    updateFiles((JSONObject) data, currentFile.getAbsolutePath(), newPath); // Pass the absolute path
} else if (data instanceof String) {
    String localPath = absolutePath;

    if (!currentFile.exists()) {
        System.out.println("File does not exist locally. Downloading...");
        downloadFile(remoteUrl, localPath, folder);
    } else {
        System.out.println("Checking " + item + " from " + remoteUrl + " ...");
        System.out.println("Local path: " + localPath);
        System.out.println("Absolute path: " + absolutePath);

        String remoteHash = (String) data; // Assuming the API provides the hash as a String
        String localHash = calculateFileHash(localPath);

        if (localHash != null && !localHash.equals(remoteHash)) {
            // Hashes are different, download the file
            System.out.println("Hashes are different. Downloading...");
            downloadFile(remoteUrl, localPath, folder);
            // Rest of the update logic...
        } else {
            System.out.println("Local hash matches remote hash. Skipping download.");
        }
    }
} else {
    System.out.println("Unsupported data type for item: " + item);
}

    }
}

private void downloadFile(String url, String localPath, String folder) {
    System.out.println("Downloading from: " + url);
    System.out.println("Saving to: " + localPath);

    try {
        URL fileUrl = new URL(url);

        File parentDir = new File(folder); // Use the folder path instead of localPath
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        String fileName = new File(url).getName(); // Extract the file name from the URL
        String fullFilePath = new File(parentDir, fileName).getAbsolutePath();

        try (InputStream in = fileUrl.openStream();
             OutputStream out = new FileOutputStream(fullFilePath)) {

            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
            }
            System.out.println("Download completed.");
        }
    } catch (IOException e) {
        e.printStackTrace();
        System.out.println("Download failed: " + e.getMessage());
    }
}



    public static void main(String[] args) {
        SwingUtilities.invokeLater(Atualizador::new);
    }
}

class CustomImageLabel extends JLabel {
    private BufferedImage image;

    public CustomImageLabel(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
