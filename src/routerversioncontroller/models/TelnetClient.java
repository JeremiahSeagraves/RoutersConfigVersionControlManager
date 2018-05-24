package routerversioncontroller.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.net.UnknownHostException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TelnetClient {

    public static String TELNET_PASSWORD = "cisco";
    public static String PRIVILEGED_MODE = "enable";
    public static String PRIVILEGED_MODE_PASSWORD = "class";
    public static String SHOW_STARTUP_CONFIG = "show startup-config";
    public static String TERMINAL_0 = "terminal length 0";
    public static String END_STARTUP_CONFIG = "end";
    public static String PASSWORD = "Password";
    public static String SPACE = "\b";
    public static String MORE = "More";
    private FileManager fileManager;

    public File getConfigurationFile(String ip, String nameDirectory) {
        boolean written = false;
        boolean isConfigurationFile = false;

        fileManager = new FileManager();
        File readFile = null;
        

        try {
            Socket soc = new Socket(ip, 23);
            //create buffered writer
            BufferedReader bwin = new BufferedReader(new InputStreamReader(soc.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
            boolean ready = false;
            Thread.sleep(1000);

            while (ready = bwin.ready()) {

                String readFir = bwin.readLine();

                if (readFir == null) {
                    break;
                }

                

                //System.out.println(readFir);
                if (!isConfigurationFile) {
                    if (readFir.startsWith(PASSWORD)) {
                        if (!written) {
                            //Escribimos la contrasenia
                            bw.write(TELNET_PASSWORD);
                            bw.newLine();
                            bw.flush();

                            bw.write(PRIVILEGED_MODE);
                            bw.newLine();
                            bw.flush();

                            bw.write(PRIVILEGED_MODE_PASSWORD);
                            bw.newLine();
                            bw.flush();

                            bw.write(TERMINAL_0);
                            bw.newLine();
                            bw.flush();

                            bw.write(SHOW_STARTUP_CONFIG);
                            bw.newLine();
                            bw.flush();

                            written = true;
                        }

                    } else if (readFir.contains(SHOW_STARTUP_CONFIG)) {
                        isConfigurationFile = true;
                        readFile = fileManager.createAuxFile(nameDirectory);
                        //bwin = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                        //Thread.sleep(1000);
                    }
                } else {
                    System.out.println(readFir);
                    fileManager.writeConfigurationFile(readFile, readFir);

                }
                Thread.sleep(1000);
                
                if (readFir.equals("end")) {
                    break;
                }
            }
            bw.close();
            bwin.close();
            soc.close();
            System.out.println("Fin de lectura");
        } catch (ConnectException e) {
            System.out.println("Tiempo de conexion excedido");
        } catch (UnknownHostException e) {
            System.out.println("Servidor no encontrado");
        } catch (IOException e) {
            System.out.println("Error de lectura");
        } catch (InterruptedException ex) {
            Logger.getLogger(TelnetClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        return readFile;
    }

}
