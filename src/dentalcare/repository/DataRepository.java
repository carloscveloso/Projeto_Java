package dentalcare.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import dentalcare.config.AppConfig;
import dentalcare.service.DentalCareService;

public class DataRepository {
    public void salvar(DentalCareService service) {
        File file = new File(AppConfig.DATA_FILE);
        file.getParentFile().mkdirs();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
            out.writeObject(service);
        } catch (IOException e) {
            System.out.println("Erro ao guardar dados: " + e.getMessage());
        }
    }

    public DentalCareService carregar() {
        File file = new File(AppConfig.DATA_FILE);
        if (!file.exists()) {
            return null;
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object objeto = in.readObject();
            if (objeto instanceof DentalCareService) {
                return (DentalCareService) objeto;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ficheiro de dados antigo ou invalido. A criar um novo estado.");
            if (file.exists()) {
                file.delete();
            }
        }
        return null;
    }
}
