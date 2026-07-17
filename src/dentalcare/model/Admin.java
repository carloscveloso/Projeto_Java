package dentalcare.model;

public class Admin extends Utilizador {
    public Admin(String nome, String email, String password, String numeroCartaoCidadao, String numeroFiscal, String telefone, String morada, String localidade) {
        super(nome, email, password, numeroCartaoCidadao, numeroFiscal, telefone, morada, localidade, PerfilUtilizador.ADMIN);
    }
}
