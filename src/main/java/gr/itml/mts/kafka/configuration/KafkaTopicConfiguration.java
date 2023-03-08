package gr.itml.mts.kafka.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
@ConditionalOnProperty(value = "mts.mode", havingValue = "KAFKA", matchIfMissing = false)
public class KafkaTopicConfiguration {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.uploadsTopicName}")
    private String uploadsTopicName;

    @Value(value = "${kafka.downloadsTopicName}")
    private String downloadsTopicName;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic uploadTopic() {
        return new NewTopic(uploadsTopicName, 1, (short) 1);
    }

    @Bean
    public NewTopic downloadTopic() {
        return new NewTopic(downloadsTopicName, 1, (short) 1);
    }
}