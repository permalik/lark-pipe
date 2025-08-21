package org.example;

import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        List<String> consumptionTopics = Arrays.asList(
            "prompt.raw",
            "prompt.clean",
            "inference.result",
            "feedback.stub",
            "feedback.submission"
        );
        PromptRawConsumer consumer = new PromptRawConsumer(consumptionTopics);
        PromptCleanProducer producer = new PromptCleanProducer();

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
                consumer.consumeAndProcess((topic, key, value) -> {
                    if ("prompt.raw".equals(topic)) {
                        producer.produce("bus.prompt.clean", key, value);
                    } else if ("prompt.clean".equals(topic)) {
                        producer.produce("bus.inference.request", key, value);
                    }
                });

                Thread.sleep(100);
            }
        } catch (InterruptedException err) {
            logger.error("Interrupted: " + err.getMessage());
        }
    }
}
