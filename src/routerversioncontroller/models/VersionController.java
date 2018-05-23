/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routerversioncontroller.models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author NoeCutz
 */
public class VersionController {

    private FileManager fileManager;
    private TelnetClient telnetClient;
    private ArrayList<Device> devices;

    public VersionController(ArrayList<Device> devices) {
        fileManager = new FileManager();
        telnetClient = new TelnetClient();
        this.devices = devices;
    }

    public void createFiles() {

        fileManager.createFolders(devices);

       
        for (int i = 0; i < devices.size(); i++) {
            
            if(devices.get(i).getName().contains("Router")){
                telnetRouters(devices.get(i));      
            }else{
                getConfigurationFileByTelnet(devices.get(i).getIpAddress(), devices.get(i).getName());
            }
           
                    
        }

    }
    
    private void getConfigurationFileByTelnet(String ip, String nameDevice){
        telnetClient.connectTelnet(ip);
                String configurationFile = telnetClient.getConfigurationFile();
                if(configurationFile !=null){
                    saveConfigurationFile(nameDevice, configurationFile);
                }else{
                    System.out.println("No se pudo obtener el archivo de configuración"
                            + " del dispositivo: " + nameDevice + 
                            " con ip: " + ip);
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
            String newName ="configurationFiles/" + folder + "/" + folder + "_v" + numFiles + ".txt";
            File newFile = new File(newName);
            lastFile.renameTo(newFile);

            //Se crea el nuevo archivo
            createCurrentConfigurationFile(folder, info);

        }

    }
    
    public void updateConfigurationFiles(){
        
        for (int i = 0; i < devices.size(); i++) {
            
            if(devices.get(i).getName().contains("Router")){
                telnetRouters(devices.get(i));      
            }else{
                telnetClient.connectTelnet(devices.get(i).getIpAddress());
                String configurationFile = telnetClient.getConfigurationFile();
                
                if(existChanges(configurationFile, devices.get(i))){
                    System.out.println("Archivo actualizado");
                   saveConfigurationFile(devices.get(i).getName(), configurationFile);
                }
              
            }
           
                    
        }
    }
    
    private boolean existChanges(String readConfiguration,Device device){
        
        File directory = new File("configurationFiles/" + device.getName());
        File[] files = directory.listFiles();
        
        File lastSavedFile = getlastConfigurationFile(files);
        
        String configurationFile = readAllBytes(lastSavedFile.getPath());
        
        if(!configurationFile.equals(readConfiguration)){
          return true; 
        }
      
        return false;
    }
    
    private String readAllBytes(String filePath){
        String content = "";
        
        try {
            content = new String (Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        return content;
    }

    private void createCurrentConfigurationFile(String folder, String text) {
        String nameFile = folder + "/" + folder + "_Actual.txt";
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
    
    private void telnetRouters(Device router){
        IpFinder ipFinder = new IpFinder();
        String ip = ipFinder.getIpAddress();
        
        String ipRouter = router.getIpAddress();
        
        
        //Identificamos en que router estamos conectados para saber por medio de 
        //que ips accedemos
        
        if(ip.startsWith("172.16.76")){
            
            if(ipRouter.startsWith("172.16.76") || ipRouter.startsWith("192.168.12") ){
                getConfigurationFileByTelnet(ipRouter, router.getName());
            }
           
        }else if(ip.startsWith("10.10.14")){
            
             if(ipRouter.startsWith("10.10.14") || ipRouter.startsWith("192.168.10") ){
                 getConfigurationFileByTelnet(ipRouter, router.getName());
            }
        }
        
    }

}
