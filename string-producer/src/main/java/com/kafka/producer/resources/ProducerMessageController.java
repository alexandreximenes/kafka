package com.kafka.producer.resources;

import com.kafka.producer.services.ProducerMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/v1/producer")
public class ProducerMessageController {

    private final ProducerMessageService producerMessageService;

    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody String message){
        producerMessageService.sendMessage(message);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
