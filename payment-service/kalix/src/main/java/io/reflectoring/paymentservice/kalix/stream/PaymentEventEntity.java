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
import org.springframework.web.bind.annotation.RequestMapping;

@Id("id")
@TypeId("payment-event")
@RequestMapping("/payment-entity/{id}")
public class PaymentEventEntity extends EventSourcedEntity<TransferId, OutgoingEvent> {

    @EventHandler
    public TransferId accountCredited(AccountCreditedEvent event) {
        return currentState();
    }

    @EventHandler
    public TransferId fraudChecked(FraudCheckedEvent event) {
        return currentState();
    }

    @EventHandler
    public TransferId accountDebited(AccountDebitedEvent event) {
        return currentState();
    }

}