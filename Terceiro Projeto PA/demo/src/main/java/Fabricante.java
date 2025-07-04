import java.util.Scanner;

public class Fabricante 
{
    public static void menu_Fabricante(String username_utilizador, int id_utilizador, String tipo)
    {
        
        int num_notificacoes = Conexao_BD.notificacoes(tipo, id_utilizador);
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
    
        while (running) {
            System.out.println("\n=== Menu de Fabricante ===");
            System.out.println("1. Visualizar informacao");
            System.out.println("2. Alterar informacao");
            System.out.println("3. Pedir para Remover Conta");
            System.out.println("4. Adicionar Equipamento");
            System.out.println("5. Listar Equipamentos");
            System.out.println("6. Pesquisar Equipamentos");
            System.out.println("7. Realizar Pedido de Certificação");
            System.out.println("8. Listar Certificacoes");
            System.out.println("9. Pesquisar por Certificação");
            System.out.println("10. Voce possui " + num_notificacoes + " notificacoes");
            System.out.println("11. Sair");
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
                case 4:
                    conexao = new Conexao_BD();
                    conexao.registaEquipamento(id_utilizador);
                    break;
                case 3:
                    conexao = new Conexao_BD();
                    conexao.pedidoRemocaoUtilizador(id_utilizador);
                    break;
                case 5:
                    conexao = new Conexao_BD();
                    conexao.pesquisarEquipamento(tipo, id_utilizador);
                    break;
                case 6:
                    conexao = new Conexao_BD(); 
                    conexao.pesquisarEquipamento(tipo, id_utilizador);
                    break;    
                case 7:
                    conexao = new Conexao_BD();
                    conexao.realizarPedidoCertificacao();
                    break;
                case 8:
                    conexao = new Conexao_BD();
                    conexao.listarCertificacao(tipo);
                    break; 
                case 9:
                    conexao = new Conexao_BD();
                    conexao.pesquiarCertificacaoFabricante(id_utilizador);
                    break;
                case 10:
                    conexao = new Conexao_BD();
                    conexao.listarNotificacoesFabricante();
                    break;
                case 11:
                    System.out.println("Adeus "+ username_utilizador + " !");
                    running = false;
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}
