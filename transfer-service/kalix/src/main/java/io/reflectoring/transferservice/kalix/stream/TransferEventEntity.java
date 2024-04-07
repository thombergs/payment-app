package io.reflectoring.transferservice.kalix.stream;

import io.reflectoring.transferservice.transfer.api.TransferId;
import io.reflectoring.transferservice.transfer.internal.outgoing.api.OutgoingEvent;
import io.reflectoring.transferservice.transfer.internal.outgoing.api.RequestAccountCreditEvent;
import io.reflectoring.transferservice.transfer.internal.outgoing.api.RequestAccountDebitEvent;
import io.reflectoring.transferservice.transfer.internal.outgoing.api.RequestFraudCheckEvent;
import kalix.javasdk.EntityContext;
import kalix.javasdk.annotations.EventHandler;
import kalix.javasdk.annotations.Id;
import kalix.javasdk.annotations.TypeId;
import kalix.javasdk.eventsourcedentity.EventSourcedEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This entity is using the concept of an event-sourced entity to create a stream of events
 * that other Kalix components in this Kalix service can subscribe to.
 * The entity itself doesn't have any state.
 */
@Id("id")
@TypeId("transfer-event")
@RequestMapping("/transfer-entity/{id}")
public class TransferEventEntity extends EventSourcedEntity<TransferId, OutgoingEvent> {

    private final TransferId id;

    public TransferEventEntity(EntityContext context) {
        this.id = new TransferId(context.entityId());
    }

    @PostMapping("/request-fraud-check")
    public Effect<String> requestFraudCheck(@RequestBody RequestFraudCheckEvent event) {
        // persist a RequestFraudCheckEvent to the journal
        return effects()
                .emitEvent(event)
                .thenReply(newState -> "OK");
    }

    @PostMapping("/request-account-debit")
    public Effect<String> requestAccountDebit(@RequestBody RequestAccountDebitEvent event) {
        // persist a RequestAccountDebitEvent to the journal
        return effects()
                .emitEvent(event)
                .thenReply(newState -> "OK");
    }

    @PostMapping("/request-account-credit")
    public Effect<String> requestAccountCredit(@RequestBody RequestAccountCreditEvent event) {
        // persist a RequestAccountCreditEvent to the journal
        return effects()
                .emitEvent(event)
                .thenReply(newState -> "OK");
    }

    @Override
    public TransferId emptyState() {
        return this.id;
    }

    @EventHandler
    public TransferId onRequestFraudCheck(RequestFraudCheckEvent event) {
        // we ignore the event here, because we just use this entity to create a stream of events
        return currentState();
    }

    @EventHandler
    public TransferId onRequestAccountDebit(RequestAccountDebitEvent event) {
        // we ignore the event here, because we just use this entity to create a stream of events
        return currentState();
    }

    @EventHandler
    public TransferId onRequestAccountCredit(RequestAccountCreditEvent event) {
        // we ignore the event here, because we just use this entity to create a stream of events
        return currentState();
    }
}
