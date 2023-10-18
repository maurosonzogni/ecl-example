package org.process.models.xmi;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.config.Config;
import org.config.EclConfig;
import org.eclipse.epsilon.ecl.EclModule;
import org.eclipse.epsilon.ecl.trace.MatchTrace;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.utils.Utils;

public class Main {

    private final static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        logger.info("Main@main -> Running ECORE processing");
        try {
            Config config = new Config();

            EclConfig eclConfig = new EclConfig();

            List<String> uriList = Utils.discoverModelFromPath(config.getRootPath(), config.getModelExtension());

            Double[][] matrix = new Double[1][1];
            // Same as eol runner
            Path eclFileFolderPath = Paths.get(eclConfig.getEclScriptsFolderPath()).toAbsolutePath();

            String eclFilePath = eclFileFolderPath.resolve(eclConfig.getEclScriptName()).toString();

            String metaModelPath = Paths.get("ecore", "aadl2_inst.ecore").toAbsolutePath().toString();

            EmfModel firstModel = Utils.createEmfModel("FirstModel", uriList.get(0), metaModelPath, true, false);

            EclModule eclModule = new EclModule();

            EmfModel secondModel = Utils.createEmfModel("SecondModel", uriList.get(1), metaModelPath, true,
                    false);

            eclModule.parse(new File(eclFilePath));

            // Add models to ecl module
            eclModule.getContext().getModelRepository().addModel(firstModel);
            eclModule.getContext().getModelRepository().addModel(secondModel);
            // execute ecl module

            MatchTrace mt = eclModule.execute();
        


            //Utils.print2dArray(matrix);
            eclModule.getContext().dispose();

        } catch (Exception e) {
            logger.info("Main@main -> ERROR: " + e.getMessage());
        }
    }
}
