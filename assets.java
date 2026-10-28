
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


    public static void main(String[] args){
            Scanner leitor = new Scanner(System.in);
            
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

            //Inicio do cadastro: O que o usuário já tem
            System.out.println("Vamos iniciar?");
            System.out.println("=== Insira os ingredientes da sua Geladeira ===");
            System.out.println("Digite o nome do ingrediente ou digite fim para encerrar");

            while (true){
                System.out.println("Ingrediente: ");
                String ingredienteAdicionado = leitor.nextLine().toLowerCase().trim();
                if(ingredienteAdicionado.equals("fim")){
                    break;
                }
                if(!cardapio.contains(ingredienteAdicionado)){
                cardapio.add(ingredienteAdicionado);
                }
                else{
                    System.out.println("(Esse ingrediente já foi adicionado1)");
                }
            }
            


            // Algoritmo de comparação

        System.out.println("\n=== Receitas possiveis de serem feitas ===");
        boolean encontrouAlguma = false;

        // Mecanismo de busca
        
        for(Receita receita: livroDeReceitas){
            int ingredientesEncontrados = 0;

            for (String ingredienteNecessario: receita.ingredientes){
                if(cardapio.contains(ingredienteNecessario)){
                    ingredientesEncontrados++;
                }
            }
            if(ingredientesEncontrados == receita.ingredientes.size()){
            System.out.println("[SUCESSO] Você pode fazer: " + receita.nome);
            encontrouAlguma = true;
            }
        
        }

        if (!encontrouAlguma){
            System.out.println("Poxa, você não temingredientes suficientes para nenhuma receita cadastrada.");
        }

        leitor.close();
    }
      
}


