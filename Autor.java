import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;

public class Autor extends Utilizadores {

    // Atributos específicos para um Autor
    private String estiloLiterario; // "ciências", "literatura", "artes", etc.
    private String dataInicioAtividade; // Data de início de atividade formatada

    // Construtor
    public Autor(String nome, String login, String password, String estado, String email, int tipo,
                 String estiloLiterario, String inicioAtividade) {
        super(nome, login, password, estado, email, tipo);
        this.estiloLiterario = estiloLiterario;

        // Converte a string para um objeto Date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dataInicioAtividadeDate = null;
        try {
            dataInicioAtividadeDate = sdf.parse(inicioAtividade);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Formata a data de início de atividade
        this.dataInicioAtividade = sdf.format(dataInicioAtividadeDate);
    }

    // Getters e setters
    public String getEstiloLiterario() {
        return estiloLiterario;
    }

    public void setEstiloLiterario(String estiloLiterario) {
        this.estiloLiterario = estiloLiterario;
    }

    public String getDataInicioAtividade() {
        return dataInicioAtividade;
    }

    public void setDataInicioAtividade(String dataInicioAtividade) {
        this.dataInicioAtividade = dataInicioAtividade;
    }

    // Método toString para representação textual
    @Override
    public String toString() {
        return "Autor{" +
                "estiloLiterario='" + estiloLiterario + '\'' +
                ", dataInicioAtividade='" + dataInicioAtividade + '\'' +
                ", " + super.toString() +
                '}';
    }
}
