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
    public static Map<String, File> resources;
    TdsModelImporter abc;

    public ModelUtil() {
        abc = new TdsModelImporter();

        resources = new HashMap<String, File>();

        File[] list_of_files = res_folder.listFiles();

        for (File file : list_of_files) {
            System.out.println(file.getAbsolutePath());
            resources.put(file.getName(), file);
        }
    }

    public StructureBuilder getStructure(String name) {
        StructureBuilder result = new StructureBuilder();

        abc.read(resources.get(name));
        result.getChildren().addAll(abc.getImport());
        abc.clear();
        return result;
    }
}
