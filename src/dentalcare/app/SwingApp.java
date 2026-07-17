package dentalcare.app;

import dentalcare.model.Cliente;
import dentalcare.model.Consultorio;
import dentalcare.model.DonoEmpresa;
import dentalcare.model.Empresa;
import dentalcare.model.Funcionario;
import dentalcare.model.Utilizador;
import dentalcare.service.DentalCareService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SwingApp extends JFrame {
    private final DentalCareService service = new DentalCareService();
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cards = new JPanel(cardLayout);
    private final JTextArea outputArea = new JTextArea();
    private Utilizador utilizadorAtual;

    public SwingApp() {
        super("DentalCare");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(760, 520);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(720, 480));
        getContentPane().setBackground(new Color(245, 247, 250));

        cards.add(criarPainelLogin(), "login");
        cards.add(criarPainelDashboard(), "dashboard");
        setContentPane(cards);
        mostrarLogin();
    }

    private JPanel criarPainelLogin() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(245, 247, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createEmptyBorder(20, 22, 20, 22));
        card.setPreferredSize(new Dimension(320, 300));

        JLabel title = new JLabel("DentalCare");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Gestao simples para clinicas dentarias");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(90, 95, 105));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Entrar");
        JButton clientButton = new JButton("Registar cliente");
        JButton ownerButton = new JButton("Registar dono");

        Dimension fieldSize = new Dimension(260, 32);
        emailField.setPreferredSize(fieldSize);
        passwordField.setPreferredSize(fieldSize);
        loginButton.setPreferredSize(fieldSize);
        clientButton.setPreferredSize(fieldSize);
        ownerButton.setPreferredSize(fieldSize);

        loginButton.setBackground(new Color(0, 120, 230));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        card.add(Box.createVerticalStrut(12));
        card.add(title);
        card.add(Box.createVerticalStrut(6));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(18));
        card.add(new JLabel("Email"));
        card.add(Box.createVerticalStrut(4));
        card.add(emailField);
        card.add(Box.createVerticalStrut(10));
        card.add(new JLabel("Password"));
        card.add(Box.createVerticalStrut(4));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(16));
        card.add(loginButton);
        card.add(Box.createVerticalStrut(8));
        card.add(clientButton);
        card.add(Box.createVerticalStrut(8));
        card.add(ownerButton);

        loginButton.addActionListener(e -> {
            Utilizador resultado = service.login(emailField.getText().trim(), new String(passwordField.getPassword()));
            if (resultado == null) {
                JOptionPane.showMessageDialog(this, "Credenciais invalidas.");
                return;
            }
            utilizadorAtual = resultado;
            mostrarDashboard();
        });

        clientButton.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(this, "Nome:");
            if (nome == null || nome.isBlank()) return;
            String email = JOptionPane.showInputDialog(this, "Email:");
            String password = JOptionPane.showInputDialog(this, "Password:");
            String cc = JOptionPane.showInputDialog(this, "Cartao de cidadao:");
            String fiscal = JOptionPane.showInputDialog(this, "Numero fiscal:");
            String telefone = JOptionPane.showInputDialog(this, "Telefone:");
            String morada = JOptionPane.showInputDialog(this, "Morada:");
            String localidade = JOptionPane.showInputDialog(this, "Localidade:");
            service.registarCliente(nome, email, password, cc, fiscal, telefone, morada, localidade);
            JOptionPane.showMessageDialog(this, "Cliente registado.");
        });

        ownerButton.addActionListener(e -> {
            String nome = JOptionPane.showInputDialog(this, "Nome:");
            if (nome == null || nome.isBlank()) return;
            String email = JOptionPane.showInputDialog(this, "Email:");
            String password = JOptionPane.showInputDialog(this, "Password:");
            String cc = JOptionPane.showInputDialog(this, "Cartao de cidadao:");
            String fiscal = JOptionPane.showInputDialog(this, "Numero fiscal:");
            String telefone = JOptionPane.showInputDialog(this, "Telefone:");
            String morada = JOptionPane.showInputDialog(this, "Morada:");
            String localidade = JOptionPane.showInputDialog(this, "Localidade:");
            service.registarDonoEmpresa(nome, email, password, cc, fiscal, telefone, morada, localidade);
            JOptionPane.showMessageDialog(this, "Dono registado.");
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(card, gbc);
        return panel;
    }

    private JPanel criarPainelDashboard() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 247, 250));

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(255, 255, 255));
        top.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        JLabel title = new JLabel("Painel DentalCare");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            utilizadorAtual = null;
            mostrarLogin();
        });

        top.add(title, BorderLayout.WEST);
        top.add(logoutButton, BorderLayout.EAST);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        actions.setBackground(new Color(245, 247, 250));
        actions.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));

        JButton empresasButton = new JButton("Listar empresas");
        JButton marcacoesButton = new JButton("Minhas marcasções");
        JButton empresaButton = new JButton("Criar empresa");
        JButton consultorioButton = new JButton("Criar consultório");
        JButton funcionarioButton = new JButton("Criar funcionario");
        JButton tipoButton = new JButton("Criar tipo consulta");

        empresasButton.addActionListener(e -> mostrarEmpresas());
        marcacoesButton.addActionListener(e -> mostrarMarcacoes());
        empresaButton.addActionListener(e -> criarEmpresa());
        consultorioButton.addActionListener(e -> criarConsultorio());
        funcionarioButton.addActionListener(e -> criarFuncionario());
        tipoButton.addActionListener(e -> criarTipoConsulta());

        actions.add(empresasButton);
        if (utilizadorAtual instanceof Cliente) {
            actions.add(marcacoesButton);
        } else {
            actions.add(empresaButton);
            actions.add(consultorioButton);
            actions.add(funcionarioButton);
            actions.add(tipoButton);
        }

        outputArea.setEditable(false);
        outputArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        outputArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        outputArea.setBackground(Color.WHITE);
        outputArea.setText("Selecione uma opcao para começar.");

        JScrollPane scroll = new JScrollPane(outputArea);
        scroll.setBorder(BorderFactory.createEmptyBorder());

        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(new Color(245, 247, 250));
        content.setBorder(BorderFactory.createEmptyBorder(8, 16, 16, 16));
        content.add(actions, BorderLayout.NORTH);
        content.add(scroll, BorderLayout.CENTER);

        panel.add(top, BorderLayout.NORTH);
        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private void mostrarLogin() {
        cardLayout.show(cards, "login");
        setTitle("DentalCare - Login");
    }

    private void mostrarDashboard() {
        cardLayout.show(cards, "dashboard");
        setTitle("DentalCare - Painel");
        outputArea.setText("Bem-vindo, " + utilizadorAtual.getNome() + "\n\nSelecione uma opcao.");
    }

    private void mostrarEmpresas() {
        List<Empresa> empresas = service.listarEmpresas();
        StringBuilder builder = new StringBuilder("Empresas registadas:\n");
        if (empresas.isEmpty()) {
            builder.append("Nenhuma empresa registada.");
        } else {
            for (Empresa empresa : empresas) {
                builder.append("- ").append(empresa.getNome()).append(" | ").append(empresa.getLocalidade()).append("\n");
            }
        }
        outputArea.setText(builder.toString());
    }

    private void mostrarMarcacoes() {
        if (!(utilizadorAtual instanceof Cliente cliente)) {
            outputArea.setText("Esta opcao e para clientes.");
            return;
        }
        List<dentalcare.model.Marcacao> marcacoes = service.listarMarcacoesCliente(cliente);
        StringBuilder builder = new StringBuilder("As suas marcacoes:\n");
        if (marcacoes.isEmpty()) {
            builder.append("Nenhuma marcacao registada.");
        } else {
            marcacoes.forEach(marcacao -> builder.append("- ").append(marcacao).append("\n"));
        }
        outputArea.setText(builder.toString());
    }

    private void criarEmpresa() {
        if (!(utilizadorAtual instanceof DonoEmpresa dono)) {
            outputArea.setText("Apenas donos de empresa podem criar empresas.");
            return;
        }
        String nome = JOptionPane.showInputDialog(this, "Nome da empresa:");
        String morada = JOptionPane.showInputDialog(this, "Morada:");
        String localidade = JOptionPane.showInputDialog(this, "Localidade:");
        String telefone = JOptionPane.showInputDialog(this, "Telefone:");
        if (nome == null || nome.isBlank()) return;
        Empresa empresa = service.criarEmpresa(dono, nome, morada, localidade, telefone);
        outputArea.setText("Empresa criada: " + (empresa != null ? empresa.getNome() : "erro"));
    }

    private void criarConsultorio() {
        if (!(utilizadorAtual instanceof DonoEmpresa dono)) {
            outputArea.setText("Apenas donos de empresa podem criar consultorios.");
            return;
        }
        String nomeEmpresa = JOptionPane.showInputDialog(this, "Nome da empresa:");
        if (nomeEmpresa == null || nomeEmpresa.isBlank()) return;
        Empresa empresa = service.listarEmpresas().stream()
                .filter(item -> item.getNome().equalsIgnoreCase(nomeEmpresa))
                .findFirst()
                .orElse(null);
        if (empresa == null) {
            outputArea.setText("Empresa nao encontrada.");
            return;
        }
        String nome = JOptionPane.showInputDialog(this, "Nome do consultorio:");
        String morada = JOptionPane.showInputDialog(this, "Morada:");
        String localidade = JOptionPane.showInputDialog(this, "Localidade:");
        String telefone = JOptionPane.showInputDialog(this, "Telefone:");
        String especialidade = JOptionPane.showInputDialog(this, "Especialidade:");
        if (nome == null || nome.isBlank()) return;
        Consultorio consultorio = service.adicionarConsultorio(empresa, nome, morada, localidade, telefone, especialidade);
        outputArea.setText("Consultorio criado: " + (consultorio != null ? consultorio.getNome() : "erro"));
    }

    private void criarFuncionario() {
        if (!(utilizadorAtual instanceof DonoEmpresa dono)) {
            outputArea.setText("Apenas donos de empresa podem criar funcionarios.");
            return;
        }
        String nomeEmpresa = JOptionPane.showInputDialog(this, "Nome da empresa:");
        if (nomeEmpresa == null || nomeEmpresa.isBlank()) return;
        Empresa empresa = service.listarEmpresas().stream()
                .filter(item -> item.getNome().equalsIgnoreCase(nomeEmpresa))
                .findFirst()
                .orElse(null);
        if (empresa == null) {
            outputArea.setText("Empresa nao encontrada.");
            return;
        }
        String nome = JOptionPane.showInputDialog(this, "Nome do funcionario:");
        String email = JOptionPane.showInputDialog(this, "Email:");
        String password = JOptionPane.showInputDialog(this, "Password:");
        String carteira = JOptionPane.showInputDialog(this, "Carteira profissional:");
        String especialidade = JOptionPane.showInputDialog(this, "Especialidade:");
        if (nome == null || nome.isBlank()) return;
        Funcionario funcionario = service.criarFuncionario(dono, empresa, empresa.getConsultorios().isEmpty() ? null : empresa.getConsultorios().get(0), nome, email, password, "", "", "", "", "", Integer.parseInt(carteira), especialidade);
        outputArea.setText("Funcionario criado: " + (funcionario != null ? funcionario.getNome() : "erro"));
    }

    private void criarTipoConsulta() {
        if (!(utilizadorAtual instanceof DonoEmpresa dono)) {
            outputArea.setText("Apenas donos de empresa podem criar tipos de consulta.");
            return;
        }
        String nomeEmpresa = JOptionPane.showInputDialog(this, "Nome da empresa:");
        if (nomeEmpresa == null || nomeEmpresa.isBlank()) return;
        Empresa empresa = service.listarEmpresas().stream()
                .filter(item -> item.getNome().equalsIgnoreCase(nomeEmpresa))
                .findFirst()
                .orElse(null);
        if (empresa == null) {
            outputArea.setText("Empresa nao encontrada.");
            return;
        }
        String nome = JOptionPane.showInputDialog(this, "Nome do tipo de consulta:");
        String preco = JOptionPane.showInputDialog(this, "Preco base:");
        if (nome == null || nome.isBlank()) return;
        var consulta = service.criarTipoConsulta(dono, empresa, empresa.getConsultorios().isEmpty() ? null : empresa.getConsultorios().get(0), nome, Double.parseDouble(preco));
        outputArea.setText("Tipo criado: " + (consulta != null ? consulta.getNome() : "erro"));
    }
}
