/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routerversioncontroller.models;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;

/**
 *
 * @author NoeCutz
 */
public class VersionController {

    private FileManager fileManager;
    private TelnetClient telnetClient;

    public VersionController() {
        fileManager = new FileManager();
        telnetClient = new TelnetClient();
    }

    public void createFiles(ArrayList<String> devices) {

        fileManager.createFolders();

        IpFinder ipFinder = new IpFinder();
        String ip = ipFinder.getIpAddress();

        for (int i = 0; i < devices.size(); i++) {

            telnetClient.connectTelnet(devices.get(i));

            String configurationFile = telnetClient.getConfigurationFile();
            saveConfigurationFile(devices.get(i), configurationFile);
        }

    }

    public void saveConfigurationFile(String folder, String info) {

        File directory = new File("configurationFiles/" + folder);
        File[] files = directory.listFiles();

        int numFiles = files.length;

        if (numFiles == 0) {
            createCurrentConfigurationFile(folder, info);
        } else if (numFiles > 0) {

            //Se busca el ultimo archivo de configuración guardado
            File lastFile = getlastConfigurationFile(files);
            

            //Al ultimo archivo se le cambia el nombre a uno con versionado
            String newName = folder + "/" + folder + "_v" + numFiles;
            File newFile = new File(newName);
            lastFile.renameTo(lastFile);

            //Se crea el nuevo archivo
            createCurrentConfigurationFile(folder, info);

        }

    }
    
    private void updateConfigurationFiles(ArrayList<String> devices){
         for (int i = 0; i < devices.size(); i++) {
            telnetClient.connectTelnet(devices.get(i));
            String configurationFile = telnetClient.getConfigurationFile();
            
             //getlastConfigurationFile(files)
            
            saveConfigurationFile(devices.get(i), configurationFile);
        }
    }

    private void createCurrentConfigurationFile(String folder, String text) {
        String nameFile = folder + "/" + folder + "_Actual";
        fileManager.writeFile(nameFile, text);
    }

    private File getlastConfigurationFile(File[] files) {

        File lastFile= null;
        
        for (int j = 0; j < files.length; j++) {
            if (files[j].getName().contains("Actual")) {
                 lastFile = files[j];
                 return lastFile;
            }
        }
        
        return lastFile;
    }

}
