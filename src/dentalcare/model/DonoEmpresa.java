package dentalcare.model;

import java.util.ArrayList;
import java.util.List;

public class DonoEmpresa extends Utilizador {
    private final List<Empresa> empresas = new ArrayList<>();

    public DonoEmpresa(String nome, String email, String password, String numeroCartaoCidadao, String numeroFiscal, String telefone, String morada, String localidade) {
        super(nome, email, password, numeroCartaoCidadao, numeroFiscal, telefone, morada, localidade, PerfilUtilizador.DONO_EMPRESA);
    }

    public void adicionarEmpresa(Empresa empresa) { empresas.add(empresa); }
    public List<Empresa> getEmpresas() { return empresas; }
}
