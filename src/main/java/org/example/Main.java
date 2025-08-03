package org.example;

public class Main {

    public static void main(String[] args) {
        PromptRawConsumer consumer = new PromptRawConsumer("prompt.raw");
        PromptCleanProducer producer = new PromptCleanProducer("prompt.clean");

        Runtime.getRuntime().addShutdownHook(
                new Thread(() -> {
                    System.out.println("Shutting down..");
                    consumer.close();
                    producer.close();
                })
            );

        System.out.printf("Starting Preprocess..");
        System.out.flush();

        try {
            while (true) {
                String processedPrompt = consumer.consumeAndProcess();
                if (processedPrompt != null) {
                    producer.produce("new_clean", processedPrompt);
                }

                Thread.sleep(100);
            }
        } catch (InterruptedException err) {
            System.err.println("Interrupted: " + err.getMessage());
        }
    }
}
