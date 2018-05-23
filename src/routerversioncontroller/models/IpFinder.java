package routerversioncontroller.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;

public class IpFinder {

    public static final String BROADCAST_IP = "255";
    public static final String MULTICAST_SUBNET_1 = "224";
    public static final String MULTICAST_SUBNET_2 = "239";
    public static final String LOCALHOST_SUBNET = "127";
    private String ipAddress;
    private InetAddress localHost;
    private NetworkInterface networkInterface;
    private int subnetMask;

    public IpFinder() {
        try {
            setIpAddress(Inet4Address.getLocalHost().getHostAddress());
            setLocalHost(Inet4Address.getLocalHost());
            setNetworkInterface(NetworkInterface.getByInetAddress(localHost));
            setSubnetMask(networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength());
        } catch (UnknownHostException ue) {
            ue.printStackTrace(System.out);
        } catch (SocketException se) {
            se.printStackTrace(System.out);
        }
    }

    public IpFinder(String ipAddress, InetAddress localHost, NetworkInterface networkInterface, int subnetMask) {
        this.ipAddress = ipAddress;
        this.localHost = localHost;
        this.networkInterface = networkInterface;
        this.subnetMask = subnetMask;
    }

    public String getSubnetMask() {
        String subnetMaskString = "";
        int subnetMask = this.subnetMask;
        for (int i = 0; i < 4; i++) {
            if (subnetMask >= 8) {
                subnetMask -= 8;
                subnetMaskString += "255.";
            } else {
                int sum = 0;
                int elevado = 7;
                for (int j = subnetMask; j > 0; j--) {
                    sum += Math.pow(2, elevado);
                    elevado--;
                }
                subnetMaskString += sum;
                for (i = i; i < 3; i++) {
                    subnetMaskString += ".0";
                }
                break;
            }

        }
        return subnetMaskString;
    }
    
        public String getSubnetMask(int subnetMask) {
        String subnetMaskString = "";
        for (int i = 0; i < 4; i++) {
            if (subnetMask >= 8) {
                subnetMask -= 8;
                subnetMaskString += "255.";
            } else {
                int sum = 0;
                int elevado = 7;
                for (int j = subnetMask; j > 0; j--) {
                    sum += Math.pow(2, elevado);
                    elevado--;
                }
                subnetMaskString += sum;
                for (i = i; i < 3; i++) {
                    subnetMaskString += ".0";
                }
                break;
            }

        }
        return subnetMaskString;
    }

    public String getNetworkAddress(String ipAddress, String subnetMaskString) {
        String networkAddress = "";
        String[] ipAddrParts = ipAddress.split("\\.");
        String[] maskParts = subnetMaskString.split("\\.");
        for (int i = 0; i < 4; i++) {
            int x = Integer.parseInt(ipAddrParts[i]);
            int y = Integer.parseInt(maskParts[i]);
            int z = x & y;
            if (i < 3) {
                networkAddress += z + ".";
            } else {
                networkAddress += z;
            }
        }
        return networkAddress;
    }

    public void doPing(String networkAddress) {
        Process p;
        String[] ipParts = networkAddress.split("\\.");
        String ipBase = ipParts[0]+"."+ipParts[1]+"."+ipParts[2]+".";
        String ipEnd = ipParts[3];
        int networkBeginning = Integer.parseInt(ipEnd);
        for (int i = networkBeginning; i < 256; i++) {
            System.out.println(i);
            try {
                System.out.println("Ping a: "+ipBase+i);
                p = Runtime.getRuntime().exec("ping " + ipBase + i);
                p.destroy();
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        }
        
    }

    public ArrayList<String> doArp() {
        ArrayList<String> connectedIps = new ArrayList<>();
        try {
            Process p;
            //p = Runtime.getRuntime().exec("netsh interface ip delete arpcache");
            p = Runtime.getRuntime().exec("arp -a");
            BufferedReader s = new BufferedReader(new InputStreamReader(p.getInputStream()));
            s.readLine();
            s.readLine();
            s.readLine();
            String x1 = s.readLine();
            while (x1 != null) {
                String[] data = x1.split(" ");
                String deviceIp = "";
                int cont = 0;
                for (int i = 0; i < data.length; i++) {
                    if (data[i].contains(".") && cont == 0) {
                        deviceIp = data[i];
                        cont++;
                    }
                }
                
                System.out.println(deviceIp);
                String[] ipAddressParts = deviceIp.split("\\.");
                String ipBeginning = ipAddressParts[0];
                String ipEnd = ipAddressParts[3];

                if (!ipBeginning.equals(MULTICAST_SUBNET_1)
                        && !ipBeginning.equals(MULTICAST_SUBNET_2)
                        && !ipBeginning.equals(LOCALHOST_SUBNET)
                        && !ipEnd.equals(BROADCAST_IP)) {
                    connectedIps.add(x1);
                }

                x1 = s.readLine();

            }
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        
        return connectedIps;
    }

    public void writeFile(ArrayList<String> listaAGuardar) {
        try {
            PrintWriter escritor = new PrintWriter(
                    new FileWriter("routerversioncontroller/files/IPConectadas.txt"));
            escritor.println("IP\t\t\tMAC Address");
            for (String ipleida : listaAGuardar) {
                if (ipleida != null) {
                    String[] datos = ipleida.split(" ");
                    String z1 = "";
                    String z2 = "";
                    int cont = 0;

                    for (int i = 0; i < datos.length; i++) {
                        if (datos[i].contains(".") && cont == 0) {
                            z1 = datos[i];
                            cont++;
                        }
                        if (datos[i].contains("-") && cont == 1) {
                            z2 = datos[i];
                        }
                    }
                    escritor.println(z1 + "\t\t" + z2);
                }

            }

            escritor.close();
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }

    }

    public ArrayList<Device> getTopologyDevices() {
        ArrayList<Device> devices =  new ArrayList<>();
        try {
            FileReader reader = new FileReader(new File("routerversioncontroller/files/topologia.txt"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println("Esta es la línea: "+line);
                String[] deviceParts = line.split(",");
                String deviceIP = deviceParts[0];
                int subnetMask = Integer.parseInt(deviceParts[1]);
                String deviceName = deviceParts[2];
                Device aDevice = new Device(deviceIP, subnetMask, deviceName);
                devices.add(aDevice);
                bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return devices;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public void setLocalHost(InetAddress localHost) {
        this.localHost = localHost;
    }

    public void setNetworkInterface(NetworkInterface networkInterface) {
        this.networkInterface = networkInterface;
    }

    public void setSubnetMask(int subnetMask) {
        this.subnetMask = subnetMask;
    }

}
