package dentalcare.app;

import dentalcare.config.AppConfig;
import dentalcare.service.DentalCareService;
import java.util.Scanner;

public class DentalCareApp {
    private final DentalCareService service = new DentalCareService();
    private final Scanner scanner = new Scanner(System.in);
    private final ConsoleUI ui = new ConsoleUI();

    public void iniciar() {
        ui.showHeader(AppConfig.APP_TITLE);
        ui.showMessage("Sistema de gestao para clinicas dentarias.");

        while (true) {
            ui.showSection("Menu principal");
            ui.showMessage("1. Registar cliente");
            ui.showMessage("2. Registar dono de empresa");
            ui.showMessage("3. Login");
            ui.showMessage("4. Simular utilizadores em simultaneo");
            ui.showMessage("5. Sair");
            String opcao = ui.prompt("Escolha", scanner);

            switch (opcao) {
                case "1":
                    service.registarCliente(scanner);
                    break;
                case "2":
                    service.registarDono(scanner);
                    break;
                case "3":
                    service.login(scanner);
                    break;
                case "4":
                    service.simularOperacoesConcorretes();
                    break;
                case "5":
                    service.salvar();
                    ui.showMessage("A sair...");
                    return;
                default:
                    ui.showMessage("Opcao invalida.");
            }

            ui.showDivider();
        }
    }
}
