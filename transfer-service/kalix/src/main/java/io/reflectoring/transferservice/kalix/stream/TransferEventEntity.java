package io.reflectoring.transferservice.kalix.stream;

import io.reflectoring.transferservice.transfer.api.TransferId;
import io.reflectoring.transferservice.transfer.internal.outgoing.api.OutgoingEvent;
import io.reflectoring.transferservice.transfer.internal.outgoing.api.RequestAccountCreditEvent;
import io.reflectoring.transferservice.transfer.internal.outgoing.api.RequestAccountDebitEvent;
import io.reflectoring.transferservice.transfer.internal.outgoing.api.RequestFraudCheckEvent;
import kalix.javasdk.annotations.EventHandler;
import kalix.javasdk.annotations.Id;
import kalix.javasdk.annotations.TypeId;
import kalix.javasdk.eventsourcedentity.EventSourcedEntity;
import org.springframework.web.bind.annotation.RequestMapping;

@Id("id")
@TypeId("transfer-event")
@RequestMapping("/transfer-entity/{id}")
public class TransferEventEntity extends EventSourcedEntity<TransferId, OutgoingEvent> {

    @EventHandler
    public TransferId requestFraudCheck(RequestFraudCheckEvent event) {
        return currentState();
    }

    @EventHandler
    public TransferId requestAccoundDebit(RequestAccountDebitEvent event) {
        return currentState();
    }

    @EventHandler
    public TransferId requestAccountCredit(RequestAccountCreditEvent event) {
        return currentState();
    }
}
