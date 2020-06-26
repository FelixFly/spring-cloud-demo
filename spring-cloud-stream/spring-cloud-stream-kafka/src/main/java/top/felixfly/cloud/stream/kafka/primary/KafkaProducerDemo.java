package top.felixfly.cloud.stream.kafka.primary;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * kafka 生产者演示
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/16
 */
public class KafkaProducerDemo {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.serializer", StringSerializer.class.getName());
        properties.put("value.serializer", StringSerializer.class.getName());
        KafkaProducer<String, String> producer = new KafkaProducer<>(properties);
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("test","HelloWorld");
        Future<RecordMetadata> send = producer.send(producerRecord);
        send.get();
    }
}
