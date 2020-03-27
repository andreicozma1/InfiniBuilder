package app.utils;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;

import app.structures.StructureBuilder;
import javafx.scene.Node;
import org.apache.commons.io.IOUtils;
import sun.nio.ch.IOUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ModelUtil {

    File res_folder;
    public static Map<String, File> resources;

    TdsModelImporter obj_importer;


    public ModelUtil() {
        obj_importer = new TdsModelImporter();
        resources = new HashMap<String, File>();

        // ONLY DO IF JAR
        JarFile jf = null;
        try {
            String s = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
            System.out.println(s);
            jf = new JarFile(s.toString());

            Enumeration<JarEntry> entries = jf.entries();
            while (entries.hasMoreElements()) {
                JarEntry je = entries.nextElement();

                if (je.getName().startsWith("models") && je.getName().endsWith(".3ds")) {
                    System.out.println(je.getName());


                    InputStream is = getClass().getResourceAsStream("/models/" + je.getName());
                    File f = File.createTempFile(je.getName(), ".3ds");
                    f.deleteOnExit();
                    FileOutputStream out = new FileOutputStream(f);
                    IOUtils.copy(is,out);


                    resources.put(je.getName(), f);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                jf.close();
            } catch (Exception e) {
            }
        }


        // ONLY DO IF NOT JAR
        res_folder = new File(this.getClass().getResource("/models").getFile());

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
