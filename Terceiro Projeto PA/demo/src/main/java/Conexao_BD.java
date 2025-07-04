import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;

/**
 * Esta classe é responsável pela conexão com a base de dados PostgreSQL.
 * 
 * @author Guilherme Rodrigues
 */
public class Conexao_BD 
{
    
    private static final String PROPERTIES_FILE = "src/main/java/properties.txt";
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    
    // Bloco estático para carregar as configurações ao iniciar a classe
    static {
        carregarConfiguracoes();
    }
    
    /**
     * Método para carregar as configurações do ficheiro properties.txt.
     */
    private static void carregarConfiguracoes() 
    {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream(PROPERTIES_FILE)) 
        {
            properties.load(fis);
            URL = properties.getProperty("url");
            USER = properties.getProperty("user");
            PASSWORD = properties.getProperty("password");
        } 
        catch (IOException e) 
        {
            System.out.println("Erro ao carregar as configurações: " + e.getMessage());
        }
    }

    /**
     * Este método estabelece uma conexão com a base de dados.
     * 
     * @author Guilherme Rodrigues
     * @return Connection - objeto de conexão com a base de dados
     * @throws SQLException se ocorrer um erro na conexão
     */
    public Connection conexao() throws SQLException 
    {
        try 
        {
            // Carregar o driver do PostgreSQL
            Class.forName("org.postgresql.Driver");
            
            // Estabelecer ligação com a base de dados
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conectado com sucesso!");
            return conn;
        } 
        catch (ClassNotFoundException e) 
        {
            System.out.println("Erro! Driver do PostgreSQL não encontrado!");
            throw new SQLException("Driver não encontrado", e);
        }
    }

    /**
     * Este método registra um novo cliente na base de dados.
     * 
     * @author Guilherme Rodrigues
     * 
     * @param id_utilizador ID do utilizador
     * @param username Nome de utilizador
     * @param password Pass do utilizador
     * @param email Email do utilizador
     * @param tipo Tipo de utilizador
     * @param estado Estado do utilizador
     */
    public static void registaGestor() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insira o Nome do Gestor: ");
        String nome = scanner.nextLine();
        System.out.print("Insira o username: ");
        String user = scanner.nextLine();
        System.out.print("Insira a senha: ");
        String password = scanner.nextLine();
        System.out.print("Insira o email: ");
        String mail = scanner.nextLine();
        String tipo = "Gestor";
        boolean estado = true;

        // Verificar formato do email usando regex
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)\\.(\\w+)$";
        boolean emailValido = mail.matches(emailRegex);
        
        if (!emailValido) 
        {
            System.out.println("Formato de email inválido! Deve ser: [designação]@[entidade].[domínio]");
            return; // Sai da função ou pode implementar um loop para pedir o email novamente
        }

        String sql = "INSERT INTO utilizador (nome_utilizador, username_utilizador, password_utilizador, " +
                     "email_utilizador, tipo, estado_utilizador, Pedido_Remocao, desativado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();  // Criar instância
            Connection conn = conexaoBD.conexao();  // Chamar o método
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, nome);
            stmt.setString(2, user);
            stmt.setString(3, password);
            stmt.setString(4, mail);
            stmt.setString(5, tipo);
            stmt.setBoolean(6, estado);
            stmt.setBoolean(7, false);
            stmt.setBoolean(8, false);
    
            stmt.executeUpdate();
            System.out.println("\nUtilizador registado com sucesso na base de dados!");
    
            stmt.close();
            conn.close();  // Fechar a conexão para evitar consumo de recursos
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao registar utilizador: " + e.getMessage());
        }
    }
    
    /**
     * Este método remove um utilizador da base de dados.
     * 
     * @author Guilherme Rodrigues
     * 
     * @param id_utilizador ID do utilizador a ser removido
     */
    public static void removerGestor(int id_utilizador) 
    {
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            
            // 1. Descobrir o tipo do utilizador
            String checkSql = "SELECT tipo FROM utilizador WHERE id_utilizador = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, id_utilizador);
            ResultSet rs = checkStmt.executeQuery();
            
            if (rs.next()) 
            {
                String tipo = rs.getString("tipo");
                /* 
                if(!tipo.equals("Gestor"))
                {
                    // 2. Primeiro remover da tabela da classe filha
                    String deleteChildSql = "DELETE FROM " + tipo.toLowerCase() + " WHERE id_utilizador = ?";
                    PreparedStatement deleteChildStmt = conn.prepareStatement(deleteChildSql);
                    deleteChildStmt.setInt(1, id_utilizador);
                    deleteChildStmt.executeUpdate();
                    deleteChildStmt.close();
                    
                }
                */
                
                // 3. Depois remover da tabela utilizador
                String deleteUserSql = "UPDATE utilizador SET desativado = true WHERE id_utilizador = ?";
                PreparedStatement deleteUserStmt = conn.prepareStatement(deleteUserSql);
                deleteUserStmt.setInt(1, id_utilizador);
                deleteUserStmt.executeUpdate();
                deleteUserStmt.close();
                
                System.out.println("\n" + tipo + " removido com sucesso da base de dados!");
            } 
            else 
            {
                System.out.println("Erro: Utilizador com ID " + id_utilizador + " não encontrado.");
            }
            
            checkStmt.close();
            rs.close();
            conn.close();
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao remover utilizador: " + e.getMessage());
        }    
    }

    /**
     * Este método Pesquisa um utilizador na base de dados com base no username.
     * 
     * @author Guilherme Rodrigues
     * 
     * @param username_utilizador username do utilizador
     */
    public static void listarPorNome(String username_utilizador) 
    {
        String sql = "SELECT * FROM utilizador WHERE username_utilizador LIKE ? AND desativado = false";
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();  // Criar instância
            Connection conn = conexaoBD.conexao();  // Chamar o método
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" +username_utilizador+ "%");
        
            ResultSet rs = pstmt.executeQuery();
            System.out.println("\n=== Utilizador ===");
            
            boolean encontrado = false;
            while (rs.next()) 
            {
                encontrado = true;
                int id_utilizador = rs.getInt("id_utilizador");
                String username = rs.getString("username_utilizador");
                String password = rs.getString("password_utilizador");
                String email = rs.getString("email_utilizador");
                String tipo = rs.getString("tipo");
                boolean estado = rs.getBoolean("estado_utilizador");
                
                System.out.println("ID: " + id_utilizador);
                System.out.println("Username: " + username);
                System.out.println("Email: " + email);
                System.out.println("Tipo: " + tipo);
                System.out.println("Estado: " + estado);
                System.out.println("------------------");
            }
            
            if (!encontrado) 
            {
                System.out.println("Nenhum utilizador encontrado com o username: " + username_utilizador);
            }
            rs.close();
            pstmt.close();
            conn.close();  // Fechar a conexão para evitar consumo de recursos
        } 
        catch (SQLException e)
        {
            System.out.println("Erro ao listar gestores: " + e.getMessage());
        }
               
    }

    /**
     * Este método Pesquisa um utilizador na base de dados com base no tipo.
     * 
     * @author Guilherme Rodrigues
     * 
     * @param tipo_utilizador tipo do utilizador
     */
    public static void listarPorTipo(String tipo_utilizador)
    {
        String sql = "SELECT * FROM utilizador WHERE tipo = ? AND desativado = false";
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();  // Criar instância
            Connection conn = conexaoBD.conexao();  // Chamar o método
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tipo_utilizador);
        
        ResultSet rs = pstmt.executeQuery();
        System.out.println("\n=== Utilizador ===");
        
        boolean encontrado = false;
        while (rs.next()) 
        {
            encontrado = true;
            int id_utilizador = rs.getInt("id_utilizador");
            String username = rs.getString("username_utilizador");
            String password = rs.getString("password_utilizador");
            String email = rs.getString("email_utilizador");
            String tipo = rs.getString("tipo");
            boolean estado = rs.getBoolean("estado_utilizador");
            
            System.out.println("ID: " + id_utilizador);
            System.out.println("Username: " + username);
            System.out.println("Email: " + email);
            System.out.println("Tipo: " + tipo);
            System.out.println("Estado: " + estado);
            System.out.println("------------------");
        }
        
        if (!encontrado) {
            System.out.println("Nenhum utilizador encontrado com o tipo: " + tipo_utilizador);
        }
            rs.close();
            pstmt.close();
            conn.close();  // Fechar a conexão para evitar consumo de recursos
        } 
        catch (SQLException e)
        {
            System.out.println("Erro ao listar gestores: " + e.getMessage());
        }
    }
    
    /**
     * Este método Pesquisa todos os utilizadores na base de dados e ordena-os por nome.
     * 
     * @author Guilherme Rodrigues
     */
    public static void listarClientes() 
    {
        String sql = "SELECT * FROM utilizador WHERE desativado = false ORDER BY username_utilizador";
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();  // Criar instância
            Connection conn = conexaoBD.conexao();  // Chamar o método
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("\n=== Utilizadores ===");
            while (rs.next()) 
            {
                int id_utilizador = rs.getInt("id_utilizador");
                String username = rs.getString("username_utilizador");
                String email = rs.getString("email_utilizador");
                String tipo = rs.getString("tipo");
                boolean estado = rs.getBoolean("estado_utilizador");
                System.out.println("ID: " + id_utilizador);
                System.out.println("Username: " + username);
                System.out.println("Email: " + email);
                System.out.println("Estado: " + estado);
                System.out.println("------------------");
            }
            rs.close();
            stmt.close();
            conn.close();  // Fechar a conexão
        } 
        catch (SQLException e)
        {
            System.out.println("Erro ao listar utilizadores: " + e.getMessage());
        }
    }

    /**
     * Método para fechar recursos da conexão de forma segura
     * 
     * @author Guilherme Rodrigues
     * 
     * @param conn Connection a ser fechada
     * @param stmt Statement a ser fechado
     * @param rs ResultSet a ser fechado
     */
    public static void fecharRecursos(Connection conn, Statement stmt, ResultSet rs) 
    {
        try 
        {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao fechar a conexão! " + e.getMessage());
        }
    }

    /**
     * Este método verifica o login de um utilizador na base de dados. Percorre toda a base de dados para verificar se o utilizador existe e a sua password coincide
     * 
     * @author Guilherme Rodrigues
     * 
     * @param username_utilizador Username do utilizador
     * @param password_utilizador Password do utilizador
     */
    public static void verificarLogin(String username_utilizador, String password_utilizador)
    {
        String sql = "SELECT id_utilizador, username_utilizador, password_utilizador, tipo, estado_utilizador, desativado FROM utilizador WHERE username_utilizador = ? AND password_utilizador = ?";
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();  // Criar instância
            Connection conn = conexaoBD.conexao();  // Chamar o método
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username_utilizador);
            pstmt.setString(2, password_utilizador);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) 
            {
                String username = rs.getString("username_utilizador");
                String tipo = rs.getString("tipo");
                boolean estado = rs.getBoolean("estado_utilizador");
                boolean desativado = rs.getBoolean("desativado");
                int id_utilizador = rs.getInt("id_utilizador");
                
                if (estado == false || desativado == true) 
                {
                    System.out.println("\nUtilizador desativado!");
                    System.out.println("Contacte o Gestor!");
                }
                else
                {
                    if(tipo.equals("Gestor"))
                    {
                        System.out.println("\n Bem-vindo, " + username + "!");
                        Gestor gestor = new Gestor();
                        int num_notificacoes = notificacoes(tipo, 0);
                        //gestor.menu_Gestor(num_notificacoes, username);
                        Pagina_Inicial janelaInicial = new Pagina_Inicial(username, id_utilizador, tipo, num_notificacoes);
                        janelaInicial.setSize(600, 600);
                        janelaInicial.setLocationRelativeTo(null);
                        janelaInicial.setVisible(true);
                    }
                    else if(tipo.equals("Tecnico"))
                    {
                        System.out.println("\n Bem-vindo, " + username + "!");
                        Tecnico tecnico = new Tecnico();
                        //int num_notificacoes = notificacoes(tipo);
                        //tecnico.menu_Tecnico(username, id_utilizador, tipo);
                        Pagina_Inicial janelaInicial = new Pagina_Inicial(username, id_utilizador, tipo, 0);
                        janelaInicial.setSize(600, 600);
                        janelaInicial.setLocationRelativeTo(null);
                        janelaInicial.setVisible(true);
                    }
                    else
                    {
                        System.out.println("\n Bem-vindo, " + username + "!");
                        //int num_notificacoes = notificacoes(tipo);
                        Fabricante fabricante = new Fabricante();
                        //fabricante.menu_Fabricante(username, id_utilizador, tipo);
                        Pagina_Inicial janelaInicial = new Pagina_Inicial(username, id_utilizador, tipo, 0);
                        janelaInicial.setSize(600, 600);
                        janelaInicial.setLocationRelativeTo(null);
                        janelaInicial.setVisible(true);
                    }
                }

            } 
            else 
            {
                System.out.println("\nUtillizador ou senha incorretos!");
                JOptionPane.showMessageDialog(null, "Dados incorretos!", "Erro", JOptionPane.ERROR_MESSAGE);
                Main.caixaDeDialogo();
            }
            rs.close();
            pstmt.close();
            conn.close();  // Fechar a conexão
        } 
        catch (SQLException e)
        {
            System.out.println("Erro ao verificar login: " + e.getMessage());
        }
    }   

    /**
     * Metodo para registar um tecnico na base de dados.
     * 
     * @author Guilherme Rodrigues
     */
    public static void registaTecnico()
    {

        Scanner sc = new Scanner(System.in);

        // Capturar dados do técnico
        System.out.println("Insira o Nome do Tecnico: ");
        String nome_utilizador = sc.nextLine();
        System.out.print("Insira o Username do Tecnico: ");
        String username_utilizador = sc.nextLine();
        System.out.print("Insira a Password do Tecnico: ");
        String password_utilizador = sc.nextLine();
        System.out.print("Insira o Email do Tecnico: ");
        String email_utilizador = sc.nextLine();

        String sql1 = "INSERT INTO utilizador (nome_utilizador, username_utilizador, password_utilizador, email_utilizador, tipo, estado_utilizador, Pedido_Remocao, desativado) " +
                      "VALUES (?, ?,?, ?, ?, ?, ?, ?) RETURNING id_utilizador";

        int id_utilizador = -1;  // Variável para armazenar o ID gerado

        try
        {
            Connection conn = new Conexao_BD().conexao();
            PreparedStatement pstmt = conn.prepareStatement(sql1);

            // Definir parâmetros da query
            pstmt.setString(1, nome_utilizador);
            pstmt.setString(2, username_utilizador);
            pstmt.setString(3, password_utilizador);
            pstmt.setString(4, email_utilizador);
            pstmt.setString(5, "Tecnico");
            pstmt.setBoolean(6, false);
            pstmt.setBoolean(7, false);
            pstmt.setBoolean(8, false);
            
            // Executar a query e capturar o ID gerado
            try (ResultSet rs = pstmt.executeQuery()) 
            {
                if (rs.next()) 
                {
                    id_utilizador = rs.getInt(1); // Obtém o ID retornado pelo banco
                }
            }

            //System.out.println("\nUtilizador registado com sucesso! ID: " + id_utilizador);
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao registar Utilizador: " + e.getMessage());
            return; // Se der erro, sai da função
        }

        // Se o ID não foi gerado corretamente, não segue para a segunda parte
        if (id_utilizador == -1) 
        {
            System.out.println("Erro ao obter ID do Utilizador.");
            return;
        }

        // Capturar dados adicionais do técnico
        System.out.print("Insira o NIF do Tecnico: ");
        String nif = sc.nextLine();
        // Expressão regular para validar o NIF
        if (!nif.matches("\\d{9}")) 
        {
            System.out.println("NIF inválido! Deve ter 9 dígitos.");
            return; // Sai da função se o NIF for inválido
        }
        int nifInt = Integer.parseInt(nif);
        System.out.print("Insira o Telefone do Tecnico: ");
        String telefone = sc.nextLine();
        // Expressão regular para validar o número de telefone
        if (!telefone.matches("[923]\\d{8}")) 
        {
            System.out.println("Número de telefone inválido! Deve ter 9 dígitos e começar com 9, 2 ou 3.");
            return; // Sai da função se o número for inválido
        }
        int telefoneInt = Integer.parseInt(telefone);
        sc.nextLine();
        System.out.print("Insira a Morada do Tecnico: ");
        String morada = sc.nextLine();
        System.out.print("Insira o Nivel de Certificação do Tecnico: (1-7): ");
        int nivelCertificacao = sc.nextInt();
        sc.nextLine();
        System.out.print("Insira a Área de Especialidade do Tecnico: ");
        String areaEspecializacao = sc.nextLine();
        sc.nextLine();

        String sql2 = "INSERT INTO tecnico (id_utilizador, nif_tecnico, telefone_tecnico, morada_tecnico, nivel_certificacao, area_especializacao) " +
                      "VALUES (?, ?, ?, ?, ?, ?)";

        try 
        {
            Connection conn = new Conexao_BD().conexao();
            PreparedStatement pstmt = conn.prepareStatement(sql2);
            
            pstmt.setInt(1, id_utilizador); // Agora o ID é passado corretamente
            pstmt.setInt(2, nifInt);
            pstmt.setInt(3, telefoneInt);
            pstmt.setString(4, morada);
            pstmt.setInt(5, nivelCertificacao);
            pstmt.setString(6, areaEspecializacao);
            
            pstmt.executeUpdate();
            System.out.println("\nTécnico registado com sucesso na base de dados!");
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao registar Técnico: " + e.getMessage());
        }
    }

    /**
     * Metodo para registar um fabricante na base de dados.
     * 
     * @author Guilherme Rodrigues
     */
    public static void registaFabricante()
    {
        Scanner sc = new Scanner(System.in);

        // Capturar dados do fabricante
        System.out.print("Insira o Nome do Fabricante: ");
        String nome_utilizador = sc.nextLine();
        System.out.print("Insira o Username do Fabricante: ");
        String username_utilizador = sc.nextLine();
        System.out.print("Insira a Password do Fabricante: ");
        String password_utilizador = sc.nextLine();
        System.out.print("Insira o Email do Fabricante: ");
        String email_utilizador = sc.nextLine();

        String sql1 = "INSERT INTO utilizador (nome_utilizador, username_utilizador, password_utilizador, email_utilizador, tipo, estado_utilizador, Pedido_Remocao, desativado) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_utilizador";

        int id_utilizador = -1;  // Variável para armazenar o ID gerado

        try
        {
            Connection conn = new Conexao_BD().conexao();
            PreparedStatement pstmt = conn.prepareStatement(sql1);

            // Definir parâmetros da query
            pstmt.setString(1, nome_utilizador);
            pstmt.setString(2, username_utilizador);
            pstmt.setString(3, password_utilizador);
            pstmt.setString(4, email_utilizador);
            pstmt.setString(5, "Fabricante");
            pstmt.setBoolean(6, false);
            pstmt.setBoolean(7, false);
            pstmt.setBoolean(8, false);
            
            // Executar a query e capturar o ID gerado
            try (ResultSet rs = pstmt.executeQuery()) 
            {
                if (rs.next()) 
                {
                    id_utilizador = rs.getInt(1); // Obtém o ID retornado pelo banco
                }
            }

            //System.out.println("\nUtilizador registado com sucesso! ID: " + id_utilizador);
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao registar Utilizador: " + e.getMessage());
            return; // Se der erro, sai da função
        }

        // Se o ID não foi gerado corretamente, não segue para a segunda parte
        if (id_utilizador == -1) 
        {
            System.out.println("Erro ao obter ID do Utilizador.");
            return;
        }

        // Capturar dados adicionais do técnico
        System.out.print("Insira o NIF do Fabricante: ");
        String nif = sc.nextLine();
        if (!nif.matches("\\d{9}")) 
        {
            System.out.println("NIF inválido! Deve ter 9 dígitos.");
            return; // Sai da função se o NIF for inválido
        }
        int nifInt = Integer.parseInt(nif);
        System.out.print("Insira o Telefone do Fabricante: ");
        String telefone = sc.nextLine();
        if (!telefone.matches("[923]\\d{8}")) 
        {
            System.out.println("Número de telefone inválido! Deve ter 9 dígitos e começar com 9, 2 ou 3.");
            return; // Sai da função se o número for inválido
        }
        int telefoneInt = Integer.parseInt(telefone);
        sc.nextLine();
        System.out.print("Insira a Morada do Fabricante: ");
        String morada = sc.nextLine();
        System.out.print("Insira o Sector Comercial: ");
        String sectorComercias = sc.nextLine();
        System.out.print("Insira a Data de Inicio de Actividade no Formato (YYYY-MM-DD): ");
        String dataInicioActividade = sc.nextLine();

        String sql2 = "INSERT INTO fabricante(id_utilizador, nif_fabricante, telefone_fabricante, morada_fabricante, sector_comercial, inicio_actividade) " +
                      "VALUES (?, ?, ?, ?, ?, ?)";

        try 
        {
            Connection conn = new Conexao_BD().conexao();
            PreparedStatement pstmt = conn.prepareStatement(sql2);
            
            pstmt.setInt(1, id_utilizador); // Agora o ID é passado corretamente
            pstmt.setInt(2, nifInt);
            pstmt.setInt(3, telefoneInt);
            pstmt.setString(4, morada);
            pstmt.setString(5, sectorComercias);
            pstmt.setDate(6, java.sql.Date.valueOf(dataInicioActividade));
            
            pstmt.executeUpdate();
            System.out.println("\nFabricante registado com sucesso na base de dados!");
        } 
        catch (SQLException e) 
        {
            String sql = "DELETE FROM utilizador WHERE id_utilizador = (SELECT MAX(id_utilizador) FROM utilizador)";
            try 
            {
                Conexao_BD conexaoBD = new Conexao_BD();  // Criar instância
                Connection conn = conexaoBD.conexao();  // Chamar o método
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id_utilizador);
                stmt.executeUpdate();
                System.out.println("\nUtilizador removido com sucesso da base de dados!");
                stmt.close();
                conn.close();  // Fechar a conexão para evitar consumo de recursos
            } 
            catch (SQLException f) 
            {
                System.out.println("Erro ao remover utilizador: " + e.getMessage());
            }   

            System.out.println("Erro ao registar Fabricante: " + e.getMessage());
        }  
    }

    /**
     * Metodo para alterar as informacoes de um utilizador na base de dados
     * 
     * @param id_utilizador
     * @param campo
     * @param novoValor
     */
    public static void atualizarUtilizador(int id_utilizador, String campo, String novoValor) 
    {
        String sql = "UPDATE utilizador SET " + campo + " = ? WHERE id_utilizador = ?";
        
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, novoValor);
            stmt.setInt(2, id_utilizador);
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) 
            {
                System.out.println("\nInformação do utilizador atualizada com sucesso!");
            } 
            else 
            {
                System.out.println("\nNenhum utilizador encontrado com o ID " + id_utilizador);
            }
            
            stmt.close();
            conn.close();
        } 
        catch (SQLException e) {
            System.out.println("Erro ao atualizar utilizador: " + e.getMessage());
        }
    }

    /**
     * Metodo para alterar as informacoes de um especializado na base de dados
     * 
     * @au 
     * 
     * @param id_utilizador
     * @param campo
     * @param novoValor
     * @param tipo
     */
    public static void atualizarEspecializado(int id_utilizador, String campo, String novoValor, String tipo) 
    {
        String tabela = tipo.equalsIgnoreCase("Tecnico") ? "tecnico" : "fabricante";
        String sql = "UPDATE " + tabela + " SET " + campo + " = ? WHERE id_utilizador = ?";

        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Verifica o campo e define o valor corretamente
            switch (campo) 
            {
                case "nif_tecnico":
                case "telefone_tecnico":
                case "nivel_certificacao":
                case "area_especializacao": // INT
                case "nif_fabricante":
                case "telefone_fabricante":
                    stmt.setInt(1, Integer.parseInt(novoValor));
                    break;

                case "estado_utilizador": // BOOLEAN
                    stmt.setBoolean(1, Boolean.parseBoolean(novoValor));
                    break;

                default: // STRING (padrão)
                    stmt.setString(1, novoValor);
                    break;
            }

            stmt.setInt(2, id_utilizador);
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) 
            {
                System.out.println("\nInformação do " + tipo + " atualizada com sucesso!");
            } 
            else 
            {
                System.out.println("\nNenhuma atualização realizada para " + tipo + ".");
            }
            
            stmt.close();
            conn.close();
        } 
        catch (NumberFormatException e) 
        {
            System.out.println("Erro: O valor inserido não é válido para o campo '" + campo + "'.");
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao atualizar " + tipo + ": " + e.getMessage());
        }
    }

    /**
     * Metodo para alterar o estado de um utilizador na base de dados
     * 
     * @author Guilherme Rodrigues
     * 
     * @param id_utilizador
     * @param campo
     * @param novoValor
     */
    public static void atualizarEstado(int id_utilizador, String campo, boolean novoValor) 
    {
        String sql = "UPDATE utilizador SET " + campo + " = ? WHERE id_utilizador = ?";
        
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setBoolean(1, novoValor);
            stmt.setInt(2, id_utilizador);
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) 
            {
                System.out.println("\nInformação do utilizador atualizada com sucesso!");
            } 
            else 
            {
                System.out.println("\nNenhum utilizador encontrado com o ID " + id_utilizador);
            }
            
            stmt.close();
            conn.close();
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao atualizar utilizador: " + e.getMessage());
        }
    }

    public static void atualizarDesativado(int id_utilizador, String campo, boolean novoValor) 
    {
        String sql = "UPDATE utilizador SET " + campo + " = ? WHERE id_utilizador = ?";
        
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setBoolean(1, novoValor);
            stmt.setInt(2, id_utilizador);
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) 
            {
                System.out.println("\nInformação do utilizador atualizada com sucesso!");
            } 
            else 
            {
                System.out.println("\nNenhum utilizador encontrado com o ID " + id_utilizador);
            }
            
            stmt.close();
            conn.close();
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao atualizar utilizador: " + e.getMessage());
        }
    }

    /**
     * Menu de opções para alterar as informacoes de um utilizador na base de dados
     * 
     * @author Guilherme Rodrigues
     * 
     * @param id_utilizador
     */
    public static void menuAtualizarUtilizador(int id_utilizador) 
    {
        Scanner sc = new Scanner(System.in);
        
        try 
        {
            // Conectar ao banco de dados
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            String checkSql = "SELECT * FROM utilizador WHERE id_utilizador = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, id_utilizador);
            ResultSet rs = checkStmt.executeQuery();
            
            if (!rs.next()) 
            {
                System.out.println("Utilizador com ID " + id_utilizador + " não encontrado.");
                return;
            }
            
            // Mostrar dados atuais
            System.out.println("\n=== Dados Atuais do Utilizador ===");
            System.out.println("ID: " + rs.getInt("id_utilizador"));
            System.out.println("Nome: " + rs.getString("nome_utilizador"));
            System.out.println("Username: " + rs.getString("username_utilizador"));
            System.out.println("Email: " + rs.getString("email_utilizador"));
            System.out.println("Tipo: " + rs.getString("tipo"));
            System.out.println("Estado: " + rs.getBoolean("estado_utilizador"));
    
            String tipo = rs.getString("tipo");
            
            // Menu base
            System.out.println("\nQual campo deseja atualizar?");
            System.out.println("1. Nome");
            System.out.println("2. Username");
            System.out.println("3. Email");
            System.out.println("4. Password");
            System.out.println("5. Estado");
            System.out.println("6. Desativado");
    
            // Opções adicionais para Técnico
            if ("Tecnico".equalsIgnoreCase(tipo)) 
            {
                System.out.println("7. NIF");
                System.out.println("8. Telefone");
                System.out.println("9. Morada");
                System.out.println("10. Nivel de Certificacao");
                System.out.println("11. Area de Especializacao");
            }
            // Opções adicionais para Fabricante
            else if ("Fabricante".equalsIgnoreCase(tipo)) 
            {
                System.out.println("7. NIF");
                System.out.println("8. Telefone");
                System.out.println("9. Morada");
                System.out.println("10. Setor Comercial");
                System.out.println("11. Inicio de atividade");
            }
    
            System.out.println("0. Cancelar");
            
            System.out.print("\nEscolha uma opção: ");
            int opcao = sc.nextInt();
            sc.nextLine(); // Limpar buffer
    
            if (opcao == 0) 
            {
                System.out.println("Operação cancelada.");
                return;
            }
    
            String campo = "";
            String label = "";
    
            switch (opcao) 
            {
                case 1:
                    campo = "nome_utilizador";
                    label = "Nome";
                    break;
                case 2:
                    campo = "username_utilizador";
                    label = "Username";
                    break;
                case 3:
                    campo = "email_utilizador";
                    label = "Email";
                    break;
                case 4:
                    campo = "password_utilizador";
                    label = "Password";
                    break;
                case 5:
                    campo = "estado_utilizador";
                    System.out.print("Digite o novo estado (true/false): ");
                    boolean novoEstado = sc.nextBoolean();
                    atualizarEstado(id_utilizador, campo, novoEstado);
                    return; // Termina após atualizar o estado
                case 6:
                    campo = "desativado";
                    System.out.print("Deseja desativar o utilizador (true/false): ");
                    boolean novoDesativado = sc.nextBoolean();
                    atualizarDesativado(id_utilizador, campo, novoDesativado);        
                    if (novoDesativado == true) 
                        atualizarEstado(id_utilizador, "estado_utilizador", false); 
                    else
                        atualizarEstado(id_utilizador, "estado_utilizador", true);
                    return; // Termina após atualizar o estado
                case 7:
                    if ("Tecnico".equalsIgnoreCase(tipo)) 
                    {
                        campo = "nif_tecnico";
                        label = "NIF";
                    } 
                    else if ("Fabricante".equalsIgnoreCase(tipo)) 
                    {
                        campo = "nif_fabricante";
                        label = "NIF";
                    }
                    break;
                case 8:
                    if ("Tecnico".equalsIgnoreCase(tipo)) 
                    {
                        campo = "telefone_tecnico";
                        label = "Telefone";
                    } 
                    else if ("Fabricante".equalsIgnoreCase(tipo)) 
                    {
                        campo = "telefone_fabricante";
                        label = "Telefone";
                    }
                    break;
                case 9:
                    if ("Fabricante".equalsIgnoreCase(tipo)) 
                    {
                        campo = "morada_fabricante";
                        label = "Morada";
                    }
                    else if ("Tecnico".equalsIgnoreCase(tipo)) 
                    {
                        campo = "morada_tecnico";
                        label = "Morada";
                    }
                    break;
                case 10:
                    if ("Fabricante".equalsIgnoreCase(tipo)) 
                    {
                        campo = "setor_comercial_fabricante";
                        label = "Setor Comercial";
                    }
                    else if ("Tecnico".equalsIgnoreCase(tipo)) 
                    {
                        campo = "nivel_certificacao_tecnico";
                        label = "Nivel de Certificacao";
                    }
                    break;
                case 11:
                    if ("Fabricante".equalsIgnoreCase(tipo)) 
                    {
                        campo = "inicio_atividade_fabricante";
                        label = "Inicio de atividade";
                    }
                    else if ("Tecnico".equalsIgnoreCase(tipo)) 
                    {
                        campo = "area_especializacao_tecnico";
                        label = "Area de Especializacao";
                    }
                default:
                    System.out.println("Opção inválida.");
                    return;
            }
    
            System.out.print("Digite o novo " + label + ": ");
            String novoValor = sc.nextLine();
            
            if(label.equals("Telefone"))
            {
                if (!novoValor.matches("[923]\\d{8}")) 
                {
                    System.out.println("Número de telefone inválido! Deve ter 9 dígitos e começar com 9, 2 ou 3.");
                    return; // Sai da função se o número for inválido
                }
            }
    
            if(label.equals("NIF"))
            {
                if (!novoValor.matches("\\d{9}")) 
                {
                    System.out.println("NIF inválido! Deve ter 9 dígitos.");
                    return; // Sai da função se o NIF for inválido
                }
            }
    
            // Verifica se o campo pertence à tabela utilizador ou tabela especializada
            if (opcao >= 1 && opcao <= 6) 
            {
                atualizarUtilizador(id_utilizador, campo, novoValor);
            } 
            else 
            {
                atualizarEspecializado(id_utilizador, campo, novoValor, tipo);
            }
    
            rs.close();
            checkStmt.close();
            conn.close();
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao consultar utilizador: " + e.getMessage());
        }
    }

    /**
     * Este método Pesquisa um utilizador na base de dados com base no username.
     * 
     * @author Guilherme Rodrigues
     * 
     * @param username_utilizador username do utilizador
     */
    public static void visualizarInformacao(String username_utilizador) 
    {
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();

            // Obter informações básicas do utilizador
            String sql = "SELECT * FROM utilizador WHERE username_utilizador = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username_utilizador);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) 
            {
                System.out.println("\n=== Informação do Utilizador ===");
                int id_utilizador = rs.getInt("id_utilizador");
                String tipo = rs.getString("tipo");

                System.out.println("ID: " + id_utilizador);
                System.out.println("Nome: " + rs.getString("nome_utilizador"));
                System.out.println("Username: " + rs.getString("username_utilizador"));
                System.out.println("Email: " + rs.getString("email_utilizador"));
                System.out.println("Tipo: " + tipo);

                // Verificar se é Técnico ou Fabricante para mostrar informações adicionais
                if ("Tecnico".equalsIgnoreCase(tipo)) 
                {
                    visualizarTecnico(id_utilizador, conn);
                } 
                else if ("Fabricante".equalsIgnoreCase(tipo)) 
                {
                    visualizarFabricante(id_utilizador, conn);
                }
            } 
            else 
            {
                System.out.println("Utilizador não encontrado.");
            }
            
            rs.close();
            stmt.close();
            conn.close();
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao consultar utilizador: " + e.getMessage());
        }
    }

    /**
     * Metodo para visualizar informacoes adicionais de um tecnico
     * 
     * @author Guilherme Rodrigues
     * 
     * @param id_utilizador
     * @param conn
     * @throws SQLException
     */
    public static void visualizarTecnico(int id_utilizador, Connection conn) throws SQLException 
    {
        String sql = "SELECT * FROM tecnico WHERE id_utilizador = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id_utilizador);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) 
        {
            System.out.println("\n=== Informação Adicional do Técnico ===");
            System.out.println("NIF: " + rs.getString("nif_tecnico"));
            System.out.println("Telefone: " + rs.getInt("telefone_tecnico"));
            System.out.println("Morada: " + rs.getString("morada_tecnico"));
            System.out.println("Nivel de Certificacao: " + rs.getString("nivel_certificacao"));
            System.out.println("Area de Especializacao: " + rs.getString("area_especializacao"));
        } 
        else 
        {
            System.out.println("Nenhuma informação adicional encontrada para Técnico.");
        }

        rs.close();
        stmt.close();
    }

    /**
     * 
     * @author Guilherme Rodrigues
     * 
     * @param id_utilizador
     * @param conn
     * @throws SQLException
     */
    public static void visualizarFabricante(int id_utilizador, Connection conn) throws SQLException 
    {
        String sql = "SELECT * FROM fabricante WHERE id_utilizador = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id_utilizador);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) 
        {
            System.out.println("\n=== Informação Adicional do Fabricante ===");
            System.out.println("NIF: " + rs.getInt("nif_fabricante"));
            System.out.println("Telefone: " + rs.getInt("telefone_fabricante"));
            System.out.println("Morada: " + rs.getString("morada_fabricante"));
            System.out.println("Sector Comercial: " + rs.getString("sector_comercial"));
            System.out.println("Inicio da Atividade: " + rs.getString("inicio_actividade"));
        } 
        else 
        {
            System.out.println("Nenhuma informação adicional encontrada para Fabricante.");
        }

        rs.close();
        stmt.close();
    }

    /**
     * Metodo para contar o numero de notificacoes pendentes
     * 
     * @param tipo
     * @param id_utilizador
     * 
     * @author Guilherme Rodrigues
     * 
     * @return int - numero de notificacoes 
     */
    public static int notificacoes(String tipo, int id_utilizador) 
    {
        if(tipo.equals("Gestor"))
        {

            String sql = "SELECT count(estado_utilizador) as num_pedidoRegisto FROM utilizador WHERE estado_utilizador = false";
            
            int num_pedidoRemocao = 0;
            int num_pepdidoRegisto = 0;
            int num_pedidoCertificacao = 0;
            int num_Negas = 0;
            int num_finalizada = 0;
            int num_mais10dias = 0;

            try 
            {
                Conexao_BD conexaoBD = new Conexao_BD();
                Connection conn = conexaoBD.conexao();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                
                if (rs.next()) 
                {
                    num_pepdidoRegisto = rs.getInt("num_pedidoRegisto");
                }
                
                rs.close();
                stmt.close();
                conn.close();
            } 
            catch (SQLException e) 
            {
                System.out.println("Erro ao consultar notificacoes: " + e.getMessage());
            }
            
            String sql2 = "SELECT count(pedido_remocao) as num_pedidoRemocao FROM utilizador WHERE pedido_remocao = true";
    
            try 
            {
                Conexao_BD conexaoBD = new Conexao_BD();
                Connection conn = conexaoBD.conexao();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql2);
                
                if (rs.next()) 
                {
                    num_pedidoRemocao = rs.getInt("num_pedidoRemocao");
    
                }
                
                rs.close();
                stmt.close();
                conn.close();
            } 
            catch (SQLException e) 
            {
                System.out.println("Erro ao consultar notificacoes: " + e.getMessage());
            }

            String sql3 = "SELECT COUNT(submetido_certificacao) AS num_pedidoCertificacao FROM equipamento WHERE submetido_certificacao = ?";

            try 
            {
                Connection conn = new Conexao_BD().conexao();
                PreparedStatement stmt = conn.prepareStatement(sql3);
                
                stmt.setString(1, "Submetido"); // Define o valor do parâmetro

                try (ResultSet rs = stmt.executeQuery()) 
                {
                    if (rs.next()) 
                    {
                        num_pedidoCertificacao = rs.getInt("num_pedidoCertificacao");
                    }
                }
            }
            catch (SQLException e) 
            {
                System.out.println("Erro ao consultar notificacoes: " + e.getMessage());
            }

            String sql4 = "SELECT COUNT(escolhido_certificacao) AS num_Negas FROM tecnico WHERE escolhido_certificacao = 'Negado pelo Tecnico'";

            try 
            {
                Connection conn = new Conexao_BD().conexao();
                PreparedStatement stmt = conn.prepareStatement(sql4);

                try (ResultSet rs = stmt.executeQuery()) 
                {
                    if (rs.next()) 
                    {
                        num_Negas = rs.getInt("num_Negas");
                    }
                }
            }
            catch (SQLException e) 
            {
                System.out.println("Erro ao consultar notificacoes: " + e.getMessage());
            }

            String sql5 = "SELECT COUNT(estado) AS finalizada FROM certificacao WHERE estado = 'finalizado'";

            try 
            {
                Connection conn = new Conexao_BD().conexao();
                PreparedStatement stmt = conn.prepareStatement(sql5);

                try (ResultSet rs = stmt.executeQuery()) 
                {
                    if (rs.next()) 
                    {
                        num_Negas = rs.getInt("finalizada");
                    }
                }
            }
            catch (SQLException e) 
            {
                System.out.println("Erro ao consultar notificacoes: " + e.getMessage());
            }
    
            String sql6 = "SELECT * FROM certificacao WHERE NOW() - data_realizacao > INTERVAL '10 days'";

            try 
            {
                Connection conn = new Conexao_BD().conexao();
                PreparedStatement stmt = conn.prepareStatement(sql6);

                try (ResultSet rs = stmt.executeQuery()) 
                {
                    if (rs.next()) 
                    {
                        num_mais10dias = rs.getInt("finalizada");
                    }
                }
            }
            catch (SQLException e) 
            {
                System.out.println("Erro ao consultar notificacoes: " + e.getMessage());
            }
            
            return num_pedidoRemocao + num_pepdidoRegisto + num_pedidoCertificacao + num_Negas + num_finalizada + num_mais10dias;
        }
        if(tipo.equals("Fabricante"))
        {

            String sql = "SELECT COUNT(submetido_certificacao) AS pedidos_negados FROM equipamento WHERE submetido_certificacao = 'Negado'";
            String sql2 = "SELECT COUNT(submetido_certificacao) AS finalizada FROM equipamento WHERE submetido_certificacao = 'terminado' AND id_utilizador = ?";

            int num_negados = 0; // Inicializar a variável corretamente
            int num_finalizada = 0; // Inicializar a variável corretamente

            try 
            {
                Conexao_BD conexaoBD = new Conexao_BD();
                Connection conn = conexaoBD.conexao();

                // Executar sql2 com PreparedStatement
                PreparedStatement stmt1 = conn.prepareStatement(sql2);
                stmt1.setInt(1, id_utilizador);
                ResultSet rs1 = stmt1.executeQuery();

                if (rs1.next()) 
                {
                    num_finalizada = rs1.getInt("finalizada"); // Nome correto da coluna
                }

                rs1.close();
                stmt1.close();
                conn.close(); // Fechar conexão
            } 
            catch (SQLException e) 
            {
                System.out.println("Erro ao consultar notificações: " + e.getMessage());
            }
            try 
            {
                Conexao_BD conexaoBD = new Conexao_BD();
                Connection conn = conexaoBD.conexao();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                if (rs.next()) 
                {
                    num_negados = rs.getInt("pedidos_negados"); // Nome correto da coluna
                }

                rs.close();
                stmt.close();
                conn.close(); // Fechar conexão
            } 
            catch (SQLException e) 
            {
                System.out.println("Erro ao consultar notificações: " + e.getMessage());
            }
            System.out.println(num_finalizada);
            return num_negados + num_finalizada;
        }
        if(tipo.equals("Tecnico"))
        {
            int escolhido = 0;
            String sql = "SELECT COUNT(escolhido_certificacao) AS escolhido FROM tecnico WHERE escolhido_certificacao = 'Escolhido' AND id_utilizador = ?";

            try {
                Conexao_BD conexaoBD = new Conexao_BD();
                Connection conn = conexaoBD.conexao();  
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id_utilizador);  // Substitui "?" pelo valor da variável de forma segura
                
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) 
                {
                    escolhido = rs.getInt("escolhido"); // Nome correto da coluna
                }

                rs.close();
                stmt.close();
                conn.close(); // Fechar conexão
            } 
            catch (SQLException e) 
            {
                System.out.println("Erro ao consultar notificações: " + e.getMessage());
            }

            return escolhido;
        }
        return 0;
    }

    /**
     * Metodo para listar as notificacoes pendentes
     * 
     * @author Guilherme Rodrigues
     */
    public static void listarNotificacoes() 
    {
        String sql = "SELECT * FROM utilizador WHERE estado_utilizador = false";
        String sql2 = "SELECT * FROM utilizador WHERE pedido_remocao = true";
        String sql3 = "SELECT * FROM equipamento WHERE submetido_certificacao = 'Submetido'";
        String sql4 = "SELECT * FROM tecnico WHERE escolhido_certificacao = 'Negado pelo Tecnico'";

        Scanner scanner = new Scanner(System.in);

        System.out.println("\n=== Notificação de Utilizadores ===");
        System.out.println("1. Utilizadores com pedido de registo:");
        System.out.println("2. Utilizadores com pedido de remoção de conta:");
        System.out.println("3. Equipamentos com pedido de certificação:");
        System.out.println("4. Utilizadores que recusaram participar da certificação:");
        System.out.println("5. Sair");
        System.out.println("Insira a opcao desejada: ");
        int option = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha
        switch (option)
        {
            case 1:
                try 
                {
                    Conexao_BD conexaoBD = new Conexao_BD();  // Criar instância
                    Connection conn = conexaoBD.conexao();  // Chamar o método
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);
                    System.out.println("\n=== Utilizadores ===");
                    while (rs.next()) 
                    {
                        int id_utilizador = rs.getInt("id_utilizador");
                        String username = rs.getString("username_utilizador");
                        String nome = rs.getString("nome_utilizador");
                        String email = rs.getString("email_utilizador");
                        String tipo = rs.getString("tipo");
                        boolean estado = rs.getBoolean("estado_utilizador");
                        System.out.println("ID: " + id_utilizador);
                        System.out.println("Nome: " + nome);
                        System.out.println("Username: " + username);
                        System.out.println("Email: " + email);  
                        System.out.println("Tipo: " + tipo);
                        System.out.println("Estado: " + estado);
                        System.out.println("------------------");
                    }
                    rs.close();
                    stmt.close();
                    conn.close();  // Fechar a conexão
                } 
                catch (SQLException e)
                {
                    System.out.println("Erro ao listar utilizadores: " + e.getMessage());
                }
                break;
            case 2:
                try 
                {
                    Conexao_BD conexaoBD = new Conexao_BD();  // Criar instância
                    Connection conn = conexaoBD.conexao();  // Chamar o método
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql2);
                    System.out.println("\n=== Utilizadores ===");
                    while (rs.next()) 
                    {
                        int id_utilizador = rs.getInt("id_utilizador");
                        String username = rs.getString("username_utilizador");
                        String nome = rs.getString("nome_utilizador");
                        String email = rs.getString("email_utilizador");
                        String tipo = rs.getString("tipo");
                        boolean estado = rs.getBoolean("estado_utilizador");
                        System.out.println("ID: " + id_utilizador);
                        System.out.println("Nome: " + nome);
                        System.out.println("Username: " + username);
                        System.out.println("Email: " + email);  
                        System.out.println("Tipo: " + tipo);
                        System.out.println("Estado: " + estado);
                        System.out.println("------------------");
                    }
                    rs.close();
                    stmt.close();
                    conn.close();  // Fechar a conexão
                } 
                catch (SQLException e)
                {
                    System.out.println("Erro ao listar utilizadores: " + e.getMessage());
                }
                break;
            case 3:
                try 
                {
                    Conexao_BD conexaoBD = new Conexao_BD();  // Criar instância
                    Connection conn = conexaoBD.conexao();  // Chamar o método
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql3);
                    System.out.println("\n=== Equipamentos ===");
                    while (rs.next()) 
                    {
                        int id_equipamento = rs.getInt("id_equipamento");
                        int id_utilizador = rs.getInt("id_utilizador");
                        String marca_equipamento = rs.getString("marca_equipamento");
                        String modelo_equipamento = rs.getString("modelo_equipamento");
                        System.out.println("ID: " + id_equipamento);
                        System.out.println("Marca10: " + marca_equipamento);
                        System.out.println("Modelo: " + modelo_equipamento);
                        System.out.println("Fabricante: " + id_utilizador);
                        System.out.println("------------------");
                    }
                    rs.close();
                    stmt.close();
                    conn.close();  // Fechar a conexão  
                } 
                catch (SQLException e)
                {
                    System.out.println("Erro ao listar equipamentos: " + e.getMessage());
                }
                break;
            case 4:
                try 
                {
                    Conexao_BD conexaoBD = new Conexao_BD();  // Criar instância
                    Connection conn = conexaoBD.conexao();  // Chamar o método
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql4);
                    System.out.println("\n=== Tecnicos ===");
                    while (rs.next())
                    {
                        int id_utilizador = rs.getInt("id_utilizador");
                        String username = rs.getString("username_utilizador");
                        System.out.println("ID: " + id_utilizador);
                        System.out.println("Username: " + username);
                        System.out.println("------------------");
                    }
                    rs.close();
                    stmt.close();
                    conn.close();  // Fechar a conexão
                }
                catch (SQLException e)
                {
                    System.out.println("Erro ao listar tecnicos: " + e.getMessage());
                }
                break;
            case 5:
                return; // Sai do método e volta para o menu anterior
            default:
                System.out.println("Opção inválida. Tente novamente.");
        }
                    
    }

    /**
     * Metodo para verificar se o primeiro registo ja foi feito
     * 
     * @author Guilherme Rodrigues
     */
    public static void primeiroRegisto() 
    {
        String sql = "SELECT COUNT(id_utilizador) as num_utilizadores FROM utilizador";
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();  // Criar instância
            Connection conn = conexaoBD.conexao();  // Chamar o método
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("\n=== Utilizadores ===");
            if (rs.next()) 
            {
                int num_utilizadores = rs.getInt("num_utilizadores");
                if (num_utilizadores == 0) 
                {
                    System.out.println("Primeiro Registo");
                    registaGestor();
                }
            }
            rs.close();
            stmt.close();
            conn.close();  // Fechar a conexão
        } 
        catch (SQLException e)
        {
            System.out.println("Erro ao listar utilizadores: " + e.getMessage());
        }
    }

    /**
     * Este método registra um novo equipamento na base de dados.
     * 
     * @author Guilherme Rodrigues
     * 
     * @param id_utilizador O ID do utilizador que registou o equipamento
     */
    public static void registaEquipamento(int id_utilizador) 
    {
        Scanner scanner = new Scanner(System.in);
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            
            String sql = "INSERT INTO equipamento (id_utilizador, marca_equipamento, modelo_equipamento, sector_comercial_equipamento, " +
                        "potencia, amperagem, sku_equipamento, n_modelo, data_submissao, submetido_certificacao) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            System.out.println("\n=== Inserir Novo Equipamento ===");

            //System.out.print("ID Certificação: ");
            //int id_certificacao = scanner.nextInt();
            //scanner.nextLine(); // Consumir quebra de linha
            
            System.out.print("Marca do Equipamento: ");
            String marca = scanner.nextLine();
            
            System.out.print("Modelo do Equipamento: ");
            String modelo = scanner.nextLine();
            
            System.out.print("Setor Comercial do Equipamento: ");
            String setor = scanner.nextLine();
            
            System.out.print("Potência: ");
            float potencia = scanner.nextFloat();
            
            System.out.print("Amperagem: ");
            float amperagem = scanner.nextFloat();
            
            Random rand = new Random();
            int n_equipamento = rand.nextInt(1000000) + 1;
            System.out.print("Codigo SKU Gerado: " + n_equipamento);
            
            System.out.print("\nNúmero do Modelo: ");
            int n_modelo = scanner.nextInt();
            scanner.nextLine(); // Consumir quebra de linha
            
            LocalDate dataAtual = LocalDate.now();
            java.sql.Date dataSubmissaoSQL = java.sql.Date.valueOf(dataAtual);
            // Converter string para Date
            System.out.print("Data de Submissão: " + dataSubmissaoSQL);
            
            //System.out.print("\nData de Certificação (YYYY-MM-DD): ");
            //String dataCertificacao = scanner.nextLine();
            // Converter string para Date
            //java.sql.Date dataCertificacaoSQL = java.sql.Date.valueOf(dataCertificacao);
            
            //stmt.setInt(1, id_certificacao);
            stmt.setInt(1, id_utilizador);
            stmt.setString(2, marca);
            stmt.setString(3, modelo);
            stmt.setString(4, setor);
            stmt.setFloat(5, potencia);
            stmt.setFloat(6, amperagem);
            stmt.setInt(7, n_equipamento);
            stmt.setInt(8, n_modelo);
            stmt.setDate(9, dataSubmissaoSQL);
            //stmt.setDate(10, dataCertificacaoSQL);
            stmt.setString(10, "Nao submetido");
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("\nEquipamento inserido com sucesso!");
            } else {
                System.out.println("\nFalha ao inserir equipamento.");
            }
            
            stmt.close();
            conn.close();
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao inserir equipamento: " + e.getMessage());
        }
        catch (IllegalArgumentException e) 
        {
            System.out.println("Erro no formato de data. Use o formato YYYY-MM-DD.");
        }
    }

    /**
     * Este método registra um novo parâmetro na base de dados.
     * 
     * @author Guilherme Rodrigues
     */
    public static void inserirCertificacao(int id_equipamento, int id_utilizador) 
    {
        Scanner scanner = new Scanner(System.in);

        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();

            String sql = "SELECT * FROM equipamento WHERE id_equipamento = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_equipamento);
            ResultSet rs = stmt.executeQuery();

            if (!rs.next())
            {
                System.out.println("Nenhum pedido de certificacao para este equipamento.");
                return;
            }
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao verificar equipamento: " + e.getMessage());
        }    
        
        try 
        {    
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();

            System.out.println("Deseja participar desta certificação? (S/N)");
            String resposta = scanner.nextLine();

            if (resposta.equalsIgnoreCase("S"))
            {
                System.out.println("\n=== Inserir uma Nova Certificação ===");
                // Obter a data atual automaticamente
                LocalDate dataAtual = LocalDate.now();
                java.sql.Date dataCertificacaoSQL2 = java.sql.Date.valueOf(dataAtual);

        
                System.out.print("Insira as Observações Gerais: ");
                String observacoesGenericas = scanner.nextLine();
        
                System.out.print("Insira o Custo da Certificação: ");
                float custoCertificacao = scanner.nextFloat();

                String sql1 = "INSERT INTO certificacao (id_utilizador, data_realizacao, observacoes_genericas, custo_certificacao, estado) " +
                                    "VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql1);
                stmt.setInt(1, id_utilizador);
                stmt.setDate(2, dataCertificacaoSQL2);
                stmt.setString(3, observacoesGenericas);
                stmt.setFloat(4, custoCertificacao);
                stmt.setString(5, "aceite");

                stmt.executeUpdate();
                stmt.close();

                String sql2 = "SELECT MAX(id_certificacao) FROM certificacao";
                PreparedStatement stmt2 = conn.prepareStatement(sql2);
                ResultSet rs = stmt2.executeQuery();

                int id_certificacao = 0;

                if (rs.next())
                {
                    id_certificacao = rs.getInt(1);
                }

                if(id_certificacao == 0)
                {
                    System.out.println("\nNao foi possivel introduzir a certificacao!");
                }

                String sql3 = "UPDATE equipamento SET id_certificacao = ? WHERE id_equipamento = ?";
                PreparedStatement stmt3 = conn.prepareStatement(sql3);
                stmt3.setInt(1, id_certificacao);
                stmt3.setInt(2, id_equipamento);
                stmt3.executeUpdate();
                stmt3.close();

                String sql4 = "UPDATE tecnico SET escolhido_certificacao = 'Aceite pelo Tecnico' WHERE id_utilizador = ?";
                PreparedStatement stmt4 = conn.prepareStatement(sql4);
                stmt4.setInt(1, id_utilizador);
                stmt4.executeUpdate();
                
                System.out.println("\nCertificação inserida com sucesso!");
                
                conn.close();
            }
            else
            {
                System.out.println("\nGestor Notificado com Sucesso!");
                String sql4 = "UPDATE tecnico SET escolhido_certificacao = 'Negado pelo Tecnico' WHERE id_utilizador = ?";
                PreparedStatement stmt4 = conn.prepareStatement(sql4);
                stmt4.setInt(1, id_utilizador);
                stmt4.executeUpdate();

                String sql5 = "UPDATE equipamento SET submetido_certificacao = 'Submetido' WHERE id_equipamento = ?";
                PreparedStatement stmt5 = conn.prepareStatement(sql5);
                stmt5.setInt(1, id_equipamento);
                stmt5.executeUpdate();

                stmt5.close();
                stmt4.close();
                conn.close();
            }

        }    
        catch (SQLException e) 
        {
            System.out.println("Erro ao inserir dados: " + e.getMessage());
        }

    }
    
    /**
     * Este método registra um novo parâmetro na base de dados associado a uma certificação.
     * 
     * @author Guilherme Rodrigues
     * 
     */
    public static void inserirResultadosCertificacao(int id_certificacao, int id_utilizador) 
    {
        Scanner scanner = new Scanner(System.in);
        
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
    
            // Verificar se o id_certificacao pertence ao id_utilizador
            String sql = "SELECT id_certificacao FROM certificacao WHERE id_certificacao = ? AND id_utilizador = ? AND estado != 'finalizado' AND estado != 'arquivado'";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_certificacao);
            stmt.setInt(2, id_utilizador);
            ResultSet rs = stmt.executeQuery();
    
            if (!rs.next()) 
            { // Se não encontrar o id_certificacao
                System.out.println("Não está associado a essa certificação!");
                stmt.close();
                conn.close();
                return; // Sai da função
            }
            stmt.close();
    
            // Inserção dos parâmetros
            System.out.println("\n=== Inserir Resultados dos Parâmetros ===");
            System.out.print("Insira a descrição dos Parâmetros: ");
            String descricao_parametros = scanner.nextLine(); 
            
            System.out.print("Insira o Valor do Parâmetro: ");
            float valor_parametro = scanner.nextFloat();
            scanner.nextLine(); // 🔹 Adicionado para limpar o buffer
            
            LocalDate dataAtual = LocalDate.now();
            java.sql.Date dataCertificacaoSQL2 = java.sql.Date.valueOf(dataAtual);
            
            // Inserir os valores na tabela 'parametros'
            String sql2 = "INSERT INTO parametros (descricao_parametros, valor_medido, data_parametro) VALUES (?, ?, ?)";
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt2.setString(1, descricao_parametros); 
            stmt2.setFloat(2, valor_parametro);
            stmt2.setDate(3, dataCertificacaoSQL2); 
            stmt2.executeUpdate();
            stmt2.close();
    
            // Atualizar o estado da certificação para "decorrer"
            String sql3 = "UPDATE certificacao SET estado = 'decorrer', id_parametros = (SELECT COALESCE(MAX(id_parametros), 0) FROM parametros) WHERE id_certificacao = ?";

            PreparedStatement stmt3 = conn.prepareStatement(sql3);
            stmt3.setInt(1, id_certificacao);
            stmt3.executeUpdate();
            stmt3.close();

            boolean licencaValida = false;

            while (!licencaValida) 
            {
                System.out.println("Insira o nome da licença que pretende utilizar: ");
                String nome_licenca = scanner.nextLine();
            
                // Verificar se a licença existe
                String sqlCheck = "SELECT COUNT(*) FROM licencas WHERE nome_licenca = ?";
                PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck);
                stmtCheck.setString(1, nome_licenca);
                ResultSet rsCheck = stmtCheck.executeQuery();
            
                if (rsCheck.next() && rsCheck.getInt(1) > 0) 
                {
                    // Se a licença existir, faz a atualização
                    String sql4 = "UPDATE licencas SET id_certificacao = ? WHERE nome_licenca = ?";
                    PreparedStatement stmt4 = conn.prepareStatement(sql4);
                    stmt4.setInt(1, id_certificacao);
                    stmt4.setString(2, nome_licenca);
                    stmt4.executeUpdate();
                    stmt4.close();
                    licencaValida = true; // Sai do loop
                } 
                else 
                {
                    System.out.println("Licença não encontrada! Tente novamente.");
                }
            
                stmtCheck.close();
            }
    
            System.out.println("\nParâmetros inseridos com sucesso!");
    
            
            conn.close();
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao registar os parâmetros: " + e.getMessage());
        }
    }

    /** 
     * Metodo para realizar um pedido de remoção de conta
     * 
     * @author Guilherme Rodrigues
     */
    public static void pedidoRemocaoUtilizador(int id_utilizador)
    {
        String sql = "UPDATE utilizador SET  Pedido_Remocao = ? WHERE id_utilizador = ?";
        
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setBoolean(1, true);
            stmt.setInt(2, id_utilizador);
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) 
            {
                System.out.println("\n Pedido de Remoção Enviado!");
            } 
            else 
            {
                System.out.println("\nNenhum utilizador encontrado com o ID " + id_utilizador);
            }
            
            stmt.close();
            conn.close();
        } 
        catch (SQLException e) {
            System.out.println("Erro ao atualizar utilizador: " + e.getMessage());
        }
    }

    /**
     * Metodo para realizar um pedido de certificação
     * 
     * @author Guilherme Rodrigues
     */
    public static void realizarPedidoCertificacao()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n=== Realizar Pedido de Certificação ===");
        System.out.print("Insira o ID do Equipamento: ");
        int id_equipamento = scanner.nextInt();

        String submetido = "Submetido";
    
        String sql = "UPDATE equipamento SET submetido_certificacao = ? WHERE id_equipamento = ?";
        
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setString(1, submetido);  // Define o novo valor de submetido_certificacao
            stmt.setInt(2, id_equipamento); // Define o ID do equipamento
    
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) 
            {
                System.out.println("\nPedido de Certificação Submetido com sucesso!");
            } 
            else 
            {
                System.out.println("\nNenhum equipamento encontrado com o ID " + id_equipamento);
            }
            
            stmt.close();
            conn.close();
        } 
        catch (SQLException e) {
            System.out.println("Erro ao atualizar utilizador: " + e.getMessage());
        }

    }
    
    /**
     * Metodo para responder ao pedido de certificação
     * 
     * @author Guilherme Rodrigues
     */
    public static void respostaPedidoCertificacao()
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n=== Resposta ao Pedido de Certificação ===");
        System.out.print("Insira o ID do Equipamento: ");
        int id_equipamento = scanner.nextInt();
        System.out.println("\nDeseja aceitar o pedido de certificação? (S/N)");
        String resposta = scanner.next();
        
        String sql = "UPDATE equipamento SET submetido_certificacao = false WHERE id_equipamento = ?";
        
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            
            stmt.setInt(1, id_equipamento);
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) 
            {
                System.out.println("\nResposta ao Pedido de Certificação Enviada!");
            } 
            else 
            {
                System.out.println("\nNenhum equipamento encontrado com o ID " + id_equipamento);
            }
            
            stmt.close();
            conn.close();
        } 
        catch (SQLException e) {
            System.out.println("Erro ao atualizar utilizador: " + e.getMessage());
        }
    }
    
    /**
     * Metodo para listar os equipamentos na base de dados
     * 
     * @author Guilherme Rodrigues
     */  
     public static void listarEquipamentos() 
    {
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();

            Scanner scanner = new Scanner(System.in);
            System.out.println("Deseja ordenar por marca (1) ou código (2)? ");
            int escolhaOrdenacao = scanner.nextInt();

            String coluna;
            if(escolhaOrdenacao == 1) 
            {
                coluna = "marca_equipamento";
            } 
            else
            {
                coluna = "id_equipamento";
            }

            String sql = "SELECT * FROM equipamento ORDER BY " + coluna;

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("\n=== Equipamentos ===");
            
            while (rs.next()) 
            {
                int id_equipamento = rs.getInt("id_equipamento");
                int id_certificacao = rs.getInt("id_certificacao");
                int id_utilizador = rs.getInt("id_utilizador");
                String marca_equipamento = rs.getString("marca_equipamento");
                String modelo_equipamento = rs.getString("modelo_equipamento");
                String setor_comercial = rs.getString("sector_comercial_equipamento");
                float potencia = rs.getFloat("potencia");
                float amperagem = rs.getFloat("amperagem");
                int sku_equipamento = rs.getInt("sku_equipamento");
                int n_modelo = rs.getInt("n_modelo");
                String data_submissao = rs.getString("data_submissao");
                String submetido_certificacao = rs.getString("submetido_certificacao");

                System.out.println("ID Equipamento: " + id_equipamento);
                System.out.println("ID Certificação: " + id_certificacao);
                System.out.println("ID Utilizador: " + id_utilizador);
                System.out.println("Marca do Equipamento: " + marca_equipamento);
                System.out.println("Modelo do Equipamento: " + modelo_equipamento);
                System.out.println("Setor Comercial do Equipamento: " + setor_comercial);
                System.out.println("Potência do Equipamento: " + potencia);
                System.out.println("Amperagem do Equipamento: " + amperagem);
                System.out.println("SKU do Equipamento: " + sku_equipamento);
                System.out.println("N° do Modelo do Equipamento: " + n_modelo);
                System.out.println("Data de Submissão do Equipamento: " + data_submissao);
                System.out.println("Submetido para Certificação: " + submetido_certificacao);
                System.out.println("====================================");
            }
            rs.close();
            stmt.close();
            conn.close();  // Fechar a conexão
        } 
        catch (SQLException e)
        {
            System.out.println("Erro ao listar equipamentos: " + e.getMessage());
        }
    }
    
    /**
     * Metodo para pesquisar um equipamento na base de dados
     * 
     * @author Guilherme Rodrigues
     */
     public static void pesquisarEquipamento(String tipo, int id_utilizador1) 
    {
        if(tipo.equals("Gestor"))
        {
            try 
            {
                Conexao_BD conexaoBD = new Conexao_BD();
                Connection conn = conexaoBD.conexao();
    
                Scanner scanner = new Scanner(System.in);
                System.out.println("Deseja pesquisar por marca (1) ou código (2)? ");
                int escolhaOrdenacao = scanner.nextInt();
                scanner.nextLine(); // Consume newline
    
                PreparedStatement pstmt;
                ResultSet rs;
    
                if(escolhaOrdenacao == 1) 
                {
                    System.out.print("Insira a marca do equipamento: ");
                    String marca_equipamento = scanner.nextLine();
                    String sql = "SELECT * FROM equipamento WHERE marca_equipamento LIKE ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, marca_equipamento);
                } 
                else
                {
                    System.out.print("Insira a id do equipamento: ");
                    int id_equipamento = scanner.nextInt();
                    String sql = "SELECT * FROM equipamento WHERE id_equipamento = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, id_equipamento);
                }
    
                rs = pstmt.executeQuery();
                System.out.println("\n=== Equipamentos ===");
                
                boolean encontrado = false;
                while (rs.next()) 
                {
                    encontrado = true;
                    int id_equipamento = rs.getInt("id_equipamento");
                    int id_certificacao = rs.getInt("id_certificacao");
                    int id_utilizador = rs.getInt("id_utilizador");
                    String marca_equipamento = rs.getString("marca_equipamento");
                    String modelo_equipamento = rs.getString("modelo_equipamento");
                    String setor_comercial = rs.getString("sector_comercial_equipamento");
                    float potencia = rs.getFloat("potencia");
                    float amperagem = rs.getFloat("amperagem");
                    int sku_equipamento = rs.getInt("sku_equipamento");
                    int n_modelo = rs.getInt("n_modelo");
                    String data_submissao = rs.getString("data_submissao");
                    String submetido_certificacao = rs.getString("submetido_certificacao");
    
                    System.out.println("ID Equipamento: " + id_equipamento);
                    System.out.println("ID Certificação: " + id_certificacao);
                    System.out.println("ID Utilizador: " + id_utilizador);
                    System.out.println("Marca do Equipamento: " + marca_equipamento);
                    System.out.println("Modelo do Equipamento: " + modelo_equipamento);
                    System.out.println("Setor Comercial do Equipamento: " + setor_comercial);
                    System.out.println("Potência do Equipamento: " + potencia);
                    System.out.println("Amperagem do Equipamento: " + amperagem);
                    System.out.println("SKU do Equipamento: " + sku_equipamento);
                    System.out.println("N° do Modelo do Equipamento: " + n_modelo);
                    System.out.println("Data de Submissão do Equipamento: " + data_submissao);
                    System.out.println("Submetido para Certificação: " + submetido_certificacao);
                    System.out.println("====================================");
                }
    
                if (!encontrado) {
                    System.out.println("Nenhum equipamento encontrado.");
                }
    
                // Fechar recursos
                rs.close();
                pstmt.close();
                conn.close();
            } 
            catch (SQLException e)
            {
                System.out.println("Erro ao pesquisar equipamentos: " + e.getMessage());
            }
        }
        else
        {
            try 
            {
                Conexao_BD conexaoBD = new Conexao_BD();
                Connection conn = conexaoBD.conexao();
    
                Scanner scanner = new Scanner(System.in);
                System.out.println("Deseja pesquisar por marca (1) ou código (2)? ");
                int escolhaOrdenacao = scanner.nextInt();
                scanner.nextLine(); // Consume newline
    
                PreparedStatement pstmt;
                ResultSet rs;
    
                if(escolhaOrdenacao == 1) 
                {
                    System.out.print("Insira a marca do equipamento: ");
                    String marca_equipamento = scanner.nextLine();
                    String sql = "SELECT * FROM equipamento WHERE marca_equipamento LIKE ? AND id_utilizador = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, marca_equipamento);
                    pstmt.setInt(2, id_utilizador1);
                } 
                else
                {
                    System.out.print("Insira a id do equipamento: ");
                    int id_equipamento = scanner.nextInt();
                    String sql = "SELECT * FROM equipamento WHERE id_equipamento = ? AND id_utilizador = ?";
                    pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, id_equipamento);
                    pstmt.setInt(2, id_utilizador1);
                }
    
                rs = pstmt.executeQuery();
                System.out.println("\n=== Equipamentos ===");
                
                boolean encontrado = false;
                while (rs.next()) 
                {
                    encontrado = true;
                    int id_equipamento = rs.getInt("id_equipamento");
                    int id_certificacao = rs.getInt("id_certificacao");
                    int id_utilizador = rs.getInt("id_utilizador");
                    String marca_equipamento = rs.getString("marca_equipamento");
                    String modelo_equipamento = rs.getString("modelo_equipamento");
                    String setor_comercial = rs.getString("sector_comercial_equipamento");
                    float potencia = rs.getFloat("potencia");
                    float amperagem = rs.getFloat("amperagem");
                    int sku_equipamento = rs.getInt("sku_equipamento");
                    int n_modelo = rs.getInt("n_modelo");
                    String data_submissao = rs.getString("data_submissao");
                    String submetido_certificacao = rs.getString("submetido_certificacao");
    
                    System.out.println("ID Equipamento: " + id_equipamento);
                    System.out.println("ID Certificação: " + id_certificacao);
                    System.out.println("ID Utilizador: " + id_utilizador);
                    System.out.println("Marca do Equipamento: " + marca_equipamento);
                    System.out.println("Modelo do Equipamento: " + modelo_equipamento);
                    System.out.println("Setor Comercial do Equipamento: " + setor_comercial);
                    System.out.println("Potência do Equipamento: " + potencia);
                    System.out.println("Amperagem do Equipamento: " + amperagem);
                    System.out.println("SKU do Equipamento: " + sku_equipamento);
                    System.out.println("N° do Modelo do Equipamento: " + n_modelo);
                    System.out.println("Data de Submissão do Equipamento: " + data_submissao);
                    System.out.println("Submetido para Certificação: " + submetido_certificacao);
                    System.out.println("====================================");
                }
    
                if (!encontrado) {
                    System.out.println("Nenhum equipamento encontrado.");
                }
    
                // Fechar recursos
                rs.close();
                pstmt.close();
                conn.close();
            } 
            catch (SQLException e)
            {
                System.out.println("Erro ao pesquisar equipamentos: " + e.getMessage());
            }
        }
    }

    /**
     * Metodo para aceitar um pedido de certificação na base de dados
     * 
     * @author Guilherme Rodrigues
     */
    public static void aceitarPedidoCertificacao()
    {
        String sql4 = "SELECT submetido_certificacao FROM equipamento WHERE submetido_certificacao = 'Submetido'";
        
        try
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql4);
            
            if (!rs.next()) 
            {
                System.out.println("Nenhum pedido de certificação pendente.");
                return;
            }
        }
        catch (SQLException e)
        {
            System.out.println("Erro ao aceitar pedido de certificação: " + e.getMessage());
        }

        System.out.println("\n=== Aceitar Pedido de Certificação ===");
        System.out.println("Insira o ID do Equipamento: ");
        Scanner scanner = new Scanner(System.in);
        int id_equipamento = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        System.out.println("Deseja aceitar o pedido de certificação? (S/N)");
        String resposta = scanner.nextLine();
        
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            
            String sql = "UPDATE equipamento SET submetido_certificacao = ? WHERE id_equipamento = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            if(resposta.equalsIgnoreCase("S")) 
            {
                pstmt.setString(1, "Aceite");
                
                String sql2 = "SELECT u.id_utilizador, u.nome_utilizador, t.nivel_certificacao, t.escolhido_certificacao  FROM utilizador u JOIN tecnico t ON u.id_utilizador = t.id_utilizador WHERE u.tipo = 'Tecnico' AND (escolhido_certificacao IS NULL OR escolhido_certificacao <> 'Negado pelo Tecnico')";
                PreparedStatement stmt = conn.prepareStatement(sql2);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) 
                {
                    System.out.println("\n=== Selecione o Técnico ===");
                    System.out.println("ID: " + rs.getString("id_utilizador"));
                    System.out.println("Nome: " + rs.getString("nome_utilizador"));
                    System.out.println("Nivel de Certificação: " + rs.getString("nivel_certificacao"));
                } 
                else 
                {
                    System.out.println("Nenhuma informação adicional encontrada para Técnico.");
                }

                rs.close();
                stmt.close();

                System.out.println("Insira o ID do Técnico: ");
                int id_tecnico = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                String sql3 = "UPDATE tecnico SET escolhido_certificacao = 'Escolhido' WHERE id_utilizador = ?";
                PreparedStatement stmt2 = conn.prepareStatement(sql3);
                stmt2.setInt(1, id_tecnico);
                stmt2.executeUpdate();
                stmt2.close();

            } 
            else
            {
                pstmt.setString(1, "Negado");
            }
            pstmt.setInt(2, id_equipamento);
            
            int linhasAfetadas = pstmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                System.out.println("Pedido de certificação atualizado com sucesso!");
            } else {
                System.out.println("Nenhum equipamento encontrado com o ID " + id_equipamento);
            }
            
            // Fechar recursos
            pstmt.close();
            conn.close();
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao aceitar pedido de certificação: " + e.getMessage());
        }
    }

    /**
     * Metodo para listar os pedidos de certificação pendentes na base de dados
     * 
     * @author Guilherme Rodrigues
     */
    public static void listarNotificacoesFabricante() 
    {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n=== Notificação do Fabricante ===");
        System.out.println("1. Pedidos de certificação negados.");
        System.out.println("2. Pedidos de certificação Terminados.");
        System.out.println("3. Sair");
        System.out.print("Escolha uma opção: ");
        
        int option = scanner.nextInt();
        scanner.nextLine(); // Consumir a quebra de linha

        switch (option) 
        {
            case 1:
                String sql = "SELECT id_certificacao, id_equipamento FROM equipamento WHERE submetido_certificacao = 'Negado'";
                try 
                {
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);
                    
                    if (!rs.isBeforeFirst()) 
                    {
                        System.out.println("Nenhum pedido de certificação pendente.");
                        return;
                    }
                    
                    while (rs.next()) 
                    {
                        int id_certificacao = rs.getInt("id_certificacao");
                        int id_equipamento = rs.getInt("id_equipamento");
                        System.out.println("O pedido de Certificação do equipamento " + id_equipamento + " foi negado por um gestor");
                    }
                } 
                catch (SQLException e) 
                {
                    System.out.println("Erro ao listar pedidos de certificação: " + e.getMessage());
                } 
                break;

            case 2:
                String sql2 = "SELECT id_certificacao, id_equipamento FROM equipamento WHERE submetido_certificacao = 'terminado'";
                try 
                {
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql2);
                    
                    if (!rs.isBeforeFirst()) 
                    {
                        System.out.println("Nenhum pedido de certificação terminado.");
                        return;
                    }
                    
                    while (rs.next()) 
                    {
                        int id_certificacao = rs.getInt("id_certificacao");
                        int id_equipamento = rs.getInt("id_equipamento");
                        System.out.println("O pedido de Certificação do equipamento " + id_equipamento + " foi Terminado");
                    }
                } 
                catch (SQLException e) 
                {
                    System.out.println("Erro ao listar pedidos de certificação: " + e.getMessage());
                }
                break;

            case 3:
                break;

            default:
                System.out.println("Opção inválida.");
                

        }
        
    }  

    /**
     * Metodo para listar os pedidos de certificação pendentes na base de dados
     * 
     * @author Guilherme Rodrigues
     * 
     * @param int num
     */
    public static void listarNotificacoesTecnico(int num) 
    {
        System.out.println("Foi selecionado para realizar " + num + " certificação");
    }

    /**
     * Metodo para listar os pedidos de certificação pendentes na base de dados
     * 
     * @author Guilherme Rodrigues
     * 
     * @param String tipo
     */
    public static void listarCertificacao(String tipo)
    {
        if(tipo.equalsIgnoreCase("Gestor"))
        {
            
            System.out.println("Deseja ordenar por data (1) ou por numero de certificação (2)? ");
            int escolhaOrdenacao = new Scanner(System.in).nextInt();
    
            if(escolhaOrdenacao == 1)
            {
                String sql = "SELECT c.id_certificacao, e.id_equipamento, c.estado, data_realizacao FROM certificacao c JOIN equipamento e ON c.id_certificacao = e.id_certificacao ORDER BY data_realizacao";
                try 
                {
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);
                    
                    if (!rs.isBeforeFirst()) 
                    {
                        System.out.println("Nenhum pedido de certificação pendente.");
                        return;
                    }
                    
                    while (rs.next()) 
                    {
                        int id_certificacao = rs.getInt("id_certificacao");
                        int id_equipamento = rs.getInt("id_equipamento");
                        String estado = rs.getString("estado");
                        System.out.println("O pedido de Certificação do equipamento " + id_equipamento + " foi " + estado);
                    }
                } 
                catch (SQLException e) 
                {
                    System.out.println("Erro ao listar pedidos de certificação: " + e.getMessage());
                }
            }
            else
            {
                String sql = "SELECT c.id_certificacao, e.id_equipamento, c.estado, data_realizacao FROM certificacao c JOIN equipamento e ON c.id_certificacao = e.id_certificacao ORDER BY id_certificacao";
                try 
                {
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);
                    
                    if (!rs.isBeforeFirst()) 
                    {
                        System.out.println("Nenhum pedido de certificação pendente.");
                        return;
                    }
                    
                    while (rs.next()) 
                    {
                        int id_certificacao = rs.getInt("id_certificacao");
                        int id_equipamento = rs.getInt("id_equipamento");
                        String estado = rs.getString("estado");
                        System.out.println("O pedido de Certificação do equipamento " + id_equipamento + " foi " + estado);
                    }
                } 
                catch (SQLException e) 
                {
                    System.out.println("Erro ao listar pedidos de certificação: " + e.getMessage());
                }
            }
        }
        else if(tipo.equalsIgnoreCase("Fabricante"))
        {
            System.out.println("Deseja ordenar por data (1) ou por numero de certificação (2)? ");
            int escolhaOrdenacao = new Scanner(System.in).nextInt();
    
            if(escolhaOrdenacao == 1)
            {
                String sql = "SELECT c.id_certificacao, e.id_equipamento, c.estado, data_realizacao FROM certificacao c JOIN equipamento e ON c.id_certificacao = e.id_certificacao ORDER BY data_realizacao";
                try 
                {
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);
                    
                    if (!rs.isBeforeFirst()) 
                    {
                        System.out.println("Nenhum pedido de certificação pendente.");
                        return;
                    }
                    
                    while (rs.next()) 
                    {
                        int id_certificacao = rs.getInt("id_certificacao");
                        int id_equipamento = rs.getInt("id_equipamento");
                        String estado = rs.getString("estado");
                        System.out.println("O pedido de Certificação do equipamento " + id_equipamento + " foi " + estado);
                    }
                } 
                catch (SQLException e) 
                {
                    System.out.println("Erro ao listar pedidos de certificação: " + e.getMessage());
                }
            }
            else
            {
                String sql = "SELECT c.id_certificacao, e.id_equipamento, c.estado, data_realizacao FROM certificacao c JOIN equipamento e ON c.id_certificacao = e.id_certificacao ORDER BY id_certificacao";
                try 
                {
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(sql);
                    
                    if (!rs.isBeforeFirst()) 
                    {
                        System.out.println("Nenhum pedido de certificação pendente.");
                        return;
                    }
                    
                    while (rs.next()) 
                    {
                        int id_certificacao = rs.getInt("id_certificacao");
                        int id_equipamento = rs.getInt("id_equipamento");
                        String estado = rs.getString("estado");
                        System.out.println("O pedido de Certificação do equipamento " + id_equipamento + " foi " + estado);
                    }
                } 
                catch (SQLException e) 
                {
                    System.out.println("Erro ao listar pedidos de certificação: " + e.getMessage());
                }
            }
        }
    }   
    
    /**
     * Metodo para listar os pedidos de certificação pendentes na base de dados
     * 
     * @author Guilherme Rodrigues
     */
    public static void pesquiarCertificacao()
    {
        System.out.println("Deseja pesquisar todas as certificacoes que nao foram terminadas (s/n)?");
        String terminado = new Scanner(System.in).nextLine();

        if(terminado.equalsIgnoreCase("s"))
        {
            String sql = "SELECT e.id_equipamento, c.id_certificacao FROM equipamento e JOIN certificacao c ON e.id_certificacao = c.id_certificacao WHERE estado != 'Arquivado' AND estado != 'Finalizado'";
            try 
            {
                Conexao_BD conexaoBD = new Conexao_BD();
                Connection conn = conexaoBD.conexao();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                
                if (!rs.isBeforeFirst()) 
                {
                    System.out.println("Nenhum pedido de certificação econtrado.");
                    return;
                }
                
                while (rs.next()) 
                {
                    int id_certificacao = rs.getInt("id_certificacao");
                    int id_equipamento = rs.getInt("id_equipamento");
                    System.out.println("O pedido de Certificação ID: " + id_certificacao + " do equipamento " + id_equipamento);
                }
            } 
            catch (SQLException e) 
            {
                System.out.println("Erro ao listar pedidos de certificação: " + e.getMessage());
            }
        }
        else
        {
            System.out.println("Deseja procurar por: ");
            System.out.println("1. Estado");
            System.out.println("2. Numero");
            System.out.println("3. Fabricante");
            System.out.println("4. Periodo Temporal");

            int escolhaOrdenacao = new Scanner(System.in).nextInt();

            switch (escolhaOrdenacao) 
            {
                case 1:
                    System.out.println("Insira o estado: ");
                    String estado = new Scanner(System.in).nextLine();
                    String sql = "SELECT e.id_equipamento, c.id_certificacao FROM equipamento e JOIN certificacao c ON e.id_certificacao = c.id_certificacao WHERE estado = ?";
                    try 
                    {
                        Conexao_BD conexaoBD = new Conexao_BD();
                        Connection conn = conexaoBD.conexao();
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setString(1, estado);
                        ResultSet rs = stmt.executeQuery();
                        
                        if (!rs.isBeforeFirst()) 
                        {
                            System.out.println("Nenhum pedido de certificação econtrado.");
                            return;
                        }
                        
                        while (rs.next()) 
                        {
                            int id_certificacao = rs.getInt("id_certificacao");
                            int id_equipamento = rs.getInt("id_equipamento");
                            System.out.println("O pedido de Certificação do equipamento " + id_equipamento);
                        }
                    } 
                    catch (SQLException e) 
                    {
                        System.out.println("Erro ao listar pedidos de certificação: " + e.getMessage());
                    }
                    break;
                case 2:
                    System.out.println("Insira o numero: ");
                    int numero = new Scanner(System.in).nextInt();
                    String sql2 = "SELECT c.id_certificacao, e.id_equipamento, estado FROM certificacao c JOIN equipamento e ON c.id_certificacao = e.id_certificacao WHERE c.id_certificacao = ?";
                    try 
                    {
                        Conexao_BD conexaoBD = new Conexao_BD();
                        Connection conn = conexaoBD.conexao();
                        PreparedStatement stmt = conn.prepareStatement(sql2);
                        stmt.setInt(1, numero);
                        ResultSet rs = stmt.executeQuery();
                        
                        if (!rs.isBeforeFirst()) 
                        {
                            System.out.println("Nenhum pedido de certificação econtrado.");
                            return;
                        }
                        
                        while (rs.next()) 
                        {
                            String estado2 = rs.getString("estado");
                            int id_certificacao = rs.getInt("id_certificacao");
                            int id_equipamento = rs.getInt("id_equipamento");
                            System.out.println("O pedido de Certificação #" + id_certificacao + " do equipamento " + id_equipamento + " foi " + estado2);
                        }
                    } 
                    catch (SQLException e) 
                    {
                        System.out.println("Erro ao listar pedidos de certificação: " + e.getMessage());
                    }
                    break;
                        
                
                case 3:
                    System.out.println("Insira o ID do fabricante: ");
                    int fabricante = new Scanner(System.in).nextInt();
                    String sql3 = "SELECT e.id_equipamento, c.id_certificacao FROM equipamento e JOIN certificacao c ON e.id_certificacao = c.id_certificacao WHERE e.id_utilizador = ?";
                    try 
                    {
                        Conexao_BD conexaoBD = new Conexao_BD();
                        Connection conn = conexaoBD.conexao();
                        PreparedStatement stmt = conn.prepareStatement(sql3);
                        stmt.setInt(1, fabricante);
                        ResultSet rs = stmt.executeQuery();
                        
                        if (!rs.isBeforeFirst()) 
                        {
                            System.out.println("Nenhum pedido de certificação econtrado.");
                            return;
                        }
                        
                        while (rs.next()) 
                        {
                            int id_certificacao = rs.getInt("id_certificacao");
                            int id_equipamento = rs.getInt("id_equipamento");
                            System.out.println("O pedido de Certificação do equipamento " + id_equipamento);
                        }
                    } 
                    catch (SQLException e) 
                    {
                        System.out.println("Erro ao listar pedidos de certificação: " + e.getMessage());
                    }
                case 4:
                    System.out.println("Insira a data de início (YYYY-MM-DD): ");
                    String dataInicio = new Scanner(System.in).nextLine();
                    
                    System.out.println("Insira a data de fim (YYYY-MM-DD): ");
                    String dataFim = new Scanner(System.in).nextLine();
                    
                    String sql4 = "SELECT e.id_equipamento, c.id_certificacao FROM equipamento e JOIN certificacao c ON e.id_certificacao = c.id_certificacao WHERE c.data_realizacao BETWEEN ?::DATE AND ?::DATE";
                    
                    try 
                    {
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    PreparedStatement stmt = conn.prepareStatement(sql4);
                    stmt.setString(1, dataInicio);
                    stmt.setString(2, dataFim);
                    ResultSet rs = stmt.executeQuery();
                        
                        if (!rs.isBeforeFirst()) 
                        {
                            System.out.println("Nenhum pedido de certificação econtrado.");
                            return;
                        }
                        
                        while (rs.next()) 
                        {
                            int id_certificacao = rs.getInt("id_certificacao");
                            int id_equipamento = rs.getInt("id_equipamento");
                            System.out.println("O pedido de Certificação do equipamento " + id_equipamento);
                        }
                    } 
                    catch (SQLException e) 
                    {
                        System.out.println("Erro ao listar pedidos de certificação: " + e.getMessage());
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Metodo para pesquisar certificação por fabricante
     * 
     * @author Guilherme Rodrigues
     * 
     * @param id_utilizador
     */
    public static void pesquiarCertificacaoFabricante(int id_utilizador)
    {
        System.out.println("Deseja procurar por: ");
        System.out.println("1. Estado");
        System.out.println("2. Numero");

        int escolhaOrdenacao = new Scanner(System.in).nextInt();

        switch (escolhaOrdenacao) 
        {
            case 1:
                System.out.println("Insira o estado: ");
                String estado = new Scanner(System.in).nextLine();
                String sql = "SELECT e.id_equipamento, c.id_certificacao FROM equipamento e JOIN certificacao c ON e.id_certificacao = c.id_certificacao WHERE estado = ? AND e.id_utilizador = ?";
                try 
                {
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, estado);
                    stmt.setInt(2, id_utilizador);
                    ResultSet rs = stmt.executeQuery();
                    
                    if (!rs.isBeforeFirst()) 
                    {
                        System.out.println("Nenhum pedido de certificação econtrado.");
                        return;
                    }
                    
                    while (rs.next()) 
                    {
                        int id_certificacao = rs.getInt("id_certificacao");
                        int id_equipamento = rs.getInt("id_equipamento");
                        System.out.println("O pedido de Certificação do equipamento " + id_equipamento);
                    }
                } 
                catch (SQLException e) 
                {
                    System.out.println("Erro ao listar pedidos de certificação: " + e.getMessage());
                }
                break;
            case 2:
                System.out.println("Insira o numero: ");
                int numero = new Scanner(System.in).nextInt();
                String sql2 = "SELECT c.id_certificacao, e.id_equipamento, estado FROM certificacao c JOIN equipamento e ON c.id_certificacao = e.id_certificacao WHERE c.id_certificacao = ?";
                try 
                {
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    PreparedStatement stmt = conn.prepareStatement(sql2);
                    stmt.setInt(1, numero);
                    ResultSet rs = stmt.executeQuery();
                    
                    if (!rs.isBeforeFirst()) 
                    {
                        System.out.println("Nenhum pedido de certificação econtrado.");
                        return;
                    }
                    
                    while (rs.next()) 
                    {
                        String estado2 = rs.getString("estado");
                        int id_certificacao = rs.getInt("id_certificacao");
                        int id_equipamento = rs.getInt("id_equipamento");
                        System.out.println("O pedido de Certificação #" + id_certificacao + " do equipamento " + id_equipamento + " foi " + estado2);
                    }
                } 
                catch (SQLException e) 
                {
                    System.out.println("Erro ao listar pedidos de certificação: " + e.getMessage());
                }
                break;
            default:
                break;
        }
        
    }
    
    /**
     * Metodo para pesquisar certificação por tecnico
     * 
     * @author Guilherme Rodrigues
     * 
     * @param id_utilizador
     */
    public static void pesquiarCertificacaoTecnico(int id_utilizador)
    {
        System.out.println("Deseja procurar por: ");
        System.out.println("1. Listar todos ordenado por data");
        System.out.println("2. Listar todos ordenado por numero");
        System.out.println("3. Pesquisar por estado");
        System.out.println("4. Pesquisar por numero\n");

        int escolhaOrdenacao = new Scanner(System.in).nextInt();

        switch (escolhaOrdenacao) 
        {
            case 1:
                String sql = "SELECT e.id_equipamento, c.id_certificacao FROM certificacao c LEFT JOIN equipamento e ON e.id_certificacao = c.id_certificacao WHERE c.id_utilizador = ? ORDER BY c.data_realizacao";
                try 
                {
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, id_utilizador);
                    ResultSet rs = stmt.executeQuery();
                    
                    if (!rs.isBeforeFirst()) 
                    {
                        System.out.println("Nenhum pedido de certificação econtrado.");
                        return;
                    }
                    
                    while (rs.next()) 
                    {
                        int id_certificacao = rs.getInt("id_certificacao");
                        int id_equipamento = rs.getInt("id_equipamento");
                        System.out.println("O pedido de Certificação #" + id_certificacao + " do equipamento " + id_equipamento);
                    }
                } 
                catch (SQLException e) 
                {
                    System.out.println("Erro ao listar pedidos de certificação: " + e.getMessage());
                }
                break;
            case 2:
                String sql2 = "SELECT e.id_equipamento, c.id_certificacao FROM certificacao c LEFT JOIN equipamento e ON e.id_certificacao = c.id_certificacao WHERE c.id_utilizador = ? ORDER BY c.id_certificacao";
                try 
                {
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    PreparedStatement stmt = conn.prepareStatement(sql2);
                    stmt.setInt(1, id_utilizador);
                    ResultSet rs = stmt.executeQuery();
                    
                    if (!rs.isBeforeFirst()) 
                    {
                        System.out.println("Nenhum pedido de certificação econtrado.");
                        return;
                    }
                    
                    while (rs.next()) 
                    {
                        int id_certificacao = rs.getInt("id_certificacao");
                        int id_equipamento = rs.getInt("id_equipamento");
                        System.out.println("O pedido de Certificação #" + id_certificacao + " do equipamento " + id_equipamento);
                    }
                } 
                catch (SQLException e) 
                {
                    System.out.println("Erro ao listar pedidos de certificação: " + e.getMessage());
                }
                
                break;
            case 3:
                String sql3 = "SELECT e.id_equipamento, c.id_certificacao, estado FROM certificacao c LEFT JOIN equipamento e ON e.id_certificacao = c.id_certificacao WHERE c.id_utilizador = ? AND c.estado = ?";
                System.out.println("Insira o estado: ");
                String estado = new Scanner(System.in).nextLine();
                try 
                {
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    PreparedStatement stmt = conn.prepareStatement(sql3);
                    stmt.setInt(1, id_utilizador);
                    stmt.setString(2, estado);
                    ResultSet rs = stmt.executeQuery();
                    
                    if (!rs.isBeforeFirst()) 
                    {
                        System.out.println("Nenhum pedido de certificação econtrado.");
                        return;
                    }
                    
                    while (rs.next()) 
                    {
                        int id_certificacao = rs.getInt("id_certificacao");
                        int id_equipamento = rs.getInt("id_equipamento");
                        System.out.println("O pedido de Certificação #" + id_certificacao + " do equipamento " + id_equipamento);
                    }
                } 
                catch (SQLException e) 
                {
                    System.out.println("Erro ao listar pedidos de certificação: " + e.getMessage());
                }
                
                break;
            case 4:
                String sql4 = "SELECT e.id_equipamento, c.id_certificacao, estado FROM certificacao c LEFT JOIN equipamento e ON e.id_certificacao = c.id_certificacao WHERE c.id_utilizador = ? AND c.id_certificacao = ?";
                System.out.println("Insira o estado: ");
                int id = new Scanner(System.in).nextInt();
                try 
                {
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    PreparedStatement stmt = conn.prepareStatement(sql4);
                    stmt.setInt(1, id_utilizador);
                    stmt.setInt(2, id);
                    ResultSet rs = stmt.executeQuery();
                    
                    if (!rs.isBeforeFirst()) 
                    {
                        System.out.println("Nenhum pedido de certificação econtrado.");
                        return;
                    }
                    
                    while (rs.next()) 
                    {
                        int id_certificacao = rs.getInt("id_certificacao");
                        int id_equipamento = rs.getInt("id_equipamento");
                        System.out.println("O pedido de Certificação #" + id_certificacao + " do equipamento " + id_equipamento);
                    }
                } 
                catch (SQLException e) 
                {
                    System.out.println("Erro ao listar pedidos de certificação: " + e.getMessage());
                }
                
                break;
            default:
                break;
        }
    }

    /**
     * Este método registra um novo parâmetro na base de dados.
     *
     * @author Guilherme Rodrigues
     */
    public static void inserirLicenca() 
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Insira a licenca: ");
        String licenca = scanner.nextLine();

        String sql = "INSERT INTO licencas (nome_licenca) VALUES (?)";
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, licenca);
            stmt.executeUpdate();
            System.out.println("Licenca inserida com sucesso!");
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao inserir licenca: " + e.getMessage());
        }
    }

    /**
     * Este método registra um novo parâmetro na base de dados.
     * 
     * @author Guilherme Rodrigues
     * 
     * @param id_certificacao2
     * @param id_utilizador
     */
    public static void terminarCertificacao(int id_certificacao2, int id_utilizador)
    {
        String sql = "UPDATE certificacao SET estado = 'finalizado', tempo_decorrido = AGE(NOW(), data_realizacao) WHERE id_certificacao = ? AND id_utilizador = ?";
        String sql2 = "UPDATE equipamento SET submetido_certificacao = 'terminado' WHERE id_certificacao = ?";
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_certificacao2);
            stmt.setInt(2, id_utilizador);
            stmt.executeUpdate();
            
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt2.setInt(1, id_certificacao2);
            stmt2.executeUpdate();
            
            System.out.println("Certificação terminada com sucesso!");
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao terminar certificação: " + e.getMessage());
        }
    }
    
    /**
     * Este método registra um novo parâmetro na base de dados.
     * 
     * @author Guilherme Rodrigues
     * 
     * @param id_certificacao
     */
    public static void arquivarCertificacao(int id_certificacao) 
    {
        String sql = "UPDATE certificacao SET estado = 'arquivado' WHERE id_certificacao = ?";
        String sql2 = "Select id_certificacao FROM certificacao WHERE estado = 'finalizado' AND id_certificacao = ?";
        
        try
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            PreparedStatement stmt = conn.prepareStatement(sql2);
            stmt.setInt(1, id_certificacao);
            ResultSet rs = stmt.executeQuery();
            
            if (!rs.isBeforeFirst()) 
            {
                System.out.println("Pedido de certificação nao finalizado.");
                return;
            }
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao verificar certificação: " + e.getMessage());
        }

        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_certificacao);
            stmt.executeUpdate();
            System.out.println("Certificação arquivada com sucesso!");
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao arquivar certificação: " + e.getMessage());
        }
    }
}