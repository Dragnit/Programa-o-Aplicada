import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) 
    {
        Conexao_BD bd = new Conexao_BD();
        try 
        {
            Connection conn = bd.conexao(); 

            bd.primeiroRegisto();    
            menu_Entrada();
            conn.close(); 
        } 
        catch (SQLException e) 
        {
            e.printStackTrace(); // Exibe detalhes do erro no terminal
        }
    }
    
    public static void menu_Entrada()
    {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        while (running) 
        {
            System.out.println("\n=== Menu de Login e Registo ===");
            System.out.println("1. Login");
            System.out.println("2. Registo");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");
            
            int option = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha
            
            switch (option) 
            {
                case 1:
                    System.out.print("Digite o username: ");
                    String username = scanner.nextLine();
                    System.out.print("Digite a senha: ");
                    String password = scanner.nextLine();
                    Conexao_BD conexao = new Conexao_BD(); 
                    conexao.verificarLogin(username, password);;
                    break;
                case 2:
                    System.out.println("Escolha o tipo de utilizador: ");
                    System.out.println("1. Tecnico");
                    System.out.println("2. Fabricante");
                    int option2 = scanner.nextInt();
                    if (option2 == 1)
                    {
                        Conexao_BD conexao2 = new Conexao_BD();
                        conexao2.registaTecnico();
                    }
                    else
                    {
                        Conexao_BD conexao3 = new Conexao_BD();
                        conexao3.registaFabricante();
                    }
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
                break;
            }
        }
    }
}