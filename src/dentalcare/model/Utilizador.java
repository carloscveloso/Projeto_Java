package dentalcare.model;

import java.io.Serializable;

public class Utilizador implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id = String.valueOf(System.nanoTime());
    private String nome;
    private String email;
    private String password;
    private String numeroCartaoCidadao;
    private String numeroFiscal;
    private String telefone;
    private String morada;
    private String localidade;
    private PerfilUtilizador perfil;
    private boolean ativo = true;

    public Utilizador(String nome, String email, String password, String numeroCartaoCidadao, String numeroFiscal, String telefone, String morada, String localidade, PerfilUtilizador perfil) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.numeroCartaoCidadao = numeroCartaoCidadao;
        this.numeroFiscal = numeroFiscal;
        this.telefone = telefone;
        this.morada = morada;
        this.localidade = localidade;
        this.perfil = perfil;
    }

    public String getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public boolean isAtivo() { return ativo; }
    public PerfilUtilizador getPerfil() { return perfil; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
