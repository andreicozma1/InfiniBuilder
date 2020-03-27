package app.utils;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;

import app.structures.StructureBuilder;
import javafx.scene.Node;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModelUtil {

    File res_folder;
    public static Map<String, File> resources;

    TdsModelImporter obj_importer;


    public ModelUtil() {
        res_folder = new File(getClass().getResource("/models").getFile());
        System.out.println(res_folder.getAbsolutePath());

        obj_importer = new TdsModelImporter();

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


    public StructureBuilder getRandomMatching(String[] pattern) {


        ArrayList<String> matching = new ArrayList<String>();

        for (String st : resources.keySet()) {
            for (String pattern_n : pattern) {
                if (st.toLowerCase().contains(pattern_n.toLowerCase())) {
                    matching.add(st);
                }
            }
        }

        int random_matching_index = (int) Math.floor(Math.random() * matching.size());
        Node[] children = null;
        if (random_matching_index >= 0 && matching.size() > 0) {
            String random_matching_filename = matching.get(random_matching_index);
            File random_file = resources.get(random_matching_filename);

            if (random_file.getName().toLowerCase().contains("3ds")) {
                obj_importer.read(random_file);
                children = obj_importer.getImport();
                obj_importer.clear();
            }
        }

        StructureBuilder result = new StructureBuilder();
        if (children != null) {
            result.getChildren().addAll(children);
        }
        return result;
    }
}
