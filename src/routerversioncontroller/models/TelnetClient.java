package routerversioncontroller.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ArrayList;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TelnetClient {

    private FileManager fileManager;
    
    public File getConfigurationFile(String ip, String nameDirectory) {
        boolean written = false;
        boolean isConfigurationFile = false;
        
        fileManager =  new FileManager();
        File readFile = null;
        
        
        try {
            Socket soc = new Socket(ip, 23);
            //create buffered writer
            BufferedReader bwin = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
            while (true) {
                String readFir = bwin.readLine();

                if (readFir == null) {
                    break;
                }
                
                if(readFir.contains("end")){
                    break;
                }

                System.out.println(readFir);
                
                if(isConfigurationFile){
                   fileManager.writeConfigurationFile(readFile, readFir);
                }
                
                if (!isConfigurationFile) {
                    if (readFir.startsWith("Password")) {
                        if (!written) {
                            //Escribimos la contrasenia
                            bw.write("cisco");
                            bw.newLine();
                            bw.flush();

                            bw.write("enable");
                            bw.newLine();
                            bw.flush();

                            bw.write("class");
                            bw.newLine();
                            bw.flush();

                            bw.write("show startup-config");
                            bw.newLine();
                            bw.flush();

                            written = true;
                        }

                    } else if (readFir.contains("show startup-config")) {
                        isConfigurationFile = true;
                        readFile =  fileManager.createAuxFile(nameDirectory);
                    }
                }else{
                    bw.write("\b");
                    bw.newLine();
                    bw.flush();
                }
                
                
            }
            bw.close();
        } catch (ConnectException e) {
            System.out.println("Tiempo de conexion excedido");
        } catch (UnknownHostException e) {
            System.out.println("Servidor no encontrado");
        } catch (IOException e) {
            System.out.println("Error de lectura");
        }
        
        return readFile;
    }

}
