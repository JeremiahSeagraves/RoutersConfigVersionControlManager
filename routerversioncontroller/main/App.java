package routerversioncontroller.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.net.SocketException;
import routerversioncontroller.models.IpFinder;

public class App {

	public static void main(String args[]){
		IpFinder ipFinder = new IpFinder();

		String ipAddress = ipFinder.getIpAddress();
		String subnetMask = ipFinder.getSubnetMask();
		String networkAddress = ipFinder.getNetworkAddress(ipAddress,subnetMask);
		ipFinder.doPing(networkAddress);
		ArrayList<String> reachableDevices = ipFinder.doArp();
		ArrayList<String> visitedNetworks = new ArrayList<>();
		visitedNetworks.add(networkAddress+"-"+subnetMask);
		boolean newDeviceReached = true;

		while(newDeviceReached){
			newDeviceReached = false;
			Iterator<String> iterator = reachableDevices.iterator();
			while(iterator.hasNext()){
				String aReachableDevice = iterator.next();
				String[] data = aReachableDevice.split(" ");
                String deviceIp="";
                IpFinder tempIpFinder = new IpFinder();
                int cont=0;
                for (int i = 0; i < data.length; i++) {
                 	if (data[i].contains(".") && cont==0) {
                    	deviceIp = data[i];
                    	cont++;
                    }
               	}
               	String[] ipAddressParts= deviceIp.split("\\.");
                String ipBeginning = ipAddressParts[0];
				int subnetMaskInt = 0;
				switch(ipBeginning){
					case "10":
					subnetMaskInt = 26;
					break;
					case "172":
					subnetMaskInt = 28;
					break;
					default:
					subnetMaskInt = 30;
				}

				tempIpFinder.setSubnetMask(subnetMaskInt);

				String subnetMaskString = ipFinder.getSubnetMask();
				System.out.println(deviceIp+"---"+subnetMaskString);
				networkAddress = ipFinder.getNetworkAddress(deviceIp,subnetMaskString);

				if(!visitedNetworks.contains(networkAddress+"-"+subnetMaskString)){
					ipFinder.doPing(networkAddress);
					ArrayList<String> possibleNewReachableDevices = ipFinder.doArp();
					for(String possibleNewReachableDevice:possibleNewReachableDevices){
						if(!reachableDevices.contains(possibleNewReachableDevice)){
							reachableDevices.add(possibleNewReachableDevice);
							newDeviceReached = true;
						}
					}
					visitedNetworks.add(subnetMaskString);
				}
			}
			System.out.println("Redes visitadas:");
				for(String aVisitedNetwork:visitedNetworks){
					System.out.println(aVisitedNetwork);
				}
		}
		ipFinder.writeFile(reachableDevices);
	}
}
