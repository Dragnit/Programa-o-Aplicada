import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Random;
import java.util.Scanner;

/**
 * Classe do servidor TCP que recebe os comandos do cliente.
 * 
 * @author Guilherme Rodrigues 
 */
public class servidor 
{
    /**
     * Metodo principal do servidor TCP.
     * 
     * @author Guilherme Rodrigues
     * 
     * @param args
     */
    public static void main(String[] args) 
    {
        try 
        {
            System.out.println("Indique o porto a ser utilizado: ");
            int porto = new Scanner(System.in).nextInt();

            String ipServidor = InetAddress.getLocalHost().getHostAddress();
            ServerSocket serverSocket = new ServerSocket(porto);
            System.out.println("Servidor TCP iniciado na porta " + porto + " com o IP " + ipServidor + "\n");
            
            while (true) 
            {
                Socket socket = serverSocket.accept();
                
                // Criar canais de entrada e saída
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                
                String username = input.readLine();
                System.out.println(username);
                
                output.writeBytes("<Servidor> <hello>\n");
                output.flush();

                //Comandos do cliente
                String mensagem = input.readLine();
                String comando;
                while ((comando = input.readLine()) != null) 
                {
                    if (mensagem.endsWith("<Info>;")) 
                    {
                        System.out.println(mensagem);
                        Visualizar_Informações(input, output);
                    } 
                    else if (mensagem.endsWith("<update>;")) 
                    {
                        System.out.println(mensagem);
                        Alterar_Informações(input, output);
                    } 
                    else if (mensagem.endsWith("<inserir> <equipamento>;")) 
                    {
                        System.out.println(mensagem);
                        Adicionar_Equipamento(input, output);
                    }
                    else if (mensagem.endsWith("<pesquisa> <equipamento>;")) 
                    {
                        System.out.println(mensagem);
                        Listar_Equipamento(input, output);
                    }
                    else if (mensagem.endsWith("<listar> <equipamento>;"))
                    {
                        System.out.println(mensagem);
                        Pesquisar_Equipamento(input, output);
                    }
                    else if (mensagem.endsWith("<listar> <certificacao>;"))
                    {
                        System.out.println(mensagem);
                        Listar_Certificação(input, output);
                    }
                    else if (mensagem.endsWith("<certificacao> <num_serie>;")) 
                    {
                        System.out.println(mensagem);
                        Pesquisar_Certificação(input, output);
                    } 
                    else if (mensagem.endsWith("<bye>;")) 
                    {
                        System.out.println(mensagem);
                        //System.out.println("A conexão com o cliente foi encerrada.");
                        return;
                    } 
                    else 
                    {
                        output.writeBytes("Comando invalido! Use 'help' para ver os comandos disponiveis.\n");
                    }
                    output.flush();
                }

                // Fechar conexão com o cliente
                //System.out.println("Cliente desconectado.");
                input.close();
                output.close();
                socket.close();
            }
        }
        catch (Exception e) 
        {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
 
    }

    /**
     * Metodo para listar equipamentos.
     * 
     * @author Guilherme Rodrigues
     * 
     * @param input
     * @param output
     */
    public static void Listar_Equipamento(BufferedReader input, DataOutputStream output) throws NumberFormatException, IOException
    {
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();

            String sql = "SELECT * FROM equipamento WHERE sku_equipamento = ?";
            int sku_equipamento = Integer.parseInt(input.readLine());

            
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, sku_equipamento);
            ResultSet rs = stmt.executeQuery();
            
            if(!rs.isBeforeFirst())
            {
                output.writeBytes("<Servidor> <pesquisa> <equipamento> <fail>;\n");
                output.writeBytes("\nEOF\n");
            }
            else
            {
                StringBuilder mensagem = new StringBuilder("<Servidor> <pesquisa> <equipamento>");

                while (rs.next()) 
                {
                    
                    mensagem.append("\n=== Informacoes Equipamento ===");
                    mensagem.append("\n ID Equipamento: ").append(rs.getInt("id_equipamento"));
                    mensagem.append("\n ID Certificação: ").append(rs.getInt("id_certificacao"));
                    mensagem.append("\n ID Utilizador: ").append(rs.getInt("id_utilizador"));
                    mensagem.append("\n Marca do Equipamento: ").append(rs.getString("marca_equipamento"));
                    mensagem.append("\n Modelo do Equipamento: ").append(rs.getString("modelo_equipamento"));
                    mensagem.append("\n Setor Comercial do Equipamento: ").append(rs.getString("sector_comercial_equipamento"));
                    mensagem.append("\n Potência do Equipamento: ").append(rs.getFloat("potencia"));
                    mensagem.append("\n Amperagem do Equipamento: ").append(rs.getFloat("amperagem"));
                    mensagem.append("\n SKU do Equipamento: ").append(rs.getInt("sku_equipamento"));
                    mensagem.append("\n Número do Modelo: ").append(rs.getInt("n_modelo"));
                    mensagem.append("\n Data de Submissão: ").append(rs.getString("data_submissao"));
                    mensagem.append("\n Submetido Certificação: ").append(rs.getString("submetido_certificacao"));
                    mensagem.append("\n");
                }

                output.writeBytes(mensagem.toString());
                output.writeBytes("\nEOF\n");
            }

            rs.close();
            stmt.close();
            conn.close();  // Fechar a conexão
        } 
        catch (SQLException e)
        {
            //System.out.println("Erro ao listar equipamentos: " + e.getMessage());
            output.writeBytes(" <username> <pesquisa> <equipamento> <fail>\n" + "\nEOF\n");
        }
    }

    /**
     * Metodo para adicionar equipamentos.
     * 
     * @author Guilherme Rodrigues
     * 
     * @param input
     * @param output
     */
    public static void Adicionar_Equipamento(BufferedReader input, DataOutputStream output) throws NumberFormatException, IOException
    {
        int id_utilizador = Integer.parseInt(input.readLine());
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            
            String sql = "INSERT INTO equipamento (id_utilizador, marca_equipamento, modelo_equipamento, sector_comercial_equipamento, " +
                        "potencia, amperagem, sku_equipamento, n_modelo, data_submissao, submetido_certificacao) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement stmt = conn.prepareStatement(sql);

            String marca = input.readLine();
            String modelo = input.readLine();
            String setor = input.readLine();
            float potencia = Float.parseFloat(input.readLine());
            float amperagem = Float.parseFloat(input.readLine());
    
            Random rand = new Random();
            int n_equipamento = rand.nextInt(1000000) + 1;
            int n_modelo = Integer.parseInt(input.readLine());
            
            LocalDate dataAtual = LocalDate.now();
            java.sql.Date dataSubmissaoSQL = java.sql.Date.valueOf(dataAtual);
            
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
            if (rowsAffected > 0) 
            {
                output.writeBytes("\n <Servidor> <inserir> <equipamento> <ok>;");
                output.writeBytes("\nEOF\n");
            } 
            else 
            {
                output.writeBytes("\n <Servidor> <inserir> <equipamento> <fail>;");
                output.writeBytes("\nEOF\n");
            }
            
            stmt.close();
            conn.close();
        } 
        catch (SQLException e) 
        {
            output.writeBytes("\n <Servidor> <inserir> <equipamento> <fail>;");
            //output.writeBytes("Erro ao inserir equipamento: " + e.getMessage());
        }
        catch (IllegalArgumentException e) 
        {
            output.writeBytes("\n <Servidor> <inserir> <equipamento> <fail>;");
            //output.writeBytes("Erro no formato de data. Use o formato YYYY-MM-DD.");
        }
    }

    /**
     * Metodo para alterar informacoes do utilizador.
     * 
     * @author Guilherme Rodrigues
     * 
     * @param input
     * @param output
     */
    public static void Alterar_Informações(BufferedReader input, DataOutputStream output) throws NumberFormatException, IOException
    {
        int id_utilizador = Integer.parseInt(input.readLine());

        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();

            // Obter dados do utilizador
            String sql = "SELECT * FROM utilizador WHERE id_utilizador = ?";
            PreparedStatement stmtUtilizador = conn.prepareStatement(sql);
            stmtUtilizador.setInt(1, id_utilizador);
            ResultSet rs = stmtUtilizador.executeQuery();

            if (!rs.next()) 
            {
                output.writeBytes("Utilizador nao encontrado.\nEOF\n");
                return;
            }

            // Mensagem base
            StringBuilder mensagem = new StringBuilder("<Servidor> <Info>");
            mensagem.append("\n=== Informacoes Atuais ===");
            mensagem.append("\n1. Nome: ").append(rs.getString("nome_utilizador"));
            mensagem.append("\n2. Username: ").append(rs.getString("username_utilizador"));
            mensagem.append("\n3. Email: ").append(rs.getString("email_utilizador"));

            String tipo = rs.getString("tipo");
            rs.close();
            stmtUtilizador.close();

            String sql2 = "SELECT * FROM fabricante WHERE id_utilizador = ?";
            PreparedStatement stmtFab = conn.prepareStatement(sql2);
            stmtFab.setInt(1, id_utilizador);
            ResultSet rs2 = stmtFab.executeQuery();

            if (rs2.next()) 
            {
                mensagem.append("\n4. NIF: ").append(rs2.getString("nif_fabricante"));
                mensagem.append("\n5. Telefone: ").append(rs2.getInt("telefone_fabricante"));
                mensagem.append("\n6. Morada: ").append(rs2.getString("morada_fabricante"));
                mensagem.append("\n7. Sector Comercial: ").append(rs2.getString("sector_comercial"));
                mensagem.append("\n8. Inicio da Atividade: ").append(rs2.getString("inicio_actividade"));
            }

            rs2.close();
            stmtFab.close();
            

            // Mostrar tudo ao utilizador
            output.writeBytes(mensagem.toString() + "\n");
            output.writeBytes("EOF\n");

            // Atualização
            String campo = input.readLine();
            String label = "";
            int campoEscolhido = 0;

            try 
            {
                campoEscolhido = Integer.parseInt(campo);
            } 
            catch (NumberFormatException e) 
            {
                output.writeBytes("Valor invalido. Deve ser um número entre 1 e 8.\nEOF\n");
                return;
            }

            // Mapear campo
            switch (campoEscolhido) 
            {
                case 1: label = "nome_utilizador"; break;
                case 2: label = "username_utilizador"; break;
                case 3: label = "email_utilizador"; break;
                case 4: label = "nif_fabricante"; break;
                case 5: label = "telefone_fabricante"; break;
                case 6: label = "morada_fabricante"; break;
                case 7: label = "sector_comercial"; break;
                case 8: label = "inicio_actividade"; break;
                default: 
                {
                    output.writeBytes("Campo invalido. Escolhe de 1 a 8.\nEOF\n");
                    return;
                }
            }

            String novoValor = input.readLine();
            boolean valido = true;

            // Validação
            if (label.equals("telefone_fabricante") && !novoValor.matches("[923]\\d{8}")) 
            {
                //output.writeBytes("Numero de telefone invalido! Deve ter 9 digitos e comecar com 9, 2 ou 3.");
                output.writeBytes("<Servidor> <update> <fail>\n");
                output.writeBytes("\nEOF\n");
                valido = false;
                //System.out.println("Validação errada de telefone");
            }
            if (label.equals("nif_fabricante") && !novoValor.matches("\\d{9}")) 
            {
                //output.writeBytes("NIF invalido! Deve ter 9 digitos.\nEOF\n");
                output.writeBytes("<Servidor> <update> <fail>\n");
                output.writeBytes("\nEOF\n");
                valido = false;
                //System.out.println("Validação errada de NIF");
            }

            // Preparar update
            String sqlUpdate;

            if (valido==true) 
            {
                if (campoEscolhido <= 3) 
                {
                    sqlUpdate = "UPDATE utilizador SET " + label + " = ? WHERE id_utilizador = ?";
                } 
                else 
                {
                    sqlUpdate = "UPDATE fabricante SET " + label + " = ? WHERE id_utilizador = ?";
                }
                PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
                
                if(label.equals("nif_fabricante") || label.equals("telefone_fabricante"))
                {
                    int num = Integer.parseInt(novoValor);
                    stmtUpdate.setInt(1, num);
                }
                else
                {
                    stmtUpdate.setString(1, novoValor);
                }
                stmtUpdate.setInt(2, id_utilizador);
                
                int linhasAfetadas = stmtUpdate.executeUpdate();
                if (linhasAfetadas > 0) 
                {
                    //output.writeBytes("Informacao do utilizador atualizada com sucesso!\n");
                    output.writeBytes("<Servidor> <update> <ok>\n");
                    output.writeBytes("\nEOF\n");

                } 
                else 
                {
                    //output.writeBytes("Nenhuma linha foi atualizada.\n");
                    output.writeBytes("<Servidor> <update> <fail>\n");
                    output.writeBytes("\nEOF\n");

                }
                output.writeBytes("EOF\n");

                stmtUpdate.close();
                conn.close();
            }
        } 
        catch (SQLException e) 
        {
            output.writeBytes("Erro: " + e.getMessage() + "\nEOF\n");
        }
    }

    /**
     * Metodo para visualizar informacoes do utilizador.
     * 
     * @author Guilherme Rodrigues
     * 
     * @param input
     * @param output
     * @throws NumberFormatException
     * @throws IOException
     */
    public static void Visualizar_Informações(BufferedReader input, DataOutputStream output) throws NumberFormatException, IOException
    {
        try 
        {
            Conexao_BD conexao = new Conexao_BD();
            Connection conn = conexao.conexao();
        
            String username_utilizador = input.readLine();
        
            String sql = "SELECT * FROM utilizador WHERE username_utilizador = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username_utilizador);
            ResultSet rs = stmt.executeQuery();
        
            if (rs.next()) 
            {
                //System.out.println("\n Enviando sobre do utilizador...");
                int id_utilizador = rs.getInt("id_utilizador");
                String tipo = rs.getString("tipo");
        
                String mensagem = "<Servidor> <Info> \n<ID: " + id_utilizador +
                                    "\nNome: " + rs.getString("nome_utilizador") +
                                    "\nUsername: " + rs.getString("username_utilizador") +
                                    "\nEmail: " + rs.getString("email_utilizador") +
                                    "\nTipo: " + tipo;
        
                if (tipo.equalsIgnoreCase("fabricante")) 
                {
                    String sql2 = "SELECT * FROM fabricante WHERE id_utilizador = ?";
                    PreparedStatement stmt2 = conn.prepareStatement(sql2);
                    stmt2.setInt(1, id_utilizador);
                    ResultSet rs2 = stmt2.executeQuery();
        
                    while (rs2.next()) 
                    {
                        mensagem += "\nNIF: " + rs2.getString("nif_fabricante") +
                                    "\nTelefone: " + rs2.getInt("telefone_fabricante") +
                                    "\nMorada: " + rs2.getString("morada_fabricante") +
                                    "\nSector Comercial: " + rs2.getString("sector_comercial") +
                                    "\nInicio da Atividade: " + rs2.getString("inicio_actividade") +
                                    " >";
                    }
                    rs2.close();
                    stmt2.close();
                }
        
                output.writeBytes(mensagem + "\n");
                output.writeBytes("EOF\n");

            } 
        
            rs.close();
            stmt.close();
            conn.close();
        } 
        catch (SQLException e) 
        {
            output.writeBytes("Erro: " + e.getMessage() + "\nEOF\n");
        }
    }

    /**
     * Metodo para pesquisar equipamentos.
     * 
     * @author Guilherme Rodrigues
     * 
     * @param input
     * @param output
     * @throws NumberFormatException
     * @throws IOException
     */
    public static void Pesquisar_Equipamento(BufferedReader input, DataOutputStream output) throws NumberFormatException, IOException
    {
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();

            int id_utilizador = Integer.parseInt(input.readLine());
                    
            PreparedStatement pstmt;
            ResultSet rs;

            String sql = "SELECT * FROM equipamento WHERE  id_utilizador = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id_utilizador);

            rs = pstmt.executeQuery();
            
            StringBuilder mensagem = new StringBuilder("<username> <listar> <equipamento>\n");

            boolean encontrado = false;
            while (rs.next()) 
            {
                encontrado = true;
                mensagem.append("\n=== Informacoes Equipamento ===");
                mensagem.append("\nID Equipamento: ").append(rs.getInt("id_equipamento"));
                mensagem.append("\nID Certificação: ").append(rs.getInt("id_certificacao"));
                mensagem.append("\nID Utilizador: ").append(rs.getInt("id_utilizador"));
                mensagem.append("\nMarca do Equipamento: ").append(rs.getString("marca_equipamento"));
                mensagem.append("\nModelo do Equipamento: ").append(rs.getString("modelo_equipamento"));
                mensagem.append("\nSetor Comercial do Equipamento: ").append(rs.getString("sector_comercial_equipamento"));
                mensagem.append("\nPotência do Equipamento: ").append(rs.getFloat("potencia"));
                mensagem.append("\nAmperagem do Equipamento: ").append(rs.getFloat("amperagem"));
                mensagem.append("\nSKU do Equipamento: ").append(rs.getInt("sku_equipamento"));
                mensagem.append("\nN Modelo do Equipamento: ").append(rs.getInt("n_modelo"));
                mensagem.append("\nData Submissão do Equipamento: ").append(rs.getString("data_submissao"));
                mensagem.append("\nSubmetido Certificação do Equipamento: ").append(rs.getString("submetido_certificacao"));
                mensagem.append("\n");
            
                output.writeBytes(mensagem + "\n");
                output.writeBytes("EOF\n");
                
            }

            if (!encontrado) 
            {
                output.writeBytes("<Servidor> <llistar> <equipamento> <fail>;\n");
                output.writeBytes("\nEOF\n");
                return;
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

    /**
     * Metodo para listar certificacoes.
     * 
     * @author Guilherme Rodrigues
     * 
     * @param input
     * @param output
     * @throws NumberFormatException
     * @throws IOException
     */
    public static void Listar_Certificação(BufferedReader input, DataOutputStream output) throws NumberFormatException, IOException 
    {
        int id_utilizador = Integer.parseInt(input.readLine());
        String sql = "SELECT c.id_certificacao, e.id_equipamento, c.estado, data_realizacao FROM certificacao c JOIN equipamento e ON c.id_certificacao = e.id_certificacao WHERE c.id_utilizador = ?";
    
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id_utilizador);
            ResultSet rs = stmt.executeQuery();
    
            if (!rs.isBeforeFirst()) 
            {
                output.writeBytes("<Servidor> <listar> <certificação> <fail>;\n");
                output.writeBytes("\nEOF\n");
            }
            else
            {
                StringBuilder mensagem = new StringBuilder("=== Informações Certificação ===");
        
                while (rs.next()) 
                {
                    mensagem.append("\nID Certificação: ").append(rs.getInt("id_certificacao"));
                    mensagem.append("\nID Equipamento: ").append(rs.getInt("id_equipamento"));
                    mensagem.append("\nEstado da Certificação: ").append(rs.getString("estado"));
                    mensagem.append("\nData Realização da Certificação: ").append(rs.getString("data_realizacao"));
                    mensagem.append("\n");
                }
        
                output.writeBytes(mensagem.toString() + "\n");
                output.writeBytes("EOF\n");
            }

            rs.close();
            stmt.close();
            conn.close();
        } 
        catch (SQLException e) 
        {
            output.writeBytes("Erro ao listar pedidos de certificação: " + e.getMessage() + "\nEOF\n");
        }
    }

    /**
     * Metodo para pesquisar certificacoes.
     * 
     * @author Guilherme Rodrigues
     * 
     * @param input
     * @param output
     * @throws NumberFormatException
     * @throws IOException
     */
    public static void Pesquisar_Certificação(BufferedReader input, DataOutputStream output) throws NumberFormatException, IOException
    {
        //System.out.println("Insira o ID do utilizador: ");
        int id_utilizador = Integer.parseInt(input.readLine());

        String sql = "";
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();

            int numero = Integer.parseInt(input.readLine());
            sql = "SELECT c.id_certificacao, e.id_equipamento, c.estado, c.data_realizacao " +
                "FROM certificacao c JOIN equipamento e ON c.id_certificacao = e.id_certificacao " +
                "WHERE c.id_certificacao = ? AND e.id_utilizador = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, numero);
            stmt.setInt(2, id_utilizador);

            rs = stmt.executeQuery();

            StringBuilder mensagem = new StringBuilder("<username> <pesquisa> <certificacao>\n");
            
            if(!rs.isBeforeFirst())
            {
                output.writeBytes("<Servidor> <pesquisa> <certificacao> <fail>;\n");
                output.writeBytes("\nEOF\n");
            }
            else
            {
                while (rs.next()) 
                {
                    mensagem.append("=== Informações da Certificação ===");
                    mensagem.append("\nID Certificação: ").append(rs.getInt("id_certificacao"));
                    mensagem.append("\nID Equipamento: ").append(rs.getInt("id_equipamento"));
                    mensagem.append("\nEstado da Certificação: ").append(rs.getString("estado"));
                    mensagem.append("\nData Realização: ").append(rs.getString("data_realizacao"));
                    mensagem.append("\n");
                }
            }

            output.writeBytes(mensagem.toString() + "\n");
            output.writeBytes("EOF\n");

        } 
        catch (SQLException e) 
        {
            output.writeBytes("Erro ao listar pedidos de certificação: " + e.getMessage() + "\nEOF\n");
        }

    }
}