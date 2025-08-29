package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        String consumptionTopic = "saga.out";
        PromptRawConsumer consumer = new PromptRawConsumer(consumptionTopic);
        PromptCleanProducer producer = new PromptCleanProducer();

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    logger.info("Shutting down..");
                    consumer.close();
                    producer.close();
                })
            );

        logger.info("Starting pipe..");

        try {
            do {
                consumer.consumeAndProcess((key, value) -> {
                    producer.produce("saga.in", key, value);
                    // producer.produce("write", key, value);
                    // producer.produce("vec", key, value);
                });

                Thread.sleep(100);
            } while (true);
        } catch (InterruptedException err) {
            logger.error("Interrupted: " + err.getMessage());
        }
    }
}
