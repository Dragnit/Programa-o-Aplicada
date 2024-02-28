import java.util.Scanner;

public class Utilizadores {

    private static Scanner scanner = new Scanner(System.in);


    // Atributos obrigatórios
    private String nome;
    private String login;
    private String password;
    private String estado;
    private String email;
    private int tipo;

    // Atributos opcionais (podem ser nulos)
    private int numeroContribuinte;
    private int contactoTelefonico;
    private String morada;

    // Construtor com atributos obrigatórios
    public Utilizadores(String nome, String login, String password, String estado, String email, int tipo) {
        this.nome = nome;
        this.login = login;
        this.password = password;
        this.estado = estado;
        this.email = email;
        this.tipo = tipo;
    }

    // Getters e setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public Integer getNumeroContribuinte() {
        return numeroContribuinte;
    }

    public void setNumeroContribuinte(Integer numeroContribuinte) {
        this.numeroContribuinte = numeroContribuinte;
    }

    public Integer getContactoTelefonico() {
        return contactoTelefonico;
    }

    public void setContactoTelefonico(Integer contactoTelefonico) {
        this.contactoTelefonico = contactoTelefonico;
    }

    public String getMorada() {
        return morada;
    }

    public void setMorada(String morada) {
        this.morada = morada;
    }

    // Métodos adicionais (opcional)
    public boolean isAtivo() {
        return "activo".equals(estado);
    }

    public void ativar() {
        estado = "activo";
    }

    public void desativar() {
        estado = "inactivo";
    }

    

    // Valida o número de contribuinte
    public static int verificarNumeroContribuinte(int numeroContribuinte) {
        while (true) {
            if (numeroContribuinte < 0 || numeroContribuinte > 999999999) {
                System.out.println("Número de contribuinte inválido: " + numeroContribuinte);
                System.out.println("Número de contribuinte deve ter 9 dígitos. Por favor, digite novamente: ");
                numeroContribuinte = scanner.nextInt();
                continue;
            }
    
            String numeroContribuinteStr = Integer.toString(numeroContribuinte);
            if (numeroContribuinteStr.length() != 9) {
                System.out.println("Número de contribuinte deve ter 9 dígitos. Por favor, digite novamente: ");
                numeroContribuinte = scanner.nextInt();
                continue;
            }
    
            return numeroContribuinte;
        }
    }

    //Valida o contato telefônico
    public static int verificarContatoTelefonico(int contactoTelefonico) {
        while (true) {
            if (contactoTelefonico < 0 || contactoTelefonico > 999999999) {
                System.out.println("Contacto inválido: " + contactoTelefonico);
                System.out.println("Contacto telefônico deve ter 9 dígitos. Por favor, digite novamente: ");
                contactoTelefonico = scanner.nextInt();
                continue;
            }
    
            String contactoStr = Integer.toString(contactoTelefonico);
            if (contactoStr.length() != 9) {
                System.out.println("Contacto telefônico deve ter 9 dígitos. Por favor, digite novamente: ");
                contactoTelefonico = scanner.nextInt();
                continue;
            }
    
            // Verifica se o prefixo é 91, 92, 93 ou 96
            if (!contactoStr.startsWith("91") && !contactoStr.startsWith("92") && !contactoStr.startsWith("93") && !contactoStr.startsWith("96")) {
                System.out.println("Prefixo do contacto inválido. O prefixo deve ser 91, 92, 93 ou 96. Por favor, digite novamente: ");
                contactoTelefonico = scanner.nextInt();
                continue;
            }
    
            return contactoTelefonico;
        }
    }
    

    // Método toString atualizado
    @Override
    public String toString() {
        return "Utilizador{" +
                "nome='" + nome + '\'' +
                ", login='" + login + '\'' +
                ", password='*****'" + // Senha escondida por segurança
                ", estado='" + estado + '\'' +
                ", email='" + email + '\'' +
                ", tipo=" + tipo +
                ", numeroContribuinte=" + numeroContribuinte +
                ", contactoTelefonico='" + contactoTelefonico + '\'' +
                ", morada='" + morada + '\'' +
                '}';
    }
}
