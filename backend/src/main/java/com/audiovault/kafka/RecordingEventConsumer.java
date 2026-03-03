package com.audiovault.kafka;

import com.audiovault.dto.RecordingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RecordingEventConsumer {
    
    @KafkaListener(topics = "${kafka.topic.recording-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void onRecordingCreated(RecordingEvent event) {
        log.info("Consumed recording.created event: id={}, file={}, duration={}s", event.getRecordingId(), event.getFileName(), event.getDuration());
    }

    @KafkaListener(topics = "${kafka.topic.recording-deleted}", groupId = "${spring.kafka.consumer.group-id}")
    public void onRecordingDeleted(RecordingEvent event) {
        log.info("Consumed recording.deleted event: id={}, file={}", event.getRecordingId(), event.getFileName());
    }
}
