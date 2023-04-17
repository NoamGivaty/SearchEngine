package com.SearchEngine.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.SearchEngine.crawler.Crawler;
import com.SearchEngine.model.CrawlerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Optional;

import static com.SearchEngine.kafka.Producer.APP_TOPIC;

@Component
public class Consumer {


    @Autowired
    Crawler crawler;

    @Autowired
    ObjectMapper om;

    @KafkaListener(topics = {APP_TOPIC})
    public void listen(ConsumerRecord<?, ?> record) throws IOException, InterruptedException {

        Optional<?> kafkaMessage = Optional.ofNullable(record.value());

        if (kafkaMessage.isPresent()) {

            Object message = kafkaMessage.get();
            CrawlerRecord rec = om.readValue(message.toString(), CrawlerRecord.class);
            crawler.crawlOneRecord(rec.getCrawlId(),rec);
        }
    }
}
