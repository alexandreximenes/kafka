package com.kafka.consumer.listeners;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConsumerListener {

//    @KafkaListener(topics = "str-topic", groupId = "group-1", containerFactory = "containerFactory")
//    @KafkaListener(topicPartitions = {
//            @TopicPartition(topic = "str-topic", partitions = {"0"})
//    }, groupId = "group-1", containerFactory = "containerFactory")
    @KafkaListenerCustom(groupId = "group-1")
    public void listener1(String message){
        log.info("LISTENER 1 ::: Mensagem recebida: {}", message);
        throw new IllegalArgumentException("Teste de exception");
    }

    @KafkaListenerCustom(groupId = "group-1")
    public void listener2(String message){
        log.info("LISTENER 2 ::: Mensagem recebida: {}", message);
    }


    @KafkaListenerCustom(groupId = "group-2", topics = "str-topic", containerFactory = "validationMessageContainerFactory")
    public void listener3(String message){
        log.info("LOG ::: Mensagem recebida: {}", message);
    }
}
