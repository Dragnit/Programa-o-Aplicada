import java.util.Scanner;


public class Main {

    private static final int OPCAO_SAIR = 0;
    private static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) {
        int opcao;

        do {
            mostrarMenu();
            opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    validarNumeroContribuinte();
                    break;
                case 2:
                    validarContatoTelefonico();
                    break;
                case OPCAO_SAIR:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != OPCAO_SAIR);
    }

    private static void mostrarMenu() {
        System.out.println("----- Menu -----");
        System.out.println("1. Validar Número de Contribuinte");
        System.out.println("2. Validar Contato Telefônico");
        System.out.println("0. Sair");
        System.out.print("Digite a opção desejada: ");
    }

    private static int lerOpcao() {
        int opcao = 0;
        try {
            opcao = Integer.parseInt(System.console().readLine());
        } catch (NumberFormatException e) {
            System.err.println("Opção inválida: " + e.getMessage());
        }
        return opcao;
    }

    private static void validarNumeroContribuinte() {
        System.out.print("Digite o número de contribuinte: ");
        int numeroContribuinte = Integer.parseInt(System.console().readLine());

        try {
            Utilizadores.verificarNumeroContribuinte(numeroContribuinte);
            System.out.println("Número de contribuinte válido!");
            System.out.println("Pressione qualquer tecla para voltar ao menu...");
            scanner.nextLine(); // Consumir a nova linha
            System.out.print("\033[2J");
            System.out.flush();

        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void validarContatoTelefonico() {
        while (true) {
            try {
                System.out.print("Digite o Contato Telefônico: ");
                int contactoTelefonico = Integer.parseInt(System.console().readLine());
    
                Utilizadores.verificarContatoTelefonico(contactoTelefonico);
                System.out.println("Contacto válido!");
                break;
    
            } catch (NumberFormatException e) {
                System.err.println("O valor digitado não é um número inteiro. Por favor, digite novamente: ");
    
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
    
        System.out.println("Pressione qualquer tecla para voltar ao menu...");
        scanner.nextLine(); // Consumir a nova linha
        System.out.print("\033[2J");
        System.out.flush();
    }
    
}

