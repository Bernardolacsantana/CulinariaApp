//Importações necessárias para o funcionamento do servidor HTTP Java local
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

// Classe Receita para armazenar informações sobre cada receita
class Receita {
    String nome;
    ArrayList<String> ingredientesBusca;
    String ingredientesDetalhados;
    String modoPreparo;
    String urlImagem;

    public Receita(String nome, String urlImagem, String ingredientesDetalhados, String modoPreparo) {
        this.nome = nome;
        this.urlImagem = urlImagem;
        this.ingredientesDetalhados = ingredientesDetalhados;
        this.modoPreparo = modoPreparo;
        this.ingredientesBusca = new ArrayList<>();
    }

    public void addingrediente(String ingrediente) {
        this.ingredientesBusca.add(ingrediente.toLowerCase().trim());
    }
}

public class assets {
    static ArrayList<Receita> livroDeReceitas = new ArrayList<>();
    static Map<String, String> usuarioCadastrado = new HashMap<>();

    public static void main(String[] args) throws IOException {

        // Cadastro de receitas padrão
        Receita A1 = new Receita(
            "Omelete Simples",
            "omelete.jpg",
            "3 ovos, 1 pitada de sal, 50g de queijo ralado",
            "/n1. Bata os ovos com o sal em uma tigela. 2. Despeje em uma frigideira untada em fogo baixo. 3. Adicione o queijo, dobre ao meio e sirva."
        );
        A1.addingrediente("ovos");
        A1.addingrediente("sal");
        A1.addingrediente("queijo");
        livroDeReceitas.add(A1);

        Receita A2 = new Receita(
            "Macarrão À Bolonhesa",
            "macarrao.jpg",
            "250g de macarrão, 2 dentes de alho, 2 colheres de azeite, 1 sachê de molho de tomate, sal a gosto, 300g de carne moída",
            "1. Cozinhe o macarrão em água e sal. 2. Refogue o alho no azeite e frite a carne moída. 3. Adicione o molho de tomate, deixe apurar por 5 minutos e misture ao macarrão."
        );
        A2.addingrediente("macarrao");
        A2.addingrediente("alho");
        A2.addingrediente("azeite");
        A2.addingrediente("molho de tomate");
        A2.addingrediente("sal");
        A2.addingrediente("carne moida");
        livroDeReceitas.add(A2);

        Receita A3 = new Receita(
            "Salada de Atum",
            "atum.jpg",
            "1 lata de atum sólido, 2 tomates picados, 1 pitada de sal",
            "1. Escorra o óleo ou água da lata de atum. 2. Misture o atum com os tomates picados em uma tigela. 3. Tempere com sal e sirva gelado."
        );
        A3.addingrediente("atum");
        A3.addingrediente("tomate");
        A3.addingrediente("sal");
        livroDeReceitas.add(A3);

        HttpServer servidor = HttpServer.create(new InetSocketAddress(8080), 0);

        servidor.createContext("/buscar", new BuscarHandler());
        servidor.createContext("/sugestoes", new SugestoesHandler());
        servidor.createContext("/cadastrar", new CadastrarHandler());
        servidor.createContext("/usuario", new UsuarioHandler());

        servidor.setExecutor(null);
        System.out.println("Servidor Java Ativo! Porta 8080 conectada com sucesso.");
        servidor.start();
    }

    static Map<String, List<String>> lerParametros(String query) throws IOException {
        Map<String, List<String>> mapa = new HashMap<>();
        if (query != null) {
            for (String par : query.split("&")) {
                String[] kv = par.split("=");
                if (kv.length > 1) {
                    String chave = URLDecoder.decode(kv[0], StandardCharsets.UTF_8.name());
                    String valor = URLDecoder.decode(kv[1], StandardCharsets.UTF_8.name()).trim();
                    mapa.computeIfAbsent(chave, k -> new ArrayList<>()).add(valor);
                }
            }
        }
        return mapa;
    }

    static void enviarResposta(HttpExchange exchange, String html) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Content-Type", "text/html; charset=UTF-8");
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    // Função auxiliar gerar cartão HTML
    static String gerarCardHtml(Receita r, boolean isSucesso) {
        String classeExtra = isSucesso ? " sucesso" : "";
        String icone = isSucesso ? "✅ " : "📖 ";
        
        return "<div class='cardReceita" + classeExtra + "'>" +
               "<img src='" + r.urlImagem + "' alt='Foto da receita' class='imgReceita'>" +
               "<h3>" + icone + r.nome + "</h3>" +
               "<p class='detalheIng'><b>🛒 Medidas:</b> " + r.ingredientesDetalhados + "</p>" +
               "<div class='boxPreparo'><b>👨‍🍳 Modo de Preparo:</b><br>" + r.modoPreparo + "</div>" +
               "</div>";
    }

    static class UsuarioHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, List<String>> params = lerParametros(exchange.getRequestURI().getQuery());
            if (params.containsKey("nome") && params.containsKey("email")) {
                usuarioCadastrado.put("nome", params.get("nome").get(0));
                usuarioCadastrado.put("email", params.get("email").get(0));
                enviarResposta(exchange, "Bem-vindo, " + usuarioCadastrado.get("nome") + "!");
            } else {
                enviarResposta(exchange, "Erro ao processar cadastro.");
            }
        }
    }

    static class SugestoesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            StringBuilder html = new StringBuilder();
            for (Receita v : livroDeReceitas) {
                html.append(gerarCardHtml(v, false));
            }
            enviarResposta(exchange, html.toString());
        }
    }

    static class CadastrarHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, List<String>> params = lerParametros(exchange.getRequestURI().getQuery());
            if (params.containsKey("nome") && params.containsKey("ingredientes")) {
                String nomeBruto = params.get("nome").get(0);
                String nomeFormatado = nomeBruto.substring(0, 1).toUpperCase() + nomeBruto.substring(1);
                
                String img = params.containsKey("imagem") && !params.get("imagem").get(0).isEmpty() 
                    ? params.get("imagem").get(0) 
                    : "padrao.jpg";

                String detalhes = params.containsKey("detalhes") && !params.get("detalhes").get(0).isEmpty()
                    ? params.get("detalhes").get(0)
                    : params.get("ingredientes").get(0);

                String preparo = params.containsKey("preparo") && !params.get("preparo").get(0).isEmpty()
                    ? params.get("preparo").get(0)
                    : "Misture os ingredientes e prepare a gosto.";

                Receita nova = new Receita(nomeFormatado, img, detalhes, preparo);
                
                for (String ing : params.get("ingredientes").get(0).split(",")) {
                    if (!ing.trim().isEmpty()) {
                        nova.addingrediente(ing);
                    }
                }
                livroDeReceitas.add(nova);
                enviarResposta(exchange, "Receita '" + nomeFormatado + "' cadastrada com sucesso!");
            } else {
                enviarResposta(exchange, "Dados incompletos.");
            }
        }
    }

    static class BuscarHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, List<String>> params = lerParametros(exchange.getRequestURI().getQuery());
            List<String> ingredientesUsuario = new ArrayList<>();
            
            // Garante que os ingredientes buscados fiquem em minúsculo
            for (String item : params.getOrDefault("ingredientes", new ArrayList<>())) {
                ingredientesUsuario.add(item.toLowerCase());
            }

            StringBuilder html = new StringBuilder();
            boolean encontrouAlguma = false;

            for (Receita receita : livroDeReceitas) {
                int encontrados = 0;
                for (String ing : receita.ingredientesBusca) {
                    if (ingredientesUsuario.contains(ing)) {
                        encontrados++;
                    }
                }

                if (encontrados == receita.ingredientesBusca.size()) {
                    html.append(gerarCardHtml(receita, true));
                    encontrouAlguma = true;
                }
            }

            if (!encontrouAlguma) {
                html.append("<p class='avisoVazio'>Nenhuma receita encontrada com esses ingredientes.</p>");
            }

            enviarResposta(exchange, html.toString());
        }
    }
}