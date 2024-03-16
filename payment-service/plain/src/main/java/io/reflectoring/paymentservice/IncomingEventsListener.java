package io.reflectoring.paymentservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import io.cloudevents.core.v1.CloudEventBuilder;
import io.reflectoring.paymentservice.model.AccountCreditedEvent;
import io.reflectoring.paymentservice.model.AccountDebitedEvent;
import io.reflectoring.paymentservice.model.FraudCheckedEvent;
import io.reflectoring.paymentservice.model.RequestAccountCreditEvent;
import io.reflectoring.paymentservice.model.RequestAccountDebitEvent;
import io.reflectoring.paymentservice.model.RequestFraudCheckEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class IncomingEventsListener {

    private static final Logger logger = LoggerFactory.getLogger(IncomingEventsListener.class);

    private final KafkaTemplate<String, CloudEvent> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final Duration eventDelay;

    public IncomingEventsListener(
            KafkaTemplate<String, CloudEvent> kafkaTemplate,
            @Value("${app.event-delay}") Duration eventDelay) {
        this.kafkaTemplate = kafkaTemplate;
        this.eventDelay = eventDelay;
        this.objectMapper = new ObjectMapper();
    }

    @KafkaListener(id = "requestFraudCheckEventListener", topics = Topics.REQUEST_FRAUD_CHECK_EVENT)
    public void onRequestFraudCheckEvent(CloudEvent event) throws IOException {
        RequestFraudCheckEvent incomingEvent = objectMapper.readValue(new ByteArrayInputStream(event.getData().toBytes()), RequestFraudCheckEvent.class);
        logger.info("received event {}", incomingEvent);
        delay();

        FraudCheckedEvent outgoingEvent = new FraudCheckedEvent(
                incomingEvent.transferId(),
                FraudCheckedEvent.FraudCheckResult.SUCCESS);


        CloudEvent cloudEvent = new CloudEventBuilder()
                .withId(UUID.randomUUID().toString())
                .withSource(URI.create("fraud-check-service"))
                .withType("FraudCheckedEvent")
                .withData(objectMapper.writeValueAsBytes(outgoingEvent))
                .withDataContentType("application/json")
                .build();

        kafkaTemplate.send(Topics.FRAUD_CHECKED, cloudEvent)
                .thenRun(() -> logger.info("published event {}", outgoingEvent));
    }

    @KafkaListener(id = "requestAccountDebitEventListener", topics = Topics.REQUEST_ACCOUNT_DEBIT)
    public void onRequestAccountDebitEvent(CloudEvent event) throws IOException {
        RequestAccountDebitEvent incomingEvent = objectMapper.readValue(new ByteArrayInputStream(event.getData().toBytes()), RequestAccountDebitEvent.class);
        logger.info("received event {}", incomingEvent);
        delay();

        AccountDebitedEvent outgoingEvent = new AccountDebitedEvent(
                incomingEvent.transferId(),
                incomingEvent.sourceAccountId(),
                incomingEvent.amount(),
                AccountDebitedEvent.DebitResult.SUCCESS);

        CloudEvent cloudEvent = new CloudEventBuilder()
                .withId(UUID.randomUUID().toString())
                .withSource(URI.create("fraud-check-service"))
                .withType("AccountDebitedEvent")
                .withData(objectMapper.writeValueAsBytes(outgoingEvent))
                .withDataContentType("application/json")
                .build();

        kafkaTemplate.send(Topics.ACCOUNT_DEBITED, cloudEvent)
                .thenRun(() -> logger.info("published event {}", outgoingEvent));
    }

    @KafkaListener(id = "requestAccountCreditEvent", topics = Topics.REQUEST_ACCOUNT_CREDIT)
    public void onRequestAccountCreditEvent(CloudEvent event) throws IOException {
        RequestAccountCreditEvent incomingEvent = objectMapper.readValue(new ByteArrayInputStream(event.getData().toBytes()), RequestAccountCreditEvent.class);
        logger.info("received event {}", incomingEvent);
        delay();

        AccountCreditedEvent outgoingEvent = new AccountCreditedEvent(
                incomingEvent.transferId(),
                incomingEvent.targetAccountId(),
                incomingEvent.amount(),
                AccountCreditedEvent.CreditResult.SUCCESS);

        CloudEvent cloudEvent = new CloudEventBuilder()
                .withId(UUID.randomUUID().toString())
                .withSource(URI.create("fraud-check-service"))
                .withType("AccountCreditedEvent")
                .withData(objectMapper.writeValueAsBytes(outgoingEvent))
                .withDataContentType("application/json")
                .build();

        kafkaTemplate.send(Topics.ACCOUNT_CREDITED, cloudEvent)
                .thenRun(() -> logger.info("published event {}", outgoingEvent));
    }

    private void delay() {
        try {
            logger.info("delaying for {}", eventDelay);
            TimeUnit.SECONDS.sleep(eventDelay.getSeconds());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
