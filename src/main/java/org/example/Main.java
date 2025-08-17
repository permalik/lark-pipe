package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        PromptRawConsumer consumer = new PromptRawConsumer("prompt.raw");
        PromptCleanProducer producer = new PromptCleanProducer("prompt.clean");

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    logger.info("Shutting down..");
                    consumer.close();
                    producer.close();
                })
            );

        logger.info("Starting Preprocess..");

        try {
            while (true) {
                String processedPrompt = consumer.consumeAndProcess();
                if (processedPrompt != null) {
                    producer.produce("new_clean", processedPrompt);
                }

                Thread.sleep(100);
            }
        } catch (InterruptedException err) {
            logger.error("Interrupted: " + err.getMessage());
        }
    }
}
