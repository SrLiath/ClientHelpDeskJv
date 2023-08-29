/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package raven.controllers;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class Dicio {
public static String urgencia(String valorNumerico ){
        Map<Integer, String> mapaDeConversao = new HashMap<>();
        
        mapaDeConversao.put(5, "Muito Alta");
        mapaDeConversao.put(4, "Alta");
        mapaDeConversao.put(3, "Média");
        mapaDeConversao.put(2, "Baixa");
        mapaDeConversao.put(1, "Muito Baixa");
        int numeroInteiro = Integer.parseInt(valorNumerico);
            String resultado = mapaDeConversao.get(numeroInteiro);
            return resultado;
}

public static String makeCategory(String categoria) {
        try {
            // Carregar o conteúdo do arquivo JSON
            FileReader reader = new FileReader("config.json");
            JSONTokener tokener = new JSONTokener(reader);
            JSONArray json = new JSONArray(tokener);
            
            // Iterar através do JSON e procurar a categoria correspondente
            for (int i = 0; i < json.length(); i++) {
                JSONObject jsonObject = json.getJSONObject(i);
                int id = jsonObject.getInt("id");
                String completename = jsonObject.getString("completename");
                if (id == Integer.parseInt(categoria)) {
                    return completename;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
public static String getStatus(Integer value){
           Map<Integer, String> statusMap = new HashMap<>();
        statusMap.put(1, "Novo");
        statusMap.put(2, "Em Andamento (atribuído)");
        statusMap.put(3, "Em Andamento (planejado)");
        statusMap.put(4, "Pendente");
        statusMap.put(5, "Solucionado");
        statusMap.put(6, "Fechado");
            int numeroInteiro =value;
            String resultado = statusMap.get(numeroInteiro);
            return resultado;
}


}
