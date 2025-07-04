import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JFormattedTextField.AbstractFormatter;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

public class Registo extends JFrame 
{
    private Container c;

    // Campos de texto com nome fixo
    private JTextField Nome;
    private JTextField Username;
    private JPasswordField Password;
    private JTextField Email;
    private JTextField NIF;
    private JTextField Telefone;
    private JTextField Morada;
    private JTextField NivelCertificacao;
    private JTextField AreaEspecializacao;
    private JTextField SectorComercial;
    private JDatePickerImpl DataInicio;

    public Registo() 
    {
        super("Formulário de Registo");
        c = getContentPane();
        c.setLayout(new GridBagLayout());
        c.setBackground(Color.LIGHT_GRAY);
    }


    public Registo(int num) 
    {
        super("Formulário de Registo");
        c = getContentPane();
        c.setLayout(new GridBagLayout()); 
        c.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Registo", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.ipady = 10;
        c.add(titulo, gbc);
        gbc.gridwidth = 1;
        gbc.ipady = 0;

        int linha = 1;
        Nome = adicionaCampo("Nome", c, gbc, linha++);
        Username = adicionaCampo("Username", c, gbc, linha++);
        Password = new JPasswordField();
        adicionaCampoComCampoPersonalizado("Password", Password, c, gbc, linha++);
        Email = adicionaCampo("Email", c, gbc, linha++);
        NIF = adicionaCampo("NIF", c, gbc, linha++);
        Telefone = adicionaCampo("Telefone", c, gbc, linha++);
        Morada = adicionaCampo("Morada", c, gbc, linha++);

        if(num == 1)
        {
            NivelCertificacao = adicionaCampo("Nivel Certificacao", c, gbc, linha++);
            AreaEspecializacao = adicionaCampo("Area Especializacao", c, gbc, linha++);
        }
        else
        {
            SectorComercial = adicionaCampo("Sector Comercial", c, gbc, linha++);
            DataInicio = adicionarData("Data de Inicio de Actividade", c, gbc, linha++);
        }

        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.gridwidth = 1;

        JButton botao_registar = new JButton("Registar");
        botao_registar.setPreferredSize(new Dimension(200, 40));
        botao_registar.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                botao_registar_ActionPerformed(e);
            }
        });
        c.add(botao_registar, gbc);

        gbc.gridx = 1;
        JButton botao_cancelar = new JButton("Cancelar");
        botao_cancelar.setPreferredSize(new Dimension(200, 40));
        botao_cancelar.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                botao_cancelar_ActionPerformed(e);
            }
        });
        c.add(botao_cancelar, gbc);
    }

    /**
     * Metodo para adicionar campos de texto
     * 
     * @author Guilherme Rodrigues
     * 
     * @param labelTexto
     * @param c
     * @param gbc
     * @param linha
     * @return
     */
    private JTextField adicionaCampo(String labelTexto, Container c, GridBagConstraints gbc, int linha) 
    {
        JLabel label = new JLabel(labelTexto + ":");
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = linha;
        c.add(label, gbc);

        JTextField textField = new JTextField();
        textField.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        c.add(textField, gbc);

        return textField;
    }

    /**
     * Metodo para adicionar campos de texto com campos personalizados
     * 
     * @author Guilherme Rodrigues
     * 
     * @param labelTexto
     * @param campo
     * @param c
     * @param gbc
     * @param linha
     */
    private void adicionaCampoComCampoPersonalizado(String labelTexto, JTextField campo, Container c, GridBagConstraints gbc, int linha) 
    {
        JLabel label = new JLabel(labelTexto + ":");
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = linha;
        c.add(label, gbc);

        campo.setPreferredSize(new Dimension(250, 30));
        gbc.gridx = 1;
        c.add(campo, gbc);
    }

    /**
     * Metodo para adicionar campos de data
     * 
     * @author Guilherme Rodrigues
     * 
     * @param labelTexto
     * @param c
     * @param gbc
     * @param linha
     * @return
     */
    private JDatePickerImpl adicionarData(String labelTexto, Container c, GridBagConstraints gbc, int linha) 
    {
        JLabel label = new JLabel(labelTexto + ":");
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0;
        gbc.gridy = linha;
        c.add(label, gbc);

        UtilDateModel model = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Hoje");
        p.put("text.month", "Mês");
        p.put("text.year", "Ano");

        JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel, criarDateFormatter());
        datePicker.setPreferredSize(new Dimension(250, 30));

        gbc.gridx = 1;
        c.add(datePicker, gbc);

        return datePicker;
    }

    /**
     * Metodo para criar o formatter da data
     * 
     * @author Guilherme Rodrigues
     * 
     * @return
     */
    private AbstractFormatter criarDateFormatter() 
    {
        return new AbstractFormatter() 
        {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

            @Override
            public Object stringToValue(String text) throws ParseException 
            {
                return dateFormatter.parseObject(text);
            }

            @Override
            public String valueToString(Object value) throws ParseException 
            {
                if (value != null) {
                    Calendar cal = (Calendar) value;
                    return dateFormatter.format(cal.getTime());
                }
                return "";
            }
        };
    }

    /**
     * Metodo para o botao cancelar
     * 
     * @author Guilherme Rodrigues
     * 
     * @param e
     */
    private void botao_cancelar_ActionPerformed(ActionEvent e)  
    {
        System.out.println("A voltar para o registo");
        Registo.this.dispose();
        Main.MenuRegisto();
    }   

    /**
     * Metodo para o botao registar
     * 
     * @author Guilherme Rodrigues
     * 
     * @param e
     */
    private void botao_registar_ActionPerformed(ActionEvent e) 
    {
        System.out.println("A registar técnico...");
        
        // Capturar dados dos campos GUI
        String nome_utilizador = Nome.getText().trim();
        String username_utilizador = Username.getText().trim();
        String password_utilizador = new String(Password.getPassword());
        String email_utilizador = Email.getText().trim();
        String nif = NIF.getText().trim();
        String telefone = Telefone.getText().trim();
        String morada = Morada.getText().trim();
        
        // Validações básicas
        if (nome_utilizador.isEmpty() || username_utilizador.isEmpty() || password_utilizador.isEmpty() || email_utilizador.isEmpty() || nif.isEmpty() || telefone.isEmpty() || morada.isEmpty()) 
        {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos obrigatórios!");
            return;
        }
        
        // Validar NIF
        if (!nif.matches("\\d{9}")) 
        {
            JOptionPane.showMessageDialog(this, "NIF inválido! Deve ter 9 dígitos.");
            return;
        }
        int nifInt = Integer.parseInt(nif);
        
        // Validar telefone
        if (!telefone.matches("[923]\\d{8}")) 
        {
            JOptionPane.showMessageDialog(this, "Número de telefone inválido! Deve ter 9 dígitos e começar com 9, 2 ou 3.");
            return;
        }
        int telefoneInt = Integer.parseInt(telefone);
        
        // Capturar dados específicos do técnico (se existirem)
        String nivelCertificacaoStr = "";
        String areaEspecializacao = "";
        
        if (NivelCertificacao != null) 
        {
            nivelCertificacaoStr = NivelCertificacao.getText().trim();
        }
        if (AreaEspecializacao != null) 
        {
            areaEspecializacao = AreaEspecializacao.getText().trim();
        }
        
        // Validar nível de certificação
        int nivelCertificacao = 1; // valor padrão
        if (!nivelCertificacaoStr.isEmpty()) 
        {
            try 
            {
                nivelCertificacao = Integer.parseInt(nivelCertificacaoStr);
                if (nivelCertificacao < 1 || nivelCertificacao > 7) 
                {
                    JOptionPane.showMessageDialog(this, "Nível de certificação deve estar entre 1 e 7!");
                    return;
                }
            } 
            catch (NumberFormatException ex) 
            {
                JOptionPane.showMessageDialog(this, "Nível de certificação deve ser um número!");
                return;
            }
        }
        
        // Primeira query - inserir na tabela utilizador
        String sql1 = "INSERT INTO utilizador (nome_utilizador, username_utilizador, password_utilizador, email_utilizador, tipo, estado_utilizador, Pedido_Remocao, desativado) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_utilizador";
        int id_utilizador = -1;
        
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
                    id_utilizador = rs.getInt(1);
                }
            }
            
            System.out.println("Utilizador registado! ID: " + id_utilizador);
            
        } 
        catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(this, "Erro ao registar utilizador: " + ex.getMessage());
            System.out.println("Erro ao registar Utilizador: " + ex.getMessage());
            return;
        }
        
        // Verificar se o ID foi gerado corretamente
        if (id_utilizador == -1) 
        {
            JOptionPane.showMessageDialog(this, "Erro ao obter ID do utilizador.");
            return;
        }
        
        // Segunda query - inserir na tabela tecnico
        String sql2 = "INSERT INTO tecnico (id_utilizador, nif_tecnico, telefone_tecnico, morada_tecnico, nivel_certificacao, area_especializacao) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
        
        try 
        {
            Connection conn = new Conexao_BD().conexao();
            PreparedStatement pstmt = conn.prepareStatement(sql2);
            
            pstmt.setInt(1, id_utilizador);
            pstmt.setInt(2, nifInt);
            pstmt.setInt(3, telefoneInt);
            pstmt.setString(4, morada);
            pstmt.setInt(5, nivelCertificacao);
            pstmt.setString(6, areaEspecializacao.isEmpty() ? "Geral" : areaEspecializacao);
            
            pstmt.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Técnico registado com sucesso na base de dados!");
            System.out.println("Técnico registado com sucesso!");
            
            // Fechar janela e voltar ao menu
            Registo.this.dispose();
            //Main.menu_Entrada();
            Main.caixaDeDialogo();
            
        } catch (SQLException ex) 
        {
            JOptionPane.showMessageDialog(this, "Erro ao registar técnico: " + ex.getMessage());
            System.out.println("Erro ao registar Técnico: " + ex.getMessage());
        }
    }

    public void RegistoGestor() 
    {
        //super("Formulário de Registo");
        c = getContentPane();
        c.setLayout(new GridBagLayout()); 
        c.setBackground(Color.LIGHT_GRAY);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titulo = new JLabel("Registo", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.ipady = 10;
        c.add(titulo, gbc);
        gbc.gridwidth = 1;
        gbc.ipady = 0;

        int linha = 1;
        Nome = adicionaCampo("Nome", c, gbc, linha++);
        Username = adicionaCampo("Username", c, gbc, linha++);
        Password = new JPasswordField();
        adicionaCampoComCampoPersonalizado("Password", Password, c, gbc, linha++);
        Email = adicionaCampo("Email", c, gbc, linha++);
        NIF = adicionaCampo("NIF", c, gbc, linha++);
        Telefone = adicionaCampo("Telefone", c, gbc, linha++);
        Morada = adicionaCampo("Morada", c, gbc, linha++);

        gbc.gridx = 0;
        gbc.gridy = linha;
        gbc.gridwidth = 1;

        JButton botao_registar = new JButton("Registar");
        botao_registar.setPreferredSize(new Dimension(200, 40));
        botao_registar.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                System.out.println("A registar técnico...");
        
                // Capturar dados dos campos GUI
                String nome_utilizador = Nome.getText().trim();
                String username_utilizador = Username.getText().trim();
                String password_utilizador = new String(Password.getPassword());
                String email_utilizador = Email.getText().trim();
                String nif = NIF.getText().trim();
                String telefone = Telefone.getText().trim();
                String morada = Morada.getText().trim();
                
                // Validações básicas
                if (nome_utilizador.isEmpty() || username_utilizador.isEmpty() || password_utilizador.isEmpty() || email_utilizador.isEmpty() || nif.isEmpty() || telefone.isEmpty() || morada.isEmpty()) 
                {
                    JOptionPane.showMessageDialog(Registo.this, "Por favor, preencha todos os campos obrigatórios!");
                    return;
                }
                
                // Primeira query - inserir na tabela utilizador
                String sql1 = "INSERT INTO utilizador (nome_utilizador, username_utilizador, password_utilizador, email_utilizador, tipo, estado_utilizador, Pedido_Remocao, desativado) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING id_utilizador";
                int id_utilizador = -1;
                
                try 
                {
                    Connection conn = new Conexao_BD().conexao();
                    PreparedStatement pstmt = conn.prepareStatement(sql1);
                    
                    // Definir parâmetros da query
                    pstmt.setString(1, nome_utilizador);
                    pstmt.setString(2, username_utilizador);
                    pstmt.setString(3, password_utilizador);
                    pstmt.setString(4, email_utilizador);
                    pstmt.setString(5, "Gestor");
                    pstmt.setBoolean(6, false);
                    pstmt.setBoolean(7, false);
                    pstmt.setBoolean(8, false);
                    
                    // Executar a query e capturar o ID gerado
                    try (ResultSet rs = pstmt.executeQuery()) 
                    {
                        if (rs.next()) 
                        {
                            id_utilizador = rs.getInt(1);
                        }
                    }
                    
                    System.out.println("Utilizador registado! ID: " + id_utilizador);
                    dispose();
                    
                } 
                catch (SQLException ex) 
                {
                    JOptionPane.showMessageDialog(Registo.this, "Erro ao registar utilizador: " + ex.getMessage());
                    System.out.println("Erro ao registar Utilizador: " + ex.getMessage());
                    return;
                }
                
                // Verificar se o ID foi gerado corretamente
                if (id_utilizador == -1) 
                {
                    JOptionPane.showMessageDialog(Registo.this, "Erro ao obter ID do utilizador.");
                    return;
                }

            }
        });
        c.add(botao_registar, gbc);

        gbc.gridx = 1;
        JButton botao_cancelar = new JButton("Cancelar");
        botao_cancelar.setPreferredSize(new Dimension(200, 40));
        botao_cancelar.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
                dispose();
            }
        });
        c.add(botao_cancelar, gbc);
    }
}
