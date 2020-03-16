package models;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import environment.StructureBuilder;
import javafx.scene.Node;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ModelUtil {

    File res_folder = new File(getClass().getResource("res/").getFile());
    public static Map<String, File> resources;
    TdsModelImporter abc;

    public ModelUtil() {
        abc = new TdsModelImporter();

        resources = new HashMap<String, File>();

        File[] list_of_files = res_folder.listFiles();

        for (File file : list_of_files) {
            File[] list_of_files1 = file.listFiles();
            for (File file1 : list_of_files1) {
                System.out.println(file1.getAbsolutePath());
                resources.put(file1.getName(), file1);
            }
        }
    }

    public StructureBuilder getStructure(String name) {
        StructureBuilder result = new StructureBuilder();

        abc.read(resources.get(name));
        result.getChildren().addAll(abc.getImport());
        abc.clear();
        return result;
    }

    public StructureBuilder getRandomMatching(String pattern){
        pattern = pattern.toLowerCase();

        ArrayList<String> matching = new ArrayList<String>();

        for(String st: resources.keySet()){
            if(st.toLowerCase().contains(pattern)){
                matching.add(st);
            }
        }

        String random = matching.get((int)Math.floor(Math.random() * (matching.size()-1)));

        StructureBuilder result = new StructureBuilder();
        abc.read(resources.get(random));
        result.getChildren().addAll(abc.getImport());
        abc.clear();
        return result;
    }
}
