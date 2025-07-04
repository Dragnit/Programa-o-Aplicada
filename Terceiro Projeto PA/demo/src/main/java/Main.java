import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import javax.swing.ButtonGroup;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
public class Main {
    
    public static void main(String[] args) 
    {
        Conexao_BD bd = new Conexao_BD();
        try 
        {
            Connection conn = bd.conexao(); 

            bd.primeiroRegisto(); 
            caixaDeDialogo();   
            //menu_Entrada();
            conn.close(); 
        } 
        catch (SQLException e) 
        {
            e.printStackTrace(); // Exibe detalhes do erro no terminal
        }
    }
    
    public static void menu_Entrada()
    {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        while (running) 
        {
            System.out.println("\n=== Menu de Login e Registo ===");
            System.out.println("1. Login");
            System.out.println("2. Registo");
            System.out.println("3. Sair");
            System.out.print("Escolha uma opção: ");
            
            int option = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha
            
            switch (option) 
            {
                case 1:
                    System.out.print("Digite o username: ");
                    String username = scanner.nextLine();
                    System.out.print("Digite a senha: ");
                    String password = scanner.nextLine();
                    Conexao_BD conexao = new Conexao_BD(); 
                    conexao.verificarLogin(username, password);;
                    break;
                case 2:
                    System.out.println("Escolha o tipo de utilizador: ");
                    System.out.println("1. Tecnico");
                    System.out.println("2. Fabricante");
                    int option2 = scanner.nextInt();
                    if (option2 == 1)
                    {
                        Conexao_BD conexao2 = new Conexao_BD();
                        conexao2.registaTecnico();
                    }
                    else
                    {
                        Conexao_BD conexao3 = new Conexao_BD();
                        conexao3.registaFabricante();
                    }
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
                break;
            }
        }
    }


   public static void caixaDeDialogo() 
   {
        JTextField username = new JTextField();
        JTextField password = new JPasswordField();

        JLabel hyperlink = new JLabel("Ainda não possui uma conta? Clique aqui");
        hyperlink.setForeground(Color.BLUE.darker());
        hyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Object[] message = {
            "Username:", username,
            "Password:", password,
            hyperlink
        };

        // Criar um JOptionPane e meter dentro de um JDialog
        JOptionPane option = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = option.createDialog(null, "Login");

        // Evento do hyperlink para fechar esta janela e abrir outra
        hyperlink.addMouseListener(new MouseAdapter() 
        {
            public void mouseClicked(MouseEvent e) 
            {
                System.out.println("Nao tinha conta!");
                dialog.setVisible(false);  // fecha esta
                MenuRegisto();
            }
        });

        dialog.setVisible(true); // Mostrar a caixa de login

        Object selectedValue = option.getValue();

        if (selectedValue != null && (int) selectedValue == JOptionPane.OK_OPTION) {
            //System.out.println("Username: " + username.getText());
            //System.out.println("Password: " + password.getText());
            Conexao_BD conexao = new Conexao_BD(); 
            conexao.verificarLogin(username.getText(), password.getText());
        } else {
            //System.out.println("A sair do programa");
            JOptionPane.showMessageDialog(null, "A sair do Programa!", "Erro", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }


    public static void MenuRegisto()
    {

        Conexao_BD bd = new Conexao_BD();
        Conexao_BD conexaoBD = new Conexao_BD();

        JRadioButton button = new JRadioButton("FABRICANTE");
        JRadioButton button2 = new JRadioButton("TECNICO");

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(button);
        grupo.add(button2);

        Object[] message = {
            "Escolha o tipo de utilizador:", button, button2
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Registo", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) 
        {
            if (button.isSelected()) 
            {
                System.out.println("Selecionaste: FABRICANTE");
                Registo janelaRegisto = new Registo( 1);
                janelaRegisto.setSize(600, 600);
                janelaRegisto.setLocationRelativeTo(null);
                janelaRegisto.setVisible(true);
            } 
            else if (button2.isSelected()) 
            {
                System.out.println("Selecionaste: TECNICO");
                Registo janelaRegisto = new Registo(2);
                janelaRegisto.setSize(600, 600);
                janelaRegisto.setLocationRelativeTo(null);
                janelaRegisto.setVisible(true);
            } 
            else 
            {
                System.out.println("Não escolheste nada!");
            }
        }
        else
        {
            System.out.println("A voltar para o Login");
            caixaDeDialogo();
        }
    }

}