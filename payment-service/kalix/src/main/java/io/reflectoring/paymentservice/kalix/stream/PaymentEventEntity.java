package io.reflectoring.paymentservice.kalix.stream;

import io.reflectoring.paymentservice.transfer.api.TransferId;
import io.reflectoring.paymentservice.transfer.internal.outgoing.api.AccountCreditedEvent;
import io.reflectoring.paymentservice.transfer.internal.outgoing.api.AccountDebitedEvent;
import io.reflectoring.paymentservice.transfer.internal.outgoing.api.FraudCheckedEvent;
import io.reflectoring.paymentservice.transfer.internal.outgoing.api.OutgoingEvent;
import kalix.javasdk.annotations.EventHandler;
import kalix.javasdk.annotations.Id;
import kalix.javasdk.annotations.TypeId;
import kalix.javasdk.eventsourcedentity.EventSourcedEntity;
import kalix.javasdk.eventsourcedentity.EventSourcedEntityContext;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * This entity is using the concept of an event-sourced entity to create a stream of events.
 * It doesn't have any state.
 */
@Id("id")
@TypeId("payment-event")
@RequestMapping("/payment-entity/{id}")
public class PaymentEventEntity extends EventSourcedEntity<TransferId, OutgoingEvent> {

    private final TransferId id;

    public PaymentEventEntity(EventSourcedEntityContext context) {
        this.id = new TransferId(context.entityId());
    }

    @Override
    public TransferId emptyState() {
        return this.id;
    }

    @PostMapping("/account-debited")
    public Effect<String> accountDebited(@RequestBody AccountDebitedEvent event) {
        // persist a AccountDebitedEvent to the journal
        return effects()
                .emitEvent(event)
                .thenReply(newState -> "OK");
    }

    @PostMapping("/account-credited")
    public Effect<String> accountCredited(@RequestBody AccountCreditedEvent event) {
        // persist a AccountCreditedEvent to the journal
        return effects()
                .emitEvent(event)
                .thenReply(newState -> "OK");
    }

    @PostMapping("/fraud-checked")
    public Effect<String> fraudChecked(@RequestBody FraudCheckedEvent event) {
        // persist a FraudCheckedEvent to the journal
        return effects()
                .emitEvent(event)
                .thenReply(newState -> "OK");
    }

    @EventHandler
    public TransferId onAccountCredited(AccountCreditedEvent event) {
        // we ignore the event here, because we just use this entity to create a stream of events
        return currentState();
    }

    @EventHandler
    public TransferId onFraudChecked(FraudCheckedEvent event) {
        // we ignore the event here, because we just use this entity to create a stream of events
        return currentState();
    }

    @EventHandler
    public TransferId onAccountDebited(AccountDebitedEvent event) {
        // we ignore the event here, because we just use this entity to create a stream of events
        return currentState();
    }

}