package raven.controllers;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

public class Json {
    public static boolean checkSession() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Ler o arquivo JSON
            File jsonFile = new File("token.json");
            JsonNode tokenJson = objectMapper.readTree(jsonFile);

            // Obter a URL do JSON
            String url = tokenJson.get("url").asText();

            // Obter os valores
            String appToken = tokenJson.get("app_Token").asText();

            // Construir a URL para a requisição
            String requestUrl = url + "getMyProfiles/";

            // Criar um objeto JSON para enviar na requisição
            JsonNode requestData = objectMapper.createObjectNode()
                    .put("app_token", appToken)
                    .put("session_token", tokenJson.get("session_token").asText());

            // Fazer a requisição POST
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(requestUrl);
            httpPost.setEntity(new StringEntity(requestData.toString()));
            httpPost.setHeader("Content-Type", "application/json");

            HttpResponse response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
     public static boolean hasSessionToken() {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Read the JSON file
            File jsonFile = new File("token.json");
            JsonNode tokenJson = objectMapper.readTree(jsonFile);

            // Check if the "session_token" field exists in the JSON
            JsonNode sessionTokenNode = tokenJson.get("session_token");
            return sessionTokenNode != null && !sessionTokenNode.isNull();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
     
     
     public static void insertJson(String app, String user){
     ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Ler o arquivo JSON
            File jsonFile = new File("token.json");
            JsonNode tokenJson = objectMapper.readTree(jsonFile);
            ((com.fasterxml.jackson.databind.node.ObjectNode) tokenJson).put("app_Token", app);
            ((com.fasterxml.jackson.databind.node.ObjectNode) tokenJson).put("user_Token", user);
            objectMapper.writeValue(jsonFile, tokenJson);

            System.out.println("token.json atualizado com novo valor de app_Token.");

        } catch (IOException e) {
            e.printStackTrace();
        }
     }
     public static boolean getSession(){
    ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Ler o arquivo JSON
            File jsonFile = new File("token.json");
            JsonNode tokenJson = objectMapper.readTree(jsonFile);

            // Obter o valor do campo "url" do JSON
            String url = tokenJson.get("url").asText();

            // Obter os valores de user_token e app_token do JSON
            String userToken = tokenJson.get("user_Token").asText();
            String appToken = tokenJson.get("app_Token").asText();

            // Construir a URL para a requisição
            String requestUrl = url + "initSession/";

            // Criar um objeto JSON para enviar na requisição
            JsonNode requestData = objectMapper.createObjectNode()
                    .put("user_token", userToken)
                    .put("app_token", appToken);

            // Fazer a requisição POST
            HttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(requestUrl);
            httpPost.setEntity(new StringEntity(requestData.toString()));
            httpPost.setHeader("Content-Type", "application/json");

            HttpResponse response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            String responseBody = EntityUtils.toString(response.getEntity());
            
            System.out.println("Status Code: " + statusCode);
            System.out.println("Response Body: " + responseBody);
            if (statusCode == HttpStatus.SC_OK) { // Status code 200
                // Parse the response JSON to get the session_token
                JsonNode responseJson = objectMapper.readTree(responseBody);
                String sessionToken = responseJson.get("session_token").asText();

                // Adicionar o session_token ao tokenJson
                ((com.fasterxml.jackson.databind.node.ObjectNode) tokenJson).put("session_token", sessionToken);

                // Salvar as alterações de volta no arquivo JSON
                objectMapper.writeValue(jsonFile, tokenJson);

                System.out.println("token.json atualizado com novo session_token.");
                return true;
            } else {
                System.out.println("Erro na requisição: Código de status " + statusCode + " " + requestUrl);
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
       
     }

}
