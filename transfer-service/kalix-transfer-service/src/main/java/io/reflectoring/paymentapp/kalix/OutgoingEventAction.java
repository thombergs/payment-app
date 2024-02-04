package io.reflectoring.paymentapp.kalix;

import io.reflectoring.paymentapp.transfer.internal.outgoing.api.OutgoingEvents;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestAccountCreditEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestAccountDebitEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestFraudCheckEvent;
import kalix.javasdk.action.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/outgoing")
public class OutgoingEventAction extends Action {

    private static final Logger logger = LoggerFactory.getLogger(OutgoingEventAction.class);

    private final OutgoingEvents outgoingEvents;

    public OutgoingEventAction(OutgoingEvents outgoingEvents) {
        this.outgoingEvents = outgoingEvents;
    }

    @PostMapping("/request-fraud-check")
    public Effect<String> requestFraudCheck(@RequestBody RequestFraudCheckEvent event) {
        logger.info("outgoing event: {}", event);
        outgoingEvents.requestFraudCheck(event);
        return effects().reply("sent event");
    }

    @PostMapping("/request-account-debit")
    public Effect<String> requestAccountDebit(@RequestBody RequestAccountDebitEvent event) {
        logger.info("outgoing event: {}", event);
        outgoingEvents.requestAccountDebit(event);
        return effects().reply("sent event");
    }

    @PostMapping("/request-account-credit")
    public Effect<String> requestAccountCredit(@RequestBody RequestAccountCreditEvent event) {
        logger.info("outgoing event: {}", event);
        outgoingEvents.requestAccountCredit(event);
        return effects().reply("sent event");
    }

}
