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
            "1. Bata os ovos com o sal em uma tigela. 2. Despeje em uma frigideira untada em fogo baixo. 3. Adicione o queijo, dobre ao meio e sirva."
        );
        A1.addingrediente("ovos");
        A1.addingrediente("sal");
        A1.addingrediente("queijo");
        livroDeReceitas.add(A1);

        Receita A2 = new Receita(
            "Macarrão À Bolonhesa",
            "macarraobolonhesa.jpg",
            "500g de macarrão, 2 dentes de alho, 2 colheres de azeite, 1 sachê de molho de tomate, sal a gosto, 500g de carne moída, 1 cebola picada",
            "1. Cozinhe o macarrão em água e sal. 2. Refogue o alho no azeite e frite a carne moída. 3. Adicione o molho de tomate, deixe apurar por 5 minutos e misture ao macarrão."
        );
        A2.addingrediente("macarrao");
        A2.addingrediente("alho");
        A2.addingrediente("azeite");
        A2.addingrediente("molho de tomate");
        A2.addingrediente("sal");
        A2.addingrediente("carne moida");
        A2.addingrediente("cebola");
        livroDeReceitas.add(A2);

        Receita A3 = new Receita(
            "Salada de Atum",
            "saladaatum.jpg",
            "1 lata de atum sólido, 2 tomates picados, azeitonas picadas, 1/3 de pimentão vermelho, 1/3 de pimentão verde, 2 colheres de maionese,1 tomate, folhas de alface, 1 pitada de sal, castanhas de caju picadas",
            "1. Escorra o óleo ou água da lata de atum. 2. Coloque o atum com os tomates picados, os pimentões e as azeitonas em uma tigela, e adicione duas colheres de maionese e misture bem. 3. Tempere com sal a gosto. 4. Em um prato, coloque as folhas de alface e adicione a mistura de atum, por cima salpique as castanhas de caju picadas. 5. Sirva gelado."
        );
        A3.addingrediente("atum");
        A3.addingrediente("tomate");
        A3.addingrediente("sal");
        A3.addingrediente("azeitona");
        A3.addingrediente("pimentaovermelho");
        A3.addingrediente("pimentaoverde");
        A3.addingrediente("maionese");
        A3.addingrediente("alface");
        A3.addingrediente("castanhacaju");
        livroDeReceitas.add(A3);

        Receita A4 = new Receita(
            "Fricassê de Frango",
            "fricasse.jpg",
            "1 lata de creme de leite, 1 lata de milho verde, 1 copo de requeijão, 100g de azeitona sem caroço, 2 peitos de frango, 200g de mussarela fatiada, 100g de batata palha, 1 xícara de água, sal a gosto",
            "1. Bata no liquidificador o milho, o requeijão, o creme de leite e a água. 2. Refogue o creme do liquidificador com o frango desfiado, as azeitonas e o sal até ficar com uma textura espessa. 3.Coloque o refogado numa assadeira, cubra com mussarela e espalhe a batata palha por cima. 4.Leve ao forno até borbulhar. 5.Sirva com arroz branco."
        );
        A4.addingrediente("frango");
        A4.addingrediente("cremeleite");
        A4.addingrediente("sal");
        A4.addingrediente("requeijao");
        A4.addingrediente("batata palha");
        A4.addingrediente("milho verde");
        A4.addingrediente("azeitona");
        A4.addingrediente("mussarela");
        livroDeReceitas.add(A4);

        Receita A5 = new Receita(
            "Pudim de Leite Condensado",
            "pudim.jpg",
            "Pudim: 1 lata de leite condensado, 1 lata de leite (medida da lata de leite condensado), 3 ovos. Calda: 1 xícara de açúcar, 1/2 xícara de água",
            "1. Para a calda, derreta o açúcar em fogo baixo até formar um caramelo. 2. Adicione a água e cozinhe até dissolver o caramelo. 3. Despeje a calda em uma forma de pudim e reserve. 4. No liquidificador, bata o leite condensado, o leite e os ovos até obter uma mistura homogênea. 5. Despeje a mistura na forma caramelizada. 6. Leve ao forno em banho-maria por aproximadamente 1 hora ou até firmar. 7. Deixe esfriar, desenforme e sirva gelado."
        );
        A5.addingrediente("leitecondensado");
        A5.addingrediente("leite");
        A5.addingrediente("ovos");
        A5.addingrediente("açucar");
        livroDeReceitas.add(A5);

        Receita A6 = new Receita(
            "Panqueca de Carne Moída",
            "panqueca.jpg",
            "1 xícara de farinha de trigo, 1 xícara de leite, 1 ovo, 1 pitada de sal, 500g de carne moída, 1 cebola picada, 2 dentes de alho picados, 1 sachê de molho de tomate, queijo ralado a gosto",
            "1. Misture a farinha, o leite, o ovo e o sal até formar uma massa homogênea. 2. Cozinhe a massa em uma frigideira untada até dourar dos dois lados. 3. Refogue a carne moída com a cebola e o alho, adicione o molho de tomate e cozinhe por alguns minutos. 4. Recheie as panquecas com a carne moída e enrole-as. 5. Coloque as panquecas em uma assadeira, cubra com queijo ralado e leve ao forno para gratinar."
        );
        A6.addingrediente("farinha");
        A6.addingrediente("leite");
        A6.addingrediente("ovos");
        A6.addingrediente("sal");
        A6.addingrediente("carne moida");
        A6.addingrediente("cebola");
        A6.addingrediente("alho");
        A6.addingrediente("molho de tomate");
        A6.addingrediente("queijo");
        livroDeReceitas.add(A6);

        HttpServer servidor = HttpServer.create(new InetSocketAddress(8080), 0);
        //Criação das funções/rotas do servidor
        servidor.createContext("/buscar", new BuscarHandler()); //busca de receitas por ingredientes
        servidor.createContext("/sugestoes", new SugestoesHandler()); //exibição de todas as receitas cadastradas
        servidor.createContext("/cadastrar", new CadastrarHandler()); //cadastro de nova receita
        servidor.createContext("/usuario", new UsuarioHandler()); //cadastro de usuário

        servidor.setExecutor(null);
        System.out.println("Servidor Java Ativo! Porta 8080 conectada com sucesso.");
        servidor.start();
    }

    //Processador da lógica do navegador
    static Map<String, List<String>> lerParametros(String query) throws IOException {
        Map<String, List<String>> mapa = new HashMap<>();
        if (query != null) {
            for (String par : query.split("&")) {
                String[] kv = par.split("=", 2);
                if (kv.length > 1) {
                    String chave = URLDecoder.decode(kv[0], StandardCharsets.UTF_8.name());
                    String valor = URLDecoder.decode(kv[1], StandardCharsets.UTF_8.name()).trim();
                    mapa.computeIfAbsent(chave, k -> new ArrayList<>()).add(valor);
                }
            }
        }
        return mapa;
    }

    // Função para enviar e garantir que o navegador leia e interprete a lógica processada pelo Java
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

    // Função auxiliar para gerar cartão HTML
    //Cria condição para a receita: r=receita, valor booleano.
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
    //Cadastro do usuário
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

    //Função para exibir as sugestões
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

    //Função de cadastro de novas receitas
    static class CadastrarHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, List<String>> params = lerParametros(exchange.getRequestURI().getQuery());
            if (params.containsKey("nome") && params.containsKey("ingredientes")) {
                String nomeBruto = params.get("nome").get(0); //Nome como o usuário digitou
                String nomeFormatado = nomeBruto.substring(0, 1).toUpperCase() + nomeBruto.substring(1);
                
                //Recebe imagem e adiciona uma padrão caso não seja fornecida
                String img = params.containsKey("imagem") && !params.get("imagem").get(0).isEmpty() 
                    ? params.get("imagem").get(0) 
                    : "padrao.jpg";

                //Recebe a quantidade de ingredientes
                String detalhes = params.containsKey("detalhes") && !params.get("detalhes").get(0).isEmpty()
                    ? params.get("detalhes").get(0)
                    : params.get("ingredientes").get(0);

                //Recebe o modo de preparo
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
                enviarResposta(exchange, "Receita '" + nomeFormatado + "' foi cadastrada com sucesso!");
            }
            else {
                enviarResposta(exchange, "Dados incompletos.");
            }
        }
    }

    //Função de busca de receitas por ingredientes
    static class BuscarHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, List<String>> params = lerParametros(exchange.getRequestURI().getQuery());
            List<String> ingredientesUsuario = new ArrayList<>();
            
                for (String item : params.getOrDefault("ingredientes", new ArrayList<>())) {
                ingredientesUsuario.add(item.toLowerCase()); // Garante que os ingredientes buscados fiquem em minúsculo
            }

        // Se não houver ingredientes fornecidos, exibe todas as receitas
        if(ingredientesUsuario.isEmpty()){
            StringBuilder htmlTodas = new StringBuilder();
            for (Receita v : livroDeReceitas) {
                htmlTodas.append(gerarCardHtml(v, false));
            }
            enviarResposta(exchange, htmlTodas.toString());
            return;
        }

        StringBuilder html = new StringBuilder();
        boolean encontrouAlguma = false;

        for(Receita receita : livroDeReceitas) {
            int encontrados = 0;

            for(String ing: receita.ingredientesBusca){
                if(ingredientesUsuario.contains(ing)){
                encontrados++;
                }
            }
        

            if(encontrados > 0){
                boolean temTudo = ingredientesUsuario.containsAll(receita.ingredientesBusca);
                    html.append(gerarCardHtml(receita, temTudo));
                    encontrouAlguma = true;
            }
        }
        if (!encontrouAlguma) {
            html.append("<p class='avisoVazio'>Nenhuma receita encontrada com os ingredientes selecionados, tente outros ingredientes.</p>");
        }

        enviarResposta(exchange, html.toString());

        }
    }   
}