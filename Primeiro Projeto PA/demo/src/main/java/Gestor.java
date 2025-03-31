import java.util.Scanner;

/**
 * Classe Gestor representa o menu de Gestor e as diversas ações que podem ser tomadas pelo Gestor.
 * 
 * @author Guilherme Rodrigues
 * 
 * @param Scanner scanner, serve para ler o input do utilizador
 * @param boolean running, serve para controlar o loop do menu
 */
public class Gestor 
{

    Scanner scanner = new Scanner(System.in);
    boolean running = true;

    /**
     * Menu de ações do Gestor
     * 
     * @author Guilherme Rodrigues
     */
    public void menu_Gestor(int num_notificacoes, String utilizador)
    {

        
        while (running) {
            System.out.println("\n=== Menu de Registo ===");
            System.out.println("1. Registar Gestor");
            System.out.println("2. Remover Utilizador");
            System.out.println("3. Pesquisar Utilizador por username");
            System.out.println("4. Listar todos os Utilizadores por tipo");
            System.out.println("5. Listar todos os Utilizadores");
            System.out.println("6. Alterar Informacoes de um Utilizador");
            System.out.println("7. Inserir Licença");
            System.out.println("8. Aceitar Pedido de Certificação");
            System.out.println("9. Listar todas as Certificacoes");
            System.out.println("10. Pesquisar por Certificação");
            System.out.println("11. Pesquisar por Equipamento");
            System.out.println("12. Arquivar uma Certificação");
            System.out.println("13. Voce possui " + num_notificacoes + " notificacoes");
            System.out.println("14. Sair");
            System.out.print("Escolha uma opção: ");
            
            int option = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha
            
            switch (option) 
            {
                case 1:
                    Conexao_BD.registaGestor();
                break;
                case 2:
                    System.out.print("Digite o ID do utilizador: ");
                    int id_utilizador = scanner.nextInt();
                
                    Conexao_BD.removerGestor(id_utilizador);
                break;
                case 3:
                    System.out.print("Insira o username: ");
                    String username = scanner.nextLine();

                    Conexao_BD.listarPorNome(username);
                break;
                case 4:
                    System.out.print("Digite o tipo de utilizador: ");
                    String tipo = scanner.nextLine();

                    Conexao_BD.listarPorTipo(tipo);
                break;
                case 5:
                    Conexao_BD.listarClientes();
                break;
                case 6:
                    System.out.print("Digite o ID do utilizador: ");
                    id_utilizador = scanner.nextInt();
                
                    Conexao_BD.menuAtualizarUtilizador(id_utilizador);
                    break;
                case 7:
                    Conexao_BD.inserirLicenca();
                    break;
                case 8:
                    Conexao_BD.aceitarPedidoCertificacao();
                    break;
                case 9:
                    String tipo1 = "Gestor";
                    Conexao_BD.listarCertificacao(tipo1);
                    break;
                case 10:
                    Conexao_BD.pesquiarCertificacao();
                    break; 
                case 11:
                    String tipo2 = "Gestor";
                    Conexao_BD.pesquisarEquipamento(tipo2, 0);
                    break;
                case 12:
                    System.out.print("Insira o ID da certificação: ");
                    int id_certificacao = scanner.nextInt();
                    Conexao_BD.arquivarCertificacao(id_certificacao);
                    break;
                case 13:
                    Conexao_BD.listarNotificacoes();
                    num_notificacoes = 0;
                    break;
                case 14:
                    System.out.println("Adeus " + utilizador + " !");
                    running = false;
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}
    