package io.reflectoring.transferservice.transfer.internal.incoming.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.cloudevents.CloudEvent;
import io.reflectoring.transferservice.transfer.api.TransferService;
import io.reflectoring.transferservice.transfer.internal.incoming.api.AccountCreditedEvent;
import io.reflectoring.transferservice.transfer.internal.incoming.api.AccountDebitedEvent;
import io.reflectoring.transferservice.transfer.internal.incoming.api.FraudCheckedEvent;
import io.reflectoring.transferservice.transfer.internal.outgoing.internal.Topics;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Component
public class KafkaBackedIncomingEvents {

    private final TransferService transferService;
    private final ObjectMapper objectMapper;

    public KafkaBackedIncomingEvents(TransferService transferService, ObjectMapper objectMapper) {
        this.transferService = transferService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(id = "onFraudCheckedEventListener", topics = Topics.FRAUD_CHECKED)
    public void onFraudChecked(CloudEvent event) throws IOException {
        FraudCheckedEvent incomingEvent = objectMapper.readValue(new ByteArrayInputStream(event.getData().toBytes()), FraudCheckedEvent.class);
        transferService.onFraudChecked(incomingEvent);
    }

    @KafkaListener(id = "onAccountCreditedEvent", topics = Topics.ACCOUNT_CREDITED)
    public void onAccountCredited(CloudEvent event) throws IOException {
        AccountCreditedEvent incomingEvent = objectMapper.readValue(new ByteArrayInputStream(event.getData().toBytes()), AccountCreditedEvent.class);
        transferService.onTargetAccountCredited(incomingEvent);
    }

    @KafkaListener(id = "onAccountDebitedEvent", topics = Topics.ACCOUNT_DEBITED)
    public void onAccountDebited(CloudEvent event) throws Exception {
        AccountDebitedEvent incomingEvent = objectMapper.readValue(new ByteArrayInputStream(event.getData().toBytes()), AccountDebitedEvent.class);
        transferService.onSourceAccountDebited(incomingEvent);
    }
}
