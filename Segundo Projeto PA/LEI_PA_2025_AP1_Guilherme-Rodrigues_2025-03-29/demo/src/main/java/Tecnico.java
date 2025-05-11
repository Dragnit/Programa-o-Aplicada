import java.util.Scanner;

public class Tecnico 
{
    public static void menu_Tecnico(String username_utilizador, int id_utilizador, String tipo)
    {
        int num_notificacoes = Conexao_BD.notificacoes(tipo, id_utilizador);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
    
        while (running) {
            System.out.println("\n=== Menu de Tecnico ===");
            System.out.println("1. Visualizar informacao");
            System.out.println("2. Alterar informacao");
            System.out.println("3. Pedidos de Certificação");
            System.out.println("4. Listar Pedidos de Certificação");
            System.out.println("5. Inserir Resultados de Certificação");
            System.out.println("6. Terminar Certificação");
            System.out.println("7. Remoção de Conta");
            System.out.println("8. Voce possui " + num_notificacoes + " notificacoes");
            System.out.println("9. Sair");
            System.out.print("Escolha uma opção: ");
            
            int option = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha
            
            switch (option) 
            {
                case 1:
                    Conexao_BD conexao = new Conexao_BD();
                    conexao.visualizarInformacao(username_utilizador);
                    break;
                case 2:
                    conexao = new Conexao_BD();
                    conexao.menuAtualizarUtilizador(id_utilizador);
                    break;
                case 3:
                    conexao = new Conexao_BD();
                    System.out.println("Insira o id do Equipamento: ");
                    int id_equipamento = scanner.nextInt();
                    conexao.inserirCertificacao(id_equipamento, id_utilizador);
                    break;
                case 7:
                    conexao = new Conexao_BD();
                    conexao.pedidoRemocaoUtilizador(id_utilizador);
                    break;
                case 4:
                    conexao = new Conexao_BD();
                    //System.out.println(id_utilizador);
                    conexao.pesquiarCertificacaoTecnico(id_utilizador);
                    break;
                case 5:
                    conexao = new Conexao_BD();
                    System.out.println("Insira o id da Certificação: ");
                    int id_certificacao = scanner.nextInt();
                    conexao.inserirResultadosCertificacao(id_certificacao, id_utilizador);
                    break;
                case 6:
                    conexao = new Conexao_BD();
                    System.out.println("Insira o id da Certificação: ");
                    int id_certificacao2 = scanner.nextInt();
                    conexao.terminarCertificacao(id_certificacao2, id_utilizador);
                    break;
                case 8:
                    conexao = new Conexao_BD();
                    conexao.listarNotificacoesTecnico(num_notificacoes);
                    break;
                case 9:
                    System.out.println("Adeus " + username_utilizador + " !");
                    running = false;
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}
