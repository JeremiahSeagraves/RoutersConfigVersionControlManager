package routerversioncontroller.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.*;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ArrayList;
import java.net.*;


public class VersionController{

	public void connectTelnet(String ip){

		try{
			Socket soc = new Socket(ip,23);
			//create buffered writer
			BufferedReader bwin = new BufferedReader(new InputStreamReader(soc.getInputStream()));
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
			while (true) {
				String readFir = bwin.readLine();
				if (readFir == null) break;
				System.out.println(readFir);
				if(readFir.startsWith("Password")){

			        	//Escribimos la contrasenia
					bw.write("cisco");
					bw.newLine();
					bw.flush();

			            //Entramos modo privilegiado
					bw.write("enable");
					bw.newLine();
					bw.flush();

			            //Escribimos la contrasenia del modo privilegiado
					bw.write("class");
					bw.newLine();
					bw.flush();

			            //Mostramos el archivo de configuracion
					bw.write("show startup-config");
					bw.newLine();
					bw.write("/32");
					bw.flush();

					String configuration = bwin.readLine();

					System.out.println(configuration);

			            // instead might have to explicitly write "\r\n"
			            // depending platform you're connecting from.
					bw.flush();
				}

			}
		}catch(ConnectException e){
			System.out.println("Tiempo de conexion excedido");
		}catch(UnknownHostException e){
			System.out.println("Servidor no encontrado");
		}catch(IOException e){
			System.out.println("Error de lectura");
		}
		
		
	}
}
