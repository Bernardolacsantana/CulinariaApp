
import java.util.*;

class Receitas{
    String nome;
    ArrayList<String> ingredientes;

    public Receita(String nome){
        this.nome = nome;
        this.ingredientes = new ArrayList<>();
    }
    //Funçaõ p/ adicionar os ingredientes
    public void addingrediente(String ingrediente){
        this.ingredientes.add(ingrediente.to.LoweCase());
    }
}

public class assets {

    static ArrayList<String> cardapio = new ArrayList<>();
    static ArrayList<Receita> livroDeReceitas = new ArrayList<>();


    public static void main(String[] args){
        Scanner Null = new Scanner(System.in);
        
        //Cadastro de receitas:
        Receita A1 = new Receita("Omelete");
        A1.addingrediente("Ovo");
        A1.addingrediente("Sal");
        A1.addingrediente("qeuijo");
        livroDeReceitas.add(A1);

        Receita A2 = new Receita("Macarrão A Bolhonhesa");
        A2.addingrediente("Macarrão");
        A2.addingrediente("Alho");
        A2.addingrediente("Azeite");
        A2.addingrediente("Molho de Tomate");
        A2.addingrediente("Sal");
        A2.addingrediente("Carne Moida");
        livroDeReceitas.add(A2);

    }
    // Comparação de receitas

    
}


