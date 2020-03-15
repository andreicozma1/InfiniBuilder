package utils;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import environment.StructureBuilder;
import javafx.scene.Node;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ModelUtil {

    File res_folder = new File(getClass().getResource("../res/").getFile());
    public static Map<String,Node[]> models;

    public ModelUtil() {
        TdsModelImporter abc = new TdsModelImporter();
        models = new HashMap<String, Node[]>();

        File[] list_of_files = res_folder.listFiles();

        for(File file : list_of_files){
            System.out.println(file.getAbsolutePath());
            abc.read(file);
            models.put(file.getName(),abc.getImport());
        }
    }
}
