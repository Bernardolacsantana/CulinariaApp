//Importações necessárias para criar um servidor local
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;

import java.net.InetSocketAddress;
import java.net.URLDecoder;

import java.nio.charset.StandardCharsets;

import java.util.*;

class Receita{
    String nome;
    ArrayList<String> ingredientes;

    public Receita(String nome){
        this.nome = nome;
        this.ingredientes = new ArrayList<>();
    }
    //Funçaõ p/ adicionar os ingredientes
    public void addingrediente(String ingrediente){
        this.ingredientes.add(ingrediente.toLowerCase());
    }
}

public class assets {

    static ArrayList<String> cardapio = new ArrayList<>();
    static ArrayList<Receita> livroDeReceitas = new ArrayList<>();


    public static void main(String[] args) throws IOException{
                        
            //Cadastro de receitas:
        Receita A1 = new Receita("Omelete");
        A1.addingrediente("ovo");
        A1.addingrediente("sal");
        A1.addingrediente("queijo");
        livroDeReceitas.add(A1);

        Receita A2 = new Receita("Macarrão À Bolonhesa");
        A2.addingrediente("macarrao");
        A2.addingrediente("alho");
        A2.addingrediente("azeite");
        A2.addingrediente("molho de tomate");
        A2.addingrediente("sal");
        A2.addingrediente("carne moida");
        livroDeReceitas.add(A2);

        //Servidor HTTP nativo na porta 8080
        HttpServer servidor = HttpServer.create(new InetSocketAddress(8080), 0);
        
        servidor.createContext("/busca", new BuscarHandler());

        servidor.setExecutor(null);
        System.out.println("Servidor Java Ativo! Porta 8080 conectada");
        //ponto de partida
        servidor.start();

    }

    static class BuscarHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) throws IOException{
            //Responsável pelo acesso
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin","*");
            
            String query = exchange.getRequestURI().getQuery();
            List<String> ingredientesUsuario = new ArrayList<>();

            if (query != null){
                String[] pares = query.split("&");
                for (String par:pares){
                    String[] chaveValor = par.split("=");
                    if(chaveValor.length > 1 && chaveValor [0].equals("ingredientes")){
                        String ingrediente = URLDecoder.decode(chaveValor[1], StandardCharsets.UTF_8.toString()).toLowerCase();
                        ingredientesUsuario.add(ingrediente);
                    }
                }
            }
            //Comparador
            StringBuilder respostaHtml = new StringBuilder();
            respostaHtml.append("<html><head><meta charset='UTF-8></head><body style='font-family:Arial; padding: 20px; backgrond-color: #f7ffd4;'>");
            respostaHtml.append("<h2 style='color: rgb(119, 4, 4);'>Direto do Forno - Resultado da Busca</h2");
            respostaHtml.append("<p>Com os ingredientes selecionados: <b>").append(ingredientesUsuario).append("</b></p>");

            boolean encontrouAlguma = false;

            for(Receita receita: livroDeReceitas){
                int ingredientesEncontrados = 0;

                for (String ingredienteNecessario : receita.ingredientes){
                    if (ingredientesUsuario.contains(ingredienteNecessario)) {
                        ingredientesEncontrados++;
                    }
                }
                if (ingredientesEncontrados == receita.ingredientes.size()){
                respostaHtml.append("<li style='font-size:18px; color: green; margin-bottom: 8px;<b> Receita encontrada</b> Pode fazer").append(receita.nome).append("</li>");
                encontrouAlguma = true;
                }
            }
            

            if (!encontrouAlguma){
                respostaHtml.append("<li style='font-size: 16px; color: #555'>Poxa, você tem ingredientes suficientes para nenhuma receita cadastrada.</li>");
            }
            respostaHtml.append("</ul><br><a href ='javascript:history.back()' style='background: rgb(2, 32, 10); color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;'>Voltar para a Denspensa</a></body></html>");

            byte[] bytesResposta = respostaHtml.toString().getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(200, bytesResposta.length);
            OutputStream os = exchange.getResponseBody();
            os.write(bytesResposta);
            os.close();
        } 
        
    }
      
}


