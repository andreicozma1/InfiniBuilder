package app.structures.spawnables.utils;

import app.structures.objects.BaseModel;
import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import javafx.scene.Node;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class TDModelUtil {
    private static final String TAG = "ModelUtil";

    private final Map<String, File> resources;

    private final TdsModelImporter obj_importer;

    private final String FOLDER_NAME = "models";
    private final String EXTENSION_3DS = "3ds";

    /**
     * Class used to handle reading in 3D models from either the classpath or JAR package
     */
    public TDModelUtil() {
        Log.d(TAG, "CONSTRUCTOR");

        obj_importer = new TdsModelImporter();
        resources = new HashMap<>();

        String s = getClass().getProtectionDomain().getCodeSource().getLocation().getFile();
        Log.d(TAG, "Execution source: " + s);

        if (s.endsWith(".jar")) {
            // ONLY DO IF JAR
            // In this case we will need to read the 3D model data as a stream
            // Due to this, the streamed data will be temporarily saved as a file while the game is runnimg
            JarFile jf = null;
            try {
                jf = new JarFile(s);
                Enumeration<JarEntry> entries = jf.entries();
                // while there are more models to read
                while (entries.hasMoreElements()) {
                    JarEntry je = entries.nextElement();

                    if (je.getName().startsWith(FOLDER_NAME) && je.getName().endsWith(EXTENSION_3DS)) {
                        Log.d(TAG, "Found model at: " + je.getName());

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
                        // delete temporarily saved model files when exiting program
                        file.deleteOnExit();

                        resources.put(String.valueOf(path.getFileName()), file);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (jf != null) {
                        jf.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            // ONLY DO IF NOT JAR. In this case we are actually reading the 3D models from a folder
            File res_folder = new File(this.getClass().getResource("/" + FOLDER_NAME).getFile());

            File[] list_of_files = res_folder.listFiles();
            // iterate through all of our folders and find files models within them
            for (File file : Objects.requireNonNull(list_of_files)) {
                File[] list_of_files1 = file.listFiles();
                for (File file1 : Objects.requireNonNull(list_of_files1)) {
                    Log.d(TAG, "Found model at: " + file1.getAbsolutePath());
                    resources.put(file1.getName(), file1);
                }
            }
        }
    }


    /**
     * Function used to randomly return a 3D model based on an array of strings that is passed as argument
     * @param pattern
     * @return
     */
    public BaseModel getRandomMatching(String[] pattern) {

        ArrayList<String> matching = new ArrayList<String>();

        // find matching filenames in our model map
        for (String st : resources.keySet()) {
            for (String pattern_n : pattern) {
                if (st.toLowerCase().contains(pattern_n.toLowerCase())) {
                    matching.add(st);
                }
            }
        }

        int random_matching_index = (int) Math.floor(Math.random() * matching.size());

        // parse all the matching file names and return a BaseModel as our result
        BaseModel result = new BaseModel();
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
