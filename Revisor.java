public class Revisor extends Utilizadores {

    // Atributos necessários para um Revisor
    private String especializacao; // "ciências", "literatura", "artes", etc.
    private String formacaoAcademica;
  
    // Construtor
    public Revisor(String nome, String login, String password, String estado, String email, int tipo,
                    String especializacao, String formacaoAcademica) {
      super(nome, login, password, estado, email, tipo);
      this.especializacao = especializacao;
      this.formacaoAcademica = formacaoAcademica;
    }
  
    // Getters e setters
    public String getEspecializacao() {
      return especializacao;
    }
  
    public void setEspecializacao(String especializacao) {
      this.especializacao = especializacao;
    }
  
    public String getFormacaoAcademica() {
      return formacaoAcademica;
    }
  
    public void setFormacaoAcademica(String formacaoAcademica) {
      this.formacaoAcademica = formacaoAcademica;
    }
  
    // Método toString para representação textual
    @Override
    public String toString() {
      return "Revisor{" +
          "especializacao='" + especializacao + '\'' +
          ", formacaoAcademica='" + formacaoAcademica + '\'' +
          ", " + super.toString() +
          '}';
    }
  }
  