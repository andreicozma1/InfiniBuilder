package models;

import com.interactivemesh.jfx.importer.obj.ObjModelImporter;
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
    TdsModelImporter tds_importer;
    ObjModelImporter obj_importer;

    public ModelUtil() {
        tds_importer = new TdsModelImporter();
        obj_importer = new ObjModelImporter();

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

        tds_importer.read(resources.get(name));
        result.getChildren().addAll(tds_importer.getImport());
        tds_importer.clear();
        return result;
    }

    public StructureBuilder getRandomMatching(String[] pattern) {


        ArrayList<String> matching = new ArrayList<String>();

        for (String st : resources.keySet()) {
            for(String pattern_n : pattern){
                if (st.toLowerCase().contains(pattern_n.toLowerCase())) {
                    matching.add(st);
                }
            }
        }
        StructureBuilder result = new StructureBuilder();
        int random_matching_index = (int) Math.floor(Math.random() * (matching.size() - 1));

        String random_matching_filename = matching.get(random_matching_index);
        File random_file = resources.get(random_matching_filename);
        Node[] children = null;
        if(random_file.getName().contains("3ds")){
            tds_importer.read(random_file);
            children = tds_importer.getImport();
            tds_importer.clear();
        }
        if(children!=null){
            result.getChildren().addAll(children);
        }
        return result;
    }
}
