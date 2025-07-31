package org.example;

import java.util.Properties;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

public class PromptCleanProducer {
    private final KafkaProducer<String, String> producer;
    private final String topic;

    public PromptCleanProducer(String topic) {
        this.topic = topic;
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        producer = new KafkaProducer<>(props);
    }

    public void produce(String key, String value) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topic, key, value);
        producer.send(record, (metadata, exception) -> {
            if (exception == null) {
                System.out.println("Sent to " + metadata.topic() + ", partition " + metadata.partition());
            } else {
                exception.printStackTrace();
            }
        });
    }

    public void close() {
        producer.close();
    }
}
