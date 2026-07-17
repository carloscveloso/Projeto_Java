package dentalcare;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class LegacyMain {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        DentalCareSystem sistema = new DentalCareSystem();
        System.out.println("====================================================");
        System.out.println("        DENTALCARE - GESTAO DE MARCACOES");
        System.out.println("====================================================");

        while (true) {
            System.out.println("\n1. Registar cliente");
            System.out.println("2. Registar dono de empresa");
            System.out.println("3. Login");
            System.out.println("4. Simular utilizadores em simultaneo");
            System.out.println("5. Sair");
            System.out.print("Escolha: ");
            String opcao = SCANNER.nextLine();

            switch (opcao) {
                case "1":
                    registarCliente(sistema);
                    break;
                case "2":
                    registarDono(sistema);
                    break;
                case "3":
                    login(sistema);
                    break;
                case "4":
                    sistema.simularOperacoesConcorretes();
                    break;
                case "5":
                    sistema.salvar();
                    System.out.println("A sair...");
                    return;
                default:
                    System.out.println("Opcao invalida.");
            }
        }
    }

    private static void registarCliente(DentalCareSystem sistema) {
        System.out.print("Nome: ");
        String nome = SCANNER.nextLine();
        System.out.print("Email: ");
        String email = SCANNER.nextLine();
        System.out.print("Password: ");
        String password = SCANNER.nextLine();
        System.out.print("Cartao de cidadao: ");
        String cc = SCANNER.nextLine();
        System.out.print("Numero fiscal: ");
        String fiscal = SCANNER.nextLine();
        System.out.print("Telefone: ");
        String telefone = SCANNER.nextLine();
        System.out.print("Morada: ");
        String morada = SCANNER.nextLine();
        System.out.print("Localidade: ");
        String localidade = SCANNER.nextLine();

        Cliente cliente = sistema.registarCliente(nome, email, password, cc, fiscal, telefone, morada, localidade);
        if (cliente != null) {
            System.out.println("Cliente registado com sucesso.");
        }
    }

    private static void registarDono(DentalCareSystem sistema) {
        System.out.print("Nome: ");
        String nome = SCANNER.nextLine();
        System.out.print("Email: ");
        String email = SCANNER.nextLine();
        System.out.print("Password: ");
        String password = SCANNER.nextLine();
        System.out.print("Cartao de cidadao: ");
        String cc = SCANNER.nextLine();
        System.out.print("Numero fiscal: ");
        String fiscal = SCANNER.nextLine();
        System.out.print("Telefone: ");
        String telefone = SCANNER.nextLine();
        System.out.print("Morada: ");
        String morada = SCANNER.nextLine();
        System.out.print("Localidade: ");
        String localidade = SCANNER.nextLine();

        DonoEmpresa dono = sistema.registarDonoEmpresa(nome, email, password, cc, fiscal, telefone, morada, localidade);
        if (dono != null) {
            System.out.println("Dono de empresa registado com sucesso.");
        }
    }

    private static void login(DentalCareSystem sistema) {
        System.out.print("Email: ");
        String email = SCANNER.nextLine();
        System.out.print("Password: ");
        String password = SCANNER.nextLine();

        Utilizador utilizador = sistema.login(email, password);
        if (utilizador == null) {
            System.out.println("Credenciais invalidas.");
            return;
        }

        System.out.println("Bem-vindo, " + utilizador.getNome() + "!");
        if (utilizador instanceof Cliente) {
            menuCliente(sistema, (Cliente) utilizador);
        } else if (utilizador instanceof DonoEmpresa) {
            menuDono(sistema, (DonoEmpresa) utilizador);
        } else if (utilizador instanceof Funcionario) {
            menuFuncionario(sistema, (Funcionario) utilizador);
        } else if (utilizador instanceof Admin) {
            menuAdmin(sistema, (Admin) utilizador);
        }
    }

    private static void menuCliente(DentalCareSystem sistema, Cliente cliente) {
        while (true) {
            System.out.println("\nCliente");
            System.out.println("1. Ver empresas");
            System.out.println("2. Marcar consulta");
            System.out.println("3. Ver minhas marcacoes");
            System.out.println("4. Pagar consulta");
            System.out.println("5. Voltar");
            System.out.print("Escolha: ");
            String opcao = SCANNER.nextLine();

            switch (opcao) {
                case "1":
                    sistema.listarEmpresas().forEach(e -> System.out.println("- " + e.getNome() + " | " + e.getLocalidade()));
                    break;
                case "2":
                    marcarConsultaCliente(sistema, cliente);
                    break;
                case "3":
                    sistema.listarMarcacoesCliente(cliente).forEach(m -> System.out.println(m));
                    break;
                case "4":
                    pagarConsultaCliente(sistema, cliente);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Opcao invalida.");
            }
        }
    }

    private static void menuDono(DentalCareSystem sistema, DonoEmpresa dono) {
        while (true) {
            System.out.println("\nDono de empresa");
            System.out.println("1. Criar empresa");
            System.out.println("2. Criar consultorio");
            System.out.println("3. Criar funcionario");
            System.out.println("4. Criar tipo de consulta");
            System.out.println("5. Ver marcacoes da empresa");
            System.out.println("6. Voltar");
            System.out.print("Escolha: ");
            String opcao = SCANNER.nextLine();

            switch (opcao) {
                case "1":
                    criarEmpresa(sistema, dono);
                    break;
                case "2":
                    criarConsultorio(sistema, dono);
                    break;
                case "3":
                    criarFuncionario(sistema, dono);
                    break;
                case "4":
                    criarTipoConsulta(sistema, dono);
                    break;
                case "5":
                    verMarcacoesDono(sistema, dono);
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Opcao invalida.");
            }
        }
    }

    private static void menuFuncionario(DentalCareSystem sistema, Funcionario funcionario) {
        while (true) {
            System.out.println("\nFuncionario");
            System.out.println("1. Ver marcacoes para mim");
            System.out.println("2. Confirmar marcacao");
            System.out.println("3. Cancelar marcacao");
            System.out.println("4. Concluir marcacao");
            System.out.println("5. Voltar");
            System.out.print("Escolha: ");
            String opcao = SCANNER.nextLine();

            switch (opcao) {
                case "1":
                    sistema.listarMarcacoesFuncionario(funcionario).forEach(m -> System.out.println(m));
                    break;
                case "2":
                    confirmarMarcacaoFuncionario(sistema, funcionario);
                    break;
                case "3":
                    cancelarMarcacaoFuncionario(sistema, funcionario);
                    break;
                case "4":
                    concluirMarcacaoFuncionario(sistema, funcionario);
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Opcao invalida.");
            }
        }
    }

    private static void menuAdmin(DentalCareSystem sistema, Admin admin) {
        while (true) {
            System.out.println("\nAdministrador");
            System.out.println("1. Criar admin");
            System.out.println("2. Ver empresas");
            System.out.println("3. Desativar empresa");
            System.out.println("4. Ver estatisticas");
            System.out.println("5. Voltar");
            System.out.print("Escolha: ");
            String opcao = SCANNER.nextLine();

            switch (opcao) {
                case "1":
                    criarAdmin(sistema, admin);
                    break;
                case "2":
                    sistema.listarEmpresas().forEach(e -> System.out.println(e));
                    break;
                case "3":
                    desativarEmpresa(sistema);
                    break;
                case "4":
                    System.out.println(sistema.gerarRelatorioFinanceiro(LocalDate.now().minusMonths(1), LocalDate.now()));
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Opcao invalida.");
            }
        }
    }

    private static void criarEmpresa(DentalCareSystem sistema, DonoEmpresa dono) {
        System.out.print("Nome da empresa: ");
        String nome = SCANNER.nextLine();
        System.out.print("Morada: ");
        String morada = SCANNER.nextLine();
        System.out.print("Localidade: ");
        String localidade = SCANNER.nextLine();
        System.out.print("Telefone: ");
        String telefone = SCANNER.nextLine();
        Empresa empresa = sistema.criarEmpresa(dono, nome, morada, localidade, telefone);
        if (empresa != null) {
            System.out.println("Empresa criada: " + empresa.getNome());
        }
    }

    private static void criarConsultorio(DentalCareSystem sistema, DonoEmpresa dono) {
        System.out.print("Nome da empresa: ");
        String nomeEmpresa = SCANNER.nextLine();
        Empresa empresa = sistema.encontrarEmpresaPorNome(nomeEmpresa);
        if (empresa == null) {
            System.out.println("Empresa nao encontrada.");
            return;
        }
        System.out.print("Nome do consultorio: ");
        String nome = SCANNER.nextLine();
        System.out.print("Morada: ");
        String morada = SCANNER.nextLine();
        System.out.print("Localidade: ");
        String localidade = SCANNER.nextLine();
        System.out.print("Telefone: ");
        String telefone = SCANNER.nextLine();
        System.out.print("Especialidade: ");
        String especialidade = SCANNER.nextLine();
        Consultorio consultorio = sistema.adicionarConsultorio(empresa, nome, morada, localidade, telefone, especialidade);
        if (consultorio != null) {
            System.out.println("Consultorio criado: " + consultorio.getNome());
        }
    }

    private static void criarFuncionario(DentalCareSystem sistema, DonoEmpresa dono) {
        System.out.print("Nome da empresa: ");
        String nomeEmpresa = SCANNER.nextLine();
        Empresa empresa = sistema.encontrarEmpresaPorNome(nomeEmpresa);
        if (empresa == null) {
            System.out.println("Empresa nao encontrada.");
            return;
        }
        System.out.print("Nome do consultorio: ");
        String nomeConsultorio = SCANNER.nextLine();
        Consultorio consultorio = sistema.encontrarConsultorio(empresa, nomeConsultorio);
        if (consultorio == null) {
            System.out.println("Consultorio nao encontrado.");
            return;
        }
        System.out.print("Nome do funcionario: ");
        String nome = SCANNER.nextLine();
        System.out.print("Email: ");
        String email = SCANNER.nextLine();
        System.out.print("Password: ");
        String password = SCANNER.nextLine();
        System.out.print("Carteira profissional: ");
        int carteira = Integer.parseInt(SCANNER.nextLine());
        System.out.print("Especialidade: ");
        String especialidade = SCANNER.nextLine();
        Funcionario funcionario = sistema.criarFuncionario(dono, empresa, consultorio, nome, email, password, "", "", "", "", "", carteira, especialidade);
        if (funcionario != null) {
            System.out.println("Funcionario criado: " + funcionario.getNome());
        }
    }

    private static void criarTipoConsulta(DentalCareSystem sistema, DonoEmpresa dono) {
        System.out.print("Nome da empresa: ");
        String nomeEmpresa = SCANNER.nextLine();
        Empresa empresa = sistema.encontrarEmpresaPorNome(nomeEmpresa);
        if (empresa == null) {
            System.out.println("Empresa nao encontrada.");
            return;
        }
        System.out.print("Nome do consultorio: ");
        String nomeConsultorio = SCANNER.nextLine();
        Consultorio consultorio = sistema.encontrarConsultorio(empresa, nomeConsultorio);
        if (consultorio == null) {
            System.out.println("Consultorio nao encontrado.");
            return;
        }
        System.out.print("Nome da consulta: ");
        String nome = SCANNER.nextLine();
        System.out.print("Preco base: ");
        double preco = Double.parseDouble(SCANNER.nextLine());
        ConsultaTipo consulta = sistema.criarTipoConsulta(dono, empresa, consultorio, nome, preco);
        if (consulta != null) {
            System.out.println("Tipo de consulta criado: " + consulta.getNome());
        }
    }

    private static void verMarcacoesDono(DentalCareSystem sistema, DonoEmpresa dono) {
        for (Empresa empresa : dono.getEmpresas()) {
            System.out.println("Empresa: " + empresa.getNome());
            sistema.listarMarcacoesEmpresa(empresa).forEach(m -> System.out.println(" - " + m));
        }
    }

    private static void marcarConsultaCliente(DentalCareSystem sistema, Cliente cliente) {
        System.out.print("Nome da empresa: ");
        String nomeEmpresa = SCANNER.nextLine();
        Empresa empresa = sistema.encontrarEmpresaPorNome(nomeEmpresa);
        if (empresa == null) {
            System.out.println("Empresa nao encontrada.");
            return;
        }
        System.out.print("Nome do consultorio: ");
        String nomeConsultorio = SCANNER.nextLine();
        Consultorio consultorio = sistema.encontrarConsultorio(empresa, nomeConsultorio);
        if (consultorio == null) {
            System.out.println("Consultorio nao encontrado.");
            return;
        }
        System.out.print("Tipo de consulta: ");
        String tipo = SCANNER.nextLine();
        ConsultaTipo consulta = consultorio.buscarConsultaPorNome(tipo);
        if (consulta == null) {
            System.out.println("Tipo de consulta nao encontrado.");
            return;
        }
        System.out.print("Email do funcionario (opcional): ");
        String emailFuncionario = SCANNER.nextLine();
        Funcionario funcionario = null;
        if (!emailFuncionario.isBlank()) {
            funcionario = consultorio.buscarFuncionarioPorEmail(emailFuncionario);
        }
        System.out.print("Data e hora (yyyy-MM-dd HH:mm): ");
        String dataTexto = SCANNER.nextLine();
        LocalDateTime dataHora;
        try {
            dataHora = LocalDateTime.parse(dataTexto, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (DateTimeParseException e) {
            System.out.println("Data invalida.");
            return;
        }
        Marcacao marcacao = sistema.marcarConsulta(cliente, empresa, consultorio, consulta, funcionario, dataHora);
        if (marcacao != null) {
            System.out.println("Marcacao criada: " + marcacao);
        }
    }

    private static void pagarConsultaCliente(DentalCareSystem sistema, Cliente cliente) {
        System.out.print("ID da marcacao: ");
        String id = SCANNER.nextLine();
        Marcacao marcacao = sistema.encontrarMarcacaoPorId(id);
        if (marcacao == null || !marcacao.getCliente().equals(cliente)) {
            System.out.println("Marcacao nao encontrada.");
            return;
        }
        sistema.pagarConsulta(marcacao);
        System.out.println("Consulta paga.");
    }

    private static void confirmarMarcacaoFuncionario(DentalCareSystem sistema, Funcionario funcionario) {
        System.out.print("ID da marcacao: ");
        String id = SCANNER.nextLine();
        Marcacao marcacao = sistema.encontrarMarcacaoPorId(id);
        if (marcacao == null || !marcacao.getFuncionario().equals(funcionario)) {
            System.out.println("Marcacao nao encontrada para este funcionario.");
            return;
        }
        sistema.confirmarMarcacao(marcacao, new ArrayList<>(), true);
        System.out.println("Marcacao confirmada.");
    }

    private static void cancelarMarcacaoFuncionario(DentalCareSystem sistema, Funcionario funcionario) {
        System.out.print("ID da marcacao: ");
        String id = SCANNER.nextLine();
        Marcacao marcacao = sistema.encontrarMarcacaoPorId(id);
        if (marcacao == null || !marcacao.getFuncionario().equals(funcionario)) {
            System.out.println("Marcacao nao encontrada para este funcionario.");
            return;
        }
        System.out.print("Motivo: ");
        String motivo = SCANNER.nextLine();
        sistema.cancelarMarcacao(marcacao, motivo);
        System.out.println("Marcacao cancelada.");
    }

    private static void concluirMarcacaoFuncionario(DentalCareSystem sistema, Funcionario funcionario) {
        System.out.print("ID da marcacao: ");
        String id = SCANNER.nextLine();
        Marcacao marcacao = sistema.encontrarMarcacaoPorId(id);
        if (marcacao == null || !marcacao.getFuncionario().equals(funcionario)) {
            System.out.println("Marcacao nao encontrada para este funcionario.");
            return;
        }
        System.out.print("Problema/observacao: ");
        String problema = SCANNER.nextLine();
        sistema.concluirMarcacao(marcacao, problema);
        System.out.println("Marcacao concluida.");
    }

    private static void criarAdmin(DentalCareSystem sistema, Admin admin) {
        System.out.print("Nome: ");
        String nome = SCANNER.nextLine();
        System.out.print("Email: ");
        String email = SCANNER.nextLine();
        System.out.print("Password: ");
        String password = SCANNER.nextLine();
        sistema.criarAdmin(admin, nome, email, password, "", "", "", "", "");
        System.out.println("Admin criado.");
    }

    private static void desativarEmpresa(DentalCareSystem sistema) {
        System.out.print("Nome da empresa: ");
        String nome = SCANNER.nextLine();
        Empresa empresa = sistema.encontrarEmpresaPorNome(nome);
        if (empresa != null) {
            empresa.setAtiva(false);
            sistema.salvar();
            System.out.println("Empresa desativada.");
        } else {
            System.out.println("Empresa nao encontrada.");
        }
    }

    public static class DentalCareSystem implements Serializable {
        private static final long serialVersionUID = 1L;
        private static final String DATA_FILE = "data/dentalcare.ser";

        private final List<Utilizador> utilizadores = new CopyOnWriteArrayList<>();
        private final List<Empresa> empresas = new CopyOnWriteArrayList<>();
        private final List<Marcacao> marcacoes = new CopyOnWriteArrayList<>();
        private final Map<String, Utilizador> utilizadoresPorEmail = new ConcurrentHashMap<>();
        private final Map<String, List<Empresa>> empresasPorLocalidade = new ConcurrentHashMap<>();
        private final Map<String, List<Empresa>> empresasPorDono = new ConcurrentHashMap<>();
        private final Map<String, List<Consultorio>> consultoriosPorEmpresa = new ConcurrentHashMap<>();

        public DentalCareSystem() {
            carregar();
            if (utilizadores.isEmpty() && empresas.isEmpty()) {
                popularDadosIniciais();
            }
        }

        private void popularDadosIniciais() {
            Admin admin = new Admin("Admin DentalCare", "admin@dentalcare.pt", "admin123", "", "", "", "", "");
            adicionarUtilizador(admin);

            DonoEmpresa dono = registarDonoEmpresa("Ana Sousa", "ana@dentalcare.pt", "ana123", "12345678", "123456789", "912345678", "Rua da Estrela", "Lisboa");
            Empresa empresa = criarEmpresa(dono, "Clinica Smile", "Rua da Luz", "Lisboa", "213456789");
            Consultorio consultorio = adicionarConsultorio(empresa, "Consultorio Central", "Rua Nova", "Lisboa", "213456780", "Consulta Geral");
            criarFuncionario(dono, empresa, consultorio, "Dr. Miguel Costa", "miguel@dentalcare.pt", "miguel123", "", "", "", "", "", 12345, "Dentista");
            criarTipoConsulta(dono, empresa, consultorio, "Consulta Geral", 45.00);
            criarTipoConsulta(dono, empresa, consultorio, "Limpeza", 60.00);
            salvar();
        }

        public void salvar() {
            File file = new File(DATA_FILE);
            file.getParentFile().mkdirs();
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                out.writeObject(this);
            } catch (IOException e) {
                System.out.println("Erro ao guardar dados: " + e.getMessage());
            }
        }

        @SuppressWarnings("unchecked")
        private void carregar() {
            File file = new File(DATA_FILE);
            if (!file.exists()) {
                return;
            }
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                DentalCareSystem carregado = (DentalCareSystem) in.readObject();
                this.utilizadores.clear();
                this.utilizadores.addAll(carregado.utilizadores);
                this.empresas.clear();
                this.empresas.addAll(carregado.empresas);
                this.marcacoes.clear();
                this.marcacoes.addAll(carregado.marcacoes);
                this.utilizadoresPorEmail.clear();
                this.utilizadoresPorEmail.putAll(carregado.utilizadoresPorEmail);
                this.empresasPorLocalidade.clear();
                this.empresasPorLocalidade.putAll(carregado.empresasPorLocalidade);
                this.empresasPorDono.clear();
                this.empresasPorDono.putAll(carregado.empresasPorDono);
                this.consultoriosPorEmpresa.clear();
                this.consultoriosPorEmpresa.putAll(carregado.consultoriosPorEmpresa);
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Erro ao carregar dados: " + e.getMessage());
            }
        }

        private void adicionarUtilizador(Utilizador utilizador) {
            utilizadores.add(utilizador);
            utilizadoresPorEmail.put(utilizador.getEmail(), utilizador);
        }

        public Cliente registarCliente(String nome, String email, String password, String cartao, String fiscal, String telefone, String morada, String localidade) {
            if (utilizadoresPorEmail.containsKey(email)) {
                System.out.println("Email ja existe.");
                return null;
            }
            Cliente cliente = new Cliente(nome, email, password, cartao, fiscal, telefone, morada, localidade);
            adicionarUtilizador(cliente);
            salvar();
            return cliente;
        }

        public DonoEmpresa registarDonoEmpresa(String nome, String email, String password, String cartao, String fiscal, String telefone, String morada, String localidade) {
            if (utilizadoresPorEmail.containsKey(email)) {
                System.out.println("Email ja existe.");
                return null;
            }
            DonoEmpresa dono = new DonoEmpresa(nome, email, password, cartao, fiscal, telefone, morada, localidade);
            adicionarUtilizador(dono);
            salvar();
            return dono;
        }

        public Admin criarAdmin(Utilizador autor, String nome, String email, String password, String cartao, String fiscal, String telefone, String morada, String localidade) {
            if (!(autor instanceof Admin)) {
                return null;
            }
            if (utilizadoresPorEmail.containsKey(email)) {
                return null;
            }
            Admin admin = new Admin(nome, email, password, cartao, fiscal, telefone, morada, localidade);
            adicionarUtilizador(admin);
            salvar();
            return admin;
        }

        public Utilizador login(String email, String password) {
            Utilizador utilizador = utilizadoresPorEmail.get(email);
            if (utilizador != null && utilizador.getPassword().equals(password) && utilizador.isAtivo()) {
                return utilizador;
            }
            return null;
        }

        public Empresa criarEmpresa(DonoEmpresa dono, String nome, String morada, String localidade, String telefone) {
            if (dono == null) {
                return null;
            }
            Empresa empresa = new Empresa(nome, morada, localidade, telefone, dono);
            empresas.add(empresa);
            dono.adicionarEmpresa(empresa);
            empresasPorLocalidade.computeIfAbsent(localidade, k -> new ArrayList<>()).add(empresa);
            empresasPorDono.computeIfAbsent(dono.getEmail(), k -> new ArrayList<>()).add(empresa);
            consultoriosPorEmpresa.put(empresa.getNome(), new ArrayList<>());
            salvar();
            return empresa;
        }

        public Consultorio adicionarConsultorio(Empresa empresa, String nome, String morada, String localidade, String telefone, String especialidade) {
            if (empresa == null || !empresa.isAtiva()) {
                return null;
            }
            Consultorio consultorio = new Consultorio(nome, morada, localidade, telefone, especialidade);
            empresa.adicionarConsultorio(consultorio);
            consultoriosPorEmpresa.computeIfAbsent(empresa.getNome(), k -> new ArrayList<>()).add(consultorio);
            salvar();
            return consultorio;
        }

        public Funcionario criarFuncionario(DonoEmpresa dono, Empresa empresa, Consultorio consultorio, String nome, String email, String password, String cartao, String fiscal, String telefone, String morada, String localidade, int carteiraProfissional, String especialidade) {
            if (dono == null || empresa == null || consultorio == null) {
                return null;
            }
            if (utilizadoresPorEmail.containsKey(email)) {
                return null;
            }
            Funcionario funcionario = new Funcionario(nome, email, password, cartao, fiscal, telefone, morada, localidade, carteiraProfissional, especialidade);
            consultorio.adicionarFuncionario(funcionario);
            adicionarUtilizador(funcionario);
            salvar();
            return funcionario;
        }

        public ConsultaTipo criarTipoConsulta(DonoEmpresa dono, Empresa empresa, Consultorio consultorio, String nome, double precoBase) {
            if (dono == null || empresa == null || consultorio == null) {
                return null;
            }
            ConsultaTipo tipo = new ConsultaTipo(nome, precoBase);
            consultorio.adicionarTipoConsulta(tipo);
            salvar();
            return tipo;
        }

        public Marcacao marcarConsulta(Cliente cliente, Empresa empresa, Consultorio consultorio, ConsultaTipo tipoConsulta, Funcionario funcionario, LocalDateTime dataHora) {
            if (cliente == null || empresa == null || consultorio == null || tipoConsulta == null) {
                return null;
            }
            if (!empresa.isAtiva()) {
                System.out.println("Empresa inativa.");
                return null;
            }
            if (!consultorio.temSlotDisponivel(dataHora, funcionario)) {
                System.out.println("Slot indisponivel.");
                return null;
            }
            Marcacao marcacao = new Marcacao(cliente, empresa, consultorio, tipoConsulta, funcionario, dataHora);
            consultorio.adicionarMarcacao(marcacao);
            marcacoes.add(marcacao);
            consultorio.marcarSlot(dataHora, funcionario);
            salvar();
            return marcacao;
        }

        public void confirmarMarcacao(Marcacao marcacao, List<ServicoComplementar> servicos, boolean paga) {
            if (marcacao == null) {
                return;
            }
            marcacao.setEstado(EstadoMarcacao.CONFIRMADA);
            marcacao.setServicosComplementares(servicos);
            marcacao.setValorTotal(marcacao.getTipoConsulta().getPrecoBase() + marcacao.calcularServicos());
            if (paga) {
                marcacao.setPaga(true);
                marcacao.setEstado(EstadoMarcacao.PAGA);
            }
            salvar();
        }

        public void cancelarMarcacao(Marcacao marcacao, String motivo) {
            if (marcacao == null) {
                return;
            }
            marcacao.setEstado(EstadoMarcacao.CANCELADA);
            marcacao.setMotivoCancelamento(motivo);
            salvar();
        }

        public void concluirMarcacao(Marcacao marcacao, String problema) {
            if (marcacao == null) {
                return;
            }
            marcacao.setEstado(EstadoMarcacao.CONCLUIDA);
            marcacao.setObservacoes(problema);
            salvar();
        }

        public void pagarConsulta(Marcacao marcacao) {
            if (marcacao == null) {
                return;
            }
            marcacao.setPaga(true);
            marcacao.setEstado(EstadoMarcacao.PAGA);
            salvar();
        }

        public List<Empresa> listarEmpresas() {
            return empresas;
        }

        public List<Marcacao> listarMarcacoesCliente(Cliente cliente) {
            return marcacoes.stream().filter(m -> m.getCliente().equals(cliente)).sorted(Comparator.comparing(Marcacao::getDataHora)).toList();
        }

        public List<Marcacao> listarMarcacoesEmpresa(Empresa empresa) {
            return marcacoes.stream().filter(m -> m.getEmpresa().equals(empresa)).sorted(Comparator.comparing(Marcacao::getDataHora)).toList();
        }

        public List<Marcacao> listarMarcacoesFuncionario(Funcionario funcionario) {
            return marcacoes.stream().filter(m -> funcionario != null && funcionario.equals(m.getFuncionario())).sorted(Comparator.comparing(Marcacao::getDataHora)).toList();
        }

        public Empresa encontrarEmpresaPorNome(String nome) {
            return empresas.stream().filter(e -> e.getNome().equalsIgnoreCase(nome)).findFirst().orElse(null);
        }

        public Consultorio encontrarConsultorio(Empresa empresa, String nome) {
            if (empresa == null) {
                return null;
            }
            return empresa.getConsultorios().stream().filter(c -> c.getNome().equalsIgnoreCase(nome)).findFirst().orElse(null);
        }

        public Marcacao encontrarMarcacaoPorId(String id) {
            return marcacoes.stream().filter(m -> m.getId().equals(id)).findFirst().orElse(null);
        }

        public String gerarRelatorioFinanceiro(LocalDate inicio, LocalDate fim) {
            double total = 0;
            int count = 0;
            for (Marcacao marcacao : marcacoes) {
                if (marcacao.getEstado() == EstadoMarcacao.PAGA && !marcacao.getDataHora().toLocalDate().isBefore(inicio) && !marcacao.getDataHora().toLocalDate().isAfter(fim)) {
                    total += marcacao.getValorTotal();
                    count++;
                }
            }
            return String.format("Pagas: %d | Total: %.2f EUR | Comissao DentalCare: %.2f EUR", count, total, total * 0.05);
        }

        public void simularOperacoesConcorretes() {
            ExecutorService executor = Executors.newFixedThreadPool(3);
            executor.submit(() -> registarCliente("Cliente A", "clienteA@dentalcare.pt", "123", "", "", "", "", ""));
            executor.submit(() -> registarCliente("Cliente B", "clienteB@dentalcare.pt", "123", "", "", "", "", ""));
            executor.submit(() -> registarDonoEmpresa("Bruno Teixeira", "bruno@dentalcare.pt", "123", "", "", "", "", ""));
            executor.shutdown();
            try {
                executor.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            System.out.println("Simulacao concluida.");
        }
    }

    public static class Utilizador implements Serializable {
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

        public String getId() {
            return id;
        }

        public String getNome() {
            return nome;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public boolean isAtivo() {
            return ativo;
        }

        public PerfilUtilizador getPerfil() {
            return perfil;
        }

        public void setAtivo(boolean ativo) {
            this.ativo = ativo;
        }
    }

    public static class Cliente extends Utilizador {
        public Cliente(String nome, String email, String password, String numeroCartaoCidadao, String numeroFiscal, String telefone, String morada, String localidade) {
            super(nome, email, password, numeroCartaoCidadao, numeroFiscal, telefone, morada, localidade, PerfilUtilizador.CLIENTE);
        }
    }

    public static class DonoEmpresa extends Utilizador {
        private final List<Empresa> empresas = new ArrayList<>();

        public DonoEmpresa(String nome, String email, String password, String numeroCartaoCidadao, String numeroFiscal, String telefone, String morada, String localidade) {
            super(nome, email, password, numeroCartaoCidadao, numeroFiscal, telefone, morada, localidade, PerfilUtilizador.DONO_EMPRESA);
        }

        public void adicionarEmpresa(Empresa empresa) {
            empresas.add(empresa);
        }

        public List<Empresa> getEmpresas() {
            return empresas;
        }
    }

    public static class Funcionario extends Utilizador {
        private int numeroCarteiraProfissional;
        private String especialidade;

        public Funcionario(String nome, String email, String password, String numeroCartaoCidadao, String numeroFiscal, String telefone, String morada, String localidade, int numeroCarteiraProfissional, String especialidade) {
            super(nome, email, password, numeroCartaoCidadao, numeroFiscal, telefone, morada, localidade, PerfilUtilizador.FUNCIONARIO);
            this.numeroCarteiraProfissional = numeroCarteiraProfissional;
            this.especialidade = especialidade;
        }

        public int getNumeroCarteiraProfissional() {
            return numeroCarteiraProfissional;
        }

        public String getEspecialidade() {
            return especialidade;
        }
    }

    public static class Admin extends Utilizador {
        public Admin(String nome, String email, String password, String numeroCartaoCidadao, String numeroFiscal, String telefone, String morada, String localidade) {
            super(nome, email, password, numeroCartaoCidadao, numeroFiscal, telefone, morada, localidade, PerfilUtilizador.ADMIN);
        }
    }

    public static class Empresa implements Serializable {
        private static final long serialVersionUID = 1L;
        private String nome;
        private String morada;
        private String localidade;
        private String telefone;
        private DonoEmpresa dono;
        private boolean ativa = true;
        private final List<Consultorio> consultorios = new ArrayList<>();

        public Empresa(String nome, String morada, String localidade, String telefone, DonoEmpresa dono) {
            this.nome = nome;
            this.morada = morada;
            this.localidade = localidade;
            this.telefone = telefone;
            this.dono = dono;
        }

        public String getNome() {
            return nome;
        }

        public String getLocalidade() {
            return localidade;
        }

        public boolean isAtiva() {
            return ativa;
        }

        public void setAtiva(boolean ativa) {
            this.ativa = ativa;
        }

        public List<Consultorio> getConsultorios() {
            return consultorios;
        }

        public void adicionarConsultorio(Consultorio consultorio) {
            consultorios.add(consultorio);
        }

        @Override
        public String toString() {
            return nome + " - " + localidade + " - " + (ativa ? "Ativa" : "Inativa");
        }
    }

    public static class Consultorio implements Serializable {
        private static final long serialVersionUID = 1L;
        private String nome;
        private String morada;
        private String localidade;
        private String telefone;
        private String especialidade;
        private final List<Funcionario> funcionarios = new ArrayList<>();
        private final List<ConsultaTipo> tiposConsulta = new ArrayList<>();
        private final List<Marcacao> marcacoes = new ArrayList<>();
        private final List<AgendaSlot> agenda = new ArrayList<>();

        public Consultorio(String nome, String morada, String localidade, String telefone, String especialidade) {
            this.nome = nome;
            this.morada = morada;
            this.localidade = localidade;
            this.telefone = telefone;
            this.especialidade = especialidade;
        }

        public String getNome() {
            return nome;
        }

        public void adicionarFuncionario(Funcionario funcionario) {
            funcionarios.add(funcionario);
        }

        public void adicionarTipoConsulta(ConsultaTipo tipo) {
            tiposConsulta.add(tipo);
        }

        public void adicionarMarcacao(Marcacao marcacao) {
            marcacoes.add(marcacao);
        }

        public boolean temSlotDisponivel(LocalDateTime dataHora, Funcionario funcionario) {
            return agenda.stream().noneMatch(slot -> slot.getDataHora().equals(dataHora) && (funcionario == null || slot.getFuncionarioEmail().equals(funcionario.getEmail())));
        }

        public void marcarSlot(LocalDateTime dataHora, Funcionario funcionario) {
            agenda.add(new AgendaSlot(dataHora, funcionario == null ? null : funcionario.getEmail()));
        }

        public ConsultaTipo buscarConsultaPorNome(String nome) {
            return tiposConsulta.stream().filter(t -> t.getNome().equalsIgnoreCase(nome)).findFirst().orElse(null);
        }

        public Funcionario buscarFuncionarioPorEmail(String email) {
            return funcionarios.stream().filter(f -> f.getEmail().equalsIgnoreCase(email)).findFirst().orElse(null);
        }
    }

    public static class ConsultaTipo implements Serializable {
        private static final long serialVersionUID = 1L;
        private String nome;
        private double precoBase;

        public ConsultaTipo(String nome, double precoBase) {
            this.nome = nome;
            this.precoBase = precoBase;
        }

        public String getNome() {
            return nome;
        }

        public double getPrecoBase() {
            return precoBase;
        }
    }

    public static class ServicoComplementar implements Serializable {
        private static final long serialVersionUID = 1L;
        private String nome;
        private double preco;

        public ServicoComplementar(String nome, double preco) {
            this.nome = nome;
            this.preco = preco;
        }

        public double getPreco() {
            return preco;
        }
    }

    public static class Marcacao implements Serializable {
        private static final long serialVersionUID = 1L;
        private final String id = String.valueOf(System.nanoTime());
        private final Cliente cliente;
        private final Empresa empresa;
        private final Consultorio consultorio;
        private final ConsultaTipo tipoConsulta;
        private final Funcionario funcionario;
        private final LocalDateTime dataHora;
        private EstadoMarcacao estado = EstadoMarcacao.PENDENTE;
        private double valorTotal;
        private boolean paga;
        private String observacoes = "";
        private String motivoCancelamento = "";
        private List<ServicoComplementar> servicosComplementares = new ArrayList<>();

        public Marcacao(Cliente cliente, Empresa empresa, Consultorio consultorio, ConsultaTipo tipoConsulta, Funcionario funcionario, LocalDateTime dataHora) {
            this.cliente = cliente;
            this.empresa = empresa;
            this.consultorio = consultorio;
            this.tipoConsulta = tipoConsulta;
            this.funcionario = funcionario;
            this.dataHora = dataHora;
            this.valorTotal = tipoConsulta.getPrecoBase();
        }

        public String getId() {
            return id;
        }

        public Cliente getCliente() {
            return cliente;
        }

        public Empresa getEmpresa() {
            return empresa;
        }

        public Consultorio getConsultorio() {
            return consultorio;
        }

        public ConsultaTipo getTipoConsulta() {
            return tipoConsulta;
        }

        public Funcionario getFuncionario() {
            return funcionario;
        }

        public LocalDateTime getDataHora() {
            return dataHora;
        }

        public EstadoMarcacao getEstado() {
            return estado;
        }

        public void setEstado(EstadoMarcacao estado) {
            this.estado = estado;
        }

        public double getValorTotal() {
            return valorTotal;
        }

        public void setValorTotal(double valorTotal) {
            this.valorTotal = valorTotal;
        }

        public boolean isPaga() {
            return paga;
        }

        public void setPaga(boolean paga) {
            this.paga = paga;
        }

        public void setServicosComplementares(List<ServicoComplementar> servicosComplementares) {
            this.servicosComplementares = servicosComplementares;
        }

        public void setObservacoes(String observacoes) {
            this.observacoes = observacoes;
        }

        public void setMotivoCancelamento(String motivoCancelamento) {
            this.motivoCancelamento = motivoCancelamento;
        }

        public double calcularServicos() {
            return servicosComplementares.stream().mapToDouble(ServicoComplementar::getPreco).sum();
        }

        @Override
        public String toString() {
            return id + " | " + cliente.getNome() + " | " + empresa.getNome() + " | " + tipoConsulta.getNome() + " | " + estado + " | " + dataHora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
    }

    public static class AgendaSlot implements Serializable {
        private static final long serialVersionUID = 1L;
        private LocalDateTime dataHora;
        private String funcionarioEmail;

        public AgendaSlot(LocalDateTime dataHora, String funcionarioEmail) {
            this.dataHora = dataHora;
            this.funcionarioEmail = funcionarioEmail;
        }

        public LocalDateTime getDataHora() {
            return dataHora;
        }

        public String getFuncionarioEmail() {
            return funcionarioEmail;
        }
    }

    public enum PerfilUtilizador {
        CLIENTE,
        DONO_EMPRESA,
        FUNCIONARIO,
        ADMIN
    }

    public enum EstadoMarcacao {
        PENDENTE,
        CONFIRMADA,
        CANCELADA,
        CONCLUIDA,
        PAGA
    }
}
