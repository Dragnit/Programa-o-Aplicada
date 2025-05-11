import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

/**
 * Classe Fabricante_Servidor representa o menu do Fabricante com os diversos comandos que podem ser executados pelo Fabricante.
 * 
 * @author Guilherme Rodrigues
 * 
 * @param Scanner scanner, serve para ler o input do utilizador
 * @param boolean running, serve para controlar o loop do menu
 */
public class Fabricante_Servidor 
{
    /**
     * Menu_Fabricante2 representa o menu do Fabricante com os diversos comandos que podem ser executados pelo Fabricante.
     * 
     * @author Guilherme Rodrigues
     * 
     * @param username
     * @param id_utilizador
     * @param tipo
     */
    public static void Menu_Fabricante2(String username, int id_utilizador, String tipo) 
    {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        int opção;

        try
        {
            System.out.println("Indique o IP do Servidor: ");
            String ipServidor = scanner.nextLine();
            System.out.println("Indique o Porto do Servidor: ");
            int porto = scanner.nextInt();
            Socket socket = new Socket(ipServidor, porto);


            System.out.println("O seu IP eh: " + socket.getInetAddress().getHostAddress() + "\n");
            
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    
            output.writeBytes("<" +username + "> " + "<hello>;\n");
            String server = input.readLine();
            System.out.println(server);
            while(running)
            {
                System.out.println("\n=== Menu de Fabricante ===");
                System.out.println("1. Visualizar informacao");
                System.out.println("2. Alterar informacao");
                System.out.println("3. Adicionar Equipamento");
                System.out.println("4. Listar Equipamentos");
                System.out.println("5. Pesquisar Equipamentos");
                System.out.println("6. Listar Certificacoes");
                System.out.println("7. Pesquisar por Certificação");
                System.out.println("8. Sair");
                System.out.print("Escolha uma opção: ");    
                
                int option = scanner.nextInt();
                scanner.nextLine(); // Consumir a quebra de linha
                
                switch (option) 
                {
                    case 1:
                        String mensagem = "<"+username+">"+"<Info>;";
                        output.writeBytes(mensagem+ "\n");
                        output.writeBytes(username);
                        output.writeBytes(option + "\n");
                        output.flush();
                        output.writeBytes(username + "\n");
                        output.flush();
                    
                        String resposta;
                        while (!(resposta = input.readLine()).equals("EOF")) 
                        {
                            System.out.println(resposta);
                        }
                        break;
                    case 2:
                        String mensagem3 = "<"+username+"> "+"<update>;";
                        output.writeBytes(mensagem3+ "\n");
                        output.writeBytes(option + "\n");
                        output.writeBytes(id_utilizador + "\n");
                        output.flush();
                    
                        String resposta2;
                        while (!(resposta2 = input.readLine()).equals("EOF")) 
                        {
                            System.out.println(resposta2);
                        }
                    
                        System.out.println("Insira o campo que deseja alterar (1-8): ");
                        String campo = scanner.nextLine();
                        output.writeBytes(campo + "\n");
                    
                        System.out.println("Insira o novo valor: ");
                        String novoValor = scanner.nextLine();
                        output.writeBytes(novoValor + "\n");
                        output.flush();
                    
                        String resposta3;
                        while (!(resposta3 = input.readLine()).equals("EOF")) 
                        {
                            System.out.println(resposta3);
                        }
                        break;
                    case 3:
                        String mensagem4 = "<"+username+"> "+"<inserir> <equipamento>;";
                        output.writeBytes(mensagem4+ "\n");
                        output.writeBytes(option + "\n");
                        output.writeBytes(id_utilizador + "\n");
                        output.flush();
                        
                        System.out.println("Insira a marca do equipamento: ");
                        String marca = scanner.nextLine(); 
                        output.writeBytes(marca + "\n");

                        System.out.println("Insira o modelo do equipamento: ");
                        String modelo = scanner.nextLine();
                        scanner.nextLine(); 
                        output.writeBytes(modelo + "\n");

                        System.out.println("Insira o sector comercial do equipamento: ");
                        String sector_comercial = scanner.nextLine();
                        output.writeBytes(sector_comercial + "\n");

                        System.out.println("Insira a potência do equipamento: ");
                        float potencia = scanner.nextFloat();
                        scanner.nextLine(); 
                        output.writeBytes(potencia + "\n");

                        System.out.println("Insira a amperagem do equipamento: ");
                        float amperagem = scanner.nextFloat();
                        scanner.nextLine(); 
                        output.writeBytes(amperagem + "\n");

                        System.out.println("Insira o número do equipamento: ");
                        int n_modelo = scanner.nextInt();
                        scanner.nextLine(); 
                        output.writeBytes(n_modelo + "\n");

                        String resposta4;
                        while (!(resposta4 = input.readLine()).equals("EOF")) 
                        {
                            System.out.println(resposta4);
                        }

                        break;
                    case 4:
                        String mensagem5 = "<"+username+"> "+"<pesquisa> <equipamento>;";
                        output.writeBytes(mensagem5 + "\n");
                        System.out.println("Insira o código SKU: ");
                        int sku = scanner.nextInt();
                        scanner.nextLine(); // Consume newline

                        output.writeBytes(option + "\n");
                        output.writeBytes(sku + "\n");
                        output.flush();
                    
                        String resposta5;
                        while (!(resposta5 = input.readLine()).equals("EOF")) 
                        {
                            System.out.println(resposta5);
                        }
                        break;
                    case 5:
                        String mensagem6 = "<"+username+"> "+"<listar> <equipamento>;";
                        output.writeBytes(mensagem6 + "\n");
                        output.writeBytes(option + "\n");
                        output.writeBytes(id_utilizador + "\n");
                        output.flush();
                        
                        String resposta6;
                        while (!(resposta6 = input.readLine()).equals("EOF")) 
                        {
                            System.out.println(resposta6);
                        }
                        break;
                    case 6:
                        String mensagem7 = "<"+username+"> "+" <listar> <certificacao>;\n";
                        output.writeBytes(mensagem7 + "\n");
                        output.writeBytes(option + "\n");
                        output.writeBytes(id_utilizador + "\n");
                        output.flush();
                    
                        String resposta7;
                        while (!(resposta7 = input.readLine()).equals("EOF")) 
                        {
                            System.out.println(resposta7);
                        }
                        break;
                    case 7:
                        String mensagem8 = "<"+username+"> "+"<pesquisa> <certificacao> <num_serie>;";
                        output.writeBytes(mensagem8 + "\n");
                        output.writeBytes(option + "\n");
                        output.writeBytes(id_utilizador + "\n");
                        
                        System.out.println("Insira o numero da certificação: ");
                        int idCertificacao = scanner.nextInt();
                        
                        scanner.nextLine(); // limpar o buffer
                        output.writeBytes(idCertificacao + "\n");

                        output.flush();

                        String resposta8;
                        while (!(resposta8 = input.readLine()).equals("EOF")) 
                        {
                            System.out.println(resposta8);
                        }
                        break;
                    case 8:
                        String mensagem2 = "<"+username+">"+"<bye>;";
                        output.writeBytes(mensagem2+ "\n");
                        output.writeBytes(option + "\n");
                        output.flush();
                    
                        System.out.println("A sair do menu de fabricante...");
                        
                        input.close();
                        output.close();
                        socket.close();
                        
                        running = false;
                        break;
                    default:
                        break;
                }
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        
    }

}
