package org.process.models.xmi;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.config.Config;
import org.utils.SearchFileTraversal;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class EcoreModelHandler {
    private List<String> uriModels;
    private String rootPathFolder;
    private final List<String> modelExtension;
    private Config configObj = null;

    private final static Logger logger = LogManager.getLogger(EcoreModelHandler.class);

    public EcoreModelHandler(Config configObj) {
        this.configObj = configObj;
        this.uriModels = new ArrayList<>();
        this.modelExtension = Arrays.asList("xml", "xmi", "ecore", "aaxl2");
        this.rootPathFolder = Paths.get(this.configObj.getRootPath(), this.configObj.getOutputFolderName(), "xmi")
                .toString();
    }

    public List<String> getUriModels() {
        return uriModels;
    }

    public void setRootPathFolder(String rootPathFolder) {
        this.rootPathFolder = rootPathFolder;
    }

    public void discoverModelFromPath() throws Exception {
        if (rootPathFolder == null)
            throw new Exception("There is not root path for reading the XMI models");
        File rootPathFolderFile = Paths.get(rootPathFolder).toFile();
        if (!rootPathFolderFile.exists())
            throw new Exception("The path to get the xmi converted files does not exist: " + rootPathFolderFile);
        if (!rootPathFolderFile.isDirectory())
            throw new Exception("The file to process the xmi converted models must be a directory");
        this.uriModels = new ArrayList<>();
        Files.walk(Path.of(rootPathFolder)).sorted().map(Path::toFile).forEach(
                (File file) -> {
                    if (file.isFile()) {
                        String uriModel = file.getPath();
                        String ext = SearchFileTraversal.getExtension(uriModel);
                        if (this.modelExtension.contains(ext))
                            this.uriModels.add(file.getPath());
                    }
                });
    }

    // TODO: valutare dove e come mettere questo, che non è altro che il metodo
    // precedente con un return
    public List<String> discoverModelPath() throws Exception {
        if (rootPathFolder == null)
            throw new Exception("There is not root path for reading the XMI models");
        File rootPathFolderFile = Paths.get(rootPathFolder).toFile();
        if (!rootPathFolderFile.exists())
            throw new Exception("The path to get the xmi converted files does not exist: " + rootPathFolderFile);
        if (!rootPathFolderFile.isDirectory())
            throw new Exception("The file to process the xmi converted models must be a directory");
        List<String> uris = new ArrayList<>();
        Files.walk(Path.of(rootPathFolder)).sorted().map(Path::toFile).forEach(
                (File file) -> {
                    if (file.isFile()) {
                        String uriModel = file.getPath();
                        String ext = SearchFileTraversal.getExtension(uriModel);
                        if (this.modelExtension.contains(ext))
                            uris.add(file.getPath());
                    }
                });
        return uris;
    }


}
