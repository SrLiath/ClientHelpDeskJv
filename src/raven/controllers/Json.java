package raven.controllers;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
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
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
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
         
  public static String makeGetRequestPanel(String baseUrl, String sessionToken, String appToken, String userToken, String filtro) throws IOException, URISyntaxException {
        try {
            System.out.println(filtro);
            HttpClient httpClient = HttpClients.createDefault();

            // Construct the URL with parameters
            URIBuilder uriBuilder = new URIBuilder(baseUrl);
            uriBuilder.addParameter("session_token", sessionToken);
            uriBuilder.addParameter("app_token", appToken);
            uriBuilder.addParameter("user_token", userToken);
            uriBuilder.addParameter("criteria[1][field]", "4");
            uriBuilder.addParameter("criteria[1][searchtype]", "equals");
            uriBuilder.addParameter("criteria[1][value]", Integer.toString(getOwnId()));
            uriBuilder.addParameter("criteria[0][field]", "12");
            uriBuilder.addParameter("criteria[0][searchtype]", "equals");
            uriBuilder.addParameter("criteria[0][value]", filtro);
            uriBuilder.addParameter("criteria[2][field]", "1");
            uriBuilder.addParameter("criteria[2][searchtype]", "contains");
            uriBuilder.addParameter("criteria[2][value]", java.net.InetAddress.getLocalHost().getHostName());
            uriBuilder.addParameter("order", "DESC");
            uriBuilder.addParameter("sort", "2");
            uriBuilder.addParameter("range", "0-100");

            HttpGet httpGet = new HttpGet(uriBuilder.build());

            httpGet.setHeader("Accept", "*/*");
            httpGet.setHeader("Accept-Encoding", "gzip, deflate, br");
            httpGet.setHeader("Connection", "keep-alive");

            HttpResponse response = httpClient.execute(httpGet);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                return EntityUtils.toString(response.getEntity());
            } else {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (IOException e) {
            return null;
        }
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
 
 public static String sendTicket(String url, String body) {
        try {
            URL apiUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) apiUrl.openConnection();

            // Configurar o método HTTP para POST
            connection.setRequestMethod("POST");

            // Definir os headers da requisição, se necessário
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "user_token " + getU());
            connection.setRequestProperty("App-Token", getA());
            connection.setRequestProperty("Session-Token", getS());
            connection.setRequestProperty("Connection", "keep-alive");

            // Habilitar o envio do corpo da requisição
            connection.setDoOutput(true);

            // Escrever o corpo da requisição no OutputStream
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = body.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Obter a resposta da requisição
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Ler a resposta do servidor
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Fechar a conexão
            connection.disconnect();

            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

public static String getTecnico(String id, String own) throws IOException, URISyntaxException {
    String detalhes = Json.makeGetRequest(getUrl() + "Ticket/" + id + "/Ticket_User/", getS(), getA(), getU());
    JSONArray detalhesArray = new JSONArray(detalhes);

    for (int i = 0; i < detalhesArray.length(); i++) {
        JSONObject detalhesJSON = detalhesArray.getJSONObject(i);
        int userId = detalhesJSON.optInt("users_id", -1);

        if (userId != -1 && !Integer.toString(userId).equals(own)) {
            String tecnicoDetalhes = Json.makeGetRequest(getUrl() + "User/" + userId, getS(), getA(), getU());
            JSONObject tecnicoDetalhesJSON = new JSONObject(tecnicoDetalhes);
            String nomeTecnico = tecnicoDetalhesJSON.optString("firstname", "");
            String sobrenomeTecnico = tecnicoDetalhesJSON.optString("realname", "");
            return nomeTecnico + " " + sobrenomeTecnico;

        }
    }

    return "não atribuído";
}

public static Integer getOwnId() throws IOException, URISyntaxException{
    String detalhes = Json.makeGetRequest(getUrl() + "getFullSession/", getS(), getA(), getU());
    JSONObject detalhesJSON = new JSONObject(detalhes);
    System.out.println(detalhesJSON);
    JSONObject sessionJSON = detalhesJSON.getJSONObject("session"); 
    String idString = sessionJSON.optString("glpiID", ""); 


    int id = Integer.parseInt(idString);
    return id;
}

}
