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
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

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

      public static String getA() {
        String appToken = null;

        try {
            // Cria um ObjectMapper para ler JSON
            ObjectMapper objectMapper = new ObjectMapper();

            // Lê o arquivo JSON e converte para um JsonNode
            JsonNode jsonNode = objectMapper.readTree(new File("token.json"));

            // Obtém o valor do app_Token do JsonNode
            appToken = jsonNode.get("app_Token").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return appToken;
    }
      
     public static String getS() {
        String sessionToken = null;

        try {
            // Cria um ObjectMapper para ler JSON
            ObjectMapper objectMapper = new ObjectMapper();

            // Lê o arquivo JSON e converte para um JsonNode
            JsonNode jsonNode = objectMapper.readTree(new File("token.json"));

            // Obtém o valor do app_Token do JsonNode
            sessionToken = jsonNode.get("session_token").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sessionToken;
    }
     
      public static String getU() {
        String userToken = null;

        try {
            // Cria um ObjectMapper para ler JSON
            ObjectMapper objectMapper = new ObjectMapper();

            // Lê o arquivo JSON e converte para um JsonNode
            JsonNode jsonNode = objectMapper.readTree(new File("token.json"));

            // Obtém o valor do app_Token do JsonNode
            userToken = jsonNode.get("user_Token").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userToken;
    }
         public static String getUrl() {
        String url = null;

        try {
            // Cria um ObjectMapper para ler JSON
            ObjectMapper objectMapper = new ObjectMapper();

            // Lê o arquivo JSON e converte para um JsonNode
            JsonNode jsonNode = objectMapper.readTree(new File("token.json"));

            // Obtém o valor do app_Token do JsonNode
            url = jsonNode.get("url").asText();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return url;
    }
 public static String makeGetRequest(String baseUrl, String sessionToken, String appToken, String userToken) throws IOException, URISyntaxException {
    try {
        HttpClient httpClient = HttpClients.createDefault();
        
        // Construct the URL with parameters
        URIBuilder uriBuilder = new URIBuilder(baseUrl);
        uriBuilder.addParameter("session_token", sessionToken);
        uriBuilder.addParameter("app_token", appToken);
        uriBuilder.addParameter("user_token", userToken);
        
        
        HttpGet httpGet = new HttpGet(uriBuilder.build());

        httpGet.setHeader("Accept", "*/*");
        httpGet.setHeader("Accept-Encoding", "gzip, deflate, br");
        httpGet.setHeader("Connection", "keep-alive");

        HttpResponse response = httpClient.execute(httpGet);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            return EntityUtils.toString(response.getEntity());
        } else {
            System.out.println("Request failed with status code: " + statusCode);
            return EntityUtils.toString(response.getEntity());
        }
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
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
}
