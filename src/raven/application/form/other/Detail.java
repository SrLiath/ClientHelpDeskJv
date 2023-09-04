package raven.application.form.other;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Image;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import raven.application.Application;
import raven.controllers.Json;
import javax.swing.table.DefaultTableModel;
import org.json.JSONException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import org.json.JSONArray;
import org.json.JSONObject;
import raven.application.Application;
import raven.controllers.Dicio;
import raven.controllers.Json;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 *
 * @author Raven
 */
public class Detail extends javax.swing.JPanel {
    private Color originalColor;
    public Detail(String id) throws IOException, URISyntaxException, ParseException {
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
        tecnicoDetail.setText(tecnico);
        tituloDetail.setText(tituloChat);
        statusChamado.setText(status);
        textChat.setText(textContentChat);
if (tecnico != null && !tecnico.isEmpty()) {
    String detalhesTecnicos = Json.makeGetRequest(Json.getUrl() + "Ticket/" + id + "/ITILFollowup/", Json.getS(), Json.getA(), Json.getU());
    JSONArray tecnicoDetalhesArray = new JSONArray(detalhesTecnicos);
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Converter JSONArray em uma lista de objetos JSON
    List<JSONObject> tecnicoDetalhesList = new ArrayList<>();
    for (int i = 0; i < tecnicoDetalhesArray.length(); i++) {
        tecnicoDetalhesList.add(tecnicoDetalhesArray.getJSONObject(i));
    }

    if (!tecnicoDetalhesList.isEmpty()) {
        // Ordenar a lista com base na data
        tecnicoDetalhesList.sort(Comparator.comparing(o -> {
            try {
                return dateFormat.parse(o.getString("date"));
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }
        }));

        // Obter o último item da lista ordenada
        JSONObject ultimoItem = tecnicoDetalhesList.get(tecnicoDetalhesList.size() - 1);
        String ultimaData = ultimoItem.getString("date");

        // Construir o conteúdo formatado
        StringBuilder formattedContent = new StringBuilder();
        for (JSONObject item : tecnicoDetalhesList) {
            String content = item.getString("content");
            
            // Decodificar caracteres HTML usando Jsoup
            Document doc = Jsoup.parse(content);
            String decodedContent = doc.text();
            
            formattedContent.append(decodedContent).append("\n\n");
        }

        // Construir a saída formatada
        StringBuilder formattedOutput = new StringBuilder();
        formattedOutput.append("Última data: ").append(ultimaData).append("\n").append("-------------\n").append(formattedContent);

        // Definir o texto formatado em respChat.setText()
        String formattedText = removeHtmlTags(formattedOutput.toString());
   
        respChat.setText(formattedText);
    } else {
        // Se a lista estiver vazia, não há detalhes disponíveis
        respChat.setText("Sem detalhes disponíveis.");
    }
} else {
    // Se tecnico for nulo ou vazio, não há operações a serem realizadas
    respChat.setText("Nenhum valor técnico disponível.");
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
              ImageIcon terminal = new ImageIcon("src/raven/Interface/images/icons/images/cil-terminal.png");
        Image imgt = terminal.getImage();
        Image imgScalet = imgt.getScaledInstance(14, 14, Image.SCALE_SMOOTH);
        ImageIcon scaledIcont = new ImageIcon(imgScalet);
        terminalt.setIcon(scaledIcont);
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
        terminalt = new javax.swing.JLabel();
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
        chatTicket = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textChat = new javax.swing.JTextArea();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        respChat = new javax.swing.JTextArea();
        tecnicoDetail = new javax.swing.JLabel();
        tituloDetail = new javax.swing.JLabel();

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
        jLabel2.setText("Cliente Help Desk");

        javax.swing.GroupLayout BarLayout = new javax.swing.GroupLayout(Bar);
        Bar.setLayout(BarLayout);
        BarLayout.setHorizontalGroup(
            BarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BarLayout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addComponent(terminalt, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 596, Short.MAX_VALUE)
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
                .addGroup(BarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(terminalt, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(BarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMin, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                        .addComponent(btnMax, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)))
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
                            .addComponent(dataAbertura, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, detailTicketLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)))
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
                .addContainerGap(68, Short.MAX_VALUE))
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

        textChat.setColumns(20);
        textChat.setRows(5);
        jScrollPane1.setViewportView(textChat);
        textChat.setEditable(false);
        Color corFundox = new Color(255,255,255); // Vermelho
        Bar.setBackground(corFundox);

        respChat.setColumns(20);
        respChat.setRows(5);
        jScrollPane2.setViewportView(respChat);
        respChat.setEditable(false);

        tecnicoDetail.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        tecnicoDetail.setText("Não atribuido");

        tituloDetail.setFont(new java.awt.Font("Segoe UI", 3, 14)); // NOI18N
        tituloDetail.setText("Titulo");

        javax.swing.GroupLayout chatTicketLayout = new javax.swing.GroupLayout(chatTicket);
        chatTicket.setLayout(chatTicketLayout);
        chatTicketLayout.setHorizontalGroup(
            chatTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chatTicketLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(chatTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(chatTicketLayout.createSequentialGroup()
                        .addGroup(chatTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tecnicoDetail))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, chatTicketLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(chatTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, chatTicketLayout.createSequentialGroup()
                                .addComponent(tituloDetail)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel12))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        chatTicketLayout.setVerticalGroup(
            chatTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(chatTicketLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(chatTicketLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(chatTicketLayout.createSequentialGroup()
                        .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, chatTicketLayout.createSequentialGroup()
                        .addComponent(tituloDetail)
                        .addGap(0, 0, 0)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(tecnicoDetail)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout detailLayout = new javax.swing.GroupLayout(detail);
        detail.setLayout(detailLayout);
        detailLayout.setHorizontalGroup(
            detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, detailLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(chatTicket, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(detailTicket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        detailLayout.setVerticalGroup(
            detailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(detailTicket, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(chatTicket, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

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
    private javax.swing.JPanel chatTicket;
    private javax.swing.JTextField dataAbertura;
    private javax.swing.JPanel detail;
    private javax.swing.JPanel detailTicket;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea respChat;
    private javax.swing.JTextField statusChamado;
    private javax.swing.JLabel tecnicoDetail;
    private javax.swing.JTextField tecnicoResponsavel;
    private javax.swing.JTextField tempoAtendimento;
    private javax.swing.JTextField tempoSolucao;
    private javax.swing.JLabel terminalt;
    private javax.swing.JTextArea textChat;
    private javax.swing.JLabel tituloDetail;
    private javax.swing.JTextField ultimaAtualizacao;
    private javax.swing.JTextField urgenciaChamado;
    // End of variables declaration//GEN-END:variables
}
