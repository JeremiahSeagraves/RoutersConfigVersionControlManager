package routerversioncontroller.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileManager {

    public static String ROOT_DIRECTORY = "configurationFiles/";

    public FileManager() {

    }

    public void createConfigurationFile(File readFile, String nameDirectory) {

        File directory = new File(ROOT_DIRECTORY + nameDirectory);
        File[] files = directory.listFiles();

        String nameFile = "";

        int numFiles = files.length;

        if (numFiles == 0) {
            createCurrentConfigurationFile(readFile, nameDirectory);
        } else if (numFiles > 0) {

            //Se busca el ultimo archivo de configuración guardado
            File lastFile = getlastConfigurationFile(files);
            createVersionedConfigurationFile(lastFile, nameDirectory, numFiles);

            //Al ultimo archivo se le cambia el nombre a uno con versionado
            //Se crea el nuevo archivo
            createCurrentConfigurationFile(readFile, nameDirectory);

        }
    }

    private void createCurrentConfigurationFile(File file, String nameDirectory) {
        String currentFileName = ROOT_DIRECTORY + nameDirectory + "/" + nameDirectory + "_Actual.txt";
        File newFile = new File(currentFileName);
        file.renameTo(newFile);
        System.out.println("Creado el archivo " + file.getAbsolutePath());
    }

    private void createVersionedConfigurationFile(File file, String nameDirectory, int versionNumber) {
        String newName = ROOT_DIRECTORY + nameDirectory + "/" + nameDirectory + "_v" + versionNumber + ".txt";
        File newFile = new File(newName);
        file.renameTo(newFile);
        System.out.println("Creado el archivo " + file.getAbsolutePath());
    }

    public void writeConfigurationFile(File file, String text) {

        try {

            if (!file.exists()) {
                file.createNewFile();
                System.out.println("Creado el archivo " + file.getAbsolutePath());
            }

            BufferedWriter bw;
            FileWriter writer = new FileWriter(
                    file, true);
            bw = new BufferedWriter(writer);
            bw.write(text);
            writer.close();
            bw.close();
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    public File createAuxFile(String nameDirectory) {
        String nameFile = ROOT_DIRECTORY + nameDirectory + "/" + nameDirectory + "_Aux.txt";
        return new File(nameFile);
    }

    public void createFolders(ArrayList<Device> devices) {

        for (int i = 0; i < devices.size(); i++) {
            File file = new File("configurationFiles/" + devices.get(i).getName());
            if (!file.exists()) {
                file.mkdirs();
                System.out.println("Creado la carpeta" + file.getAbsolutePath());
            } else {
                System.out.println("La carpeta " + file.getAbsolutePath() + " ya existe");
            }
        }

    }

    public File getlastConfigurationFile(File[] files) {

        File lastFile = null;

        for (int j = 0; j < files.length; j++) {
            if (files[j].getName().contains("Actual")) {
                lastFile = files[j];
                return lastFile;
            }
        }

        return lastFile;
    }

    public File getlastConfigurationFile(String nameDirectory) {

        File directory = new File(ROOT_DIRECTORY + nameDirectory);
        File[] files = directory.listFiles();

        File lastFile = null;

        for (int j = 0; j < files.length; j++) {
            if (files[j].getName().contains("Actual")) {
                lastFile = files[j];
                return lastFile;
            }
        }

        return lastFile;
    }

    public boolean areEquals(File fileUno, File fileDos) {
        FileReader fileReader1 = null;
        boolean equals = true;
        try {
            
            fileReader1 = new FileReader(fileUno);
            FileReader fileReader2 = new FileReader(fileDos);
            BufferedReader buffer1 = new BufferedReader(fileReader1);
            BufferedReader buffer2 = new BufferedReader(fileReader2);
            String line1 = buffer1.readLine();
            String line2 = buffer2.readLine();
            while ((line1 != null) && (line2 != null) && equals) {
                
                if (!line1.equals(line2)) {
                    equals = false;
                }
                
                line1 = buffer1.readLine();
                line2 = buffer2.readLine();
            }  
            
            fileReader1.close();
            
        } catch (FileNotFoundException ex) {
            System.out.println("No se encontro el archivo para comparar");
        } catch (IOException ex) {
            System.out.println("Error de lectura");
        } 
           
        return equals;
    }
}
