/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routerversioncontroller.models;

import java.io.File;
import java.nio.file.Files;

/**
 *
 * @author NoeCutz
 */
public class VersionController {
    
    public void getConfigurationFiles(){
        
        
        
        TelnetClient telnetClient = new TelnetClient();
        
        
        IpFinder ipFinder =  new IpFinder();
        String ip = ipFinder.getIpAddress();
        
        
        telnetClient.connectTelnet("");  
        
        String configurationFile  = telnetClient.getConfigurationFile();
        
        saveConfigurationFile("", configurationFile);
        
        
    }
    
    
    public void saveConfigurationFile(String folder, String info){
        FileManager fileManager =  new FileManager();
        fileManager.createFolders();
        
        File directory = new File("configurationFiles/");
        File[] files = directory.listFiles();
        
        int numFiles = files.length;
        
        
        if(numFiles == 0){
           String nameFile = "Actual";
           fileManager.writeFile(nameFile, info);
        }else if(numFiles > 0){
            
            File lastFile = null ;
            for(int j=0; j< numFiles;j++){
                if(files[j].getName().contains("Actual")){
                    lastFile = files[j];
                }
            }
            
            String newName = "";
            
            File newFile = new File(newName);
            
            lastFile.renameTo(lastFile);
               
        }
        
    }
    
    
}
