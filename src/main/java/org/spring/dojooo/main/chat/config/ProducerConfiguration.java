package org.spring.dojooo.main.chat.config;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.spring.dojooo.main.chat.dto.SendMessageForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Configuration
public class ProducerConfiguration {

    @Value("${spring.kafka.producer.bootstrap-servers}")
    private String kafkaBroker;

    @Autowired
    private KafkaTemplate<String, SendMessageForm> kafkaTemplate;

    private Map<String, Object> producerConfigurations() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBroker); //Kafka 서버 주소
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class); //DTO -> JSON
        props.put(ProducerConfig.RETRIES_CONFIG, 3); //실패시 재시도 횟수
        return props;
    }

    @Bean
    public KafkaTemplate<String, SendMessageForm> kafkaTemplate(){
        return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(producerConfigurations()));
    }

    public void send(String topic, SendMessageForm message) {
        message = message.setSendDateTocurrentTime(); //현재 시간을 기준으로 새 객체 반환
        try {
            kafkaTemplate.send("message", message).get(3, TimeUnit.SECONDS);
            kafkaTemplate.send(topic, message).get(3, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            System.out.println(e);
        }
    }


}
