package routerversioncontroller.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import routerversioncontroller.models.Device;
import routerversioncontroller.models.IpFinder;
import routerversioncontroller.models.TelnetClient;

public class App {

    public static void main(String args[]) throws IOException {
        
        IpFinder ipFinder = new IpFinder();
        TelnetClient telnetClient = new TelnetClient();

        String ipAddress = ipFinder.getIpAddress();
        String subnetMask = ipFinder.getSubnetMask();
        String networkAddress = ipFinder.getNetworkAddress(ipAddress, subnetMask);
        //ipFinder.doPing(networkAddress);

        ArrayList<String> visitedNetworks = new ArrayList<>();
        //visitedNetworks.add(networkAddress + "\\" + subnetMask);
        ArrayList<Device> topologyDevices = ipFinder.getTopologyDevices();

        for (Device aTopologyDevice : topologyDevices) {
            String subnetMaskString = ipFinder.getSubnetMask(aTopologyDevice.getSubnetMask());
            String aNetworkAddress = ipFinder.getNetworkAddress(aTopologyDevice.getIpAddress(), subnetMaskString);
            if (!visitedNetworks.contains(aNetworkAddress + "\\" + subnetMaskString)) {
                visitedNetworks.add(aNetworkAddress+ "\\" +subnetMaskString);
                System.out.println("Red visitada: "+aNetworkAddress);
                ipFinder.doPing(aNetworkAddress);
                ArrayList<String> possibleNewReachableDevices = ipFinder.doArp();
            }
            System.out.println(aTopologyDevice.getName()+" "+ aTopologyDevice.getIpAddress() + "\\" + aTopologyDevice.getSubnetMask());
        }

        System.out.println("Redes visitadas:");
        for (String aVisitedNetwork : visitedNetworks) {
            System.out.println(aVisitedNetwork);
        }
        ArrayList<String> reachableDevices = ipFinder.doArp();
        ipFinder.writeFile(reachableDevices);
    }
}
