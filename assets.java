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
    String urlImagem;

    public Receita(String nome, String urlImagem){
        this.nome = nome;
        this.urlImagem = urlImagem;
        this.ingredientes = new ArrayList<>();
    }
    //Funçaõ p/ adicionar os ingredientes
    public void addingrediente(String ingrediente){
        this.ingredientes.add(ingrediente.trim().toLowerCase());
    }
}

public class assets {

    static ArrayList<Receita> livroDeReceitas = new ArrayList<>();


    public static void main(String[] args) throws IOException{
                        
        //Cadastro de receitas:
        Receita A1 = new Receita("Omelete", "https//unsplash.com");
        A1.addingrediente("ovos");
        A1.addingrediente("sal");
        A1.addingrediente("queijo");
        livroDeReceitas.add(A1);

        Receita A2 = new Receita("Macarrão À Bolonhesa", "https//unsplash.com");
        A2.addingrediente("macarrao");
        A2.addingrediente("alho");
        A2.addingrediente("azeite");
        A2.addingrediente("molho de tomate");
        A2.addingrediente("sal");
        A2.addingrediente("carne moida");
        livroDeReceitas.add(A2);

        Receita A3 = new Receita("Salada de Atum", "https//unsplash.com");
        A3.addingrediente("atum");
        A3.addingrediente("tomate");
        A3.addingrediente("sal");
        livroDeReceitas.add(A3);

        //Servidor HTTP nativo na porta 8080
        HttpServer servidor = HttpServer.create(new InetSocketAddress(8080), 0);
        
        //Rota de busca para receitas
        servidor.createContext("/buscar", new BuscarHandler());

        //Rota para exibir as sugestões enviando para exibirReceitas

        servidor.createContext("/sugestoes", new SugestoesHandler());

        //Rota para receber e salvar nova receita

        servidor.createContext("/cadastrar", new CadastrarHandler());

        servidor.setExecutor(null);
        System.out.println("Servidor Java Ativo! Porta 8080 conectada");
        //ponto de partida
        servidor.start();
    }

    //Extração de parâmetros da URL
    static Map<String, List<String>> lerParametros(String query) throws IOException{
        Map<String, List<String>> mapa = new HashMap<>();
        if(query != null){
            for(String par:query.split("&")){
                String[] kv = par.split("=");
                if(kv.length > 1){
                    String chave = URLDecoder.decode(kv[0], StandardCharsets.UTF_8.name());
                    String valor = URLDecoder.decode(kv[1], StandardCharsets.UTF_8.name()).toLowerCase().trim();
                    mapa.computeIfAbsent(chave, k -> new ArrayList<>()).add(valor);
                }
            }
        }
        return mapa;
    }

    //Envio de resposta HTML e limpeza do navegador
    static void enviarResposta(HttpExchange exchange, String html) throws IOException{
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        try(OutputStream os = exchange.getResponseBody()){
            os.write(bytes);
        }
    }

    //Cadastro de usuário
    static class UsuarioHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) throws IOException{
            Map<String, List<String>> params = lerParametros(exchange.getRequestURI().getQuery());
            if(params.containsKey("nome") && params.containsKey("email")){
                usuarioCadastrado.put("nome", params.get("nome").get(0));
                usuarioCadastrado.get("email", params.get("email").get(0));
                System.out.println("Conexão completa, usuário " + usuarioCadastrado.get("nome") + " definido");
                enviarResposta(exchange, "Bem-vindo", + usuárioCadastrado.get("nome") + "!");
            }
            else{
                enviarResposta(exchange, "Erro ao processar cadastro.");
            }
        }
    }

    //Imagens nas receitas
    static class SugestõesHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) throws IOException{
            StringBuilder html = new StringBuilder();
            for(Receita v : livroDeReceitas){
                html.append("<div class='cardReceita'>")
                    .append("<img src='").append(v.urlImagem).append("' alt='Imagem da receita' class='imgReceita'>")
                    .append("<h3>").append(v.nome).append("</h3>")
                    .append("<p><b>Ingredientes:</b>").append(String.join(", ", v.ingredientes)).append("</p>")
                    .append("</div>");
            }
            enviarResposta(exchange, html.toString());
        }
    }
    
    //Rota 1: Lista de receitas cadastradas como sugestão
    static class SugestoesHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) throws IOException{

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");

            StringBuilder html = new StringBuilder();
            for(Receita v: livroDeReceitas){
                html.append("<div class='cardReceita'>")
                    .append("<h3>").append(v.nome).append("</h3>")
                    .append("<p><b>Ingredientes:</b>").append(String.join(", ", v.ingredientes)).append("</p>")
                    .append("</div>");
            }
            enviarResposta(exchange, html.toString());
        }
    }

    //Rota 2: Cadastro de nova receita
    static class CadastrarHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) throws IOException{
            Map<String, List<String>> params = lerParametros(exchange.getRequestURI().getQuery());
            if(params.containsKey("nome") && params.containsKey("ingredientes")){
                String nomeBruto = params.get("nome").get(0);
                String nomeFormatado = nomeBruto.substring(0, 1).toUpperCase() + nomeBruto.substring(1);

                String img =params.containsKey("imagem") && !params.get("imagem").get(0).isEmpty()
                    ? params.get("imagem").get(0)
                    : "https://unsplash.com";
                
                Receita nova = new Receita(nomeFormatado, img);
                for(String ing : params.get("ingredientes").get(0).split(",")){
                    if(!ing.trim().isEmpty()){
                        nova.addingrediente(ing != null ? ing : ing);
                    }
                }
                livroDeReceitas.add(nova);
                enviarResposta(exchange, "Receita cadastrada com sucesso!");
            }
            else{
                enviarResposta(exchange, "Dados incompletos");
            }
            
        }
    }

    //Rota 3: COmparação de receitas cadastradas com dados inseridos
    static class BuscarHandler implements HttpHandler{
        @Override
        public void handle(HttpExchange exchange) throws IOException{

            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");

            Map<String, List<String>> params = lerParametros(exchange.getRequestURI().getQuery());
            List<String> ingredientesUsuario = params.getOrDefault("ingredientes", new ArrayList<>());

            StringBuilder html = new StringBuilder();
            boolean encontrouAlguma = false;
            
            for(Receita receita: livroDeReceitas){
                int encontrados = 0;
                for(String ing : receita.ingredientes){
                    if(ingredientesUsuario.contains(ing)){
                        encontrados++;
                    }
                }
                if(encontrados == receita.ingredientes.size()){
                    html.append("<div class='cardReceita sucesso'>")
                        .append("<h3>").append(receita.nome).append("</h3>")
                        .append("<p>Você tem tosdos os ingredientes necessários!</p>")
                        .append("</div>");
                    encontrouAlguma = true;
                }
            }
            if(!encontrouAlguma){
                html.append("<p class = 'avisoVazio'> Nenhuma receita encontrada com os ingredintes que você selecionados. Tente marcar mais opções.</p>");
            }

            enviarResposta(exchange, html.toString());
        }
    }
}    