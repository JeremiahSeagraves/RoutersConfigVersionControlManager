package routerversioncontroller.main;

import java.util.ArrayList;
import routerversioncontroller.models.Device;
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
        //ipFinder.doPing(networkAddress);

       // ArrayList<String> reachableDevices = ipFinder.doArp();
        ArrayList<String> visitedNetworks = new ArrayList<>();
        visitedNetworks.add(networkAddress + "\\" + subnetMask);
        ArrayList<Device> topologyDevices = ipFinder.getTopologyDevices();
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
        
        VersionController versionController = new VersionController(topologyDevices);
        versionController.initializeFiles();
        versionController.updateConfigurationFiles();
    }
}
