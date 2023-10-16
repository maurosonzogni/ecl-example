package org.process.models.xmi;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.config.Config;
import org.config.EclConfig;

public class Main {

    private final static Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        logger.info("Main@main -> Running ECORE processing");
        try {
            Config config = new Config();

            EclConfig eclConfig = new EclConfig();

            EcoreModelHandler ecoreModelHandler = new EcoreModelHandler(config);



            // ECL
            logger.info("INIZIO FASE ECL");

            EclRunner eclRunner = EclRunner.getInstance();
            long startTimeEclRunner = System.nanoTime();

            eclRunner.run(eclConfig, ecoreModelHandler.discoverModelPath());
            long endTimeEclRunner = System.nanoTime();

            logger.info("Ecl runner execution time in seconds: " + ((endTimeEclRunner - startTimeEclRunner)/ 1000000000)+ " s");


        } catch (Exception e) {
            logger.info("Main@main -> ERROR: " + e.getMessage());
        }
    }
}
