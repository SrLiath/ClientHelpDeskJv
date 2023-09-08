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
import java.net.UnknownHostException;


public class Local {
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
