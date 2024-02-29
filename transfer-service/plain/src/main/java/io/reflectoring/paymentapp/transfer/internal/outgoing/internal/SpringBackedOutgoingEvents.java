package io.reflectoring.paymentapp.transfer.internal.outgoing.internal;

import io.reflectoring.paymentapp.transfer.internal.outgoing.api.OutgoingEvents;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestAccountCreditEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestAccountDebitEvent;
import io.reflectoring.paymentapp.transfer.internal.outgoing.api.RequestFraudCheckEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class SpringBackedOutgoingEvents implements OutgoingEvents {

    private final ApplicationEventPublisher eventPublisher;

    public SpringBackedOutgoingEvents(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void requestFraudCheck(RequestFraudCheckEvent event) {
        eventPublisher.publishEvent(event);
    }

    @Override
    public void requestAccountDebit(RequestAccountDebitEvent event) {
        eventPublisher.publishEvent(event);
    }

    @Override
    public void requestAccountCredit(RequestAccountCreditEvent event) {
        eventPublisher.publishEvent(event);
    }
}
