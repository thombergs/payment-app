package io.reflectoring.transferservice.transfer.internal.outgoing.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.v1.CloudEventBuilder;
import io.reflectoring.transferservice.transfer.internal.outgoing.api.OutgoingEvents;
import io.reflectoring.transferservice.transfer.internal.outgoing.api.RequestAccountCreditEvent;
import io.reflectoring.transferservice.transfer.internal.outgoing.api.RequestAccountDebitEvent;
import io.reflectoring.transferservice.transfer.internal.outgoing.api.RequestFraudCheckEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

@Component
public class KafkaBackedOutgoingEvents implements OutgoingEvents {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, CloudEvent> kafkaTemplate;

    public KafkaBackedOutgoingEvents(
            ObjectMapper objectMapper,
            KafkaTemplate<String, CloudEvent> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void requestFraudCheck(RequestFraudCheckEvent event) {
        try {
            CloudEvent cloudEvent = new CloudEventBuilder()
                    .withId(UUID.randomUUID().toString())
                    .withSource(URI.create("plain-transfer-service"))
                    .withType("RequestFraudCheckEvent")
                    .withData(objectMapper.writeValueAsBytes(event))
                    .withDataContentType("application/json")
                    .build();

            kafkaTemplate.send(Topics.REQUEST_FRAUD_CHECK_EVENT, cloudEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void requestAccountDebit(RequestAccountDebitEvent event) {
        try {
            CloudEvent cloudEvent = new CloudEventBuilder()
                    .withId(UUID.randomUUID().toString())
                    .withSource(URI.create("plain-transfer-service"))
                    .withType("RequestAccountDebitEvent")
                    .withData(objectMapper.writeValueAsBytes(event))
                    .withDataContentType("application/json")
                    .build();

            kafkaTemplate.send(Topics.REQUEST_ACCOUNT_DEBIT, cloudEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void requestAccountCredit(RequestAccountCreditEvent event) {
        try {
            CloudEvent cloudEvent = new CloudEventBuilder()
                    .withId(UUID.randomUUID().toString())
                    .withSource(URI.create("plain-transfer-service"))
                    .withType("RequestAccountCreditEvent")
                    .withData(objectMapper.writeValueAsBytes(event))
                    .withDataContentType("application/json")
                    .build();

            kafkaTemplate.send(Topics.REQUEST_ACCOUNT_CREDIT, cloudEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
