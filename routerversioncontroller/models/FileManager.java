package routerversioncontroller.models;

import java.util.ArrayList;


public class FileManager(){

	ArrayList<String> nameFolders = new ArrayList<>();


	public FileManager(){
		nameFolders.add("Enrutador_1");
		nameFolders.add("Enrutador_2");
		nameFolders.add("Enrutador_3");
		nameFolders.add("sw_1");
		nameFolders.add("sw_2");
		nameFolders.add("sw_3");
		nameFolders.add("sw_4");
		nameFolders.add("sw_5");
		nameFolders.add("sw_6");
	}

	public void writeFile(String text) {

        try {
            PrintWriter writer = new PrintWriter(
                new FileWriter("routerversioncontroller/files/Enrutador_3"));
                writer.print(text);
                
            }

            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
        }
    }

    public void createFolders(){

    	for(int i=0; i< nameFolders.size(); i++){
    		File file = new File("routerversioncontroller/files/" + nameFolders.get(i) );

    		if(!file.exists()){
    			file.mkdirs();
    		}
    	}
    	
    }
}