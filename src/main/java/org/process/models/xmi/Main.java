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
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.types.EolCollectionType;
import org.utils.Utils;

public class Main {

    private final static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        logger.info("Main@main -> Running ECORE processing");
        try {
            Config config = new Config();

            EclConfig eclConfig = new EclConfig();

            List<String> uriList = Utils.discoverModelFromPath(config.getRootPath(), config.getModelExtension());

            Variable thresholdVariable = new Variable("threshold", eclConfig.getEclParams().getThreshold(),
                    EolCollectionType.Collection);
            Variable componentDistanceWeigth = new Variable("componentDistanceWeigth",
                    eclConfig.getEclParams().getComponentDistanceWeigth(),
                    EolCollectionType.Collection);
            Variable connectorDistanceWeigth = new Variable("connectorDistanceWeigth",
                    eclConfig.getEclParams().getConnectorDistanceWeigth(),
                    EolCollectionType.Collection);

            Double[][] matrix = new Double[uriList.size()][uriList.size()];
            // Same as eol runner
            Path eclFileFolderPath = Paths.get(eclConfig.getEclScriptsFolderPath()).toAbsolutePath();
            String eclFilePath = eclFileFolderPath.resolve(eclConfig.getEclScriptName()).toString();
            String metaModelPath = Paths.get("ecore", "aadl2_inst.ecore").toAbsolutePath().toString();

            for (int i = 0; i < uriList.size(); i++) {
                logger.info("STEP: " + i + " OF " + uriList.size());
                // We need to create a model that satisfy addModel method, for this reason we
                // use te EmfModel that implements even IModel interface
                EmfModel firstModel = Utils.createEmfModel("FirstModel", uriList.get(i), metaModelPath, true, false);

                for (int j = 0; j < uriList.size(); j++) {

                    EclModule eclModule = new EclModule();
                    try {

                        eclModule.parse(new File(eclFilePath));

                        EmfModel secondModel = Utils.createEmfModel("SecondModel", uriList.get(j), metaModelPath, true,
                                false);

                        eclModule.parse(new File(eclFilePath));

                        // pass data that can be used in ecl script
                        // Maybe thresholds or others
                        eclModule.getContext().getFrameStack().putGlobal(thresholdVariable, componentDistanceWeigth,
                                connectorDistanceWeigth);

                        // Add models to ecl module
                        eclModule.getContext().getModelRepository().addModel(firstModel);
                        eclModule.getContext().getModelRepository().addModel(secondModel);
                        // execute ecl module
                        eclModule.execute();

                    } catch (Exception e) {
                        logger.error("Error performing ECL script: " + e.getMessage());
                    }
                    // Only 3 digists after 0
                    matrix[i][j] = Math.round(
                            (Double) eclModule.getContext().getFrameStack().get("editDistance").getValue() * 1000.0)
                            / 1000.0;
                }
            }

            Utils.print2dArray(matrix);

        } catch (Exception e) {
            logger.info("Main@main -> ERROR: " + e.getMessage());
        }
    }
}
