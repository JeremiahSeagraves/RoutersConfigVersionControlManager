package routerversioncontroller.models;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileManager {


    public FileManager() {
        
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

    public void createFolders(ArrayList<Device> devices) {

        for (int i = 0; i < devices.size(); i++) {
            File file = new File("configurationFiles/" +devices.get(i).getName());
            if (!file.exists()) {
                file.mkdirs();
                System.out.println("Creado la carpeta"+ file.getAbsolutePath());
            }else{
                System.out.println("La carpeta " + file.getAbsolutePath() + " ya existe");
            }
        }

    }
}
