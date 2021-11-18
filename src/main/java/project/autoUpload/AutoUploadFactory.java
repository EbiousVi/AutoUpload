package project.autoUpload;

import project.types.SubTypes;
import project.filesWalker.*;

import java.util.HashMap;
import java.util.Map;

public class AutoUploadFactory {

    static Map<SubTypes, AutoUpload> factory = new HashMap<>();

    public static AutoUpload getAutoUploadInstance(SubTypes subType, Map<Parts, Boolean> parts) {
        initInstanceMap(parts);
        return factory.get(subType);
    }

    private static void initInstanceMap(Map<Parts, Boolean> parts) {
        if (!factory.isEmpty()) return;
        factory.put(SubTypes.G_BASE_BASE, new AutoUploadG_Base(new FilesWalkerG_Base(parts)));
        factory.put(SubTypes.G_ADV_BASE, new AutoUploadG_Adv(new FilesWalkerG_Base(parts)));
        factory.put(SubTypes.G_ADV_ADV, new AutoUploadG_Adv(new FilesWalkerG_Adv(parts)));

        factory.put(SubTypes.T_BASE_BASE, new AutoUploadT_Base(new FilesWalkerG_Base(parts)));
        factory.put(SubTypes.T_ADV_ADV, new AutoUploadT_Adv(new FilesWalkerT_Base(parts), new FilesWalkerT_Adv(parts)));
    }
}
