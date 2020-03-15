package utils;

import com.interactivemesh.jfx.importer.tds.TdsModelImporter;
import environment.StructureBuilder;
import javafx.scene.Node;

import java.net.URL;

public class ModelUtil {

    StructureBuilder str = new StructureBuilder(0, 0, 0);

    URL modelUrl = getClass().getResource("../res/Oak_Tree.3ds");

    ModelUtil() {
        TdsModelImporter abc = new TdsModelImporter();
        abc.read(modelUrl);
        Node[] nodes = abc.getImport();

        str.getChildren().addAll(nodes);
        str.setScaleX(10);
        str.setScaleY(10);
        str.setScaleZ(10);
    }

    StructureBuilder getModel(int model_id) {

        return str;
    }
}
