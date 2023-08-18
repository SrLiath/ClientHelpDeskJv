package raven.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import raven.views.Login;
import raven.controllers.Json;
import java.io.IOException;
import java.io.File;




public class ClientHelpDesk {
    
    public boolean authenticate() {
        String jsonFilePath = "token.json";

        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(jsonFilePath);

        try {
            if (!jsonFile.exists()) {
                // Cria um novo arquivo JSON com os valores iniciais
                ObjectNode initialJson = objectMapper.createObjectNode();
                initialJson.put("user_Token", "");
                initialJson.put("app_Token", "");
                initialJson.put("url", "https://suporte.techsize.com.br/apirest.php/");
                initialJson.put("inventory_install", "");

                objectMapper.writeValue(jsonFile, initialJson);
                System.out.println("Arquivo JSON criado com valores iniciais.");
            }

            // O arquivo JSON existe, então verifica os campos user_Token e app_Token
            JsonNode jsonObject = objectMapper.readTree(jsonFile);

            String userToken = jsonObject.get("user_Token").asText();
            String appToken = jsonObject.get("app_Token").asText();

            if (userToken.isEmpty() || appToken.isEmpty()) {
                // Chama a tela Swing de login
                Login login = new Login();
        
                // Abre tela de Login
                login.setVisible(true);
                return true;
            } else {
                if (Json.hasSessionToken()){
                    //////////////////CHECAGEM DE SESSION///////////////////
                    if(Json.checkSession()){
                        return true;
                    } else {
                        if(Json.getSession()){
                            return true;
                        } else {
                            // Chama a tela Swing de login
                            Login login = new Login();
        
                            // Set the login view visible
                            login.setVisible(true);
                            return true;
                        }
                    }
                } else {
                    if(Json.getSession()){
                        return true;
                    } else {
                        // Chama a tela Swing de login
                        Login login = new Login();
        
                        // Set the login view visible
                        login.setVisible(true);
                        return true;
                    }
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
