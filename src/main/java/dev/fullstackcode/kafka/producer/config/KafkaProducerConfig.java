package dev.fullstackcode.kafka.producer.config;

import dev.fullstackcode.kafka.producer.dto.Employee;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServerUrl;

    @Value("${cloudkarafka-username}")
    private String cloudKarafkaUserName;

    @Value("${cloudkarafka-password}")
    private String cloudKarafkaPassword;

    private final String jaasTemplate = "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";";

    @Bean
    public Map<String, Object> producerConfig()  {

        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServerUrl);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.TYPE_MAPPINGS, "Employee:dev.fullstackcode.kafka.producer.dto.Employee");
        props.put("sasl.mechanism", "SCRAM-SHA-256");
        props.put("sasl.jaas.config", String.format(jaasTemplate, cloudKarafkaUserName, cloudKarafkaPassword));
        props.put("security.protocol", "SASL_SSL");
        props.put("enable.idempotence" , "false");

        // additional properties you might need to configure depending on the cluster setup
//        props.put("ssl.truststore.location", "<path to jks certificate>");
//        props.put("ssl.truststore.password", "<password>");
//        props.put("ssl.endpoint.identification.algorithm", "");

        return props;
    }

    @Bean
    public ProducerFactory<Integer, Employee> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }


    @Bean
    public KafkaTemplate<Integer, Employee> kafkaTemplate(ProducerFactory<Integer, Employee> producerFactory) {
        return new KafkaTemplate<Integer, Employee>(producerFactory);
    }

}
