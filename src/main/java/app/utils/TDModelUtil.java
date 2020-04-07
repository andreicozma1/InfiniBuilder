package app.utils;

import app.structures.objects.Base_Model;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;

import javafx.scene.Node;

import java.io.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class TDModelUtil {

    private Map<String, File> resources;

    private TdsModelImporter obj_importer;

    private final String FOLDER_NAME = "models";
    private final String EXTENSION_3DS = "3ds";

    public TDModelUtil() {
        System.out.println("ModelUtil");

        obj_importer = new TdsModelImporter();
        resources = new HashMap<>();

        String s = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        System.out.println("Execution source: " + s);

        if (s.endsWith(".jar")) {
            // ONLY DO IF JAR
            JarFile jf = null;
            try {
                jf = new JarFile(s);
                Enumeration<JarEntry> entries = jf.entries();
                while (entries.hasMoreElements()) {
                    JarEntry je = entries.nextElement();

                    if (je.getName().startsWith(FOLDER_NAME) && je.getName().endsWith(EXTENSION_3DS)) {
                        System.out.println(je.getName());
                        Path path = Paths.get(je.getName());
                        InputStream is = this.getClass().getResourceAsStream("/" + je.getName());

                        File file = File.createTempFile(path.getFileName().toString(), EXTENSION_3DS);
                        OutputStream out = new FileOutputStream(file);
                        int read;
                        byte[] bytes = new byte[1024];

                        while ((read = is.read(bytes)) != -1) {
                            out.write(bytes, 0, read);
                        }
                        out.close();
                        file.deleteOnExit();

                        resources.put(String.valueOf(path.getFileName()), file);
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
        } else {
            // ONLY DO IF NOT JAR
            File res_folder = new File(this.getClass().getResource("/" + FOLDER_NAME).getFile());

            File[] list_of_files = res_folder.listFiles();
            for (File file : list_of_files) {
                File[] list_of_files1 = file.listFiles();
                for (File file1 : list_of_files1) {
                    System.out.println(file1.getAbsolutePath());
                    resources.put(file1.getName(), file1);
                }
            }
        }
    }


    public Base_Model getRandomMatching(String[] pattern) {

        ArrayList<String> matching = new ArrayList<String>();

        for (String st : resources.keySet()) {
            for (String pattern_n : pattern) {
                if (st.toLowerCase().contains(pattern_n.toLowerCase())) {
                    matching.add(st);
                }
            }
        }

        int random_matching_index = (int) Math.floor(Math.random() * matching.size());

        Base_Model result = new Base_Model();
        if (random_matching_index >= 0 && matching.size() > 0) {
            String random_matching_filename = matching.get(random_matching_index);
            File random_file = resources.get(random_matching_filename);

            if (random_file.getName().toLowerCase().contains(EXTENSION_3DS)) {
                obj_importer.read(random_file);
                Node[] children = obj_importer.getImport();
                obj_importer.clear();
                result.getProps().setPROPERTY_ITEM_TAG(random_matching_filename);
                if (children != null) {
                    result.getChildren().addAll(children);
                }
            }
        }

        return result;
    }
}
