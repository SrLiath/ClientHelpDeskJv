/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package raven.controllers;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Local {

        public static String getAnyId() {
        try {
            // Crie um processo para executar o script.bat
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "GetAnyId.bat");
            
            // Redirecione a saída do processo para um BufferedReader
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            
            // Leia a saída do processo e armazene-a em uma StringBuilder
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            
            // Aguarde até que o processo seja concluído
            int exitCode = process.waitFor();
            
            if (exitCode == 0) {
                return output.toString();
            } else {
                // Se o script retornar um código de saída não zero, retorne uma string vazia
                return "";
            }
        } catch (IOException | InterruptedException e) {
            // Em caso de erro, retorne uma string vazia
            return "";
        }
    }

    public static String calculateHash(String input) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));

    // Converte os bytes do hash em uma representação hexadecimal
    StringBuilder hexString = new StringBuilder();
    for (byte hashByte : hashBytes) {
        String hex = Integer.toHexString(0xff & hashByte);
        if (hex.length() == 1) {
            hexString.append('0');
        }
        hexString.append(hex);
    }

    return hexString.toString();
    }
    
    public static void makeConfig(String conteudo) {
    try {
        // Cria um objeto Writer com UTF-8 para o arquivo especificado
        Writer writer = new OutputStreamWriter(new FileOutputStream("config.json"), "UTF-8");
        
        // Escreve o conteúdo da variável no arquivo
        writer.write(conteudo);
        
        // Fecha o arquivo
        writer.close();
        
        System.out.println("Conteúdo escrito com sucesso no arquivo.");
    } catch (IOException e) {
        System.out.println("Ocorreu um erro ao escrever no arquivo: " + e.getMessage());
    }
}
    public static String getMachineName() {
        try {
            InetAddress localMachine = InetAddress.getLocalHost();
            return localMachine.getHostName();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "Unknown";
        }
    }
}
