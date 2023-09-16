package raven.application.form.other;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.util.logging.Level;
import java.util.logging.Logger;
import raven.application.Application;
import raven.controllers.Dicio;
import raven.controllers.Json;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import org.json.JSONTokener;



public class Detail extends javax.swing.JPanel {
    private Color originalColor;
    private int ownId = Json.getOwnId();
    private JPanel chatPanel ;
    private int idPanel;

    public Detail(String id) throws IOException, URISyntaxException, ParseException {
        idPanel = Integer.parseInt(id);
        initComponents();
        String detalhes = Json.makeGetRequest(Json.getUrl() + "Ticket/" + id, Json.getS(), Json.getA(), Json.getU());
        JSONObject detalhesJSON = new JSONObject(detalhes);
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date dataCriacaoc = null;
        Date ultimaAtualizacaoc = null;
        String dataCriacaoStr = detalhesJSON.optString("date", "");
        String ultimaAtualizacaoStr = detalhesJSON.optString("date_mod", "");
        String category = detalhesJSON.optString("itilcategories_id", "");
        String urgency = detalhesJSON.optString("urgency", "");
        String ownId = detalhesJSON.optString("users_id_recipient", "");
        String status = detalhesJSON.optString("status", "");
        String tituloChat = detalhesJSON.optString("name", "");
        String textContentChat = detalhesJSON.optString("content", "");
        


        
        dataCriacaoc = inputDateFormat.parse(dataCriacaoStr);
        ultimaAtualizacaoc = inputDateFormat.parse(ultimaAtualizacaoStr);
        String dataCriacaoFormatted = outputDateFormat.format(dataCriacaoc);
        String ultimaAtualizacaoFormatted = outputDateFormat.format(ultimaAtualizacaoc);
        dataAbertura.setText(dataCriacaoFormatted);
        ultimaAtualizacao.setText(ultimaAtualizacaoFormatted);
        category = Dicio.makeCategory(category);
        urgency = Dicio.urgencia(urgency);
        int idStatus = Integer.parseInt(status);
        status = Dicio.getStatus(idStatus);
        categoriaChamado.setText(category);
        urgenciaChamado.setText(urgency);
        String tecnico = Json.getTecnico(id, ownId);
        tecnicoResponsavel.setText(tecnico);
//        tecnicoDetail.setText(tecnico);
//        tituloDetail.setText(tituloChat);
        statusChamado.setText(status);
        //textChat.setText(textContentChat);
        JPanel chatPanel = new JPanel();
        JPanel chatContainer = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatContainer.setLayout(new BoxLayout(chatContainer, BoxLayout.Y_AXIS));

        enviado(chatPanel,Json.getOwnName(),textContentChat);

        if (tecnico != null && !tecnico.isEmpty()) {
    int check = 0;
    String detalhesTecnicos = Json.makeGetRequest(Json.getUrl() + "Ticket/" + id + "/ITILFollowup/", Json.getS(), Json.getA(), Json.getU());
    String documentItenss = Json.makeGetRequest(Json.getUrl() + "Ticket/" + id + "/Document_Item/", Json.getS(), Json.getA(), Json.getU());

    JSONArray detalhesTecnicosArray = new JSONArray(detalhesTecnicos);
    JSONArray documentItensArray = new JSONArray(documentItenss);

for (int i = 0; i < documentItensArray.length(); i++) {
    JSONObject documentItem = documentItensArray.getJSONObject(i);

    // Verifique se 'rel' é igual a "Document"
    if (documentItem.has("links")) {
        JSONArray linksArray = documentItem.getJSONArray("links");
        for (int j = 0; j < linksArray.length(); j++) {
            JSONObject linkObj = linksArray.getJSONObject(j);
            if (linkObj.has("rel") && linkObj.getString("rel").equals("Document")) {
                String href = linkObj.getString("href");
                String dados = Json.makeGetRequest(href, Json.getS(), Json.getA(), Json.getU());

                // Analise os dados obtidos
                JSONObject dadosObj = new JSONObject(dados);
                
                 if (dadosObj.has("date_creation")) {
                String dataCriacao = dadosObj.getString("date_creation");
                dadosObj.put("date", dataCriacao);
                dadosObj.put("content", "");

                dadosObj.remove("date_creation");
    }
                detalhesTecnicosArray.put(dadosObj);
            }
        }
    }
}

// Agora, detalhesTecnicosArray contém os novos dados obtidos
String novoDetalhesTecnicos = detalhesTecnicosArray.toString();
JSONArray detalhesTecnicosArrayD = new JSONArray(new JSONTokener(novoDetalhesTecnicos));

        // 2. Definir um comparador personalizado para datas
        Comparator<JSONObject> dateComparator = new Comparator<JSONObject>() {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            @Override
            public int compare(JSONObject obj1, JSONObject obj2) {
                try {
                    Date date1 = dateFormat.parse(obj1.getString("date"));
                    Date date2 = dateFormat.parse(obj2.getString("date"));
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                    return 0;
                }
            }
        };

        // 3. Converter o JSONArray para uma lista de objetos JSONObject
        List<JSONObject> detalhesTecnicosList = new ArrayList<>();
        for (int i = 0; i < detalhesTecnicosArray.length(); i++) {
            detalhesTecnicosList.add(detalhesTecnicosArray.getJSONObject(i));
        }

        // 4. Classificar a lista com base nas datas
        Collections.sort(detalhesTecnicosList, dateComparator);

        // 5. Converter a lista de volta para um JSONArray
        JSONArray detalhesTecnicosArrayOrdenados = new JSONArray(detalhesTecnicosList);
        detalhesTecnicos = detalhesTecnicosArrayOrdenados.toString();
    JSONArray tecnicoDetalhesArray = new JSONArray(detalhesTecnicos);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    // Converter JSONArray em uma lista de objetos JSON
    List<JSONObject> tecnicoDetalhesList = new ArrayList<>();
    for (int i = 0; i < tecnicoDetalhesArray.length(); i++) {
        tecnicoDetalhesList.add(tecnicoDetalhesArray.getJSONObject(i));
    }

      for (JSONObject item : tecnicoDetalhesList) {
          System.out.println(item);
            String content = item.getString("content");
            String userId = Integer.toString(item.getInt("users_id"));
            
            // Decodificar caracteres HTML usando Jsoup
            Document doc = Jsoup.parse(content);
            String decodedContent = doc.text();          
            String formattedText = removeHtmlTags(decodedContent);
            String mime = item.has("mime") ? item.getString("mime") : "";
            if (mime.equals("")){
            if (ownId.toString().equals(userId.toString())) {
                enviado(chatPanel, Json.getOwnName(), formattedText);
            } else {
                recebido(chatPanel, Json.getTecnico(id, Integer.toString(Json.getOwnId())), formattedText);
            }}else{
                  if (!ownId.toString().equals(userId.toString())) {
                String name = item.getString("filename");
                enviadoFile(chatPanel, Json.getOwnName(), name, item);
            } else if(!mime.equals("")) {
                String name = item.getString("filename");
                recebidoFile(chatPanel, Json.getOwnName(), name, item);
            }

            }
            
            chatContainer.add(chatPanel);
            check = 1;

        }

if (check == 0){
    chatContainer.add(chatPanel);
}
frameChat.setViewportView(chatContainer);
JViewport viewport = frameChat.getViewport();
viewport.setViewPosition(new java.awt.Point(0, viewport.getViewSize().height));


 
} else {
            chatContainer.add(chatPanel);
            frameChat.setViewportView(chatContainer);


}

        

        if (!detalhesJSON.isNull("time_to_own")) {
        String tempoAtendiment = detalhesJSON.optString("time_to_own");
        Date tempoAtendimentoc = inputDateFormat.parse(tempoAtendiment);
        String tempoAtendimentoFormatted = outputDateFormat.format(tempoAtendimentoc);
        tempoAtendimento.setText(tempoAtendimentoFormatted);
        } else {
            tempoAtendimento.setText("N/A"); // Or any default value you prefer
        }
        if (!detalhesJSON.isNull("time_to_resolve")) {
        String tempoResolve = detalhesJSON.optString("time_to_resolve");
        Date tempoResolvec = inputDateFormat.parse(tempoResolve);
        String tempoSolucaoFormatted = outputDateFormat.format(tempoResolvec);
        tempoSolucao.setText(tempoSolucaoFormatted);
        } else {
            tempoSolucao.setText("N/A"); // Or any default value you prefer
        }

        // Disable text fields
        dataAbertura.setEnabled(false);
        tempoAtendimento.setEnabled(false);
        tempoSolucao.setEnabled(false);
        ultimaAtualizacao.setEnabled(false);
        categoriaChamado.setEnabled(false);
        urgenciaChamado.setEnabled(false);
        tecnicoResponsavel.setEnabled(false);
        statusChamado.setEnabled(false);

        int newWidth = 20;
        int newHeight = 20;
        //Btn fechar minimizar e maximizar
        ImageIcon iconx = new ImageIcon("src/raven/Interface/images/icons/images/x.png");
        Image imgx = iconx.getImage();
        Image imgScalex = imgx.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIconx = new ImageIcon(imgScalex);
        btnX.setIcon(scaledIconx);
        btnX.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Application.close();
            }
        });
        ImageIcon iconMin = new ImageIcon("src/raven/Interface/images/icons/images/ma.png");
        Image imgMin = iconMin.getImage();
        Image imgScaleMin = imgMin.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIconMin = new ImageIcon(imgScaleMin);
        btnMin.setIcon(scaledIconMin);
        btnMin.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Application.minimize();
            }
        });
        ImageIcon iconMax = new ImageIcon("src/raven/Interface/images/icons/images/mx.png");
        Image imgMax = iconMax.getImage();
        Image imgScaleMax = imgMax.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIconMax = new ImageIcon(imgScaleMax);
        btnMax.setIcon(scaledIconMax);
        btnMax.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Application.maximize();
            }
        });
        detailTicket.putClientProperty(FlatClientProperties.STYLE,"background: $Panel.right.background;");
        Bar.putClientProperty(FlatClientProperties.STYLE,"background: $Bar.background;");
        




    }
    


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Bar = new javax.swing.JPanel();
        btnMin = new javax.swing.JLabel();
        btnMax = new javax.swing.JLabel();
        btnX = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        detail = new javax.swing.JPanel();
        detailTicket = new javax.swing.JPanel();
        dataAbertura = new javax.swing.JTextField();
        tempoAtendimento = new javax.swing.JTextField();
        tempoSolucao = new javax.swing.JTextField();
        ultimaAtualizacao = new javax.swing.JTextField();
        categoriaChamado = new javax.swing.JTextField();
        urgenciaChamado = new javax.swing.JTextField();
        tecnicoResponsavel = new javax.swing.JTextField();
        statusChamado = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        textMsg = new javax.swing.JTextField();
        jToggleButton1 = new javax.swing.JToggleButton();
        frameChat = new javax.swing.JScrollPane();

        btnMin.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N

        btnMax.setFont(new java.awt.Font("Roboto Black", 1, 24)); // NOI18N

        btnX.setFont(new java.awt.Font("Roboto Black", 1, 24));
        btnX.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnXMouseClicked(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(204, 204, 204));
        jLabel2.setText("Suporte - TechSize");

        javax.swing.GroupLayout BarLayout = new javax.swing.GroupLayout(Bar);
        Bar.setLayout(BarLayout);
        BarLayout.setHorizontalGroup(
            BarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnMin, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnMax, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnX, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        BarLayout.setVerticalGroup(
            BarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(btnX, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(BarLayout.createSequentialGroup()
                .addGroup(BarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnMin, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addComponent(btnMax, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        dataAbertura.setMinimumSize(new java.awt.Dimension(70, 30));

        tempoAtendimento.setMinimumSize(new java.awt.Dimension(70, 30));

        tempoSolucao.setMinimumSize(new java.awt.Dimension(70, 30));

        ultimaAtualizacao.setMinimumSize(new java.awt.Dimension(70, 30));

        categoriaChamado.setMinimumSize(new java.awt.Dimension(70, 30));

        urgenciaChamado.setMinimumSize(new java.awt.Dimension(70, 30));

        tecnicoResponsavel.setMinimumSize(new java.awt.Dimension(70, 30));

        statusChamado.setMinimumSize(new java.awt.Dimension(70, 30));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setText("Chamado");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setText("Voltar");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });

        jLabel4.setText("Data abertura");

        jLabel5.setText("Tempo para atendimento");

        jLabel6.setText("Tempo para solução");

        jLabel7.setText("Ultima atualização");

        jLabel8.setText("Categoria");

        jLabel9.setText("Urgência");

        jLabel10.setText("Técnico responsável");

        jLabel11.setText("Status");

        javax.swing.GroupLayout detailTicketLayout = new javax.swing.GroupLayout(detailTicket);
        detailTicket.setLayout(detailTicketLayout);
        detailTicketLayout.setHorizontalGroup(
            detailTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, detailTicketLayout.createSequentialGroup()
                .addGroup(detailTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, detailTicketLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, detailTicketLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(detailTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel11, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(detailTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(detailTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(tempoSolucao, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                                .addComponent(ultimaAtualizacao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(categoriaChamado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(statusChamado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(urgenciaChamado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tecnicoResponsavel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(tempoAtendimento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(dataAbertura, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE))))
                .addGap(44, 44, 44))
        );
        detailTicketLayout.setVerticalGroup(
            detailTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(detailTicketLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(detailTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(detailTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dataAbertura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(detailTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tempoAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addGroup(detailTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tempoSolucao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(detailTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ultimaAtualizacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(24, 24, 24)
                .addGroup(detailTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(categoriaChamado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(18, 18, 18)
                .addGroup(detailTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(urgenciaChamado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addGap(18, 18, 18)
                .addGroup(detailTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tecnicoResponsavel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(detailTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusChamado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addContainerGap(74, Short.MAX_VALUE))
        );

        originalColor = jLabel3.getForeground(); // Armazena a cor original

        jLabel3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                jLabel3.setForeground(new Color(85, 170, 255)); // Define a cor ao passar o mouse
            }

            @Override
            public void mouseExited(MouseEvent e) {
                jLabel3.setForeground(originalColor); // Restaura a cor original ao sair do hover
            }
        });

        textMsg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textMsgActionPerformed(evt);
            }
        });

        jToggleButton1.setLabel("");
        jToggleButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jToggleButton1MouseClicked(evt);
            }
        });
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout detailLayout = new javax.swing.GroupLayout(detail);
        detail.setLayout(detailLayout);
        detailLayout.setHorizontalGroup(
            detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, detailLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(detailLayout.createSequentialGroup()
                        .addComponent(textMsg, javax.swing.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(frameChat))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(detailTicket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        detailLayout.setVerticalGroup(
            detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(detailTicket, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, detailLayout.createSequentialGroup()
                .addComponent(frameChat)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, detailLayout.createSequentialGroup()
                        .addComponent(textMsg, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        // Carregue a imagem e ajuste-a ao tamanho do botão
        ImageIcon icon = new ImageIcon(getClass().getResource("/raven/Interface/images/icons/images/send.png"));
        Image img = icon.getImage().getScaledInstance(15,15,Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);

        jToggleButton1.setIcon(icon);
        frameChat.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Bar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(detail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(Bar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(detail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        Color corFundo = new Color(66, 71, 76); // Vermelho
        Bar.setBackground(corFundo);
    }// </editor-fold>//GEN-END:initComponents

    private void btnXMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnXMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnXMouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        try {
            Application.showForm(new FormDashboard());        // TODO add your handling code here:
        } catch (IOException ex) {
            Logger.getLogger(Detail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (URISyntaxException ex) {
            Logger.getLogger(Detail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Detail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jLabel3MouseClicked

    private void textMsgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textMsgActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textMsgActionPerformed

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jToggleButton1MouseClicked
        // TODO add your handling code here:
             String msg = "";
        msg = textMsg.getText();
        if (msg != ""){
        Json.sendMsg(Json.getS(), Json.getA(), idPanel, ownId, msg);
        }
        textMsg.setText("");
         try {
                Application.showForm(new Detail((String) Integer.toString(idPanel)));
            } catch (IOException ex) {
                Logger.getLogger(FormDashboard.class.getName()).log(Level.SEVERE, null, ex);
            } catch (URISyntaxException ex) {
                Logger.getLogger(FormDashboard.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(FormDashboard.class.getName()).log(Level.SEVERE, null, ex);
            }
    
    }//GEN-LAST:event_jToggleButton1MouseClicked
    public static String removeHtmlTags(String htmlText) {
        // Expressão regular para localizar tags HTML
        String regex = "<[^>]+>";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(htmlText);
        
        // Substituir as tags HTML por uma string vazia
        String formattedText = matcher.replaceAll("");
        return formattedText;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Bar;
    private javax.swing.JLabel btnMax;
    private javax.swing.JLabel btnMin;
    private javax.swing.JLabel btnX;
    private javax.swing.JTextField categoriaChamado;
    private javax.swing.JTextField dataAbertura;
    private javax.swing.JPanel detail;
    private javax.swing.JPanel detailTicket;
    private javax.swing.JScrollPane frameChat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JTextField statusChamado;
    private javax.swing.JTextField tecnicoResponsavel;
    private javax.swing.JTextField tempoAtendimento;
    private javax.swing.JTextField tempoSolucao;
    private javax.swing.JTextField textMsg;
    private javax.swing.JTextField ultimaAtualizacao;
    private javax.swing.JTextField urgenciaChamado;
    // End of variables declaration//GEN-END:variables
    public void enviado(JPanel chatPanel, String nome, String message) {
        // Crie uma instância do DetailChatOwn (supondo que DetailChatOwn seja um JPanel)
        DetailChatOwn detailChatOwn = new DetailChatOwn(nome, message);

        // Adicione o DetailChatOwn ao JPanel chatPanel
        chatPanel.add(detailChatOwn);
    }

    public void recebido(JPanel chatPanel, String nome, String message) {
        DetailChat detailChat = new DetailChat(nome, message);

        chatPanel.add(detailChat);
    }
    public void enviadoFile(JPanel chatPanel, String nome, String nomeFile, JSONObject json) {
        DetailChatFileReceive detailChatOwn = new DetailChatFileReceive(nome, nomeFile, json);

        chatPanel.add(detailChatOwn);
    }
        public void recebidoFile(JPanel chatPanel, String nome, String nomeFile, JSONObject json) {
        DetailChatFileSent detailChatOwn = new DetailChatFileSent(nome, nomeFile, json);

        chatPanel.add(detailChatOwn);
    }
}
