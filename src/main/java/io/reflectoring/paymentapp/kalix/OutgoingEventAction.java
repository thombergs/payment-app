package io.reflectoring.paymentapp.kalix;

import io.reflectoring.paymentapp.transfer.internal.outgoing.api.OutgoingEvents;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestAccountCreditEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestAccountDebitEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestFraudCheckEvent;
import kalix.javasdk.action.Action;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/outgoing")
public class OutgoingEventAction extends Action {

    private final OutgoingEvents outgoingEvents;

    public OutgoingEventAction(OutgoingEvents outgoingEvents) {
        this.outgoingEvents = outgoingEvents;
    }

    @PostMapping("/request-fraud-check")
    public Effect<String> requestFraudCheck(@RequestBody RequestFraudCheckEvent event) {
        outgoingEvents.requestFraudCheck(event);
        return effects().reply("sent event");
    }

    @PostMapping("/request-account-debit")
    public Effect<String> requestAccountDebit(@RequestBody RequestAccountDebitEvent event) {
        outgoingEvents.requestAccountDebit(event);
        return effects().reply("sent event");
    }

    @PostMapping("/request-account-credit")
    public Effect<String> requestAccountCredit(@RequestBody RequestAccountCreditEvent event) {
        outgoingEvents.requestAccountCredit(event);
        return effects().reply("sent event");
    }

}
