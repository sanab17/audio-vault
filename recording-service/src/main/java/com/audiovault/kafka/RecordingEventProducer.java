package com.audiovault.kafka;

import com.audiovault.dto.RecordingEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecordingEventProducer {
    
    private final KafkaTemplate<String, RecordingEvent> kafkaTemplate;

    @Value("${kafka.topic.recording-created}")
    private String recordingCreatedTopic;

    @Value("${kafka.topic.recording-deleted}")
    private String recordingDeletedTopic;

    public void publishRecordingCreated(RecordingEvent event) {
        log.info("Publishing recording.created event for ID: {}", event.getRecordingId());
        kafkaTemplate.send(recordingCreatedTopic, event.getRecordingId().toString(), event);
    }

    public void publishRecordingDeleted(RecordingEvent event) {
        log.info("Publishing recording.deleted event for ID: {}", event.getRecordingId());
        kafkaTemplate.send(recordingDeletedTopic, event.getRecordingId().toString(), event);
    }
}
