package org.spring.dojooo.main.chat.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.spring.dojooo.main.chat.dto.MessageRoom;
import org.spring.dojooo.main.chat.dto.SendMessageForm;

import org.spring.dojooo.main.chat.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableKafka
@Configuration
public class ConsumerConfiguration {


    @Autowired
    private KafkaService kafkaService;

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String kafkaBroker;

    // 공통 JSON 역직렬화기
    private JsonDeserializer<SendMessageForm> getAllTrustJsonDeserializer() {
        JsonDeserializer<SendMessageForm> deserializer = new JsonDeserializer<>(SendMessageForm.class);
        deserializer.addTrustedPackages("*");
        return deserializer;
    }

    private Map<String, Object> consumerConfigurations() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBroker);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "messageListener");
        return props;
    }

    private ConsumerFactory<String, SendMessageForm> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                consumerConfigurations(),
                new StringDeserializer(),
                getAllTrustJsonDeserializer()
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SendMessageForm> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SendMessageForm> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    // 동적으로 토픽 구독 설정
    public void messageConsumerFactory(List<String> topics) {
        for (String topic : topics) {
            messageConsumerFactory(topic);
        }
    }

    public void messageConsumerFactory(String topic) {
        ContainerProperties containerProps = new ContainerProperties(topic);
        containerProps.setMessageListener((MessageListener<String, SendMessageForm>) record -> {
            kafkaService.broadcastMessage("/sub/" + topic, record.value());
        });

        ConcurrentMessageListenerContainer<String, SendMessageForm> container =
                new ConcurrentMessageListenerContainer<>(consumerFactory(), containerProps);
        container.setBeanName("container-" + topic);
        container.start();
    }

    // 메시지 룸용 ConsumerFactory (MessageRoom 타입 처리)
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MessageRoom> messageRoomKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, MessageRoom> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(messageRoomConsumerFactory());
        return factory;
    }

    private ConsumerFactory<String, MessageRoom> messageRoomConsumerFactory() {
        JsonDeserializer<MessageRoom> jsonDeserializer = new JsonDeserializer<>(MessageRoom.class);
        jsonDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(
                consumerConfigurations(),
                new StringDeserializer(),
                jsonDeserializer
        );
    }
}
