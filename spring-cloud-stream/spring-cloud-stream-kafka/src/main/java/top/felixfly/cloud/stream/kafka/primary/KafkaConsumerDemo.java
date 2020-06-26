package top.felixfly.cloud.stream.kafka.primary;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

/**
 * kafka 消费端演示
 *
 * @author FelixFly <chenglinxu@yeah.net>
 * @date 2020/2/16
 */
public class KafkaConsumerDemo {

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "localhost:9092");
        properties.put("key.deserializer", StringDeserializer.class.getName());
        properties.put("value.deserializer", StringDeserializer.class.getName());
        properties.put("group.id", "custom-group");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singletonList("test"));
        // 拉的模式
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMinutes(1));
        records.forEach(record ->{
            System.out.println(record.headers());
            System.out.println(record.value());
        });


    }
}
