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
    public static String ROUTE1 = "172.16.76";
    public static String ROUTE2 = "10.10.14";
    public static String ROUTE1_ROUTER2 = "192.168.10.253";
    public static String ROUTE1_ROUTER3 = "192.168.12.2";
    public static String ROUTE2_ROUTER2 = "192.168.12.1";
    public static String ROUTE2_ROUTER1 = "192.168.10.254";

    public VersionController(ArrayList<Device> devices) {
        fileManager = new FileManager();
        telnetClient = new TelnetClient();
        this.devices = devices;
    }

    public void initializeFiles() {

        fileManager.createFolders(devices);

        for (int i = 0; i < devices.size(); i++) {

            if (devices.get(i).getName().contains("Router")) {
                initializeRouterFiles(devices.get(i));
            } else {

                initializeFile(devices.get(i).getIpAddress(), devices.get(i).getName());

            }

        }

    }

    private void initializeFile(String ip, String name) {
        File readFile = telnetClient.getConfigurationFile(ip, name);

        if (readFile == null) {
            System.out.println("No se pudo obtener el archivo de configuracion "
                    + "del dispositivo " + name + " - " + ip);
        } else {
            fileManager.createConfigurationFile(readFile, name);
        }

    }

    private void initializeRouterFiles(Device router) {
        IpFinder ipFinder = new IpFinder();
        String ip = ipFinder.getIpAddress();

        String ipRouter = router.getIpAddress();

        //Identificamos en que router estamos conectados para saber por medio de 
        //que ips accedemos
        if (ip.startsWith(ROUTE1)) {

            if (ipRouter.startsWith(ROUTE1) || ipRouter.equals(ROUTE1_ROUTER2)
                    || ipRouter.equals(ROUTE1_ROUTER3)) {
                initializeFile(ipRouter, router.getName());

            }

        } else if (ip.startsWith(ROUTE2)) {

            if (ipRouter.startsWith(ROUTE2) || ipRouter.equals(ROUTE2_ROUTER2)
                    || ipRouter.equals(ROUTE2_ROUTER1)) {
                initializeFile(ipRouter, router.getName());
            }
        }

    }

    public void updateConfigurationFiles() {

        for (int i = 0; i < devices.size(); i++) {

            if (devices.get(i).getName().contains("Router")) {
                updateRouterFiles(devices.get(i));
            } else {
                updateConfigurationFile(devices.get(i).getIpAddress(), devices.get(i).getName());
            }

        }
    }

    private void updateConfigurationFile(String ip, String name) {
        File readFile = telnetClient.getConfigurationFile(ip, name);

        if (readFile == null) {
            System.out.println("No se pudo obtener el archivo de configuracion "
                    + "del dispositivo " + name + " - " + ip);
        } else {
            File lastFile = fileManager.getlastConfigurationFile(name);

            if (fileManager.areEquals(readFile, lastFile)) {
                readFile.delete();
            } else {
                fileManager.createConfigurationFile(readFile, name);

            }
        }
    }

    private void updateRouterFiles(Device router) {
        IpFinder ipFinder = new IpFinder();
        String ip = ipFinder.getIpAddress();

        String ipRouter = router.getIpAddress();

        //Identificamos en que router estamos conectados para saber por medio de 
        //que ips accedemos
        if (ip.startsWith(ROUTE1)) {

            if (ipRouter.startsWith(ROUTE1) || ipRouter.equals(ROUTE1_ROUTER2)
                    || ipRouter.equals(ROUTE1_ROUTER3)) {
                updateConfigurationFile(ipRouter, router.getName());

            }

        } else if (ip.startsWith(ROUTE2)) {

            if (ipRouter.startsWith(ROUTE2) || ipRouter.equals(ROUTE2_ROUTER2)
                    || ipRouter.equals(ROUTE2_ROUTER1)) {
                updateConfigurationFile(ipRouter, router.getName());
            }
        }

    }

}
