package io.reflectoring.plainexternalservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.v1.CloudEventBuilder;
import io.reflectoring.plainexternalservice.model.FraudCheckedEvent;
import io.reflectoring.plainexternalservice.model.RequestFraudCheckEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@Component
public class IncomingEventsListener {

    private static final Logger logger = LoggerFactory.getLogger(IncomingEventsListener.class);

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public IncomingEventsListener(
            KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    @KafkaListener(id = "requestFraudCheckEventListener", topics = Topics.REQUEST_FRAUD_CHECK_EVENT)
    public void onRequestFraudCheckEvent(String eventString) throws IOException {
        RequestFraudCheckEvent incomingEvent = objectMapper.readValue(eventString, RequestFraudCheckEvent.class);
        logger.info("received event {}", incomingEvent);

        FraudCheckedEvent outgoingEvent = new FraudCheckedEvent(
                incomingEvent.transferId(),
                FraudCheckedEvent.FraudCheckResult.SUCCESS);


//        CloudEvent cloudEvent = new CloudEventBuilder()
//                .withId(UUID.randomUUID().toString())
//                .withSource(URI.create("fraud-check-service"))
//                .withType(FraudCheckedEvent.class.getSimpleName())
//                .withData(objectMapper.writeValueAsBytes(outgoingEvent))
//                .withDataContentType("application/octet-stream")
//                .build();

        kafkaTemplate.send(Topics.FRAUD_CHECKED, objectMapper.writeValueAsBytes(outgoingEvent))
                .thenRun(() -> logger.info("published event {}", outgoingEvent));
    }

}
