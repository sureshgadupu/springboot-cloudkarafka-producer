package dev.fullstackcode.kafka.producer.service;


import dev.fullstackcode.kafka.producer.dto.Employee;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
public class EmployeeService {

    @Autowired
    private KafkaTemplate<Integer, Employee> kafkaTemplate;

    @Value("${cloudkarafka.topic}")
    private String topic;

    public void publishEmployee(Employee employee) {

            kafkaTemplate.send(topic,employee.getId(),employee).addCallback(
                        result ->System.out.println("Message published successfully to topic: \n"+ result.getProducerRecord()),
                        ex -> System.out.println("Failed to send message"+ ex)
                );
    }


}

