package routerversioncontroller.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import routerversioncontroller.models.Device;
import routerversioncontroller.models.FileManager;
import routerversioncontroller.models.IpFinder;
import routerversioncontroller.models.TelnetClient;
import routerversioncontroller.models.VersionController;

public class App {

    public static void main(String args[]) {
        IpFinder ipFinder = new IpFinder();
        TelnetClient telnetClient = new TelnetClient();

        String ipAddress = ipFinder.getIpAddress();
        String subnetMask = ipFinder.getSubnetMask();
        String networkAddress = ipFinder.getNetworkAddress(ipAddress, subnetMask);
        ipFinder.doPing(networkAddress);

        ArrayList<String> reachableDevices = ipFinder.doArp();
        ArrayList<String> visitedNetworks = new ArrayList<>();
        visitedNetworks.add(networkAddress + "\\" + subnetMask);
        ArrayList<Device> topologyDevices = ipFinder.getTopologyDevices();
        
        VersionController versionController = new VersionController(topologyDevices);
        FileManager fileManager = new FileManager();
/*  
        for (Device aTopologyDevice : topologyDevices) {
            String subnetMaskString = ipFinder.getSubnetMask(aTopologyDevice.getSubnetMask());
            String aNetworkAddress = ipFinder.getNetworkAddress(aTopologyDevice.getIpAddress(), subnetMaskString);
            if (!visitedNetworks.contains(aNetworkAddress + "\\" + subnetMaskString)) {
                visitedNetworks.add(aNetworkAddress+ "\\" +subnetMaskString);
                System.out.println("Red visitada: "+aNetworkAddress);
                ipFinder.doPing(aNetworkAddress);
                ArrayList<String> possibleNewReachableDevices = ipFinder.doArp();
                for (String possibleNewReachableDevice : possibleNewReachableDevices) {
                    if (!reachableDevices.contains(possibleNewReachableDevice)) {
                        reachableDevices.add(possibleNewReachableDevice);
                    }
                }
            }
            System.out.println(aTopologyDevice.getName()+" "+ aTopologyDevice.getIpAddress() + "\\" + subnetMaskString);
        }

        System.out.println("Redes visitadas:");
        for (String aVisitedNetwork : visitedNetworks) {
            System.out.println(aVisitedNetwork);
        }
        ipFinder.writeFile(reachableDevices);*/


        Scanner scanner = new Scanner(System.in);
        int option;
        ArrayList<String> folders = new ArrayList<>();

        System.out.println("Aplicación para el respaldo de las configuraciones.");
        System.out.println("##################################################");
        do {
            System.out.println("\n***********************MENU***********************");
            System.out.println("Introduzca el número de la opcion que desea realizar, para salir introduzca 0");
            System.out.println("1.- Escanear redes.");
            System.out.println("2.- Actualizar archivos de configuración.");
            System.out.println("3.- Exportar configuración a PDF");
            option = scanner.nextInt();
            switch (option) {
                case 0:
                    System.out.println("¡Nos vemos!");
                    break;
                case 1:
                    System.out.println("Selecciono la opción 1");
                    for (Device aTopologyDevice : topologyDevices) {
                        String subnetMaskString = ipFinder.getSubnetMask(aTopologyDevice.getSubnetMask());
                        String aNetworkAddress = ipFinder.getNetworkAddress(aTopologyDevice.getIpAddress(), subnetMaskString);
                        if (!visitedNetworks.contains(aNetworkAddress + "\\" + subnetMaskString)) {
                            visitedNetworks.add(aNetworkAddress + "\\" + subnetMaskString);
                            System.out.println("Red visitada: " + aNetworkAddress);
                            ipFinder.doPing(aNetworkAddress);
                            ArrayList<String> possibleNewReachableDevices = ipFinder.doArp();
                            for (String possibleNewReachableDevice : possibleNewReachableDevices) {
                                if (!reachableDevices.contains(possibleNewReachableDevice)) {
                                    reachableDevices.add(possibleNewReachableDevice);
                                }
                            }
                        }
                        System.out.println(aTopologyDevice.getName() + " " + aTopologyDevice.getIpAddress() + "\\" + subnetMaskString);
                    }

                    System.out.println("Redes visitadas:");
                    for (String aVisitedNetwork : visitedNetworks) {
                        System.out.println(aVisitedNetwork);
                    }
                    ipFinder.writeFile(reachableDevices);
                    break;
                case 2:
                    System.out.println("Selecciono la opción 2");
                    versionController.initializeFiles();
                    break;
                case 3:
                    folders = fileManager.getNameFolders(topologyDevices);
                    int indexFolder;
                    int indexFile;
                    String pathFile="configurationFiles/";
                    
                    System.out.println("Seleccione un dispositivo: ");
                    for (int i = 0; i < folders.size(); i++) {
                        System.out.println(i + ".- " + folders.get(i));
                    }
                    indexFolder = scanner.nextInt();
                    File folder = new File("configurationFiles/" + folders.get(indexFolder));
                    pathFile+=folders.get(indexFolder)+"/";
                    
                    File[] listOfFiles = folder.listFiles();
                    System.out.println("El dispositivo " + folders.get(indexFolder) + " cuenta con las siguientes versiones de configuración: ");
                    for (int i = 0; i < listOfFiles.length; i++) {
                        if (listOfFiles[i].isFile()) {
                            System.out.println(i + ".- " + listOfFiles[i].getName());
                        }
                    }
                    System.out.println("¿Cuál desea exportar a PDF?");
                    indexFile = scanner.nextInt();
                    pathFile+=listOfFiles[indexFile].getName();
                    
                    System.out.println("Seleccionaste el archivo " + listOfFiles[indexFile].getName());
                    
                    try {
                        fileManager.createPDF(pathFile);
                    } catch (FileNotFoundException ex) {
                        System.out.println("Error de PDF");
                    }
                    
                    break;
                default:
                    System.out.println("¡La opción seleccionada no existe!");
                    break;
            }
        } while (option != 0);
    }
}
