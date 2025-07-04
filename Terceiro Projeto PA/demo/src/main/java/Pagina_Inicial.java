import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Pagina_Inicial extends JFrame 
{
    private String utilizador;
    private int id;
    private String tipo;
    private int num_notificacoes;
    
    // construtor
    /**
     *  Cria uma nova instância da página inicial do utilizador.
     *  Este construtor inicializa a interface gráfica com base no tipo de utilizador (Gestor, Fabricante ou Técnico),
     *  exibindo informações relevantes e botões de ação correspondentes.
     * 
     * @author Guilherme Rodrigues
     * 
     * @param utilizador
     * @param id
     * @param tipo
     * @param num_notificacoes
     */
    public Pagina_Inicial(String utilizador, int id, String tipo, int num_notificacoes) 
    {
        this.utilizador = utilizador;
        this.id = id;
        this.tipo = tipo;
        System.out.println(tipo);
        this.num_notificacoes = num_notificacoes;
        
        setTitle("Menu de " + tipo + " - " + utilizador);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        
        // Layout principal com BorderLayout
        setLayout(new BorderLayout());
        
        // Painel esquerdo - Informação do utilizador
        JPanel painelInfo = criarPainelInformacao();
        
        // Painel direito - Botões de ação
        JPanel painelBotoes;
        if(tipo.equals("Gestor")) 
        {
            painelBotoes = criarPainelBotoesGestor();
        } 
        else if (tipo.equals("Fabricante")) 
        {
            painelBotoes = criarPainelBotoesFabricante();
        } 
        else 
        {
            painelBotoes = criarPainelBotoesTecnico();
        }
        
        // Adicionar painéis ao frame
        add(painelInfo, BorderLayout.WEST);
        add(painelBotoes, BorderLayout.CENTER);
        
        setVisible(true);
    }
    
    /**
     *  Cria um painel com informações do utilizador.
     *  Este painel exibe o nome, ID, tipo de utilizador e número de notificações do utilizador.
     *  O painel é estilizado com bordas, cores e fontes apropriadas para melhorar a legibilidade.  
     * 
     *  @author Guilherme Rodrigues
     * 
     * @return
     */
    private JPanel criarPainelInformacao() 
    {
        JPanel painel = new JPanel();
        painel.setLayout(new BoxLayout(painel, BoxLayout.Y_AXIS));
        painel.setBorder(BorderFactory.createTitledBorder("Informação utilizador"));
        painel.setPreferredSize(new Dimension(250, 0));
        painel.setBackground(Color.LIGHT_GRAY);
        
        // Adicionar informações do utilizador
        painel.add(Box.createVerticalStrut(20));
        
        JLabel lblTitulo = new JLabel("Dados do Utilizador");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(lblTitulo);
        
        painel.add(Box.createVerticalStrut(20));
        
        JLabel lblNome = new JLabel("Nome: " + utilizador);
        lblNome.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(lblNome);
        
        painel.add(Box.createVerticalStrut(10));
        
        JLabel lblId = new JLabel("ID: " + id);
        lblId.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(lblId);
        
        painel.add(Box.createVerticalStrut(10));
        
        JLabel lblTipo = new JLabel("Tipo: " + tipo);
        lblTipo.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(lblTipo);
        
        painel.add(Box.createVerticalStrut(10));
        
        JLabel lblNotificacoes = new JLabel("Notificações: " + num_notificacoes);
        lblNotificacoes.setAlignmentX(Component.CENTER_ALIGNMENT);
        painel.add(lblNotificacoes);
        
        painel.add(Box.createVerticalGlue());
        
        return painel;
    }
    
    /**
     *  Cria um painel com botões de ação específicos para o tipo de utilizador.
     *  Dependendo do tipo de utilizador (Gestor, Fabricante ou Técnico), diferentes botões são adicionados ao painel.
     *  Cada botão tem uma ação associada que será executada quando o botão for clicado.
     * 
     *  @author Guilherme Rodrigues
     * 
     * @return
     */
    private JPanel criarPainelBotoesGestor() 
    {
        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(15, 1, 5, 5));
        painel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Botões de ação - Gestor"), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        // Adicionar botões do Gestor
        painel.add(new JLabel("=== Menu de Gestor ===", JLabel.CENTER));
        painel.add(criarBotao("1. Registar Gestor"));
        painel.add(criarBotao("2. Remover Utilizador"));
        painel.add(criarBotao("3. Pesquisar Utilizador por username"));
        painel.add(criarBotao("4. Listar todos os Utilizadores por tipo"));
        painel.add(criarBotao("5. Listar todos os Utilizadores"));
        painel.add(criarBotao("6. Alterar Informações de um Utilizador"));
        painel.add(criarBotao("7. Inserir Licença"));
        painel.add(criarBotao("8. Aceitar Pedido de Certificação"));
        painel.add(criarBotao("9. Listar todas as Certificações"));
        painel.add(criarBotao("10. Pesquisar por Certificação"));
        painel.add(criarBotao("11. Pesquisar por Equipamento"));
        painel.add(criarBotao("12. Arquivar uma Certificação"));
        painel.add(criarBotao("13. Ver Notificações (" + num_notificacoes + ")"));
        painel.add(criarBotao("14. Sair"));
        
        return painel;
    }
    
    /**
     *  Cria um painel com botões de ação específicos para o tipo de utilizador Fabricante.
     *  Este painel contém botões para visualizar e alterar informações, gerenciar equipamentos e certificações,
     *  além de opções para sair e ver notificações.
     * 
     *  @author Guilherme Rodrigues
     * 
     * @return
     */
    private JPanel criarPainelBotoesFabricante() 
    {
        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(12, 1, 5, 5));
        painel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Botões de ação - Fabricante"), BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        // Adicionar botões do Fabricante
        painel.add(new JLabel("=== Menu de Fabricante ===", JLabel.CENTER));
        painel.add(criarBotao("1. Visualizar informação"));
        painel.add(criarBotao("2. Alterar informação"));
        painel.add(criarBotao("3. Pedir para Remover Conta"));
        painel.add(criarBotao("4. Adicionar Equipamento"));
        painel.add(criarBotao("5. Listar Equipamentos"));
        painel.add(criarBotao("6. Pesquisar Equipamentos"));
        painel.add(criarBotao("7. Realizar Pedido de Certificação"));
        painel.add(criarBotao("8. Listar Certificações"));
        painel.add(criarBotao("9. Pesquisar por Certificação"));
        painel.add(criarBotao("10. Ver Notificações (" + num_notificacoes + ")"));
        painel.add(criarBotao("11. Sair"));
        
        return painel;
    }
    
    /**
     *  Cria um painel com botões de ação específicos para o tipo de utilizador Técnico.
     *  Este painel contém botões para visualizar e alterar informações, gerenciar pedidos de certificação,
     *  além de opções para sair e ver notificações.
     * 
     *  @author Guilherme Rodrigues
     * 
     * @return
     */
    private JPanel criarPainelBotoesTecnico() 
    {
        JPanel painel = new JPanel();
        painel.setLayout(new GridLayout(10, 1, 5, 5));
        painel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder("Botões de ação - Técnico"),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        // Adicionar botões do Técnico
        painel.add(new JLabel("=== Menu de Técnico ===", JLabel.CENTER));
        painel.add(criarBotao("1. Visualizar informação"));
        painel.add(criarBotao("2. Alterar informação"));
        painel.add(criarBotao("3. Pedidos de Certificação"));
        painel.add(criarBotao("4. Listar Pedidos de Certificação"));
        painel.add(criarBotao("5. Inserir Resultados de Certificação"));
        painel.add(criarBotao("6. Terminar Certificação"));
        painel.add(criarBotao("7. Remoção de Conta"));
        painel.add(criarBotao("8. Ver Notificações (" + num_notificacoes + ")"));
        painel.add(criarBotao("9. Sair"));
        
        return painel;
    }
    
    /**
     * Método para criar um botão com ação associada que depende do texto do botão.
     * 
     * @author Guilherme Rodrigues
     * 
     * @param texto
     * @return
     */
    private JButton criarBotao(String texto) 
    {
        JButton botao = new JButton(texto);
        botao.addActionListener((ActionEvent e) -> {
            // Aqui você pode implementar as ações específicas
            if (texto.contains("Sair")) 
            {
                int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja sair?", "Confirmar Saída", JOptionPane.YES_NO_OPTION);
                if (resposta == JOptionPane.YES_OPTION) 
                {
                    System.out.println("Adeus " + utilizador + "!");
                    this.dispose();
                    Main.caixaDeDialogo();
                }
            } 
            else if(texto.contains("Registar Gestor"))
            {
                Registo janela = new Registo(); // NÃO monta nada ainda
                janela.RegistoGestor();         // monta só o layout do gestor
                janela.setSize(600, 700);
                janela.setLocationRelativeTo(null);
                janela.setVisible(true);
            }
            else if(texto.contains("Remover Utilizador"))
            {
                abrirJanelaRemoverUtilizador();
            }
            else if(texto.contains("Pesquisar Utilizador por username"))
            {
                abrirJanelaBuscaUtilizador();
            }
            else if(texto.contains("Listar todos os Utilizadores por tipo"))
            {
                abrirJanelaBuscaTipo();
            }
            else if(texto.contains("Listar todos os Utilizadores"))
            {
                abrirJanelaBuscaTodos();
            }
            else if(texto.contains("Alterar Informações de um Utilizador"))
            {
                abrirJanelaAtualizarUtilizador();
            }
            else if(texto.contains("Inserir Licença"))
            {
                abrirJanelaInserirLicenca();
            }
            else if(texto.contains("Aceitar Pedido de Certificação"))
            {
               //AceitarPedidoCertificacao();
            }
            else if(texto.contains("Listar todas as Certificações"))
            {
                //ListarCertificacoes();
            }
            else if(texto.contains("Pesquisar por Certificação"))
            {
                //PesquisarCertificacao();                
            }
            else if(texto.contains("Pesquisar por Equipamento"))
            {
                //PesquisarEquipamento();
            }
            else if(texto.contains("Arquivar uma Certificação"))
            {
                //ArquivarCertificacao();
            }
            else if(texto.contains("Ver Notificações"))
            {
                //VerNotificacoes();
            }   
        });
        return botao;
    }
    
    /**
     *  Método para abrir uma janela de remoção de utilizador.
     *  Esta janela permite ao gestor buscar um utilizador pelo ID e exibir suas informações.
     *  Se o utilizador estiver ativo, o gestor pode optar por desativá-lo.
     *  Caso o utilizador já esteja desativado, uma mensagem apropriada é exibida.
     * 
     * @author Guilherme Rodrigues
     */
    private void abrirJanelaRemoverUtilizador() 
    {
        JFrame janelaRemover = new JFrame("Remover Utilizador");
        janelaRemover.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaRemover.setSize(600, 500);
        janelaRemover.setLocationRelativeTo(this);
        janelaRemover.setLayout(new GridBagLayout());
        janelaRemover.getContentPane().setBackground(Color.LIGHT_GRAY);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titulo = new JLabel("Remover Utilizador", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.ipady = 15;
        janelaRemover.add(titulo, gbc);
        gbc.gridwidth = 1;
        gbc.ipady = 0;

        // Campo ID do Utilizador
        JLabel labelId = new JLabel("ID do Utilizador:");
        labelId.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        janelaRemover.add(labelId, gbc);

        JTextField campoIdUtilizador = new JTextField();
        campoIdUtilizador.setPreferredSize(new Dimension(250, 35));
        campoIdUtilizador.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        janelaRemover.add(campoIdUtilizador, gbc);

        // Label para mostrar informações do utilizador
        JLabel labelInfo = new JLabel(" ");
        labelInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        labelInfo.setForeground(Color.BLUE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 15, 15, 15);
        janelaRemover.add(labelInfo, gbc);
        gbc.gridwidth = 1;

        // Botão Buscar Utilizador
        JButton botaoBuscar = new JButton("Buscar Utilizador");
        botaoBuscar.setPreferredSize(new Dimension(200, 40));
        botaoBuscar.setFont(new Font("Arial", Font.PLAIN, 14));
        botaoBuscar.setBackground(new Color(70, 130, 180));
        botaoBuscar.setForeground(Color.WHITE);
        botaoBuscar.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                String idTexto = campoIdUtilizador.getText().trim();
                
                if (idTexto.isEmpty()) 
                {
                    JOptionPane.showMessageDialog(janelaRemover, "Por favor, insira o ID do utilizador!");
                    return;
                }
                
                try 
                {
                    int idUtilizador = Integer.parseInt(idTexto);
                    
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    
                    String sql = "SELECT nome_utilizador, username_utilizador, email_utilizador, tipo, desativado FROM utilizador WHERE id_utilizador = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, idUtilizador);
                    ResultSet rs = pstmt.executeQuery();
                    
                    if (rs.next()) 
                    {
                        String nome = rs.getString("nome_utilizador");
                        String username = rs.getString("username_utilizador");
                        String email = rs.getString("email_utilizador");
                        String tipo = rs.getString("tipo");
                        boolean desativado = rs.getBoolean("desativado");
                        
                        if (desativado) 
                        {
                            labelInfo.setText("<html><div style='text-align: center;'>" +
                                            "<b>Nome:</b> " + nome + "<br>" +
                                            "<b>Username:</b> " + username + "<br>" +
                                            "<b>Email:</b> " + email + "<br>" +
                                            "<b>Tipo:</b> " + tipo + "<br>" +
                                            "<span style='color: red;'><b>Status:</b> JÁ DESATIVADO</span>" +
                                            "</div></html>");
                        } 
                        else 
                        {
                            labelInfo.setText("<html><div style='text-align: center;'>" +
                                            "<b>Nome:</b> " + nome + "<br>" +
                                            "<b>Username:</b> " + username + "<br>" +
                                            "<b>Email:</b> " + email + "<br>" +
                                            "<b>Tipo:</b> " + tipo + "<br>" +
                                            "<span style='color: green;'><b>Status:</b> ATIVO</span>" +
                                            "</div></html>");
                        }
                    } 
                    else 
                    {
                        labelInfo.setText("<html><span style='color: red;'>Utilizador não encontrado!</span></html>");
                    }
                    
                    rs.close();
                    pstmt.close();
                    conn.close();
                    
                } 
                catch (NumberFormatException ex) 
                {
                    JOptionPane.showMessageDialog(janelaRemover, "ID deve ser um número válido!");
                } 
                catch (SQLException ex) 
                {
                    JOptionPane.showMessageDialog(janelaRemover, "Erro ao buscar utilizador: " + ex.getMessage());
                    System.out.println("Erro SQL: " + ex.getMessage());
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 15, 10, 15);
        janelaRemover.add(botaoBuscar, gbc);
        gbc.gridwidth = 1;

        // Botões de ação
        gbc.insets = new Insets(20, 15, 10, 15);
        
        JButton botaoRemover = new JButton("Remover Utilizador");
        botaoRemover.setPreferredSize(new Dimension(200, 40));
        botaoRemover.setFont(new Font("Arial", Font.BOLD, 14));
        botaoRemover.setBackground(new Color(220, 20, 60));
        botaoRemover.setForeground(Color.WHITE);
        botaoRemover.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                String idTexto = campoIdUtilizador.getText().trim();
                
                if (idTexto.isEmpty()) 
                {
                    JOptionPane.showMessageDialog(janelaRemover, "Por favor, primeiro busque um utilizador!");
                    return;
                }
                
                try 
                {
                    int idUtilizador = Integer.parseInt(idTexto);
                    
                    // Confirmação antes de remover
                    int opcao = JOptionPane.showConfirmDialog(janelaRemover, 
                        "Tem certeza que deseja desativar este utilizador?\n" +
                        "Esta ação irá marcar o utilizador como desativado.",
                        "Confirmar Remoção", 
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                    
                    if (opcao == JOptionPane.YES_OPTION) 
                    {
                        // Remover utilizador da BD
                        try 
                        {
                            Conexao_BD conexaoBD = new Conexao_BD();
                            Connection conn = conexaoBD.conexao();
                            
                            // 1. Descobrir o tipo do utilizador
                            String checkSql = "SELECT tipo, nome_utilizador, desativado FROM utilizador WHERE id_utilizador = ?";
                            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                            checkStmt.setInt(1, idUtilizador);
                            ResultSet rs = checkStmt.executeQuery();
                            
                            if (rs.next()) 
                            {
                                String tipo = rs.getString("tipo");
                                String nome = rs.getString("nome_utilizador");
                                boolean jaDesativado = rs.getBoolean("desativado");
                                
                                if (jaDesativado) 
                                {
                                    JOptionPane.showMessageDialog(janelaRemover, "Este utilizador já está desativado!");
                                    return;
                                }
                                
                                // 2. Desativar o utilizador
                                String updateSql = "UPDATE utilizador SET desativado = true WHERE id_utilizador = ?";
                                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                                updateStmt.setInt(1, idUtilizador);
                                
                                int linhasAfetadas = updateStmt.executeUpdate();
                                
                                if (linhasAfetadas > 0) 
                                {
                                    JOptionPane.showMessageDialog(janelaRemover, 
                                        tipo + " '" + nome + "' foi desativado com sucesso!",
                                        "Sucesso", 
                                        JOptionPane.INFORMATION_MESSAGE);
                                    
                                    // Limpar campos
                                    campoIdUtilizador.setText("");
                                    labelInfo.setText(" ");
                                    
                                    System.out.println("\n" + tipo + " " + nome + " desativado com sucesso da base de dados!");
                                } 
                                else 
                                {
                                    JOptionPane.showMessageDialog(janelaRemover, "Erro ao desativar utilizador!");
                                }
                                
                                updateStmt.close();
                            } 
                            else 
                            {
                                JOptionPane.showMessageDialog(janelaRemover, "Utilizador com ID " + idUtilizador + " não encontrado!");
                            }
                            
                            checkStmt.close();
                            rs.close();
                            conn.close();
                            
                        } 
                        catch (SQLException ex) 
                        {
                            JOptionPane.showMessageDialog(janelaRemover, "Erro ao remover utilizador: " + ex.getMessage());
                            System.out.println("Erro ao remover utilizador: " + ex.getMessage());
                        }
                    }
                    
                } 
                catch (NumberFormatException ex) 
                {
                    JOptionPane.showMessageDialog(janelaRemover, "ID deve ser um número válido!");
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 4;
        janelaRemover.add(botaoRemover, gbc);

        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setPreferredSize(new Dimension(200, 40));
        botaoCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        botaoCancelar.setBackground(new Color(128, 128, 128));
        botaoCancelar.setForeground(Color.WHITE);
        botaoCancelar.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                janelaRemover.dispose();
            }
        });
        gbc.gridx = 1;
        janelaRemover.add(botaoCancelar, gbc);
        
        janelaRemover.setVisible(true);
    }

    /**
     *  Método para abrir uma janela de busca de utilizador.
     *  Esta janela permite ao gestor buscar um utilizador pelo username e exibir suas informações.
     *  Se o utilizador estiver ativo, suas informações são exibidas; caso contrário, uma mensagem apropriada é mostrada.
     *
     * @author Guilherme Rodrigues
     */
    private void abrirJanelaBuscaUtilizador() 
    {
        JFrame janelaRemover = new JFrame("Visualizar Utilizador");
        janelaRemover.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaRemover.setSize(600, 500);
        janelaRemover.setLocationRelativeTo(this);
        janelaRemover.setLayout(new GridBagLayout());
        janelaRemover.getContentPane().setBackground(Color.LIGHT_GRAY);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titulo = new JLabel("Visualizar Utilizador", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.ipady = 15;
        janelaRemover.add(titulo, gbc);
        gbc.gridwidth = 1;
        gbc.ipady = 0;

        // Campo ID do Utilizador
        JLabel labelId = new JLabel("Username do Utilizador:");
        labelId.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        janelaRemover.add(labelId, gbc);

        JTextField campoIdUtilizador = new JTextField();
        campoIdUtilizador.setPreferredSize(new Dimension(250, 35));
        campoIdUtilizador.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        janelaRemover.add(campoIdUtilizador, gbc);

        // Label para mostrar informações do utilizador
        JLabel labelInfo = new JLabel(" ");
        labelInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        labelInfo.setForeground(Color.BLUE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 15, 15, 15);
        janelaRemover.add(labelInfo, gbc);
        gbc.gridwidth = 1;

        // Botão Buscar Utilizador
        JButton botaoBuscar = new JButton("Buscar Utilizador");
        botaoBuscar.setPreferredSize(new Dimension(200, 40));
        botaoBuscar.setFont(new Font("Arial", Font.PLAIN, 14));
        botaoBuscar.setBackground(new Color(70, 130, 180));
        botaoBuscar.setForeground(Color.WHITE);
        botaoBuscar.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                String idTexto = campoIdUtilizador.getText().trim();
                
                if (idTexto.isEmpty()) 
                {
                    JOptionPane.showMessageDialog(janelaRemover, "Por favor, insira o Username do utilizador!");
                    return;
                }
                
                try 
                {
                    //int idUtilizador = Integer.parseInt(idTexto);
                    
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    
                    String sql = "SELECT nome_utilizador, username_utilizador, email_utilizador, tipo, desativado FROM utilizador WHERE username_utilizador = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, idTexto);
                    ResultSet rs = pstmt.executeQuery();
                    
                    if (rs.next()) 
                    {
                        String nome = rs.getString("nome_utilizador");
                        String username = rs.getString("username_utilizador");
                        String email = rs.getString("email_utilizador");
                        String tipo = rs.getString("tipo");
                        boolean desativado = rs.getBoolean("desativado");
                        
                        if (desativado) 
                        {
                            labelInfo.setText("<html><div style='text-align: center;'>" +
                                            "<b>Nome:</b> " + nome + "<br>" +
                                            "<b>Username:</b> " + username + "<br>" +
                                            "<b>Email:</b> " + email + "<br>" +
                                            "<b>Tipo:</b> " + tipo + "<br>" +
                                            "<span style='color: red;'><b>Status:</b> JÁ DESATIVADO</span>" +
                                            "</div></html>");
                        } 
                        else 
                        {
                            labelInfo.setText("<html><div style='text-align: center;'>" +
                                            "<b>Nome:</b> " + nome + "<br>" +
                                            "<b>Username:</b> " + username + "<br>" +
                                            "<b>Email:</b> " + email + "<br>" +
                                            "<b>Tipo:</b> " + tipo + "<br>" +
                                            "<span style='color: green;'><b>Status:</b> ATIVO</span>" +
                                            "</div></html>");
                        }
                    } 
                    else 
                    {
                        labelInfo.setText("<html><span style='color: red;'>Utilizador não encontrado!</span></html>");
                    }
                    
                    rs.close();
                    pstmt.close();
                    conn.close();
                    
                } 
                catch (NumberFormatException ex) 
                {
                    JOptionPane.showMessageDialog(janelaRemover, "Username deve ser um username válido!");
                } 
                catch (SQLException ex) 
                {
                    JOptionPane.showMessageDialog(janelaRemover, "Erro ao buscar utilizador: " + ex.getMessage());
                    System.out.println("Erro SQL: " + ex.getMessage());
                }
            }
        });
        gbc.gridy = 3;
        gbc.insets = new Insets(15, 15, 10, 15);
        gbc.gridx = 0;
        janelaRemover.add(botaoBuscar, gbc);

        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setPreferredSize(new Dimension(200, 40));
        botaoCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        botaoCancelar.setBackground(new Color(128, 128, 128));
        botaoCancelar.setForeground(Color.WHITE);
        botaoCancelar.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                janelaRemover.dispose();
            }
        });
        gbc.gridx = 1;
        janelaRemover.add(botaoCancelar, gbc);
        
        janelaRemover.setVisible(true);
    }

    /**
     * Método para abrir uma janela de busca de utilizador.
     * Esta janela permite ao gestor buscar um utilizador pelo username e exibir suas informações.
     * Se o utilizador estiver ativo, suas informações são exibidas; caso contrário, uma mensagem apropriada é mostrada.
     *
     * @author Guilherme Rodrigues
     */
    private void abrirJanelaBuscaTipo() 
    {
        JFrame janelaRemover = new JFrame("Visualizar Utilizador");
        janelaRemover.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaRemover.setSize(600, 500);
        janelaRemover.setLocationRelativeTo(this);
        janelaRemover.setLayout(new GridBagLayout());
        janelaRemover.getContentPane().setBackground(Color.LIGHT_GRAY);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titulo = new JLabel("Visualizar Utilizador", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.ipady = 15;
        janelaRemover.add(titulo, gbc);
        gbc.gridwidth = 1;
        gbc.ipady = 0;

        // Campo Tipo do Utilizador
        JLabel labelTipo = new JLabel("Tipo do Utilizador:");
        labelTipo.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        janelaRemover.add(labelTipo, gbc);

        String[] tipos = {"Gestor", "Tecnico", "Fabricante"};
        JComboBox<String> comboTipo = new JComboBox<>(tipos);
        comboTipo.setPreferredSize(new Dimension(250, 35));
        comboTipo.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridx = 1;
        janelaRemover.add(comboTipo, gbc);

        // Label para mostrar informações do utilizador
        JLabel labelInfo = new JLabel(" ");
        labelInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        labelInfo.setForeground(Color.BLUE);
        labelInfo.setVerticalAlignment(JLabel.TOP);
        
        // Criar JScrollPane para o label
        JScrollPane scrollPane = new JScrollPane(labelInfo);
        scrollPane.setPreferredSize(new Dimension(550, 250));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Resultados"));
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 15, 15, 15);
        janelaRemover.add(scrollPane, gbc);
        
        // Resetar configurações do GridBagConstraints
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.weighty = 0;

        // Botão Buscar Utilizador
        JButton botaoBuscar = new JButton("Buscar Utilizador");
        botaoBuscar.setPreferredSize(new Dimension(200, 40));
        botaoBuscar.setFont(new Font("Arial", Font.PLAIN, 14));
        botaoBuscar.setBackground(new Color(70, 130, 180));
        botaoBuscar.setForeground(Color.WHITE);
        botaoBuscar.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                String tipoSelecionado = (String) comboTipo.getSelectedItem();
                
                try 
                {
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    
                    String sql = "SELECT nome_utilizador, username_utilizador, email_utilizador, tipo, desativado FROM utilizador WHERE tipo = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setString(1, tipoSelecionado);
                    ResultSet rs = pstmt.executeQuery();
                    
                    StringBuilder resultados = new StringBuilder();
                    resultados.append("<html><div style='text-align: center; padding: 10px;'>");
                    resultados.append("<b>Utilizadores do tipo ").append(tipoSelecionado).append(":</b><br><br>");
                    
                    boolean encontrouUtilizadores = false;
                    
                    while (rs.next()) 
                    {
                        encontrouUtilizadores = true;
                        String nome = rs.getString("nome_utilizador");
                        String username = rs.getString("username_utilizador");
                        String email = rs.getString("email_utilizador");
                        String tipo = rs.getString("tipo");
                        boolean desativado = rs.getBoolean("desativado");
                        
                        resultados.append("─────────────────────────<br>");
                        resultados.append("<b>Nome:</b> ").append(nome).append("<br>");
                        resultados.append("<b>Username:</b> ").append(username).append("<br>");
                        resultados.append("<b>Email:</b> ").append(email).append("<br>");
                        
                        if (desativado) 
                        {
                            resultados.append("<span style='color: red;'><b>Status:</b> DESATIVADO</span><br><br>");
                        } 
                        else 
                        {
                            resultados.append("<span style='color: green;'><b>Status:</b> ATIVO</span><br><br>");
                        }
                    }
                    
                    if (!encontrouUtilizadores) 
                    {
                        resultados.append("<span style='color: red;'>Nenhum utilizador encontrado do tipo ").append(tipoSelecionado).append("!</span>");
                    } 
                    
                    resultados.append("</div></html>");
                    labelInfo.setText(resultados.toString());
                    
                    // Fazer scroll para o topo após carregar os dados
                    SwingUtilities.invokeLater(() -> {
                        scrollPane.getVerticalScrollBar().setValue(0);
                    });
                    
                    rs.close();
                    pstmt.close();
                    conn.close();
                    
                } 
                catch (SQLException ex) 
                {
                    JOptionPane.showMessageDialog(janelaRemover, "Erro ao buscar utilizadores: " + ex.getMessage());
                    System.out.println("Erro SQL: " + ex.getMessage());
                }
            }
        });
        gbc.gridy = 3;
        gbc.insets = new Insets(15, 15, 10, 15);
        gbc.gridx = 0;
        janelaRemover.add(botaoBuscar, gbc);

        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setPreferredSize(new Dimension(200, 40));
        botaoCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        botaoCancelar.setBackground(new Color(128, 128, 128));
        botaoCancelar.setForeground(Color.WHITE);
        botaoCancelar.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                janelaRemover.dispose();
            }
        });
        gbc.gridx = 1;
        janelaRemover.add(botaoCancelar, gbc);
        
        janelaRemover.setVisible(true);
    }

    private void abrirJanelaBuscaTodos() 
    {
        JFrame janelaRemover = new JFrame("Visualizar Utilizador");
        janelaRemover.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaRemover.setSize(600, 500);
        janelaRemover.setLocationRelativeTo(this);
        janelaRemover.setLayout(new GridBagLayout());
        janelaRemover.getContentPane().setBackground(Color.LIGHT_GRAY);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titulo = new JLabel("Visualizar Utilizador", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.ipady = 15;
        janelaRemover.add(titulo, gbc);
        gbc.gridwidth = 1;
        gbc.ipady = 0;

        // Label para mostrar informações do utilizador
        JLabel labelInfo = new JLabel(" ");
        labelInfo.setFont(new Font("Arial", Font.PLAIN, 14));
        labelInfo.setForeground(Color.BLUE);
        labelInfo.setVerticalAlignment(JLabel.TOP);
        
        // Criar JScrollPane para o label
        JScrollPane scrollPane = new JScrollPane(labelInfo);
        scrollPane.setPreferredSize(new Dimension(550, 250));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Resultados"));
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(10, 15, 15, 15);
        janelaRemover.add(scrollPane, gbc);
        
        // Resetar configurações do GridBagConstraints
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.weighty = 0;

        // Botão Buscar Utilizador
        JButton botaoBuscar = new JButton("Buscar Utilizador");
        botaoBuscar.setPreferredSize(new Dimension(200, 40));
        botaoBuscar.setFont(new Font("Arial", Font.PLAIN, 14));
        botaoBuscar.setBackground(new Color(70, 130, 180));
        botaoBuscar.setForeground(Color.WHITE);
        botaoBuscar.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            { 
                try 
                {
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    
                    String sql = "SELECT nome_utilizador, username_utilizador, email_utilizador, tipo, desativado FROM utilizador";
                    PreparedStatement pstmt = conn.prepareStatement(sql);;
                    ResultSet rs = pstmt.executeQuery();
                    
                    StringBuilder resultados = new StringBuilder();
                    resultados.append("<html><div style='text-align: center; padding: 10px;'>");
                    
                    boolean encontrouUtilizadores = false;
                    
                    while (rs.next()) 
                    {
                        encontrouUtilizadores = true;
                        String nome = rs.getString("nome_utilizador");
                        String username = rs.getString("username_utilizador");
                        String email = rs.getString("email_utilizador");
                        String tipo = rs.getString("tipo");
                        boolean desativado = rs.getBoolean("desativado");
                        
                        resultados.append("─────────────────────────<br>");
                        resultados.append("<b>Nome:</b> ").append(nome).append("<br>");
                        resultados.append("<b>Username:</b> ").append(username).append("<br>");
                        resultados.append("<b>Email:</b> ").append(email).append("<br>");
                        
                        if (desativado) 
                        {
                            resultados.append("<span style='color: red;'><b>Status:</b> DESATIVADO</span><br><br>");
                        } 
                        else 
                        {
                            resultados.append("<span style='color: green;'><b>Status:</b> ATIVO</span><br><br>");
                        }
                    }
                    
                    resultados.append("</div></html>");
                    labelInfo.setText(resultados.toString());
                    
                    // Fazer scroll para o topo após carregar os dados
                    SwingUtilities.invokeLater(() -> {
                        scrollPane.getVerticalScrollBar().setValue(0);
                    });
                    
                    rs.close();
                    pstmt.close();
                    conn.close();
                    
                } 
                catch (SQLException ex) 
                {
                    JOptionPane.showMessageDialog(janelaRemover, "Erro ao buscar utilizadores: " + ex.getMessage());
                    System.out.println("Erro SQL: " + ex.getMessage());
                }
            }
        });
        gbc.gridy = 3;
        gbc.insets = new Insets(15, 15, 10, 15);
        gbc.gridx = 0;
        janelaRemover.add(botaoBuscar, gbc);

        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setPreferredSize(new Dimension(200, 40));
        botaoCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        botaoCancelar.setBackground(new Color(128, 128, 128));
        botaoCancelar.setForeground(Color.WHITE);
        botaoCancelar.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                janelaRemover.dispose();
            }
        });
        gbc.gridx = 1;
        janelaRemover.add(botaoCancelar, gbc);
        
        janelaRemover.setVisible(true);
    }

    private void abrirJanelaAtualizarUtilizador() 
    {
        JFrame janelaAtualizar = new JFrame("Atualizar Utilizador");
        janelaAtualizar.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaAtualizar.setSize(700, 600);
        janelaAtualizar.setLocationRelativeTo(this);
        janelaAtualizar.setLayout(new GridBagLayout());
        janelaAtualizar.getContentPane().setBackground(Color.LIGHT_GRAY);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titulo = new JLabel("Atualizar Utilizador", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.ipady = 15;
        janelaAtualizar.add(titulo, gbc);
        gbc.gridwidth = 1;
        gbc.ipady = 0;

        // Campo ID do Utilizador
        JLabel labelId = new JLabel("ID do Utilizador:");
        labelId.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 1;
        janelaAtualizar.add(labelId, gbc);

        JTextField campoId = new JTextField();
        campoId.setPreferredSize(new Dimension(200, 30));
        campoId.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        janelaAtualizar.add(campoId, gbc);

        // Label para mostrar dados atuais do utilizador
        JLabel labelDadosAtuais = new JLabel(" ");
        labelDadosAtuais.setFont(new Font("Arial", Font.PLAIN, 12));
        labelDadosAtuais.setForeground(Color.BLUE);
        labelDadosAtuais.setVerticalAlignment(JLabel.TOP);
        
        JScrollPane scrollDados = new JScrollPane(labelDadosAtuais);
        scrollDados.setPreferredSize(new Dimension(600, 150));
        scrollDados.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollDados.setBorder(BorderFactory.createTitledBorder("Dados Atuais"));
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 0.3;
        janelaAtualizar.add(scrollDados, gbc);

        // Reset das configurações
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 0;
        gbc.weighty = 0;

        // ComboBox para seleção do campo a atualizar
        JLabel labelCampo = new JLabel("Campo a Atualizar:");
        labelCampo.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 3;
        janelaAtualizar.add(labelCampo, gbc);

        JComboBox<String> comboCampos = new JComboBox<>();
        comboCampos.setPreferredSize(new Dimension(200, 30));
        comboCampos.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        janelaAtualizar.add(comboCampos, gbc);

        // Campo para novo valor
        JLabel labelNovoValor = new JLabel("Novo Valor:");
        labelNovoValor.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridx = 0;
        gbc.gridy = 4;
        janelaAtualizar.add(labelNovoValor, gbc);

        JTextField campoNovoValor = new JTextField();
        campoNovoValor.setPreferredSize(new Dimension(200, 30));
        campoNovoValor.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1;
        janelaAtualizar.add(campoNovoValor, gbc);

        // CheckBox para campos boolean
        JCheckBox checkBoxValor = new JCheckBox();
        checkBoxValor.setFont(new Font("Arial", Font.PLAIN, 12));
        checkBoxValor.setBackground(Color.LIGHT_GRAY);
        checkBoxValor.setVisible(false);
        gbc.gridx = 1;
        gbc.gridy = 4;
        janelaAtualizar.add(checkBoxValor, gbc);

        // Botão Buscar Dados
        JButton botaoBuscar = new JButton("Buscar Dados");
        botaoBuscar.setPreferredSize(new Dimension(150, 40));
        botaoBuscar.setFont(new Font("Arial", Font.PLAIN, 14));
        botaoBuscar.setBackground(new Color(70, 130, 180));
        botaoBuscar.setForeground(Color.WHITE);
        
        botaoBuscar.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                String idText = campoId.getText().trim();
                if (idText.isEmpty()) 
                {
                    JOptionPane.showMessageDialog(janelaAtualizar, "Por favor, insira o ID do utilizador.");
                    return;
                }

                try 
                {
                    int id_utilizador = Integer.parseInt(idText);
                    
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    String checkSql = "SELECT * FROM utilizador WHERE id_utilizador = ?";
                    PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                    checkStmt.setInt(1, id_utilizador);
                    ResultSet rs = checkStmt.executeQuery();
                    
                    if (!rs.next()) 
                    {
                        JOptionPane.showMessageDialog(janelaAtualizar, "Utilizador com ID " + id_utilizador + " não encontrado.");
                        return;
                    }
                    
                    // Mostrar dados atuais
                    StringBuilder dadosAtuais = new StringBuilder();
                    dadosAtuais.append("<html><div style='text-align: center; padding: 10px;'>");
                    dadosAtuais.append("─────────────────────────<br>");
                    dadosAtuais.append("<b>ID:</b> ").append(rs.getInt("id_utilizador")).append("<br>");
                    dadosAtuais.append("<b>Nome:</b> ").append(rs.getString("nome_utilizador")).append("<br>");
                    dadosAtuais.append("<b>Username:</b> ").append(rs.getString("username_utilizador")).append("<br>");
                    dadosAtuais.append("<b>Email:</b> ").append(rs.getString("email_utilizador")).append("<br>");
                    dadosAtuais.append("<b>Tipo:</b> ").append(rs.getString("tipo")).append("<br>");
                    
                    boolean desativado = rs.getBoolean("desativado");
                    if (desativado) 
                    {
                        dadosAtuais.append("<span style='color: red;'><b>Status:</b> DESATIVADO</span><br>");
                    } 
                    else 
                    {
                        dadosAtuais.append("<span style='color: green;'><b>Status:</b> ATIVO</span><br>");
                    }
                    
                    dadosAtuais.append("─────────────────────────<br>");
                    dadosAtuais.append("</div></html>");
                    
                    labelDadosAtuais.setText(dadosAtuais.toString());
                    
                    // Limpar e popular ComboBox baseado no tipo
                    comboCampos.removeAllItems();
                    String tipo = rs.getString("tipo");
                    
                    // Campos base para todos os tipos
                    comboCampos.addItem("Nome");
                    comboCampos.addItem("Username");
                    comboCampos.addItem("Email");
                    comboCampos.addItem("Password");
                    comboCampos.addItem("Estado");
                    comboCampos.addItem("Desativado");
                    
                    // Campos específicos por tipo
                    if ("Tecnico".equalsIgnoreCase(tipo)) 
                    {
                        comboCampos.addItem("NIF");
                        comboCampos.addItem("Telefone");
                        comboCampos.addItem("Morada");
                        comboCampos.addItem("Nivel de Certificacao");
                        comboCampos.addItem("Area de Especializacao");
                    }
                    else if ("Fabricante".equalsIgnoreCase(tipo)) 
                    {
                        comboCampos.addItem("NIF");
                        comboCampos.addItem("Telefone");
                        comboCampos.addItem("Morada");
                        comboCampos.addItem("Setor Comercial");
                        comboCampos.addItem("Inicio de atividade");
                    }
                    
                    rs.close();
                    checkStmt.close();
                    conn.close();
                    
                } 
                catch (NumberFormatException ex) 
                {
                    JOptionPane.showMessageDialog(janelaAtualizar, "ID deve ser um número válido.");
                }
                catch (SQLException ex) 
                {
                    JOptionPane.showMessageDialog(janelaAtualizar, "Erro ao buscar utilizador: " + ex.getMessage());
                }
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.insets = new Insets(20, 15, 10, 15);
        janelaAtualizar.add(botaoBuscar, gbc);

        // ActionListener para o ComboBox - controlar visibilidade dos campos
        comboCampos.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                String campoSelecionado = (String) comboCampos.getSelectedItem();
                if (campoSelecionado != null && (campoSelecionado.equals("Estado") || campoSelecionado.equals("Desativado"))) 
                {
                    campoNovoValor.setVisible(false);
                    checkBoxValor.setVisible(true);
                } 
                else 
                {
                    campoNovoValor.setVisible(true);
                    checkBoxValor.setVisible(false);
                }
            }
        });

        // Botão Atualizar
        JButton botaoAtualizar = new JButton("Atualizar");
        botaoAtualizar.setPreferredSize(new Dimension(150, 40));
        botaoAtualizar.setFont(new Font("Arial", Font.BOLD, 14));
        botaoAtualizar.setBackground(new Color(34, 139, 34));
        botaoAtualizar.setForeground(Color.WHITE);
        
        botaoAtualizar.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                String idText = campoId.getText().trim();
                String campoSelecionado = (String) comboCampos.getSelectedItem();
                
                if (idText.isEmpty() || campoSelecionado == null) 
                {
                    JOptionPane.showMessageDialog(janelaAtualizar, "Preencha todos os campos obrigatórios.");
                    return;
                }
                
                try 
                {
                    int id_utilizador = Integer.parseInt(idText);
                    String novoValor = "";
                    boolean valorBoolean = false;
                    
                    // Verificar se é campo boolean ou texto
                    if (campoSelecionado.equals("Estado") || campoSelecionado.equals("Desativado")) 
                    {
                        valorBoolean = checkBoxValor.isSelected();
                    } 
                    else 
                    {
                        novoValor = campoNovoValor.getText().trim();
                        if (novoValor.isEmpty()) 
                        {
                            JOptionPane.showMessageDialog(janelaAtualizar, "Digite o novo valor.");
                            return;
                        }
                        
                        // Validações específicas
                        if (campoSelecionado.equals("Telefone")) 
                        {
                            if (!novoValor.matches("[923]\\d{8}")) 
                            {
                                JOptionPane.showMessageDialog(janelaAtualizar, "Número de telefone inválido! Deve ter 9 dígitos e começar com 9, 2 ou 3.");
                                return;
                            }
                        }
                        
                        if (campoSelecionado.equals("NIF")) 
                        {
                            if (!novoValor.matches("\\d{9}")) 
                            {
                                JOptionPane.showMessageDialog(janelaAtualizar, "NIF inválido! Deve ter 9 dígitos.");
                                return;
                            }
                        }
                    }
                    
                    // Determinar o tipo de utilizador para saber qual método chamar
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    String tipoSql = "SELECT tipo FROM utilizador WHERE id_utilizador = ?";
                    PreparedStatement tipoStmt = conn.prepareStatement(tipoSql);
                    tipoStmt.setInt(1, id_utilizador);
                    ResultSet tipoRs = tipoStmt.executeQuery();
                    
                    if (tipoRs.next()) 
                    {
                        String tipo = tipoRs.getString("tipo");
                        
                        // Chamar método apropriado baseado no campo selecionado
                        String campo = obterNomeCampo(campoSelecionado, tipo);
                        
                        if (campoSelecionado.equals("Estado") || campoSelecionado.equals("Desativado")) 
                        {
                            // Para campos boolean, usar String.valueOf para converter boolean para string
                            String valorString = String.valueOf(valorBoolean);
                            atualizarUtilizador(id_utilizador, campo, valorString);
                            
                            // Se desativado = true, também definir estado = false
                            if (campoSelecionado.equals("Desativado") && valorBoolean) 
                            {
                                atualizarUtilizador(id_utilizador, "estado_utilizador", "false");
                            }
                            // Se desativado = false, também definir estado = true  
                            else if (campoSelecionado.equals("Desativado") && !valorBoolean)
                            {
                                atualizarUtilizador(id_utilizador, "estado_utilizador", "true");
                            }
                        }
                        else 
                        {
                            // Determinar se é campo da tabela principal ou especializada
                            if (campoSelecionado.equals("Nome") || campoSelecionado.equals("Username") || 
                                campoSelecionado.equals("Email") || campoSelecionado.equals("Password")) 
                            {
                                atualizarUtilizador(id_utilizador, campo, novoValor);
                            } 
                            else 
                            {
                                atualizarEspecializado(id_utilizador, campo, novoValor, tipo);
                            }
                        }
                        
                        JOptionPane.showMessageDialog(janelaAtualizar, "Utilizador atualizado com sucesso!");
                        
                        // Limpar campos após sucesso
                        campoId.setText("");
                        campoNovoValor.setText("");
                        checkBoxValor.setSelected(false);
                        comboCampos.removeAllItems();
                        labelDadosAtuais.setText(" ");
                    }
                    
                    tipoRs.close();
                    tipoStmt.close();
                    conn.close();
                    
                } 
                catch (NumberFormatException ex) 
                {
                    JOptionPane.showMessageDialog(janelaAtualizar, "ID deve ser um número válido.");
                }
                catch (SQLException ex) 
                {
                    JOptionPane.showMessageDialog(janelaAtualizar, "Erro ao atualizar utilizador: " + ex.getMessage());
                }
            }
        });
        
        gbc.gridx = 1;
        janelaAtualizar.add(botaoAtualizar, gbc);

        // Botão Cancelar
        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setPreferredSize(new Dimension(150, 40));
        botaoCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        botaoCancelar.setBackground(new Color(128, 128, 128));
        botaoCancelar.setForeground(Color.WHITE);
        botaoCancelar.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                janelaAtualizar.dispose();
            }
        });
        
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        janelaAtualizar.add(botaoCancelar, gbc);
        
        janelaAtualizar.setVisible(true);
    }

    /**
     *  Método para atualizar um utilizador na tabela principal.
     *  Este método recebe o ID do utilizador, o campo a ser atualizado e o novo valor.
     *  Ele executa uma atualização na tabela 'utilizador' com base nos parâmetros fornecidos.
     *  Se o campo for booleano, converte o novo valor para boolean antes de atualizar.
     * 
     *  @author Guilherme Rodrigues
     * 
     * @param id_utilizador
     * @param campo
     * @param novoValor
     */
    private void atualizarUtilizador(int id_utilizador, String campo, String novoValor) 
    {
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            
            String sql = "UPDATE utilizador SET " + campo + " = ? WHERE id_utilizador = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            
            // Verificar se é campo boolean
            if (campo.equals("estado_utilizador") || campo.equals("desativado")) 
            {
                boolean valorBoolean = Boolean.parseBoolean(novoValor);
                pstmt.setBoolean(1, valorBoolean);
            } 
            else 
            {
                pstmt.setString(1, novoValor);
            }
            
            pstmt.setInt(2, id_utilizador);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) 
            {
                System.out.println("Campo " + campo + " atualizado com sucesso!");
            } 
            else 
            {
                System.out.println("Nenhuma linha foi atualizada.");
            }
            
            pstmt.close();
            conn.close();
            
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao atualizar utilizador: " + e.getMessage());
            throw new RuntimeException("Erro na atualização: " + e.getMessage());
        }
    }

    /**
     *  Método para atualizar um utilizador na tabela especializada.
     *  Este método recebe o ID do utilizador, o campo a ser atualizado e o novo valor.
     *  Ele executa uma atualização na tabela 'tecnico' ou 'fabricante' com base nos parâmetros fornecidos.
     *  Ele também verifica o tipo de utilizador para determinar a tabela correta.
     *
     *  @author Guilherme Rodrigues
     * 
     * @param id_utilizador
     * @param campo
     * @param novoValor
     * @param tipo
     */
    private void atualizarEspecializado(int id_utilizador, String campo, String novoValor, String tipo) 
    {
        try 
        {
            Conexao_BD conexaoBD = new Conexao_BD();
            Connection conn = conexaoBD.conexao();
            
            String tabela = "";
            if ("Tecnico".equalsIgnoreCase(tipo)) 
            {
                tabela = "tecnico";
            } 
            else if ("Fabricante".equalsIgnoreCase(tipo)) 
            {
                tabela = "fabricante";
            } 
            else 
            {
                throw new IllegalArgumentException("Tipo de utilizador inválido: " + tipo);
            }
            
            String sql = "UPDATE " + tabela + " SET " + campo + " = ? WHERE id_utilizador = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, novoValor);
            pstmt.setInt(2, id_utilizador);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) 
            {
                System.out.println("Campo " + campo + " atualizado com sucesso na tabela " + tabela + "!");
            } 
            else 
            {
                System.out.println("Nenhuma linha foi atualizada na tabela " + tabela + ".");
            }
            
            pstmt.close();
            conn.close();
            
        } 
        catch (SQLException e) 
        {
            System.out.println("Erro ao atualizar " + tipo + ": " + e.getMessage());
            throw new RuntimeException("Erro na atualização: " + e.getMessage());
        }
    }

    /**
     *  Método para obter o nome do campo correspondente ao campo selecionado.
     *  Este método recebe o campo selecionado e o tipo de utilizador (Tecnico ou Fabricante)
     *  e retorna o nome do campo correspondente na tabela.
     *
     *  @author Guilherme Rodrigues
     *
     * @param campoSelecionado
     * @param tipo
     * @return
     */
    private String obterNomeCampo(String campoSelecionado, String tipo) 
    {
        switch (campoSelecionado) 
        {
            case "Nome":
                return "nome_utilizador";
            case "Username":
                return "username_utilizador";
            case "Email":
                return "email_utilizador";
            case "Estado":
                return "estado_utilizador";
            case "Desativado":
                return "desativado";
            case "NIF":
                return "Tecnico".equalsIgnoreCase(tipo) ? "nif_tecnico" : "nif_fabricante";
            case "Telefone":
                return "Tecnico".equalsIgnoreCase(tipo) ? "telefone_tecnico" : "telefone_fabricante";
            case "Morada":
                return "Tecnico".equalsIgnoreCase(tipo) ? "morada_tecnico" : "morada_fabricante";
            case "Nivel de Certificacao":
                return "nivel_certificacao_tecnico";
            case "Area de Especializacao":
                return "area_especializacao_tecnico";
            case "Setor Comercial":
                return "setor_comercial_fabricante";
            case "Inicio de atividade":
                return "inicio_atividade_fabricante";
            default:
                return "";
        }
    }

    private void abrirJanelaInserirLicenca() 
    {
        JFrame janelaInserir = new JFrame("Inserir Licença");
        janelaInserir.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        janelaInserir.setSize(600, 400);
        janelaInserir.setLocationRelativeTo(this);
        janelaInserir.setLayout(new GridBagLayout());
        janelaInserir.getContentPane().setBackground(Color.LIGHT_GRAY);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        JLabel titulo = new JLabel("Inserir Licença", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.ipady = 15;
        janelaInserir.add(titulo, gbc);
        gbc.gridwidth = 1;
        gbc.ipady = 0;

        // Label para licença
        JLabel labelLicenca = new JLabel("Nome da Licença:");
        labelLicenca.setFont(new Font("Arial", Font.BOLD, 14));
        labelLicenca.setForeground(Color.DARK_GRAY);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        janelaInserir.add(labelLicenca, gbc);

        // Campo de texto para licença
        JTextField campoLicenca = new JTextField();
        campoLicenca.setFont(new Font("Arial", Font.PLAIN, 14));
        campoLicenca.setPreferredSize(new Dimension(400, 30));
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(5, 15, 20, 15);
        janelaInserir.add(campoLicenca, gbc);

        // Resetar configurações do GridBagConstraints
        gbc.gridwidth = 1;
        gbc.insets = new Insets(15, 15, 10, 15);

        // Botão Inserir
        JButton botaoInserir = new JButton("Inserir Licença");
        botaoInserir.setPreferredSize(new Dimension(200, 40));
        botaoInserir.setFont(new Font("Arial", Font.PLAIN, 14));
        botaoInserir.setBackground(new Color(70, 130, 180));
        botaoInserir.setForeground(Color.WHITE);
        botaoInserir.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            { 
                String licenca = campoLicenca.getText().trim();
                
                if (licenca.isEmpty()) 
                {
                    JOptionPane.showMessageDialog(janelaInserir, "Por favor, insira o nome da licença.");
                    return;
                }
                
                String sql = "INSERT INTO licencas (nome_licenca) VALUES (?)";
                try 
                {
                    Conexao_BD conexaoBD = new Conexao_BD();
                    Connection conn = conexaoBD.conexao();
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, licenca);
                    stmt.executeUpdate();
                    
                    JOptionPane.showMessageDialog(janelaInserir, "Licença inserida com sucesso!");
                    campoLicenca.setText(""); // Limpar o campo após inserção
                    
                    stmt.close();
                    conn.close();
                } 
                catch (SQLException ex) 
                {
                    JOptionPane.showMessageDialog(janelaInserir, "Erro ao inserir licença: " + ex.getMessage());
                    System.out.println("Erro SQL: " + ex.getMessage());
                }
            }
        });
        gbc.gridx = 0;
        gbc.gridy = 3;
        janelaInserir.add(botaoInserir, gbc);

        // Botão Cancelar
        JButton botaoCancelar = new JButton("Cancelar");
        botaoCancelar.setPreferredSize(new Dimension(200, 40));
        botaoCancelar.setFont(new Font("Arial", Font.BOLD, 14));
        botaoCancelar.setBackground(new Color(128, 128, 128));
        botaoCancelar.setForeground(Color.WHITE);
        botaoCancelar.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                janelaInserir.dispose();
            }
        });
        gbc.gridx = 1;
        janelaInserir.add(botaoCancelar, gbc);
        
        janelaInserir.setVisible(true);
    }
}