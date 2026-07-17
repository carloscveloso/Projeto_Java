package dentalcare.model;

public class Cliente extends Utilizador {
    public Cliente(String nome, String email, String password, String numeroCartaoCidadao, String numeroFiscal, String telefone, String morada, String localidade) {
        super(nome, email, password, numeroCartaoCidadao, numeroFiscal, telefone, morada, localidade, PerfilUtilizador.CLIENTE);
    }
}
