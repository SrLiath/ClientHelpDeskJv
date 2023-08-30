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
public static String urgenciaReverse(String textoUrgencia) {
    Map<String, Integer> mapaDeConversao = new HashMap<>();
    
    mapaDeConversao.put("Muito Alta", 5);
    mapaDeConversao.put("Alta", 4);
    mapaDeConversao.put("Média", 3);
    mapaDeConversao.put("Baixa", 2);
    mapaDeConversao.put("Muito Baixa", 1);
    
    int numero = mapaDeConversao.get(textoUrgencia);
    
    return Integer.toString(numero); 
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

public static String makeCategoryReverse(String categoria, String subCategoria, String acao) {
    StringBuilder result = new StringBuilder();

    if (categoria != null && !categoria.equals("---")) {
        result.append(categoria);
    }

    if (subCategoria != null && !subCategoria.equals("---")) {
        if (result.length() > 0) {
            result.append(" > ");
        }
        result.append(subCategoria);
    }

    if (acao != null && !acao.equals("---")) {
        if (result.length() > 0) {
            result.append(" > ");
        }
        result.append(acao);
    }

    String var = result.toString();

    try {
        // Ler o conteúdo do arquivo config.json e transformá-lo em um objeto JSON
        JSONTokener tokener = new JSONTokener(new FileReader("config.json"));
        JSONArray jsonArray = new JSONArray(tokener);

        // Iterar sobre os elementos do objeto JSON para encontrar o elemento correspondente à string var
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String completename = jsonObject.optString("completename", "");

            if (var.equals(completename)) {
                // Se a string var corresponder a completename, obtenha o valor "id"
                String id = jsonObject.optString("id", "");
                return id;
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return ""; // Retornar uma string vazia se não encontrar correspondência
}


public static String getStatus(Integer value){
           Map<Integer, String> statusMap = new HashMap<>();
        statusMap.put(0, "Todos");
        statusMap.put(1, "Novo");
        statusMap.put(2, "Em Andamento (atribuído)");
        statusMap.put(3, "Em Andamento (planejado)");
        statusMap.put(4, "Pendente");
        statusMap.put(5, "Solucionado");
        statusMap.put(6, "Fechado");
            int numeroInteiro =value;
            String resultado = statusMap.get(numeroInteiro);
            if (resultado.equals("0")){
                return "";
            }
            return resultado;
}
public static String getStatusReverse(String status) {
    Map<String, String> reverseStatusMap = new HashMap<>();
    reverseStatusMap.put("Todos", "0");
    reverseStatusMap.put("Novo", "1");
    reverseStatusMap.put("Em Andamento (atribuído)", "2");
    reverseStatusMap.put("Em Andamento (planejado)", "3");
    reverseStatusMap.put("Pendente", "4");
    reverseStatusMap.put("Solucionado", "5");
    reverseStatusMap.put("Fechado", "6");

    String result = reverseStatusMap.get(status);
    if ("0".equals(result)) {
        return ""; // Or some other default value to indicate an invalid status
    }
    return result;
}



}
