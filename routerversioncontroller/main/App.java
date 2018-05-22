package routerversioncontroller.main;

import java.util.ArrayList;
import routerversioncontroller.models.IpFinder;

public class App {

    public static void main(String args[]){
        IpFinder ipFinder = new IpFinder();

        String ipAddress = ipFinder.getIpAddress();
        String subnetMask = ipFinder.getSubnetMask();
        String networkAddress = ipFinder.getNetworkAddress(ipAddress,subnetMask);
        ipFinder.doPing(networkAddress);
        ArrayList<String> connectedIps = ipFinder.doArp();
        ipFinder.writeFile(connectedIps);
    }
}
