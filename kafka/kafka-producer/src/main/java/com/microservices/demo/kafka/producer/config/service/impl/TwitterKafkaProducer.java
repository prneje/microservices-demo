package com.microservices.demo.kafka.producer.config.service.impl;

import com.microservices.demo.kafka.avro.model.TwitterAvroModel;
import com.microservices.demo.kafka.producer.config.service.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;


import java.util.concurrent.CompletableFuture;

@Service
public class TwitterKafkaProducer implements KafkaProducer<Long, TwitterAvroModel> {

    private static final Logger LOG = LoggerFactory.getLogger(TwitterKafkaProducer.class);

    private KafkaTemplate<Long, TwitterAvroModel> kafkaTemplate;

    public TwitterKafkaProducer(KafkaTemplate<Long, TwitterAvroModel> template) {
        this.kafkaTemplate = template;
    }

    @Override
    public void send(String topicName, Long key, TwitterAvroModel message) {
        LOG.info("Sending message='{}' to topic='{}'", message, topicName);
        CompletableFuture<SendResult<Long, TwitterAvroModel>> kafkaResultFuture =
                kafkaTemplate.send(topicName, key, message);
        addCallback(topicName, message, kafkaResultFuture);
    }

    //TODO: @PreDestroy
    public void close() {
        if (kafkaTemplate != null) {
            LOG.info("Closing kafka producer!");
            kafkaTemplate.destroy();
        }
    }

    // TODO
    private void addCallback(String topicName, TwitterAvroModel message,
                             CompletableFuture<SendResult<Long, TwitterAvroModel>> kafkaResultFuture) {
//        kafkaResultFuture.addCallback(new CompletableFuture<>() {
//            @Override
//            public void onFailure(Throwable throwable) {
//                LOG.error("Error while sending message {} to topic {}", message.toString(), topicName, throwable);
//            }
//
//            @Override
//            public void onSuccess(SendResult<Long, TwitterAvroModel> result) {
//                    RecordMetadata metadata = result.getRecordMetadata();
//                    LOG.debug("Received new metadata. Topic: {}; Partition {}; Offset {}; Timestamp {}, at time {}",
//                            metadata.topic(),
//                            metadata.partition(),
//                            metadata.offset(),
//                            metadata.timestamp(),
//                            System.nanoTime());
//            }
//        });
    }
}