package org.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import org.config.Config;



public class SearchFileTraversal {
    String rootPath;
    String folderOutputName;
    List<String> extensions;
    List<String> searchPaths;
    Set<String> dataFilesFound = new HashSet<>();
    private Config configObj = null;

    SearchFileTraversal(String rootPath, String[] searchPaths, String[] exts, String folderOutputName) {
        this.rootPath = rootPath;
        this.searchPaths = Arrays.asList(searchPaths);
        this.extensions = Arrays.asList(exts);
        this.folderOutputName = folderOutputName;
    }

    public SearchFileTraversal(Config configObj) {
        this.configObj = configObj;
        this.rootPath = this.configObj.getRootPath();
        this.searchPaths = this.configObj.getArchivesForSearching();
        this.extensions = this.configObj.getExtensionsForSearching();
        this.folderOutputName = this.configObj.getOutputFolderName();
        try {
            Path previousFoundFiles = Paths.get(this.configObj.getRootPath(), ".files-found.txt").toAbsolutePath();
            this.dataFilesFound.addAll(Files.readAllLines(previousFoundFiles));
            this.dataFilesFound = this.dataFilesFound.stream().filter(x -> Paths.get(x).toFile().exists()).collect(Collectors.toSet());
            if (this.dataFilesFound.size() > 0)
                System.out.println("PATHS previously LOADED");
        } catch (Exception e) {
            System.out.println("NOT LOADED FILES DISCOVERED IN PREVIOUS SEARCHING");

        }
    }

    public SearchFileTraversal setFolderOutPutName(String outFolder) {
        this.folderOutputName = outFolder;
        return this;
    }

    @Override
    public String toString() {
        return "rootPath: " + this.rootPath + "; " + "searchPaths: " +
                this.searchPaths + "; " + "ext: " + this.extensions;

    }


    static public String getExtension(String path) {
        if (!path.contains(".")) return "txt";
        String[] chunksFileString = path.split("\\.");
        return chunksFileString[chunksFileString.length - 1];
    }

    public SearchFileTraversal setSearchPaths(List<String> searchPaths) {
        this.searchPaths = searchPaths;
        return this;
    }

}
