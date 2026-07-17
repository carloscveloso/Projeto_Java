package dentalcare.model;

public class Funcionario extends Utilizador {
    private int numeroCarteiraProfissional;
    private String especialidade;

    public Funcionario(String nome, String email, String password, String numeroCartaoCidadao, String numeroFiscal, String telefone, String morada, String localidade, int numeroCarteiraProfissional, String especialidade) {
        super(nome, email, password, numeroCartaoCidadao, numeroFiscal, telefone, morada, localidade, PerfilUtilizador.FUNCIONARIO);
        this.numeroCarteiraProfissional = numeroCarteiraProfissional;
        this.especialidade = especialidade;
    }

    public int getNumeroCarteiraProfissional() { return numeroCarteiraProfissional; }
    public String getEspecialidade() { return especialidade; }
}
