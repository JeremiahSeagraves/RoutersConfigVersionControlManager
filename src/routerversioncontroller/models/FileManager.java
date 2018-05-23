package routerversioncontroller.models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileManager {

    ArrayList<String> nameFolders = new ArrayList<>();

    public FileManager() {
        nameFolders.add("Enrutador_1");
        nameFolders.add("Enrutador_2");
        nameFolders.add("Enrutador_3");
        nameFolders.add("sw_1");
        nameFolders.add("sw_2");
        nameFolders.add("sw_3");
        nameFolders.add("sw_4");
        nameFolders.add("sw_5");
        nameFolders.add("sw_6");
    }

    public void writeFile(String path, String text) {

        try {
            String fullPath = "configurationFiles/" + path;
            PrintWriter writer = new PrintWriter(
                    new FileWriter(fullPath));
            writer.print(text);
            writer.close();
            System.out.println("Creado el archivo " + fullPath );

        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    public void createFolders() {

        for (int i = 0; i < nameFolders.size(); i++) {
            File file = new File("configurationFiles/" + nameFolders.get(i));
            if (!file.exists()) {
                file.mkdirs();
                System.out.println("Creado la carpeta"+ file.getAbsolutePath());
            }else{
                System.out.println("La carpeta " + file.getAbsolutePath() + " ya existe");
            }
        }

    }
}
