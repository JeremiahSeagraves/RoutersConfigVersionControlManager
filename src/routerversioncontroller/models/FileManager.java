package routerversioncontroller.models;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.slf4j.Logger;
import org.slf4j.ILoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileManager {


    public FileManager() {
        
    }

    public void writeFile(String path, String text) {

        try {
            String fullPath = "configurationFiles/" + path;
            PrintWriter writer = new PrintWriter(
                    new FileWriter(fullPath));
            writer.print(text);
            writer.close();
            System.out.println("Creado el archivo " + fullPath );

        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    public void createFolders(ArrayList<Device> devices) {

        for (int i = 0; i < devices.size(); i++) {
            File file = new File("configurationFiles/" +devices.get(i).getName());
            if (!file.exists()) {
                file.mkdirs();
                System.out.println("Creado la carpeta"+ file.getAbsolutePath());
            }else{
                System.out.println("La carpeta " + file.getAbsolutePath() + " ya existe");
            }
        }

    }
    
    public ArrayList<String> getNameFolders(ArrayList<Device> devices) {
        ArrayList<String> folders = new ArrayList<>();
        for (int i = 0; i < devices.size(); i++) {
            System.out.println("GET NAME FOLDERS");
            System.out.println(devices.get(i).getName());
            if(!folders.contains(devices.get(i).getName())){
                folders.add(devices.get(i).getName());
            }
        }
        return folders;
    }
    
    public void createPDF(String nameFile) throws FileNotFoundException{
        PdfWriter writer = new PdfWriter(nameFile);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);
        document.add(new Paragraph("Hello World!"));
        document.close();
    }
}
