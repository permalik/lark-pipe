package org.example;

import java.util.Properties;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PromptCleanProducer {

    private static final Logger logger = LoggerFactory.getLogger(
        PromptCleanProducer.class
    );
    private final KafkaProducer<String, String> producer;

    public PromptCleanProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class.getName()
        );
        props.put(
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class.getName()
        );

        producer = new KafkaProducer<>(props);
    }

    public void produce(String topic, String key, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(
            topic,
            key,
            value
        );
        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                logger.info(
                    "Produced: topic={}, partition={}",
                    metadata.topic(),
                    metadata.partition()
                );
            } else {
                logger.error("Failed produce:", exception);
            }
        });
    }

    public void close() {
        producer.close();
    }
}
