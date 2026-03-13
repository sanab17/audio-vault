package com.audiovault.search.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

// import com.audiovault.search.dto.RecordingEvent;
import com.audiovault.dto.RecordingEvent;
import com.audiovault.search.service.SearchService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class RecordingEventConsumer {
    
    private final SearchService searchService;

    @KafkaListener(
            topics = "${kafka.topics.recording-created}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleRecordingCreated(RecordingEvent event) {
        log.info("Received CREATED event for recording ID: {}, fileName: {}", event.getId(), event.getFileName());
    
        try {
            searchService.indexRecording(event);
            log.info("Successfully indexed recording ID: {}", event.getId());
        } catch (Exception e) {
            log.error("Error indexing recording ID: {}", event.getId(), e);
        }
    }

    @KafkaListener(
            topics = "${kafka.topics.recording-updated}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleRecordingUpdated(RecordingEvent event) {
        log.info("Received UPDATED event for recording ID: {}", event.getId());
        
        try {
            searchService.updateRecording(event);
            log.info("Successfully updated recording ID: {}", event.getId());
        } catch (Exception e) {
            log.error("Error updating recording ID: {}", event.getId(), e);
        }
    }

    @KafkaListener(
            topics = "${kafka.topics.recording-deleted}",
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleRecordingDeleted(RecordingEvent event) {
        log.info("Received DELETED event for recording ID: {}", event.getId());
        
        try {
            searchService.deleteRecording(event.getId());
            log.info("Successfully deleted recording ID: {}", event.getId());
        } catch (Exception e) {
            log.error("Error deleting recording ID: {}", event.getId(), e);
        }
    }
}
