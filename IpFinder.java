import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.*;


public class IpFinder{

	private String ipAddress;
	private InetAddress localHost;
	private NetworkInterface networkInterface;
	private int subnetMask;

	public IpFinder(){
		try{
			setIpAddress(Inet4Address.getLocalHost().getHostAddress());
			setLocalHost(Inet4Address.getLocalHost());
			setNetworkInterface(NetworkInterface.getByInetAddress(localHost));
			setSubnetMask(networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength());
		}catch(UnknownHostException ue){
			ue.printStackTrace(System.out);
		}catch(SocketException se){
			se.printStackTrace(System.out);
		}
	}

	public String getSubnetMask(){
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

	public String getNetworkAddress(String ipAddress, String subnetMaskString){
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

	public void doPing(String networkAddress){
		Process p;
        String ipBase = networkAddress.substring(0, networkAddress.length() -1);
        for (int i = 1; i < 256; i++) {
            System.out.println(i);
            try{
            	p = Runtime.getRuntime().exec("ping " + ipBase + i);
            }catch(IOException e){
            	e.printStackTrace(System.out);
            }
        }
	}

	public ArrayList<String> doArp(){
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
                connectedIps.add(x1);
                x1 = s.readLine();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connectedIps;
	}

	 public void writeFile(ArrayList<String> listaAGuardar) {
        try {
            PrintWriter escritor = new PrintWriter(new FileWriter("IPConectadas.txt"));
            escritor.println("IP\t\t\tMAC Address");
            for (String ipleida : listaAGuardar) {
                if (ipleida != null) {
                    String[] datos = ipleida.split(" ");
                    for (String string : datos) {
						System.out.println(string);
					}
                    String z1="";
                    String z2="";
                    int cont=0;

                    for (int i = 0; i < datos.length; i++) {
                        if (datos[i].contains(".") && cont==0) {
                            z1 = datos[i];
                            cont++;
                        }
                        if (datos[i].contains("-") && cont==1) {
                            z2 = datos[i];
                        }
                    }
                    escritor.println(z1 + "\t\t" + z2);
                }

            }

            escritor.close();
        } catch (IOException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

	public void setIpAddress(String ipAddress){
		this.ipAddress = ipAddress;
	}

	public String getIpAddress(){
		return this.ipAddress;
	}

	public void setLocalHost(InetAddress localHost){
		this.localHost = localHost;
	}

	public void setNetworkInterface(NetworkInterface networkInterface){
		this.networkInterface = networkInterface;
	}

	public void setSubnetMask(int subnetMask){
		this.subnetMask = subnetMask;
	}



}