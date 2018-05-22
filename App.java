
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.net.*;

public class App {

    public static void main(String args[]) throws UnknownHostException, IOException {
        String ipAddress = Inet4Address.getLocalHost().getHostAddress();
        InetAddress localHost = Inet4Address.getLocalHost();
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(localHost);
        int subnetMask = networkInterface.getInterfaceAddresses().get(0).getNetworkPrefixLength();
        int cont = 0;

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

        String networkAddr = "";
        String[] ipAddrParts = ipAddress.split("\\.");
        String[] maskParts = subnetMaskString.split("\\.");
        for (int i = 0; i < 4; i++) {
            int x = Integer.parseInt(ipAddrParts[i]);
            int y = Integer.parseInt(maskParts[i]);
            int z = x & y;
            if (i < 3) {
                networkAddr += z + ".";
            } else {
                networkAddr += z;
            }
        }

        Process p;
        String pingResult = "";
        String ipBase = networkAddr.substring(0, networkAddr.length() -1);
        System.out.println(ipBase);
        for (int i = 1; i < 256; i++) {
            System.out.println(i);
            p = Runtime.getRuntime().exec("ping " + ipBase + i);
        }
              //p = Runtime.getRuntime().exec("ping " + ipBase + "255");
        try {
            p = Runtime.getRuntime().exec("arp -a");
            BufferedReader s = new BufferedReader(new InputStreamReader(p.getInputStream()));
            ArrayList<String> listaIpConectadas = new ArrayList();
            s.readLine();
            s.readLine();
            s.readLine();
            String x1 = s.readLine();
            while (x1 != null) {
                //System.out.println(x1);
                listaIpConectadas.add(x1);
                x1 = s.readLine();
                
            }
            escribirArchivo(listaIpConectadas);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }	    

	private static byte[] getMacBytes(String macStr) throws IllegalArgumentException {
		byte[] bytes = new byte[6];
		String[] hex = macStr.split("(\\:|\\-)");
		if (hex.length != 6) {
			throw new IllegalArgumentException("Invalid MAC address.");
		}
		try {
			for (int i = 0; i < 6; i++) {
				bytes[i] = (byte) Integer.parseInt(hex[i], 16);
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid hex digit in MAC address.");
		}
		return bytes;
	}

    public static void escribirArchivo(ArrayList<String> listaAGuardar) {
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
}
